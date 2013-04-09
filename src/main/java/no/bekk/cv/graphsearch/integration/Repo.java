package no.bekk.cv.graphsearch.integration;

import no.bekk.cv.graphsearch.graph.nodes.Fag;
import no.bekk.cv.graphsearch.graph.nodes.Firma;
import no.bekk.cv.graphsearch.graph.nodes.Person;
import no.bekk.cv.graphsearch.graph.nodes.Prosjekt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class Repo {

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private Neo4jTemplate template;

    @Transactional
    public void populate() {
        Person olemartin = new Person("Ole-Martin Mørk", "Scientist");
        Person vegardH = new Person("Vegard Hartmann", "Manager");
        Person kristoffer = new Person("Kristoffer Dyrkorn", "Scientist");
        Person andreas = new Person("Andreas Heim", "Seniorkonsulent");
        Person vegardS = new Person("Vegard Skjefstad", "Manager");

        Firma raadet = new Firma("Norges Forskningsråd");
        Firma nav = new Firma("NAV");
        Firma svv = new Firma("Statens Vegvesen");

        Prosjekt mod = new Prosjekt("Modernisering");
        Prosjekt raadForvaltning = new Prosjekt("Norges Forskningsråd");
        Prosjekt navForvaltning = new Prosjekt("NAV");
        Prosjekt svvForvaltning = new Prosjekt("Statens Vegvesen");

        Fag java = new Fag("Java");
        Fag node = new Fag("Node.js");
        Fag neo4J = new Fag("Neo4J");
        Fag redis = new Fag("Redis");
        Fag elasticSearch = new Fag("Elastic Search");
        Fag solr = new Fag("SOLR");
        Fag puppet = new Fag("Puppet");
        Fag bash = new Fag("Bash");
        Fag jQuery = new Fag("JQuery");


        olemartin.jobbetI(mod, raadForvaltning).og().kan(java, node, neo4J, redis, solr, jQuery);
        vegardH.jobbetI(mod, navForvaltning).kan(java);
        kristoffer.jobbetI(navForvaltning, svvForvaltning).og().kan(java, elasticSearch, solr);
        andreas.jobbetI(mod, svvForvaltning).og().kan(java, bash, puppet);
        vegardS.jobbetI(raadForvaltning).og().kan(java, jQuery);

        raadForvaltning.brukte(java, solr, jQuery);
        mod.brukte(java, jQuery);
        navForvaltning.brukte(solr);
        svvForvaltning.brukte(elasticSearch, puppet);

        raadet.haddeProsjekt(raadForvaltning);
        nav.haddeProsjekt(navForvaltning, mod);
        svv.haddeProsjekt(svvForvaltning);


        save(olemartin, kristoffer, andreas, vegardS);
        save(raadet, nav, svv);
        save(mod, raadForvaltning, navForvaltning, svvForvaltning);
        save(java, node, neo4J, redis, elasticSearch, solr, puppet, bash, jQuery);
    }

    private void save(Object... objekter) {
        for (Object objekt : objekter) {
            template.save(objekt);
        }
    }
}
