// UserInfo.java
package cse360project;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserInfo {
    // Existing fields
    private String username;
    private String password;
    private List<String> roles;
    private List<String> accessibleGroups;
    private List<String> accessibleArticles;
    private boolean isAdminForGroup; // New: Determines if the user has admin rights in a group
    private List<String> specialAccessGroups = new ArrayList<>();
    private List<String> specialAccessArticles = new ArrayList<>();

    // New: Map to store permissions per group
    private Map<String, Boolean> groupAdminPermissions;

    // Constructor
    public UserInfo(String username, String password, List<String> roles, List<String> accessibleGroups, List<String> accessibleArticles) {
        this.username = username;
        this.password = password;
        this.roles = roles != null ? new ArrayList<>(roles) : new ArrayList<>();
        this.accessibleGroups = accessibleGroups != null ? new ArrayList<>(accessibleGroups) : new ArrayList<>();
        this.accessibleArticles = accessibleArticles != null ? new ArrayList<>(accessibleArticles) : new ArrayList<>();
        this.isAdminForGroup = false;
        this.groupAdminPermissions = new HashMap<>();
    }

    // Check if user is admin for a specific group
    public boolean isAdminForGroup(String group) {
        return groupAdminPermissions.getOrDefault(group, false);
    }

    // Add admin rights to a group for the user
    public void grantAdminRights(String group) {
        groupAdminPermissions.put(group, true);
    }

    // Revoke admin rights from a group for the user
    public void revokeAdminRights(String group) {
        groupAdminPermissions.put(group, false);
    }

    // Check if user has view/edit permissions for articles in a group
    public boolean canEditArticles(String group) {
        return roles.contains("Instructor") || accessibleGroups.contains(group);
    }

    public boolean canViewArticles(String group) {
        return roles.contains("Student") || canEditArticles(group);
    }

    // Check if user belongs to a specific group
    public boolean hasGroupAccess(String group) {
        return accessibleGroups.contains(group);
    }

    // Check if user has access to a specific article
    public boolean hasArticleAccess(String articleId) {
        return accessibleArticles.contains(articleId);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public List<String> getRoles() {
        return roles;
    }

 // Method to get accessible groups for the user
    public static List<String> getAccessibleGroups(String username) {
        List<String> groups = new ArrayList<>();
        File groupsFile = new File("GroupsDatabase.txt"); // Path to your groups database file

        try (BufferedReader br = new BufferedReader(new FileReader(groupsFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":"); // Adjust delimiter as needed
                if (parts.length >= 2) {
                    String dbUsername = parts[0].trim();
                    String dbGroups = parts[1].trim(); // Assuming groups are in the second column

                    if (dbUsername.equals(username)) {
                        for (String group : dbGroups.split(",")) { // Adjust delimiter as needed
                            groups.add(group.trim());
                        }
                        break; // Stop reading after finding the user's groups
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle file reading exceptions
        }

        return groups;
    }

    // Method to get accessible articles for the user
    public static List<String> getAccessibleArticles(String username) {
        List<String> articles = new ArrayList<>();
        File articlesFile = new File("articles.txt"); // Path to your articles database file

        try (BufferedReader br = new BufferedReader(new FileReader(articlesFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":"); // Adjust delimiter as needed
                if (parts.length >= 2) {
                    String dbUsername = parts[0].trim();
                    String dbArticles = parts[1].trim(); // Assuming articles are in the second column

                    if (dbUsername.equals(username)) {
                        for (String article : dbArticles.split(",")) { // Adjust delimiter as needed
                            articles.add(article.trim());
                        }
                        break; // Stop reading after finding the user's articles
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle file reading exceptions
        }

        return articles;
    }


    // Add group access to user
    public void addGroupAccess(String group) {
        if (!accessibleGroups.contains(group)) {
            accessibleGroups.add(group);
        }
    }

    // Add article access to user
    public void addArticleAccess(String articleId) {
        if (!accessibleArticles.contains(articleId)) {
            accessibleArticles.add(articleId);
        }
    }
    
    public void removeRole(String role) {
        if (this.roles != null) {
            this.roles.remove(role);
        }
    }
    
    public void addRole(String role) {
        if (!roles.contains(role)) {
            roles.add(role);
        }
    }
    
    public void grantSpecialAccessToGroup(String groupId) {
        if (!specialAccessGroups.contains(groupId)) {
            specialAccessGroups.add(groupId);
        }
    }

    public void grantSpecialAccessToArticle(String articleId) {
        if (!specialAccessArticles.contains(articleId)) {
            specialAccessArticles.add(articleId);
        }
    }

    public boolean hasSpecialAccessToGroup(String groupId) {
        return specialAccessGroups.contains(groupId);
    }

    public boolean hasSpecialAccessToArticle(String articleId) {
        return specialAccessArticles.contains(articleId);
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
