import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class LoginApp extends Application {

    private PasswordManager passwordManager = new PasswordManager();
    private RoleManager roleManager = new RoleManager();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Login - Secure Identity Management");

        // Grid for form layout
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(10);
        grid.setHgap(10);

        // Username field
        Label usernameLabel = new Label("Username:");
        TextField usernameInput = new TextField();
        grid.add(usernameLabel, 0, 0);
        grid.add(usernameInput, 1, 0);

        // Password field
        Label passwordLabel = new Label("Password:");
        PasswordField passwordInput = new PasswordField();
        grid.add(passwordLabel, 0, 1);
        grid.add(passwordInput, 1, 1);

        // Login Button
        Button loginButton = new Button("Login");
        grid.add(loginButton, 1, 2);

        // Action on login click
        loginButton.setOnAction(e -> {
            String username = usernameInput.getText();
            String password = passwordInput.getText();

            // Password validation here (for demonstration, always correct)
            try {
                User user = roleManager.userDatabase.get(username);
                if (user != null && passwordManager.verifyPassword(password, user.getPasswordHash())) {
                    // Redirect based on role (simple example)
                    if (user.getRoles().contains("Admin")) {
                        System.out.println("Welcome, Admin!");
                    } else {
                        System.out.println("Welcome, " + user.getRoles().get(0) + "!");
                    }
                } else {
                    System.out.println("Invalid username or password.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        Scene scene = new Scene(grid, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}