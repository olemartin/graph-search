package no.bekk.cv.graphsearch.parser;

public class Customer extends Query {
    public Customer(String name) {
        super(name);
    }

    @Override
    public void initQuery(StringBuilder start, StringBuilder match, String searchFor) {
        start.append("start prosjekt=node:prosjekt(navn = \"" + getName() + "\") \n");
        match.append("match entitet " + getRelationType(searchFor).print() + " prosjekt \n");
    }

    @Override
    public void appendQuery(StringBuilder start, StringBuilder match, String searchFor, int count) {
        start.append(", prosjekt"+count+"=node:prosjekt(navn = \"" + getName() + "\") \n");
        match.append(", entitet " + getRelationType(searchFor).print() + " prosjekt"+count+" \n");
    }
}
