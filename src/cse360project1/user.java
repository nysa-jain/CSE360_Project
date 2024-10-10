package cse360project1;

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
    public Boolean checkUserAuthentication(String userName, String password, File userD) {
        try (Scanner userReader = new Scanner(userD)) {
            while (userReader.hasNextLine()) {
                String data = userReader.nextLine();
                String[] infos = data.split(":");
                if (infos.length >= 2) {
                    String id = infos[0];
                    String storedHash = infos[1];

                    // Check if username and password match
                    if (id.equals(userName) && storedHash.equals(hashPassword(password))) {
                        return true;
                    }
                }
            }
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Method to add a user account with hashed password
    public Boolean addUserAccount(String userName, String password, File userD) {
        // Check if user already exists
        if (checkIfUserExists(userName, userD)) {
            return false;
        }
        try (FileWriter fw = new FileWriter(userD, true)) {
            String hashedPassword = hashPassword(password);
            String otpExpiry = LocalDateTime.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            fw.write(userName + ":" + hashedPassword + ":" + otpExpiry + ":false\n");
            return true;
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Method to check if user already exists
    public Boolean checkIfUserExists(String userName, File userD) {
        try (Scanner userReader = new Scanner(userD)) {
            while (userReader.hasNextLine()) {
                String data = userReader.nextLine();
                String[] infos = data.split(":");
                if (infos.length >= 1) {
                    String id = infos[0];
                    if (id.equals(userName)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Method to hash passwords securely using SHA-256
    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(password.getBytes());
        return Base64.getEncoder().encodeToString(hash);
    }

    // Method to validate one-time password (returns true if OTP is valid)
    public Boolean validateOneTimePassword(String userName, File userD) {
        try (Scanner userReader = new Scanner(userD)) {
            while (userReader.hasNextLine()) {
                String data = userReader.nextLine();
                String[] infos = data.split(":");
                if (infos.length >= 4) {
                    String id = infos[0];
                    String otpExpiry = infos[2];
                    String otpFlag = infos[3];

                    // If user exists and OTP flag is true
                    if (id.equals(userName) && otpFlag.equals("true")) {
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
        return false;
    }

    // Method to reset a user's password (e.g., for OTP)
    public Boolean resetPassword(String userName, String newPassword, File userD) {
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
                        otpFlag = "false";
                        userFound = true;
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
                    writer.write(updatedContent.toString());
                }
                return true;
            }
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return false;
    }
}
