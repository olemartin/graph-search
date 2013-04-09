package no.bekk.cv.graphsearch.integration;

import no.bekk.cv.graphsearch.graph.nodes.Fag;
import no.bekk.cv.graphsearch.graph.nodes.Person;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface PersonRepository extends GraphRepository<Person> {

    @Query("start person=node:person(navn = {navn}) match person -[:KAN]-> fag return fag")
    public Iterable<Fag> hentFagSomPersonenKan(@Param("navn") String navn);
}
