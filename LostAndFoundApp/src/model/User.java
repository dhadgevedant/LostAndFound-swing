package model;

import java.sql.Timestamp;

/**
 * User model class
 */
public class User {
    private int userId;
    private String username;
    private String password;
    private boolean isAdmin;
    private String email;
    private String phone;
    private Timestamp registrationDate;
    
    // Constructor with essential fields
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    // Full constructor
    public User(int userId, String username, String password, boolean isAdmin, 
                String email, String phone, Timestamp registrationDate) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
        this.email = email;
        this.phone = phone;
        this.registrationDate = registrationDate;
    }
    
    // Getters and setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public boolean isAdmin() { return isAdmin; }
    public void setAdmin(boolean isAdmin) { this.isAdmin = isAdmin; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public Timestamp getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(Timestamp registrationDate) { this.registrationDate = registrationDate; }
    
    @Override
    public String toString() {
        return username + (isAdmin ? " (Admin)" : "");
    }
}