package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class TransactionService {

    // Create a static File object representing the transactions CSV file
    public static String myFile = "src/main/resources/transactions.csv";

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

        LocalDate dateStamp = getCurrentDateStamp();
        String timeStamp = getCurrentTimeStamp();

        // Create a new Transaction object
        Transaction deposit = new Transaction(dateStamp, timeStamp, description, vendor, amount);

        // Write the deposit to the file
        try (BufferedWriter myWriter = new BufferedWriter(new FileWriter(myFile, true))) {
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

        LocalDate dateStamp = getCurrentDateStamp();
        String timeStamp = getCurrentTimeStamp();

        // Create a new transaction object
        Transaction payment = new Transaction(dateStamp, timeStamp, description, vendor, amount);

        // Save to CSV file
        try (BufferedWriter myWriter = new BufferedWriter(new FileWriter(myFile, true))) {
            myWriter.write(payment.toString());
            myWriter.newLine();
            return "\nPayment saved successfully!\n";
        } catch (IOException e) {
            return "Error saving payment: " + e.getMessage();
        }
    }

    // Displays the full ledger by reading all transactions from the CSV file
    public static void displayLedger() {
        boolean inLedger = true;
        while (inLedger) {
            System.out.println("\nLedger...");
            UserServices.DisplayLedgerScreen();
            String choice = UserServices.getScanner().nextLine().trim().toUpperCase();

            ArrayList<Transaction> allTransactions = TransactionService.readTransactions(myFile);

            switch (choice) {
                case "A":
                    System.out.println("Showing All Transactions...");
                    if (allTransactions.isEmpty()) {
                        System.out.println("No transactions found.");
                    } else {
                        for (Transaction t : allTransactions) {
                            System.out.println(t);
                        }
                    }
                    break;

                case "D":
                    System.out.println("Showing Only Deposits...");
                    ArrayList<Transaction> deposits = new ArrayList<>();

                    for (Transaction t : allTransactions) {
                        if (t.getAmount() > 0) {
                            deposits.add(t);
                        }
                    }
                    if (deposits.isEmpty()) {
                        System.out.println("No deposit transactions found.");
                    } else {
                        for (Transaction d : deposits) {
                            System.out.println(d);
                        }
                    }
                    break;
                case "P":
                    System.out.println("Showing only Payments...");
                    ArrayList<Transaction> payments = new ArrayList<>();
                    for (Transaction t : allTransactions) {
                        if (t.getAmount() < 0) {
                            payments.add(t);
                        }
                    }
                    if (payments.isEmpty()) {
                        System.out.println("No payment transactions found.");
                    } else {
                        for (Transaction p : payments) {
                            System.out.println(p);
                        }
                    }
                    break;
                case "R":
                    System.out.println("Reports Screen...");
                    UserServices.displayReportsScreen();
                    int reportChoice = UserServices.getScanner().nextInt();
                    UserServices.getScanner().nextLine();
                    break;
                case "H":
                    System.out.println("Returning to Home Screen...");
                    inLedger = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }

    //Returns the current date as a formatted string (yyyy-MM-dd)
    public static LocalDate getCurrentDateStamp() {
        return LocalDate.now();
    }

    //Returns the current time as a formatted string (HH:mm:ss) without nanoseconds
    public static String getCurrentTimeStamp() {
        return LocalTime.now().withNano(0).toString();
    }

    //method to get a file reader by passing in the file name for a file in src/main/resources
    public static BufferedReader getFileReader(String fileName) {
        try {
            FileReader reader = new FileReader(fileName);
            return new BufferedReader(reader);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<Transaction> readTransactions(String fileName) {
        ArrayList<Transaction> transactions = new ArrayList<>();
        try (BufferedReader reader = getFileReader(myFile)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("date")) {
                    continue;
                }
                String[] parts = line.split("\\|");
                LocalDate date = LocalDate.parse(parts[0].trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                String time = parts[1].trim();
                String description = parts[2].trim();
                String vendor = parts[3].trim();
                double amount = Double.parseDouble(parts[4].trim());
                transactions.add((new Transaction(date, time, description, vendor, amount)));
            }
            transactions.sort(Transaction.sortByNewestDateTime());

        } catch (Exception e) {
            System.out.println("Error reading payments:" + e.getMessage());
        }
        return transactions;
    }
}
