package no.bekk.cv.graphsearch.parser.grappa;

import no.bekk.cv.graphsearch.graph.nodes.Fag;
import no.bekk.cv.graphsearch.graph.nodes.Person;
import no.bekk.cv.graphsearch.graph.nodes.Prosjekt;
import no.bekk.cv.graphsearch.parser.domain.*;
import no.bekk.cv.graphsearch.parser.domain.query.*;
import org.parboiled.BaseParser;
import org.parboiled.Rule;
import org.parboiled.annotations.BuildParseTree;
import org.parboiled.annotations.SuppressNode;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static no.bekk.cv.graphsearch.parser.grappa.Type.*;

@BuildParseTree
public class GraphGrammar extends BaseParser<GraphSearchQuery> {

    static List<String> fag = new ArrayList<>();
    static List<String> kunder = new ArrayList<>();
    static List<String> konsulenter = new ArrayList<>();

    public static void init(List<Fag> fags, List<Prosjekt> customers, List<Person> consultants) {
        fag = fags.stream().map(Fag::getNavn).collect(Collectors.toList());
        kunder = customers.stream().map(Prosjekt::getNavn).collect(Collectors.toList());
        konsulenter = consultants.stream().map(Person::getNavn).collect(Collectors.toList());
    }

    public Rule expression() {
        return sequence(
                start(),
                firstOf(
                        people(), projects(), technologies(ROOT)
                ),
                oneOrMore(
                        firstOf(
                                and(),
                                sequence(know(SEARCH), subjects()),
                                sequence(workedAt(SEARCH), customers()),
                                sequence(know(SEARCH), customers()),
                                sequence(know(SEARCH), technologies(MIDDLE)),
                                sequence(workedWith(), consultants()),
                                know(PARAM),
                                workedAt(PARAM)
                        )
                )
        );
    }

    Object workedWith() {
        return "med";
    }

    @SuppressNode
    Rule that() {
        return optional("som");
    }

    Rule people() {
        List<String> subjects = Arrays.asList("alle konsulenter", "alle", "konsulenter");
        return firstOf(subjects.stream().map(this::peopleSequence).toArray());
    }

    Rule projects() {
        List<String> subjects = Arrays.asList("prosjekter", "engasjement");
        return firstOf(subjects.stream().map(this::projectSequence).toArray());
    }

    Rule technologies(Type type) {
        List<String> subjects = Arrays.asList("teknologier", "teknologi", "fag");
        if (type == ROOT) {
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
        return optional("Finn");
    }

    Rule know(Type type) {
        Rule verbs = firstOf("kan", "kjenner", "programmerer", "bruker", "brukte", "brukes av", "brukt av", "har brukt");
        if (type == PARAM) {
            return sequence(
                    push(pop().setRetrieveParameters(true).addTarget(new UsedTechnology())),
                    optional(that()),
                    verbs);
        } else {
            return sequence(
                    optional(that()),
                    verbs);
        }
    }

    Rule workedAt(Type type) {
        Rule verbs = firstOf("har jobbet på", "har jobbet hos", "konsulterte", "har konsultert", "har vært hos");
        if (type == PARAM) {
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
        Rule[] rules = new Rule[fag.size()];
        for (int i = 0; i < fag.size(); i++) {
            rules[i] = sequence(
                    push(pop().addTarget(new Technology(fag.get(i)))),
                    fag.get(i));
        }
        return firstOf(rules);
    }

    Rule customers() {
        Rule[] rules = new Rule[kunder.size()];
        for (int i = 0; i < kunder.size(); i++) {
            rules[i] = sequence(
                    push(pop().addTarget(new Customer(kunder.get(i)))),
                    kunder.get(i));
        }
        return firstOf(rules);
    }

    Rule consultants() {
        Rule[] rules = new Rule[konsulenter.size()];
        for (int i = 0; i < konsulenter.size(); i++) {
            rules[i] = sequence(
                    push(pop().addTarget(new Consultant(konsulenter.get(i)))),
                    konsulenter.get(i));
        }
        return firstOf(rules);
    }


    Rule whiteSpace() {
        return zeroOrMore(anyOf(" \t\f"));
    }

    @Override
    protected Rule fromStringLiteral(@Nonnull String string) {
        return sequence(ignoreCase(string), whiteSpace());
    }

    Object and() {
        return "og";
    }
}