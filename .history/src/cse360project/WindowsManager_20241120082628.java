package cse360project;

import javafx.application.Application;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class WindowsManager extends Stage {
	// File objects representing the databases for users, admins, and invitation
	// codes
	private final File userDatabaseFile = new File("UserDatabase.txt");
	private final File adminDatabaseFile = new File("AdminDatabase.txt");
	private final File codeDatabaseFile = new File("CodeDatabase.txt");
	private final AdminApp adminApp = new AdminApp();

	public void start(Stage primaryStage) {
		// Create a GridPane layout for better organization
		GridPane layout = new GridPane();
		layout.setPadding(new Insets(20)); // Set padding around the layout
		layout.setVgap(15); // Vertical spacing
		layout.setHgap(10); // Horizontal spacing
		layout.setAlignment(Pos.CENTER); // Center align the layout

		// Create welcome label and buttons for admin and user login
		Label welcomeLabel = new Label("Welcome to the CSE 360 Help System");
		welcomeLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;"); // Style for the welcome label

		Button adminButton = new Button("Admin"); // Button to open the admin login window
		Button userButton = new Button("User"); // Button to open the user login window

		// Style the buttons
		adminButton.setStyle(
				"-fx-font-size: 14px; -fx-padding: 10px; -fx-background-color: #4CAF50; -fx-text-fill: white;");
		userButton.setStyle(
				"-fx-font-size: 14px; -fx-padding: 10px; -fx-background-color: #2196F3; -fx-text-fill: white;");

		// Set action for the admin button
		adminButton.setOnAction(e -> {
			showAdminLoginWindow(primaryStage);
		});

		// Set action for the user button
		userButton.setOnAction(e -> {
			showUserLoginWindow(primaryStage);// Open a new stage for user login
		});

		// Add the welcome label and buttons to the layout
		layout.add(welcomeLabel, 0, 0, 2, 1); // Span the label across two columns
		layout.add(adminButton, 0, 1); // Admin button in the first column
		layout.add(userButton, 1, 1); // User button in the second column

		// Create a scene with the specified layout and dimensions
		Scene scene = new Scene(layout, 600, 600); // Increased width for better spacing
		primaryStage.setScene(scene); // Set the scene for the primary stage
		primaryStage.show(); // Display the primary stage
	}
	
	private Button createBackButton(Stage stage, Runnable backAction) {
	    Button backButton = new Button("Back");
	    backButton.setOnAction(e -> backAction.run());
	    backButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-padding: 10 20; -fx-font-size: 14px;");
	    return backButton;
	}
	
	public void showAdminLoginWindow(Stage stage) {
		stage.setTitle("Admin Login");

		VBox layout = new VBox(15);
		layout.setAlignment(Pos.CENTER);
		layout.setStyle("-fx-background-color: #f4f4f4; -fx-padding: 20;");

		Label usernameLabel = new Label("Username:");
		usernameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
		TextField usernameField = new TextField();
		usernameField.setPromptText("Enter your username");
		usernameField.setStyle("-fx-padding: 10;");

		Label passwordLabel = new Label("Password:");
		passwordLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
		PasswordField passwordField = new PasswordField();
		passwordField.setPromptText("Enter your password");
		passwordField.setStyle("-fx-padding: 10;");

		Label statusLabel = new Label();
		statusLabel.setStyle("-fx-text-fill: red;");

		Button loginButton = new Button("Login");
		loginButton.setStyle(
				"-fx-background-color: #2e9ed6; -fx-text-fill: white; -fx-padding: 10 20; -fx-font-size: 14px;");
		loginButton.setOnAction(e -> {
			String username = usernameField.getText();
			String password = passwordField.getText();
			if (adminApp.checkAdminAuthentication(username, password, adminDatabaseFile)) {
				showAdminDashboard(stage);
			} else {
				statusLabel.setText("Invalid username or password.");
				usernameField.clear();
				passwordField.clear();
			}
		});
		
		Button backButton = createBackButton(stage, () -> start(stage)); // Navigate back to main menu

		layout.getChildren().addAll(usernameLabel, usernameField, passwordLabel, passwordField, loginButton,
				statusLabel,backButton);
		Scene scene = new Scene(layout, 600, 600);
		stage.setScene(scene);
		stage.show();
	}

	public void showUserLoginWindow(Stage primaryStage) {
		// Set the title of the primary stage
		primaryStage.setTitle("User Login");

		// Create a GridPane layout for the UI components
		GridPane layout = new GridPane();
		layout.setPadding(new Insets(20)); // Add padding around the layout
		layout.setVgap(10); // Set vertical spacing
		layout.setHgap(10); // Set horizontal spacing
		layout.setAlignment(Pos.CENTER); // Center align the layout

		// Create a label for the user login options
		Label welcomeLabel = new Label("Welcome! Please log in or sign up.");
		welcomeLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;"); // Style the welcome label

		// Create buttons for login and signup
		Button loginButton = new Button("Login");
		Button signUpButton = new Button("New User? Sign Up");
		Button backButton = createBackButton(primaryStage, () -> start(primaryStage));

		// Create text fields for user input
		TextField UserNameField = new TextField();
		UserNameField.setPromptText("User Name"); // Placeholder text
		PasswordField passwordField = new PasswordField();
		passwordField.setPromptText("Password"); // Placeholder text

		// Status label for messages
		Label statusLabel = new Label();

		// ComboBox for role selection
		ComboBox<String> roleComboBox = new ComboBox<>();
		roleComboBox.setPromptText("Select Role");

		// In UserLoginWindow.java
		loginButton.setOnAction(e -> {
			String username = UserNameField.getText();
			String password = passwordField.getText();

			// Check user authentication and retrieve roles
			List<String> roles = UserInfo.getRoles(username, password, userDatabaseFile);
			if (roles != null && !roles.isEmpty()) {
				// Retrieve groups and articles the user has access to
				List<String> accessibleGroups = UserInfo.getAccessibleGroups(username);
				List<String> accessibleArticles = UserInfo.getAccessibleArticles(username);

				UserInfo userInfo = new UserInfo(username, password, roles, accessibleGroups, accessibleArticles);

				// Check if the user is the first instructor to a group and grant admin rights
				for (String group : accessibleGroups) {
					if (userInfo.getRoles().contains("Instructor") && !userInfo.isAdminForGroup(group)) {
						// Logic to check if the instructor is the first in the group
						// For simplicity, grant admin rights if no other admin exists
						boolean isFirstInstructor = true; // Replace with actual check
						if (isFirstInstructor) {
							userInfo.grantAdminRights(group);
						}
					}
				}

				statusLabel.setText("Login successful!");

				// Populate the role selection ComboBox and proceed with the UI logic
				roleComboBox.getItems().clear();
				for (String role : roles) {
					roleComboBox.getItems().add(role);
				}
				roleComboBox.setVisible(true);

				// Handle role-based access
				Button proceedButton = new Button("Proceed");
				proceedButton.setOnAction(event -> {
					String selectedRole = roleComboBox.getValue();
					if (selectedRole != null) {
						statusLabel.setText("Opening view for " + selectedRole);

						// Redirect to the corresponding view based on the user role
						switch (selectedRole) {
						case "Student":
							if (userInfo.canViewArticles("Student")) {
								showStudentWindow(primaryStage);
							} else {
								statusLabel.setText("Access denied to student dashboard.");
							}
							break;
						case "Instructor":
							if (userInfo.canEditArticles("Instructor")) {
								showInstructorWindow(primaryStage);
							} else {
								statusLabel.setText("Access denied to instructor dashboard.");
							}
							break;
						case "Admin":
							if (userInfo.isAdminForGroup("Admin")) {
								showAdminDashboard(primaryStage);
							} else {
								statusLabel.setText("Access denied to admin dashboard.");
							}
							break;
						default:
							statusLabel.setText("Unknown role selected.");
							break;
						}
					} else {
						statusLabel.setText("Please select a role.");
					}
				});

				layout.add(roleComboBox, 0, 5, 2, 1);
				layout.add(proceedButton, 0, 6, 2, 1);
				proceedButton.setVisible(true);
			} else {
				statusLabel.setText("Invalid username or password.");
			}
		});

		// Set action for the sign-up button
		signUpButton.setOnAction(e -> {
			showSetupAccountWindow(primaryStage); // Start the new window
		});

		// Add all components to the layout
		layout.add(welcomeLabel, 0, 0, 2, 1); // Span the welcome label across two columns
		layout.add(UserNameField, 0, 1);
		layout.add(passwordField, 0, 2);
		layout.add(loginButton, 0, 3);
		layout.add(signUpButton, 1, 3);
		layout.add(statusLabel, 0, 4, 2, 1); // Status label spanning two columns
		layout.add(backButton, 0, 5, 2, 1);

		// Create a scene with the layout and set it to the primary stage
		Scene scene = new Scene(layout, 600, 600);
		primaryStage.setScene(scene);
		primaryStage.show(); // Show the primary stage
	}

	public void showAdminDashboard(Stage primaryStage) {
		primaryStage.setTitle("Admin Dashboard"); // Set the title of the window

		// Create a vertical box (VBox) layout with spacing between elements
		VBox layout = new VBox(15);
		layout.setAlignment(Pos.CENTER); // Center align all the elements in the VBox
		layout.setStyle("-fx-background-color: #f4f4f4; -fx-padding: 20;"); // Set background color and padding

		// Create UI components for the dashboard
		Label titleLabel = new Label("Admin Dashboard");
		titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2e9ed6;"); // Style title

		Button manageUsersButton = new Button("Manage Users");
		manageUsersButton.setStyle(
				"-fx-background-color: #2e9ed6; -fx-text-fill: white; -fx-padding: 10 20; -fx-font-size: 14px;"); // Button
																													// styling

		Button generateInvitationButton = new Button("Generate Invitation Code");
		generateInvitationButton.setStyle(
				"-fx-background-color: #2e9ed6; -fx-text-fill: white; -fx-padding: 10 20; -fx-font-size: 14px;"); // Button
																													// styling

		Button manageArticlesButton = new Button("Manage Help Articles");
		manageArticlesButton.setStyle(
				"-fx-background-color: #2e9ed6; -fx-text-fill: white; -fx-padding: 10 20; -fx-font-size: 14px;"); // Button
																													// styling

		// Log Out button
		Button logoutButton = new Button("Log Out");
		logoutButton.setStyle(
				"-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-padding: 10 20; -fx-font-size: 14px;"); // Style
																													// for
																													// log
																													// out
																													// button
		Button manageGroupsButton = new Button("Manage Groups"); // New button for group management
	    manageGroupsButton.setStyle("-fx-background-color: #2e9ed6; -fx-text-fill: white; -fx-padding: 10 20; -fx-font-size: 14px;");

		// Set the action to be performed when the "Manage Users" button is clicked
		manageUsersButton.setOnAction(e -> {
			showAdminWindow(primaryStage);
		});

		// Set the action to be performed when the "Generate Invitation Code" button is
		// clicked
		generateInvitationButton.setOnAction(e -> {
			// Logic to generate an invitation code for a student
			String code = adminApp.generateInvitationCode("Student", codeDatabaseFile); // Generate code for "Student"
																						// role
			showAlert("Invitation Code", "Generated code: " + code); // Show alert with the generated code
		});

		// Set action for the manage articles button
		manageArticlesButton.setOnAction(e -> {
			// Create and display the ArticleManagementWindow
			showArticleManagementWindow(primaryStage); // Open a new stage for managing articles
		});
		
	    manageGroupsButton.setOnAction(e -> showGroupManagementWindow(new Stage())); // Action to open Group Management window


	    logoutButton = createBackButton(primaryStage, () -> showAdminLoginWindow(primaryStage)); // Back to admin login

	    layout.getChildren().addAll(titleLabel, manageUsersButton, generateInvitationButton, manageArticlesButton, manageGroupsButton, logoutButton);

	    

		// Create a new scene with the layout and set the stage properties
		Scene scene = new Scene(layout, 600, 600); // Scene with width 400 and height 300
		primaryStage.setScene(scene); // Set the scene for the primary stage
		primaryStage.show(); // Display the stage
	}

	private void showAdminWindow(Stage primaryStage) {
	    VBox adminLayout = new VBox(15);

	    Button createUserButton = createStyledButton("Create User");
	    Button deleteUserButton = createStyledButton("Delete User");
	    Button viewUsersButton = createStyledButton("View Users");
	    Button changeRoleButton = createStyledButton("Change User Role");

	    Label statusLabel = new Label();
	    statusLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #d9534f;");

	    createUserButton.setOnAction(event -> openCreateUserWindow(statusLabel));
	    deleteUserButton.setOnAction(event -> openDeleteUserWindow(statusLabel));
	    viewUsersButton.setOnAction(event -> viewAllUsers(primaryStage));
	    changeRoleButton.setOnAction(e -> openChangeUserRoleWindow(primaryStage, statusLabel));

	    Button backButton = createBackButton(primaryStage, () -> showAdminDashboard(primaryStage)); // Back button to Admin Dashboard

	    adminLayout.getChildren().addAll(createUserButton, deleteUserButton, viewUsersButton, changeRoleButton, backButton, statusLabel);
	    adminLayout.setAlignment(Pos.CENTER);
	    adminLayout.setStyle("-fx-background-color: #f4f4f4; -fx-padding: 20;");

	    Scene adminScene = new Scene(adminLayout, 600, 600);

	    Stage adminStage = new Stage();
	    adminStage.setScene(adminScene);
	    adminStage.setTitle("Admin Window");
	    adminStage.show();
	}

	private Button createStyledButton(String text) {
		Button button = new Button(text);
		button.setStyle(
				"-fx-background-color: #5bc0de; -fx-text-fill: white; -fx-padding: 10 20; -fx-font-size: 14px;"); // Button
																													// styling
		return button;
	}

	private void openCreateUserWindow(Label statusLabel) {
	    Stage createUserStage = new Stage();
	    VBox layout = new VBox(10);
	    layout.setStyle("-fx-padding: 20; -fx-background-color: #f4f4f4;");

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

	        if (adminApp.addUserAccount(username, password, role, userDatabaseFile)) {
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
	    layout.setStyle("-fx-padding: 20; -fx-background-color: #f4f4f4;");

	    ComboBox<String> usernameComboBox = new ComboBox<>();
	    List<UserInfo> users = adminApp.getAllUsers(userDatabaseFile);
	    for (UserInfo user : users) {
	        usernameComboBox.getItems().add(user.getUsername());
	    }

	    Button deleteButton = createStyledButton("Delete User");
	    deleteButton.setOnAction(e -> {
	        String selectedUsername = usernameComboBox.getValue();

	        if (selectedUsername != null && adminApp.removeAccount(selectedUsername, userDatabaseFile)) {
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

	private void viewAllUsers(Stage primaryStage) {
	    List<UserInfo> users = adminApp.getAllUsers(userDatabaseFile);

	    Stage userWindow = new Stage();
	    VBox userLayout = new VBox(10);
	    userLayout.setStyle("-fx-padding: 20; -fx-background-color: #f4f4f4;");

	    TableView<UserInfo> table = new TableView<>();

	    TableColumn<UserInfo, String> usernameCol = new TableColumn<>("Username");
	    usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));

	    TableColumn<UserInfo, String> passwordCol = new TableColumn<>("Password");
	    passwordCol.setCellValueFactory(new PropertyValueFactory<>("password"));

	    TableColumn<UserInfo, String> roleCol = new TableColumn<>("Roles");
	    roleCol.setCellValueFactory(cellData -> {
	        List<String> roles = cellData.getValue().getRoles();
	        String rolesString = String.join(", ", roles);
	        return new SimpleStringProperty(rolesString);
	    });

	    table.getColumns().addAll(usernameCol, passwordCol, roleCol);
	    table.getItems().addAll(users);

	    userLayout.getChildren().addAll(table);

	    Scene userScene = new Scene(userLayout, 400, 300);
	    userWindow.setScene(userScene);
	    userWindow.setTitle("User List");
	    userWindow.show();
	}


	private void openChangeUserRoleWindow(Stage primaryStage, Label statusLabel) {
	    Stage changeRoleStage = new Stage();
	    VBox layout = new VBox(10);
	    layout.setStyle("-fx-padding: 20; -fx-background-color: #f4f4f4;");

	    ComboBox<String> usernameComboBox = new ComboBox<>();
	    List<UserInfo> users = adminApp.getAllUsers(userDatabaseFile);
	    for (UserInfo user : users) {
	        usernameComboBox.getItems().add(user.getUsername());
	    }

	    ComboBox<String> roleComboBox = new ComboBox<>();
	    roleComboBox.getItems().addAll("Admin", "Student", "Instructor");

	    Button addRoleButton = createStyledButton("Add Role");
	    addRoleButton.setOnAction(e -> {
	        String selectedUsername = usernameComboBox.getValue();
	        String selectedRole = roleComboBox.getValue();

	        if (selectedUsername != null && selectedRole != null) {
	            if (adminApp.addRole(selectedUsername, selectedRole, userDatabaseFile)) {
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
	            if (adminApp.removeRole(selectedUsername, selectedRole, userDatabaseFile)) {
	                statusLabel.setText("Role removed successfully.");
	            } else {
	                statusLabel.setText("Failed to remove role.");
	            }
	        } else {
	            statusLabel.setText("Please select a username and a role.");
	        }
	    });


	    layout.getChildren().addAll(new Label("Select Username"), usernameComboBox, new Label("Select Role"),
	            roleComboBox, addRoleButton, removeRoleButton, statusLabel);
	    layout.setAlignment(Pos.CENTER);

	    changeRoleStage.setScene(new Scene(layout, 300, 300));
	    changeRoleStage.setTitle("Change User Role");
	    changeRoleStage.show();
	}

	private void showAlert(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION); // Create an information-type alert
		alert.setTitle(title); // Set the title of the alert
		alert.setHeaderText(null); // No header text for the alert
		alert.setContentText(message); // Set the content/message of the alert
		alert.showAndWait(); // Show the alert and wait for the user to close it
	}

	public void showSetupAccountWindow(Stage primaryStage) {
	    primaryStage.setTitle("Set Up Your Account");

	    GridPane layout = new GridPane();
	    layout.setPadding(new Insets(20));
	    layout.setVgap(10);
	    layout.setHgap(10);
	    layout.setAlignment(Pos.CENTER);

	    TextField userNameField = new TextField();
	    userNameField.setPromptText("Enter Username");

	    TextField emailField = new TextField();
	    emailField.setPromptText("Enter Email");

	    PasswordField passwordField = new PasswordField();
	    passwordField.setPromptText("Enter Password");

	    ComboBox<String> roleComboBox = new ComboBox<>();
	    roleComboBox.getItems().addAll("Student", "Instructor");
	    roleComboBox.setValue("Student");

	    TextField invitationCodeField = new TextField();
	    invitationCodeField.setPromptText("Invitation Code");

	    Button finishButton = new Button("Finish Setup");
	    finishButton.setStyle("-fx-font-size: 14px; -fx-padding: 10px; -fx-background-color: #4CAF50; -fx-text-fill: white;");
	    Label statusLabel = new Label();

	    finishButton.setOnAction(e -> {
	        String role = roleComboBox.getValue();
	        String invitationCode = invitationCodeField.getText();
	        String userName = userNameField.getText();
	        String pass = passwordField.getText();

	        if (adminApp.validateInvitationCode(invitationCode, role, codeDatabaseFile)) {
	            if (adminApp.addUserAccount(userName, pass, role, userDatabaseFile)) {
	                statusLabel.setText("Account created successfully!");
	            } else {
	                statusLabel.setText("Account creation failed. User may already exist.");
	            }
	        } else {
	            statusLabel.setText("Invalid invitation code.");
	        }
	    });

	    Button backButton = createBackButton(primaryStage, () -> showUserLoginWindow(primaryStage)); // Back to User Login

	    layout.add(new Label("User Name:"), 0, 0);
	    layout.add(userNameField, 1, 0);
	    layout.add(new Label("Email:"), 0, 1);
	    layout.add(emailField, 1, 1);
	    layout.add(new Label("Password:"), 0, 2);
	    layout.add(passwordField, 1, 2);
	    layout.add(new Label("Invitation Code:"), 0, 3);
	    layout.add(invitationCodeField, 1, 3);
	    layout.add(new Label("Role:"), 0, 4);
	    layout.add(roleComboBox, 1, 4);
	    layout.add(finishButton, 0, 5, 2, 1);
	    layout.add(statusLabel, 0, 6, 2, 1);
	    layout.add(backButton, 0, 7, 2, 1); // Add Back button to layout

	    Scene scene = new Scene(layout, 600, 600);
	    primaryStage.setScene(scene);
	    primaryStage.show();
	}


	public void showArticleManagementWindow(Stage primaryStage) {
	    final HelpArticleManager articleManager = new HelpArticleManager();
	    articleManager.loadArticles();

	    TextArea articleContent = new TextArea();
	    articleContent.setPromptText("Enter article content here...");

	    ListView<HelpArticle> articleListView = new ListView<>();
	    TextField titleField = new TextField();
	    TextField descriptionField = new TextField();
	    TextField keywordsField = new TextField();
	    TextField groupsField = new TextField();
	    ComboBox<String> difficultyLevelComboBox = new ComboBox<>();
	    Label statusLabel = new Label();

	    difficultyLevelComboBox.getItems().addAll("Beginner", "Intermediate", "Advanced", "Expert");
	    updateArticleListView(articleListView, articleManager);

	    primaryStage.setTitle("Article Management");

	    Button addButton = new Button("Add Article");
	    Button editButton = new Button("Edit Article");
	    Button deleteButton = new Button("Delete Article");
	    Button saveEditsButton = new Button("Save Edits");
	    Button backupButton = new Button("Backup Articles");
	    Button restoreButton = new Button("Restore Articles");

	    addButton.setOnAction(e -> {
	        try {
	            addArticle(titleField, descriptionField, articleContent, keywordsField, groupsField,
	                    difficultyLevelComboBox, articleManager, articleListView, statusLabel);
	        } catch (Exception e1) {
	            e1.printStackTrace();
	        }
	    });
	    editButton.setOnAction(e -> editArticle(titleField, descriptionField, articleContent, keywordsField, groupsField,
	            difficultyLevelComboBox, articleListView, statusLabel));
	    deleteButton.setOnAction(e -> deleteArticle(articleListView, articleManager, statusLabel));
	    saveEditsButton.setOnAction(e -> saveEdits(titleField, descriptionField, articleContent, keywordsField, groupsField,
	            difficultyLevelComboBox, articleListView, articleManager, statusLabel));
	    backupButton.setOnAction(e -> backupArticles(articleManager, statusLabel));
	    restoreButton.setOnAction(e -> restoreArticles(articleListView, articleManager, statusLabel));

	    articleListView.setOnMouseClicked(event -> {
	        if (event.getClickCount() == 2) {
	            HelpArticle selectedArticle = articleListView.getSelectionModel().getSelectedItem();
	            if (selectedArticle != null) {
	                viewArticleDetails(selectedArticle);
	            }
	        }
	    });

	    Button backButton = createBackButton(primaryStage, () -> showAdminDashboard(primaryStage)); // Back to Admin Dashboard

	    VBox layout = new VBox(10);
	    layout.setPadding(new Insets(15));
	    layout.setAlignment(Pos.CENTER);

	    Label titleLabel = new Label("Article Management");
	    titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

	    VBox formLayout = new VBox(10, new Label("Title:"), titleField, new Label("Description:"), descriptionField,
	            new Label("Keywords:"), keywordsField, new Label("Groups:"), groupsField,
	            new Label("Difficulty Level:"), difficultyLevelComboBox, new Label("Content:"), articleContent,
	            statusLabel);

	    HBox buttonLayout = new HBox(10, addButton, editButton, deleteButton, saveEditsButton, backupButton,
	            restoreButton, backButton); // Add Back button here
	    buttonLayout.setAlignment(Pos.CENTER);

	    layout.getChildren().addAll(titleLabel, formLayout, articleListView, buttonLayout);

	    Scene scene = new Scene(layout, 600, 600);
	    primaryStage.setScene(scene);
	    primaryStage.show();
	}

	private void addArticle(TextField titleField, TextField descriptionField, TextArea articleContent,
		TextField keywordsField, TextField groupsField, ComboBox<String> difficultyLevelComboBox,
		HelpArticleManager articleManager, ListView<HelpArticle> articleListView, Label statusLabel) throws Exception {
		String title = titleField.getText();
		String description = descriptionField.getText();
		String content = articleContent.getText();
		String keywords = keywordsField.getText();
		String groups = groupsField.getText();
		String difficulty = difficultyLevelComboBox.getValue();
		String author = "LoggedInUser"; // Replace with actual logged-in username

		if (validateArticleInput(title, description, content, keywords, groups, difficulty)) {
			long uniqueId = articleManager.generateUniqueId();
			HelpArticle newArticle = new HelpArticle(uniqueId, title, description, difficulty, content, keywords,
					groups, LocalDateTime.now(), LocalDateTime.now(), author);
			articleManager.addArticle(newArticle); // Use HelpArticleManager to add article
			updateArticleListView(articleListView, articleManager);
			showFeedback("Success", "Article added successfully.", statusLabel);
		} else {
			showFeedback("Error", "All fields must be filled.", statusLabel);
		}
	}

	private void editArticle(TextField titleField, TextField descriptionField, TextArea articleContent,
			TextField keywordsField, TextField groupsField, ComboBox<String> difficultyLevelComboBox,
			ListView<HelpArticle> articleListView, Label statusLabel) {
		HelpArticle selectedArticle = articleListView.getSelectionModel().getSelectedItem();
		if (selectedArticle != null) {
			titleField.setText(selectedArticle.getTitle());
			descriptionField.setText(selectedArticle.getDescription());
			keywordsField.setText(selectedArticle.getKeywords());
			groupsField.setText(selectedArticle.getGroups());
			difficultyLevelComboBox.setValue(selectedArticle.getDifficultyLevel());
			articleContent.setText(selectedArticle.getBodyContent());
		} else {
			showFeedback("Error", "Please select an article to edit.", statusLabel);
		}
	}

	private void saveEdits(TextField titleField, TextField descriptionField, TextArea articleContent,
			TextField keywordsField, TextField groupsField, ComboBox<String> difficultyLevelComboBox,
			ListView<HelpArticle> articleListView, HelpArticleManager articleManager, Label statusLabel) {
		HelpArticle selectedArticle = articleListView.getSelectionModel().getSelectedItem();
		if (selectedArticle != null) {
			String title = titleField.getText();
			String description = descriptionField.getText();
			String content = articleContent.getText();
			String keywords = keywordsField.getText();
			String groups = groupsField.getText();
			String difficulty = difficultyLevelComboBox.getValue();

			if (validateArticleInput(title, description, content, keywords, groups, difficulty)) {
				selectedArticle.setTitle(title);
				selectedArticle.setDescription(description);
				selectedArticle.setDifficultyLevel(difficulty);
				selectedArticle.setBodyContent(content);
				selectedArticle.setKeywords(keywords);
				selectedArticle.setGroups(groups);
				selectedArticle.setModifiedDate(LocalDateTime.now());
				articleManager.saveArticles(); // Save updated articles through HelpArticleManager
				updateArticleListView(articleListView, articleManager);
				showFeedback("Success", "Article edited successfully.", statusLabel);
			} else {
				showFeedback("Error", "All fields must be filled.", statusLabel);
			}
		}
	}

	private void deleteArticle(ListView<HelpArticle> articleListView, HelpArticleManager articleManager,
			Label statusLabel) {
		HelpArticle selectedArticle = articleListView.getSelectionModel().getSelectedItem();
		if (selectedArticle != null) {
			articleManager.deleteArticle(selectedArticle); // Use HelpArticleManager to delete article
			updateArticleListView(articleListView, articleManager);
			showFeedback("Success", "Article deleted successfully.", statusLabel);
		} else {
			showFeedback("Error", "Please select an article to delete.", statusLabel);
		}
	}

	private void backupArticles(HelpArticleManager articleManager, Label statusLabel) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save Backup File");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

		File file = fileChooser.showSaveDialog(new Stage());
		if (file != null) {
			if (articleManager.backupArticles(file)) { // Use HelpArticleManager for backup
				showFeedback("Success", "Backup completed successfully.", null);
			} else {
				showFeedback("Error", "Could not backup articles.", null);
			}
		}
	}

	private void restoreArticles(ListView<HelpArticle> articleListView, HelpArticleManager articleManager,
			Label statusLabel) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Backup File");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

		File file = fileChooser.showOpenDialog(new Stage());
		if (file != null) {
			boolean restored = articleManager.restoreArticles(file); // Use HelpArticleManager for restore
			if (restored) {
				updateArticleListView(articleListView, articleManager);
				showFeedback("Success", "Articles restored successfully.", statusLabel);
			} else {
				showFeedback("Error", "Could not restore articles.", statusLabel);
			}
		}
	}

	private void updateArticleListView(ListView<HelpArticle> articleListView, HelpArticleManager articleManager) {
	    articleListView.getItems().clear();
	    articleListView.getItems().addAll(articleManager.getArticles());  // Add all articles directly
	}


	private boolean validateArticleInput(String title, String description, String content, String keywords,
			String groups, String difficulty) {
		return !(title.isEmpty() || description.isEmpty() || content.isEmpty() || keywords.isEmpty() || groups.isEmpty()
				|| difficulty == null);
	}

	private void showFeedback(String title, String message, Label statusLabel) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
		statusLabel.setText(message); // Update the status label with feedback
	}

	public void showStudentWindow(Stage primaryStage) {
	    final File feedbackFile = new File("feedback.txt");
	    final List<HelpArticle> articles;
	    TableView<HelpArticle> articleTableView = new TableView<>();
	    Label activeGroupLabel = new Label("Active Group: ");
	    Label levelCountLabel = new Label("Articles by Level: ");
	    ComboBox<String> levelComboBox;
	    ComboBox<String> groupComboBox;

	    primaryStage.setTitle("Student View");

	    Label welcomeLabel = new Label("Welcome to the Student View!");
	    welcomeLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

	    GridPane layout = new GridPane();
	    layout.setPadding(new Insets(20));
	    layout.setVgap(10);
	    layout.setHgap(10);
	    layout.setAlignment(Pos.CENTER);

	    TextField searchField = new TextField();
	    searchField.setPromptText("Search articles...");

	    levelComboBox = new ComboBox<>();
	    levelComboBox.setItems(
	            FXCollections.observableArrayList("All Levels", "Beginner", "Intermediate", "Advanced", "Expert"));
	    levelComboBox.setValue("All Levels");

	    groupComboBox = new ComboBox<>();
	    groupComboBox.setItems(FXCollections.observableArrayList("All Groups", "SQL", "IDEs", "Java"));
	    groupComboBox.setValue("All Groups");

	    Button searchButton = new Button("Search");

	    articleTableView.setMinWidth(500);
	    articleTableView.setMaxHeight(300);

	    TableColumn<HelpArticle, Long> seqColumn = new TableColumn<>("Seq");
	    seqColumn.setCellValueFactory(cellData -> new SimpleLongProperty(cellData.getValue().getUniqueId()).asObject());
	    TableColumn<HelpArticle, String> titleColumn = new TableColumn<>("Title");
	    titleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
	    TableColumn<HelpArticle, String> authorColumn = new TableColumn<>("Author");
	    authorColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAuthor()));
	    TableColumn<HelpArticle, String> abstractColumn = new TableColumn<>("Abstract");
	    abstractColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));

	    articleTableView.getColumns().addAll(seqColumn, titleColumn, authorColumn, abstractColumn);

	    HelpArticleManager helpArticleManager = new HelpArticleManager();
	    helpArticleManager.loadArticles();
	    
	    // Filter articles using hasAccessRights before adding them to the list
	    articles = helpArticleManager.getArticles().stream()
	            .filter(this::hasAccessRights)
	            .toList();  // Collect only accessible articles

	    articleTableView.getItems().addAll(articles);

	    // Double-click event to view article details
	    articleTableView.setOnMouseClicked(event -> {
	        if (event.getClickCount() == 2) {
	            HelpArticle selectedArticle = articleTableView.getSelectionModel().getSelectedItem();
	            if (selectedArticle != null) {
	                viewArticleDetails(selectedArticle);
	            }
	        }
	    });

	    Button sendGeneralFeedbackButton = new Button("Send General Feedback");
	    Button sendSpecificFeedbackButton = new Button("Send Specific Feedback");

	    TextArea generalFeedbackArea = new TextArea();
	    generalFeedbackArea.setPromptText("Enter your general feedback here...");
	    generalFeedbackArea.setPrefHeight(100);

	    TextArea unableToFindArea = new TextArea();
	    unableToFindArea.setPromptText("What information could you not find?");
	    unableToFindArea.setPrefHeight(100);

	    TextArea additionalCommentsArea = new TextArea();
	    additionalCommentsArea.setPromptText("Additional comments...");
	    additionalCommentsArea.setPrefHeight(100);

	    sendGeneralFeedbackButton.setOnAction(e -> {
	        String feedback = "General Feedback: " + generalFeedbackArea.getText();
	        storeFeedback(feedback, "generic", feedbackFile);
	        generalFeedbackArea.clear();
	    });

	    sendSpecificFeedbackButton.setOnAction(e -> {
	        String feedback = "Specific Feedback:\n" + "Unable to find: " + unableToFindArea.getText() + "\n"
	                + "Additional comments: " + additionalCommentsArea.getText();
	        storeFeedback(feedback, "specific", feedbackFile);
	        unableToFindArea.clear();
	        additionalCommentsArea.clear();
	    });

	    updateGroupAndLevelCounts(articles, groupComboBox, activeGroupLabel, levelCountLabel);

	    searchButton.setOnAction(e -> {
	        String searchQuery = searchField.getText();
	        String selectedLevel = levelComboBox.getValue();
	        String selectedGroup = groupComboBox.getValue();

	        List<HelpArticle> articlesList = searchArticles(searchQuery, selectedLevel, selectedGroup, articles);
	        ObservableList<HelpArticle> observableArticles = FXCollections.observableArrayList(articlesList);
	        articleTableView.setItems(observableArticles);

	        updateGroupAndLevelCounts(articlesList, groupComboBox, activeGroupLabel, levelCountLabel);
	    });

	    // Add a back button for navigation
	    Button backButton = createBackButton(primaryStage, () -> showUserLoginWindow(primaryStage)); // Back to User Login

	    layout.add(welcomeLabel, 0, 0, 2, 1);
	    layout.add(new Label("Search:"), 0, 1);
	    layout.add(searchField, 1, 1);
	    layout.add(new Label("Filter by Level:"), 0, 2);
	    layout.add(levelComboBox, 1, 2);
	    layout.add(new Label("Filter by Group:"), 0, 3);
	    layout.add(groupComboBox, 1, 3);
	    layout.add(searchButton, 0, 4, 2, 1);
	    layout.add(activeGroupLabel, 0, 5, 2, 1);
	    layout.add(levelCountLabel, 0, 6, 2, 1);
	    layout.add(articleTableView, 0, 7, 2, 1);
	    layout.add(sendGeneralFeedbackButton, 0, 8);
	    layout.add(generalFeedbackArea, 1, 8);
	    layout.add(sendSpecificFeedbackButton, 0, 9);
	    layout.add(unableToFindArea, 1, 9);
	    layout.add(additionalCommentsArea, 1, 10);
	    layout.add(backButton, 0, 11, 2, 1); // Add Back button at the end of the layout

	    Scene scene = new Scene(layout, 600, 600);
	    primaryStage.setScene(scene);
	    primaryStage.show();
	}

	private List<HelpArticle> searchArticles(String searchQuery, String level, String group, List<HelpArticle> articles) {
		List<HelpArticle> result = new ArrayList<>();
		for (HelpArticle article : articles) {
			boolean matchesSearch = article.getTitle().contains(searchQuery)
					|| article.getAuthor().contains(searchQuery) || article.getDescription().contains(searchQuery);
			boolean matchesLevel = level.equals("All Levels") || article.getDifficultyLevel().equals(level);
			boolean matchesGroup = group.equals("All Groups") || article.getGroups().contains(group);

			if (matchesSearch && (matchesLevel && matchesGroup)) {
				result.add(article);
			}
		}
		return result;
	}

	private void storeFeedback(String feedback, String feedbackType, File feedbackFile) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(feedbackFile, true))) {
			String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
			writer.write("[" + timestamp + "] " + feedbackType.toUpperCase() + ":\n" + feedback + "\n\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	private void updateGroupAndLevelCounts(List<HelpArticle> articles, ComboBox<String> groupComboBox, Label activeGroupLabel, Label levelCountLabel) {
		String activeGroup = groupComboBox.getValue();
		int beginnerCount = 0, intermediateCount = 0, advancedCount = 0, expertCount = 0;

		for (HelpArticle article : articles) {
			switch (article.getDifficultyLevel()) {
			case "Beginner":
				beginnerCount++;
				break;
			case "Intermediate":
				intermediateCount++;
				break;
			case "Advanced":
				advancedCount++;
				break;
			case "Expert":
				expertCount++;
				break;
			}
		}
		levelCountLabel
				.setText(String.format("Articles by Level: Beginner: %d, Intermediate: %d, Advanced: %d, Expert: %d",
						beginnerCount, intermediateCount, advancedCount, expertCount));
		activeGroupLabel.setText("Active Group: " + activeGroup);
	}

	private void viewArticleDetails(HelpArticle article) {
	    Stage articleDetailStage = new Stage();
	    articleDetailStage.setTitle("Article Details");

	    GridPane detailLayout = new GridPane();
	    detailLayout.setPadding(new Insets(20));
	    detailLayout.setVgap(10);
	    detailLayout.setHgap(10);
	    detailLayout.setAlignment(Pos.CENTER);

	    // Labels displaying article details
	    Label titleLabel = new Label("Title: " + article.getTitle());
	    Label authorLabel = new Label("Author: " + article.getAuthor());
	    Label descriptionLabel = new Label("Description: " + article.getDescription());
	    Label bodyLabel = new Label("Content: " + article.getBodyContent());
	    Label createdDateLabel = new Label("Created Date: " + article.getCreatedDate());
	    Label modifiedDateLabel = new Label("Last Modified: " + article.getModifiedDate());

	    // Adding labels to layout
	    detailLayout.add(titleLabel, 0, 0);
	    detailLayout.add(authorLabel, 0, 1);
	    detailLayout.add(descriptionLabel, 0, 2);
	    detailLayout.add(bodyLabel, 0, 3);
	    detailLayout.add(createdDateLabel, 0, 4);
	    detailLayout.add(modifiedDateLabel, 0, 5);

	    // Close button
	    Button closeButton = new Button("Close");
	    closeButton.setOnAction(e -> articleDetailStage.close());
	    detailLayout.add(closeButton, 0, 6);

	    Scene detailScene = new Scene(detailLayout, 400, 400);
	    articleDetailStage.setScene(detailScene);
	    articleDetailStage.show();
	}


	private boolean hasAccessRights(HelpArticle article) {
		return !article.getDifficultyLevel().equals("Advanced");
	}

	public void showInstructorWindow(Stage primaryStage) {
	    final List<HelpArticle> articles;
	    TableView<HelpArticle> articleTableView = new TableView<>();
	    Label activeGroupLabel = new Label("Active Group: ");
	    Label levelCountLabel = new Label("Articles by Level: ");
	    ComboBox<String> levelComboBox;
	    ComboBox<String> groupComboBox;
	    Label statusLabel = new Label(); // Status label for feedback messages
	    statusLabel.setStyle("-fx-text-fill: green; -fx-font-size: 14px;");
	    primaryStage.setTitle("Instructor Interface");

	    Label titleLabel = new Label("Instructor Interface");
	    titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2e9ed6;");

	    GridPane layout = new GridPane();
	    layout.setPadding(new Insets(20));
	    layout.setVgap(10);
	    layout.setHgap(10);
	    layout.setAlignment(Pos.CENTER);

	    TextField searchField = new TextField();
	    searchField.setPromptText("Search articles...");

	    levelComboBox = new ComboBox<>();
	    levelComboBox.setItems(FXCollections.observableArrayList("All Levels", "Beginner", "Intermediate", "Advanced", "Expert"));
	    levelComboBox.setValue("All Levels");

	    groupComboBox = new ComboBox<>();
	    groupComboBox.setItems(FXCollections.observableArrayList("All Groups", "SQL", "IDEs", "Java"));
	    groupComboBox.setValue("All Groups");

	    Button searchButton = new Button("Search");
	    Button backupAllButton = new Button("Backup All Articles");
	    Button restoreButton = new Button("Restore Articles");
	    Button backupByGroupButton = new Button("Backup by Group");

	    articleTableView.setMinWidth(500);
	    articleTableView.setMaxHeight(300);

	    TableColumn<HelpArticle, Long> seqColumn = new TableColumn<>("Seq");
	    seqColumn.setCellValueFactory(cellData -> new SimpleLongProperty(cellData.getValue().getUniqueId()).asObject());
	    TableColumn<HelpArticle, String> titleColumn = new TableColumn<>("Title");
	    titleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
	    TableColumn<HelpArticle, String> authorColumn = new TableColumn<>("Author");
	    authorColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAuthor()));
	    TableColumn<HelpArticle, String> abstractColumn = new TableColumn<>("Abstract");
	    abstractColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));

	    articleTableView.getColumns().addAll(seqColumn, titleColumn, authorColumn, abstractColumn);

	    HelpArticleManager helpArticleManager = new HelpArticleManager();
	    helpArticleManager.loadArticles();
	    articles = helpArticleManager.getArticles();
	    articleTableView.getItems().addAll(articles);

	    // Double-click event to view article details
	    articleTableView.setOnMouseClicked(event -> {
	        if (event.getClickCount() == 2) {
	            HelpArticle selectedArticle = articleTableView.getSelectionModel().getSelectedItem();
	            if (selectedArticle != null) {
	                viewArticleDetails(selectedArticle);
	            }
	        }
	    });

	    updateGroupAndLevelCounts(articles, groupComboBox, activeGroupLabel, levelCountLabel);

	    searchButton.setOnAction(e -> {
	        String searchQuery = searchField.getText();
	        String selectedLevel = levelComboBox.getValue();
	        String selectedGroup = groupComboBox.getValue();

	        List<HelpArticle> articlesList = searchArticles(searchQuery, selectedLevel, selectedGroup, articles);
	        ObservableList<HelpArticle> observableArticles = FXCollections.observableArrayList(articlesList);
	        articleTableView.setItems(observableArticles);

	        updateGroupAndLevelCounts(articles, groupComboBox, activeGroupLabel, levelCountLabel);
	    });

	    // Backup All Articles
	    backupAllButton.setOnAction(e -> {
	        FileChooser fileChooser = new FileChooser();
	        fileChooser.setTitle("Save Backup File");
	        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
	        File backupFile = fileChooser.showSaveDialog(primaryStage);
	        if (backupFile != null && helpArticleManager.backupArticles(backupFile)) {
	            showFeedback("Success", "All articles backed up successfully.", statusLabel);
	        } else {
	            showFeedback("Error", "Failed to backup articles.", statusLabel);
	        }
	    });

	    // Backup Articles by Group
	    backupByGroupButton.setOnAction(e -> {
	        String selectedGroup = groupComboBox.getValue();
	        FileChooser fileChooser = new FileChooser();
	        fileChooser.setTitle("Save Group Backup File");
	        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
	        File backupFile = fileChooser.showSaveDialog(primaryStage);
	        if (backupFile != null && helpArticleManager.backupArticlesByGroup(selectedGroup, backupFile)) {
	            showFeedback("Success", "Group articles backed up successfully.", statusLabel);
	        } else {
	            showFeedback("Error", "Failed to backup group articles.", statusLabel);
	        }
	    });

	    // Restore and Merge Articles from Backup
	    restoreButton.setOnAction(e -> {
	        FileChooser fileChooser = new FileChooser();
	        fileChooser.setTitle("Select Backup File to Restore");
	        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
	        File restoreFile = fileChooser.showOpenDialog(primaryStage);
	        if (restoreFile != null && helpArticleManager.mergeBackup(restoreFile)) {
	            articleTableView.getItems().clear();
	            articleTableView.getItems().addAll(helpArticleManager.getArticles());
	            showFeedback("Success", "Articles restored and merged successfully.", statusLabel);
	        } else {
	            showFeedback("Error", "Failed to restore articles.", statusLabel);
	        }
	    });

	    layout.add(titleLabel, 0, 0, 2, 1);
	    layout.add(new Label("Search:"), 0, 1);
	    layout.add(searchField, 1, 1);
	    layout.add(new Label("Filter by Level:"), 0, 2);
	    layout.add(levelComboBox, 1, 2);
	    layout.add(new Label("Filter by Group:"), 0, 3);
	    layout.add(groupComboBox, 1, 3);
	    layout.add(searchButton, 0, 4, 2, 1);
	    layout.add(activeGroupLabel, 0, 5, 2, 1);
	    layout.add(levelCountLabel, 0, 6, 2, 1);
	    layout.add(articleTableView, 0, 7, 2, 1);
	    layout.add(backupAllButton, 0, 8);
	    layout.add(backupByGroupButton, 1, 8);
	    layout.add(restoreButton, 0, 9, 2, 1);
	    layout.add(statusLabel, 0, 10, 2, 1); // Add status label for feedback

	    Scene scene = new Scene(layout, 600, 600);
	    primaryStage.setScene(scene);
	    primaryStage.show();
	}
	
	public void showGroupManagementWindow(Stage primaryStage) {
	    Label groupLabel = new Label("Select Group:");
	    ComboBox<String> groupComboBox = new ComboBox<>(FXCollections.observableArrayList("SQL", "IDEs", "Java"));
	    Label userLabel = new Label("User:");
	    TextField userField = new TextField();
	    CheckBox isAdminCheckbox = new CheckBox("Grant Admin Rights");
	    CheckBox isInstructorCheckbox = new CheckBox("Is Instructor"); // New checkbox for instructor status

	    Label statusLabel = new Label();

	    Button addUserButton = new Button("Add User to Group");
	    Button removeUserButton = new Button("Remove User from Group");

	    SpecialAccessManager specialAccessManager = new SpecialAccessManager();

	    // Add user to group action
	    addUserButton.setOnAction(e -> {
	        String group = groupComboBox.getValue();
	        String username = userField.getText();
	        boolean isAdmin = isAdminCheckbox.isSelected();
	        boolean isInstructor = isInstructorCheckbox.isSelected(); // Get the instructor status

	        if (specialAccessManager.addUserToGroup(group, username, isAdmin, isInstructor)) {
	            statusLabel.setText("User added to group successfully.");
	        } else {
	            statusLabel.setText("Failed to add user to group.");
	        }
	    });

	    // Remove user from group action
	    removeUserButton.setOnAction(e -> {
	        String group = groupComboBox.getValue();
	        String username = userField.getText();

	        if (specialAccessManager.removeUserFromGroup(group, username)) {
	            statusLabel.setText("User removed from group successfully.");
	        } else {
	            statusLabel.setText("Cannot remove the last admin from the group.");
	        }
	    });

	    GridPane layout = new GridPane();
	    layout.setPadding(new Insets(20));
	    layout.setVgap(10);
	    layout.setHgap(10);
	    layout.setAlignment(Pos.CENTER);

	    layout.add(groupLabel, 0, 0);
	    layout.add(groupComboBox, 1, 0);
	    layout.add(userLabel, 0, 1);
	    layout.add(userField, 1, 1);
	    layout.add(isAdminCheckbox, 1, 2);
	    layout.add(isInstructorCheckbox, 1, 3); // Add the new checkbox to the layout
	    layout.add(addUserButton, 0, 4);
	    layout.add(removeUserButton, 1, 4);
	    layout.add(statusLabel, 0, 5, 2, 1);

	    Scene scene = new Scene(layout, 400, 300);
	    primaryStage.setScene(scene);
	    primaryStage.show();
	}

	
	public void openGrantSpecialAccessWindow(Stage primaryStage) {
	    Label userLabel = new Label("Username:");
	    TextField userField = new TextField();

	    Label accessTypeLabel = new Label("Access Type:");
	    ComboBox<String> accessTypeComboBox = new ComboBox<>(FXCollections.observableArrayList("Group", "Article"));
	    accessTypeComboBox.setValue("Group");

	    Label idLabel = new Label("ID:");
	    TextField idField = new TextField();
	    idField.setPromptText("Enter Group ID or Article ID");

	    Button grantAccessButton = new Button("Grant Access");
	    Label statusLabel = new Label();

	    grantAccessButton.setOnAction(e -> {
	        String username = userField.getText();
	        String accessType = accessTypeComboBox.getValue();
	        String id = idField.getText();

	        SpecialAccessManager specialAccessManager = new SpecialAccessManager();
	        boolean success;
	        if ("Group".equals(accessType)) {
	            success = specialAccessManager.grantSpecialAccessToGroup(username, id);
	        } else {
	            success = specialAccessManager.grantSpecialAccessToArticle(username, id);
	        }

	        statusLabel.setText(success ? "Access granted successfully." : "Failed to grant access.");
	    });

	    GridPane layout = new GridPane();
	    layout.setPadding(new Insets(20));
	    layout.setVgap(10);
	    layout.setHgap(10);
	    layout.add(userLabel, 0, 0);
	    layout.add(userField, 1, 0);
	    layout.add(accessTypeLabel, 0, 1);
	    layout.add(accessTypeComboBox, 1, 1);
	    layout.add(idLabel, 0, 2);
	    layout.add(idField, 1, 2);
	    layout.add(grantAccessButton, 0, 3);
	    layout.add(statusLabel, 1, 3);

	    Scene scene = new Scene(layout, 300, 200);
	    primaryStage.setScene(scene);
	    primaryStage.show();
	}

}