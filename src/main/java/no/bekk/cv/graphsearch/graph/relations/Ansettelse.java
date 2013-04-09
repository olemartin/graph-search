package no.bekk.cv.graphsearch.graph.relations;


import no.bekk.cv.graphsearch.graph.nodes.Firma;
import no.bekk.cv.graphsearch.graph.nodes.Person;
import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.StartNode;

import java.util.Date;

@RelationshipEntity(type = "ANSATT")
public class Ansettelse {

    @GraphId
    Long id;

    @StartNode
    private Firma firma;
    @EndNode
    private Person ansatt;

    private Date fra;
    private Date to;


    private Ansettelse() {
    }
}
