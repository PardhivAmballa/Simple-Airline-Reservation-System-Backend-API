package com.airline_reservation_system.service;

import com.airline_reservation_system.model.Booking;
import com.airline_reservation_system.model.Flight;
import com.airline_reservation_system.persistence.BookingRepository;
import com.airline_reservation_system.persistence.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private FlightRepository flightRepository;

    // USER creates booking
    public Booking createBooking(String flightId, String username) {

        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Flight not found with id" + flightId+"."));

        if (flight.getAvailableSeats() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No seats available!");
        }

        // reduce seat count & save flight list
        List<Flight> flights = flightRepository.findAll();
        flights.stream()
                .filter(f -> f.getFlightId().equals(flightId))
                .forEach(f -> f.setAvailableSeats(f.getAvailableSeats() - 1));

        flightRepository.saveAll(flights);

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

    // ADMIN cancels by ID
    public void cancelBooking(String bookingId) {
        Booking booking = bookingRepository.findAll().stream()
                .filter(b -> b.getBookingId().equals(bookingId))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Booking not found with id " + bookingId+"."));

        booking.setStatus("CANCELLED");
        bookingRepository.save(booking);
    }

    // USER requests cancellation
    public String requestCancellation(String bookingId) {

        Booking booking = bookingRepository.findAll().stream()
                .filter(b -> b.getBookingId().equals(bookingId))
                .findFirst()
                .orElse(null);

        if (booking == null) {
            return "Booking not found!";
        }

        if (booking.getStatus().startsWith("CANCELLED")) {
            return "Booking already cancelled!";
        }

        booking.setStatus("CANCEL_REQUESTED");
        bookingRepository.save(booking);

        return "Cancellation request submitted.";
    }

    // ADMIN cancels specific booking
    public String adminCancelBooking(String bookingId) {

        Booking booking = bookingRepository.findAll().stream()
                .filter(b -> b.getBookingId().equals(bookingId))
                .findFirst()
                .orElse(null);

        if (booking == null) {
            return "Booking not found!";
        }

        booking.setStatus("CANCELLED_BY_ADMIN");
        bookingRepository.save(booking);

        return "Booking cancelled successfully.";
    }

    // ADMIN cancels ALL pending requests
    public String cancelAllRequested() {

        List<Booking> bookings = bookingRepository.findAll();
        int count = 0;

        for (Booking b : bookings) {
            if ("CANCEL_REQUESTED".equals(b.getStatus())) {
                b.setStatus("CANCELLED_BY_ADMIN");
                count++;
            }
        }

        bookingRepository.saveAll(bookings);
        return count + " requested bookings cancelled.";
    }

    public List<Booking> getAllCancellationRequests() {
        return bookingRepository.findAll().stream()
                .filter(b -> "CANCEL_REQUESTED".equals(b.getStatus()))
                .toList();
    }
}
