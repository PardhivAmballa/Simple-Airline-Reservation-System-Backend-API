package com.airline_reservation_system.service;

import com.airline_reservation_system.model.Flight;
import com.airline_reservation_system.model.Booking;
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
        return flight;
    }

    // Search flights by source and destination
    public List<Flight> searchFlights(String source, String destination) {
        return flightRepository.findAll().stream()
                .filter(f -> {
                    List<String> route = f.getRoute();
                    return route.contains(source)
                            && route.contains(destination)
                            && route.indexOf(source) < route.indexOf(destination);
                })
                .toList();
    }

    // Add a new flight
    public Flight addFlight(Flight flight) {
        List<Flight> flights = flightRepository.findAll();
        boolean exists = flights.stream()
                .anyMatch(f -> f.getFlightId().equalsIgnoreCase(flight.getFlightId()));
        if (exists) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Flight with ID " + flight.getFlightId() + " already exists."
            );
        }
        flights.add(flight);
        flightRepository.saveAll(flights);
        return flight;
    }

    // Update flight status and location
    public Flight updateFlightStatus(String flightId, String status,String location) {
        List<Flight> flights = flightRepository.findAll();
        Flight flight = flights.stream()
                .filter(f -> f.getFlightId().equals(flightId))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Flight not found with id" + flightId+"."));
        if(status!=null){flight.setStatus(status);}
        if(location!=null){flight.setCurrentLocation(location);}
        flightRepository.saveAll(flights);
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

        // Cancel all bookings associated with the deleted flight
        List<Booking> bookings = bookingRepository.findByFlightId(flightId);
        for(Booking b : bookings){
            b.setStatus("CANCELLED_BY_ADMIN");
        }
        bookingRepository.saveAll(bookings);
    }
}