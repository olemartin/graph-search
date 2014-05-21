package no.bekk.cv.graphsearch.integration;

import no.bekk.cv.graphsearch.graph.nodes.Person;
import no.bekk.cv.graphsearch.graph.nodes.Prosjekt;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:config/applicationContext.xml")
@Transactional
public class GraphRepositoryTest {

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    Neo4jTemplate template;

    @Autowired
    RepositoryPopulator repo;

    @Test
    public void lagretPersonSkalKunneHentesFraDatabasen() {
        Person olemartin = template.save(new Person("Ole-Martin Mørk", "Scientist"));
        Person sok = template.findOne(olemartin.getId(), Person.class);
        assertThat(sok.getNavn(), is("Ole-Martin Mørk"));
        assertThat(sok.getRolle(), is("Scientist"));
    }

    @Test
    public void lagretPersonSkalKunneHentesFraDatabasenViaIndex() {
        Person person = new Person("Ole-Martin Mørk", "Scientist");
        person.jobbetI(new Prosjekt("NAV Modernisering"));
        template.save(person);
        GraphRepository<Person> repo = template.repositoryFor(Person.class);
        Person funnet = repo.findAllByQuery("navn", "Ole*").single();
        assertThat(funnet.getNavn(), is("Ole-Martin Mørk"));
    }


    public void testPopuler() {
        repo.populate();
    }
}
