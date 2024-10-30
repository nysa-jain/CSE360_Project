package cse360project;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * The InstructorWindow class represents the interface displayed to instructors upon successful login.
 */
public class InstructorWindow extends Application {

    public InstructorWindow() {
		// TODO Auto-generated constructor stub
	}

	@Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Instructor Window");

        // Create a label to welcome the instructor
        Label welcomeLabel = new Label("Welcome to the Instructor Interface!");

        // Layout setup for the Instructor Window
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