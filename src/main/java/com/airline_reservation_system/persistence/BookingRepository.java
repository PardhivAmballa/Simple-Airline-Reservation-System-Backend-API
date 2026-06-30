package com.airline_reservation_system.persistence;

import com.airline_reservation_system.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, String> {

    // Used in FlightService.deleteFlight() to cancel bookings for a deleted flight
    List<Booking> findByFlightId(String flightId);

    // Used in BookingService.getUserBookings()
    List<Booking> findByUsername(String username);

    // Used in BookingService.getAllCancellationRequests() and cancelAllRequested()
    List<Booking> findByStatus(String status);
}