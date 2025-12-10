package com.airline_reservation_system.persistence;

import com.airline_reservation_system.model.Booking;
import com.airline_reservation_system.util.LogUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class BookingRepository {
    private static final String FILE_PATH = "bookings.json";

    @Autowired
    private JsonFileUtil jsonFileUtil;

    // Retrieve all bookings
    public List<Booking> findAll() {
        LogUtil.system("Loading all bookings");
        return jsonFileUtil.readData(FILE_PATH, new TypeReference<List<Booking>>() {});
    }

    // Save or update a booking
    public Booking save(Booking booking) {
        List<Booking> bookings = new ArrayList<>(findAll());
        // Remove existing booking if updating (critical for status changes)
        if (booking.getBookingId() != null) {
            bookings.removeIf(b -> b.getBookingId().equals(booking.getBookingId()));
            LogUtil.activity("Updating booking: " + booking.getBookingId());
        } else {
            booking.setBookingId(UUID.randomUUID().toString());
            LogUtil.activity("Creating new booking: " + booking.getBookingId());
        }

        // Add the new/updated booking
        bookings.add(booking);
        jsonFileUtil.writeData(FILE_PATH, bookings);
        return booking;
    }

    // Save or update multiple bookings
    public void saveAll(List<Booking> bookings) {
        LogUtil.system("Saving all bookings (" + bookings.size() + ")");
        jsonFileUtil.writeData(FILE_PATH, bookings);
    }

    // Find a booking by its ID
    public Booking findById(String bookingId) {
        LogUtil.system("Finding booking by ID: " + bookingId);
        return findAll().stream()
                .filter(b -> b.getBookingId().equals(bookingId))
                .findFirst()
                .orElse(null);
    }

    // Find bookings by flight ID
    public List<Booking> findByFlightId(String flightId) {
        LogUtil.system("Finding bookings by flight ID: " + flightId);
        return  findAll().stream()
                .filter(b -> b.getFlightId().equals(flightId))
                .toList();
    }
}