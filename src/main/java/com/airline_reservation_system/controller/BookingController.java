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

    // 💡 FIX 1: Changed path variable from /{flight} to /{flightId}
    @PostMapping("/{flightId}")
    @PreAuthorize("hasRole('USER')")
    public Booking bookFlight(@PathVariable String flightId, Authentication authentication) {
        return bookingService.createBooking(flightId, authentication.getName());
    }

    @GetMapping("/my-bookings")
    @PreAuthorize("hasRole('USER')")
    public List<Booking> getMyBookings(Authentication authentication) {
        return bookingService.getUserBookings(authentication.getName());
    }

    // 💡 ADDITION: Admin endpoint to get all bookings
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Booking> getAllBookings() {
        return bookingService.getAllBookings();
    }

    // 💡 ADDITION: Admin endpoint to cancel/delete any booking
    @DeleteMapping("/{bookingId}")
    @PreAuthorize("hasRole('ADMIN')")
    public String cancelBooking(@PathVariable String bookingId) {
        bookingService.cancelBooking(bookingId);
        return "Booking " + bookingId + " cancelled successfully!";
    }

    @PostMapping("/cancel-request/{bookingId}")
    @PreAuthorize("hasRole('USER')")
    public String requestCancel(@PathVariable String bookingId) {
        return bookingService.requestCancellation(bookingId);
    }

    @PostMapping("/admin/cancel-all-requests")
    @PreAuthorize("hasRole('ADMIN')")
    public String cancelAllRequests() {
        return bookingService.cancelAllRequested();
    }
}