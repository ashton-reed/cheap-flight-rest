package com.ashton.reed.cheapflightrest.controller.flights;

import com.ashton.reed.cheapflightrest.models.Root;
import com.ashton.reed.cheapflightrest.service.CheapFlightService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class CheapFlightsController {
    private final CheapFlightService cheapFlightService;

    @Autowired
    public CheapFlightsController(CheapFlightService cheapFlightService) {
        this.cheapFlightService = cheapFlightService;
    }

    @PostMapping(value = "/flight-query", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public void flights(@RequestBody Root input) {
        try {
            var httpResponse = cheapFlightService.getFlightInfo(input);
            System.out.println(httpResponse.body());
            if (httpResponse.statusCode() != 200) {
                System.out.println("aya it worked");
            }
        } catch(Exception e) {
            log.error("Error: When calling getFlightInfo() ",e);
        }
    }
}
