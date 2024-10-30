package cse360project1;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;

/**
 * The InstructorWindow class represents a JavaFX application that allows instructors to register
 * for an account using a username, password, and invitation code.
 */
public class InstructorWindow extends Application {

    private user userApp; // Instance of user to handle user account operations
    private AdminApp adminApp; // Instance of AdminApp to manage admin-related functionalities
    private File userD; // File object representing the user database
    private File codeD; // File object representing the code database

    /**
     * Constructor for InstructorWindow.
     * Initializes the instructor registration interface with the provided AdminApp instance
     * and user and code database files.
     *
     * @param adminApp The AdminApp object for handling admin actions.
     * @param userD    The file representing the user database.
     * @param codeD    The file representing the code database.
     */
    public InstructorWindow(AdminApp adminApp, File userD, File codeD) {
        this.adminApp = adminApp; // Initialize the adminApp instance
        this.userD = userD; // Initialize the user database file
        this.codeD = codeD; // Initialize the code database file
    }

    @Override
    public void start(Stage primaryStage) {
        userApp = new user(); // Create an instance of the user class
        
        primaryStage.setTitle("Instructor Registration"); // Set the title of the registration window

        // Create labels and input fields for the registration form
        Label welcomeLabel = new Label("Welcome to Instructor Registration");
        Label userNameLabel = new Label("Username:");
        TextField userNameField = new TextField(); // Text field for entering username
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField(); // Password field for entering password
        Label verificationCodeLabel = new Label("Invitation Code:");
        TextField verificationCodeField = new TextField(); // Text field for entering invitation code
        Label statusLabel = new Label(); // Label to display registration status messages

        Button registerButton = new Button("Register"); // Button to submit the registration

        // Set action for the register button
        registerButton.setOnAction(e -> {
            String userName = userNameField.getText(); // Get entered username
            String password = passwordField.getText(); // Get entered password
            String invitationCode = verificationCodeField.getText(); // Get entered invitation code

            // Validate the invitation code and register
            if (adminApp.validateInvitationCode(invitationCode, "Instructor", codeD)) {
                // Attempt to add the user account
                if (userApp.addUserAccount(userName, password, userD)) {
                    statusLabel.setText("Account created successfully!"); // Success message
                    verificationCodeField.setDisable(true); // Disable code input after successful registration
                } else {
                    statusLabel.setText("Account already created."); // Message if account already exists
                }
            } else {
                statusLabel.setText("Invalid invitation code for the role."); // Message for invalid invitation code
            }
        });

        // Create a vertical box layout for the registration form
        VBox root = new VBox(10, welcomeLabel, userNameLabel, userNameField, passwordLabel, passwordField,
                verificationCodeLabel, verificationCodeField, registerButton, statusLabel);
        Scene scene = new Scene(root, 400, 400); // Create a scene with the specified layout and dimensions
        primaryStage.setScene(scene); // Set the scene for the primary stage
        primaryStage.show(); // Display the primary stage
    }

    public static void main(String[] args) {
        launch(args); // Launch the JavaFX application
    }
}