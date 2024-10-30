package cse360project1;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;

/**
 * The StudentWindow class allows students to register by creating an account using their username,
 * password, and verification code.
 */
public class StudentWindow extends Application {

    private user userApp; // Instance of the user class to manage user accounts
    private AdminApp adminApp; // Instance of AdminApp to validate verification codes
    private File userD; // File representing the user database

    @Override
    public void start(Stage primaryStage) {
        userApp = new user(); // Initialize the user application
        adminApp = new AdminApp(); // Initialize the admin application
        userD = new File("UserDatabase.txt"); // Specify the user database file

        primaryStage.setTitle("Student Registration"); // Set the title of the registration window

        // UI elements for student registration
        Label welcomeLabel = new Label("Welcome to Student Registration"); // Welcome label
        Label userNameLabel = new Label("Username:"); // Username label
        TextField userNameField = new TextField(); // Input field for username
        Label passwordLabel = new Label("Password:"); // Password label
        PasswordField passwordField = new PasswordField(); // Input field for password
        Label verificationCodeLabel = new Label("Verification Code:"); // Verification code label
        TextField verificationCodeField = new TextField(); // Input field for verification code
        Label statusLabel = new Label(); // Label to display registration status messages

        Button registerButton = new Button("Register"); // Button to initiate registration

        // Register button action event
        registerButton.setOnAction(e -> {
            String userName = userNameField.getText(); // Get the entered username
            String password = passwordField.getText(); // Get the entered password
            String verificationCode = verificationCodeField.getText(); // Get the entered verification code

            // Validate the verification code and register the user
            if (adminApp.validateVerificationCode(userName, verificationCode, "Student", userD)) {
                // If verification code is valid, attempt to create the user account
                if (userApp.addUserAccount(userName, password, userD)) {
                    statusLabel.setText("Account created successfully!"); // Success message
                    verificationCodeField.setDisable(true); // Disable code input after successful registration
                } else {
                    statusLabel.setText("Account already created."); // Message for duplicate account
                }
            } else {
                statusLabel.setText("Invalid verification code."); // Message for invalid verification code
            }
        });

        // Layout setup for the registration window
        VBox root = new VBox(10, welcomeLabel, userNameLabel, userNameField, passwordLabel, passwordField, verificationCodeLabel, verificationCodeField, registerButton, statusLabel);
        Scene scene = new Scene(root, 400, 400); // Create a new scene with specified dimensions
        primaryStage.setScene(scene); // Set the scene for the primary stage
        primaryStage.show(); // Display the primary stage
    }

    public static void main(String[] args) {
        launch(args); // Launch the JavaFX application
    }
}