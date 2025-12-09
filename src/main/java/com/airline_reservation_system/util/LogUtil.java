package com.airline_reservation_system.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogUtil {

    private static final String LOG_DIR = "logs";
    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSSSSS");

    // ANSI Colors
    private static final String GREEN = "\u001B[32m";   // INFO
    private static final String BLUE = "\u001B[34m";    // SYSTEM
    private static final String RED = "\u001B[31m";     // ERROR
    private static final String RESET = "\u001B[0m";

    private static void writeLog(String level, String color, String message) {
        try {
            File dir = new File(LOG_DIR);
            if (!dir.exists()) dir.mkdirs();

            String fileName = "log-" + LocalDateTime.now().toLocalDate() + ".txt";
            File file = new File(dir, fileName);

            FileWriter fw = new FileWriter(file, true);

            String timestamp = LocalDateTime.now().format(formatter);

            // Write to FILE (no color codes in file)
            fw.write(timestamp + " | " + level + " | " + message + System.lineSeparator());
            fw.close();

            // Write to CONSOLE with colors
            System.out.println(color + timestamp + " | " + level + " | " + message + RESET);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void activity(String message) {
        writeLog("INFO", GREEN, message);
    }

    public static void error(String message) {
        writeLog("ERROR", RED, message);
    }

    public static void system(String message) {
        writeLog("SYSTEM", BLUE, message);
    }
}