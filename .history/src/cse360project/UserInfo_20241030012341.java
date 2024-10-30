package cse360project;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserInfo {
    private String username;
    private String password;
    private List<String> roles;

    // Constructor
    public UserInfo(String username, String password, List<String> roles) {
        this.username = username;
        this.password = password;
        this.roles = roles != null ? new ArrayList<>(roles) : new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    // Method to add a role to the user
    public void addRole(String role) {
        if (!roles.contains(role)) {
            roles.add(role);
        }
    }

    public void removeRole(String role) {
        if (this.roles != null) {
            this.roles.remove(role);
        }
    }

    // Getter for roles
    public List<String> getRoles() {
        return roles;
    }

    public static List<String> getRoles(String username, String password, File userDatabase) {
        List<String> roles = new ArrayList<>();
        user userAuth = new user(); // Create an instance of the user class

        // Check if credentials match
        if (userAuth.checkUserAuthentication(username, password, userDatabase)) {
            // Read the user database to find the roles
            try (BufferedReader br = new BufferedReader(new FileReader(userDatabase))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(":");
                    if (parts.length >= 3) {
                        String dbUsername = parts[0].trim();
                        String dbRoles = parts[2].trim(); // Assuming roles are in the 3rd column

                        // Check if the username matches
                        if (dbUsername.equals(username)) {
                            // Split roles if there are multiple
                            for (String role : dbRoles.split(",")) { // Assuming roles are separated by semicolons
                                roles.add(role.trim()); // Add trimmed role to the list
                            }
                            return roles; // Return the list of roles
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace(); // Handle exceptions
            }
        }
        return null; // Return null if authentication fails
    }
}