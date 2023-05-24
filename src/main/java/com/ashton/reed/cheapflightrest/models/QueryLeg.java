package com.ashton.reed.cheapflightrest.models;

public class QueryLeg {
    public OriginPlaceId originPlaceId;
    public DestinationPlaceId destinationPlaceId;
    public Date date;
}
/*
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