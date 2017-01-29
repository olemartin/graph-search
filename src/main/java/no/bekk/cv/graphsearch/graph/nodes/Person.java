package no.bekk.cv.graphsearch.graph.nodes;

import no.bekk.cv.graphsearch.graph.relations.Ansettelse;
import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;
import org.springframework.data.neo4j.annotation.RelatedToVia;
import org.springframework.data.neo4j.support.index.IndexType;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@NodeEntity
public class Person implements SearchEntity {

    @GraphId
    Long id;
    @Indexed(indexName = "person", indexType = IndexType.FULLTEXT)
    private String navn;
    private String rolle;

    @RelatedTo(type = "KAN")
    private Set<Fag> fag = new HashSet<>();

    @RelatedTo(type = "KONSULTERTE", direction = Direction.BOTH)
    private Set<Prosjekt> prosjekter = new HashSet<>();

    @RelatedToVia
    private Set<Ansettelse> ansettelser = new HashSet<>();


    private Person() {
    }

    public Person(String navn, String rolle) {
        this.navn = navn;
        this.rolle = rolle;
    }

    public String getRolle() {
        return rolle;
    }

    public Long getId() {
        return id;
    }

    public String getNavn() {
        return navn;
    }

    public Person jobbetI(Prosjekt... prosjekter) {
        for (Prosjekt prosjekt : prosjekter) {
            this.prosjekter.add(prosjekt);
            prosjekt.haddeKonsulent(this);
        }
        return this;
    }

    public Person kan(Fag... fag) {
        Collections.addAll(this.fag, fag);
        return this;
    }

    public Person og() {
        return this;
    }
}
