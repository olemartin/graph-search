package no.bekk.cv.graphsearch.parser;

import org.parboiled.common.ImmutableList;

import java.util.List;

public class GraphSearchQuery {
    private Query returnValue;
    private ImmutableList<Query> targets = ImmutableList.of();

    public GraphSearchQuery() {
    }

    public GraphSearchQuery(Query returnValue) {
        this.returnValue = returnValue;
    }

    private GraphSearchQuery(Query returnValue, ImmutableList<Query> targets) {
        this.returnValue = returnValue;
        this.targets = targets;
    }

    public GraphSearchQuery addTarget(Query target) {
        return new GraphSearchQuery(this.returnValue, ImmutableList.of(this.targets, target));
    }

    public List<Query> getTargets() {
        return targets;
    }

    public Query getReturnValue() {
        return returnValue;
    }
}
