package com.ashton.reed.cheapflightrest;

import com.ashton.reed.cheapflightrest.models.*;
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
import java.util.ArrayList;

@Service
public class CheapFlightService {

    final static int FIRST_ELEMENT = 0;

    /**
     * @param originPlaceId originPlaceId
     * @param destinationPlaceId destinationPlaceId
     * @param date date
     * @return Link to cheap flight
     * @throws IOException IOException
     * @throws InterruptedException InterruptedException
     */
    public Object getFlightInfo(final String originPlaceId,
                                final String destinationPlaceId,
                                final int date) throws IOException, InterruptedException {

        QueryModel model = setMarketUS(originPlaceId,destinationPlaceId,date);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // Converting (POJO)input to JSON
        String requestBody = objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(model);

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

    /**
     *
     * @param originPlaceId originPlaceId
     * @param destinationPlaceId destinationPlaceId
     * @param monthDayYear monthDayYear
     * @return model
     */
    public QueryModel setMarketUS(final String originPlaceId,
                                  final String destinationPlaceId,
                                  final int monthDayYear) {
        Query query = new Query();
        QueryModel queryModel = new QueryModel();
        QueryLeg queryLeg = new QueryLeg();
        OriginPlaceId originPlaceIdObj = new OriginPlaceId();
        DestinationPlaceId destinationPlaceIdObj = new DestinationPlaceId();

        query.queryLegs = new ArrayList<>();
        query.market = "US";
        query.locale = "en-US";
        query.currency = "USD";
        query.cabinClass = "CABIN_CLASS_ECONOMY";
        query.adults = 1;

        originPlaceIdObj.iata = originPlaceId;
        destinationPlaceIdObj.iata = destinationPlaceId;
        queryLeg.date = convertDate(monthDayYear);

        queryLeg.originPlaceId = originPlaceIdObj;
        queryLeg.destinationPlaceId = destinationPlaceIdObj;

        query.queryLegs.add(queryLeg);
        queryModel.query = query;

        return queryModel;
    }

    /**
     * @param monthDayYear 03232023 monthDayYear
     * @return date
     */
    public Date convertDate(final int monthDayYear) {
        Date date = new Date();
        String number = String.valueOf(monthDayYear);
        var day = Integer.parseInt(number.substring(0,2));
        var month = Integer.parseInt(number.substring(2,4));
        var year = Integer.parseInt(number.substring(4,8));

        date.day = day;
        date.month = month;
        date.year = year;

        return date;
    }
}
