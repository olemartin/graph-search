package no.bekk.cv.graphsearch.service;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import no.bekk.cv.graphsearch.graph.nodes.Person;
import no.bekk.cv.graphsearch.integration.GraphSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.conversion.EndResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Transactional
@Service
public class SearchService {


    @Autowired
    private GraphSearchRepository personRepository;

    @Transactional
    public List<String> search(String cypher) {
        EndResult<Person> personer =
                personRepository.query(cypher, new HashMap<String, Object>());

        Iterable<String> navn = Iterables.transform(personer, new Function<Person, String>() {
            public String apply(Person person) {
                return person.getNavn();
            }
        });
        return Lists.newArrayList(navn);
    }
}
