package no.bekk.cv.graphsearch.query;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class QueryParserTest {

    private final QueryParser parser = new TextualQueryParser();

    @Test
    public void testFinnAlleSomKanJava() {
        String query = parser.parseQuery("Finn alle som kan Java");
        assertThat(query, is("start fag1=node:fag(navn = \"Java\") \nmatch person -[:KAN]-> fag1 \nreturn distinct person"));
        System.out.println(query);
    }

    @Test
    public void testFinnAlleProsjekterSomKanJava() {
        String query = parser.parseQuery("Finn prosjekter som bruker Java");
        assertThat(query, is("start fag1=node:fag(navn = \"Java\") \nmatch prosjekt -[:BRUKTE]-> fag1 \nreturn distinct prosjekt"));
        System.out.println(query);
    }

    @Test
    public void testFinnAlleSomKanJavaOgNeo4j() {
        String query = parser.parseQuery("Finn alle som kan Java og som kan Neo4J");
        assertThat(query, is("start fag1=node:fag(navn = \"Neo4J\") , fag2=node:fag(navn = \"Java\") \nmatch person -[:KAN]-> fag1 , person -[:KAN]-> fag2 \nreturn distinct person"));
        System.out.println(query);
    }

    @Test
    public void testFinnAlleSomHarJobbetPaNav() {
        String query = parser.parseQuery("Finn alle som har jobbet hos Modernisering");
        assertThat(query, is("start prosjekt1=node:prosjekt(navn = \"Modernisering\") \nmatch person -[:KONSULTERTE]-> prosjekt1 \nreturn distinct person"));
        System.out.println(query);
    }

    @Test
    public void testFinnAlleSomHarJobbetPaNavOgKanJava() {
        String query = parser.parseQuery("Finn alle som har jobbet hos Modernisering og som kan Java og som kan Neo4J");
        assertThat(query, is("start fag1=node:fag(navn = \"Neo4J\") , fag2=node:fag(navn = \"Java\") , prosjekt3=node:prosjekt(navn = \"Modernisering\") \n" +
                                     "match person -[:KAN]-> fag1 , person -[:KAN]-> fag2 , person -[:KONSULTERTE]-> prosjekt3 \n" +
                                     "return distinct person"));
        System.out.println(query);
    }

    @Test
    public void testFinnTeknologiBruktAvNAV() {
        String query = parser.parseQuery("Finn teknologi brukt av Modernisering");
        assertThat(query, is("start prosjekt1=node:prosjekt(navn = \"Modernisering\") \nmatch fag <-[:BRUKTE]- prosjekt1 \nreturn distinct fag"));
        System.out.println(query);
    }

    @Test
    public void testFinnAlleSomKanTeknologiBruktAvNAV() {
        String query = parser.parseQuery("Finn alle som kan teknologi brukt av Modernisering");
        assertThat(query, is("start prosjekt1=node:prosjekt(navn = \"Modernisering\") \nmatch fag <-[:BRUKTE]- prosjekt1 , person -[:KAN]-> fag \nreturn distinct person"));
        System.out.println(query);
    }

    @Test
    public void testFinnAlleSomKanAllTeknologiBruktAvNAV() {
        String query = parser.parseQuery("Finn alle som kan all teknologi brukt av Modernisering");
        assertThat(query, is("start prosjekt1=node:prosjekt(navn = \"Modernisering\") \nmatch fag <-[:BRUKTE]- prosjekt1 , person -[:KAN]-> fag \n" +
                                     "with count(fag) as knownTech, person, prosjekt1 " +
                                     "where length(()<-[:BRUKTE]-prosjekt1)=knownTech " +
                                     "return distinct person"));
        System.out.println(query);
    }

    @Test
    public void testFinnAlleSomKanTeknologiBruktAvNAVOgSomKanJava() {
        String query = parser.parseQuery("Finn alle som kan teknologi brukt av NAV og som kan Java");
        assertThat(query, is("start fag1=node:fag(navn = \"Java\") , prosjekt2=node:prosjekt(navn = \"NAV\") \nmatch person -[:KAN]-> fag1 , fag <-[:BRUKTE]- prosjekt2 , person -[:KAN]-> fag \nreturn distinct person"));
        System.out.println(query);
    }

    @Test
    public void testFinnProsjekterVegardSkjefstadHarJobbetPaa() {
        String query = parser.parseQuery("Finn prosjekter med \"Vegard Skjefstad\"");
        assertThat(query, is("start person1=node:person(navn = \"Vegard Skjefstad\") \nmatch prosjekt <-[:KONSULTERTE]- person1 \nreturn distinct prosjekt"));
        System.out.println(query);
    }

    @Test
    public void testFinnProsjekterVegardSkjefstadHarJobbetPaaOgSomBrukteJava() {
        String query = parser.parseQuery("Finn prosjekter som bruker Java med \"Vegard Skjefstad\"");
        assertThat(query, is("start person1=node:person(navn = \"Vegard Skjefstad\") , fag2=node:fag(navn = \"Java\") \nmatch prosjekt <-[:KONSULTERTE]- person1 , prosjekt -[:BRUKTE]-> fag2 \nreturn distinct prosjekt"));
        System.out.println(query);
    }

    @Test
    public void testFinnAlleSomHarJobbetPa() {
        String query = parser.parseQuery("Finn alle som har jobbet hos");
        assertThat(query, is("start person1=node(*) \nmatch prosjekt <-[:KONSULTERTE]- person1 \nreturn distinct prosjekt"));
        System.out.println(query);
    }

    @Test
    public void finnAlleSomKan() {
        String query = parser.parseQuery("Finn alle som kan");
        assertThat(query, is("start person1=node(*) \nmatch fag <-[:KAN]- person1 \nreturn distinct fag"));
        System.out.println(query);
    }

    @Test
    public void finnProsjekterMed() {
        String query = parser.parseQuery("Finn prosjekter med");
        assertThat(query, is("start prosjekt1=node(*) \nmatch person -[:KONSULTERTE]-> prosjekt1 \nreturn distinct person"));
        System.out.println(query);
    }

    @Test
    public void finnProsjekterSomBrukte() {
        String query = parser.parseQuery("Finn prosjekter som bruker");
        assertThat(query, is("start prosjekt1=node(*) \nmatch fag <-[:BRUKTE]- prosjekt1 \nreturn distinct fag"));
        System.out.println(query);
    }

    @Test
    public void finnAlleSomKanTeknologiBruktAv() {
        String query = parser.parseQuery("Finn alle som kan teknologi brukt av");
        assertThat(query, is("start fag1=node(*) \nmatch prosjekt -[:BRUKTE]-> fag1 \nreturn distinct prosjekt"));
        System.out.println(query);
    }
}
