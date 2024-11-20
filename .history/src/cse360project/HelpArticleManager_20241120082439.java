package cse360project;

import java.io.*;
import java.util.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class HelpArticleManager {

    private static List<HelpArticle> articles;
    private final String filePath = "articles.txt";
    static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public HelpArticleManager() {
        this.articles = new ArrayList<>();
    }

    // Load articles from the file
    public void loadArticles() {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                HelpArticle article = parseArticle(line);
                if (article != null) {
                    articles.add(article);
                } else {
                    System.out.println("Line format incorrect: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Save all articles to the file
    public void saveArticles() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (HelpArticle article : articles) {
                writer.write(article.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Parse a line from the file into a HelpArticle object
    private HelpArticle parseArticle(String line) {
    	 String[] fields = line.split("\t");
    	    
    	    // Ensure the line has at least 8 fields, allowing for optional fields where necessary
    	    if (fields.length < 8) {
    	        System.out.println("Line format incorrect: " + line);
    	        return null; // Invalid data format
    	    }

    	    try {
    	        long uniqueId = Long.parseLong(fields[0].trim());
    	        String title = fields[1].trim();
    	        String description = fields[2].trim();
    	        String difficultyLevel = fields[3].trim();
    	        String bodyContent = fields[4].trim();
    	        String keywords = fields[5].trim();
    	        String groups = fields[6].trim();

    	        // Parse dates and handle cases where created or modified date may be missing
    	        LocalDateTime createdDate = fields.length > 7 ? parseDateTime(fields[7].trim()) : LocalDateTime.now();
    	        LocalDateTime modifiedDate = fields.length > 8 ? parseDateTime(fields[8].trim()) : createdDate;

    	        String author = fields.length > 9 ? fields[9].trim() : "Unknown";

    	        return new HelpArticle(uniqueId, title, description, difficultyLevel, 
    	                               AESUtil.decrypt(bodyContent), keywords, groups, 
    	                               createdDate, modifiedDate, author);
    	    } catch (Exception e) {
    	        e.printStackTrace();
    	        return null;
    	    }
    }

    // Helper method to parse date, time, or date-time formats
    private LocalDateTime parseDateTime(String dateTimeStr) {
        try {
            // Try full date and time first
            return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        } catch (DateTimeParseException e1) {
            try {
                // Fallback to date-only format
                LocalDate date = LocalDate.parse(dateTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                return date.atStartOfDay(); // Use start of day as time
            } catch (DateTimeParseException e2) {
                try {
                    // Fallback to time-only format, use today's date
                    LocalTime time = LocalTime.parse(dateTimeStr, DateTimeFormatter.ofPattern("HH:mm:ss"));
                    return LocalDate.now().atTime(time); // Combine with today's date
                } catch (DateTimeParseException e3) {
                    System.out.println("Unrecognized date format: " + dateTimeStr);
                    return null;
                }
            }
        }
    }

    // Backup articles to another file
    public boolean backupArticles(File backupFile) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(backupFile))) {
            for (HelpArticle article : articles) {
                writer.write(article.toFileString());
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
 // Backup articles by group
    public boolean backupArticlesByGroup(String groupName, File backupFile) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(backupFile))) {
            for (HelpArticle article : articles) {
                if (article.getGroups().contains(groupName)) {
                    writer.write(article.toFileString());
                    writer.newLine();
                }
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Merge articles from a backup file without duplicates
    public boolean mergeBackup(File backupFile) {
        Map<Long, HelpArticle> existingArticles = new HashMap<>();
        for (HelpArticle article : articles) {
            existingArticles.put(article.getUniqueId(), article);
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(backupFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                HelpArticle article = parseArticle(line);
                if (article != null && !existingArticles.containsKey(article.getUniqueId())) {
                    articles.add(article);
                    existingArticles.put(article.getUniqueId(), article);
                }
            }
            saveArticles(); // Save the updated list after merging
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    // Restore articles from a backup file
    public boolean restoreArticles(File backupFile) {
        articles.clear();  // Clear existing articles before restoring
        try (BufferedReader reader = new BufferedReader(new FileReader(backupFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                HelpArticle article = parseArticle(line);
                if (article != null) {
                    articles.add(article);
                }
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Add a new article to the list
    public void addArticle(HelpArticle article) {
        articles.add(article);
        saveArticles();  // Immediately save after adding
    }

    // Delete an article from the list
    public void deleteArticle(HelpArticle article) {
        articles.remove(article);
        saveArticles();  // Immediately save after deletion
    }

    // Get the list of all articles
    public static List<HelpArticle> getArticles() {
        return new ArrayList<>(articles);
    }

    // Generate a unique ID for each article
    public long generateUniqueId() {
        return articles.isEmpty() ? 1 : articles.get(articles.size() - 1).getUniqueId() + 1;
    }
}