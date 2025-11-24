package com.airline_reservation_system.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Flight {
    private String flightId;
    private String airline;
    private List<String> route;
    private int totalSeats;
    private int availableSeats;
    private double price;
    private String status;
    private String currentLocation;
}
