package no.bekk.cv.graphsearch.parser.api_ai;

import org.junit.Test;

public class ApiAiParserTest {


    @Test
    public void should_do_simple_integration() {
        ApiAiParser parser = new ApiAiParser();
        String cypher = parser.parseQuery("Everyone that knows Java and Elastic Search");
        System.out.println(cypher);

    }

}