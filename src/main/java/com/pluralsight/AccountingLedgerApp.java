package com.pluralsight;

public class AccountingLedgerApp {

    public static void main(String[] args) {
// Control variable to keep the application running
boolean isRunning = true;

// Main loop
while (isRunning){
    UserServices.printSeparator();
    UserServices.displayHomeScreen();

    // Read user input
    String choice = UserServices.getScanner().nextLine().trim().toUpperCase();

    // Handle user choice
    switch (choice){
        case "D":
            System.out.println("Adding Deposit...");
            UserServices.printSeparator();
            UserServices.pause(1000);
            break;
        case "P":
            System.out.println("Making Payment...");
            UserServices.printSeparator();
            UserServices.pause(1000);

            break;
        case "L":
            System.out.println("Viewing Ledger...");
            UserServices.printSeparator();
            UserServices.pause(1000);
            break;
        case "X":
            System.out.println("Exiting...");
            UserServices.pause(1000);
            isRunning = false;
            break;
        default:
            System.out.println("Invalid choice. Please try again");
    }
}
    }
}
