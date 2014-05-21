package no.bekk.cv.graphsearch.service;

import no.bekk.cv.graphsearch.graph.nodes.Person;
import no.bekk.cv.graphsearch.integration.GraphSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.conversion.EndResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Transactional
@Service
public class SearchService {

    @Autowired
    private GraphSearchRepository personRepository;

    @Transactional
    public List<String> search(String cypher) {
        EndResult<Person> personer = personRepository.query(cypher, new HashMap<>());

        return StreamSupport.stream(personer.spliterator(), false).<String>map(Person::getNavn).collect(Collectors.toList());
    }
}
