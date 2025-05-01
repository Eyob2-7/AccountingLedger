package com.pluralsight;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.LocalTime;

public class Utility {

    //Returns the current date
    public static LocalDate getCurrentDateStamp() {
        return LocalDate.now();
    }

    //Returns the current time as (HH:mm:ss)
    public static String getCurrentTimeStamp() {
        return LocalTime.now().withNano(0).toString();
    }

    // Opens a file reader
    public static BufferedReader getFileReader(String fileName) {
        try {
            FileReader reader = new FileReader(fileName);
            return new BufferedReader(reader);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    // Pause the program for a given number of milliseconds
    public static void pause(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    // validate required string inputs
    public static boolean inputValidator(String userInput) {
        return userInput == null || userInput.trim().isEmpty();
    }
}
