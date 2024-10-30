package cse360project;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;

/**
 * The AdminDashboard class extends JavaFX's Application class to create the main dashboard for
 * the Admin. The dashboard provides options to manage users and generate invitation codes.
 * It interacts with the AdminApp class to perform various administrative tasks.
 */
public class AdminDashboard extends Application {

    private final AdminApp adminApp; // Instance of the AdminApp to handle logic related to admin tasks
    private final File userDatabaseFile; // File object representing the user database
    private final File codeDatabaseFile; // File object representing the code database
    private final HelpArticleManager helpArticleManager; // Instance of HelpArticleManager for articles

    /**
     * Constructor for the AdminDashboard.
     * Initializes the dashboard with the given AdminApp instance and the user/code database files.
     *
     * @param adminApp The AdminApp object for handling admin operations.
     * @param userDatabaseFile The file representing the user database.
     * @param codeDatabaseFile The file representing the code database.
     * @param helpArticleManager The HelpArticleManager for managing help articles.
     */
    public AdminDashboard(AdminApp adminApp, File userDatabaseFile, File codeDatabaseFile, HelpArticleManager helpArticleManager) {
        this.adminApp = adminApp;
        this.userDatabaseFile = userDatabaseFile;
        this.codeDatabaseFile = codeDatabaseFile;
        this.helpArticleManager = helpArticleManager; // Initialize HelpArticleManager
    }

    /**
     * The start method initializes the JavaFX UI for the Admin Dashboard.
     * It sets up the layout, buttons, and event handlers for the dashboard.
     *
     * @param primaryStage The primary stage for this application, provided by JavaFX.
     */
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Admin Dashboard"); // Set the title of the window

        // Create a vertical box (VBox) layout with spacing between elements
        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER); // Center align all the elements in the VBox
        layout.setStyle("-fx-background-color: #f4f4f4; -fx-padding: 20;"); // Set background color and padding

        // Create UI components for the dashboard
        Label titleLabel = new Label("Admin Dashboard");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2e9ed6;"); // Style title

        Button manageUsersButton = new Button("Manage Users");
        manageUsersButton.setStyle("-fx-background-color: #2e9ed6; -fx-text-fill: white; -fx-padding: 10 20; -fx-font-size: 14px;"); // Button styling

        Button generateInvitationButton = new Button("Generate Invitation Code");
        generateInvitationButton.setStyle("-fx-background-color: #2e9ed6; -fx-text-fill: white; -fx-padding: 10 20; -fx-font-size: 14px;"); // Button styling

        Button manageArticlesButton = new Button("Manage Help Articles");
        manageArticlesButton.setStyle("-fx-background-color: #2e9ed6; -fx-text-fill: white; -fx-padding: 10 20; -fx-font-size: 14px;"); // Button styling

        // Log Out button
        Button logoutButton = new Button("Log Out");
        logoutButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-padding: 10 20; -fx-font-size: 14px;"); // Style for log out button
        logoutButton.setOnAction(e -> {
            primaryStage.close(); // Close the admin dashboard
        });

        // Set the action to be performed when the "Manage Users" button is clicked
        manageUsersButton.setOnAction(e -> {
            AdminWindow adminWindow = new AdminWindow(adminApp, userDatabaseFile, codeDatabaseFile);
            adminWindow.show();
        });

        // Set the action to be performed when the "Generate Invitation Code" button is clicked
        generateInvitationButton.setOnAction(e -> {
            // Logic to generate an invitation code for a student
            String code = adminApp.generateInvitationCode("Student", codeDatabaseFile); // Generate code for "Student" role
            showAlert("Invitation Code", "Generated code: " + code); // Show alert with the generated code
        });

        // Set action for the manage articles button
        manageArticlesButton.setOnAction(e -> {
            // Create and display the ArticleManagementWindow
            ArticleManagementWindow articleWindow = new ArticleManagementWindow();
            articleWindow.start(new Stage()); // Open a new stage for managing articles
        });

        // Add all UI components to the layout
        layout.getChildren().addAll(titleLabel, manageUsersButton, generateInvitationButton, manageArticlesButton, logoutButton);

        // Create a new scene with the layout and set the stage properties
        Scene scene = new Scene(layout, 600, 600); // Scene with width 400 and height 300
        primaryStage.setScene(scene); // Set the scene for the primary stage
        primaryStage.show(); // Display the stage
    }

    /**
     * Helper method to display an alert dialog with a specified title and message.
     *
     * @param title   The title of the alert dialog.
     * @param message The message to display inside the alert dialog.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION); // Create an information-type alert
        alert.setTitle(title); // Set the title of the alert
        alert.setHeaderText(null); // No header text for the alert
        alert.setContentText(message); // Set the content/message of the alert
        alert.showAndWait(); // Show the alert and wait for the user to close it
    }
}