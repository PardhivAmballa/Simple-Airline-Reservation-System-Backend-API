package com.airline_reservation_system.model;

public class AsyncBookingRequest {

    private String flightId;
    private String username;

    public AsyncBookingRequest() {}

    public AsyncBookingRequest(String flightId, String username) {
        this.flightId = flightId;
        this.username = username;
    }

    public String getFlightId() {
        return flightId;
    }

    public void setFlightId(String flightId) {
        this.flightId = flightId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
