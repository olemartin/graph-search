package no.bekk.cv.graphsearch.parser;

public class Technology extends Query {
    public Technology(String name) {
        super(name);
    }

    @Override
    public void initQuery(StringBuilder start, StringBuilder match, String searchFor) {
        start.append("start fag=node:fag(navn = \"" + getName() + "\") \n");
        match.append("match entitet " + getRelationType(searchFor).print() + " fag \n");
    }

    @Override
    public void appendQuery(StringBuilder start, StringBuilder match, String searchFor, int count) {
        start.append(", fag" + count + "=node:fag(navn = \"" + getName() + "\") \n");
        match.append(", entitet " + getRelationType(searchFor).print() + " fag" + count + " \n");
    }

    @Override
    public String toString() {
        return "teknologi " + getName();
    }
}