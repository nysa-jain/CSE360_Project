package com.example;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordManager {

    // Method to hash a password (MD5 hashing example for simplicity)
    public byte[] hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        return md.digest(password.getBytes());
    }

    // Method to verify that two passwords match
    public boolean verifyPassword(String password, byte[] storedHash) throws NoSuchAlgorithmException {
        byte[] hashedInput = hashPassword(password);
        return MessageDigest.isEqual(hashedInput, storedHash);
    }

    // Method to reset the password using OTP
    public void resetPassword(User user, String newPassword) throws NoSuchAlgorithmException {
        if (user.isOneTimePassword()) {
            user.setOneTimePassword(false);
            user.setOtpExpiry(null);  // Clear OTP expiry
            user.setPasswordHash(hashPassword(newPassword));
            System.out.println("Password reset for user: " + user.getUsername());
        } else {
            System.out.println("One-time password is not active for this user.");
        }
    }
}