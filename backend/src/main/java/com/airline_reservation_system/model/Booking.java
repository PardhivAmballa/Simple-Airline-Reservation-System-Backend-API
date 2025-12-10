package com.airline_reservation_system.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Booking {
    private String bookingId;
    private String flightId;
    private String username;
    private String status;
}