package com.airline_reservation_system.persistence;

import com.airline_reservation_system.model.Booking;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.ArrayList; // <--- ADD THIS IMPORT
import java.util.List;
import java.util.UUID;

@Repository
public class BookingRepository {
    private static final String FILE_PATH = "bookings.json";

    @Autowired
    private JsonFileUtil jsonFileUtil;

    public List<Booking> findAll() {
        return jsonFileUtil.readData(FILE_PATH, new TypeReference<List<Booking>>() {});
    }

    public Booking save(Booking booking) {
        // 💡 FIX: Create a new mutable list from the immutable list returned by findAll()
        List<Booking> bookings = new ArrayList<>(findAll());

        // Remove existing booking if updating (critical for status changes)
        if (booking.getBookingId() != null) {
            bookings.removeIf(b -> b.getBookingId().equals(booking.getBookingId()));
        } else {
            // Assign new ID only if it's a new booking
            booking.setBookingId(UUID.randomUUID().toString());
        }

        // Add the new/updated booking
        bookings.add(booking);

        jsonFileUtil.writeData(FILE_PATH, bookings);
        return booking;
    }

    public void saveAll(List<Booking> bookings) {
        // Note: For safety, the list passed here should already be a mutable copy.
        jsonFileUtil.writeData(FILE_PATH, bookings);
    }

    public Booking findById(String bookingId) {
        return findAll().stream()
                .filter(b -> b.getBookingId().equals(bookingId))
                .findFirst()
                .orElse(null);
    }

    public List<Booking> findByFlightId(String flightId) {
        return  findAll().stream()
                .filter(b -> b.getFlightId().equals(flightId))
                .toList();
    }
}