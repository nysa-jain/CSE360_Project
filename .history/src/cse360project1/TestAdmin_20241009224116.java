package cse360project1;

import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestAdminApp {
    AdminApp adminApp;
    File adminD;
    File userD;
    File codeD;

    @BeforeEach
    void setUp() throws Exception {
        adminApp = new AdminApp();
        adminD = new File("AdminDatabase.txt");
        userD = new File("UserDatabase.txt");
        codeD = new File("CodeDatabase.txt");

        // Clean up files or create fresh files for testing to avoid leftover data from previous tests
        try (FileWriter adminWriter = new FileWriter(adminD)) {
            adminWriter.write("admin:" + adminApp.hashPassword("securepassword") + ":Admin\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (FileWriter userWriter = new FileWriter(userD)) {
            userWriter.write(""); // Clear user database content for fresh tests
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (FileWriter codeWriter = new FileWriter(codeD)) {
            codeWriter.write(""); // Clear code database content for fresh tests
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testAdminLoginWithCorrectCredentials() {
        String userName = "admin";
        String password = "securepassword";
        assertTrue(adminApp.checkAdminAuthentication(userName, password, adminD), 
            "Admin login failed with correct credentials");
    }

    @Test
    void testAdminLoginWithIncorrectPassword() {
        String userName = "admin";
        String password = "wrongpassword";
        assertFalse(adminApp.checkAdminAuthentication(userName, password, adminD), 
            "Admin login passed with incorrect password");
    }

    @Test
    void testGenerateInvitationCode() {
        String role = "Student";
        String code = adminApp.generateInvitationCode(role, codeD);
        assertNotNull(code, "Failed to generate an invitation code");
    }

    @Test
    void testValidateCorrectInvitationCode() {
        String role = "Student";
        String code = adminApp.generateInvitationCode(role, codeD);
        assertTrue(adminApp.validateInvitationCode(code, role, codeD), 
            "Failed to validate a correct invitation code");
    }

    @Test
    void testValidateIncorrectInvitationCode() {
        String role = "Student";
        String code = adminApp.generateInvitationCode(role, codeD);
        assertFalse(adminApp.validateInvitationCode("wrongcode", role, codeD), 
            "Validation passed with an incorrect invitation code");
    }

    @Test
    void testAddingNewUserAccount() {
        String userName = "newUser";
        String password = "newPassword";
        String role = "Student";
        assertTrue(adminApp.addUserAccount(userName, password, role, userD), 
            "Failed to add a new user account");
    }

    @Test
    void testAddingExistingUserAccount() {
        String userName = "existingUser";
        String password = "existingPassword";
        String role = "Student";
        adminApp.addUserAccount(userName, password, role, userD);
        assertFalse(adminApp.addUserAccount(userName, password, role, userD), 
            "Added a user account that already exists");
    }

    @Test
    void testRemovingExistingUserAccount() {
        String userName = "removeUser";
        String password = "removePassword";
        String role = "Student";
        adminApp.addUserAccount(userName, password, role, userD);
        assertTrue(adminApp.removeAccount(userName, userD), 
            "Failed to remove an existing user account");
    }

    @Test
    void testRemovingNonExistentUserAccount() {
        String userName = "nonExistentUser";
        assertFalse(adminApp.removeAccount(userName, userD), 
            "Removed a non-existent user account");
    }

    @Test
    void testHashPasswordConsistency() {
        String password = "consistentPassword";
        String hash1 = adminApp.hashPassword(password);
        String hash2 = adminApp.hashPassword(password);
        assertEquals(hash1, hash2, "Hashing the same password should yield the same result");
    }

    @Test
    void testHashPasswordUniqueness() {
        String password1 = "password1";
        String password2 = "password2";
        String hash1 = adminApp.hashPassword(password1);
        String hash2 = adminApp.hashPassword(password2);
        assertNotEquals(hash1, hash2, "Different passwords should have different hashes");
    }

    @Test
    void testCheckUsernameExist() {
        String userName = "testUser";
        String password = "testPassword";
        String role = "Student";
        adminApp.addUserAccount(userName, password, role, userD);
        assertTrue(adminApp.checkUsernameExist(userName, userD), 
            "Failed to find an existing username");
    }

    @Test
    void testCheckUsernameDoesNotExist() {
        assertFalse(adminApp.checkUsernameExist("nonExistingUser", userD), 
            "Non-existing username check returned true");
    }
}
