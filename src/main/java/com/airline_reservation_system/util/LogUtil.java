package com.airline_reservation_system.util;

import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;

public class LogUtil {

    private static final String LOG_DIR = "logs";

    static {
        new File(LOG_DIR).mkdirs();
    }

    public static void activity(String message) {
        write("activity.log", message);
    }

    public static void error(String message) {
        write("error.log", message);
    }

    public static void system(String message) {
        write("system.log", message);
    }

    private static void write(String filename, String message) {
        try (FileWriter writer = new FileWriter("logs/" + filename, true)) {
            writer.write(LocalDateTime.now() + " : " + message + "\n");
        } catch (Exception e) {
            System.out.println("Log write failed: " + e.getMessage());
        }
    }
}
