package com.ashton.reed.cheapflightrest;

import com.ashton.reed.cheapflightrest.models.QueryModel;
import com.ashton.reed.cheapflightrest.models.ResponseModel;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutionException;

@Service
public class CheapFlightService {

    public void getFlightInfo(QueryModel flightItinerary) throws IOException, InterruptedException, ExecutionException {
        // Converting (POJO)input to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        String requestBody = objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(flightItinerary);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://skyscanner-api.p.rapidapi.com/v3/flights/live/search/create"))
                .header("content-type", "application/json")
                .header("X-RapidAPI-Key", "f97aefd7aemsh71ad17ff2a959d1p14e65djsn82acd5d21dbb")
                .header("X-RapidAPI-Host", "skyscanner-api.p.rapidapi.com")
                .method("POST", HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        var response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        var responseModel = objectMapper.readValue(response.body(), ResponseModel.class);

        System.out.println(responseModel.content.sortingOptions.cheapest.get(0).itineraryId);
        System.out.println("You made it here");
    }

    /**
     * Using JSONObjects to traverse JSON because we do
     * not know the Itineraries ID's at runtime when they are generated
     * @param httpResponseBody Flight Query
     * @return all Itineraries ID's from
     */
    public JSONArray getAllItinerariesId(final String httpResponseBody) {
        try {
            JSONObject jsonObject = new JSONObject(httpResponseBody);
            return jsonObject.getJSONObject("content")
                    .getJSONObject("results")
                    .getJSONObject("itineraries").names();
        } catch (Exception e) {
            throw new RuntimeException(String.format("Error getting ALL Itineraries ID's %s", e));
        }
    }

    /**
     * @param httpResponseBody Flight Query
     * @param itinerariesId All itinerary ID's
     * @return Itinerary based on ID
     */
    public Object getItineraryById(final String httpResponseBody, final JSONArray itinerariesId) {
         try {
            JSONObject jsonObject = new JSONObject(httpResponseBody);
            JSONObject itineraryById = jsonObject.getJSONObject("content")
                .getJSONObject("results")
                .getJSONObject("itineraries")
                .getJSONObject((String) itinerariesId.get(1)); //TODO: pass through cheapest ID, this is hardCoded
            var pricingOptions = itineraryById.getJSONArray("pricingOptions");
            var something = pricingOptions.getJSONObject(0);
            return something.getJSONObject("price").get("amount");
         } catch (Exception e) {
             throw new RuntimeException(String.format("Error getting itinerary based on ID given %s", e));
         }
    }
}
