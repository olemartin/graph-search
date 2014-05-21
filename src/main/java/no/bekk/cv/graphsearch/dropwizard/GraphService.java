package no.bekk.cv.graphsearch.dropwizard;

import com.google.common.cache.CacheBuilderSpec;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import no.bekk.cv.graphsearch.integration.GraphSearchRepository;
import no.bekk.cv.graphsearch.parser.parboiled.GraphGrammar;
import no.bekk.cv.graphsearch.resource.SearchResource;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class GraphService extends Application<GraphConfiguration> {

    @Override
    public void initialize(Bootstrap<GraphConfiguration> bootstrap) {
        CacheBuilderSpec.disableCaching();
        bootstrap.addBundle(new AssetsBundle("/assets/", "/", "index.html"));
    }

    @Override
    public void run(GraphConfiguration graphConfiguration, Environment environment) throws Exception {

        AnnotationConfigApplicationContext applicationContext =
                new AnnotationConfigApplicationContext();

        applicationContext.register(SpringConfiguration.class);
        applicationContext.refresh();

        SearchResource restResource = applicationContext.getBean(SearchResource.class);
        GraphSearchRepository repository = applicationContext.getBean(GraphSearchRepository.class);
        GraphGrammar.init(repository.hentAlleFag(), repository.hentAlleKunder());
        environment.jersey().register(restResource);
        environment.jersey().setUrlPattern("/rest/*");
    }

    public static void main(String[] args) throws Exception {
        new GraphService().run(new String[]{"server", "graph-search.yml"});
    }
}
