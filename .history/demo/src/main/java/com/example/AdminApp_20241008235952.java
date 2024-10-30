package com.example;

// Example usage of the system
public class AdminApp {

    public static void main(String[] args) throws Exception {
        RoleManager roleManager = new RoleManager();
        PasswordManager passwordManager = new PasswordManager();

        // First Admin user
        byte[] adminPass = passwordManager.hashPassword("admin123");
        roleManager.createUser("admin", adminPass);

        // Invite a user
        byte[] studentPass = passwordManager.hashPassword("student123");
        roleManager.inviteUser("student1", studentPass, "Student");

        // List users and roles
        roleManager.listUsers();
    }
}