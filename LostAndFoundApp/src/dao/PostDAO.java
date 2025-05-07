package dao;

import model.Post;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Post entity
 */
public class PostDAO {
    private Connection connection;
    private UserDAO userDAO;

    public PostDAO() {
        connection = DBConnection.getConnection();
        userDAO = new UserDAO();
    }

    /**
     * Create a new post
     */
    public boolean createPost(Post post) {
        String query = "INSERT INTO posts (user_id, title, description, image_path, location, date_found) " +
                       "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, post.getUserId());
            ps.setString(2, post.getTitle());
            ps.setString(3, post.getDescription());
            ps.setString(4, post.getImagePath());
            ps.setString(5, post.getLocation());
            ps.setDate(6, post.getDateFound());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    post.setPostId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Get all posts that are approved
     */
    public List<Post> getApprovedPosts() {
        return getPosts("SELECT * FROM posts WHERE is_approved = true ORDER BY post_date DESC");
    }

    /**
     * Get all pending posts (for admin)
     */
    public List<Post> getPendingPosts() {
        return getPosts("SELECT * FROM posts WHERE is_approved = false ORDER BY post_date DESC");
    }

    /**
     * Get posts by user ID
     */
    public List<Post> getPostsByUserId(int userId) {
        String query = "SELECT * FROM posts WHERE user_id = ? ORDER BY post_date DESC";
        List<Post> posts = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Post post = mapResultSetToPost(rs);
                post.setUsername(userDAO.getUsernameById(post.getUserId()));
                posts.add(post);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return posts;
    }

    /**
     * Update post approval status
     */
    public boolean updatePostApproval(int postId, boolean isApproved) {
        String query = "UPDATE posts SET is_approved = ? WHERE post_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setBoolean(1, isApproved);
            ps.setInt(2, postId);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Update post claimed status
     */
    public boolean updatePostClaimedStatus(int postId, boolean isClaimed) {
        String query = "UPDATE posts SET is_claimed = ? WHERE post_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setBoolean(1, isClaimed);
            ps.setInt(2, postId);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Get post by ID
     */
    public Post getPostById(int postId) {
        String query = "SELECT * FROM posts WHERE post_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, postId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Post post = mapResultSetToPost(rs);
                post.setUsername(userDAO.getUsernameById(post.getUserId()));
                return post;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Helper method to run SELECT queries returning a list of posts
     */
    private List<Post> getPosts(String query) {
        List<Post> posts = new ArrayList<>();

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Post post = mapResultSetToPost(rs);
                post.setUsername(userDAO.getUsernameById(post.getUserId()));
                posts.add(post);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return posts;
    }

    /**
     * Helper method to map ResultSet to Post object
     */
    private Post mapResultSetToPost(ResultSet rs) throws SQLException {
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
        post.setPostDate(rs.getTimestamp("post_date"));
        return post;
    }
}
