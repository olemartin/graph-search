package no.bekk.cv.graphsearch.graph.nodes;

import no.bekk.cv.graphsearch.graph.relations.Ansettelse;
import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;
import org.springframework.data.neo4j.annotation.RelatedToVia;
import org.springframework.data.neo4j.support.index.IndexType;

import java.util.HashSet;
import java.util.Set;

@NodeEntity
public class Firma {

    @GraphId
    Long id;
    @Indexed(indexName = "firmaer", indexType = IndexType.FULLTEXT)
    private String navn;

    @RelatedToVia
    private Set<Ansettelse> ansatte = new HashSet<>();

    @RelatedTo(type = "PROSJEKT", direction = Direction.BOTH)
    private Set<Prosjekt> prosjekter = new HashSet<>();

    public Firma(String navn) {
        this.navn = navn;
    }

    private Firma() {
    }

    public void haddeProsjekt(Prosjekt... prosjekter) {
        for (Prosjekt prosjekt : prosjekter) {
            this.prosjekter.add(prosjekt);
            prosjekt.setFirma(this);
        }
    }
}
