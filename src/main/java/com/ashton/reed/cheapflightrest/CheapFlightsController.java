package com.ashton.reed.cheapflightrest;

import com.ashton.reed.cheapflightrest.models.Root;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
    public Map<String, Object> flights(@RequestBody Root input) {
        try {
            var httpResponse = cheapFlightService.getFlightInfo(input);
            JSONObject jsonObject = new JSONObject(httpResponse.body().toString());
            JSONArray itineraryId = cheapFlightService.getAllItinerariesId(jsonObject);
            JSONObject itinerariesById = cheapFlightService.getItineraryById(jsonObject,itineraryId);
            System.out.println(itinerariesById);

            return jsonObject.toMap();
        } catch(Exception e) {
            log.error(String.format("Error: POST request to SkyScanner API [ %s ]",e));
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
