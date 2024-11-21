package cse360project;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ArticleManagementWindow extends Application {

    private final List<HelpArticle> articles; // List to store articles
    private final TextArea articleContent; // TextArea for article content
    private final ListView<HelpArticle> articleListView; // ListView to display articles
    private final TextField titleField, descriptionField, keywordsField, groupsField;
    private final ComboBox<String> difficultyLevelComboBox;
    private final Label statusLabel; // Status label for feedback

    public ArticleManagementWindow() {
        articles = new ArrayList<>(); // Initialize the articles list
        loadArticles(); // Load existing articles from a file

        articleContent = new TextArea(); // TextArea for article content
        articleListView = new ListView<>(); // ListView for displaying articles
        titleField = new TextField(); // Field for title
        descriptionField = new TextField(); // Field for description
        keywordsField = new TextField(); // Field for keywords
        groupsField = new TextField(); // Field for groups
        difficultyLevelComboBox = new ComboBox<>(); // ComboBox for difficulty level
        statusLabel = new Label(); // Status label for feedback

        updateArticleListView(); // Update the ListView with loaded articles
        articleContent.setPromptText("Enter article content here..."); // Placeholder text

        // Initialize ComboBox values
        difficultyLevelComboBox.getItems().addAll("Beginner", "Intermediate", "Advanced", "Expert");
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Article Management");

        // Create buttons for article management
        Button addButton = new Button("Add Article");
        Button editButton = new Button("Edit Article");
        Button deleteButton = new Button("Delete Article");
        Button saveEditsButton = new Button("Save Edits");
        Button backupButton = new Button("Backup Articles");
        Button restoreButton = new Button("Restore Articles");

        // Set actions for buttons
        addButton.setOnAction(e -> addArticle());
        editButton.setOnAction(e -> editArticle());
        deleteButton.setOnAction(e -> deleteArticle());
        saveEditsButton.setOnAction(e -> saveEdits());
        backupButton.setOnAction(e -> backupArticles());
        restoreButton.setOnAction(e -> restoreArticles());

        // Layout setup for the Article Management Window
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(15));
        layout.setAlignment(Pos.CENTER);

        // Add a title label
        Label titleLabel = new Label("Article Management");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Form for article details
        VBox formLayout = new VBox(10, 
            new Label("Title:"), titleField, 
            new Label("Description:"), descriptionField,
            new Label("Keywords:"), keywordsField,
            new Label("Groups:"), groupsField,
            new Label("Difficulty Level:"), difficultyLevelComboBox,
            new Label("Content:"), articleContent,
            statusLabel // Status label
        );

        // Horizontal box for buttons
        HBox buttonLayout = new HBox(10, addButton, editButton, deleteButton, saveEditsButton, backupButton, restoreButton);
        buttonLayout.setAlignment(Pos.CENTER);

        layout.getChildren().addAll(titleLabel, formLayout, articleListView, buttonLayout);

        Scene scene = new Scene(layout, 600, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void addArticle() {
        String title = titleField.getText();
        String description = descriptionField.getText();
        String content = articleContent.getText();
        String keywords = keywordsField.getText();
        String groups = groupsField.getText();
        String difficulty = difficultyLevelComboBox.getValue();
        
        if (validateArticleInput(title, description, content, keywords, groups, difficulty)) {
            long uniqueId = generateUniqueId();
            HelpArticle newArticle = new HelpArticle(uniqueId, title, description, difficulty, content, keywords, groups, LocalDateTime.now(), LocalDateTime.now(), "Instructor");
            articles.add(newArticle);
            updateArticleListView(); // Refresh the ListView
            saveArticles(); // Save updated articles to file
            clearFormFields(); // Clear form fields
            showFeedback("Success", "Article added successfully.");
        } else {
            showFeedback("Error", "All fields must be filled.");
        }
    }

    private void editArticle() {
        HelpArticle selectedArticle = articleListView.getSelectionModel().getSelectedItem();
        if (selectedArticle != null) {
            titleField.setText(selectedArticle.getTitle());
            descriptionField.setText(selectedArticle.getDescription());
            keywordsField.setText(selectedArticle.getKeywords());
            groupsField.setText(selectedArticle.getGroups());
            difficultyLevelComboBox.setValue(selectedArticle.getDifficultyLevel());
            articleContent.setText(selectedArticle.getBodyContent());
        } else {
            showFeedback("Error", "Please select an article to edit.");
        }
    }

    private void saveEdits() {
        HelpArticle selectedArticle = articleListView.getSelectionModel().getSelectedItem();
        if (selectedArticle != null) {
            int index = articleListView.getSelectionModel().getSelectedIndex();
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
                updateArticleListView(); // Refresh the ListView
                saveArticles(); // Save updated articles to file
                clearFormFields(); // Clear form fields
                showFeedback("Success", "Article edited successfully.");
            } else {
                showFeedback("Error", "All fields must be filled.");
            }
        }
    }

    private void deleteArticle() {
        HelpArticle selectedArticle = articleListView.getSelectionModel().getSelectedItem();
        if (selectedArticle != null) {
            articles.remove(selectedArticle); // Remove selected article
            updateArticleListView(); // Refresh the ListView
            saveArticles(); // Save updated articles to file
            clearFormFields(); // Clear form fields
            showFeedback("Success", "Article deleted successfully.");
        } else {
            showFeedback("Error", "Please select an article to delete.");
        }
    }
    
    private void backupArticles() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Backup File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        
        File file = fileChooser.showSaveDialog(new Stage());
        if (file != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (HelpArticle article : articles) {
                    writer.write(article.toString());
                    writer.newLine();
                }
                showFeedback("Success", "Backup completed successfully.");
            } catch (IOException e) {
                showFeedback("Error", "Could not backup articles: " + e.getMessage());
            }
        }
    }

    private void restoreArticles() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Backup File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                List<HelpArticle> backupArticles = new ArrayList<>();
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    long uniqueId = Long.parseLong(parts[0]);
                    String title = parts[1];
                    String description = parts[2];
                    String difficulty = parts[3];
                    String content = parts[4];
                    String keywords = parts[5];
                    String groups = parts[6];
                    LocalDateTime createdDate = LocalDateTime.parse(parts[7]);
                    LocalDateTime modifiedDate = LocalDateTime.parse(parts[8]);
                    String author = parts[9];

                    HelpArticle backupArticle = new HelpArticle(uniqueId, title, description, difficulty, content, keywords, groups, createdDate, modifiedDate, author);
                    backupArticles.add(backupArticle);
                }

                // Merge or replace articles based on user's choice
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Restore Articles");
                alert.setHeaderText("Restore Options");
                alert.setContentText("Choose your restore option:");
                ButtonType mergeButton = new ButtonType("Merge");
                ButtonType replaceButton = new ButtonType("Replace");
                ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
                alert.getButtonTypes().setAll(mergeButton, replaceButton, cancelButton);

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == mergeButton) {
                    // Merge articles
                    for (HelpArticle backupArticle : backupArticles) {
                        if (articles.stream().noneMatch(a -> a.getUniqueId() == backupArticle.getUniqueId())) {
                            articles.add(backupArticle);
                        }
                    }
                    showFeedback("Success", "Backup articles merged successfully.");
                } else if (result.isPresent() && result.get() == replaceButton) {
                    // Replace articles
                    articles.clear();
                    articles.addAll(backupArticles);
                    showFeedback("Success", "Articles replaced successfully.");
                }
                updateArticleListView(); // Refresh the ListView
                saveArticles(); // Save updated articles to file
            } catch (IOException e) {
                showFeedback("Error", "Could not restore articles: " + e.getMessage());
            }
        }
    }

	private void updateArticleListView() {
		articleListView.getItems().clear(); // Clear the ListView
		articleListView.getItems().addAll(articles); // Add all articles to the ListView
	}

	private void loadArticles() {
		// Load articles from a file
		try (BufferedReader reader = new BufferedReader(new FileReader("articles.txt"))) {
			String line;
			while ((line = reader.readLine()) != null) {
				if (!line.trim().isEmpty()) { // Ignore blank lines
					String[] fields = line.split("\\|");
					if (fields.length == 9) { // Ensure that all fields are present
						long id = Long.parseLong(fields[0]);
						String title = fields[1];
						String description = fields[2];
						String difficulty = fields[3];
						String bodyContent = fields[4];
						String keywords = fields[5];
						String groups = fields[6];
						String createdDate = fields[7];
						String modifiedDate = fields[8];

						HelpArticle article = new HelpArticle(id, title, description, difficulty, bodyContent, keywords, groups, null, null, createdDate);
						articles.add(article);
					} else {
						System.err.println("Invalid article format: " + line); // Log invalid line
					}
				}
			}
		} catch (IOException e) {
			// Handle the exception (e.g., file not found)
			showAlert("Error", "Could not load articles: " + e.getMessage());
		} catch (NumberFormatException e) {
			showAlert("Error", "Invalid article ID in file: " + e.getMessage());
		}
	}

	private void saveArticles() {
		// Define a date formatter for the created and modified dates
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		// Save articles to a file
		try (BufferedWriter writer = new BufferedWriter(new FileWriter("articles.txt"))) {
			for (HelpArticle article : articles) {
				writer.write(String.join("|", 
						String.valueOf(article.getUniqueId()), 
						article.getTitle(), 
						article.getDescription(), 
						article.getDifficultyLevel(), 
						article.getBodyContent(),
						article.getKeywords(), 
						article.getGroups(), 
						article.getCreatedDate().format(formatter), 
						article.getModifiedDate().format(formatter))); // Ensure dates are formatted as strings
						writer.newLine();
			}
		} catch (IOException e) {
			showAlert("Error", "Could not save articles: " + e.getMessage());
		}
	}

	private boolean validateArticleInput(String title, String description, String content, String keywords, String groups, String difficulty) {
        return !(title.isEmpty() || description.isEmpty() || content.isEmpty() || keywords.isEmpty() || groups.isEmpty() || difficulty == null);
    }

    private void showFeedback(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
        statusLabel.setText(message); // Update the status label with feedback
    }


    private void clearFormFields() {
        titleField.clear();
        descriptionField.clear();
        keywordsField.clear();
        groupsField.clear();
        difficultyLevelComboBox.setValue(null);
        articleContent.clear();
    }

	private long generateUniqueId() {
		// Generate a unique ID for the article
		return articles.isEmpty() ? 1 : articles.get(articles.size() - 1).getUniqueId() + 1;
	}

	private String getCurrentDate() {
		// Get the current date as a String (could be formatted as needed)
		return java.time.LocalDateTime.now().toString();
	}

	private void showAlert(String title, String message) {
		// Show a simple alert dialog with a message
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	public static void main(String[] args) {
		launch(args); // Launch the JavaFX application
	}
}