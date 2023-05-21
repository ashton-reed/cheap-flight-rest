package com.ashton.reed.cheapflightrest.models.results;

import java.util.ArrayList;

public class Legs {
    public String originPlaceId;
    public String destinationPlaceId;
    public DepartureDateTime departureDateTime;
    public ArrivalDateTime arrivalDateTime;
    public int durationInMinutes;
    public int stopCount;
    public ArrayList<String> marketingCarrierIds;
    public ArrayList<String> operatingCarrierIds;
    public ArrayList<String> segmentIds;
}
