package com.ashton.reed.cheapflightrest;

import com.ashton.reed.cheapflightrest.models.Root;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class CheapFlightService {

    public HttpResponse<String> getFlightInfo(Root input) throws IOException, InterruptedException {
        // Converting (POJO)input to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(input);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://skyscanner-api.p.rapidapi.com/v3/flights/live/search/create"))
                .header("content-type", "application/json")
                .header("X-RapidAPI-Key", "f97aefd7aemsh71ad17ff2a959d1p14e65djsn82acd5d21dbb")
                .header("X-RapidAPI-Host", "skyscanner-api.p.rapidapi.com")
                .method("POST", HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    }
    public JSONArray getAllItinerariesId(JSONObject jsonObject) {
        return jsonObject.getJSONObject("content")
                .getJSONObject("results")
                .getJSONObject( "itineraries").names();
    }
    public JSONObject getItineraryById(JSONObject jsonObject, JSONArray itineraryId) {
      return jsonObject.getJSONObject("content")
                .getJSONObject("results")
                .getJSONObject( "itineraries")
                .getJSONObject((String) itineraryId.get(2));
    }
}
