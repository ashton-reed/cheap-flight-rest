package com.ashton.reed.cheapflightrest;

import com.ashton.reed.cheapflightrest.models.Root;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
            if (httpResponse.statusCode() == 500) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch(Exception e) {
            log.error(String.format("Error: POST request to SkyScanner API [ %s ]",e));
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
