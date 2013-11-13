package no.bekk.cv.graphsearch.parser;

import no.bekk.cv.graphsearch.graph.nodes.Fag;
import no.bekk.cv.graphsearch.graph.nodes.Prosjekt;
import org.junit.BeforeClass;
import org.junit.Test;
import org.parboiled.Parboiled;
import org.parboiled.errors.ParseError;
import org.parboiled.parserunners.ReportingParseRunner;
import org.parboiled.support.ParseTreeUtils;
import org.parboiled.support.ParsingResult;
import scala.actors.threadpool.Arrays;

import java.util.Iterator;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class GraphSearchParserTest {

    @BeforeClass
    public static void setup() {
        GraphSearchParser.init(
                Arrays.asList(new Fag[]{new Fag("Java"), new Fag("Neo4J")}),
                Arrays.asList(new Prosjekt[]{new Prosjekt("Modernisering"), new Prosjekt("Statens vegvesen")}));
    }

    @Test
    public void enkelParsing() {
        String parseTreePrintOut = parse("alle som kan Java");
        assertThat(parseTreePrintOut, not(is("")));
        System.out.println(parseTreePrintOut);
    }

    @Test
    public void littMer() {
        String parseTreePrintOut = parse("Finn alle konsulenter som kan Neo4J");
        assertThat(parseTreePrintOut, not(is("")));
        System.out.println(parseTreePrintOut);
    }

    @Test
    public void finnProsjekter() {
        String parseTreePrintOut = parse("prosjekter som bruker Neo4J");
        assertThat(parseTreePrintOut, not(is("")));
        System.out.println(parseTreePrintOut);
    }

    @Test
    public void finnTeknologier() {
        String input = "teknologier som brukes av Norges forskningsråd";
        GraphSearchParser parser = Parboiled.createParser(GraphSearchParser.class);
        ReportingParseRunner runner = new ReportingParseRunner(parser.Expression());
        ParsingResult result = runner.run(input);
        Iterator iterator = result.valueStack.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }

    private String parse(String input) {
        GraphSearchParser parser = Parboiled.createParser(GraphSearchParser.class);
        ParsingResult<?> result = ReportingParseRunner.run(parser.Expression(), input);
        if (result.hasErrors()) {
            for (ParseError parseError : result.parseErrors) {
                System.out.println(parseError.getStartIndex());
                System.out.println(parseError.getEndIndex());
                System.out.println(parseError.getInputBuffer());
                System.out.println(parseError.getErrorMessage());
            }
        }
        Iterator iterator = result.valueStack.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
        return ParseTreeUtils.printNodeTree(result);
    }

}
