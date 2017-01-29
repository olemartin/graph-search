package no.bekk.cv.graphsearch.graph.nodes;

import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;
import org.springframework.data.neo4j.support.index.IndexType;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@NodeEntity
public class Prosjekt implements SearchEntity{
    @GraphId
    Long id;
    @Indexed(indexName = "prosjekt", indexType = IndexType.FULLTEXT)
    private String navn;

    @RelatedTo(type = "EID_AV")
    private Firma firma;

    @RelatedTo(type = "MEDARBEIDER")
    private Set<Person> prosjektmedarbeidere = new HashSet<>();

    @RelatedTo(type = "BRUKTE")
    private Set<Fag> teknologier = new HashSet<>();

    private Prosjekt() {
    }

    public Prosjekt(String navn) {
        this.navn = navn;
    }

    public void haddeKonsulent(Person person) {
        prosjektmedarbeidere.add(person);
    }

    public void setFirma(Firma firma) {
        this.firma = firma;
    }

    public Prosjekt brukte(Fag... teknologier) {
        Collections.addAll(this.teknologier, teknologier);
        return this;
    }

    public String getNavn() {
        return navn;
    }
}
