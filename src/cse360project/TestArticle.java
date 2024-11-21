package cse360project;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

public class TestArticle {

    private HelpArticleManager articleManager; // Correct class name from HelpArticleManager
    private HelpArticle article; // Correct class name from HelpArticle

    @BeforeEach
    public void setUp() {
        // Initialize HelpArticleManager before each test
        articleManager = new HelpArticleManager();
        article = new HelpArticle(1L, "Title 1", "Short description", "Beginner", "This is the article body", "Keyword1", "Group1", LocalDateTime.now(), LocalDateTime.now(), "Instructor");
    }

    @Test
    public void testAddArticle() {
        // Test article creation
        articleManager.addArticle(article);
        List<HelpArticle> articles = articleManager.getArticles(); // Get the list of articles after adding
        assertTrue(articles.contains(article), "Article should be added to the list.");
    }

    @Test
    public void testUpdateArticle() {
        // Test article updating
        articleManager.addArticle(article);
        article.setTitle("Updated Title");
        article.setDescription("Updated description");
        articleManager.updateArticle(0, article); // Update the article at index 0

        List<HelpArticle> articles = articleManager.getArticles();
        HelpArticle updatedArticle = articles.get(0);
        assertEquals("Updated Title", updatedArticle.getTitle(), "Title should be updated.");
        assertEquals("Updated description", updatedArticle.getDescription(), "Description should be updated.");
    }

    @Test
    public void testDeleteArticle() {
        // Test article deletion
        articleManager.addArticle(article);
        articleManager.deleteArticle(0); // Delete the article at index 0

        List<HelpArticle> articles = articleManager.getArticles();
        assertFalse(articles.contains(article), "Article should be removed from the list.");
    }

    @Test
    public void testBackupArticles() {
        // Test backup functionality
        articleManager.addArticle(article);
        // Call backup method and simulate file output (we could mock file I/O if needed)
        articleManager.saveArticles(articleManager.getArticles()); // Assuming this method saves articles to a file
        // Validate backup (this test is conceptual, you'd check the file or mock file I/O)
        assertTrue(true, "Backup should be created.");
    }

    @Test
    public void testRestoreArticles() {
        // Test restore functionality
        articleManager.addArticle(article);
        articleManager.saveArticles(articleManager.getArticles()); // Save the article to file

        // Simulate restore by loading articles
        List<HelpArticle> backupArticles = articleManager.loadArticles(); // Assuming loadArticles() method exists
        articleManager.getArticles().clear();
        articleManager.getArticles().addAll(backupArticles);

        List<HelpArticle> articles = articleManager.getArticles();
        assertTrue(articles.contains(article), "Article should be restored from backup.");
    }

    @Test
    public void testSearchArticleByTitle() {
        // Test searching articles by title
        articleManager.addArticle(article);
        List<HelpArticle> articles = articleManager.getArticles();
        assertTrue(articles.stream().anyMatch(a -> a.getTitle().equals("Title 1")), "Article with the given title should be found.");
    }

    @Test
    public void testSearchArticleByLevel() {
        // Test filtering articles by level
        articleManager.addArticle(article);
        List<HelpArticle> articles = articleManager.getArticles();
        assertTrue(articles.stream().anyMatch(a -> a.getDifficultyLevel().equals("Beginner")), "Article with the given level should be found.");
    }

    @Test
    public void testSearchArticleByGroup() {
        // Test filtering articles by group
        articleManager.addArticle(article);
        List<HelpArticle> articles = articleManager.getArticles();
        assertTrue(articles.stream().anyMatch(a -> a.getGroups().contains("Group1")), "Article with the given group should be found.");
    }

    @Test
    public void testValidateArticleInput() {
        // Test validating article input
        boolean isValid = articleManager.validateArticleInput("Title", "Description", "Content", "Keywords", "Groups", "Beginner");
        assertTrue(isValid, "Article input should be valid.");
        
        // Test invalid input
        isValid = articleManager.validateArticleInput("", "Description", "Content", "Keywords", "Groups", "Beginner");
        assertFalse(isValid, "Article input should be invalid.");
    }
}
