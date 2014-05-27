package no.bekk.cv.graphsearch.parser.domain;

import java.io.Serializable;

public class RetrieveParameters implements Serializable {
    private boolean retrieveParameters;
    private String relation;

    public RetrieveParameters(boolean retrieveParameters, String relation) {

        this.retrieveParameters = retrieveParameters;
        this.relation = relation;
    }

    public boolean isRetrieveParameters() {
        return retrieveParameters;
    }

    public String getRelation() {
        return relation;
    }
}
