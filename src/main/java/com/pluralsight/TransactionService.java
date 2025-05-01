package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class TransactionService {

    // File path for storing transactions
    public static String myFile = "src/main/resources/transactions.csv";
    public static int currentYear = Utility.getCurrentDateStamp().getYear();

    // Adds a deposit and saves to CSV
    public static String addDeposit() {

        // Print this before asking user the inputs
        System.out.println("\nAdding Deposit...");

        // Print a separator for clean UI
        UserServices.printSeparator("Add Deposit");

        // Ask user for deposit information
        String description = UserServices.question("Enter description:");
        if (Utility.inputValidator(description)) {
            System.out.println("Description can not be empty!");
            return "Deposit failed: description was empty";
        }

        // Ask user for vendor information
        String vendor = UserServices.question("Enter vendor:");
        if (Utility.inputValidator(vendor)) {
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

        // Create a new Transaction object
        Transaction deposit = new Transaction(Utility.getCurrentDateStamp(), Utility.getCurrentTimeStamp(), description, vendor, amount);

        // Write the deposit to the file
        try (BufferedWriter myWriter = new BufferedWriter(new FileWriter(myFile, true))) {
            myWriter.write(deposit.toString());
            myWriter.newLine();
            return "\nDeposit saved\n";
        } catch (IOException e) {
            return "Error saving deposit: " + e.getMessage();
        }
    }

    // Adds a payment and saves to CSV
    public static String makePayment() {

        System.out.println("\nMake Payment (Debit)...");
        UserServices.printSeparator("Make payment(Debit)");

        // Ask for user input
        String description = UserServices.question("Enter description:");
        if (Utility.inputValidator(description)) {
            System.out.println("Description can not be empty!");
            return "Payment failed: description was empty";
        }

        String vendor = UserServices.question("Enter vendor:");
        if (Utility.inputValidator(vendor)) {
            System.out.println("Vendor can not be empty!");
            return "Payment failed: vendor was empty";
        }


        double amount = UserServices.questionDouble("Enter amount:");
        amount = -Math.abs(amount);//force negative for debit
        if (UserServices.isZero(amount)) {
            System.out.println("Amount can not be empty!");
            return "Payment failed: please enter amount";
        }

        // Create a new transaction object
        Transaction payment = new Transaction(Utility.getCurrentDateStamp(), Utility.getCurrentTimeStamp(), description, vendor, amount);

        // Save to CSV file
        try (BufferedWriter myWriter = new BufferedWriter(new FileWriter(myFile, true))) {
            myWriter.write(payment.toString());
            myWriter.newLine();
            return "\nPayment saved\n";
        } catch (IOException e) {
            return "Error saving payment: " + e.getMessage();
        }
    }

    // Displays the main Ledger screen and handles user menu options
    public static void displayLedger() {
        boolean inLedger = true;
        while (inLedger) {
            System.out.println("\nLedger...");
            UserServices.DisplayLedgerScreen();
            String choice = UserServices.getScanner().nextLine().trim().toUpperCase();

            // Read all transactions from file
            ArrayList<Transaction> allTransactions = TransactionService.readTransactions(myFile);

            switch (choice) {
                case "A":
                    showAllTransactions(allTransactions);
                    break;

                case "D":
                    showOnlyDeposits(allTransactions);
                    break;
                case "P":
                    showOnlyPayments(allTransactions);
                    break;
                case "R":
                    boolean inReports = true;
                    while (inReports) {
                        System.out.println("Reports Screen...");
                        UserServices.displayReportsScreen();
                        int reportChoice = UserServices.getScanner().nextInt();
                        UserServices.getScanner().nextLine();
                        switch (reportChoice) {
                            case 1:
                                monthToDateReport(allTransactions);
                                break;
                            case 2:
                                previousMonthReport(allTransactions);
                                break;
                            case 3:
                                yearToDateReport(allTransactions);
                                break;
                            case 4:
                                previousYearReport(allTransactions);
                                break;
                            case 5:
                                searchByVendorReport(allTransactions);
                                break;
                            case 0:
                                System.out.println("Returning to Ledger Screen...");
                                inReports = false;
                                break;
                        }
                    }
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

    // Read all transactions from the CSV
    public static ArrayList<Transaction> readTransactions(String fileName) {
        ArrayList<Transaction> transactions = new ArrayList<>();
        try (BufferedReader reader = Utility.getFileReader(myFile)) {
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
            // Sort transactions after reading
            transactions.sort(Transaction.sortByNewestDateTime());

        } catch (Exception e) {
            System.out.println("Error reading payments:" + e.getMessage());
        }
        return transactions;
    }

    // Show all transactions
    public static void showAllTransactions(ArrayList<Transaction> allTransactions) {
        System.out.println("Showing All Transactions...");
        if (allTransactions.isEmpty()) {
            System.out.println("No transactions found.");
        } else {
            for (Transaction t : allTransactions) {
                System.out.println(t);
            }
        }
    }

    // Show only deposits
    public static void showOnlyDeposits(ArrayList<Transaction> allTransactions) {
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
    }

    // Show only payments
    public static void showOnlyPayments(ArrayList<Transaction> allTransactions) {
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
    }

    // Displays current month report
    public static void monthToDateReport(ArrayList<Transaction> allTransactions) {
        UserServices.printSeparator("Month-To-Date-Report");
        int currentMonth = Utility.getCurrentDateStamp().getMonthValue();
        ArrayList<Transaction> monthToDate = new ArrayList<>();
        for (Transaction t : allTransactions) {
            if (t.getDate().getMonthValue() == currentMonth && t.getDate().getYear() == currentYear) {
                monthToDate.add(t);
            }
        }
        if (monthToDate.isEmpty()) {
            System.out.println("No transactions found for this month.");
        } else {
            for (Transaction t : monthToDate) {
                System.out.println(t);
            }
        }
    }

    // Displays previous month report
    public static void previousMonthReport(ArrayList<Transaction> allTransactions) {
        UserServices.printSeparator("Previous Month Report");
        LocalDate previousMonthDate = Utility.getCurrentDateStamp().minusMonths(1);
        int prevMonth = previousMonthDate.getMonthValue();
        int prevYear = previousMonthDate.getYear();

        ArrayList<Transaction> previousMonth = new ArrayList<>();

        for (Transaction t : allTransactions) {
            LocalDate tDate = t.getDate();
            if (tDate.getMonthValue() == prevMonth && tDate.getYear() == prevYear) {
                previousMonth.add(t);
            }
        }

        if (previousMonth.isEmpty()) {
            System.out.println("No transactions found for the previous month.");
        } else {
            for (Transaction t : previousMonth) {
                System.out.println(t);
            }
        }
    }

    // Displays current calendar year report
    public static void yearToDateReport(ArrayList<Transaction> allTransactions) {
        UserServices.printSeparator("Year-To-Date Report");
        ArrayList<Transaction> yearToDate = new ArrayList<>();

        for (Transaction t : allTransactions) {
            if (t.getDate().getYear() == currentYear) {
                yearToDate.add(t);
            }
        }

        if (yearToDate.isEmpty()) {
            System.out.println("No transactions found for this year.");
        } else {
            for (Transaction t : yearToDate) {
                System.out.println(t);
            }
        }
    }

    // Displays previous calendar year report
    public static void previousYearReport(ArrayList<Transaction> allTransactions) {
        UserServices.printSeparator("Previous Year Report");
        int previousYear = Utility.getCurrentDateStamp().minusYears(1).getYear();
        ArrayList<Transaction> previousYearList = new ArrayList<>();
        for (Transaction t : allTransactions) {
            if (t.getDate().getYear() == previousYear) {
                previousYearList.add(t);
            }
        }
        if (previousYearList.isEmpty()) {
            System.out.println("No transactions found for the previous year.");
        } else {
            for (Transaction t : previousYearList) {
                System.out.println(t);
            }
        }
    }

    // Searches by vendor name and displays report
    public static void searchByVendorReport(ArrayList<Transaction> allTransactions) {
        UserServices.printSeparator("Search by Vendor");
        System.out.print("Enter vendor name to search: ");
        String vendorSearch = UserServices.getScanner().nextLine().trim().toLowerCase();
        ArrayList<Transaction> vendorMatches = new ArrayList<>();
        for (Transaction t : allTransactions) {
            if (t.getVendor().toLowerCase().contains(vendorSearch)) {
                vendorMatches.add(t);
            }
        }
        if (vendorMatches.isEmpty()) {
            System.out.println("No transactions found for vendor: " + vendorSearch);
        } else {
            for (Transaction t : vendorMatches) {
                System.out.println(t);
            }
        }
    }
}
