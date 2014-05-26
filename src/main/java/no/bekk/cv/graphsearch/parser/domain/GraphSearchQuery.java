package no.bekk.cv.graphsearch.parser.domain;

import com.google.common.collect.ImmutableList;
import no.bekk.cv.graphsearch.parser.domain.query.Query;

import java.util.List;

public class GraphSearchQuery {
    private Query returnValue;
    private ImmutableList<Query> targets = ImmutableList.of();
    private Query middleTarget;
    private RetrieveParameters retrieveParameters;

    public GraphSearchQuery(Query returnValue) {
        this.returnValue = returnValue;
    }

    private GraphSearchQuery(Query returnValue, ImmutableList<Query> targets, Query middleTarget,
                             RetrieveParameters retrieveParameters) {

        this.returnValue = returnValue;
        this.targets = targets;
        this.middleTarget = middleTarget;
        this.retrieveParameters = retrieveParameters;
    }

    public GraphSearchQuery addTarget(Query target) {
        ImmutableList<Query> list = ImmutableList.<Query>builder().addAll(this.targets).add(target).build();
        return new GraphSearchQuery(this.returnValue, list, middleTarget, retrieveParameters);
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
        return new GraphSearchQuery(this.returnValue, this.targets, query, retrieveParameters);
    }


    public GraphSearchQuery setRetrieveParameters(boolean retrieveParameters, String know) {
        return new GraphSearchQuery(this.returnValue, this.targets, middleTarget, new RetrieveParameters(retrieveParameters, know));
    }

    public boolean isRetrieveParameters() {
        return retrieveParameters != null && retrieveParameters.isRetrieveParameters();
    }

    public GraphSearchQuery setRetrieveParameters(boolean retrieveParameters) {
        return setRetrieveParameters(retrieveParameters, null);
    }
}
