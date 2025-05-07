package controller;

import model.Claim;
import model.Post;

import java.util.ArrayList;
import java.util.List;

import dao.DBConnection;

import java.sql.*;

/**
 * Controller for admin functionality
 */
public class AdminController {
    private Connection conn;

    public AdminController() {
        conn = DBConnection.getConnection();
    }

    public List<Post> getPendingPosts() {
        List<Post> pendingPosts = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            String query = "SELECT p.*, u.username FROM posts p " +
                           "JOIN users u ON p.user_id = u.user_id " +
                           "WHERE p.is_approved = FALSE " +
                           "ORDER BY p.post_date DESC";

            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Post post = new Post(
                    rs.getInt("post_id"),
                    rs.getInt("user_id"),
                    rs.getString("title"),
                    rs.getString("description"),
                    rs.getString("location"),
                    rs.getDate("date_found"),
                    rs.getString("image_path"),
                    rs.getBoolean("is_approved"),
                    rs.getBoolean("is_claimed")
                );
                post.setUsername(rs.getString("username"));
                pendingPosts.add(post);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); if (stmt != null) stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
        }

        return pendingPosts;
    }

    public List<Post> getApprovedPosts() {
        List<Post> approvedPosts = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            String query = "SELECT p.*, u.username FROM posts p " +
                           "JOIN users u ON p.user_id = u.user_id " +
                           "WHERE p.is_approved = TRUE " +
                           "ORDER BY p.post_date DESC";

            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Post post = new Post(
                    rs.getInt("post_id"),
                    rs.getInt("user_id"),
                    rs.getString("title"),
                    rs.getString("description"),
                    rs.getString("location"),
                    rs.getDate("date_found"),
                    rs.getString("image_path"),
                    rs.getBoolean("is_approved"),
                    rs.getBoolean("is_claimed")
                );
                post.setUsername(rs.getString("username"));
                approvedPosts.add(post);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); if (stmt != null) stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
        }

        return approvedPosts;
    }

    public List<Claim> getClaimsForPost(int postId) {
        List<Claim> claims = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
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
            try { if (rs != null) rs.close(); if (stmt != null) stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
        }

        return claims;
    }

    public boolean approvePost(int postId, boolean approve) {
        PreparedStatement stmt = null;

        try {
            String query = "UPDATE posts SET is_approved = ? WHERE post_id = ?";
            stmt = conn.prepareStatement(query);
            stmt.setBoolean(1, approve);
            stmt.setInt(2, postId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try { if (stmt != null) stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public boolean approveClaim(int claimId) {
        boolean success = false;
        Connection localConn = null;
    
        try {
            localConn = DBConnection.getConnection();
            localConn.setAutoCommit(false);
    
            int postId = -1;
    
            // Step 1: Get post_id from claim
            String getPostQuery = "SELECT post_id FROM claims WHERE claim_id = ?";
            try (PreparedStatement stmt = localConn.prepareStatement(getPostQuery)) {
                stmt.setInt(1, claimId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        postId = rs.getInt("post_id");
                    } else {
                        return false; // Claim ID not found
                    }
                }
            }
    
            // Step 2: Approve this claim
            String approveClaimQuery = "UPDATE claims SET is_approved = TRUE WHERE claim_id = ?";
            try (PreparedStatement stmt = localConn.prepareStatement(approveClaimQuery)) {
                stmt.setInt(1, claimId);
                stmt.executeUpdate();
            }
    
            // Step 3: Set the post as claimed
            String markPostClaimedQuery = "UPDATE posts SET is_claimed = TRUE WHERE post_id = ?";
            try (PreparedStatement stmt = localConn.prepareStatement(markPostClaimedQuery)) {
                stmt.setInt(1, postId);
                stmt.executeUpdate();
            }
    
            // Step 4: Reject all other claims for the same post
            String rejectOtherClaimsQuery = "DELETE FROM claims WHERE post_id = ? AND claim_id != ?";
            
            try (PreparedStatement stmt = localConn.prepareStatement(rejectOtherClaimsQuery)) {
                stmt.setInt(1, postId);
                stmt.setInt(2, claimId);
                stmt.executeUpdate();
            }
    
            localConn.commit();
            localConn.setAutoCommit(true);
            success = true;
            return true;
    
        } catch (SQLException e) {
            if (localConn != null) {
                try {
                    localConn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
    
        } finally {
            try {
                if (localConn != null && !localConn.getAutoCommit()) {
                    localConn.setAutoCommit(true);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    

    public boolean rejectClaim(int claimId) {
        PreparedStatement stmt = null;

        try {

            String query = "delete from claims where claim_id = ?";
            
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, claimId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try { if (stmt != null) stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
}