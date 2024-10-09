package com.example;
import java.util.ArrayList;
import java.util.List;

public class User {
    private String email;
    private String username;
    private byte[] passwordHash; // Non-string data type to store password
    private boolean oneTimePassword;
    private String otpExpiry;
    private String firstName;
    private String middleName;
    private String lastName;
    private String preferredFirstName;
    private List<String> roles;
    private List<String> topics = new ArrayList<>(List.of("Beginner", "Intermediate", "Advanced", "Expert"));
    private String topicLevel = "Intermediate"; // Default level

    public User(String username, byte[] passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.roles = new ArrayList<>();
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public byte[] getPasswordHash() {
        return passwordHash;
    }

    public boolean isOneTimePassword() {
        return oneTimePassword;
    }

    public void setOneTimePassword(boolean oneTimePassword) {
        this.oneTimePassword = oneTimePassword;
    }

    public String getOtpExpiry() {
        return otpExpiry;
    }

    public void setOtpExpiry(String otpExpiry) {
        this.otpExpiry = otpExpiry;
    }

    public void addRole(String role) {
        roles.add(role);
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPreferredFirstName(String preferredFirstName) {
        this.preferredFirstName = preferredFirstName;
    }

    public void setPasswordHash(byte[] hashPassword) {
        
    }
}