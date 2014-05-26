package no.bekk.cv.graphsearch.parser.domain.query;

public class Technology extends Query {
    public Technology(String name) {
        super(name);
    }

    @Override
    public void initQuery(StringBuilder start, StringBuilder match, String searchFor, Query middleTarget) {
        start.append("start fag=node:fag(navn = \"").append(getName()).append("\") \n");
        match.append("match ")
                .append(searchFor)
                .append(" ")
                .append(getRelationType(searchFor).print())
                .append(" fag \n");
    }

    @Override
    public void appendQuery(StringBuilder start, StringBuilder match, String searchFor, Query middleTarget, int count) {
        start.append(", fag").append(count).append("=node:fag(navn = \"").append(getName()).append("\") \n");
        match.append(", ")
                .append(searchFor)
                .append(" ")
                .append(getRelationType(searchFor).print())
                .append(" fag")
                .append(count)
                .append(" \n");
    }

    @Override
    public String toString() {
        return "teknologi " + getName();
    }
}