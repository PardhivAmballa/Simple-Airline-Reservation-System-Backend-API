package com.airline_reservation_system.service;

import com.airline_reservation_system.async.BookingProcessor;
import com.airline_reservation_system.model.AsyncBookingRequest;
import com.airline_reservation_system.model.Booking;
import com.airline_reservation_system.model.Flight;
import com.airline_reservation_system.persistence.BookingRepository;
import com.airline_reservation_system.persistence.FlightRepository;
import com.airline_reservation_system.util.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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

    @Lazy
    @Autowired
    private BookingProcessor bookingProcessor;

    // USER creates booking
    public Booking createBooking(String flightId, String username) {
        LogUtil.activity("User " + username + " is attempting to book flight " + flightId);

        // Thread-safe block to prevent race conditions while booking seats
        synchronized (flightId.intern()) {
            Flight flight = flightRepository.findById(flightId)
                    .orElseThrow(() -> {
                        LogUtil.error("Booking failed: Flight not found " + flightId);
                        return new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Flight not found with id " + flightId + ".");
                    });

            if (flight.getAvailableSeats() <= 0) {
                LogUtil.error("Booking failed: No seats available on flight " + flightId);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No seats available!");
            }

            // Reduce seat count & save flight
            flight.setAvailableSeats(flight.getAvailableSeats() - 1);
            flightRepository.save(flight);

            Booking booking = new Booking(null, flightId, username, "CONFIRMED");
            LogUtil.activity("Booking CONFIRMED for user " + username + " on flight " + flightId);
            return bookingRepository.save(booking);
        }
    }

    // public method that controller will call
    public String submitAsyncBooking(String flightId, String username) {

        // Validate before queueing async job
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> {
                    LogUtil.error("Async booking failed: Flight not found " + flightId);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Flight not found with id " + flightId + ".");
                });

        if (flight.getAvailableSeats() <= 0) {
            LogUtil.error("Async booking failed: No seats available on " + flightId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No seats available!");
        }

        // Queue background booking
        bookingProcessor.submit(new AsyncBookingRequest(flightId, username));
        LogUtil.activity("Async booking queued for user " + username + " on flight " + flightId);

        return "Your booking request is being processed. Check your bookings shortly.";
    }

    // internal background processing
    public void processBookingInBackground(AsyncBookingRequest req) {
        createBooking(req.getFlightId(), req.getUsername());
    }

    // Get bookings belonging to a user
    public List<Booking> getUserBookings(String username) {
        return bookingRepository.findByUsername(username);
    }

    // Return all bookings (ADMIN)
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    // ADMIN cancels a booking by ID
    public void cancelBooking(String bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Booking not found with id " + bookingId + "."));

        booking.setStatus("CANCELLED");
        LogUtil.system("Admin cancelled booking " + bookingId);
        bookingRepository.save(booking);
    }

    // USER requests cancellation
    public String requestCancellation(String bookingId) {

        Booking booking = bookingRepository.findById(bookingId).orElse(null);

        if (booking == null) {
            LogUtil.error("Cancellation request failed: Booking " + bookingId + " not found.");
            return "Booking not found!";
        }
        if (booking.getStatus().startsWith("CANCELLED")) {
            LogUtil.activity("Cancellation request ignored — booking already cancelled: " + bookingId);
            return "Booking already cancelled!";
        }

        booking.setStatus("CANCEL_REQUESTED");
        bookingRepository.save(booking);
        LogUtil.activity("User requested cancellation for booking " + bookingId);
        return "Cancellation request submitted.";
    }

    // ADMIN cancels specific booking
    public String adminCancelBooking(String bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElse(null);

        if (booking == null) {
            return "Booking not found!";
        }
        booking.setStatus("CANCELLED_BY_ADMIN");
        bookingRepository.save(booking);
        LogUtil.system("Admin approved and cancelled booking (REQUESTED): " + bookingId);
        return "Booking cancelled successfully.";
    }

    // ADMIN cancels all requested cancellations
    public String cancelAllRequested() {
        List<Booking> requested = bookingRepository.findByStatus("CANCEL_REQUESTED");
        int count = requested.size();

        for (Booking b : requested) {
            b.setStatus("CANCELLED_BY_ADMIN");
        }

        bookingRepository.saveAll(requested);
        LogUtil.system("Admin cancelled " + count + " pending cancellation requests.");
        return count + " requested bookings cancelled.";
    }

    // ADMIN views all cancellation requests
    public List<Booking> getAllCancellationRequests() {
        LogUtil.system("ADMIN viewed all cancellation requests");
        return bookingRepository.findByStatus("CANCEL_REQUESTED");
    }
}