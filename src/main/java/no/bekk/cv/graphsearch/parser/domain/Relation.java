package no.bekk.cv.graphsearch.parser.domain;

public class Relation {

    private String value;
    private Direction direction;

    public Relation(String value, Direction direction) {
        this.value = value;
        this.direction = direction;
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
