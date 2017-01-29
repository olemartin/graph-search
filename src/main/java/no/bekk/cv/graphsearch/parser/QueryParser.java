package no.bekk.cv.graphsearch.parser;

import no.bekk.cv.graphsearch.parser.domain.query.Query;

import java.util.List;

public interface QueryParser {
    String parseQuery(String query);

    default void createQuery(StringBuilder start, StringBuilder match, List<Query> targets, Query middleTarget,
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
