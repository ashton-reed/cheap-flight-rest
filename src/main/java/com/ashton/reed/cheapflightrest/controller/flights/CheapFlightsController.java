package com.ashton.reed.cheapflightrest.controller.flights;

import com.ashton.reed.cheapflightrest.models.Root;
import com.ashton.reed.cheapflightrest.service.CheapFlightService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private static String RESULT_STATUS_INCOMPLETE = "RESULT_STATUS_INCOMPLETE";

    @Autowired
    public CheapFlightsController(CheapFlightService cheapFlightService) {
        this.cheapFlightService = cheapFlightService;
    }

    @PostMapping(value = "/flight-query", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public void flights(@RequestBody Root input) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            var httpResponse = cheapFlightService.postFlightInfo(input);
            JsonNode node = mapper.readTree(httpResponse.body());
            String status = node.get("status").asText();

            if (httpResponse.statusCode() != 200) {
                throw new ResponseStatusException(HttpStatus.BAD_GATEWAY," There was an error");
            }
            if (status.equals(RESULT_STATUS_INCOMPLETE)) {
                log.info(String.format("Current status.... %s",status));
                var response = cheapFlightService.getFlightInfoPoll();
                JsonNode pollNode = mapper.readTree(response.body());
                String pollStatus = pollNode.get("status").asText();
                while (pollStatus.equals(RESULT_STATUS_INCOMPLETE)) {
                    log.info(String.format("Current POLL status.... %s",pollStatus));
                    cheapFlightService.getFlightInfoPoll();
                    pollNode = mapper.readTree(response.body());
                    pollStatus = pollNode.get("status").asText();
                }
                log.info("STATUS COMPLETE!");
            }

        } catch(Exception e) {
            log.error("Error: When calling getFlightInfo() ",e);
        }
    }
}
