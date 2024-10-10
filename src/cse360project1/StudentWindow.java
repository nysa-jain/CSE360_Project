package cse360project1;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;

public class StudentWindow extends Application {

    private user userApp;
    private AdminApp adminApp;
    private File userD;

    @Override
    public void start(Stage primaryStage) {
        userApp = new user();
        adminApp = new AdminApp();
        userD = new File("UserDatabase.txt");

        primaryStage.setTitle("Student Registration");

        // UI elements for student registration
        Label welcomeLabel = new Label("Welcome to Student Registration");
        Label userNameLabel = new Label("Username:");
        TextField userNameField = new TextField();
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        Label verificationCodeLabel = new Label("Verification Code:");
        TextField verificationCodeField = new TextField();
        Label statusLabel = new Label();

        Button registerButton = new Button("Register");

        // Register button action
        registerButton.setOnAction(e -> {
            String userName = userNameField.getText();
            String password = passwordField.getText();
            String verificationCode = verificationCodeField.getText();

            // Validate the verification code and register
            if (adminApp.validateVerificationCode(userName, verificationCode, "Student", userD)) {
                if (userApp.addUserAccount(userName, password, userD)) {
                    statusLabel.setText("Account created successfully!");
                    verificationCodeField.setDisable(true); // Disable code input after successful registration
                } else {
                    statusLabel.setText("Account already created.");
                }
            } else {
                statusLabel.setText("Invalid verification code.");
            }
        });

        // Layout setup
        VBox root = new VBox(10, welcomeLabel, userNameLabel, userNameField, passwordLabel, passwordField, verificationCodeLabel, verificationCodeField, registerButton, statusLabel);
        Scene scene = new Scene(root, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
