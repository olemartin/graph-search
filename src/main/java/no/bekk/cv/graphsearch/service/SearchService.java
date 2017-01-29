package no.bekk.cv.graphsearch.service;

import no.bekk.cv.graphsearch.graph.nodes.SearchEntity;
import no.bekk.cv.graphsearch.integration.GraphSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.conversion.EndResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;

@Transactional
@Service
public class SearchService {

    @Autowired
    private GraphSearchRepository graphRepository;

    @Transactional
    public List<String> search(String cypher) {
        EndResult<SearchEntity> elements = graphRepository.query(cypher, new HashMap<>());

        return stream(elements.spliterator(), false).map(SearchEntity::getNavn).collect(toList());
    }
}
