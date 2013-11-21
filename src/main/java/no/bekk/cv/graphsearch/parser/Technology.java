package no.bekk.cv.graphsearch.parser;

public class Technology extends Query {
    public Technology(String name) {
        super(name);
    }

    @Override
    public void initQuery(StringBuilder start, StringBuilder match, String searchFor, String rootSearchFor) {
        start.append("start fag=node:fag(navn = \"").append(getName()).append("\") \n");
        if (rootSearchFor != null) {
            createMatch(match, searchFor, rootSearchFor);
        } else {
            createMatch(match, searchFor, searchFor);
        }
    }

    private void createMatch(StringBuilder match, String relationType, String searchFor) {
        match.append("match ")
                .append(searchFor)
                .append(" ")
                .append(getRelationType(relationType).print())
                .append(" fag \n");
    }

    @Override
    public void appendQuery(StringBuilder start, StringBuilder match, String searchFor, int count) {
        start.append(", fag").append(count).append("=node:fag(navn = \"").append(getName()).append("\") \n");
        match.append(", ")
                .append(searchFor)
                .append(" ")
                .append(getRelationType(searchFor).print())
                .append(" fag")
                .append(count)
                .append(" \n");
    }

    @Override
    public String toString() {
        return "teknologi " + getName();
    }
}