package com.ashton.reed.cheapflightrest.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@RestController
public class CheapFlightsController {

    @GetMapping(value = "/flight-data", produces = {MediaType.APPLICATION_JSON_VALUE})
    public void flights(@RequestBody FlightQu input) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://skyscanner-api.p.rapidapi.com/v3/flights/live/search/create"))
                .header("content-type", "application/json")
                .header("X-RapidAPI-Key", "f97aefd7aemsh71ad17ff2a959d1p14e65djsn82acd5d21dbb")
                .header("X-RapidAPI-Host", "skyscanner-api.p.rapidapi.com")
                .method("POST", HttpRequest.BodyPublishers.ofString("" +
                        "{\n    \"query\": {\n        \"market\": \"UK\",\n        \"locale\": \"en-GB\",\n       " +
                        " \"currency\": \"EUR\",\n        \"queryLegs\": [\n            {\n               " +
                        " \"originPlaceId\": {\n                    \"iata\": \"LHR\"\n                },\n            " +
                        "    \"destinationPlaceId\": {\n                    \"iata\": \"DXB\"\n                },\n    " +
                        "            \"date\": {\n                    \"year\": 2023,\n                    \"month\": 9,\n   " +
                        "                 \"day\": 20\n                }\n            }\n        ],\n       " +
                        "\"cabinClass\": \"CABIN_CLASS_ECONOMY\",\n        \"adults\": 2,\n        \"childrenAges\": [\n      " +
                        "      3,\n            9\n        ]\n    }\n}"))
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
    }
}
