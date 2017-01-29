package no.bekk.cv.graphsearch.graph.nodes;

import org.springframework.data.neo4j.annotation.NodeEntity;

@NodeEntity
public interface SearchEntity {
    String getNavn();
}
