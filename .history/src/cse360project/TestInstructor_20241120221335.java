package cse360mainproject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestInstructor {

    private WindowsManager windowsManager;
    private HelpArticleManager articleManager;

    @BeforeEach
    public void setUp() {
        windowsManager = new WindowsManager();
        articleManager = new HelpArticleManager();
        articleManager.loadArticles(); // Load articles into the manager
    }

    @Test
    public void testSearchArticlesByLevel() {
        String level = "Beginner";
        String query = "";
        String group = "All Groups";

        int articlesBeforeSearch = articleManager.getArticles().size();
        assertTrue(articlesBeforeSearch > 0, "There should be articles in the system.");

        // Simulate a search
        long matchingArticles = articleManager.getArticles().stream()
                .filter(article -> article.getDifficultyLevel().equals(level))
                .count();

        assertTrue(matchingArticles > 0, "Articles matching the level should be found.");
    }

    @Test
    public void testBackupArticles() {
        // Simulate backup
        boolean backupSuccess = articleManager.backupArticles(new java.io.File("backup.txt"));
        assertTrue(backupSuccess, "Backup should complete successfully.");
    }

    @Test
    public void testRestoreArticles() {
        // Simulate backup first
        articleManager.backupArticles(new java.io.File("backup.txt"));

        // Simulate restoration
        boolean restoreSuccess = articleManager.restoreArticles(new java.io.File("backup.txt"));
        assertTrue(restoreSuccess, "Articles should be restored successfully.");
    }

    @Test
    public void testCreateArticle() throws Exception {
        HelpArticle newArticle = new HelpArticle(101L, "Test Title", "Test Description",
                "Beginner", "Test Body", "Test Keywords", "Test Group",
                java.time.LocalDateTime.now(), java.time.LocalDateTime.now(), "Instructor");

        articleManager.addArticle(newArticle);
        assertTrue(articleManager.getArticles().contains(newArticle), "New article should be added.");
    }

    @Test
    public void testEditArticle() {
        HelpArticle article = articleManager.getArticles().get(0);
        article.setTitle("Updated Title");

        assertEquals("Updated Title", article.getTitle(), "Article title should be updated.");
    }

    @Test
    public void testDeleteArticle() {
        HelpArticle article = articleManager.getArticles().get(0);
        articleManager.deleteArticle(article);

        assertFalse(articleManager.getArticles().contains(article), "Article should be deleted.");
    }
}
