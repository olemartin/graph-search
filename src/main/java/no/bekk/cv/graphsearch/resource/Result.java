package no.bekk.cv.graphsearch.resource;

import java.util.ArrayList;
import java.util.List;

public class Result {
    private List<String> navn = new ArrayList<>();
    private String cypher;

    public Result(Iterable<String> navn, String cypher) {
        for (String s : navn) {
            this.navn.add(s);
        }
        this.cypher = cypher;
    }

    public List<String> getNavn() {
        return navn;
    }

    public String getCypher() {
        return cypher.replaceAll("\n", "<br/>");
    }
}
