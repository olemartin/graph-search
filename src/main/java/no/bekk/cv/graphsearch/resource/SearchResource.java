package no.bekk.cv.graphsearch.resource;


import no.bekk.cv.graphsearch.integration.Repo;
import no.bekk.cv.graphsearch.query.QueryParser;
import no.bekk.cv.graphsearch.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Service
public class SearchResource {

    @Autowired
    private Repo repository;

    @Autowired
    private SearchService searchService;

    @Autowired
    private QueryParser parser;

    @GET
    @Path("database/sok")
    public Result query(@QueryParam("term") String query) {
        try {
            String cypher = parser.parseQuery(query);
            System.out.println(query);
            System.out.println(cypher);
            Iterable<String> personer = searchService.search(cypher);

            return new Result(personer, cypher);
        } catch (IllegalArgumentException e) {
            return new Result(new ArrayList<String>(), "");
        }
    }

    @GET
    @Path("populer")
    public boolean populer() {
        repository.populate();
        return true;
    }

}
