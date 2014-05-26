package no.bekk.cv.graphsearch.parser.domain.query;

import no.bekk.cv.graphsearch.parser.domain.Direction;
import no.bekk.cv.graphsearch.parser.domain.Relation;
import no.bekk.cv.graphsearch.parser.domain.RelationType;

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

    public void initQuery(StringBuilder start, StringBuilder match, String searchFor, Query middleTarget) {
        throw new IllegalStateException("This method should never be called");
    }

    public void appendQuery(StringBuilder start, StringBuilder match, String searchFor, Query middleTarget, int count) {
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
                return new Relation(RelationType.KAN, Direction.FORWARD);
            }
            if (name.equals(PROJECTS.getName())) {
                return new Relation(RelationType.BRUKTE, Direction.FORWARD);
            }
        } else if (this instanceof Customer) {
            if (name.equals(CONSULTANTS.getName())) {
                return new Relation(RelationType.KONSULTERTE, Direction.FORWARD);
            }
            if (name.equals(TECHNOLOGIES.getName())) {
                return new Relation(RelationType.BRUKTE, Direction.BACKWARD);
            }
        } else if (this instanceof UsedTechnology) {
            if (name.equals(CONSULTANTS.getName())) {
                return new Relation(RelationType.KAN, Direction.BACKWARD);
            }
            if (name.equals(PROJECTS.getName())) {
                return new Relation(RelationType.BRUKTE, Direction.BACKWARD);
            }
            if (name.equals(TECHNOLOGIES.getName())) {
                return new Relation(RelationType.BRUKTE, Direction.FORWARD);
            }
        } else if (this instanceof WorkedAt) {
            if (name.equals(CONSULTANTS.getName())) {
                return new Relation(RelationType.KONSULTERTE, Direction.BACKWARD);
            }
        } else if (this instanceof Consultant) {
            if (name.equals(PROJECTS.getName())) {
                return new Relation(RelationType.KONSULTERTE, Direction.BACKWARD);
            }
        } else {
            if (name.equals(CONSULTANTS.getName())) {
                return new Relation(RelationType.KAN, Direction.BACKWARD);
            }
            if (name.equals(PROJECTS.getName())) {
                return new Relation(RelationType.BRUKTE, Direction.BACKWARD);
            }
        }
        throw new IllegalStateException("Unknown relation type " + name + " of type " + getClass().getSimpleName());
    }
}
