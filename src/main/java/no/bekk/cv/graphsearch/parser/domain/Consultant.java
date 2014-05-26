package no.bekk.cv.graphsearch.parser.domain;

public class Consultant extends Query {
    public Consultant(String name) {
        super(name);
    }

    @Override
    public void initQuery(StringBuilder start, StringBuilder match, String searchFor, Query middleTarget) {
        start.append("start person=node:person(navn = \"").append(getName()).append("\") \n");
        match.append("match ")
                .append(searchFor)
                .append(" ")
                .append(getRelationType(searchFor).print())
                .append(" person \n");
    }

    @Override
    public void appendQuery(StringBuilder start, StringBuilder match, String searchFor, Query middleTarget, int count) {
        start.append(", person").append(count).append("=node:person(navn = \"").append(getName()).append("\") \n");
        match.append(", ")
                .append(searchFor)
                .append(" ")
                .append(getRelationType(searchFor).print())
                .append(" person")
                .append(count)
                .append(" \n");
    }

    @Override
    public String toString() {
        return "person " + getName();
    }
}