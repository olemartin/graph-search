package no.bekk.cv.graphsearch.parser.parboiled;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import no.bekk.cv.graphsearch.graph.nodes.Fag;
import no.bekk.cv.graphsearch.graph.nodes.Prosjekt;
import no.bekk.cv.graphsearch.parser.domain.*;
import org.parboiled.BaseParser;
import org.parboiled.Rule;
import org.parboiled.annotations.BuildParseTree;
import org.parboiled.annotations.SuppressNode;

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

    public Rule Expression() {
        return sequence(
                Start(),
                firstOf(
                        People(),
                        Projects(),
                        Technologies(true)
                ),
                oneOrMore(
                        firstOf(
                                And(),
                                sequence(
                                        Know(false),
                                        Subjects()
                                ),
                                sequence(
                                        WorkedAt(false),
                                        Customers()
                                ),
                                sequence(
                                        Know(false),
                                        Customers()
                                ),
                                sequence(
                                        Know(false),
                                        Technologies(false)
                                ),
                                Know(true),
                                WorkedAt(true)
                        )
                )
        );
    }

    @SuppressNode
    Rule That() {
        return optional("som ");
    }

    Rule People() {
        List<String> subjects = Arrays.asList("alle konsulenter ", "alle ", "konsulenter ");
        return firstOf(Lists.transform(subjects, new Function<String, Object>() {
            public Object apply(String input) {
                return PeopleSequence(input);
            }
        }).toArray());
    }

    Rule Projects() {
        List<String> subjects = Arrays.asList("prosjekter ", "engasjement ");
        return firstOf(Lists.transform(subjects, new Function<String, Object>() {
            public Object apply(String input) {
                return ProjectSequence(input);
            }
        }).toArray());
    }

    Rule Technologies(boolean root) {
        List<String> subjects = Arrays.asList("teknologier ", "teknologi ", "fag ");
        if (root) {
            return firstOf(Lists.transform(subjects, new Function<String, Object>() {
                public Object apply(String input) {
                    return TechnologySequence(input);
                }
            }).toArray());
        } else {
            return firstOf(Lists.transform(subjects, new Function<String, Object>() {
                public Object apply(String input) {
                    return InTheMiddleSequence(input);
                }
            }).toArray());
        }
    }

    Rule InTheMiddleSequence(String name) {
        return sequence(push(pop().addMiddleTarget(Query.TECHNOLOGIES)), name);
    }

    Rule PeopleSequence(String name) {
        return sequence(push(new GraphSearchQuery(Query.CONSULTANTS)), name);
    }

    Rule TechnologySequence(String name) {
        return sequence(push(new GraphSearchQuery(Query.TECHNOLOGIES)), name);
    }

    Rule ProjectSequence(String name) {
        return sequence(push(new GraphSearchQuery(Query.PROJECTS)), name);
    }

    @SuppressNode
    Rule Start() {
        return optional("Finn ");
    }

    Rule Know(boolean empty) {
        Rule verbs = firstOf("kan ", "kjenner ", "programmerer ", "bruker ", "brukte ", "brukes av ", "brukt av ");
        if (empty) {
            return sequence(
                    push(pop().setRetrieveParameters(true).addTarget(new UsedTechology())),
                    optional(That()),
                    verbs);
        } else {
            return sequence(
                    optional(That()),
                    verbs);
        }
    }

    Rule WorkedAt(boolean empty) {
        Rule verbs = firstOf("har jobbet på ", "har jobbet hos ", "konsulterte ", "har konsultert ", "har vært hos ");
        if (empty) {

            return sequence(
                    push(pop().setRetrieveParameters(true).addTarget(new WorkedAt())),
                    optional(That()),
                    verbs);
        } else {
            return sequence(
                    optional(That()),
                    verbs);
        }
    }

    Rule Subjects() {
        Rule[] rules = new Rule[tilgjengeligeFag.size()];
        for (int i = 0; i < tilgjengeligeFag.size(); i++) {
            rules[i] = sequence(
                    push(pop().addTarget(new Technology(tilgjengeligeFag.get(i)))),
                    tilgjengeligeFag.get(i) + " ");
        }
        return firstOf(rules);
    }

    Rule Customers() {
        Rule[] rules = new Rule[tilgjengeligeKunder.size()];
        for (int i = 0; i < tilgjengeligeKunder.size(); i++) {
            rules[i] = sequence(
                    push(pop().addTarget(new Customer(tilgjengeligeKunder.get(i)))),
                    tilgjengeligeKunder.get(i) + " ");
        }
        return firstOf(rules);
    }

    Rule WhiteSpace() {
        return zeroOrMore(anyOf(" \t\f"));
    }

    @Override
    protected Rule fromStringLiteral(String string) {
        return string.endsWith(" ") ?
                sequence(ignoreCase(string.substring(0, string.length() - 1)), WhiteSpace()) :
                ignoreCase(string);
    }

    Rule And() {
        return ignoreCase("og ");
    }
}