package view;

import controller.UserController;
import model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import org.w3c.dom.Text;

import java.awt.*;

/**
 * Login view for user authentication with modern Notion-like UI
 */
public class LoginView extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private UserController userController;
    
    // UI Colors for Notion-like theme
    private final Color BACKGROUND_COLOR = Color.WHITE;
    private final Color TEXT_COLOR = new Color(25, 25, 25);
    private final Color ACCENT_COLOR = new Color(55, 53, 47);
    private final Color BORDER_COLOR = new Color(235, 235, 235);
    private final Color HOVER_COLOR = new Color(245, 245, 245);
    private final Color BUTTON_COLOR = new Color(55, 53, 47);
    private final Color BUTTON_TEXT_COLOR = Color.WHITE;
    
    public LoginView() {
        userController = new UserController();
        
        // Set UI look and feel for a more modern appearance
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
            // Set global UI properties
            UIManager.put("Panel.background", BACKGROUND_COLOR);
            UIManager.put("TextField.background", BACKGROUND_COLOR);
            UIManager.put("PasswordField.background", BACKGROUND_COLOR);
            UIManager.put("Label.foreground", TEXT_COLOR);
            UIManager.put("Button.background", BUTTON_COLOR);
            UIManager.put("Button.foreground", BUTTON_TEXT_COLOR);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        setTitle("Lost and Found - Login");
        setSize(450, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Create main panel with modern styling
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(30, 40, 30, 40));
        
        // Create header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BACKGROUND_COLOR);
        headerPanel.setBorder(new MatteBorder(0, 0, 1, 0, BORDER_COLOR));
        
        JLabel titleLabel = new JLabel("Lost and Found", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_COLOR);
        
        JLabel subtitleLabel = new JLabel("Sign in to your account", JLabel.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(100, 100, 100));
        subtitleLabel.setBorder(new EmptyBorder(5, 0, 15, 0));
        
        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(subtitleLabel, BorderLayout.CENTER);
        
        // Create form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(BACKGROUND_COLOR);
        formPanel.setBorder(new EmptyBorder(20, 0, 10, 0));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 5, 8, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        // Username label and field
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        usernameLabel.setForeground(TEXT_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        formPanel.add(usernameLabel, gbc);
        
        usernameField = new JTextField();
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(usernameField, gbc);
        
        // Password label and field
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        passwordLabel.setForeground(TEXT_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(passwordLabel, gbc);
        
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(passwordField, gbc);
        
        // Login button
        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.setContentAreaFilled(false);
        loginButton.setOpaque(true);
        loginButton.setBackground(BUTTON_COLOR);
        loginButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        loginButton.setPreferredSize(new Dimension(100, 40));
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.insets = new Insets(20, 5, 5, 5);
        
        loginButton.setForeground(BUTTON_TEXT_COLOR);
        loginButton.setBackground(TEXT_COLOR);
        loginButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(TEXT_COLOR),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        formPanel.add(loginButton, gbc);
        
        // Register text and button
        JPanel registerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        registerPanel.setBackground(BACKGROUND_COLOR);
        registerPanel.setSize(450, 700);
        
        JLabel registerText = new JLabel("Don't have an account?");
        registerText.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        registerText.setForeground(new Color(100, 100, 100));
        
        registerButton = new JButton("Register");
        registerButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        registerButton.setForeground(ACCENT_COLOR);
        registerButton.setBorderPainted(false);
        registerButton.setContentAreaFilled(false);
        registerButton.setFocusPainted(false);
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        registerPanel.add(registerText);
        registerPanel.add(registerButton);
        
        // Add components to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(registerPanel, BorderLayout.SOUTH);
        
        // Add action listeners
        loginButton.addActionListener(e -> login());
        registerButton.addActionListener(e -> showRegisterDialog());
        
        // Set enter key to trigger login
        getRootPane().setDefaultButton(loginButton);
        
        // Add panel to frame
        setContentPane(mainPanel);
        setVisible(true);
    }
    
    /**
     * Create a styled JButton with Notion-like appearance
     */
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        button.setBackground(BUTTON_COLOR);
        button.setForeground(BUTTON_TEXT_COLOR);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
    
    private void login() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            showErrorMessage("Please enter username and password", "Login Error");
            return;
        }
        
        User user = userController.authenticateUser(username, password);
        if (user != null) {
            // Login successful, open main view
            dispose();
            if (user.isAdmin()) {
                new AdminView(user);
            } else {
                new MainView(user);
            }
        } else {
            showErrorMessage("Invalid username or password", "Login Error");
        }
    }
    
    /**
     * Show styled error message
     */
    private void showErrorMessage(String message, String title) {
        UIManager.put("OptionPane.background", BACKGROUND_COLOR);
        UIManager.put("OptionPane.messageForeground", TEXT_COLOR);
        UIManager.put("Panel.background", BACKGROUND_COLOR);
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }
    
    private void showRegisterDialog() {
        JDialog registerDialog = new JDialog(this, "Register New User", true);
        registerDialog.setSize(450, 700);
        registerDialog.setLocationRelativeTo(this);
        
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(25, 30, 25, 30));
        
        // Create header
        JLabel headerLabel = new JLabel("Create an Account", JLabel.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        headerLabel.setForeground(TEXT_COLOR);
        headerLabel.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        // Create form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(BACKGROUND_COLOR);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        // Username
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        usernameLabel.setForeground(TEXT_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(usernameLabel, gbc);
        
        JTextField usernameField = new JTextField();
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(usernameField, gbc);
        
        // Password
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        passwordLabel.setForeground(TEXT_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(passwordLabel, gbc);
        
        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(passwordField, gbc);
        
        // Email
        JLabel emailLabel = new JLabel("Email");
        emailLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        emailLabel.setForeground(TEXT_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(emailLabel, gbc);
        
        JTextField emailField = new JTextField();
        emailField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        emailField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(emailField, gbc);
        
        // Phone
        JLabel phoneLabel = new JLabel("Phone");
        phoneLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        phoneLabel.setForeground(TEXT_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 6;
        formPanel.add(phoneLabel, gbc);
        
        JTextField phoneField = new JTextField();
        phoneField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        phoneField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        gbc.gridx = 0;
        gbc.gridy = 7;
        formPanel.add(phoneField, gbc);
        
        // Register button
        JButton registerButton = new JButton("Register");
        registerButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerButton.setFocusPainted(false);
        registerButton.setBorderPainted(false);
        registerButton.setContentAreaFilled(false);
        registerButton.setOpaque(true);
        registerButton.setBackground(BUTTON_COLOR);
        registerButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        registerButton.setPreferredSize(new Dimension(100, 40));
        registerButton.setForeground(BUTTON_TEXT_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.insets = new Insets(20, 5, 5, 5);
        formPanel.add(registerButton, gbc);
        
        // Add components to main panel
        mainPanel.add(headerLabel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        registerButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String email = emailField.getText();
            String phone = phoneField.getText();
            
            if (username.isEmpty() || password.isEmpty()) {
                UIManager.put("OptionPane.background", BACKGROUND_COLOR);
                UIManager.put("OptionPane.messageForeground", TEXT_COLOR);
                UIManager.put("Panel.background", BACKGROUND_COLOR);
                JOptionPane.showMessageDialog(registerDialog, 
                    "Username and password are required", 
                    "Registration Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            User newUser = new User(username, password);
            newUser.setEmail(email);
            newUser.setPhone(phone);
            
            if (userController.registerUser(newUser)) {
                UIManager.put("OptionPane.background", BACKGROUND_COLOR);
                UIManager.put("OptionPane.messageForeground", TEXT_COLOR);
                UIManager.put("Panel.background", BACKGROUND_COLOR);
                JOptionPane.showMessageDialog(registerDialog, 
                    "Registration successful, please login", 
                    "Registration Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                registerDialog.dispose();
            } else {
                UIManager.put("OptionPane.background", BACKGROUND_COLOR);
                UIManager.put("OptionPane.messageForeground", TEXT_COLOR);
                UIManager.put("Panel.background", BACKGROUND_COLOR);
                JOptionPane.showMessageDialog(registerDialog, 
                    "Registration failed. Username may be taken.", 
                    "Registration Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        registerDialog.setContentPane(mainPanel);
        registerDialog.setVisible(true);
    }
}