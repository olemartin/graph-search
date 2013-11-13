package no.bekk.cv.graphsearch.parser;

import no.bekk.cv.graphsearch.graph.nodes.Fag;
import no.bekk.cv.graphsearch.graph.nodes.Prosjekt;
import org.parboiled.BaseParser;
import org.parboiled.Rule;
import org.parboiled.annotations.BuildParseTree;
import org.parboiled.annotations.SuppressNode;

import java.util.ArrayList;
import java.util.List;

@BuildParseTree
public class GraphSearchParser extends BaseParser<Query> {


    static List<String> tilgjengeligeFag = new ArrayList<>();
    static List<String> tilgjengeligeKunder = new ArrayList<>();

    public static void init(Iterable<Fag> fags, Iterable<Prosjekt> customers) {
        for (Fag fag : fags) {
            tilgjengeligeFag.add(fag.getNavn());
        }
        for (Prosjekt customer : customers) {
            tilgjengeligeKunder.add(customer.getNavn());
        }
    }

    public Rule Expression() {
        return Sequence(
                Start(),
                FirstOf(
                        People(),
                        Projects(),
                        Technologies()
                ),
                Whitepace(),
                OneOrMore(
                        FirstOf(
                                Sequence(
                                        Optional(That()),
                                        Optional(Whitepace()),
                                        Know(),
                                        Whitepace(),
                                        Targets()
                                ),
                                Sequence(
                                        Optional(That()),
                                        Optional(Whitepace()),
                                        WorkedAt(),
                                        Whitepace(),
                                        Customers()
                                ),
                                Sequence(
                                        Whitepace(),
                                        And(),
                                        Whitepace()
                                ),
                                Sequence(
                                        Optional(That()),
                                        Optional(Whitepace()),
                                        Know(),
                                        Whitepace(),
                                        Customers()
                                )
                        )
                )
        );
    }


    @SuppressNode
    Rule That() {
        return Optional("som");
    }

    Rule People() {
        return FirstOf(
                PeopleSequence("alle konsulenter"),
                PeopleSequence("alle"),
                PeopleSequence("konsulenter"));
    }

    Rule PeopleSequence(String name) {
        return Sequence(push(Query.CONSULTANTS), name);
    }

    Rule Projects() {
        return FirstOf(
                ProjectSequence("prosjekter"),
                ProjectSequence("engasjement"));
    }

    Rule Technologies() {
        return FirstOf(
                TechnologySequence("teknologier"),
                TechnologySequence("teknologi"),
                TechnologySequence("fag"));
    }

    Rule TechnologySequence(String name) {
        return Sequence(push(Query.TECHNOLOGIES), name);
    }

    Rule ProjectSequence(String name) {
        return Sequence(push(Query.PROJECTS), name);
    }

    @SuppressNode
    Rule Start() {
        return Optional(Sequence(String("Finn"), Whitepace()));
    }

    Rule Know() {
        return FirstOf("kan", "kjenner", "programmerer", "bruker", "brukes av", "brukt av");
    }

    Rule WorkedAt() {
        return FirstOf("har jobbet på", "har jobbet hos", "konsulterte", "har konsultert", "har vært hos");
    }

    Rule Targets() {
        Rule[] rules = new Rule[tilgjengeligeFag.size()];
        for (int i = 0; i < tilgjengeligeFag.size(); i++) {
            rules[i] = Sequence(push(new Technology(tilgjengeligeFag.get(i))), tilgjengeligeFag.get(i));
        }
        return FirstOf(rules);
    }

    Rule Customers() {
        Rule[] rules = new Rule[tilgjengeligeKunder.size()];
        for (int i = 0; i < tilgjengeligeKunder.size(); i++) {
            rules[i] = Sequence(push(new Customer(tilgjengeligeKunder.get(i))), tilgjengeligeKunder.get(i));
        }
        return FirstOf(rules);
    }

    @SuppressNode
    Rule Whitepace() {
        return String(" ");
    }

    Rule And() {
        return String("og");
    }
}
