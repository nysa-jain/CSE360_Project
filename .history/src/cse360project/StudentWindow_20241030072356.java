package cse360project;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * The StudentWindow class represents the interface displayed to students upon successful login.
 */
public class StudentWindow extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Student View");

        // Create a label to welcome the student
        Label welcomeLabel = new Label("Welcome to the Student View!");
        welcomeLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;"); // Style the welcome label

        // Layout setup for the Student View
        GridPane layout = new GridPane();
        layout.setPadding(new Insets(20)); // Add padding around the layout
        layout.setVgap(10); // Set vertical spacing
        layout.setHgap(10); // Set horizontal spacing
        layout.setAlignment(Pos.CENTER); // Center align the layout

        // Create a button for students to view their articles
        Button viewArticlesButton = new Button("View Articles");
        viewArticlesButton.setStyle("-fx-font-size: 14px; -fx-padding: 10px; -fx-background-color: #4CAF50; -fx-text-fill: white;");

        // Create a button for students to log out
        Button logoutButton = new Button("Log Out");
        logoutButton.setStyle("-fx-font-size: 14px; -fx-padding: 10px; -fx-background-color: #f44336; -fx-text-fill: white;");

        // Add action for the view articles button
        viewArticlesButton.setOnAction(e -> {
            // Logic to view articles goes here (e.g., open a new window)
            System.out.println("View Articles button clicked!");
        });

        // Add action for the logout button
        logoutButton.setOnAction(e -> {
            // Logic to log out goes here (e.g., close the current window)
            System.out.println("Log Out button clicked!");
            primaryStage.close(); // Close the current stage for now
        });

        // Add all components to the layout
        layout.add(welcomeLabel, 0, 0, 2, 1); // Span the welcome label across two columns
        layout.add(viewArticlesButton, 0, 1);
        layout.add(logoutButton, 1, 1);

        // Create a scene with the layout
        Scene scene = new Scene(layout, 600, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}