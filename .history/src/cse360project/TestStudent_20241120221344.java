package cse360mainproject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestStudent {

    private WindowsManager windowsManager;
    private HelpArticleManager articleManager;

    @BeforeEach
    public void setUp() {
        windowsManager = new WindowsManager();
        articleManager = new HelpArticleManager();
        articleManager.loadArticles(); // Load articles into the manager
    }

    @Test
    public void testSearchArticlesByTitle() {
        String query = "Sample Title";

        List<HelpArticle> results = articleManager.getArticles().stream()
                .filter(article -> article.getTitle().contains(query))
                .toList();

        assertFalse(results.isEmpty(), "Articles matching the title should be found.");
    }

    @Test
    public void testSearchArticlesByGroup() {
        String group = "All Groups";
        List<HelpArticle> results = articleManager.getArticles().stream()
                .filter(article -> article.getGroups().contains(group))
                .toList();

        assertFalse(results.isEmpty(), "Articles matching the group should be found.");
    }

    @Test
    public void testSendGeneralFeedback() {
        java.io.File feedbackFile = new java.io.File("feedback.txt");
        String feedback = "General feedback message";

        boolean feedbackSent = storeFeedback(feedback, "generic", feedbackFile);
        assertTrue(feedbackSent, "Feedback should be stored successfully.");
    }

    @Test
    public void testSendSpecificFeedback() {
        java.io.File feedbackFile = new java.io.File("feedback.txt");
        String feedback = "Specific feedback message";

        boolean feedbackSent = storeFeedback(feedback, "specific", feedbackFile);
        assertTrue(feedbackSent, "Feedback should be stored successfully.");
    }

    private boolean storeFeedback(String feedback, String feedbackType, java.io.File feedbackFile) {
        try (java.io.BufferedWriter writer = new java.io.BufferedWriter(new java.io.FileWriter(feedbackFile, true))) {
            String timestamp = java.time.LocalDateTime.now()
                    .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            writer.write("[" + timestamp + "] " + feedbackType.toUpperCase() + ":\n" + feedback + "\n\n");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
