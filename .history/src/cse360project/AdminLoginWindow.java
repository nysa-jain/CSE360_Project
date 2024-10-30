package cse360project;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;

/**
 * The AdminLoginWindow class is a JavaFX application window that provides
 * the login interface for administrators. It checks the credentials provided
 * by the admin and opens the Admin Dashboard upon successful login.
 */
public class AdminLoginWindow extends Application {

    private final AdminApp adminApp; // Instance of AdminApp to handle authentication logic
    private final File adminDatabaseFile; // File object representing the admin database
    private final File userDatabaseFile; // File object representing the user database
    private final File codeDatabaseFile; // File object representing the invitation code database

    /**
     * Constructor for AdminLoginWindow.
     * Initializes the login window with the provided AdminApp instance and necessary database files.
     *
     * @param adminApp          The AdminApp object for handling admin authentication and operations.
     * @param adminDatabaseFile  The file representing the admin database.
     * @param userDatabaseFile   The file representing the user database.
     * @param codeDatabaseFile   The file representing the invitation code database.
     */
    public AdminLoginWindow(AdminApp adminApp, File adminDatabaseFile, File userDatabaseFile, File codeDatabaseFile) {
        this.adminApp = adminApp;
        this.adminDatabaseFile = adminDatabaseFile;
        this.userDatabaseFile = userDatabaseFile;
        this.codeDatabaseFile = codeDatabaseFile;
    }

    /**
     * The start method initializes the JavaFX UI for the Admin Login window.
     * It creates the layout with fields for entering username and password, and a button to handle login actions.
     *
     * @param primaryStage The primary stage for this application, provided by JavaFX.
     */
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Admin Login"); // Set the title of the window

        // Create a vertical box (VBox) layout with spacing between elements
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER); // Center align all the elements in the VBox

        // Create UI components for the login window
        Label usernameLabel = new Label("Username:"); // Label for the username field
        TextField usernameField = new TextField(); // Text field for entering the username
        Label passwordLabel = new Label("Password:"); // Label for the password field
        PasswordField passwordField = new PasswordField(); // Password field to enter the password (hides input)
        Label statusLabel = new Label(); // Label to display the login status (errors or success)

        Button loginButton = new Button("Login"); // Button for triggering the login action

        // Set the action to be performed when the "Login" button is clicked
        loginButton.setOnAction(e -> {
            // Retrieve the username and password entered by the admin
            String username = usernameField.getText();
            String password = passwordField.getText();

            // Check if the provided credentials are valid using the adminApp's authentication method
            if (adminApp.checkAdminAuthentication(username, password, adminDatabaseFile)) {
                // If authentication is successful, launch the Admin Dashboard and close the login window
                AdminDashboard adminDashboard = new AdminDashboard(adminApp, userDatabaseFile, codeDatabaseFile);
                adminDashboard.start(new Stage()); // Start the Admin Dashboard in a new stage
                primaryStage.close(); // Close the current login window
            } else {
                // If authentication fails, display an error message on the status label
                statusLabel.setText("Invalid username or password.");
            }
        });

        // Add all UI components to the layout
        layout.getChildren().addAll(usernameLabel, usernameField, passwordLabel, passwordField, loginButton, statusLabel);

        // Create a new scene with the layout and set the stage properties
        Scene scene = new Scene(layout, 300, 200); // Scene with width 300 and height 200
        primaryStage.setScene(scene); // Set the scene for the primary stage
        primaryStage.show(); // Display the stage
    }
}