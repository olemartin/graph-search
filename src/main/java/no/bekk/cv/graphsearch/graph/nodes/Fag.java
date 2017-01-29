package no.bekk.cv.graphsearch.graph.nodes;

import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.support.index.IndexType;

@NodeEntity
public class Fag implements SearchEntity {

    @GraphId
    Long id;
    @Indexed(indexName = "fag", indexType = IndexType.FULLTEXT)
    private String navn;

    public Fag(String navn) {
        this.navn = navn;
    }

    private Fag() {

    }

    public Long getId() {
        return id;
    }

    public String getNavn() {
        return navn;
    }
}
