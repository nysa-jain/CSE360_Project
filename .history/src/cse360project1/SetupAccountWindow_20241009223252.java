package cse360project1;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;

public class SetupAccountWindow extends Application {

    private AdminApp adminApp;
    private File userD;
    private String fullName;
    private String email;
    private String password;
    private File codeD;

    public SetupAccountWindow(AdminApp adminApp, File userD, String fullName, String email, String password, File codeD) {
        this.adminApp = adminApp;
        this.userD = userD;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.codeD = codeD;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Finish Setting Up Account");

        VBox layout = new VBox(10);

        TextField fullNameField = new TextField(fullName);
        fullNameField.setDisable(true);

        TextField emailField = new TextField(email);
        emailField.setDisable(true);

        PasswordField passwordField = new PasswordField();
        passwordField.setText(password);
        passwordField.setDisable(true);

        ComboBox<String> roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll("Student", "Instructor");
        roleComboBox.setValue("Student");

        Button finishButton = new Button("Finish Setup");
        Label statusLabel = new Label();

        finishButton.setOnAction(e -> {
            String role = roleComboBox.getValue();
            if (adminApp.addUserAccount(email, password, role, userD)) {
                statusLabel.setText("Account created successfully!");
            } else {
                statusLabel.setText("Account creation failed. User may already exist.");
            }
        });

        layout.getChildren().addAll(
            new Label("Full Name:"), fullNameField,
            new Label("Email:"), emailField,
            new Label("Role:"), roleComboBox,
            finishButton, statusLabel);

        Scene scene = new Scene(layout, 300, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
