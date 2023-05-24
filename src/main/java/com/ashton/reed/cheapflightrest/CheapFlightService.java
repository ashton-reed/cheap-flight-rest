package com.ashton.reed.cheapflightrest;

import com.ashton.reed.cheapflightrest.models.QueryModel;
import com.ashton.reed.cheapflightrest.models.SkyScannerModel;
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

    final static int FIRST_ELEMENT = 0;

    /**
     * @param flightItinerary flightItinerary
     * @return
     * @throws IOException          IOException
     * @throws InterruptedException InterruptedException
     * @throws ExecutionException   ExecutionException
     */
    public Object getFlightInfo(final QueryModel flightItinerary) throws IOException, InterruptedException, ExecutionException {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // Converting (POJO)input to JSON
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

        HttpResponse<String> skyScannerResponse = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        SkyScannerModel skyScannerModel = objectMapper.readValue(skyScannerResponse.body(), SkyScannerModel.class);
        String cheapestItineraryId = skyScannerModel.content.sortingOptions.cheapest.get(FIRST_ELEMENT).itineraryId;
        if (cheapestItineraryId == null) {
            //TODO: make POLL request to api and log it
            throw new RuntimeException("Unable to get cheapestItineraryId get(FIRST_ELEMENT)");
        }

        JSONObject itineraryInformation = getItineraryById(skyScannerResponse.body(), cheapestItineraryId);
        var cheapPrice = itineraryInformation.getJSONObject("price").get("amount");
        JSONObject cheapPriceLink = itineraryInformation.getJSONArray("items").getJSONObject(0);

        return cheapPriceLink.get("deepLink");
    }

    /**
     *
     * @param httpResponseBody response from SkyScanner API
     * @param itineraryId cheapest itineraryId
     * @return first element from pricingOptions
     */
    public JSONObject getItineraryById(final String httpResponseBody, final String itineraryId) {
         try {
            JSONObject jsonObject = new JSONObject(httpResponseBody);
            JSONObject itineraryById = jsonObject.getJSONObject("content")
                .getJSONObject("results")
                .getJSONObject("itineraries")
                .getJSONObject(itineraryId);
            JSONArray pricingOptions = itineraryById.getJSONArray("pricingOptions");
            return pricingOptions.getJSONObject(0);
         } catch (Exception e) {
             throw new RuntimeException(String.format("Error getting itinerary based on ID given %s", e));
         }
    }
}
