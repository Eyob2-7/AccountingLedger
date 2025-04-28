package com.pluralsight;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

public class TransactionService {

    // Handles user input for adding a deposit and saves as a positive amount to the CSV file
    public static String addDeposit() {

        // Print this before asking user the inputs
        System.out.println("\nAdding Deposit...");

        // Print a separator for clean UI
        UserServices.printSeparator("Add Deposit");

        // Ask user for deposit information
        String description = UserServices.question("Enter description:");
        if (UserServices.inputValidator(description)) {
            System.out.println("Description can not be empty!");
            return "Deposit failed: description was empty";
        }

        // Ask user for vendor information
        String vendor = UserServices.question("Enter vendor:");
        if (UserServices.inputValidator(vendor)) {
            System.out.println("Vendor can not be empty!");
            return "Deposit failed: vendor was empty";
        }

        // Ask user for amount
        double amount = UserServices.questionDouble("Enter amount:");
        amount = Math.abs(amount);//force positive for deposit
        if (UserServices.isZero(amount)) {
            System.out.println("Amount can not be empty!");
            return "Deposit failed: please enter amount";
        }

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

    // Handles user input for making a payment and saves as a negative amount to the CSV file
    public static String makePayment() {

        System.out.println("\nMake Payment (Debit)...");
        UserServices.printSeparator("Make payment(Debit)");

        // Ask for user input
        String description = UserServices.question("Enter description:");
        if (UserServices.inputValidator(description)) {
            System.out.println("Description can not be empty!");
            return "Deposit failed: description was empty";
        }

        String vendor = UserServices.question("Enter vendor:");
        if (UserServices.inputValidator(vendor)) {
            System.out.println("Vendor can not be empty!");
            return "Deposit failed: vendor was empty";
        }


        double amount = UserServices.questionDouble("Enter amount:");
        amount = -Math.abs(amount);//force negative for debit
        if (UserServices.isZero(amount)) {
            System.out.println("Amount can not be empty!");
            return "Deposit failed: please enter amount";
        }

        String dateStamp = getCurrentDateStamp();
        String timeStamp = getCurrentTimeStamp();

        // Create a new transaction object
        Transaction payment = new Transaction(dateStamp, timeStamp, description, vendor, amount);

        // Save to CSV file
        try (BufferedWriter myWriter = new BufferedWriter(new FileWriter(file, true))) {
            myWriter.write(payment.toString());
            myWriter.newLine();
            return "\nPayment saved successfully!\n";
        } catch (IOException e) {
            return "Error saving payment: " + e.getMessage();
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

    // Create a static File object representing the transactions CSV file
    public static String file = "src/main/resources/transactions.csv";
}
