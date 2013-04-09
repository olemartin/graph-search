package no.bekk.cv.graphsearch.resource;


import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import no.bekk.cv.graphsearch.graph.nodes.Fag;
import no.bekk.cv.graphsearch.graph.nodes.Person;
import no.bekk.cv.graphsearch.integration.PersonRepository;
import no.bekk.cv.graphsearch.integration.Repo;
import no.bekk.cv.graphsearch.query.TextualQueryParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.conversion.EndResult;
import org.springframework.stereotype.Service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Service
public class SearchResource {

    @Autowired
    private Repo repository;

    @Autowired
    private PersonRepository personRepository;

    @GET
    @Path("persons/{person}/fag")
    public Iterable<Fag> getRelations(@PathParam("person") String person) {
        Iterable<Fag> fag = personRepository.hentFagSomPersonenKan(person);
        for (Fag fag1 : fag) {
            System.out.println(fag1.getNavn());
        }
        return fag;
    }


    @GET
    @Path("database/sok")
    public Result query(@QueryParam("term") String query) {

        String cypher = new TextualQueryParser().parseQuery(query);
        System.out.println(query);
        System.out.println(cypher);
        EndResult<Person> personer =
                personRepository.query(cypher, new HashMap<String, Object>());

        Iterable<String> navn = Iterables.transform(personer, new Function<Person, String>() {
            public String apply(Person person) {
                return person.getNavn();
            }
        });
        return new Result(navn, cypher);
    }

    @GET
    @Path("populer")
    public boolean populer() {
        repository.populate();
        return true;
    }

}
