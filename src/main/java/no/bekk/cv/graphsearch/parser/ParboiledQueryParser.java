package no.bekk.cv.graphsearch.parser;

import no.bekk.cv.graphsearch.integration.PersonRepository;
import no.bekk.cv.graphsearch.query.QueryParser;
import org.parboiled.Parboiled;
import org.parboiled.parserunners.TracingParseRunner;
import org.parboiled.support.ParseTreeUtils;
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
        TracingParseRunner runner = new TracingParseRunner(parser.Expression());
        ParsingResult<Query> result = runner.run(query);
        if (!result.hasErrors()) {
            System.out.println(ParseTreeUtils.printNodeTree(result));
            StringBuilder start = new StringBuilder();
            StringBuilder match = new StringBuilder();
            List<Query> targets = getQueries(result);
            String searchFor = result.valueStack.pop().getName();
            createQuery(start, match, targets, searchFor);
            String retur = "return distinct entitet";
            return start.append(match).append(retur).toString();
        } else {
            throw new IllegalArgumentException("Ugyldig spørring");
        }
    }

    private void createQuery(StringBuilder start, StringBuilder match, List<Query> targets, String searchFor) {
        for (int i = 0; i < targets.size(); i++) {
            Query target = targets.get(i);
            if (start.length() == 0) {
                target.initQuery(start, match, searchFor);
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
