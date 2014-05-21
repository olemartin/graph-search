package no.bekk.cv.graphsearch.parser.domain;

public class Customer extends Query {
    public Customer(String name) {
        super(name);
    }

    @Override
    public void initQuery(StringBuilder start, StringBuilder match, String searchFor, Query middleTarget) {
        start.append("start prosjekt=node:prosjekt(navn = \"").append(getName()).append("\") \n");
        if (middleTarget != null) {
            match.append("match ")
                    .append(middleTarget.getName())
                    .append(" ")
                    .append(getRelationType(middleTarget.getName()).print())
                    .append(" prosjekt \n");
        } else {
            match.append("match ")
                    .append(searchFor)
                    .append(" ")
                    .append(getRelationType(searchFor).print())
                    .append(" prosjekt \n");
        }
    }

    @Override
    public void appendQuery(StringBuilder start, StringBuilder match, String searchFor, Query middleTarget, int count) {
        start.append(", prosjekt").append(count).append("=node:prosjekt(navn = \"").append(getName()).append("\") \n");
        match.append(", ")
                .append(searchFor)
                .append(" ")
                .append(getRelationType(searchFor).print())
                .append(" prosjekt")
                .append(count)
                .append(" \n");
    }
}
