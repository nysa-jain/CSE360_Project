package cse360project;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * The InstructorWindow class represents the interface displayed to instructors upon successful login.
 */
public class InstructorWindow extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Instructor Interface");

        // Create components
        Label titleLabel = new Label("Instructor Interface");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2e9ed6;"); // Updated style

        Button openArticleManagementButton = new Button("Article Management");
        openArticleManagementButton.setStyle("-fx-background-color: #2e9ed6; -fx-text-fill: white; -fx-padding: 10 20; -fx-font-size: 14px;"); // Styled button

        // Log Out button
        Button logoutButton = new Button("Log Out");
        logoutButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-padding: 10 20; -fx-font-size: 14px;"); // Style for log out button
        logoutButton.setOnAction(e -> {
            primaryStage.close(); // Close the instructor window
        });

        // Set actions for buttons
        openArticleManagementButton.setOnAction(e -> openArticleManagement());

        // Layout setup
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20)); // Added padding for layout
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(titleLabel, openArticleManagementButton, logoutButton); // Include the logout button

        Scene scene = new Scene(layout, 600, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void openArticleManagement() {
        // Open the Article Management window
        ArticleManagementWindow articleManagementWindow = new ArticleManagementWindow();
        try {
            articleManagementWindow.start(new Stage()); // Open a new stage for Article Management
        } catch (Exception e) {
            e.printStackTrace(); // Handle exception
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}