package com.ashton.reed.cheapflightrest.models;

import java.util.List;

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
