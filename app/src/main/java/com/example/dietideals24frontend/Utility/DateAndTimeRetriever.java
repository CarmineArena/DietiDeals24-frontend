package com.example.dietideals24frontend.Utility;

import android.annotation.SuppressLint;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateAndTimeRetriever {
    public static String getCurrentDate() {
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return date.format(formatter);
    }

    public static String getCurrentTime() {
        LocalTime time = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSSSSS");
        return time.format(formatter);
    }

    @SuppressLint("DefaultLocale")
    public static String formatTime(long seconds) {
        long hours   = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long sec     = seconds % 60;

        return String.format("%02d ore %02d minuti %02d secondi", hours, minutes, sec);
    }
}