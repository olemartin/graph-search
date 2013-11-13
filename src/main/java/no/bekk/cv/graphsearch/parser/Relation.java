package no.bekk.cv.graphsearch.parser;

import no.bekk.cv.graphsearch.query.Direction;

public class Relation {

    private String value;
    private Direction direction;

    public Relation(String value, Direction direction) {
        this.value = value;
        this.direction = direction;
    }

    public String getValue() {
        return value;
    }

    public Direction getDirection() {
        return direction;
    }

    public String print() {
        switch (direction) {
            case FORWARD:
                return "-" + value + "->";
            case BACKWARD:
                return "<-" + value + "-";
            default:
                throw new IllegalArgumentException("Unknown direction");
        }
    }
}
