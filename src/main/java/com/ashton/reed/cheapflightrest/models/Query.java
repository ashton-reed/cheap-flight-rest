package com.ashton.reed.cheapflightrest.models;

import java.util.List;
    class Date {
        public int year;
        public int month;
        public int day;
    }

    class DestinationPlaceId {
        public String iata;
    }

    class OriginPlaceId {
        public String iata;
    }

    public class Query {
        /**
         * "Market for which the search is for. e.g. `UK`.
         * SkyScanner provides a Culture API to get all the markets they support."
         */
        public String market;
        /**
         * "Locale that the results are returned in. e.g. `en-GB`(British English).
         * SkyScanner provides a Culture API to get all the locales they support."
         */
        public String locale;
        /**
         * "Currency that the search results are returned in. e.g. `GBP`.
         * SkyScanner provides a Culture API to get all the currencies they support."
         */
        public String currency;
        /**
         * "List of legs to search for."
         * minItems:1 maxItems: 6
         */
        public List<QueryLeg> queryLegs;
        /**
         * 0:"CABIN_CLASS_ECONOMY"
         * 1:"CABIN_CLASS_PREMIUM_ECONOMY"
         * 2:"CABIN_CLASS_BUSINESS"
         * 3:"CABIN_CLASS_FIRST"
         */
        public String cabinClass;
        /**
         * Number of adults. Should be greater than or equal to 1.
         */
        public int adults;
        /**
         * minItems: 0 maxItems: 8
         */
    }
/**
 * Note: Want to add a return flight add another date to the list
 * Your originPlaceId should match destinationPlaceId for your return flight
 *  "queryLegs": [
 *             {
 *                 "originPlaceId": {
 *                     "iata": "LHR"
 *                 },
 *                 "destinationPlaceId": {
 *                     "iata": "DXB"
 *                 },
 *                 "date": {
 *                     "year": 2023,
 *                     "month": 9,
 *                     "day": 20
 *                 }
 *             },
 *             {
 *                 "originPlaceId": {
 *                     "iata": "DXB"
 *                 },
 *                 "destinationPlaceId": {
 *                     "iata": "LHR"
 *                 },
 *                 "date": {
 *                     "year": 2023,
 *                     "month": 10,
 *                     "day": 15
 *                 }
 *             }
 *         ]
 *
 */
    class QueryLeg {
        public OriginPlaceId originPlaceId;
        public DestinationPlaceId destinationPlaceId;
        public Date date;
    }