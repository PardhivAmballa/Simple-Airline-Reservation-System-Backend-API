package com.airline_reservation_system.controller;

import com.airline_reservation_system.model.Booking;
import com.airline_reservation_system.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    // User endpoint to book a flight
    @PostMapping("/{flightId}")
    @PreAuthorize("hasRole('USER')")
    public String bookFlight(@PathVariable String flightId, Authentication authentication) {
        return bookingService.submitAsyncBooking(flightId, authentication.getName());
    }

    // User endpoint to get their own bookin
    @GetMapping("/my-bookings")
    @PreAuthorize("hasRole('USER')")
    public List<Booking> getMyBookings(Authentication authentication) {
        return bookingService.getUserBookings(authentication.getName());
    }

    // Admin endpoint to get all bookings
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Booking> getAllBookings() {
        return bookingService.getAllBookings();
    }

    //  Admin endpoint to cancel/delete any booking
    @DeleteMapping("/{bookingId}")
    @PreAuthorize("hasRole('ADMIN')")
    public String cancelBooking(@PathVariable String bookingId) {
        bookingService.cancelBooking(bookingId);
        return "Booking " + bookingId + " cancelled successfully!";
    }

    // User endpoint to request cancellation of their own booking
    @PostMapping("/cancel-request/{bookingId}")
    @PreAuthorize("hasRole('USER')")
    public String requestCancel(@PathVariable String bookingId) {
        return bookingService.requestCancellation(bookingId);
    }

    // Admin endpoint to cancel all requested cancellations
    @PostMapping("/admin/cancel-all-requests")
    @PreAuthorize("hasRole('ADMIN')")
    public String cancelAllRequests() {
        return bookingService.cancelAllRequested();
    }

    // Admin endpoint to view all cancellation requests
    @GetMapping("/admin/cancel-requests")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Booking> getAllCancelRequests() {
        return bookingService.getAllCancellationRequests();
    }

    // Admin endpoint to cancel a single requested cancellation
    @PostMapping("/admin/cancel-request/{bookingId}")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminCancelSingleRequest(@PathVariable String bookingId) {
        return bookingService.adminCancelBooking(bookingId);
    }
}