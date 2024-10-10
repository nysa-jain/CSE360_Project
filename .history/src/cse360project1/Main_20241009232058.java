package cse360project1;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;

/**
 * The Main class serves as the entry point for the CSE 360 Help System application.
 * It initializes the user interface and manages the flow between admin and user logins.
 */
public class Main extends Application {

    // File objects representing the databases for users, admins, and invitation codes
    private final File userDatabaseFile = new File("UserDatabase.txt");
    private final File adminDatabaseFile = new File("AdminDatabase.txt");
    private final File codeDatabaseFile = new File("CodeDatabase.txt");
    private final AdminApp adminApp = new AdminApp(); // Instance of AdminApp for admin functionalities

    @Override
    public void start(Stage primaryStage) {
        // Initialize the admin database if not already initialized
        adminApp.initializeAdminDatabase(adminDatabaseFile); // Ensure the admin database is set up

        primaryStage.setTitle("CSE 360 Help System"); // Set the title of the primary stage

        VBox layout = new VBox(10); // Create a vertical box layout with spacing
        layout.setAlignment(Pos.CENTER); // Center align the layout

        // Create welcome label and buttons for admin and user login
        Label welcomeLabel = new Label("Welcome to the CSE 360 Help System");
        Button adminButton = new Button("Admin"); // Button to open the admin login window
        Button userButton = new Button("User"); // Button to open the user login window

        // Set action for the admin button
        adminButton.setOnAction(e -> {
            // Create and display the AdminLoginWindow
            AdminLoginWindow loginWindow = new AdminLoginWindow(adminApp, adminDatabaseFile, userDatabaseFile, codeDatabaseFile);
            loginWindow.start(new Stage()); // Open a new stage for admin login
        });

        // Set action for the user button
        userButton.setOnAction(e -> {
            // Create and display the UserLoginWindow
            UserLoginWindow userWindow = new UserLoginWindow();
            userWindow.start(new Stage()); // Open a new stage for user login
        });

        // Add the welcome label and buttons to the layout
        layout.getChildren().addAll(welcomeLabel, adminButton, userButton);
        
        // Create a scene with the specified layout and dimensions
        Scene scene = new Scene(layout, 300, 200);
        primaryStage.setScene(scene); // Set the scene for the primary stage
        primaryStage.show(); // Display the primary stage
    }

    public static void main(String[] args) {
        launch(args); // Launch the JavaFX application
    }
}