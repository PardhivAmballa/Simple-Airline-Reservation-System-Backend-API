package com.airline_reservation_system.service;

import com.airline_reservation_system.model.Flight;
import com.airline_reservation_system.model.Booking;
import com.airline_reservation_system.util.LogUtil;
import com.airline_reservation_system.persistence.BookingRepository;
import com.airline_reservation_system.persistence.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class FlightService {
    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private BookingRepository bookingRepository;

    // Retrieve all flights
    public List<Flight> getAllFlights() {
        return flightRepository.findAll();
    }

    // Retrieve flight by ID
    public Flight getFlightById(String id) {
        Flight flight = flightRepository.getFlightOrNull(id);
        if(flight == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Flight not found with id "+id+".");
        }
        LogUtil.activity("Found flight " + id + " successfully.");
        return flight;
    }

    // Search flights by source and destination
    public List<Flight> searchFlights(String source, String destination) {
        LogUtil.activity("Searching flights: " + source + " → " + destination);
        List<Flight> result = flightRepository.findAll().stream()
                .filter(f -> {
                    List<String> route = f.getRoute();
                    return route.contains(source)
                            && route.contains(destination)
                            && route.indexOf(source) < route.indexOf(destination);
                })
                .toList();
        if (result.isEmpty()) {LogUtil.activity("No flights found for " + source + " → " + destination);}
        else {LogUtil.activity("Found " + result.size() + " flights for " + source + " → " + destination);}
        return result;
    }

    // Add a new flight
    public Flight addFlight(Flight flight) {
        List<Flight> flights = flightRepository.findAll();
        boolean exists = flights.stream()
                .anyMatch(f -> f.getFlightId().equalsIgnoreCase(flight.getFlightId()));
        if (exists) {
            LogUtil.error("ADMIN attempted to add existing flight ID " + flight.getFlightId());
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Flight with ID " + flight.getFlightId() + " already exists."
            );
        }
        flights.add(flight);
        LogUtil.system("ADMIN added new flight " + flight.getFlightId());
        flightRepository.saveAll(flights);
        return flight;
    }

    // Update flight status and location
    public Flight updateFlightStatus(String flightId, String status, String location) {
        LogUtil.system("ADMIN attempting to update flight " + flightId);
        List<Flight> flights = flightRepository.findAll();
        Flight flight = flights.stream()
                .filter(f -> f.getFlightId().equals(flightId))
                .findFirst()
                .orElseThrow(() -> {
                    LogUtil.error("Flight status update failed: Flight not found -> " + flightId);
                    return new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Flight not found with id " + flightId + "."
                    );
                });

        // Apply updates
        boolean changed = false;
        if (status != null) {
            flight.setStatus(status);
            changed = true;
            LogUtil.activity("Flight " + flightId + " status updated to: " + status);
        }
        if (location != null) {
            flight.setCurrentLocation(location);
            changed = true;
            LogUtil.activity("Flight " + flightId + " location updated to: " + location);
        }
        if (!changed) {
            LogUtil.error("ADMIN attempted empty update on flight " + flightId);
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "No valid fields provided to update."
            );
        }
        flightRepository.saveAll(flights);
        LogUtil.system("ADMIN successfully updated flight " + flightId);
        return flight;
    }


    // Delete a flight by ID
    public void deleteFlight(String flightId) {
        List<Flight> flights = flightRepository.findAll();
        boolean removed = flights.removeIf(f -> f.getFlightId().equals(flightId));
        if (!removed) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Flight not found with id " + flightId + ".");
        }
        flightRepository.saveAll(flights);
        LogUtil.system("ADMIN deleted flight " + flightId);

        // Cancel all bookings associated with the deleted flight
        List<Booking> bookings = bookingRepository.findByFlightId(flightId);
        for(Booking b : bookings){
            b.setStatus("CANCELLED_BY_ADMIN");
        }
        bookingRepository.saveAll(bookings);
        LogUtil.activity("System auto-cancelled bookings for deleted flight " + flightId);
    }
}