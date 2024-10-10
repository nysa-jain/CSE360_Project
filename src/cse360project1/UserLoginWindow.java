package cse360project1;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;

public class UserLoginWindow extends Application {

    private AdminApp adminApp;
    private File userD;
    private File codeD;

    @Override
    public void start(Stage primaryStage) {
        adminApp = new AdminApp();
        userD = new File("UserDatabase.txt");
        codeD = new File("CodeDatabase.txt");

        primaryStage.setTitle("User Registration");

        VBox layout = new VBox(10);

        TextField fullNameField = new TextField();
        fullNameField.setPromptText("Full Name");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm Password");

        TextField invitationCodeField = new TextField();
        invitationCodeField.setPromptText("Invitation Code");

        Button nextButton = new Button("Next");
        Label statusLabel = new Label();

        nextButton.setOnAction(e -> {
            String fullName = fullNameField.getText();
            String email = emailField.getText();
            String password = passwordField.getText();
            String confirmPassword = confirmPasswordField.getText();
            String code = invitationCodeField.getText();

            if (!password.equals(confirmPassword)) {
                statusLabel.setText("Passwords do not match.");
                return;
            }

            // Validate the invitation code and proceed to setup
            if (adminApp.validateInvitationCode(code, "Student", codeD) || 
                adminApp.validateInvitationCode(code, "Instructor", codeD)) {
                SetupAccountWindow setupWindow = new SetupAccountWindow(
                        adminApp, userD, fullName, email, password, codeD);
                setupWindow.start(new Stage());
                primaryStage.close();
            } else {
                statusLabel.setText("Invalid invitation code.");
            }
        });

        layout.getChildren().addAll(
            fullNameField, emailField, passwordField, confirmPasswordField,
            invitationCodeField, nextButton, statusLabel);

        Scene scene = new Scene(layout, 300, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
