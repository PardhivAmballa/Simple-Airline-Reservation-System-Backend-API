package com.airline_reservation_system.service;

import com.airline_reservation_system.model.Booking;
import com.airline_reservation_system.model.Flight;
import com.airline_reservation_system.persistence.BookingRepository;
import com.airline_reservation_system.persistence.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.ArrayList; // Import required for mutability safety in FlightService logic

@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private FlightRepository flightRepository;

    // 💡 FIX 1: Corrected method parameter from filightId to flightId
    public Booking createBooking(String flightId, String username) {
        List<Flight> flights = flightRepository.findAll();

        Flight flight = flights.stream()
                // 💡 FIX 2: Corrected variable name in filter
                .filter(f -> f.getFlightId().equals(flightId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Flight not found!"));

        if (flight.getAvailableSeats() <= 0) {
            throw new RuntimeException("No seats available!");
        }

        // Decrement seat and save flights (FlightRepository saveAll will handle persistence)
        flight.setAvailableSeats(flight.getAvailableSeats()-1);

        // This saveAll MUST write the entire list back to flights.json
        flightRepository.saveAll(flights);

        // 💡 FIX 3: Corrected variable name when creating new Booking object
        Booking booking = new Booking(null, flightId, username, "CONFIRMED");
        return bookingRepository.save(booking);
    }

    public List<Booking> getUserBookings(String username) {
        return bookingRepository.findAll().stream()
                .filter(b -> b.getUsername().equals(username))
                .toList();
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public void cancelBooking(String bookingId) {
        // Must ensure findAll() returns a mutable list, or wrap it here
        List<Booking> bookings = bookingRepository.findAll();

        Booking booking = bookings.stream()
                .filter(b -> b.getBookingId().equals(bookingId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Booking not found!"));

        if (booking.getStatus().equals("CANCELLED")) {
            throw new RuntimeException("Booking already cancelled!");
        }

        booking.setStatus("CANCELLED");

        // Logic to increment available seats (assuming FlightService/Repository handles flight update)
        // [Missing logic to increment seats on the flight]

        bookingRepository.saveAll(bookings);
    }

    public String requestCancellation(String bookingId) {
        Booking booking = bookingRepository.findById(bookingId);
        if(booking == null) {
            return "Booking not found!";
        }
        booking.setStatus("CANCEL_REQUESTED");
        bookingRepository.save(booking);
        return "Cancellation request submitted.";
    }

    public String adminCancelBooking(String bookingId) {
        Booking booking = bookingRepository.findById(bookingId);
        if(booking == null) {
            return "Booking not found!";
        }
        booking.setStatus("CANCELLED_BY_ADMIN");
        bookingRepository.save(booking);
        return "Booking cancelled successfully.";
    }

    public String cancelAllRequested() {
        List<Booking> bookings = bookingRepository.findAll();
        int count = 0;
        for(Booking b : bookings) {
            if("CANCEL_REQUESTED".equals(b.getStatus())) {
                b.setStatus("CANCELLED_BY_ADMIN");
                count++;
            }
        }
        bookingRepository.saveAll(bookings);
        return count + " requested bookings cancelled.";
    }
}