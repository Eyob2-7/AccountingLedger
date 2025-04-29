package com.pluralsight;

public class AccountingLedgerApp {

    public static void main(String[] args) {

// Control variable to keep the application running
        boolean isRunning = true;

// Main loop
        while (isRunning) {

            // Displays the home screen
            UserServices.displayHomeScreen();

            // Read user input
            String choice = UserServices.getScanner().nextLine().trim().toUpperCase();

            // Handle user choice
            switch (choice) {
                case "D":
                    String statusMessage = TransactionService.addDeposit();
                    System.out.println(statusMessage);
                    UserServices.pause(1000);
                    break;
                case "P":
                    String paymentMessage = TransactionService.makePayment();
                    System.out.println(paymentMessage);
                    UserServices.pause(1000);
                    break;
                case "L":
                   TransactionService.displayLedger();
                    UserServices.pause(1000);
                    break;
                case "X":
                    UserServices.exit();
                    isRunning = false;
                    break;
            }
        }
    }
}
