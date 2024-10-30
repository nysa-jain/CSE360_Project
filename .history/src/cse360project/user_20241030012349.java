package cse360project;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class user {

    // Constructor
    public user() { }

    // Method to check user authentication
    public static Boolean checkUserAuthentication(String userName, String password, File userD) {
        // Attempt to read the user database
        try (Scanner userReader = new Scanner(userD)) {
            // Iterate through each line in the user database
            while (userReader.hasNextLine()) {
                String data = userReader.nextLine();
                String[] infos = data.split(":");
                // Check if the line contains at least a username and hashed password
                if (infos.length >= 2) {
                    String id = infos[0];
                    String storedHash = infos[1];

                    // Check if username and password match
                    if (id.equals(userName) && storedHash.equals(hashPassword(password))) {
                        return true; // Successful authentication
                    }
                }
            }
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return false; // Authentication failed
    }

    // Method to add a user account with hashed password
    public Boolean addUserAccount(String userName, String password, String role, File userD) {
        if (checkIfUserExists(userName, userD)) {
            return false; // User already exists, cannot add
        }
        try (FileWriter fw = new FileWriter(userD, true)) {
            String hashedPassword = hashPassword(password); // Hash the password
            String otpExpiry = LocalDateTime.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            fw.write(userName + ":" + hashedPassword + ":" + otpExpiry + ":false:" + role + "\n"); // Add role here
            return true; // User account added successfully
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return false; // Failed to add user account
    }
    
    // Method to check if user already exists
    public Boolean checkIfUserExists(String userName, File userD) {
        // Attempt to read the user database
        try (Scanner userReader = new Scanner(userD)) {
            // Iterate through each line in the user database
            while (userReader.hasNextLine()) {
                String data = userReader.nextLine();
                String[] infos = data.split(":");
                // Check if the line contains at least a username
                if (infos.length >= 1) {
                    String id = infos[0];
                    if (id.equals(userName)) {
                        return true; // User exists
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; // User does not exist
    }

    // Method to hash passwords securely using SHA-256
    private static String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256"); // Create MessageDigest instance for SHA-256
        byte[] hash = md.digest(password.getBytes()); // Compute hash
        return Base64.getEncoder().encodeToString(hash); // Return base64 encoded hash
    }

    // Method to validate one-time password (returns true if OTP is valid)
    public Boolean validateOneTimePassword(String userName, File userD) {
        // Attempt to read the user database
        try (Scanner userReader = new Scanner(userD)) {
            // Iterate through each line in the user database
            while (userReader.hasNextLine()) {
                String data = userReader.nextLine();
                String[] infos = data.split(":");
                // Check if the line contains necessary information
                if (infos.length >= 4) {
                    String id = infos[0];
                    String otpExpiry = infos[2];
                    String otpFlag = infos[3];

                    // If user exists and OTP flag is true
                    if (id.equals(userName) && otpFlag.equals("true")) {
                        // Parse the expiry date and compare with current date
                        LocalDateTime expiryDate = LocalDateTime.parse(otpExpiry, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                        if (LocalDateTime.now().isBefore(expiryDate)) {
                            return true; // OTP is still valid
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; // OTP is invalid or does not exist
    }

    // Method to reset a user's password (e.g., for OTP)
    public Boolean resetPassword(String userName, String newPassword, File userD) {
        // Attempt to read the user database
        try (Scanner userReader = new Scanner(userD)) {
            StringBuilder updatedContent = new StringBuilder();
            boolean userFound = false;

            // Read through file and update password for the specified user
            while (userReader.hasNextLine()) {
                String data = userReader.nextLine();
                String[] infos = data.split(":");
                if (infos.length >= 4) {
                    String id = infos[0];
                    String hashedPassword = infos[1];
                    String otpExpiry = infos[2];
                    String otpFlag = infos[3];

                    if (id.equals(userName)) {
                        // Hash new password and set OTP flag to false
                        hashedPassword = hashPassword(newPassword);
                        otpFlag = "false"; // Reset OTP flag
                        userFound = true; // Mark user as found
                    }

                    // Update the line with the new or existing information
                    updatedContent.append(id)
                            .append(":")
                            .append(hashedPassword)
                            .append(":")
                            .append(otpExpiry)
                            .append(":")
                            .append(otpFlag)
                            .append("\n");
                }
            }

            // Write the updated content back to the file if the user was found
            if (userFound) {
                try (FileWriter writer = new FileWriter(userD)) {
                    writer.write(updatedContent.toString()); // Write updated content to file
                }
                return true; // Password reset successfully
            }
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return false; // Failed to reset password
    }
}