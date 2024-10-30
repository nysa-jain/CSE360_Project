package com.example;
import java.util.HashMap;
import java.util.Map;

public class RoleManager {
    Map<String, User> userDatabase;

    public RoleManager() {
        userDatabase = new HashMap<>();
    }

    // Method to assign roles to a user
    public void assignRole(String username, String role) {
        User user = userDatabase.get(username);
        if (user != null) {
            user.addRole(role);
            System.out.println("Role " + role + " assigned to " + username);
        } else {
            System.out.println("User not found.");
        }
    }

    // Method to create a new user (First user becomes Admin by default)
    public void createUser(String username, byte[] passwordHash) {
        if (userDatabase.isEmpty()) {
            User admin = new User(username, passwordHash);
            admin.addRole("Admin");
            userDatabase.put(username, admin);
            System.out.println("Admin account created for " + username);
        } else {
            User newUser = new User(username, passwordHash);
            userDatabase.put(username, newUser);
            System.out.println("New user created: " + username);
        }
    }

    // Method to list users and their roles
    public void listUsers() {
        userDatabase.forEach((username, user) -> {
            System.out.println("User: " + username + ", Roles: " + user.getRoles());
        });
    }

    // Method to invite a new user (Simulates sending a one-time invitation code)
    public void inviteUser(String username, byte[] passwordHash, String role) {
        User invitedUser = new User(username, passwordHash);
        invitedUser.addRole(role);
        invitedUser.setOneTimePassword(true);  // Invitation-based accounts start with OTP
        userDatabase.put(username, invitedUser);
        System.out.println("Invitation sent to: " + username + " for role: " + role);
    }
}