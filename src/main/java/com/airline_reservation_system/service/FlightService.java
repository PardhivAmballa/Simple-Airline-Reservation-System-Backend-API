package com.airline_reservation_system.service;

import com.airline_reservation_system.model.Flight;
import com.airline_reservation_system.model.Booking;
import com.airline_reservation_system.persistence.BookingRepository;
import com.airline_reservation_system.persistence.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlightService {
    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private BookingRepository bookingRepository;

    public List<Flight> getAllFlights() {
        return flightRepository.findAll();
    }

    public Flight getFlightById(String id) {
        Flight flight = flightRepository.getFlightOrNull(id);
        if(flight == null){
            throw new RuntimeException("Flight not found!"+id);
        }
        return flight;
    }

    public List<Flight> searchFlights(String source, String destination) {
        return flightRepository.findAll().stream()
                .filter(f -> {
                    List<String> route = f.getRoute();
                    return route.contains(source) && route.contains(destination)
                            && route.indexOf(source) < route.indexOf(destination);
                })
                .collect(Collectors.toList());
    }

    public Flight addFlight(Flight flight) {
        List<Flight> flights = flightRepository.findAll();
        flights.add(flight);
        flightRepository.saveAll(flights);
        return flight;
    }

    public Flight updateFlightStatus(String flightId, String status,String location) {
        List<Flight> flights = flightRepository.findAll();
        Flight flight = flights.stream()
                .filter(f -> f.getFlightId().equals(flightId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Flight not found!"));
        if(status!=null){flight.setStatus(status);}
        if(location!=null){flight.setCurrentLocation(location);}
        flight.setCurrentLocation(location);
        flightRepository.saveAll(flights);
        return flight;
    }

    public void deleteFlight(String flightId) {
        List<Flight> flights = flightRepository.findAll();
        flights.removeIf(f -> f.getFlightId().equals(flightId));
        flightRepository.saveAll(flights);
        List<Booking> bookings = bookingRepository.findByFlightId(flightId);
        for(Booking b : bookings){
            b.setStatus("CANCELLED_BY_ADMIN");
        }
        bookingRepository.saveAll(bookings);
    }
}