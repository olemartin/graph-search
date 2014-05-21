package no.bekk.cv.graphsearch.parser.parboiled;

import no.bekk.cv.graphsearch.graph.nodes.Fag;
import no.bekk.cv.graphsearch.graph.nodes.Prosjekt;
import no.bekk.cv.graphsearch.parser.domain.*;
import org.parboiled.BaseParser;
import org.parboiled.Rule;
import org.parboiled.annotations.BuildParseTree;
import org.parboiled.annotations.SuppressNode;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@BuildParseTree
public class GraphGrammar extends BaseParser<GraphSearchQuery> {


    static List<String> tilgjengeligeFag = new ArrayList<>();
    static List<String> tilgjengeligeKunder = new ArrayList<>();

    public static synchronized void init(Iterable<Fag> fags, Iterable<Prosjekt> customers) {
        if (tilgjengeligeFag.isEmpty()) {
            for (Fag fag : fags) {
                tilgjengeligeFag.add(fag.getNavn());
            }
            for (Prosjekt customer : customers) {
                tilgjengeligeKunder.add(customer.getNavn());
            }
        }
    }

    public Rule expression() {
        return sequence(
                start(),
                firstOf(
                        people(),
                        projects(),
                        technologies(true)
                ),
                oneOrMore(
                        firstOf(
                                And(),
                                sequence(
                                        know(false),
                                        subjects()
                                ),
                                sequence(
                                        workedAt(false),
                                        customers()
                                ),
                                sequence(
                                        know(false),
                                        customers()
                                ),
                                sequence(
                                        know(false),
                                        technologies(false)
                                ),
                                know(true),
                                workedAt(true)
                        )
                )
        );
    }

    @SuppressNode
    Rule that() {
        return optional("som ");
    }

    Rule people() {
        List<String> subjects = Arrays.asList("alle konsulenter ", "alle ", "konsulenter ");
        return firstOf(subjects.stream().map(this::peopleSequence).toArray());
    }

    Rule projects() {
        List<String> subjects = Arrays.asList("prosjekter ", "engasjement ");
        return firstOf(subjects.stream().map(this::projectSequence).toArray());
    }

    Rule technologies(boolean root) {
        List<String> subjects = Arrays.asList("teknologier ", "teknologi ", "fag ");
        if (root) {
            return firstOf(subjects.stream().map(this::technologySequence).toArray());
        } else {
            return firstOf(subjects.stream().map(this::inTheMiddleSequence).toArray());
        }
    }

    Rule inTheMiddleSequence(String name) {
        return sequence(push(pop().addMiddleTarget(Query.TECHNOLOGIES)), name);
    }

    Rule peopleSequence(String name) {
        return sequence(push(new GraphSearchQuery(Query.CONSULTANTS)), name);
    }

    Rule technologySequence(String name) {
        return sequence(push(new GraphSearchQuery(Query.TECHNOLOGIES)), name);
    }

    Rule projectSequence(String name) {
        return sequence(push(new GraphSearchQuery(Query.PROJECTS)), name);
    }

    @SuppressNode
    Rule start() {
        return optional("Finn ");
    }

    Rule know(boolean empty) {
        Rule verbs = firstOf("kan ", "kjenner ", "programmerer ", "bruker ", "brukte ", "brukes av ", "brukt av ");
        if (empty) {
            return sequence(
                    push(pop().setRetrieveParameters(true).addTarget(new UsedTechology())),
                    optional(that()),
                    verbs);
        } else {
            return sequence(
                    optional(that()),
                    verbs);
        }
    }

    Rule workedAt(boolean empty) {
        Rule verbs = firstOf("har jobbet på ", "har jobbet hos ", "konsulterte ", "har konsultert ", "har vært hos ");
        if (empty) {

            return sequence(
                    push(pop().setRetrieveParameters(true).addTarget(new WorkedAt())),
                    optional(that()),
                    verbs);
        } else {
            return sequence(
                    optional(that()),
                    verbs);
        }
    }

    Rule subjects() {
        Rule[] rules = new Rule[tilgjengeligeFag.size()];
        for (int i = 0; i < tilgjengeligeFag.size(); i++) {
            rules[i] = sequence(
                    push(pop().addTarget(new Technology(tilgjengeligeFag.get(i)))),
                    tilgjengeligeFag.get(i) + " ");
        }
        return firstOf(rules);
    }

    Rule customers() {
        Rule[] rules = new Rule[tilgjengeligeKunder.size()];
        for (int i = 0; i < tilgjengeligeKunder.size(); i++) {
            rules[i] = sequence(
                    push(pop().addTarget(new Customer(tilgjengeligeKunder.get(i)))),
                    tilgjengeligeKunder.get(i) + " ");
        }
        return firstOf(rules);
    }

    Rule whiteSpace() {
        return zeroOrMore(anyOf(" \t\f"));
    }

    @Override
    protected Rule fromStringLiteral(@Nonnull String string) {
        return string.endsWith(" ") ?
                sequence(ignoreCase(string.substring(0, string.length() - 1)), whiteSpace()) :
                ignoreCase(string);
    }

    Rule And() {
        return ignoreCase("og ");
    }
}