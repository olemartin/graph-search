package no.bekk.cv.graphsearch.integration;

import no.bekk.cv.graphsearch.graph.nodes.Fag;
import no.bekk.cv.graphsearch.graph.nodes.Person;
import no.bekk.cv.graphsearch.graph.nodes.Prosjekt;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface PersonRepository extends GraphRepository<Person> {

    @Query("start fag=node:fag(\"*:*\") return distinct fag")
    public List<Fag> hentAlleFag();

    @Query("start prosjekt=node:prosjekt(\"*:*\") return distinct prosjekt")
    public List<Prosjekt> hentAlleKunder();
}
