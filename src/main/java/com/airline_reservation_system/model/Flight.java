package com.airline_reservation_system.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "flights")
public class Flight {
    @Id
    private String flightId;
    private String airline;

    @ElementCollection
    @CollectionTable(name = "flight_routes", joinColumns = @JoinColumn(name = "flight_id"))
    @Column(name = "stop")
    @OrderColumn(name = "stop_order")
    private List<String> route;

    private int totalSeats;
    private int availableSeats;
    private double price;
    private String status;
    private String currentLocation;
}
