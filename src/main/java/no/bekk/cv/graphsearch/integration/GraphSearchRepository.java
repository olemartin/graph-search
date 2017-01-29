package no.bekk.cv.graphsearch.integration;

import no.bekk.cv.graphsearch.graph.nodes.Fag;
import no.bekk.cv.graphsearch.graph.nodes.Person;
import no.bekk.cv.graphsearch.graph.nodes.Prosjekt;
import no.bekk.cv.graphsearch.graph.nodes.SearchEntity;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface GraphSearchRepository extends GraphRepository<SearchEntity> {

    @Query("start fag=node:fag(\"*:*\") return distinct fag")
    public List<Fag> hentAlleFag();

    @Query("start prosjekt=node:prosjekt(\"*:*\") return distinct prosjekt")
    public List<Prosjekt> hentAlleKunder();

    @Query("start person=node:person(\"*:*\") return distinct person")
    List<Person> hentAlleKonsulenter();
}
