package no.bekk.cv.graphsearch.query;

import java.util.ArrayList;
import java.util.List;

public class TextualQueryParser implements QueryParser {
    private StringBuilder start = new StringBuilder();
    private StringBuilder match = new StringBuilder("match ");
    private StringBuilder returned = new StringBuilder("return ");
    private StringBuilder with = new StringBuilder("");
    private StringBuilder where = new StringBuilder("");

    private int counter = 1;
    private List<Pair> relations = new ArrayList<>();

    @Override
    public String parseQuery(String query) {
        Node searchFor = initSearchFor(query);
        searchFor = appendStart(query, searchFor);
        appendMatch();
        appendReturn(searchFor);
        return start.append("\n").append(match).append("\n").append(with).append(where).append(returned).toString();
    }

    private Node appendStart(String query, Node searchFor) {
        String[] tokens = query.split(" ");
        boolean prosjekt = false;
        for (int i = tokens.length - 1; i >= 0; i--) {
            String token = tokens[i];
            String navn;

            switch (token) {
                case "kan":
                    if (searchFor == Node.FAG) {
                        navn = leggTil(Node.PERSON, tokens, i);
                        addRelasjon(Relasjon.RELASJON.KAN, Direction.BACKWARD, navn, Node.FAG);
                    } else {
                        if (!prosjekt) {
                            navn = leggTil(Node.FAG, tokens, i);
                            addRelasjon(Relasjon.RELASJON.KAN, Direction.FORWARD, navn, Node.PERSON);
                        } else {
                            addRelasjon(Relasjon.RELASJON.KAN, Direction.FORWARD, "fag", Node.PERSON);
                            if (tokens[i + 1].equals("all")) {
                                addWith("with count(fag) as knownTech, person, prosjekt1 ");
                                addWhere("where length(()<-[:BRUKTE]-prosjekt1)=knownTech ");
                            }
                            prosjekt = false;
                        }
                    }
                    break;
                case "bruker":
                    if (searchFor == Node.FAG) {
                        navn = leggTil(Node.PROSJEKT, tokens, i);
                        addRelasjon(Relasjon.RELASJON.BRUKTE, Direction.BACKWARD, navn, Node.FAG);
                    } else {
                        navn = leggTil(Node.FAG, tokens, i);
                        addRelasjon(Relasjon.RELASJON.BRUKTE, Direction.FORWARD, navn, Node.PROSJEKT);
                    }
                    break;
                case "hos":
                case "på":
                    if (searchFor == Node.PROSJEKT) {
                        navn = leggTil(Node.PERSON, tokens, i);
                        addRelasjon(Relasjon.RELASJON.KONSULTERTE, Direction.BACKWARD, navn, Node.PROSJEKT);
                    } else {
                        navn = leggTil(Node.PROSJEKT, tokens, i);
                        addRelasjon(Relasjon.RELASJON.KONSULTERTE, Direction.FORWARD, navn, Node.PERSON);
                    }
                    break;
                case "av":
                    if (searchFor == Node.PROSJEKT) {
                        navn = leggTil(Node.FAG, tokens, i);
                        addRelasjon(Relasjon.RELASJON.BRUKTE, Direction.FORWARD, navn, Node.PROSJEKT);
                        return searchFor;
                    } else {
                        navn = leggTil(Node.PROSJEKT, tokens, i);
                        addRelasjon(Relasjon.RELASJON.BRUKTE, Direction.BACKWARD, navn, Node.FAG);
                        if (searchFor == Node.PERSON) {
                            prosjekt = true;
                        }
                    }
                    break;
                case "med":
                    if (searchFor == Node.PERSON) {
                        navn = leggTil(Node.PROSJEKT, tokens, i);
                        addRelasjon(Relasjon.RELASJON.KONSULTERTE, Direction.FORWARD, navn, Node.PERSON);
                    } else {
                        navn = leggTil(Node.PERSON, tokens, i);
                        addRelasjon(Relasjon.RELASJON.KONSULTERTE, Direction.BACKWARD, navn, Node.PROSJEKT);
                    }
                    break;
                case "og":

            }
        }
        return searchFor;
    }

    private void addWhere(String statement) {
        where.append(statement);
    }

    private void addWith(String statement) {
        with.append(statement);
    }

    private void appendMatch() {
        for (Pair relation : relations) {
            if (relations.indexOf(relation) > 0) {
                match.append(", ");
            }
            switch (relation.getDirection()) {
                case FORWARD:
                    match.append(String.format("%s -[:%s]-> %s ",
                                               relation.getFrom(),
                                               relation.getRelation(),
                                               relation.getTo()));
                    break;
                case BACKWARD:
                    match.append(String.format("%s <-[:%s]- %s ",
                                               relation.getFrom(),
                                               relation.getRelation(),
                                               relation.getTo()));
                    break;

            }
        }
    }

    private void appendReturn(Node searchFor) {
        returned.append("distinct ").append(searchFor.name().toLowerCase());
    }

    private String leggTil(Node indexName, String[] tokens, int arrayIndex) {
        String searchFor;
        String navn = String.format(indexName.name().toLowerCase() + "%s", counter++);

        if (tokens.length == arrayIndex + 1) {
            start.append(String.format("start %s=node(*) ", navn));
            return navn;
        } else {
            searchFor = tokens[arrayIndex + 1];
        }

        StringBuilder valueBuilder = new StringBuilder();
        if (searchFor.startsWith("\"")) {
            int i = arrayIndex + 1;
            do {
                searchFor = tokens[i++];
                valueBuilder.append(searchFor).append(" ");
            } while (!searchFor.endsWith("\""));
            valueBuilder = new StringBuilder(valueBuilder.substring(1, valueBuilder.length() - 2));
        } else {
            valueBuilder.append(searchFor);
        }
        if (start.length() == 0) {
            start.append(
                    String.format("start %s=node:" + indexName.name().toLowerCase() + "(navn = \"%s\") ", navn,
                                  valueBuilder.toString()));
        } else {
            start.append(
                    String.format(", %s=node:" + indexName.name().toLowerCase() + "(navn = \"%s\") ", navn,
                                  valueBuilder.toString()));
        }
        return navn;
    }

    private void addRelasjon(Relasjon.RELASJON relation, Direction direction, String to,
                             Node from) {
        relations.add(new Pair(relation.name(), from, to, direction));
    }

    private Node initSearchFor(String query) {
        if (query.endsWith("hos") || query.endsWith("hos ") || query.endsWith("av") || query.endsWith("av ")) {
            return Node.PROSJEKT;
        } else if (query.endsWith("kan") || query.endsWith("kan ") || query.endsWith("bruker") ||
                query.endsWith("bruker ")) {
            return Node.FAG;
        } else if (query.endsWith("med") || query.endsWith("med ")) {
            return Node.PERSON;
        }
        for (String token : query.split(" ")) {
            if (token.equalsIgnoreCase("alle") ||
                    token.equalsIgnoreCase("konsulenter") ||
                    token.equalsIgnoreCase("ansatte")) {
                return Node.PERSON;
            } else if (token.equalsIgnoreCase("prosjekter") ||
                    token.equalsIgnoreCase("prosjekt")) {
                return Node.PROSJEKT;
            } else if (token.equalsIgnoreCase("kunde") ||
                    token.equalsIgnoreCase("kunder")) {
                return Node.KUNDE;
            } else if (token.equalsIgnoreCase("fag") || token.equalsIgnoreCase("teknologi")) {
                return Node.FAG;
            }
        }
        throw new IllegalArgumentException("Ukjent queryformat: " + query);
    }
}
