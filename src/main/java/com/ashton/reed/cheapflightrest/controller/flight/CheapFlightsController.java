package com.ashton.reed.cheapflightrest.controller.flight;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@RestController
public class CheapFlightsController {

    @PostMapping(value = "/flight-query", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public void flights(@RequestBody Root input) throws IOException, InterruptedException {
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
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
    }
}
