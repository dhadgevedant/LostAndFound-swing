package dao;

import model.Claim;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Claim entity
 */
public class ClaimDAO {
    private Connection connection;
    private UserDAO userDAO;
    private PostDAO postDAO;
    
    public ClaimDAO() {
        connection = DBConnection.getConnection();
        userDAO = new UserDAO();
        postDAO = new PostDAO();
    }
    
    /**
     * Create a new claim
     */
    public boolean createClaim(Claim claim) {
        String query = "INSERT INTO claims (post_id, user_id, claim_reason, contact_info) " +
                       "VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, claim.getPostId());
            ps.setInt(2, claim.getUserId());
            ps.setString(3, claim.getClaimReason());
            ps.setString(4, claim.getContactInfo());
            
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    claim.setClaimId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Get claims for a post
     */
    public List<Claim> getClaimsByPostId(int postId) {
        String query = "SELECT * FROM claims WHERE post_id = ? ORDER BY claim_date DESC";
        List<Claim> claims = new ArrayList<>();
        
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, postId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Claim claim = mapResultSetToClaim(rs);
                claim.setUsername(userDAO.getUsernameById(claim.getUserId()));
                claims.add(claim);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return claims;
    }
    
    /**
     * Update claim approval status
     */
    public boolean updateClaimApproval(int claimId, boolean isApproved) {
        String query = "UPDATE claims SET is_approved = ? WHERE claim_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setBoolean(1, isApproved);
            ps.setInt(2, claimId);
            
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0 && isApproved) {
                // If a claim is approved, get the post ID and update the post status
                Claim claim = getClaimById(claimId);
                if (claim != null) {
                    postDAO.updatePostClaimedStatus(claim.getPostId(), true);
                }
            }
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Remove a claim (reject)
     */
    public boolean removeClaim(int claimId) {
        String query = "DELETE FROM claims WHERE claim_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, claimId);
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Get claim by ID
     */
    public Claim getClaimById(int claimId) {
        String query = "SELECT * FROM claims WHERE claim_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, claimId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                Claim claim = mapResultSetToClaim(rs);
                claim.setUsername(userDAO.getUsernameById(claim.getUserId()));
                return claim;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // Helper method to map ResultSet to Claim object
    private Claim mapResultSetToClaim(ResultSet rs) throws SQLException {
        return new Claim(
            rs.getInt("claim_id"),
            rs.getInt("post_id"),
            rs.getInt("user_id"),
            rs.getString("claim_reason"),
            rs.getString("contact_info"),
            rs.getBoolean("is_approved"),
            rs.getTimestamp("claim_date")
        );
    }
}