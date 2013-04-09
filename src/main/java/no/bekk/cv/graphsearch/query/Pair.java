package no.bekk.cv.graphsearch.query;

public class Pair {
    private final String relation;
    private final Node from;
    private final String to;
    private final Direction direction;

    public Pair(String relation, Node from, String to, Direction direction) {
        this.relation = relation;
        this.from = from;
        this.to = to;
        this.direction = direction;
    }

    public String getRelation() {
        return relation;
    }

    public String getTo() {
        return to;
    }

    public Direction getDirection() {
        return direction;
    }

    public String getFrom() {
        return from.name().toLowerCase();
    }
}
