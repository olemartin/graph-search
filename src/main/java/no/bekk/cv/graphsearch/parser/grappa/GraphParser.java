package no.bekk.cv.graphsearch.parser.grappa;

import no.bekk.cv.graphsearch.parser.QueryParser;
import no.bekk.cv.graphsearch.parser.domain.GraphSearchQuery;
import no.bekk.cv.graphsearch.parser.domain.query.Query;
import org.parboiled.Parboiled;
import org.parboiled.parserunners.ReportingParseRunner;
import org.parboiled.support.ParseTreeUtils;
import org.parboiled.support.ParsingResult;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;



public class GraphParser implements QueryParser {

    @Override
    @Transactional
    public String parseQuery(String queryString) {
        GraphGrammar parser = Parboiled.createParser(GraphGrammar.class);
        ReportingParseRunner<GraphSearchQuery> runner = new ReportingParseRunner<>(parser.expression());
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


}