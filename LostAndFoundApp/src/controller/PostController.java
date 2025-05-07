package controller;

import dao.PostDAO;
import model.Post;
import java.util.List;

/**
 * Controller for Post-related operations
 */
public class PostController {
    private PostDAO postDAO;
    
    public PostController() {
        postDAO = new PostDAO();
    }
    
    /**
     * Create a new post
     */
    public boolean createPost(Post post) {
        return postDAO.createPost(post);
    }
    
    /**
     * Get all approved posts
     */
    public List<Post> getApprovedPosts() {
        return postDAO.getApprovedPosts();
    }
    
    /**
     * Get pending posts for admin approval
     */
    public List<Post> getPendingPosts() {
        return postDAO.getPendingPosts();
    }
    
    /**
     * Get posts by user ID
     */
    public List<Post> getPostsByUserId(int userId) {
        return postDAO.getPostsByUserId(userId);
    }
    
    /**
     * Update post approval status
     */
    public boolean approvePost(int postId, boolean isApproved) {
        return postDAO.updatePostApproval(postId, isApproved);
    }
    
    /**
     * Get post by ID
     */
    public Post getPostById(int postId) {
        return postDAO.getPostById(postId);
    }
}