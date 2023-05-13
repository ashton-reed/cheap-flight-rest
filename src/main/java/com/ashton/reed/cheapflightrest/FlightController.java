package com.ashton.reed.cheapflightrest;

import com.ashton.reed.cheapflightrest.models.QueryModel;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class FlightController {
    private final CheapFlightService cheapFlightService;

    @Autowired
    public FlightController(CheapFlightService cheapFlightService) {
        this.cheapFlightService = cheapFlightService;
    }

    @PostMapping(value = "/cheapest-flight-price", produces = {MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody Object flights(@RequestBody final QueryModel flightItinerary) {
        try {
            var httpResponse = cheapFlightService.getFlightInfo(flightItinerary);
            JSONArray itinerariesId = cheapFlightService.getAllItinerariesId(httpResponse.body());
            JSONObject itineraryById = cheapFlightService.getItineraryById(httpResponse.body(), itinerariesId);
            var pricingOptions = itineraryById.getJSONArray("pricingOptions");
            var something = pricingOptions.getJSONObject(0);
           return something.getJSONObject("price").get("amount");
        } catch(Exception e) {
            throw new RuntimeException(String.format("Get request failed %s", e));
        }
    }

    @GetMapping(value = "/test")
    public String testing() {
        log.info("THE GET REQUEST FOR TESTING WAS CALLED");
        return "THIS ENDPOINT WORKS";
    }
}
