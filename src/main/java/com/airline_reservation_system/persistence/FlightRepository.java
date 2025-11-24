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

    public List<Flight> findAll() {
        return jsonFileUtil.readData(FILE_PATH, new TypeReference<List<Flight>>() {});
    }

    public Optional<Flight> findById(String flightId) {
        return findAll().stream()
                .filter(f -> f.getFlightId().equals(flightId))
                .findFirst();
    }

    public void saveAll(List<Flight> flights) {
        jsonFileUtil.writeData(FILE_PATH, flights);
    }

    public Flight getFlightOrNull(String flightId) {
        return findById(flightId).orElse(null);
    }
}