package com.airline_reservation_system.config;

import com.airline_reservation_system.model.Booking;
import com.airline_reservation_system.model.Flight;
import com.airline_reservation_system.persistence.BookingRepository;
import com.airline_reservation_system.persistence.FlightRepository;
import com.airline_reservation_system.util.LogUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Seeds the database with initial data on first startup (only when tables are empty).
 * Users are NOT seeded here — the root admin is already handled by UserService.initRootAdmin().
 */
@Component
public class DataInitializer {

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @PostConstruct
    public void seed() {
        seedFlights();
        seedBookings();
    }

    private void seedFlights() {
        if (flightRepository.count() > 0) return;

        List<Flight> flights = List.of(
                new Flight("AI01", "AirIndia", Arrays.asList("BLR", "HYD", "BOM"), 150, 145, 8500.00, "Scheduled", "BLR"),
                new Flight("AI02", "AirIndia", Arrays.asList("BLR", "CCU"), 180, 175, 5500.00, "In Air", "BLR"),
                new Flight("AI03", "AirIndia", Arrays.asList("BLR", "BOM", "CCU"), 150, 150, 9200.00, "Scheduled", "BLR"),
                new Flight("IG01", "IndiGo", Arrays.asList("BLR", "BOM"), 170, 160, 4800.00, "Scheduled", "BOM"),
                new Flight("IG02", "IndiGo", Arrays.asList("BLR", "HYD", "CCU"), 200, 200, 7800.00, "Scheduled", "BLR"),
                new Flight("IG03", "IndiGo", Arrays.asList("HYD", "BOM", "CCU"), 160, 155, 6900.00, "Delayed", "HYD")
        );

        flightRepository.saveAll(flights);
        LogUtil.system("Seeded " + flights.size() + " flights into database.");
        System.out.println("Database Initialized: " + flights.size() + " flights seeded.");
    }

    private void seedBookings() {
        if (bookingRepository.count() > 0) return;

        List<Booking> bookings = List.of(
                new Booking("3e334799-7631-4acf-85fa-e42eda24a95f", "AI01", "dk", "CONFIRMED"),
                new Booking("9312f8aa-e57c-4b7e-8a48-ab32c3d15749", "AI03", "dk", "CANCELLED"),
                new Booking("334963da-7bcd-470c-a71b-60bc3a2dd723", "AI02", "dk", "CANCELLED_BY_ADMIN"),
                new Booking("61d2fc6e-d204-4011-9598-bdee6f5cd1b7", "IG02", "dk", "CANCELLED_BY_ADMIN")
        );

        bookingRepository.saveAll(bookings);
        LogUtil.system("Seeded " + bookings.size() + " bookings into database.");
        System.out.println("Database Initialized: " + bookings.size() + " bookings seeded.");
    }
}
