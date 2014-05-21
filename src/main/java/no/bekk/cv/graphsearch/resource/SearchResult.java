package no.bekk.cv.graphsearch.resource;

import java.util.ArrayList;
import java.util.List;

public class SearchResult {
    private List<String> navn = new ArrayList<>();
    private String cypher;

    public SearchResult(List<String> strings, String cypher) {
        this.navn = strings;
        this.cypher = cypher;
    }

    public List<String> getNavn() {
        return navn;
    }

    public String getCypher() {
        return cypher.replaceAll("\n", "<br/>");
    }
}
