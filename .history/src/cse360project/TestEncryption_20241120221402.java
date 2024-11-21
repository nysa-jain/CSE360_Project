package cse360project;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class TestEncryption {

    private SpecialAccessManager specialAccessManager;
    private String testGroup;
    private String testUser;
    private String testInstructor;
    private String testArticleId;

    @BeforeEach
    public void setUp() {
        // Initialize SpecialAccessManager and test data
        specialAccessManager = new SpecialAccessManager();
        testGroup = "Group1";
        testUser = "student1";
        testInstructor = "instructor1";
        testArticleId = "article123";
    }

    @Test
    public void testEncryptionAndDecryption() {
        String originalContent = "Sensitive Article Body";

        // Test encryption
        String encryptedContent = AESUtil.encrypt(originalContent);
        assertNotNull(encryptedContent, "Encryption should not return null.");
        assertNotEquals(originalContent, encryptedContent, "Encrypted content should not match the original content.");

        // Test decryption
        String decryptedContent = AESUtil.decrypt(encryptedContent);
        assertNotNull(decryptedContent, "Decryption should not return null.");
        assertEquals(originalContent, decryptedContent, "Decrypted content should match the original content.");
    }

    @Test
    public void testAddUserToGroup() {
        boolean added = specialAccessManager.addUserToGroup(testGroup, testUser, false, false);
        assertTrue(added, "User should be added to the group.");

        Set<String> groupMembers = specialAccessManager.getGroupMembers(testGroup);
        assertTrue(groupMembers.contains(testUser), "User should be in the group.");
    }

    @Test
    public void testAddInstructorToGroupWithAdminRights() {
        boolean added = specialAccessManager.addUserToGroup(testGroup, testInstructor, false, true);
        assertTrue(added, "Instructor should be added to the group.");

        Set<String> groupAdmins = specialAccessManager.getGroupAdmins(testGroup);
        assertTrue(groupAdmins.contains(testInstructor), "First instructor should be given admin rights.");
    }

    @Test
    public void testGrantSpecialAccessToArticle() {
        boolean granted = specialAccessManager.grantSpecialAccessToArticle(testUser, testArticleId);
        assertTrue(granted, "Special access to the article should be granted.");
    }

    @Test
    public void testGrantSpecialAccessToGroup() {
        boolean granted = specialAccessManager.grantSpecialAccessToGroup(testUser, testGroup);
        assertTrue(granted, "Special access to the group should be granted.");
    }

    @Test
    public void testRemoveUserFromGroup() {
        // Add a user and then remove them
        specialAccessManager.addUserToGroup(testGroup, testUser, false, false);
        boolean removed = specialAccessManager.removeUserFromGroup(testGroup, testUser);
        assertTrue(removed, "User should be removed from the group.");

        Set<String> groupMembers = specialAccessManager.getGroupMembers(testGroup);
        assertFalse(groupMembers.contains(testUser), "User should no longer be in the group.");
    }

    @Test
    public void testCannotRemoveLastAdmin() {
        // Add an admin to the group
        specialAccessManager.addUserToGroup(testGroup, testInstructor, true, true);

        // Attempt to remove the last admin
        boolean removed = specialAccessManager.removeUserFromGroup(testGroup, testInstructor);
        assertFalse(removed, "The last admin in the group should not be removable.");

        Set<String> groupAdmins = specialAccessManager.getGroupAdmins(testGroup);
        assertTrue(groupAdmins.contains(testInstructor), "The last admin should still be in the group.");
    }

    @Test
    public void testIsAdminInGroup() {
        // Add an admin to the group
        specialAccessManager.addUserToGroup(testGroup, testInstructor, true, true);

        boolean isAdmin = specialAccessManager.isAdminInGroup(testGroup, testInstructor);
        assertTrue(isAdmin, "The instructor should be an admin in the group.");
    }

    @Test
    public void testGetGroupMembersAndAdmins() {
        // Add users and an admin
        specialAccessManager.addUserToGroup(testGroup, testUser, false, false);
        specialAccessManager.addUserToGroup(testGroup, testInstructor, true, true);

        Set<String> groupMembers = specialAccessManager.getGroupMembers(testGroup);
        Set<String> groupAdmins = specialAccessManager.getGroupAdmins(testGroup);

        assertTrue(groupMembers.contains(testUser), "Group members should include the test user.");
        assertTrue(groupAdmins.contains(testInstructor), "Group admins should include the instructor.");
    }
}
