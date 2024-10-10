package cse360project1;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Random;
import java.util.Scanner;

public class AdminApp {

    // Initialize the admin database with a default admin if no admin exists
    public void initializeAdminDatabase(File adminD) {
        if (!adminD.exists() || adminD.length() == 0) {
            try (FileWriter writer = new FileWriter(adminD)) {
                writer.write("admin:" + hashPassword("1234") + ":Admin\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Add a user account with hashed password and role
    public Boolean addUserAccount(String userName, String password, String role, File userD) {
        if (checkUsernameExist(userName, userD)) {
            return false;
        }
        try (FileWriter fw = new FileWriter(userD, true)) {
            fw.write(userName + ":" + hashPassword(password) + ":" + role + "\n");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Remove a user account by username
    public boolean removeAccount(String userName, File userD) {
        boolean userRemoved = false;
        StringBuilder updatedContent = new StringBuilder();

        try (Scanner userReader = new Scanner(userD)) {
            while (userReader.hasNextLine()) {
                String data = userReader.nextLine();
                String[] infos = data.split(":");
                if (!infos[0].equals(userName)) {
                    updatedContent.append(data).append("\n");
                } else {
                    userRemoved = true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (userRemoved) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(userD))) {
                writer.write(updatedContent.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return userRemoved;
    }

    // Method to check if username already exists
    public boolean checkUsernameExist(String userName, File userD) {
        try (Scanner userReader = new Scanner(userD)) {
            while (userReader.hasNextLine()) {
                String data = userReader.nextLine();
                String[] infos = data.split(":");
                if (infos[0].equals(userName)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Generate a 5-character invitation code
    public String generateInvitationCode(String role, File codeD) {
        String code = generateRandomCode();
        try (FileWriter fw = new FileWriter(codeD, true)) {
            fw.write(code + ":" + role + "\n");
            return code;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // Check if admin authentication is successful
    public boolean checkAdminAuthentication(String userName, String password, File adminD) {
        try (Scanner adminReader = new Scanner(adminD)) {
            while (adminReader.hasNextLine()) {
                String data = adminReader.nextLine();
                String[] infos = data.split(":");
                String id = infos[0];
                String pass = infos[1];

                if (id.equals(userName) && pass.equals(hashPassword(password))) {
                    return true;  // Admin authenticated successfully
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Validate the invitation code for a specific role
    public boolean validateInvitationCode(String code, String role, File codeD) {
        try (Scanner codeReader = new Scanner(codeD)) {
            while (codeReader.hasNextLine()) {
                String data = codeReader.nextLine();
                String[] infos = data.split(":");
                if (infos[0].equals(code) && infos[1].equals(role)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Validate the verification code during registration
    public boolean validateVerificationCode(String userName, String verificationCode, String role, File codeD) {
        return validateInvitationCode(verificationCode, role, codeD) && !checkUsernameExist(userName, new File("UserDatabase.txt"));
    }

    // Hash passwords securely
    public String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Generate random 5-character code
    private String generateRandomCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder code = new StringBuilder(5);
        Random rnd = new Random();
        for (int i = 0; i < 5; i++) {
            code.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return code.toString();
    }
}
