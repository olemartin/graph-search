package no.bekk.cv.graphsearch.parser;

import no.bekk.cv.graphsearch.query.Direction;

public class Query {
    public static Query CONSULTANTS = new Query("CONSULTANTS");
    public static Query PROJECTS = new Query("PROJECTS");
    public static Query TECHNOLOGIES = new Query("TECHNOLOGIES");
    private String name;

    public Query(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "search for " + name;
    }

    public String getName() {
        return name;
    }

    public void initQuery(StringBuilder start, StringBuilder match, String searchFor) {
        throw new IllegalStateException("This method should never be called");
    }

    public void appendQuery(StringBuilder start, StringBuilder match, String searchFor, int count) {
        match.append(", ")
                .append(this.getName())
                .append(" ")
                .append(getRelationType(searchFor).print())
                .append(" ")
                .append(searchFor)
                .append(" \n");
    }

    protected Relation getRelationType(String name) {
        if (this instanceof Technology) {
            if (name.equals(CONSULTANTS.getName())) {
                return new Relation("[:KAN]", Direction.FORWARD);
            }
            if (name.equals(PROJECTS.getName())) {
                return new Relation("[:BRUKTE]", Direction.FORWARD);
            }
        } else if (this instanceof Customer) {
            System.out.println(name);
            if (name.equals(CONSULTANTS.getName())) {
                return new Relation("[:KONSULTERTE]", Direction.FORWARD);
            }
            if (name.equals(TECHNOLOGIES.getName())) {
                return new Relation("[:BRUKTE]", Direction.BACKWARD);
            }
        }
        throw new IllegalStateException("Unknown relation type " + name);
    }
}
