package com.pluralsight;

import java.util.Scanner;

// This class provides utility methods for user interactions
public class UserServices {
    // Fire up the scanner for user input
    static Scanner myScanner = new Scanner(System.in);

    // Displays the Home Screen menu
    public static void displayHomeScreen() {
        System.out.println("\n=== Home Screen ===");
        System.out.println("D) Add Deposit");
        System.out.println("P) Make Payment");
        System.out.println("L) View Ledger");
        System.out.println("X) Exit");
        System.out.print("Enter your choice:");
    }

    // Prints a visual separator line
    public static void printSeparator() {
        System.out.println("-----------------------------------");
    }

    // Returns the static Scanner object
    public static Scanner getScanner(){
        return myScanner;
    }

    // Pause the program for a given number of milliseconds
    public static void pause(int milliseconds){
        try{
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
