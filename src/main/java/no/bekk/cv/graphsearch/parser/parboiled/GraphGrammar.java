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
        return Sequence(
                Start(),
                FirstOf(
                        People(),
                        Projects(),
                        Technologies(true)
                ),
                OneOrMore(
                        FirstOf(
                                And(),
                                Sequence(
                                        Know(false),
                                        Subjects()
                                ),
                                Sequence(
                                        WorkedAt(false),
                                        Customers()
                                ),
                                Sequence(
                                        Know(false),
                                        Customers()
                                ),
                                Sequence(
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
        return Optional("som ");
    }

    Rule People() {
        List<String> subjects = Arrays.asList("alle konsulenter ", "alle ", "konsulenter ");
         return FirstOf(Lists.transform(subjects, new Function<String, Object>() {
            public Object apply(String input) {
                return PeopleSequence(input);
            }
        }).toArray());
    }

    Rule Projects() {
        List<String> subjects = Arrays.asList("prosjekter ", "engasjement ");
        return FirstOf(Lists.transform(subjects, new Function<String, Object>() {
            public Object apply(String input) {
                return ProjectSequence(input);
            }
        }).toArray());
    }

    Rule Technologies(boolean root) {
        List<String> subjects = Arrays.asList("teknologier ", "teknologi ", "fag ");
        if (root) {
            return FirstOf(Lists.transform(subjects, new Function<String, Object>() {
                public Object apply(String input) {
                    return TechnologySequence(input);
                }
            }).toArray());
        } else {
            return FirstOf(Lists.transform(subjects, new Function<String, Object>() {
                public Object apply(String input) {
                    return InTheMiddleSequence(input);
                }
            }).toArray());
        }
    }

    Rule InTheMiddleSequence(String name) {
        return Sequence(push(pop().addMiddleTarget(Query.TECHNOLOGIES)), name);
    }

    Rule PeopleSequence(String name) {
        return Sequence(push(new GraphSearchQuery(Query.CONSULTANTS)), name);
    }

    Rule TechnologySequence(String name) {
        return Sequence(push(new GraphSearchQuery(Query.TECHNOLOGIES)), name);
    }

    Rule ProjectSequence(String name) {
        return Sequence(push(new GraphSearchQuery(Query.PROJECTS)), name);
    }

    @SuppressNode
    Rule Start() {
        return Optional("Finn ");
    }

    Rule Know(boolean empty) {
        Rule verbs = FirstOf("kan ", "kjenner ", "programmerer ", "bruker ", "brukte ", "brukes av ", "brukt av ");
        if (empty) {
            return Sequence(
                    push(pop().setRetrieveParameters(true).addTarget(new UsedTechology())),
                    Optional(That()),
                    verbs);
        } else {
            return Sequence(
                    Optional(That()),
                    verbs);
        }
    }

    Rule WorkedAt(boolean empty) {
        Rule verbs = FirstOf("har jobbet på ", "har jobbet hos ", "konsulterte ", "har konsultert ", "har vært hos ");
        if (empty) {

            return Sequence(
                    push(pop().setRetrieveParameters(true).addTarget(new WorkedAt())),
                    Optional(That()),
                    verbs);
        } else {
            return Sequence(
                    Optional(That()),
                    verbs);
        }
    }

    Rule Subjects() {
        Rule[] rules = new Rule[tilgjengeligeFag.size()];
        for (int i = 0; i < tilgjengeligeFag.size(); i++) {
            rules[i] = Sequence(
                    push(pop().addTarget(new Technology(tilgjengeligeFag.get(i)))),
                    tilgjengeligeFag.get(i) + " ");
        }
        return FirstOf(rules);
    }

    Rule Customers() {
        Rule[] rules = new Rule[tilgjengeligeKunder.size()];
        for (int i = 0; i < tilgjengeligeKunder.size(); i++) {
            rules[i] = Sequence(
                    push(pop().addTarget(new Customer(tilgjengeligeKunder.get(i)))),
                    tilgjengeligeKunder.get(i) + " ");
        }
        return FirstOf(rules);
    }

    Rule WhiteSpace() {
        return ZeroOrMore(AnyOf(" \t\f"));
    }

    @Override
    protected Rule fromStringLiteral(String string) {
        return string.endsWith(" ") ?
                Sequence(IgnoreCase(string.substring(0, string.length() - 1)), WhiteSpace()) :
                IgnoreCase(string);
    }

    Rule And() {
        return IgnoreCase("og ");
    }
}