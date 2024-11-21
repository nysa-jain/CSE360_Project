package cse360project;


import java.time.LocalDateTime;

public class HelpArticle {
    private long uniqueId;
    private String title;
    private String description;
    private String difficultyLevel;
    private String encryptedBodyContent;
    private String keywords;
    private String groups;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private String author;

    public HelpArticle(long uniqueId, String title, String description, String difficultyLevel, String bodyContent, String keywords, String groups, LocalDateTime createdDate, LocalDateTime modifiedDate, String author) throws Exception {
        this.uniqueId = uniqueId;
        this.title = title;
        this.description = description;
        this.difficultyLevel = difficultyLevel;
        this.encryptedBodyContent = AESUtil.encrypt(bodyContent);
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
    public String getBodyContent() { return encryptedBodyContent; }
    public String getKeywords() { return keywords; }
    public String getGroups() { return groups; }
    public LocalDateTime getCreatedDate() { return createdDate; }
    public LocalDateTime getModifiedDate() { return modifiedDate; }
    public String getAuthor() { return author; }
    public String toFileString() {
        return uniqueId + "\t" + title + "\t" + description + "\t" + difficultyLevel + "\t" + encryptedBodyContent + "\t" + keywords + "\t" + groups + "\t" + createdDate.format(HelpArticleManager.dateFormatter);
    }
    public String getDecryptedBodyContent(String userRole, UserInfo userInfo) throws Exception {
        if ("Instructor".equals(userRole) || "Admin".equals(userRole)) {
            return decryptContent(encryptedBodyContent);  // Decrypt if role is Instructor or Admin
        } else if ("Student".equals(userRole) && 
                   (userInfo.hasSpecialAccessToGroup(this.groups) || userInfo.hasSpecialAccessToArticle(String.valueOf(this.uniqueId)))) {
            return decryptContent(encryptedBodyContent);  // Decrypt if student has special access
        } else {
            return "Access Denied";  // Otherwise, show access denied
        }
    }


    private boolean hasSpecialAccess() {
        // Logic to determine if the student has special access
        return true;  // Placeholder logic, should be based on your actual access control
    }

    private String decryptContent(String encryptedContent) throws Exception {
        return AESUtil.decrypt(encryptedContent);
    }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setDifficultyLevel(String difficultyLevel) { this.difficultyLevel = difficultyLevel; }
    public void setBodyContent(String bodyContent) { this.encryptedBodyContent = bodyContent; }
    public void setKeywords(String keywords) { this.keywords = keywords; }
    public void setGroups(String groups) { this.groups = groups; }
    public void setModifiedDate(LocalDateTime modifiedDate) { this.modifiedDate = modifiedDate; }
    public void setAuthor(String author) { this.author = author; }

    @Override
    public String toString() {
        return uniqueId + "," + title + "," + description + "," + difficultyLevel + "," + encryptedBodyContent + "," + keywords + "," + groups + "," + createdDate + "," + modifiedDate + "," + author;
    }
}