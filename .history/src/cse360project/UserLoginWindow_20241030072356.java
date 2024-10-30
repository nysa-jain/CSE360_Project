package cse360project;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

public class UserLoginWindow extends Application {

    private AdminApp adminApp; // Instance of AdminApp to handle administrative tasks
    private File userD; // File reference for user database
    private File codeD; // File reference for code database
    private File adminD;

    @Override
    public void start(Stage primaryStage) {
        // Initialize AdminApp and database files
        adminApp = new AdminApp();
        userD = new File("UserDatabase.txt");
        codeD = new File("CodeDatabase.txt");
        adminD = new File("AdminDatabase.txt");

        // Set the title of the primary stage
        primaryStage.setTitle("User Login");

        // Create a GridPane layout for the UI components
        GridPane layout = new GridPane();
        layout.setPadding(new Insets(20)); // Add padding around the layout
        layout.setVgap(10); // Set vertical spacing
        layout.setHgap(10); // Set horizontal spacing
        layout.setAlignment(Pos.CENTER); // Center align the layout

        // Create a label for the user login options
        Label welcomeLabel = new Label("Welcome! Please log in or sign up.");
        welcomeLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;"); // Style the welcome label

        // Create buttons for login and signup
        Button loginButton = new Button("Login");
        Button signUpButton = new Button("New User? Sign Up");

        // Create text fields for user input
        TextField UserNameField = new TextField();
        UserNameField.setPromptText("User Name"); // Placeholder text
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password"); // Placeholder text

        // Status label for messages
        Label statusLabel = new Label();

        // ComboBox for role selection
        ComboBox<String> roleComboBox = new ComboBox<>();
        roleComboBox.setPromptText("Select Role");

        // Set action for the login button
        loginButton.setOnAction(e -> {
            String username = UserNameField.getText();
            String password = passwordField.getText();

            // Check user authentication and retrieve roles
            List<String> roles = UserInfo.getRoles(username, password, userD);
            if (roles != null && !roles.isEmpty()) {
                statusLabel.setText("Login successful!"); // Success message

                // Populate the role selection ComboBox
                roleComboBox.getItems().clear(); // Clear previous items
                for (String role : roles) { // Loop through each role
                    roleComboBox.getItems().add(role); // Add each role separately
                }
                roleComboBox.setVisible(true); // Show the ComboBox for role selection

                // Create a button to proceed to the selected role
                Button proceedButton = new Button("Proceed");
                proceedButton.setOnAction(event -> {
                    String selectedRole = roleComboBox.getValue();
                    if (selectedRole != null) {
                        statusLabel.setText("Opening view for " + selectedRole); // Status update

                        // Redirect to the corresponding view based on the user role
                        switch (selectedRole) {
                            case "Student":
                                new StudentWindow().start(new Stage()); // Open Student View
                                break;
                            case "Instructor":
                                new InstructorWindow().start(new Stage()); // Open Instructor Window
                                break;
                            case "Admin":
                                new AdminDashboard(adminApp, adminD, userD, null).start(new Stage()); // Open Admin View
                                break;
                            default:
                                statusLabel.setText("Unknown role selected.");
                                break;
                        }
                    } else {
                        statusLabel.setText("Please select a role.");
                    }
                });

                // Add the ComboBox and proceed button to the layout
                layout.add(roleComboBox, 0, 5, 2, 1); // Add role selection ComboBox
                layout.add(proceedButton, 0, 6, 2, 1); // Add proceed button
                proceedButton.setVisible(true); // Show the proceed button
            } else {
                statusLabel.setText("Invalid username or password."); // Failure message
            }
        });

        // Set action for the sign-up button
        signUpButton.setOnAction(e -> {
            // Open the registration window
            SetupAccountWindow setupWindow = new SetupAccountWindow(adminApp, userD, "", "", "", codeD);
            setupWindow.start(new Stage()); // Start the new window
            primaryStage.close(); // Close the login window
        });

        // Add all components to the layout
        layout.add(welcomeLabel, 0, 0, 2, 1); // Span the welcome label across two columns
        layout.add(UserNameField, 0, 1);
        layout.add(passwordField, 0, 2);
        layout.add(loginButton, 0, 3);
        layout.add(signUpButton, 1, 3);
        layout.add(statusLabel, 0, 4, 2, 1); // Status label spanning two columns

        // Create a scene with the layout and set it to the primary stage
        Scene scene = new Scene(layout, 600, 600);
        primaryStage.setScene(scene);
        primaryStage.show(); // Show the primary stage
    }
}