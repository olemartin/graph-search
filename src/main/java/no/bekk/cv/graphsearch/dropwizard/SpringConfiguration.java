package no.bekk.cv.graphsearch.dropwizard;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableNeo4jRepositories(basePackages = "no.bekk.cv.graphsearch.integration")
@ComponentScan(basePackages = "no.bekk.cv.graphsearch")
@EnableTransactionManagement
public class SpringConfiguration extends Neo4jConfiguration {

    public SpringConfiguration() {
        setBasePackage("no.bekk.cv.graphsearch.graph");
    }

    @Bean(destroyMethod = "shutdown")
    public GraphDatabaseService graphDatabaseService() {
        return new GraphDatabaseFactory().newEmbeddedDatabase("data/graph.db");
    }
}