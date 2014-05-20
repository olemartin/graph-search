package no.bekk.cv.graphsearch.parser;

import no.bekk.cv.graphsearch.integration.PersonRepository;
import no.bekk.cv.graphsearch.query.QueryParser;
import org.parboiled.Parboiled;
import org.parboiled.parserunners.ReportingParseRunner;
import org.parboiled.support.ParseTreeUtils;
import org.parboiled.support.ParsingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class ParboiledQueryParser implements QueryParser {

    @Autowired
    public ParboiledQueryParser(PersonRepository repository) {
        GraphSearchParser.init(repository.hentAlleFag(), repository.hentAlleKunder());
    }

    @Override
    public String parseQuery(String queryString) {
        GraphSearchParser parser = Parboiled.createParser(GraphSearchParser.class);
        ReportingParseRunner<GraphSearchQuery> runner = new ReportingParseRunner<>(parser.Expression());
        ParsingResult<GraphSearchQuery> result = runner.run(queryString);
        System.out.println(ParseTreeUtils.printNodeTree(result));
        if (!result.hasErrors()) {

            GraphSearchQuery query = result.parseTreeRoot.getValue();
            List<Query> targets = query.getTargets();
            String searchFor = query.getReturnValue().getName();

            StringBuilder start = new StringBuilder();
            StringBuilder match = new StringBuilder();

            if (query.isRetrieveParameters()) {
                createQuery(start, match, targets, query.getMiddleTarget(), searchFor);
                String retur = "return distinct " + searchFor;
                return start.append(match).append(retur).toString();
            } else {
                createQuery(start, match, targets, query.getMiddleTarget(), searchFor);
                String retur = "return distinct " + searchFor;
                return start.append(match).append(retur).toString();
            }
        } else {
            throw new IllegalArgumentException("Ugyldig spørring, " + result.parseErrors);
        }
    }

    private void createQuery(StringBuilder start, StringBuilder match, List<Query> targets, Query middleTarget,
                             String searchFor) {
        for (int i = 0; i < targets.size(); i++) {
            Query target = targets.get(i);
            if (start.length() == 0) {
                target.initQuery(start, match, searchFor, middleTarget);
            } else {
                target.appendQuery(start, match, searchFor, middleTarget, i);
            }
        }
        if (middleTarget != null) {
            middleTarget.appendQuery(start, match, searchFor, middleTarget, -1);
        }
    }
}