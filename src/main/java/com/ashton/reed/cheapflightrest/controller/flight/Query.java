package com.ashton.reed.cheapflightrest.controller.flight;

import java.util.List;

    class Date{
        public int year;
        public int month;
        public int day;
    }

    class DestinationPlaceId{
        public String iata;
    }

    class OriginPlaceId{
        public String iata;
    }

    public class FlightQuery{
        public String market;
        public String locale;
        public String currency;
        public List<QueryLeg> queryLegs;
        public String cabinClass;
        public int adults;
        public List<Integer> childrenAges;
    }

    class QueryLeg{
        public OriginPlaceId originPlaceId;
        public DestinationPlaceId destinationPlaceId;
        public Date date;
    }

    class Root {
        public FlightQuery query;
    }