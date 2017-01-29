package no.bekk.cv.graphsearch.parser.api_ai;

import ai.api.AIConfiguration;
import ai.api.AIDataService;
import ai.api.AIServiceException;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Result;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import no.bekk.cv.graphsearch.parser.QueryParser;
import no.bekk.cv.graphsearch.parser.domain.query.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

@Component
public class ApiAiParser implements QueryParser {

    private final AIConfiguration configuration = new AIConfiguration("28a2924f6a8644e48972c8c1a955780c");
    private final AIDataService dataService = new AIDataService(configuration);

    private static final String TECHNOLOGY_ENTITY_NAME = "Technology";
    private static final String PERSON_ENTITY_NAME = "Person";
    private static final String PROJECT_ENTITY_NAME = "Project";
    private static final String MIDDLETARGETS_ENTITY_NAME = "MiddleTarget";

    private final static String PEOPLE_INTENT_ID = "155c0acb-82c4-4b13-9010-8a46b47c946d";
    private final static String PROJECTS_INTENT_ID = "531dbca2-84dd-4f6a-b694-712fc50cad2a";
    private final static String TECHNOLOGIES_INTENT_ID = "9a82aef5-bd8e-4790-bb9a-d305addadcca";

    @Override
    public String parseQuery(String string) {
        try {
            AIResponse response = dataService.request(new AIRequest(string));
            if (!response.isError()) {
                Result result = response.getResult();
                List<Query> targets = new ArrayList<>();
                Query middleTarget = null;
                getEntities(result, TECHNOLOGY_ENTITY_NAME).stream().map(Technology::new).forEach(targets::add);
                getEntities(result, PERSON_ENTITY_NAME).stream().map(Consultant::new).forEach(targets::add);
                getEntities(result, PROJECT_ENTITY_NAME).stream().map(Customer::new).forEach(targets::add);
                if (!getEntities(result, MIDDLETARGETS_ENTITY_NAME).isEmpty()) {
                    middleTarget = Query.TECHNOLOGIES;
                }

                String searchFor = getIntent(result.getMetadata().getIntentId()).getName();

                StringBuilder start = new StringBuilder();
                StringBuilder match = new StringBuilder();

                createQuery(start, match, targets, middleTarget, searchFor);
                String retur = "return distinct " + searchFor;
                return start.append(match).append(retur).toString();
            } else {
                throw new IllegalArgumentException("Ugylding spørring");
            }
        } catch (AIServiceException e) {
            throw new IllegalArgumentException("Kunne ikke parse spørring");
        }
    }

    private Query getIntent(String intentId) {
        switch (intentId) {
            case TECHNOLOGIES_INTENT_ID:
                return Query.TECHNOLOGIES;
            case PEOPLE_INTENT_ID:
                return Query.CONSULTANTS;
            case PROJECTS_INTENT_ID:
                return Query.PROJECTS;
            default:
                throw new IllegalArgumentException("Unknown intent");
        }
    }

    private List<String> getEntities(Result result, String type) {
        Optional<JsonElement> object = Optional.ofNullable(result.getParameters().get(type));
        return object.map(element -> element.isJsonArray() ? handleArray(element) : handleSingle(element)).orElse(emptyList());
    }

    private List<String> handleSingle(JsonElement element) {
        return singletonList(element.getAsString());
    }

    private List<String> handleArray(JsonElement element) {
        return StreamSupport.stream(((JsonArray) element).spliterator(), false).map(JsonElement::getAsString).collect(toList());
    }
}
