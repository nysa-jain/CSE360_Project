package cse360project1;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;

/**
 * The SetupAccountWindow class allows users to finalize their account setup by providing additional information
 * such as their role in the system (Student or Instructor).
 */
public class SetupAccountWindow extends Application {

    private AdminApp adminApp; // Instance of AdminApp to handle user account management
    private File userD; // File representing the user database
    private String fullName; // User's full name
    private String email; // User's email address
    private String password; // User's password
    private File codeD; // File representing the code database

    /**
     * Constructor for the SetupAccountWindow.
     * 
     * @param adminApp The AdminApp instance for managing accounts.
     * @param userD The file where user data is stored.
     * @param fullName The user's full name.
     * @param email The user's email address.
     * @param password The user's password.
     * @param codeD The file where invitation codes are stored.
     */
    public SetupAccountWindow(AdminApp adminApp, File userD, String fullName, String email, String password, File codeD) {
        this.adminApp = adminApp;
        this.userD = userD;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.codeD = codeD;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Finish Setting Up Account"); // Set the title of the setup window

        VBox layout = new VBox(10); // Create a vertical box layout with spacing

        // Create fields to display the user's full name, email, and password
        TextField fullNameField = new TextField(fullName);
        fullNameField.setDisable(true); // Disable editing since the name is provided

        TextField emailField = new TextField(email);
        emailField.setDisable(true); // Disable editing since the email is provided

        PasswordField passwordField = new PasswordField(); // Create a password field for the user's password
        passwordField.setText(password); // Set the provided password
        passwordField.setDisable(true); // Disable editing since the password is provided

        // ComboBox to select the user's role (Student or Instructor)
        ComboBox<String> roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll("Student", "Instructor"); // Add roles to the ComboBox
        roleComboBox.setValue("Student"); // Set default value to "Student"

        Button finishButton = new Button("Finish Setup"); // Button to finish the account setup
        Label statusLabel = new Label(); // Label to display the status of account creation

        // Action event for the finish button
        finishButton.setOnAction(e -> {
            String role = roleComboBox.getValue(); // Get the selected role
            // Attempt to add the user account using the provided details
            if (adminApp.addUserAccount(email, password, role, userD)) {
                statusLabel.setText("Account created successfully!"); // Success message
            } else {
                statusLabel.setText("Account creation failed. User may already exist."); // Failure message
            }
        });

        // Add all components to the layout
        layout.getChildren().addAll(
            new Label("Full Name:"), fullNameField,
            new Label("Email:"), emailField,
            new Label("Role:"), roleComboBox,
            finishButton, statusLabel);

        Scene scene = new Scene(layout, 300, 300); // Create a scene with the layout
        primaryStage.setScene(scene); // Set the scene for the primary stage
        primaryStage.show(); // Display the primary stage
    }
}