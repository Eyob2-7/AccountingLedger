package com.pluralsight;

import java.util.Scanner;

// This class provides utility methods for user interactions
public class UserServices {
    // Fire up the scanner for user input
    static Scanner myScanner = new Scanner(System.in);

    // Displays the Home Screen menu
    public static void displayHomeScreen() {
        printSeparator("Welcome to Accounting Ledger App");
        System.out.println("Please choose an option by typing the letter followed by a parentheses:\n");
        System.out.println("D) Add Deposit");
        System.out.println("P) Make Payment");
        System.out.println("L) View Ledger");
        System.out.println("X) Exit");
        System.out.print("Enter your choice:");
    }

    // Displays the Ledger Screen menu
    public static void ledgerScreen() {
        printSeparator("Ledger Screen");
        System.out.println("Please choose an option by typing the letter followed by a parentheses:\n");
        System.out.println("A) All");
        System.out.println("D) Deposits");
        System.out.println("P) Payments");
        System.out.println("R) Reports");
        System.out.println("H) Home");
        System.out.print("Enter your choice:");

        String ledgerChoice = getScanner().nextLine().trim().toUpperCase();
    }

    // Displays the Reports Screen
    public static void displayReportsScreen(){
        printSeparator("Reports Screen");
        System.out.println("Please choose an option by typing the number:\n");
        System.out.println("1) Month To Date");
        System.out.println("2) Previous Month");
        System.out.println("3) Year To Date");
        System.out.println("4) Previous Year");
        System.out.println("5) Search by Vendor");
        System.out.println("0) Back");
        pause(1000);
    }

    // Exit Screen
    public static void exit() {
        printSeparator("Exiting...");
        UserServices.pause(1000);
        System.out.println("Thank you for using the Accounting Ledger App. Goodbye");

    }

    // Prints a visual separator line
    public static void printSeparator(String title) {
        System.out.println("\n========================================================================");
        System.out.print("                          " + title);
        System.out.println("\n========================================================================");

    }

    // Returns the static Scanner object
    public static Scanner getScanner() {
        return myScanner;
    }

    // Pause the program for a given number of milliseconds
    public static void pause(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
