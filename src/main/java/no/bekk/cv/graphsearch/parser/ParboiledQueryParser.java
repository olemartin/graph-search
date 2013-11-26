package no.bekk.cv.graphsearch.parser;

import no.bekk.cv.graphsearch.integration.PersonRepository;
import no.bekk.cv.graphsearch.query.QueryParser;
import org.parboiled.Parboiled;
import org.parboiled.parserunners.ReportingParseRunner;
import org.parboiled.support.ParsingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;


@Component
public class ParboiledQueryParser implements QueryParser {

    @Autowired
    public ParboiledQueryParser(PersonRepository repository) {
        GraphSearchParser.init(repository.hentAlleFag(), repository.hentAlleKunder());
    }

    @Override
    public String parseQuery(String query) {
        GraphSearchParser parser = Parboiled.createParser(GraphSearchParser.class);
        ReportingParseRunner<Query> runner = new ReportingParseRunner<>(parser.Expression());
        ParsingResult<Query> result = runner.run(query);
        if (!result.hasErrors()) {
            List<Query> targets = getQueries(result);
            String searchFor = result.valueStack.pop().getName();
            Query realSearchFor = null;
            if (!result.valueStack.isEmpty()) {
                realSearchFor = result.valueStack.pop();
                targets.add(realSearchFor);
            }
            StringBuilder start = new StringBuilder();
            StringBuilder match = new StringBuilder();
            createQuery(start, match, targets, searchFor, realSearchFor != null ? realSearchFor.getName() : null);
            String retur = "return distinct " + (realSearchFor != null ? realSearchFor.getName() : searchFor);
            return start.append(match).append(retur).toString();
        } else {
            throw new IllegalArgumentException("Ugyldig spørring");
        }
    }

    private void createQuery(StringBuilder start, StringBuilder match, List<Query> targets, String searchFor, String realSearchFor) {
        for (int i = 0; i < targets.size(); i++) {
            Query target = targets.get(i);
            if (start.length() == 0) {
                target.initQuery(start, match, searchFor, realSearchFor);
            } else {
                target.appendQuery(start, match, searchFor, i);
            }
        }
    }

    private List<Query> getQueries(ParsingResult<Query> result) {
        List<Query> targets = new LinkedList<>();
        while (!result.valueStack.isEmpty()) {
            Query element = result.valueStack.peek();
            if (element instanceof Technology || element instanceof Customer) {
                targets.add(result.valueStack.pop());
            } else {
                break;
            }
        }
        return targets;
    }
}