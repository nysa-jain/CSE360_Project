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
        adminApp.initializeAdminDatabase(adminDatabaseFile); // Ensure the admin database is set up

        primaryStage.setTitle("CSE 360 Help System"); // Set the title of the primary stage

        WindowsManager window = new WindowsManager();
        window.start(primaryStage); // Open a new stage for admin login
    }

    public static void main(String[] args) {
        launch(args); // Launch the JavaFX application
    }
}