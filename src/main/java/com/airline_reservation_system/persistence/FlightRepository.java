package com.airline_reservation_system.persistence;

import com.airline_reservation_system.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightRepository extends JpaRepository<Flight, String> {

    // Derived query: replaces manual findAll() + stream existence check in FlightService.addFlight()
    boolean existsByFlightIdIgnoreCase(String flightId);
}