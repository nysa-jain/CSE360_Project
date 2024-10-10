package cse360project1;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;

public class InstructorWindow extends Application {

    private user userApp;
    private AdminApp adminApp;
    private File userD;
    private File codeD;

    public InstructorWindow(AdminApp adminApp, File userD, File codeD) {
        this.adminApp = adminApp;
        this.userD = userD;
        this.codeD = codeD;
    }

    @Override
    public void start(Stage primaryStage) {
        userApp = new user();
        
        primaryStage.setTitle("Instructor Registration");

        Label welcomeLabel = new Label("Welcome to Instructor Registration");
        Label userNameLabel = new Label("Username:");
        TextField userNameField = new TextField();
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        Label verificationCodeLabel = new Label("Invitation Code:");
        TextField verificationCodeField = new TextField();
        Label statusLabel = new Label();

        Button registerButton = new Button("Register");

        registerButton.setOnAction(e -> {
            String userName = userNameField.getText();
            String password = passwordField.getText();
            String invitationCode = verificationCodeField.getText();

            // Validate the invitation code and register
            if (adminApp.validateInvitationCode(invitationCode, "Instructor", codeD)) {
                if (userApp.addUserAccount(userName, password, userD)) {
                    statusLabel.setText("Account created successfully!");
                    verificationCodeField.setDisable(true); // Disable code input after successful registration
                } else {
                    statusLabel.setText("Account already created.");
                }
            } else {
                statusLabel.setText("Invalid invitation code for the role.");
            }
        });

        VBox root = new VBox(10, welcomeLabel, userNameLabel, userNameField, passwordLabel, passwordField, verificationCodeLabel, verificationCodeField, registerButton, statusLabel);
        Scene scene = new Scene(root, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
