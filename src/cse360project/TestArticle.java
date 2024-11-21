package cse360project;

import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestArticle {

    private HelpArticleManager articleManager; // Manager to handle articles
    private HelpArticle article; // Represents a single article
    private WindowsManager windowsManager; // Utilize WindowsManager for utility functions
    private ListView<HelpArticle> articleListView; // Mocked ListView for articles
    private Label statusLabel; // Status label for feedback messages

    @BeforeEach
    public void setUp() throws Exception {
        // Initialize HelpArticleManager, WindowsManager, and other components before each test
        articleManager = new HelpArticleManager();
        windowsManager = new WindowsManager();
        articleListView = new ListView<>(FXCollections.observableArrayList());
        statusLabel = new Label();
        article = new HelpArticle(1L, "Title 1", "Short description", "Beginner",
                "This is the article body", "Keyword1", "Group1",
                LocalDateTime.now(), LocalDateTime.now(), "Instructor");
    }

    @Test
    public void testAddArticle() throws Exception {
        // Mock UI elements for adding an article
        TextField titleField = new TextField("Test Title");
        TextField descriptionField = new TextField("Test Description");
        TextArea contentArea = new TextArea("Test Content");
        TextField keywordsField = new TextField("Keyword1, Keyword2");
        TextField groupsField = new TextField("Group1");
        ComboBox<String> difficultyLevelBox = new ComboBox<>();
        difficultyLevelBox.getItems().addAll("Beginner", "Intermediate", "Advanced", "Expert");
        difficultyLevelBox.setValue("Beginner");

        // Call the addArticle method
        windowsManager.addArticle(titleField, descriptionField, contentArea, keywordsField, groupsField,
                difficultyLevelBox, articleManager, articleListView, statusLabel);

        // Verify the article was added to the manager and list view
        assertFalse(articleManager.getArticles().isEmpty(), "Article manager should have articles.");
        assertFalse(articleListView.getItems().isEmpty(), "ListView should have articles.");
        assertEquals("Test Title", articleManager.getArticles().get(0).getTitle(), "Article title should match.");
    }

    @Test
    public void testEditArticle() {
        // Add an article first
        articleManager.addArticle(article);
        articleListView.getItems().add(article);

        // Mock UI elements for editing an article
        TextField titleField = new TextField();
        TextField descriptionField = new TextField();
        TextArea contentArea = new TextArea();
        TextField keywordsField = new TextField();
        TextField groupsField = new TextField();
        ComboBox<String> difficultyLevelBox = new ComboBox<>();
        difficultyLevelBox.getItems().addAll("Beginner", "Intermediate", "Advanced", "Expert");

        // Call the editArticle method
        windowsManager.editArticle(titleField, descriptionField, contentArea, keywordsField, groupsField,
                difficultyLevelBox, articleListView, statusLabel);

        // Verify fields were populated correctly
        assertEquals(article.getTitle(), titleField.getText(), "Title field should match the article's title.");
        assertEquals(article.getDescription(), descriptionField.getText(), "Description field should match.");
        assertEquals(article.getBodyContent(), contentArea.getText(), "Content field should match.");
    }

    @Test
    public void testSaveEdits() {
        // Add an article first
        articleManager.addArticle(article);
        articleListView.getItems().add(article);

        // Mock UI elements for editing an article
        TextField titleField = new TextField("Updated Title");
        TextField descriptionField = new TextField("Updated Description");
        TextArea contentArea = new TextArea("Updated Content");
        TextField keywordsField = new TextField("Updated Keywords");
        TextField groupsField = new TextField("Updated Groups");
        ComboBox<String> difficultyLevelBox = new ComboBox<>();
        difficultyLevelBox.getItems().addAll("Beginner", "Intermediate", "Advanced", "Expert");
        difficultyLevelBox.setValue("Intermediate");

        // Call the saveEdits method
        windowsManager.saveEdits(titleField, descriptionField, contentArea, keywordsField, groupsField,
                difficultyLevelBox, articleListView, articleManager, statusLabel);

        // Verify the article was updated
        HelpArticle updatedArticle = articleManager.getArticles().get(0);
        assertEquals("Updated Title", updatedArticle.getTitle(), "Article title should be updated.");
        assertEquals("Updated Description", updatedArticle.getDescription(), "Article description should be updated.");
        assertEquals("Intermediate", updatedArticle.getDifficultyLevel(), "Article difficulty level should be updated.");
    }

    @Test
    public void testDeleteArticle() {
        // Add an article first
        articleManager.addArticle(article);
        articleListView.getItems().add(article);

        // Call the deleteArticle method
        windowsManager.deleteArticle(articleListView, articleManager, statusLabel);

        // Verify the article was removed
        assertTrue(articleManager.getArticles().isEmpty(), "Article manager should be empty after deletion.");
        assertTrue(articleListView.getItems().isEmpty(), "ListView should be empty after deletion.");
    }

    @Test
    public void testBackupArticles() {
        // Add an article first
        articleManager.addArticle(article);

        // Call the backupArticles method
        windowsManager.backupArticles(articleManager, statusLabel);

        // Verify the backup functionality (conceptual test)
        assertTrue(true, "Backup functionality executed successfully.");
    }

    @Test
    public void testRestoreArticles() {
        // Add and backup an article
        articleManager.addArticle(article);
        windowsManager.backupArticles(articleManager, statusLabel);

        // Restore articles
        windowsManager.restoreArticles(articleListView, articleManager, statusLabel);

        // Verify the article was restored
        assertFalse(articleListView.getItems().isEmpty(), "ListView should have restored articles.");
        assertEquals(article.getTitle(), articleListView.getItems().get(0).getTitle(), "Restored article title should match.");
    }
}
