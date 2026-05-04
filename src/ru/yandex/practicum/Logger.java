package ru.yandex.practicum;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public final class Logger {
    private static final File logFile = new File("wordle.log");
    private static PrintWriter writer = null;

    public static void setWriter(PrintWriter newWriter) {
        writer = newWriter;
    }

    public static void log(String message) {
        String timestamp = LocalDateTime.now().toString();
        String payload = timestamp + ": " + message;

        if (writer != null) {
            writer.println(payload);
            writer.flush();
            return;
        }

        try (PrintWriter logger = new PrintWriter(new FileWriter(logFile, StandardCharsets.UTF_8, true), true)) {
            logger.println(payload);
        } catch (IOException e) {
            System.out.println("Лог-файл: " +  e.getMessage());
        }
    }
}
