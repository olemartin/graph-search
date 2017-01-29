package no.bekk.cv.graphsearch.integration;


import no.bekk.cv.graphsearch.graph.nodes.SearchEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.conversion.EndResult;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:config/applicationContext.xml")
@Transactional
public class GraphRepositoryIntTest {

    @Autowired
    GraphSearchRepository searchRepository;

    @Autowired
    RepositoryPopulator repo;


    @Before
    public void initDatabase() {
        repo.populate();
    }

    @Test
    public void testCypher() {
        String q = "start prosjekt1=node:prosjekt(navn = \"NAV\") " +
                "match person -[:KAN]-> fag <-[:BRUKTE]- prosjekt1 " +
                "with count(fag) as knownTech, person, prosjekt1 " +
                "where length(()<-[:BRUKTE]-prosjekt1)=knownTech " +
                "return person";
        EndResult<SearchEntity> elements = searchRepository.query(q, new HashMap<>());
        for (SearchEntity element : elements) {
            System.out.println(element.getNavn());
        }

    }
}
