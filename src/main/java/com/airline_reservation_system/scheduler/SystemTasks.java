package com.airline_reservation_system.scheduler;

import com.airline_reservation_system.model.Booking;
import com.airline_reservation_system.persistence.BookingRepository;
import com.airline_reservation_system.util.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class SystemTasks {

    @Autowired
    private BookingRepository bookingRepository;

    // CLEAN LOGS OLDER THAN 2 DAYS
    // Runs every midnight
    @Scheduled(cron = "0 0 0 * * *")
    public void cleanOldLogs() {
        File logDir = new File("logs");
        if (!logDir.exists()) return;

        long cutoff = System.currentTimeMillis() - (2L * 24 * 60 * 60 * 1000); // 2 days (tune it as needed)

        File[] files = logDir.listFiles();
        if (files == null) return; // safety check
        for (File f : files) {
            if (f.lastModified() < cutoff) {
                if (f.delete()) {
                    LogUtil.system("Deleted old log file: " + f.getName());
                } else {
                    LogUtil.error("Failed to delete log file: " + f.getName());
                }
            }
        }
    }

    // NOTIFY ADMIN OF PENDING CANCELLATION REQUESTS
    @Scheduled(cron = "0 */30 * * * *") // every 30 minutes
    public void notifyCancellationRequests() {
        long count = bookingRepository.findAll().stream()
                .filter(b -> "CANCEL_REQUESTED".equals(b.getStatus()))
                .count();
        if (count > 0) {
            LogUtil.system("ADMIN ALERT: " + count + " cancellation requests pending.");
        }
    }
}
