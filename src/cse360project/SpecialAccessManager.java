// SpecialAccessManager.java
package cse360mainproject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SpecialAccessManager {

    private Map<String, Set<String>> groupMembers = new HashMap<>(); // Map to store group members
    private Map<String, Set<String>> groupAdmins = new HashMap<>(); // Map to store group admins
    private Map<String, UserInfo> users = new HashMap<>();; 

    // Add user to a group
    public boolean addUserToGroup(String group, String username, boolean isAdmin, boolean isInstructor) {
        groupMembers.putIfAbsent(group, new HashSet<>());
        groupMembers.get(group).add(username);

        if (isInstructor) {
            // Check if there are currently any admins in the group
            if (groupAdmins.getOrDefault(group, Collections.emptySet()).isEmpty()) {
                // Make the first instructor an admin with view rights
                addAdminToGroup(group, username);
                System.out.println("First instructor added to group " + group + " has been given admin and view rights.");
            }
        } else if (isAdmin) {
            addAdminToGroup(group, username);
        }
        return true;
    }

    public boolean grantSpecialAccessToGroup(String username, String groupId) {
        UserInfo user = users.get(username);
        if (user != null) {
            user.grantSpecialAccessToGroup(groupId);
            return true;
        }
        return false;
    }

    public boolean grantSpecialAccessToArticle(String username, String articleId) {
        UserInfo user = users.get(username);
        if (user != null) {
            user.grantSpecialAccessToArticle(articleId);
            return true;
        }
        return false;
    }
    // Remove user from a group
    public boolean removeUserFromGroup(String group, String username) {
        if (groupAdmins.getOrDefault(group, Collections.emptySet()).contains(username) && groupAdmins.get(group).size() <= 1) {
            System.out.println("Cannot remove the last admin from the group.");
            return false; // Prevent removal if it's the last admin in the group
        }

        groupMembers.getOrDefault(group, Collections.emptySet()).remove(username);
        groupAdmins.getOrDefault(group, Collections.emptySet()).remove(username);
        return true;
    }

    // Add admin to a group
    public boolean addAdminToGroup(String group, String username) {
        groupAdmins.putIfAbsent(group, new HashSet<>());
        groupAdmins.get(group).add(username);
        groupMembers.putIfAbsent(group, new HashSet<>());
        groupMembers.get(group).add(username);
        return true;
    }

    // Check if a user is an admin in a group
    public boolean isAdminInGroup(String group, String username) {
        return groupAdmins.getOrDefault(group, Collections.emptySet()).contains(username);
    }

    // Get all members of a group
    public Set<String> getGroupMembers(String group) {
        return groupMembers.getOrDefault(group, Collections.emptySet());
    }

    // Get all admins of a group
    public Set<String> getGroupAdmins(String group) {
        return groupAdmins.getOrDefault(group, Collections.emptySet());
    }
}
