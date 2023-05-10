package com.ashton.reed.cheapflightrest;

import com.ashton.reed.cheapflightrest.models.Root;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Map;

@Slf4j
@RestController
public class CheapFlightsController {
    private final CheapFlightService cheapFlightService;

    @Autowired
    public CheapFlightsController(CheapFlightService cheapFlightService) {
        this.cheapFlightService = cheapFlightService;
    }

    @PostMapping(value = "/flight-query", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public void flights(@RequestBody Root input) {
        try {
            var httpResponse = cheapFlightService.getFlightInfo(input);
            JSONObject jsonObject = new JSONObject(httpResponse.body().toString());
            JSONArray itinerariesId = cheapFlightService.getAllItinerariesId(jsonObject);
            JSONObject itineraryById = cheapFlightService.getItineraryById(jsonObject,itinerariesId);
            var pricingOptions = itineraryById.get("pricingOptions");

            /**
             * TODO: figure out how to convert JSONObject to array and print certain elements
             */

            ObjectMapper objectMapper = new ObjectMapper();

            String requestBody = objectMapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValuesAsArray(pricingOptions);
            System.out.println(requestBody.);
        } catch(Exception e) {
            log.error(String.format("Error: POST request to SkyScanner API [ %s ]",e));
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
