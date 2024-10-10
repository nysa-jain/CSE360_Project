package cse360project1;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;

public class AdminLoginWindow extends Application {

    private final AdminApp adminApp;
    private final File adminDatabaseFile;
    private final File userDatabaseFile;
    private final File codeDatabaseFile;

    // Constructor matching the parameters passed in Main.java
    public AdminLoginWindow(AdminApp adminApp, File adminDatabaseFile, File userDatabaseFile, File codeDatabaseFile) {
        this.adminApp = adminApp;
        this.adminDatabaseFile = adminDatabaseFile;
        this.userDatabaseFile = userDatabaseFile;
        this.codeDatabaseFile = codeDatabaseFile;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Admin Login");

        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);

        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        Label statusLabel = new Label();

        Button loginButton = new Button("Login");

        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (adminApp.checkAdminAuthentication(username, password, adminDatabaseFile)) {
                AdminDashboard adminDashboard = new AdminDashboard(adminApp, userDatabaseFile, codeDatabaseFile);
                adminDashboard.start(new Stage());
                primaryStage.close();
            } else {
                statusLabel.setText("Invalid username or password.");
            }
        });

        layout.getChildren().addAll(usernameLabel, usernameField, passwordLabel, passwordField, loginButton, statusLabel);

        Scene scene = new Scene(layout, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
