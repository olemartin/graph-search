package no.bekk.cv.graphsearch.dropwizard;

import com.google.common.cache.CacheBuilderSpec;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.assets.AssetsBundle;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import no.bekk.cv.graphsearch.resource.SearchResource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class GraphService extends Service<GraphConfiguration> {


    private ApplicationContext applicationContext;

    @Override
    public void initialize(Bootstrap<GraphConfiguration> bootstrap) {
        CacheBuilderSpec.disableCaching();
        bootstrap.setName("graph");
        bootstrap.addBundle(new AssetsBundle("/assets/", "/", "index.html"));
    }

    @Override
    public void run(GraphConfiguration graphConfiguration, Environment environment) throws Exception {
        applicationContext = new ClassPathXmlApplicationContext("config/applicationContext.xml");
        SearchResource restResource = (SearchResource) applicationContext.getBean("searchResource");
        environment.addResource(restResource);
    }


    public static void main(String[] args) throws Exception {
        new GraphService().run(new String[]{"server", "graph-search.yml"});
    }
}
