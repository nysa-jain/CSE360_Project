package cse360project1;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;

/**
 * The AdminWindow class represents a JavaFX stage that serves as the main
 * interface for administrators to manage users and generate verification codes.
 */
public class AdminWindow extends Stage {

    private AdminApp adminApp; // Instance of AdminApp to handle user management operations
    private File userD; // File object representing the user database

    /**
     * Constructor for AdminWindow.
     * Initializes the admin interface with the provided AdminApp instance and user database file.
     *
     * @param adminDatabase The AdminApp object for handling admin actions.
     * @param userD        The file representing the user database.
     * @param adminD       The file representing the admin database (not currently used in this class).
     */
    public AdminWindow(AdminApp adminDatabase, File userD, File adminD) {
        this.adminApp = adminDatabase; // Initialize the adminApp instance
        this.userD = userD; // Initialize the user database file

        // Create a vertical box (VBox) layout with spacing between elements
        VBox adminLayout = new VBox(10);

        // Buttons for admin actions
        Button createUserButton = new Button("Create User"); // Button to open the user creation window
        Button deleteUserButton = new Button("Delete User"); // Button to open the user deletion window
        Button viewUsersButton = new Button("View Users"); // Button to view all users
        Button generateCodeButton = new Button("Generate Verification Code"); // Button to generate verification codes

        Label statusLabel = new Label(); // Label to display status messages

        // Set actions for each button
        createUserButton.setOnAction(event -> openCreateUserWindow()); // Open user creation window
        deleteUserButton.setOnAction(event -> openDeleteUserWindow()); // Open user deletion window
        viewUsersButton.setOnAction(event -> viewAllUsers()); // Display all users
        generateCodeButton.setOnAction(event -> openGenerateCodeWindow()); // Open window to generate verification code

        // Add all buttons and the status label to the layout
        adminLayout.getChildren().addAll(createUserButton, deleteUserButton, viewUsersButton, generateCodeButton, statusLabel);
        adminLayout.setAlignment(Pos.TOP_CENTER); // Align the elements to the top center

        // Create a new scene with the layout and set it for the stage
        Scene adminScene = new Scene(adminLayout, 400, 300); // Scene with width 400 and height 300
        setScene(adminScene); // Set the scene for this stage
    }

    // Method to open a window for creating a user (with role and password)
    private void openCreateUserWindow() {
        // Logic to open a window for creating a user (not yet implemented)
    }

    // Method to open a window for deleting a user
    private void openDeleteUserWindow() {
        // Logic to open a window for deleting a user (not yet implemented)
    }

    // Method to display all users in the user database
    private void viewAllUsers() {
        // Logic to display all users in the user database (not yet implemented)
    }

    // Method to open a window for generating an invitation code for a specific role
    private void openGenerateCodeWindow() {
        // Logic to generate an invitation code for a specific role (not yet implemented)
    }
}