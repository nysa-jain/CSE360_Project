package cse360project1;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;

public class AdminDashboard extends Application {

    private final AdminApp adminApp;
    private final File userDatabaseFile;
    private final File codeDatabaseFile;

    // Constructor matching the parameters from AdminLoginWindow
    public AdminDashboard(AdminApp adminApp, File userDatabaseFile, File codeDatabaseFile) {
        this.adminApp = adminApp;
        this.userDatabaseFile = userDatabaseFile;
        this.codeDatabaseFile = codeDatabaseFile;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Admin Dashboard");

        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("Admin Dashboard");
        Button manageUsersButton = new Button("Manage Users");
        Button generateInvitationButton = new Button("Generate Invitation Code");

        manageUsersButton.setOnAction(e -> {
            // Logic to manage users, such as viewing, adding, or deleting users.
            // Not implemented here.
            showAlert("Manage Users", "This functionality is not yet implemented.");
        });

        generateInvitationButton.setOnAction(e -> {
            // Logic to generate invitation codes
            String code = adminApp.generateInvitationCode("Student", codeDatabaseFile);
            showAlert("Invitation Code", "Generated code for Student: " + code);
        });

        layout.getChildren().addAll(titleLabel, manageUsersButton, generateInvitationButton);
        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
