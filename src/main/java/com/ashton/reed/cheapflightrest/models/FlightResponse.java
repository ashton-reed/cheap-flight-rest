package com.ashton.reed.cheapflightrest.models;

import java.util.List;

// import com.fasterxml.jackson.databind.ObjectMapper; // version 2.11.1
// import com.fasterxml.jackson.annotation.JsonProperty; // version 2.11.1
/*
ObjectMapper om = new ObjectMapper();
Root root = om.readValue(myJsonString, Root.class);
*/
public class FlightResponse {
        public String sessionToken;
        public String status;
        public String action;
        public Content content;
}

class Content {
    public Results results;
    public Stats stats;
    //public SortingOptions sortingOptions;
}

class Results {
    public Itineraries itineraries;
//    public Legs legs;
//    public Segments segments;
//    public Places places;
//    public Carriers carriers;
//    public Agents agents;
//    public Alliances alliances;
}

class Stats {

}

class Itineraries {
    //Inside it has generated 13554-2309202215--32348-0-11182-2309210805 {} 3 keys
    public SomeGeneratedId someGeneratedId;
}

class SomeGeneratedId {

}

