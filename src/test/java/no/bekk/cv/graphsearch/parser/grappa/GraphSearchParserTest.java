package no.bekk.cv.graphsearch.parser.grappa;

import no.bekk.cv.graphsearch.graph.nodes.Fag;
import no.bekk.cv.graphsearch.graph.nodes.Person;
import no.bekk.cv.graphsearch.graph.nodes.Prosjekt;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class GraphSearchParserTest {
    private static GraphParser parser;

    @SuppressWarnings("unchecked")
    @BeforeClass
    public static void setup() {
        GraphGrammar.init(Arrays.asList(new Fag("Java"), new Fag("Neo4J")),
                Arrays.asList(new Prosjekt("Modernisering"), new Prosjekt("Statens vegvesen")),
                Arrays.asList(new Person("Ole-Martin Mørk", "Manager")));
        parser = new GraphParser();
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

    @Test
    public void finnAlleSomKan() {
        String query = parser.parseQuery("Finn alle som kan ");
        assertThat(query,
                   is("start wildcard=node(*) \nmatch CONSULTANTS <-[:KAN]- wildcard \nreturn distinct CONSULTANTS"));
        System.out.println(query);
    }

    @Test
    public void finnProsjekterSomBrukte() {
        String query = parser.parseQuery("Finn prosjekter som bruker ");
        assertThat(query,
                is("start wildcard=node(*) \nmatch PROJECTS <-[:BRUKTE]- wildcard \nreturn distinct PROJECTS"));
        System.out.println(query);
    }

    @Test
    public void testFinnAlleSomHarJobbetHos() {
        String query = parser.parseQuery("FINN ALLE SOM HAR JOBBET HOS ");
        assertThat(query,
                is("start wildcard=node(*) \nmatch CONSULTANTS <-[:KONSULTERTE]- wildcard \nreturn distinct CONSULTANTS"));
        System.out.println(query);
    }

    @Test
    public void testFinnProsjekterMedOleMartin() {
        String query = parser.parseQuery("Finn prosjekter med Ole-Martin Mørk");
        assertThat(query,
                is("start person=node:person(navn = \"Ole-Martin Mørk\") \nmatch PROJECTS <-[:KONSULTERTE]- person \nreturn distinct PROJECTS"));
        System.out.println(query);
    }
}
