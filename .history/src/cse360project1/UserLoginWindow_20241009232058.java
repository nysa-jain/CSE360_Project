package cse360project1;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;

public class UserLoginWindow extends Application {

    private AdminApp adminApp; // Instance of AdminApp to handle administrative tasks
    private File userD; // File reference for user database
    private File codeD; // File reference for code database

    @Override
    public void start(Stage primaryStage) {
        // Initialize AdminApp and database files
        adminApp = new AdminApp();
        userD = new File("UserDatabase.txt");
        codeD = new File("CodeDatabase.txt");

        // Set the title of the primary stage
        primaryStage.setTitle("User Registration");

        // Create a vertical layout for the UI components
        VBox layout = new VBox(10);

        // Create text fields for user input
        TextField fullNameField = new TextField();
        fullNameField.setPromptText("Full Name"); // Placeholder text

        TextField emailField = new TextField();
        emailField.setPromptText("Email"); // Placeholder text

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password"); // Placeholder text

        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm Password"); // Placeholder text

        TextField invitationCodeField = new TextField();
        invitationCodeField.setPromptText("Invitation Code"); // Placeholder text

        // Create a button for proceeding to the next step
        Button nextButton = new Button("Next");
        Label statusLabel = new Label(); // Label to display status messages

        // Set action for the next button
        nextButton.setOnAction(e -> {
            // Get input values from the text fields
            String fullName = fullNameField.getText();
            String email = emailField.getText();
            String password = passwordField.getText();
            String confirmPassword = confirmPasswordField.getText();
            String code = invitationCodeField.getText();

            // Check if passwords match
            if (!password.equals(confirmPassword)) {
                statusLabel.setText("Passwords do not match."); // Update status label
                return; // Exit method if passwords do not match
            }

            // Validate the invitation code and proceed to setup
            if (adminApp.validateInvitationCode(code, "Student", codeD) || 
                adminApp.validateInvitationCode(code, "Instructor", codeD)) {
                // If valid, create and start the SetupAccountWindow
                SetupAccountWindow setupWindow = new SetupAccountWindow(
                        adminApp, userD, fullName, email, password, codeD);
                setupWindow.start(new Stage()); // Start the new window
                primaryStage.close(); // Close the current stage
            } else {
                statusLabel.setText("Invalid invitation code."); // Update status label for invalid code
            }
        });

        // Add all components to the layout
        layout.getChildren().addAll(
            fullNameField, emailField, passwordField, confirmPasswordField,
            invitationCodeField, nextButton, statusLabel);

        // Create a scene with the layout and set it to the primary stage
        Scene scene = new Scene(layout, 300, 400);
        primaryStage.setScene(scene);
        primaryStage.show(); // Show the primary stage
    }

    // Main method to launch the JavaFX application
    public static void main(String[] args) {
        launch(args);
    }
}