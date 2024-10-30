package cse360project;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * The StudentView class represents the interface displayed to students upon successful login.
 */
public class StudentWindow extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Student View");

        // Create a label to welcome the student
        Label welcomeLabel = new Label("Welcome to the Student View!");

        // Layout setup for the Student View
        VBox layout = new VBox(10);
        layout.getChildren().add(welcomeLabel);

        Scene scene = new Scene(layout, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}