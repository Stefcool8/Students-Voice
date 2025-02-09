package org.example.studentsvoice;

import org.mindrot.jbcrypt.BCrypt;

import java.io.Console;

public class PasswordVerifier {
    public static void main(String[] args) {
        Console console = System.console();
        if (console == null) {
            System.out.println("No console available");
            return;
        }

        // Prompt for the password securely
        char[] passwordChars = console.readPassword("Enter password to verify: ");
        String password = new String(passwordChars);

        // Prompt for the hashed password
        char[] hashedPasswordChars = console.readPassword("Enter hashed password: ");
        String hashedPassword = new String(hashedPasswordChars);

        // Verify the password
        if (BCrypt.checkpw(password, hashedPassword)) {
            System.out.println("Password matches");
        } else {
            System.out.println("Password does not match");
        }
    }
}
