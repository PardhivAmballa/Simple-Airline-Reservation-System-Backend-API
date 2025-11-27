package com.airline_reservation_system.persistence;

import com.airline_reservation_system.model.Flight;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class FlightRepository {
    private static final String FILE_PATH = "flights.json";

    @Autowired
    private JsonFileUtil jsonFileUtil;

    // Retrieve all flights
    public List<Flight> findAll() {
        return jsonFileUtil.readData(FILE_PATH, new TypeReference<List<Flight>>() {});
    }

    // Method to find a flight by its ID
    public Optional<Flight> findById(String flightId) {
        return findAll().stream()
                .filter(f -> f.getFlightId().equals(flightId))
                .findFirst();
    }

    // Additional methods for saving and updating flights
    public void saveAll(List<Flight> flights) {
        jsonFileUtil.writeData(FILE_PATH, flights);
    }

    // Method to get a flight by ID or return null if not found
    public Flight getFlightOrNull(String flightId) {
        return findById(flightId).orElse(null);
    }
}