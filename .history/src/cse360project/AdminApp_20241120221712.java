package cse360project;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class AdminApp {

    // Initialize the admin database with a default admin if no admin exists
    public void initializeAdminDatabase(File adminD) {
        if (!adminD.exists() || adminD.length() == 0) {
            try (FileWriter writer = new FileWriter(adminD)) {
                // Default admin credentials: username = "admin", password = "1234"
                writer.write("admin:" + hashPassword("1234") + ":Admin\n");
            } catch (IOException e) {
                e.printStackTrace(); // Handle file write exceptions
            }
        }
    }

    // Add a new user account with hashed password and role
    public Boolean addUserAccount(String userName, String password, String role, File userD) {
        // Check if the username already exists
        if (checkUsernameExist(userName, userD)) {
            return false; // Username exists; cannot add
        }
        try (FileWriter fw = new FileWriter(userD, true)) {
            // Append new user to the database
            fw.write(userName + ":" + hashPassword(password) + ":" + role + "\n");
            return true; // User added successfully
        } catch (IOException e) {
            e.printStackTrace(); // Handle file write exceptions
        }
        return false; // Return false if an exception occurs
    }

    // Remove a user account from the database
    public boolean removeAccount(String userName, File userD) {
        boolean userRemoved = false; // Flag to indicate if a user is removed
        StringBuilder updatedContent = new StringBuilder(); // To store updated file data

        try (Scanner userReader = new Scanner(userD)) {
            while (userReader.hasNextLine()) {
                String data = userReader.nextLine();
                String[] infos = data.split(":"); // Parse user data
                // Skip the user to be removed; keep others
                if (!infos[0].equals(userName)) {
                    updatedContent.append(data).append("\n");
                } else {
                    userRemoved = true; // User found and removed
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle file read exceptions
        }

        if (userRemoved) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(userD))) {
                writer.write(updatedContent.toString()); // Save updated data to file
            } catch (IOException e) {
                e.printStackTrace(); // Handle file write exceptions
            }
        }

        return userRemoved; // Return whether the user was successfully removed
    }

    // Check if a username exists in the user database
    public boolean checkUsernameExist(String userName, File userD) {
        try (Scanner userReader = new Scanner(userD)) {
            while (userReader.hasNextLine()) {
                String data = userReader.nextLine();
                String[] infos = data.split(":");
                if (infos[0].equals(userName)) {
                    return true; // Username found
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle file read exceptions
        }
        return false; // Username does not exist
    }

    // Generate a random invitation code for a specific role
    public String generateInvitationCode(String role, File codeD) {
        String code = generateRandomCode(); // Generate a random 5-character code
        try (FileWriter fw = new FileWriter(codeD, true)) {
            fw.write(code + ":" + role + "\n"); // Save the code and role to the file
            return code; // Return the generated code
        } catch (IOException e) {
            e.printStackTrace(); // Handle file write exceptions
        }
        return null; // Return null if an exception occurs
    }

    // Authenticate an admin user with username and password
    public boolean checkAdminAuthentication(String userName, String password, File adminD) {
        try (Scanner adminReader = new Scanner(adminD)) {
            while (adminReader.hasNextLine()) {
                String data = adminReader.nextLine();
                String[] infos = data.split(":");
                String id = infos[0];
                String pass = infos[1];
                // Check if username and hashed password match
                if (id.equals(userName) && pass.equals(hashPassword(password))) {
                    return true; // Authentication successful
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle file read exceptions
        }
        return false; // Authentication failed
    }

    // Validate an invitation code for a given role
    public boolean validateInvitationCode(String code, String role, File codeD) {
        try (Scanner codeReader = new Scanner(codeD)) {
            while (codeReader.hasNextLine()) {
                String data = codeReader.nextLine();
                String[] infos = data.split(":");
                // Check if the code and role match
                if (infos[0].equals(code) && infos[1].equals(role)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle file read exceptions
        }
        return false; // Validation failed
    }

    // Validate the verification code during registration
    public boolean validateVerificationCode(String userName, String verificationCode, String role, File codeD) {
        // Validate invitation code and ensure username is not already taken
        return validateInvitationCode(verificationCode, role, codeD) && 
               !checkUsernameExist(userName, new File("UserDatabase.txt"));
    }

    // Securely hash passwords using SHA-256
    public String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hash); // Convert to Base64 string
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace(); // Handle missing algorithm exception
        }
        return null; // Return null if hashing fails
    }

    // Generate a random 5-character code
    private String generateRandomCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder code = new StringBuilder(5);
        Random rnd = new Random();
        for (int i = 0; i < 5; i++) {
            code.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return code.toString(); // Return generated code
    }

    // Retrieve all user information from the database
    public List<UserInfo> getAllUsers(File userD) {
        List<UserInfo> users = new ArrayList<>();
        try (Scanner userReader = new Scanner(userD)) {
            while (userReader.hasNextLine()) {
                String data = userReader.nextLine();
                String[] infos = data.split(":");
                String username = infos[0];
                String password = infos[1];
                List<String> roles = new ArrayList<>(List.of(infos[2].split(",")));
                List<String> accessibleGroups = UserInfo.getAccessibleGroups(username);
                List<String> accessibleArticles = UserInfo.getAccessibleArticles(username);
                users.add(new UserInfo(username, password, roles, accessibleGroups, accessibleArticles));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }

    // Add a role to a user
    public boolean addRole(String username, String role, File userD) {
        List<UserInfo> users = getAllUsers(userD);
        for (UserInfo user : users) {
            if (user.getUsername().equals(username)) {
                user.addRole(role); // Add the role
                updateUserDatabase(users, userD); // Save changes to the database
                return true;
            }
        }
        return false; // User not found
    }

    // Remove a role from a user
    public boolean removeRole(String username, String role, File userD) {
        List<UserInfo> users = getAllUsers(userD);
        // Check if removing admin role leaves no admins
        if (role.equalsIgnoreCase("Admin")) {
            long adminCount = users.stream()
                .filter(user -> user.getRoles().contains("Admin"))
                .count();
            if (adminCount <= 1) {
                System.out.println("Cannot remove the last admin from the system.");
                return false; // Prevent removing the last admin
            }
        }
        for (UserInfo user : users) {
            if (user.getUsername().equals(username)) {
                user.removeRole(role);
                updateUserDatabase(users, userD);
                return true; // Role removed
            }
        }
        return false; // Role not removed
    }

    // Save updated user data to the database
    private void updateUserDatabase(List<UserInfo> users, File userD) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(userD))) {
            for (UserInfo user : users) {
                writer.write(user.getUsername() + ":" + user.getPassword() + ":" + String.join(",", user.getRoles()) + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle file write exceptions
        }
    }
}
