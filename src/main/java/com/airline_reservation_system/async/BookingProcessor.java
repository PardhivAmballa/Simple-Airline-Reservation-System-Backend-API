package com.airline_reservation_system.async;

import com.airline_reservation_system.model.AsyncBookingRequest;
import com.airline_reservation_system.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class BookingProcessor {

    private ExecutorService executor;

    @Autowired
    private BookingService bookingService;

    @PostConstruct
    public void init() {
        // 10 background worker threads — safe and scalable
        executor = Executors.newFixedThreadPool(10);
    }

    public void submit(AsyncBookingRequest request) {
        executor.submit(() -> {
            try {
                bookingService.processBookingInBackground(request);
            } catch (Exception e) {
                System.err.println("Async booking failed: " + e.getMessage());
            }
        });
    }
}
