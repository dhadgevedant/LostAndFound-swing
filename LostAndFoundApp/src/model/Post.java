package model;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Post model class representing a lost/found item
 */
public class Post {
    private int postId;
    private int userId;
    private String title;
    private String description;
    private String imagePath;
    private String location;
    private Date dateFound;
    private boolean isApproved;
    private boolean isClaimed;
    private Timestamp postDate;
    private String username; // For display purposes
    
    // Constructor
    public Post(int userId, String title, String description, String location, 
                Date dateFound, String imagePath) {
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.dateFound = dateFound;
        this.imagePath = imagePath;
        this.isApproved = false;
        this.isClaimed = false;
    }
    
    // Full constructor
    public Post(int postId, int userId, String title, String description, 
                String location, Date dateFound, String imagePath, 
                boolean isApproved, boolean isClaimed) {
        this.postId = postId;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.dateFound = dateFound;
        this.imagePath = imagePath;
        this.isApproved = isApproved;
        this.isClaimed = isClaimed;
    }
    
    // Getters and setters
    public int getPostId() { return postId; }
    public void setPostId(int postId) { this.postId = postId; }
    
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    public Date getDateFound() { return dateFound; }
    public void setDateFound(Date dateFound) { this.dateFound = dateFound; }
    
    public boolean isApproved() { return isApproved; }
    public void setApproved(boolean isApproved) { this.isApproved = isApproved; }
    
    public boolean isClaimed() { return isClaimed; }
    public void setClaimed(boolean isClaimed) { this.isClaimed = isClaimed; }
    
    public Timestamp getPostDate() { return postDate; }
    public void setPostDate(Timestamp postDate) { this.postDate = postDate; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    @Override
    public String toString() {
        return title + (isApproved ? "" : " (Pending)") + (isClaimed ? " (Claimed)" : "");
    }
}