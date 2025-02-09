package org.example.studentsvoice;

import org.mindrot.jbcrypt.BCrypt;

import java.io.Console;

public class PasswordHasher {
    public static void main(String[] args) {
        Console console = System.console();
        if (console == null) {
            System.out.println("No console available");
            return;
        }

        // Prompt for the password securely
        char[] passwordChars = console.readPassword("Enter password to hash: ");
        String password = new String(passwordChars);

        // Hash the password
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        // Output the hashed password
        System.out.println("Hashed Password:");
        System.out.println(hashedPassword);
    }
}
