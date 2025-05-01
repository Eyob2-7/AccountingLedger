package com.pluralsight;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

public class Transaction {
    private LocalDate date;
    private String time;
    private String description;
    private String vendor;
    private double amount;

    // Constructor with parameters
    public Transaction(LocalDate date, String time, String description, String vendor, double amount) {
        this.date = date;
        this.time = time;
        this.description = description;
        this.vendor = vendor;
        this.amount = amount;
    }

    // Provides a comparator to sort transactions by date and time, newest first
    public static Comparator<Transaction> sortByNewestDateTime() {
        return new Comparator<Transaction>() {

            // Override the compare method to define custom sorting logic
            @Override
            public int compare(Transaction t1, Transaction t2) {
                // Define the format of date and time used in the CSV file
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                // Combine and parse the first transaction's date and time into LocalDateTime
                LocalDateTime dt1 = LocalDateTime.parse(t1.getDate() + " " + t1.getTime(), formatter);

                // Combine and parse the second transaction's date and time into LocalDateTime
                LocalDateTime dt2 = LocalDateTime.parse(t2.getDate() + " " + t2.getTime(), formatter);

                // Compare dt2 to dt1 so the most recent transaction appears first
                return dt2.compareTo(dt1);
            }
        };
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String toString() {
        return date + "|" + time + "|" + description + "|" + vendor + "|" + amount;
    }
}
