package com.pluralsight;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

public class TransactionService {

    public static String addDeposit() {

        // Create a File object representing the transactions CSV file
        String file = "src/main/resources/transactions.csv";

        // Print this before asking user the inputs
        System.out.println("\nAdding Deposit...");

        // Print a separator for clean UI
        UserServices.printSeparator("Add Deposit");

        // Ask user for deposit information
        String description = UserServices.question("Enter description:");

        // Ask user for vendor information
        String vendor = UserServices.question("Enter vendor:");

        // Ask user for amount
        double amount = UserServices.questionDouble("Enter amount:");
        String dateStamp = getCurrentDateStamp();
        String timeStamp = getCurrentTimeStamp();

        // Create a new Transaction object
        Transaction deposit = new Transaction(dateStamp, timeStamp, description, vendor, amount);

         // Write the deposit to the file
        try (BufferedWriter myWriter = new BufferedWriter(new FileWriter(file, true))) {
            myWriter.write(deposit.toString());
            myWriter.newLine();
            return "\nDeposit saved successfully!\n";
        } catch (IOException e) {
            return "Error saving deposit: " + e.getMessage();
        }
    }

    //Returns the current date as a formatted string (yyyy-MM-dd)
    public static String getCurrentDateStamp() {
        return LocalDate.now().toString();
    }

    //Returns the current time as a formatted string (HH:mm:ss) without nanoseconds
    public static String getCurrentTimeStamp() {
        return LocalTime.now().withNano(0).toString();
    }
}
