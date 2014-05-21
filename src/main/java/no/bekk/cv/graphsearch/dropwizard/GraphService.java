package no.bekk.cv.graphsearch.dropwizard;

import com.google.common.cache.CacheBuilderSpec;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import no.bekk.cv.graphsearch.integration.PersonRepository;
import no.bekk.cv.graphsearch.parser.GraphSearchParser;
import no.bekk.cv.graphsearch.resource.SearchResource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class GraphService extends Application<GraphConfiguration> {


    private ApplicationContext applicationContext;

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
        PersonRepository repository = applicationContext.getBean(PersonRepository.class);
        GraphSearchParser.init(repository.hentAlleFag(), repository.hentAlleKunder());
        environment.jersey().register(restResource);
        environment.jersey().setUrlPattern("/rest/*");
    }


    public static void main(String[] args) throws Exception {
        new GraphService().run(new String[]{"server", "graph-search.yml"});
    }
}
