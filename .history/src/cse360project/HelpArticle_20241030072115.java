package cse360mainproject;

import java.time.LocalDateTime;

public class HelpArticle {
    private long uniqueId;
    private String title;
    private String description;
    private String difficultyLevel;
    private String bodyContent;
    private String keywords;
    private String groups;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private String author;

    public HelpArticle(long uniqueId, String title, String description, String difficultyLevel, String bodyContent, String keywords, String groups, LocalDateTime createdDate, LocalDateTime modifiedDate, String author) {
        this.uniqueId = uniqueId;
        this.title = title;
        this.description = description;
        this.difficultyLevel = difficultyLevel;
        this.bodyContent = bodyContent;
        this.keywords = keywords;
        this.groups = groups;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.author = author;
    }

    // Getters and setters for all fields

    public long getUniqueId() { return uniqueId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getDifficultyLevel() { return difficultyLevel; }
    public String getBodyContent() { return bodyContent; }
    public String getKeywords() { return keywords; }
    public String getGroups() { return groups; }
    public LocalDateTime getCreatedDate() { return createdDate; }
    public LocalDateTime getModifiedDate() { return modifiedDate; }
    public String getAuthor() { return author; }

    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setDifficultyLevel(String difficultyLevel) { this.difficultyLevel = difficultyLevel; }
    public void setBodyContent(String bodyContent) { this.bodyContent = bodyContent; }
    public void setKeywords(String keywords) { this.keywords = keywords; }
    public void setGroups(String groups) { this.groups = groups; }
    public void setModifiedDate(LocalDateTime modifiedDate) { this.modifiedDate = modifiedDate; }

    @Override
    public String toString() {
        return uniqueId + "," + title + "," + description + "," + difficultyLevel + "," + bodyContent + "," + keywords + "," + groups + "," + createdDate + "," + modifiedDate + "," + author;
    }
}