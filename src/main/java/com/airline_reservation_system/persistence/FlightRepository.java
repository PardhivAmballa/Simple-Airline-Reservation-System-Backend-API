package com.airline_reservation_system.persistence;

import com.airline_reservation_system.model.Flight;
import com.airline_reservation_system.util.LogUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class FlightRepository {
    private static final String FILE_PATH = "flights.json";

    @Autowired
    private JsonFileUtil jsonFileUtil;

    // Retrieve all flights
    public List<Flight> findAll() {
        LogUtil.system("Reading all flights");
        return jsonFileUtil.readData(FILE_PATH, new TypeReference<List<Flight>>() {});
    }

    // Method to find a flight by its ID
    public Optional<Flight> findById(String flightId) {
        LogUtil.system("Finding flight with ID: " + flightId);
        return findAll().stream()
                .filter(f -> f.getFlightId().equals(flightId))
                .findFirst();
    }

    // Save/update a single flight
    public Flight save(Flight flight) {
        synchronized (this) {
            LogUtil.activity("Saving single flight: " + flight.getFlightId());
            List<Flight> flights = findAll();
            // Remove existing flight if updating (ensures uniqueness)
            flights.removeIf(f -> f.getFlightId().equals(flight.getFlightId()));
            // Add the new/updated flight
            flights.add(flight);
            jsonFileUtil.writeData(FILE_PATH, flights);
            return flight;
        }
    }

    // Additional methods for saving and updating flights
    public void saveAll(List<Flight> flights) {
        LogUtil.system("Saving all flights (" + flights.size() + ")");
        jsonFileUtil.writeData(FILE_PATH, flights);
    }

    // Method to get a flight by ID or return null if not found
    public Flight getFlightOrNull(String flightId) {
        return findById(flightId).orElse(null);
    }
}