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

public class GraphSearchParserTest {
    private static ParboiledQueryParser parser;

    @SuppressWarnings("unchecked")
    @BeforeClass
    public static void setup() {
        PersonRepository repository = mock(PersonRepository.class);
        when(repository.hentAlleFag()).thenReturn(Arrays.asList(new Fag[]{new Fag("Java"), new Fag("Neo4J")}));
        when(repository.hentAlleKunder()).thenReturn(
                Arrays.asList(new Prosjekt[]{new Prosjekt("Modernisering"), new Prosjekt("Statens vegvesen")}));
        parser = new ParboiledQueryParser(repository);
    }

    @Test
    public void testFinnAlleSomKanJava() {
        String query = parser.parseQuery("Finn alle som kan Java");
        assertThat(query,
                   is("start fag=node:fag(navn = \"Java\") \nmatch CONSULTANTS -[:KAN]-> fag \nreturn distinct CONSULTANTS"));
        System.out.println(query);
    }

    @Test
    public void testFinnAlleProsjekterSomBrukerNeo4j() {
        String query = parser.parseQuery("Finn prosjekter som bruker Neo4J");
        assertThat(query,
                   is("start fag=node:fag(navn = \"Neo4J\") \nmatch PROJECTS -[:BRUKTE]-> fag \nreturn distinct PROJECTS"));
        System.out.println(query);
    }

    @Test
    public void testFinnAlleSomKanJavaOgNeo4j() {
        String query = parser.parseQuery("Finn alle som kan Java og som kan Neo4J");
        assertThat(query,
                   is("start fag=node:fag(navn = \"Java\") \n, fag1=node:fag(navn = \"Neo4J\") \nmatch CONSULTANTS -[:KAN]-> fag \n, CONSULTANTS -[:KAN]-> fag1 \nreturn distinct CONSULTANTS"));
        System.out.println(query);
    }

    @Test
    public void testFinnAlleSomHarJobbetPaNav() {
        String query = parser.parseQuery("Finn alle som har jobbet hos Modernisering");
        assertThat(query,
                   is("start prosjekt=node:prosjekt(navn = \"Modernisering\") \nmatch CONSULTANTS -[:KONSULTERTE]-> prosjekt \nreturn distinct CONSULTANTS"));
        System.out.println(query);
    }

    @Test
    public void testFinnAlleSomHarJobbetPaNavUPPERCASE() {
        String query = parser.parseQuery("FINN ALLE SOM HAR JOBBET HOS Modernisering");
        assertThat(query,
                   is("start prosjekt=node:prosjekt(navn = \"Modernisering\") \nmatch CONSULTANTS -[:KONSULTERTE]-> prosjekt \nreturn distinct CONSULTANTS"));
        System.out.println(query);
    }

    @Test
    public void testFinnAlleSomHarJobbetPaNavOgKanJavaogNeo4J() {
        String query = parser.parseQuery("Finn alle som har jobbet hos Modernisering og som kan Java og som kan Neo4J");
        assertThat(query,
                   is("start prosjekt=node:prosjekt(navn = \"Modernisering\") \n, fag1=node:fag(navn = \"Java\") \n," +
                              " fag2=node:fag(navn = \"Neo4J\") \nmatch CONSULTANTS -[:KONSULTERTE]-> prosjekt \n," +
                              " CONSULTANTS -[:KAN]-> fag1 \n, CONSULTANTS -[:KAN]-> fag2 \nreturn distinct CONSULTANTS"
                   ));
        System.out.println(query);
    }

    @Test
    public void testFinnTeknologiBruktAvNAV() {
        String query = parser.parseQuery("Finn teknologi brukt av Modernisering");
        assertThat(query,
                   is("start prosjekt=node:prosjekt(navn = \"Modernisering\") \nmatch TECHNOLOGIES <-[:BRUKTE]- prosjekt \nreturn distinct TECHNOLOGIES"));
        System.out.println(query);
    }

    @Test
    public void testFinnAlleSomKanTeknologiBruktAvNAV() {
        String query = parser.parseQuery("Finn alle som kan teknologi brukt av Modernisering");
        assertThat(query,
                   is("start prosjekt=node:prosjekt(navn = \"Modernisering\") \nmatch TECHNOLOGIES <-[:BRUKTE]- prosjekt \n, TECHNOLOGIES <-[:KAN]- CONSULTANTS \nreturn distinct CONSULTANTS"));
        System.out.println(query);
    }


}
