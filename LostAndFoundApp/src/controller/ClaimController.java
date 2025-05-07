package controller;

import model.Claim;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import dao.DBConnection;

/**
 * Controller for claim operations
 */
public class ClaimController {
    private Connection conn;
    
    public ClaimController() {
        // Initialize database connection
        conn = DBConnection.getConnection();
    }
    
    /**
     * Create a new claim in the database
     */
    public boolean createClaim(Claim claim) {
        PreparedStatement stmt = null;
        
        try {
            // Updated query to match database schema
            String query = "INSERT INTO claims (post_id, user_id, claim_reason, contact_info, claim_date, is_approved) " +
                          "VALUES (?, ?, ?, ?, NOW(), FALSE)";
            
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, claim.getPostId());
            stmt.setInt(2, claim.getUserId());
            stmt.setString(3, claim.getClaimReason());
            stmt.setString(4, claim.getContactInfo());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Get all claims for a specific user
     */
    public List<Claim> getClaimsByUserId(int userId) {
        List<Claim> claims = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            // Updated query to match database schema
            String query = "SELECT c.*, p.title FROM claims c " +
                           "JOIN posts p ON c.post_id = p.post_id " +
                           "WHERE c.user_id = ? " +
                           "ORDER BY c.claim_date DESC";
            
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, userId);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                Claim claim = new Claim(
                    rs.getInt("claim_id"),
                    rs.getInt("post_id"),
                    rs.getInt("user_id"),
                    rs.getString("claim_reason"),
                    rs.getString("contact_info"),
                    rs.getBoolean("is_approved"),
                    rs.getTimestamp("claim_date")
                );
                claims.add(claim);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return claims;
    }
    
    /**
     * Get all claims for a specific post
     */
    public List<Claim> getClaimsByPostId(int postId) {
        List<Claim> claims = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            // Updated query to match database schema
            String query = "SELECT c.*, u.username FROM claims c " +
                           "JOIN users u ON c.user_id = u.user_id " +
                           "WHERE c.post_id = ? " +
                           "ORDER BY c.claim_date DESC";
            
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, postId);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                Claim claim = new Claim(
                    rs.getInt("claim_id"),
                    rs.getInt("post_id"),
                    rs.getInt("user_id"),
                    rs.getString("claim_reason"),
                    rs.getString("contact_info"),
                    rs.getBoolean("is_approved"),
                    rs.getTimestamp("claim_date")
                );
                claim.setUsername(rs.getString("username"));
                claims.add(claim);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return claims;
    }
    
    /**
     * Get a specific claim by ID
     */
    public Claim getClaimById(int claimId) {
        Claim claim = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            // Updated query to match database schema
            String query = "SELECT c.*, u.username FROM claims c " +
                           "JOIN users u ON c.user_id = u.user_id " +
                           "WHERE c.claim_id = ?";
            
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, claimId);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                claim = new Claim(
                    rs.getInt("claim_id"),
                    rs.getInt("post_id"),
                    rs.getInt("user_id"),
                    rs.getString("claim_reason"),
                    rs.getString("contact_info"),
                    rs.getBoolean("is_approved"),
                    rs.getTimestamp("claim_date")
                );
                claim.setUsername(rs.getString("username"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return claim;
    }
}