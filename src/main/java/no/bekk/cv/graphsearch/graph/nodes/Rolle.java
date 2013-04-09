package no.bekk.cv.graphsearch.graph.nodes;

import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.support.index.IndexType;

public class Rolle {

    @GraphId
    Long id;

    @Indexed(indexName = "fag", indexType = IndexType.FULLTEXT)
    private String navn;

    public Rolle(String navn) {
        this.navn = navn;
    }

    private Rolle() {
    }
}
