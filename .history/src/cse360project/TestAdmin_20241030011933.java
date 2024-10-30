package cse360project1;

import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * TestAdminApp is a JUnit test class that tests the functionality of the AdminApp class.
 * It includes tests for admin login, user account management, and invitation code generation and validation.
 */
class TestAdminApp {
    AdminApp adminApp; // Instance of AdminApp for testing
    File adminD; // File for storing admin data
    File userD; // File for storing user data
    File codeD; // File for storing invitation codes

    /**
     * Sets up the test environment before each test.
     * Initializes AdminApp and prepares the database files by writing test data.
     */
    @BeforeEach
    void setUp() throws Exception {
        adminApp = new AdminApp(); // Create a new instance of AdminApp
        adminD = new File("AdminDatabase.txt"); // Initialize admin database file
        userD = new File("UserDatabase.txt"); // Initialize user database file
        codeD = new File("CodeDatabase.txt"); // Initialize code database file

        // Clean up files or create fresh files for testing to avoid leftover data from previous tests
        try (FileWriter adminWriter = new FileWriter(adminD)) {
            adminWriter.write("admin:" + adminApp.hashPassword("securepassword") + ":Admin\n"); // Write admin data
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

    /**
     * Tests admin login with correct credentials.
     * Asserts that the authentication is successful.
     */
    @Test
    void testAdminLoginWithCorrectCredentials() {
        String userName = "admin";
        String password = "securepassword";
        assertTrue(adminApp.checkAdminAuthentication(userName, password, adminD), 
            "Admin login failed with correct credentials");
    }

    /**
     * Tests admin login with incorrect password.
     * Asserts that authentication fails.
     */
    @Test
    void testAdminLoginWithIncorrectPassword() {
        String userName = "admin";
        String password = "wrongpassword";
        assertFalse(adminApp.checkAdminAuthentication(userName, password, adminD), 
            "Admin login passed with incorrect password");
    }

    /**
     * Tests the generation of an invitation code.
     * Asserts that the generated code is not null.
     */
    @Test
    void testGenerateInvitationCode() {
        String role = "Student";
        String code = adminApp.generateInvitationCode(role, codeD);
        assertNotNull(code, "Failed to generate an invitation code");
    }

    /**
     * Tests the validation of a correct invitation code.
     * Asserts that the validation succeeds.
     */
    @Test
    void testValidateCorrectInvitationCode() {
        String role = "Student";
        String code = adminApp.generateInvitationCode(role, codeD);
        assertTrue(adminApp.validateInvitationCode(code, role, codeD), 
            "Failed to validate a correct invitation code");
    }

    /**
     * Tests the validation of an incorrect invitation code.
     * Asserts that the validation fails.
     */
    @Test
    void testValidateIncorrectInvitationCode() {
        String role = "Student";
        String code = adminApp.generateInvitationCode(role, codeD);
        assertFalse(adminApp.validateInvitationCode("wrongcode", role, codeD), 
            "Validation passed with an incorrect invitation code");
    }

    /**
     * Tests adding a new user account.
     * Asserts that the account creation is successful.
     */
    @Test
    void testAddingNewUserAccount() {
        String userName = "newUser";
        String password = "newPassword";
        String role = "Student";
        assertTrue(adminApp.addUserAccount(userName, password, role, userD), 
            "Failed to add a new user account");
    }

    /**
     * Tests adding an existing user account.
     * Asserts that adding the account fails since it already exists.
     */
    @Test
    void testAddingExistingUserAccount() {
        String userName = "existingUser";
        String password = "existingPassword";
        String role = "Student";
        adminApp.addUserAccount(userName, password, role, userD); // Create the user account first
        assertFalse(adminApp.addUserAccount(userName, password, role, userD), 
            "Added a user account that already exists");
    }

    /**
     * Tests removing an existing user account.
     * Asserts that the account removal is successful.
     */
    @Test
    void testRemovingExistingUserAccount() {
        String userName = "removeUser";
        String password = "removePassword";
        String role = "Student";
        adminApp.addUserAccount(userName, password, role, userD); // Create the user account first
        assertTrue(adminApp.removeAccount(userName, userD), 
            "Failed to remove an existing user account");
    }

    /**
     * Tests removing a non-existent user account.
     * Asserts that the removal fails.
     */
    @Test
    void testRemovingNonExistentUserAccount() {
        String userName = "nonExistentUser";
        assertFalse(adminApp.removeAccount(userName, userD), 
            "Removed a non-existent user account");
    }

    /**
     * Tests consistency of password hashing.
     * Asserts that hashing the same password yields the same result.
     */
    @Test
    void testHashPasswordConsistency() {
        String password = "consistentPassword";
        String hash1 = adminApp.hashPassword(password);
        String hash2 = adminApp.hashPassword(password);
        assertEquals(hash1, hash2, "Hashing the same password should yield the same result");
    }

    /**
     * Tests uniqueness of password hashing.
     * Asserts that hashing different passwords yields different results.
     */
    @Test
    void testHashPasswordUniqueness() {
        String password1 = "password1";
        String password2 = "password2";
        String hash1 = adminApp.hashPassword(password1);
        String hash2 = adminApp.hashPassword(password2);
        assertNotEquals(hash1, hash2, "Different passwords should have different hashes");
    }

    /**
     * Tests checking for an existing username.
     * Asserts that the existing username can be found in the database.
     */
    @Test
    void testCheckUsernameExist() {
        String userName = "testUser";
        String password = "testPassword";
        String role = "Student";
        adminApp.addUserAccount(userName, password, role, userD); // Create the user account first
        assertTrue(adminApp.checkUsernameExist(userName, userD), 
            "Failed to find an existing username");
    }

    /**
     * Tests checking for a non-existing username.
     * Asserts that the non-existing username cannot be found in the database.
     */
    @Test
    void testCheckUsernameDoesNotExist() {
        assertFalse(adminApp.checkUsernameExist("nonExistingUser", userD), 
            "Non-existing username check returned true");
    }
}