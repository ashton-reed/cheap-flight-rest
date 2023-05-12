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

    @PostMapping(value = "/flight-price", produces = {MediaType.APPLICATION_JSON_VALUE})
    public void flights(@RequestBody final QueryModel flightItinerary) {
        try {
            var httpResponse = cheapFlightService.getFlightInfo(flightItinerary);
            JSONArray itinerariesId = cheapFlightService.getAllItinerariesId(httpResponse.body().toString());
            JSONObject itineraryById = cheapFlightService.getItineraryById(httpResponse.body().toString(), itinerariesId);
            var pricingOptions = itineraryById.getJSONArray("pricingOptions");
            var something = pricingOptions.getJSONObject(0);
            System.out.println("this is the price!!! \t\t"+something.getJSONObject("price").get("amount"));
        } catch(Exception e) {
            throw new RuntimeException(String.format("Get request failed %s", e));
        }
    }
}
