package com.airline_reservation_system.controller;

import com.airline_reservation_system.model.Flight;
import com.airline_reservation_system.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.util.List;

@RestController
@RequestMapping("/api/flights")
public class FlightController {
    @Autowired
    private FlightService flightService;

    // Get All Flights (Accessible by USER and ADMIN)
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<Flight> getAllFlights() {
        return flightService.getAllFlights();
    }

    // Search Flights (Accessible by USER and ADMIN)
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<Flight> searchFlights(@RequestParam String source, @RequestParam String destination) {
        return flightService.searchFlights(source, destination);
    }

    // Get Flight by ID (Accessible by USER and ADMIN)
    @GetMapping("/{flightId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Flight getFlightById(@PathVariable String flightId) {
        return flightService.getFlightById(flightId);
    }

    // Admin only can add new flights
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Flight addFlight(@RequestBody Flight flight) {
        return flightService.addFlight(flight);
    }

    // Admin only can update flight status and location
    @PutMapping("/{flightId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public Flight updateFlightStatus(
            @PathVariable String flightId,
            @RequestBody Flight statusUpdate) {

        // Use the service method to update status and location
        return flightService.updateFlightStatus(
                flightId,
                statusUpdate.getStatus(),
                statusUpdate.getCurrentLocation()
        );
    }

    // Admin only can delete flights
    @DeleteMapping("/{flightId}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteFlight(@PathVariable String flightId) {
        flightService.deleteFlight(flightId);
        return "Flight with ID " + flightId + " has been deleted.";
    }
}