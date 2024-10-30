package cse360project;

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

	private final AdminApp adminApp; // Instance of AdminApp to handle user management operations
	private final File userD; // File object representing the user database

	public AdminWindow(AdminApp adminDatabase, File userD, File adminD) {
		this.adminApp = adminDatabase;
		this.userD = userD;

		VBox adminLayout = new VBox(15); // Increased spacing

		// Styling buttons
		Button createUserButton = createStyledButton("Create User");
		Button deleteUserButton = createStyledButton("Delete User");
		Button viewUsersButton = createStyledButton("View Users");
		Button changeRoleButton = createStyledButton("Change User Role");

		Label statusLabel = new Label();
		statusLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #d9534f;"); // Style for status messages

		createUserButton.setOnAction(event -> openCreateUserWindow(statusLabel));
		deleteUserButton.setOnAction(event -> openDeleteUserWindow(statusLabel));
		viewUsersButton.setOnAction(event -> viewAllUsers());
		changeRoleButton.setOnAction(e -> openChangeUserRoleWindow(statusLabel));

		adminLayout.getChildren().addAll(createUserButton, deleteUserButton, viewUsersButton, changeRoleButton, statusLabel);
		adminLayout.setAlignment(Pos.CENTER);
		adminLayout.setStyle("-fx-background-color: #f4f4f4; -fx-padding: 20;"); // Background color and padding

		Scene adminScene = new Scene(adminLayout, 600, 600);
		setScene(adminScene);
		setTitle("Admin Window"); // Title for the window
	}

	private Button createStyledButton(String text) {
		Button button = new Button(text);
		button.setStyle("-fx-background-color: #5bc0de; -fx-text-fill: white; -fx-padding: 10 20; -fx-font-size: 14px;"); // Button styling
		return button;
	}

	private void openCreateUserWindow(Label statusLabel) {
		Stage createUserStage = new Stage();
		VBox layout = new VBox(10);
		layout.setStyle("-fx-padding: 20; -fx-background-color: #f4f4f4;"); // Background color and padding

		TextField usernameField = new TextField();
		usernameField.setPromptText("Enter Username");

		PasswordField passwordField = new PasswordField();
		passwordField.setPromptText("Enter Password");

		TextField roleField = new TextField();
		roleField.setPromptText("Enter Role (e.g., Admin, User)");

		Button submitButton = createStyledButton("Create User");
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
		createUserStage.setTitle("Create User");
		createUserStage.show();
	}

	private void openDeleteUserWindow(Label statusLabel) {
		Stage deleteUserStage = new Stage();
		VBox layout = new VBox(10);
		layout.setStyle("-fx-padding: 20; -fx-background-color: #f4f4f4;"); // Background color and padding

		// Create a ComboBox to display usernames
		ComboBox<String> usernameComboBox = new ComboBox<>();
		List<UserInfo> users = adminApp.getAllUsers(userD); // Get all users
		for (UserInfo user : users) {
			usernameComboBox.getItems().add(user.getUsername()); // Add usernames to the ComboBox
		}

		Button deleteButton = createStyledButton("Delete User");
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
		deleteUserStage.setTitle("Delete User");
		deleteUserStage.show();
	}

	private void viewAllUsers() {
		List<UserInfo> users = adminApp.getAllUsers(userD); // Get all users

		// Create a new window to display users
		Stage userWindow = new Stage();
		VBox userLayout = new VBox(10); // Create a vertical box layout
		userLayout.setStyle("-fx-padding: 20; -fx-background-color: #f4f4f4;"); // Background color and padding

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
		Button closeButton = createStyledButton("Close");
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
		layout.setStyle("-fx-padding: 20; -fx-background-color: #f4f4f4;"); // Background color and padding

		// Create a ComboBox to select usernames
		ComboBox<String> usernameComboBox = new ComboBox<>();
		List<UserInfo> users = adminApp.getAllUsers(userD); // Get all users
		for (UserInfo user : users) {
			usernameComboBox.getItems().add(user.getUsername()); // Add usernames to the ComboBox
		}

		// Create a ComboBox to select roles
		ComboBox<String> roleComboBox = new ComboBox<>();
		roleComboBox.getItems().addAll("Admin", "Student", "Instructor"); // Add possible roles

		Button addRoleButton = createStyledButton("Add Role");
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

		Button removeRoleButton = createStyledButton("Remove Role");
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
				addRoleButton, removeRoleButton, statusLabel);
		layout.setAlignment(Pos.CENTER);

		changeRoleStage.setScene(new Scene(layout, 300, 300));
		changeRoleStage.setTitle("Change User Role");
		changeRoleStage.show();
	}
}