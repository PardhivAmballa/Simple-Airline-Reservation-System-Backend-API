package com.airline_reservation_system.concurrency;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

// This allows us to manage locks for different flights to prevent concurrent seat booking issues

@Component
public class SeatLockManager {

    private final ConcurrentHashMap<String, ReentrantLock> locks = new ConcurrentHashMap<>();

    public ReentrantLock getLockForFlight(String flightId) {
        // Ensures only one lock per flightId is created atomically
        return locks.computeIfAbsent(flightId, id -> new ReentrantLock());
    }
}
