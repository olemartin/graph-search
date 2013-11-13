package no.bekk.cv.graphsearch.parser;

import no.bekk.cv.graphsearch.graph.nodes.Fag;
import no.bekk.cv.graphsearch.graph.nodes.Prosjekt;
import no.bekk.cv.graphsearch.integration.PersonRepository;
import org.junit.BeforeClass;
import org.junit.Test;
import scala.actors.threadpool.Arrays;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GraphSearchParserCopyTest {
    private static ParboiledQueryParser parser;

    @BeforeClass
    public static void setup() {
        PersonRepository repository = mock(PersonRepository.class);
        when(repository.hentAlleFag()).thenReturn(Arrays.asList(new Fag[]{new Fag("Java"), new Fag("Neo4J")}));
        when(repository.hentAlleKunder()).thenReturn(Arrays.asList(new Prosjekt[]{new Prosjekt("Modernisering"), new Prosjekt("Statens vegvesen")}));
        parser = new ParboiledQueryParser(repository);
    }

    @Test
    public void testFinnAlleSomKanJava() {
        String query = parser.parseQuery("Finn alle som kan Java");
        assertThat(query,
                   is("start fag=node:fag(navn = \"Java\") \nmatch entitet -[:KAN]-> fag \nreturn distinct entitet"));
        System.out.println(query);
    }

    @Test
    public void testFinnAlleProsjekterSomKanJava() {
        String query = parser.parseQuery("Finn prosjekter som bruker Neo4J");
        assertThat(query,
                   is("start fag=node:fag(navn = \"Neo4J\") \nmatch entitet -[:BRUKTE]-> fag \nreturn distinct entitet"));
        System.out.println(query);
    }

    @Test
    public void testFinnAlleSomKanJavaOgNeo4j() {
        String query = parser.parseQuery("Finn alle som kan Java og som kan Neo4J");
        assertThat(query,
                   is("start fag=node:fag(navn = \"Neo4J\") \n, fag1=node:fag(navn = \"Java\") \nmatch entitet -[:KAN]-> fag \n, entitet -[:KAN]-> fag1 \nreturn distinct entitet"));
        System.out.println(query);
    }

    @Test
    public void testFinnAlleSomHarJobbetPaNav() {
        String query = parser.parseQuery("Finn alle som har jobbet hos Modernisering");
        assertThat(query,
                   is("start prosjekt=node:prosjekt(navn = \"Modernisering\") \nmatch entitet -[:KONSULTERTE]-> prosjekt \nreturn distinct entitet"));
        System.out.println(query);
    }

    @Test
    public void testFinnAlleSomHarJobbetPaNavOgKanJavaogNeo4J() {
        String query = parser.parseQuery("Finn alle som har jobbet hos Modernisering og som kan Java og som kan Neo4J");
        assertThat(query, is("start fag=node:fag(navn = \"Neo4J\") \n, fag1=node:fag(navn = \"Java\") \n, prosjekt2=node:prosjekt(navn = \"Modernisering\") \n" +
                                     "match entitet -[:KAN]-> fag \n, entitet -[:KAN]-> fag1 \n, entitet -[:KONSULTERTE]-> prosjekt2 \n" +
                                     "return distinct entitet"));
        System.out.println(query);
    }

    @Test
    public void testFinnTeknologiBruktAvNAV() {
        String query = parser.parseQuery("Finn teknologi brukt av Modernisering");
        assertThat(query, is("start prosjekt=node:prosjekt(navn = \"Modernisering\") \nmatch entitet <-[:BRUKTE]- prosjekt \nreturn distinct entitet"));
        System.out.println(query);
    }
}
