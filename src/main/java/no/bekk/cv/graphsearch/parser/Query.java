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
        System.out.println("Krise!");
    }

    public void appendQuery(StringBuilder start, StringBuilder match, String searchFor, int count) {
        System.out.println("Krise2!");
    }

    protected Relation getRelationType(String name) {
        if (this instanceof Technology) {
            if (name.equals(Query.CONSULTANTS.getName())) {
                return new Relation("[:KAN]", Direction.FORWARD);
            }
            if (name.equals(Query.PROJECTS.getName())) {
                return new Relation("[:BRUKTE]", Direction.FORWARD);
            }
        } else if (this instanceof Customer) {
            System.out.println(name);
            if (name.equals(Query.CONSULTANTS.getName())) {
                return new Relation("[:KONSULTERTE]", Direction.FORWARD);
            }
            if (name.equals(Query.TECHNOLOGIES.getName())) {
                return new Relation("[:BRUKTE]", Direction.BACKWARD);
            }
        }
        throw new IllegalArgumentException("Ukjent relasjon, " + name);
    }
}
