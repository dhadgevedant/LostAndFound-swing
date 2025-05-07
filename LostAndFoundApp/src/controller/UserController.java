package controller;

import dao.UserDAO;
import model.User;

/**
 * Controller for User-related operations
 */
public class UserController {
    private UserDAO userDAO;
    
    public UserController() {
        userDAO = new UserDAO();
    }
    
    /**
     * Authenticate a user
     */
    public User authenticateUser(String username, String password) {
        return userDAO.authenticateUser(username, password);
    }
    
    /**
     * Register a new user
     */
    public boolean registerUser(User user) {
        return userDAO.registerUser(user);
    }
    
    /**
     * Get user by ID
     */
    public User getUserById(int userId) {
        return userDAO.getUserById(userId);
    }
    
    /**
     * Get username for a user ID
     */
    public String getUsernameById(int userId) {
        return userDAO.getUsernameById(userId);
    }
}