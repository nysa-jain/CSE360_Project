package cse360project1;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Random;
import java.util.Scanner;

public class AdminApp {

    // Initialize the admin database with a default admin if no admin exists
    // This method checks if the admin database exists and if it's empty.
    // If it doesn't exist or is empty, a default admin account with a hashed password is created.
    public void initializeAdminDatabase(File adminD) {
        if (!adminD.exists() || adminD.length() == 0) {
            try (FileWriter writer = new FileWriter(adminD)) {
                writer.write("admin:" + hashPassword("1234") + ":Admin\n"); // Default admin credentials
            } catch (IOException e) {
                e.printStackTrace(); // Print error message in case of I/O exception
            }
        }
    }

    // Add a user account with hashed password and role
    // This method takes a username, password, role, and user database file as input.
    // It checks if the username already exists and if not, it appends the new user with a hashed password.
    public Boolean addUserAccount(String userName, String password, String role, File userD) {
        if (checkUsernameExist(userName, userD)) { // Check if username already exists
            return false; // If username exists, return false
        }
        try (FileWriter fw = new FileWriter(userD, true)) { // Open the user database in append mode
            fw.write(userName + ":" + hashPassword(password) + ":" + role + "\n"); // Write user data
            return true; // Successfully added user
        } catch (IOException e) {
            e.printStackTrace(); // Print error in case of I/O exception
        }
        return false; // Return false if any exception occurs
    }

    // Remove a user account by username
    // This method reads through the user database and removes the user with the matching username.
    public boolean removeAccount(String userName, File userD) {
        boolean userRemoved = false; // Flag to check if user was removed
        StringBuilder updatedContent = new StringBuilder(); // To hold the updated user database

        try (Scanner userReader = new Scanner(userD)) { // Read the user database
            while (userReader.hasNextLine()) {
                String data = userReader.nextLine(); // Read each line
                String[] infos = data.split(":"); // Split the data to get username and other details
                if (!infos[0].equals(userName)) { // If the username does not match, keep the data
                    updatedContent.append(data).append("\n");
                } else {
                    userRemoved = true; // Set flag if user is found and removed
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // Print error in case of I/O exception
        }

        if (userRemoved) { // If user was removed, overwrite the file with updated content
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(userD))) {
                writer.write(updatedContent.toString()); // Write the updated content back to the file
            } catch (IOException e) {
                e.printStackTrace(); // Print error in case of I/O exception
            }
        }

        return userRemoved; // Return whether the user was removed or not
    }

    // Method to check if username already exists
    // This method scans through the user database and checks if the username already exists.
    public boolean checkUsernameExist(String userName, File userD) {
        try (Scanner userReader = new Scanner(userD)) { // Read the user database
            while (userReader.hasNextLine()) {
                String data = userReader.nextLine(); // Read each line
                String[] infos = data.split(":"); // Split the data to get the username
                if (infos[0].equals(userName)) {
                    return true; // Return true if the username is found
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // Print error in case of I/O exception
        }
        return false; // Return false if username is not found
    }

    // Generate a 5-character invitation code
    // This method generates a random 5-character code and associates it with a role in the code database.
    public String generateInvitationCode(String role, File codeD) {
        String code = generateRandomCode(); // Generate a random code
        try (FileWriter fw = new FileWriter(codeD, true)) { // Append the code to the code database
            fw.write(code + ":" + role + "\n"); // Write the code and associated role
            return code; // Return the generated code
        } catch (IOException e) {
            e.printStackTrace(); // Print error in case of I/O exception
        }
        return null; // Return null if any exception occurs
    }
    
    // Check if admin authentication is successful
    // This method checks if the entered admin username and password match the stored credentials.
    public boolean checkAdminAuthentication(String userName, String password, File adminD) {
        try (Scanner adminReader = new Scanner(adminD)) { // Read the admin database
            while (adminReader.hasNextLine()) {
                String data = adminReader.nextLine(); // Read each line
                String[] infos = data.split(":"); // Split the data to get username and password
                String id = infos[0]; // Get the username
                String pass = infos[1]; // Get the stored hashed password

                if (id.equals(userName) && pass.equals(hashPassword(password))) {
                    return true;  // Return true if the username and password match
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // Print error in case of I/O exception
        }
        return false; // Return false if authentication fails
    }

    // Validate the invitation code for a specific role
    // This method checks if the given invitation code is valid and matches the provided role.
    public boolean validateInvitationCode(String code, String role, File codeD) {
        try (Scanner codeReader = new Scanner(codeD)) { // Read the code database
            while (codeReader.hasNextLine()) {
                String data = codeReader.nextLine(); // Read each line
                String[] infos = data.split(":"); // Split the data to get code and role
                if (infos[0].equals(code) && infos[1].equals(role)) {
                    return true; // Return true if the code and role match
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // Print error in case of I/O exception
        }
        return false; // Return false if the code and role do not match
    }

    // Validate the verification code during registration
    // This method checks if the invitation code is valid and if the username is not already taken.
    public boolean validateVerificationCode(String userName, String verificationCode, String role, File codeD) {
        return validateInvitationCode(verificationCode, role, codeD) && 
               !checkUsernameExist(userName, new File("UserDatabase.txt")); // Ensure code is valid and username doesn't exist
    }

    // Hash passwords securely
    // This method uses SHA-256 hashing to securely hash passwords before storing them.
    public String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256"); // Get the SHA-256 hashing algorithm
            byte[] hash = md.digest(password.getBytes()); // Hash the password
            return Base64.getEncoder().encodeToString(hash); // Return the hashed password as a Base64 string
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace(); // Print error if the algorithm is not available
        }
        return null; // Return null if hashing fails
    }

    // Generate random 5-character code
    // This method generates a random 5-character code from uppercase letters and digits.
    private String generateRandomCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"; // Allowed characters
        StringBuilder code = new StringBuilder(5); // To store the generated code
        Random rnd = new Random(); // Random number generator
        for (int i = 0; i < 5; i++) {
            code.append(chars.charAt(rnd.nextInt(chars.length()))); // Append a random character from the set
        }
        return code.toString(); // Return the generated code
    }
}