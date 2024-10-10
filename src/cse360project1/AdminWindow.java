package cse360project1;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;

public class AdminWindow extends Stage {

    private AdminApp adminApp;
    private File userD;

    public AdminWindow(AdminApp adminDatabase, File userD, File adminD) {
        this.adminApp = adminDatabase;
        this.userD = userD;

        VBox adminLayout = new VBox(10);

        // Buttons for admin actions
        Button createUserButton = new Button("Create User");
        Button deleteUserButton = new Button("Delete User");
        Button viewUsersButton = new Button("View Users");
        Button generateCodeButton = new Button("Generate Verification Code");

        Label statusLabel = new Label();

        createUserButton.setOnAction(event -> openCreateUserWindow());
        deleteUserButton.setOnAction(event -> openDeleteUserWindow());
        viewUsersButton.setOnAction(event -> viewAllUsers());
        generateCodeButton.setOnAction(event -> openGenerateCodeWindow());

        adminLayout.getChildren().addAll(createUserButton, deleteUserButton, viewUsersButton, generateCodeButton, statusLabel);
        adminLayout.setAlignment(Pos.TOP_CENTER);

        Scene adminScene = new Scene(adminLayout, 400, 300);
        setScene(adminScene);
    }

    private void openCreateUserWindow() {
        // Logic to open a window for creating a user (with role and password)
    }

    private void openDeleteUserWindow() {
        // Logic to open a window for deleting a user
    }

    private void viewAllUsers() {
        // Logic to display all users in the user database
    }

    private void openGenerateCodeWindow() {
        // Logic to generate an invitation code for a specific role
    }
}
