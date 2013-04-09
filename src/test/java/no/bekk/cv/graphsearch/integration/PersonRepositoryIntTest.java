package no.bekk.cv.graphsearch.integration;


import no.bekk.cv.graphsearch.graph.nodes.Fag;
import no.bekk.cv.graphsearch.graph.nodes.Person;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.conversion.EndResult;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:config/applicationContext.xml")
public class PersonRepositoryIntTest {

    @Autowired
    PersonRepository repo;

    @Test
    public void testHentFagSomPersonenKan() {
        Iterable<Fag> fag = repo.hentFagSomPersonenKan("Andreas Heim");
        int i = 0;

        for (Fag fag1 : fag) {
            i++;
        }
        assertThat(i, is(3));
    }


    @Test
    public void testCypher() {
        String q = "start prosjekt1=node:prosjekt(navn = \"NAV\") " +
                "match person -[:KAN]-> fag <-[:BRUKTE]- prosjekt1 " +
                "with count(fag) as knownTech, person, prosjekt1 " +
                "where length(()<-[:BRUKTE]-prosjekt1)=knownTech " +
                "return person";
        EndResult<Person> personer = repo.query(q, new HashMap<String, Object>());
        for (Person person : personer) {
            System.out.println(person.getNavn());
        }

    }
}
