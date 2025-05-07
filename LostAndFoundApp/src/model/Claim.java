package model;

import java.sql.Timestamp;

/**
 * Claim model class for item claims
 */
public class Claim {
    private int claimId;
    private int postId;
    private int userId;
    private String claimReason;
    private String contactInfo;
    private boolean isApproved;
    private Timestamp claimDate;
    private String username; // For display purposes
    private String status;  // Added status field
    
    // Constructor for creating new claims
    public Claim(int postId, int userId, String claimReason, String contactInfo) {
        this.postId = postId;
        this.userId = userId;
        this.claimReason = claimReason;
        this.contactInfo = contactInfo;
        this.isApproved = false;
    }
    
    // Full constructor
    public Claim(int claimId, int postId, int userId, String claimReason, 
                String contactInfo, boolean isApproved, Timestamp claimDate) {
        this.claimId = claimId;
        this.postId = postId;
        this.userId = userId;
        this.claimReason = claimReason;
        this.contactInfo = contactInfo;
        this.isApproved = isApproved;
        this.claimDate = claimDate;
    }
    
    // Constructor with status and date
    public Claim(int claimId, int postId, int userId, String claimReason, 
                String contactInfo, Timestamp claimDate, String status) {
        this.claimId = claimId;
        this.postId = postId;
        this.userId = userId;
        this.claimReason = claimReason;
        this.contactInfo = contactInfo;
        this.claimDate = claimDate;
        this.status = status;
        this.isApproved = "APPROVED".equals(status);
    }
    
    // Getters and setters
    public int getClaimId() { return claimId; }
    public void setClaimId(int claimId) { this.claimId = claimId; }
    
    public int getPostId() { return postId; }
    public void setPostId(int postId) { this.postId = postId; }
    
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public String getClaimReason() { return claimReason; }
    public void setClaimReason(String claimReason) { this.claimReason = claimReason; }
    
    public String getContactInfo() { return contactInfo; }
    public void setContactInfo(String contactInfo) { this.contactInfo = contactInfo; }
    
    public boolean isApproved() { return isApproved; }
    public void setApproved(boolean isApproved) { this.isApproved = isApproved; }
    
    public Timestamp getClaimDate() { return claimDate; }
    public void setClaimDate(Timestamp claimDate) { this.claimDate = claimDate; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { 
        this.status = status;
        this.isApproved = "APPROVED".equals(status);
    }
    
    @Override
    public String toString() {
        return "Claim by " + username + (isApproved ? " (Approved)" : "");
    }
}