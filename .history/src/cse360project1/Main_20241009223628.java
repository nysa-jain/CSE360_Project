package cse360project1;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {

    private final File userDatabaseFile = new File("UserDatabase.txt");
    private final File adminDatabaseFile = new File("AdminDatabase.txt");
    private final File codeDatabaseFile = new File("CodeDatabase.txt");
    private final AdminApp adminApp = new AdminApp();

    @Override
    public void start(Stage primaryStage) {
        // Initialize the admin database if not already initialized
        adminApp.initializeAdminDatabase(adminDatabaseFile);

        primaryStage.setTitle("CSE 360 Help System");

        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);

        Label welcomeLabel = new Label("Welcome to the CSE 360 Help System");
        Button adminButton = new Button("Admin");
        Button userButton = new Button("User");

        adminButton.setOnAction(e -> {
            AdminLoginWindow loginWindow = new AdminLoginWindow(adminApp, adminDatabaseFile, userDatabaseFile, codeDatabaseFile);
            loginWindow.start(new Stage());
        });

        userButton.setOnAction(e -> {
            UserLoginWindow userWindow = new UserLoginWindow();
            userWindow.start(new Stage());
        });

        layout.getChildren().addAll(welcomeLabel, adminButton, userButton);
        Scene scene = new Scene(layout, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
