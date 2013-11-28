package no.bekk.cv.graphsearch.parser;

import org.parboiled.common.ImmutableList;

import java.util.List;

public class GraphSearchQuery {
    private Query returnValue;
    private ImmutableList<Query> targets = ImmutableList.of();
    private Query middleTarget;

    public GraphSearchQuery() {
    }

    public GraphSearchQuery(Query returnValue) {
        this.returnValue = returnValue;
    }

    private GraphSearchQuery(Query returnValue, ImmutableList<Query> targets, Query middleTarget) {

        this.returnValue = returnValue;
        this.targets = targets;
        this.middleTarget = middleTarget;
    }

    public GraphSearchQuery addTarget(Query target) {
        return new GraphSearchQuery(this.returnValue, ImmutableList.of(this.targets, target), middleTarget);
    }

    public List<Query> getTargets() {
        return targets;
    }

    public Query getReturnValue() {
        return returnValue;
    }

    public Query getMiddleTarget() {
        return middleTarget;
    }

    public GraphSearchQuery addMiddleTarget(Query query) {
        return new GraphSearchQuery(this.returnValue, this.targets, query);
    }
}
