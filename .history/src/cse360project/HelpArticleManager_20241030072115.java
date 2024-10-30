package cse360mainproject;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.Alert;

public class HelpArticleManager {
    private final List<HelpArticle> articles;
    private final String filePath = "articles.txt";

    public HelpArticleManager() {
    	articles = loadArticles();
    }

    public void addArticle(HelpArticle article) {
        articles.add(article);
        saveArticles(articles);
    }

    public void updateArticle(int index, HelpArticle article) {
        articles.set(index, article);
        saveArticles(articles);
    }

    public void deleteArticle(int index) {
        articles.remove(index);
        saveArticles(articles);
    }

    public List<HelpArticle> getArticles() {
        return articles;
    }

    public List<HelpArticle> loadArticles() {
        List<HelpArticle> loadedArticles = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                long uniqueId = Long.parseLong(parts[0]);
                String title = parts[1];
                String description = parts[2];
                String difficultyLevel = parts[3];
                String bodyContent = parts[4];
                String keywords = parts[5];
                String groups = parts[6];
                LocalDateTime createdDate = LocalDateTime.parse(parts[7]);
                LocalDateTime modifiedDate = LocalDateTime.parse(parts[8]);
                String author = parts[9];

                HelpArticle article = new HelpArticle(uniqueId, title, description, difficultyLevel, bodyContent, keywords, groups, createdDate, modifiedDate, author);
                loadedArticles.add(article);
            }
        } catch (IOException e) {
            System.out.println("Error loading articles: " + e.getMessage());
        }
        return loadedArticles;
    }

    public void saveArticles(List<HelpArticle> articles) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (HelpArticle article : articles) {
                writer.write(article.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving articles: " + e.getMessage());
        }
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION); // Create an information-type alert
        alert.setTitle(title); // Set the title of the alert
        alert.setHeaderText(null); // No header text for the alert
        alert.setContentText(message); // Set the content/message of the alert
        alert.showAndWait(); // Show the alert and wait for the user to close it
    }
}