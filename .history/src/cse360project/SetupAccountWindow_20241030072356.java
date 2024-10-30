package cse360project;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.File;

/**
 * The SetupAccountWindow class allows users to finalize their account setup by providing additional information
 * such as their role in the system (Student or Instructor).
 */
public class SetupAccountWindow extends Application {

    private AdminApp adminApp; // Instance of AdminApp to handle user account management
    private File userD; // File representing the user database
    private String UserName; // User's full name
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
    public SetupAccountWindow(AdminApp adminApp, File userD, String UserName, String email, String password, File codeD) {
        this.adminApp = adminApp;
        this.userD = userD;
        this.UserName = UserName;
        this.email = email;
        this.password = password;
        this.codeD = codeD;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Set Up Your Account"); // Set the title of the setup window

        // Create a GridPane layout for better organization
        GridPane layout = new GridPane();
        layout.setPadding(new Insets(20)); // Set padding around the layout
        layout.setVgap(10); // Vertical spacing
        layout.setHgap(10); // Horizontal spacing
        layout.setAlignment(Pos.CENTER); // Center align the layout

        // Create fields to display the user's full name, email, and password
        TextField userNameField = new TextField(UserName);
        TextField emailField = new TextField(email);
        PasswordField passwordField = new PasswordField(); // Create a password field for the user's password
        passwordField.setText(password); // Set the provided password

        // ComboBox to select the user's role (Student or Instructor)
        ComboBox<String> roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll("Student", "Instructor"); // Add roles to the ComboBox
        roleComboBox.setValue("Student"); // Set default value to "Student"

        TextField invitationCodeField = new TextField();
        invitationCodeField.setPromptText("Invitation Code"); // Placeholder text

        Button finishButton = new Button("Finish Setup"); // Button to finish the account setup
        Label statusLabel = new Label(); // Label to display the status of account creation

        // Style the buttons
        finishButton.setStyle("-fx-font-size: 14px; -fx-padding: 10px; -fx-background-color: #4CAF50; -fx-text-fill: white;");

        // Action event for the finish button
        finishButton.setOnAction(e -> {
            String role = roleComboBox.getValue(); // Get the selected role
            String invitationCode = invitationCodeField.getText(); // Get the invitation code
            String userName = userNameField.getText(); // Get the updated user name
            String password = passwordField.getText(); // Get the updated password

            // Validate the invitation code and proceed to setup
            if (adminApp.validateInvitationCode(invitationCode, "Student", codeD) || 
                adminApp.validateInvitationCode(invitationCode, "Instructor", codeD)) {
                
                // Attempt to add the user account using the provided details
                if (adminApp.addUserAccount(userName, password, role, userD)) {
                    statusLabel.setText("Account created successfully!"); // Success message
                } else {
                    statusLabel.setText("Account creation failed. User may already exist."); // Failure message
                }
            } else {
                statusLabel.setText("Invalid invitation code."); // Update status label for invalid code
            }
        });

        // Add all components to the layout
        layout.add(new Label("User Name:"), 0, 0);
        layout.add(userNameField, 1, 0);
        layout.add(new Label("Email:"), 0, 1);
        layout.add(emailField, 1, 1);
        layout.add(new Label("Password:"), 0, 2);
        layout.add(passwordField, 1, 2);
        layout.add(new Label("Invitation Code:"), 0, 3);
        layout.add(invitationCodeField, 1, 3);
        layout.add(new Label("Role:"), 0, 4);
        layout.add(roleComboBox, 1, 4);
        layout.add(finishButton, 0, 5, 2, 1); // Span the finish button across two columns
        layout.add(statusLabel, 0, 6, 2, 1); // Span the status label across two columns

        Scene scene = new Scene(layout, 600, 600); // Create a scene with the layout
        primaryStage.setScene(scene); // Set the scene for the primary stage
        primaryStage.show(); // Display the primary stage
    }
}