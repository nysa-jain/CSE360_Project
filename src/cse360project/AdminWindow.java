package cse360project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.File;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;

public class AdminWindow extends Stage {

    private AdminApp adminApp; // Instance of AdminApp to handle user management operations
    private File userD; // File object representing the user database

    public AdminWindow(AdminApp adminDatabase, File userD, File adminD) {
        this.adminApp = adminDatabase;
        this.userD = userD;

        VBox adminLayout = new VBox(10);

        Button createUserButton = new Button("Create User");
        Button deleteUserButton = new Button("Delete User");
        Button viewUsersButton = new Button("View Users");
        Button changeRoleButton = new Button("Change User Role");

        Label statusLabel = new Label();

        createUserButton.setOnAction(event -> openCreateUserWindow(statusLabel));
        deleteUserButton.setOnAction(event -> openDeleteUserWindow(statusLabel));
        viewUsersButton.setOnAction(event -> viewAllUsers());
        changeRoleButton.setOnAction(e -> openChangeUserRoleWindow(statusLabel));

        adminLayout.getChildren().addAll(createUserButton, deleteUserButton, viewUsersButton, changeRoleButton, statusLabel);
        adminLayout.setAlignment(Pos.CENTER);

        Scene adminScene = new Scene(adminLayout, 400, 300);
        setScene(adminScene);
    }

    private void openCreateUserWindow(Label statusLabel) {
        Stage createUserStage = new Stage();
        VBox layout = new VBox(10);

        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter Username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter Password");

        TextField roleField = new TextField();
        roleField.setPromptText("Enter Role (e.g., Admin, User)");

        Button submitButton = new Button("Create User");
        submitButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            String role = roleField.getText();

            if (adminApp.addUserAccount(username, password, role, userD)) {
                statusLabel.setText("User created successfully.");
            } else {
                statusLabel.setText("User creation failed. Username might already exist.");
            }
            createUserStage.close();
        });

        layout.getChildren().addAll(new Label("Create User"), usernameField, passwordField, roleField, submitButton);
        layout.setAlignment(Pos.CENTER);

        createUserStage.setScene(new Scene(layout, 300, 200));
        createUserStage.show();
    }

    private void openDeleteUserWindow(Label statusLabel) {
        Stage deleteUserStage = new Stage();
        VBox layout = new VBox(10);

        // Create a ComboBox to display usernames
        ComboBox<String> usernameComboBox = new ComboBox<>();
        List<UserInfo> users = adminApp.getAllUsers(userD); // Get all users
        for (UserInfo user : users) {
            usernameComboBox.getItems().add(user.getUsername()); // Add usernames to the ComboBox
        }

        Button deleteButton = new Button("Delete User");
        deleteButton.setOnAction(e -> {
            String selectedUsername = usernameComboBox.getValue(); // Get the selected username

            if (selectedUsername != null && adminApp.removeAccount(selectedUsername, userD)) {
                statusLabel.setText("User deleted successfully.");
            } else {
                statusLabel.setText("User deletion failed. Username not found.");
            }
            deleteUserStage.close();
        });

        layout.getChildren().addAll(new Label("Select Username to Delete"), usernameComboBox, deleteButton);
        layout.setAlignment(Pos.CENTER);

        deleteUserStage.setScene(new Scene(layout, 300, 150));
        deleteUserStage.show();
    }

    private void viewAllUsers() {
        List<UserInfo> users = adminApp.getAllUsers(userD); // Get all users

        // Create a new window to display users
        Stage userWindow = new Stage();
        VBox userLayout = new VBox(10); // Create a vertical box layout

        // Create a TableView for user information
        TableView<UserInfo> table = new TableView<>();

        // Define columns for the table
        TableColumn<UserInfo, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<UserInfo, String> passwordCol = new TableColumn<>("Password");
        passwordCol.setCellValueFactory(new PropertyValueFactory<>("password"));

        TableColumn<UserInfo, String> roleCol = new TableColumn<>("Roles");
        roleCol.setCellValueFactory(cellData -> {
            List<String> roles = cellData.getValue().getRoles(); // Assuming getRoles() returns List<String>
            String rolesString = String.join(", ", roles); // Join roles into a comma-separated string
            return new SimpleStringProperty(rolesString); // Wrap as a StringProperty for display
        });

        // Add columns to the table
        table.getColumns().addAll(usernameCol, passwordCol, roleCol);

        // Populate the table with user data
        table.getItems().addAll(users); // Add user info to the table

        // Create a button to close the window
        Button closeButton = new Button("Close");
        closeButton.setOnAction(event -> userWindow.close()); // Close window action

        // Add the table and button to the layout
        userLayout.getChildren().addAll(table, closeButton);

        // Set the scene for the new stage and show it
        Scene userScene = new Scene(userLayout, 400, 300); // Scene dimensions
        userWindow.setScene(userScene); // Set the scene for the user window
        userWindow.setTitle("User List"); // Set the window title
        userWindow.show(); // Display the window
    }

    private void openChangeUserRoleWindow(Label statusLabel) {
        Stage changeRoleStage = new Stage();
        VBox layout = new VBox(10);

        // Create a ComboBox to select usernames
        ComboBox<String> usernameComboBox = new ComboBox<>();
        List<UserInfo> users = adminApp.getAllUsers(userD); // Get all users
        for (UserInfo user : users) {
            usernameComboBox.getItems().add(user.getUsername()); // Add usernames to the ComboBox
        }

        // Create a ComboBox to select roles
        ComboBox<String> roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll("Admin", "Student", "Instructor"); // Add possible roles

        Button addRoleButton = new Button("Add Role");
        addRoleButton.setOnAction(e -> {
            String selectedUsername = usernameComboBox.getValue();
            String selectedRole = roleComboBox.getValue();

            if (selectedUsername != null && selectedRole != null) {
                if (adminApp.addRole(selectedUsername, selectedRole, userD)) {
                    statusLabel.setText("Role added successfully.");
                } else {
                    statusLabel.setText("Failed to add role.");
                }
            } else {
                statusLabel.setText("Please select a username and a role.");
            }
        });

        Button removeRoleButton = new Button("Remove Role");
        removeRoleButton.setOnAction(e -> {
            String selectedUsername = usernameComboBox.getValue();
            String selectedRole = roleComboBox.getValue();

            if (selectedUsername != null && selectedRole != null) {
                if (adminApp.removeRole(selectedUsername, selectedRole, userD)) {
                    statusLabel.setText("Role removed successfully.");
                } else {
                    statusLabel.setText("Failed to remove role.");
                }
            } else {
                statusLabel.setText("Please select a username and a role.");
            }
        });

        layout.getChildren().addAll(new Label("Select Username"), usernameComboBox, 
                                     new Label("Select Role"), roleComboBox,
                                     addRoleButton, removeRoleButton);
        layout.setAlignment(Pos.CENTER);

        changeRoleStage.setScene(new Scene(layout, 300, 250));
        changeRoleStage.setTitle("Change User Role");
        changeRoleStage.show();
    }
}