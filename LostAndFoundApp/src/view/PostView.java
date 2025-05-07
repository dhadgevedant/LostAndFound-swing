package view;

import controller.PostController;
import model.Post;
import model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;

/**
 * View for creating new posts with modern Notion-like UI
 */
public class PostView extends JDialog {
    private User currentUser;
    private MainView parentView;
    private PostController postController;
    
    private JTextField titleField;
    private JTextArea descriptionArea;
    private JTextField locationField;
    private JSpinner dateSpinner;
    private JTextField imagePathField;
    private JButton browseButton;
    private JButton submitButton;
    private JButton cancelButton;
    
    // UI Colors for Notion-like theme
    private final Color BACKGROUND_COLOR = Color.WHITE;
    private final Color TEXT_COLOR = new Color(25, 25, 25);
    private final Color ACCENT_COLOR = new Color(55, 53, 47);
    private final Color BORDER_COLOR = new Color(235, 235, 235);
    private final Color FIELD_BACKGROUND = new Color(251, 251, 250);
    
    public PostView(User user, MainView parent) {
        super(parent, "Report Found Item", true);
        this.currentUser = user;
        this.parentView = parent;
        this.postController = new PostController();
        
        setSize(500, 580);
        setLocationRelativeTo(parent);
        
        // Set UI look and feel for a more modern appearance
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
            // Set global UI properties
            UIManager.put("Panel.background", BACKGROUND_COLOR);
            UIManager.put("TextArea.background", FIELD_BACKGROUND);
            UIManager.put("TextField.background", FIELD_BACKGROUND);
            UIManager.put("Label.foreground", TEXT_COLOR);
            UIManager.put("Button.background", ACCENT_COLOR);
            UIManager.put("Button.foreground", Color.WHITE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Create main panel with modern styling
        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(24, 24, 24, 24));
        
        // Create title panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(BACKGROUND_COLOR);
        titlePanel.setBorder(new MatteBorder(0, 0, 1, 0, BORDER_COLOR));
        
        JLabel headingLabel = new JLabel("Report a Found Item");
        headingLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        headingLabel.setForeground(TEXT_COLOR);
        headingLabel.setBorder(new EmptyBorder(0, 0, 16, 0));
        titlePanel.add(headingLabel, BorderLayout.CENTER);
        
        // Create form panel
        JPanel formPanel = createFormPanel();
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        
        cancelButton = createStyledButton("Cancel");
        cancelButton.setBackground(new Color(240, 240, 240));
        cancelButton.setForeground(TEXT_COLOR);
        
        submitButton = createStyledButton("Submit");
        
        buttonPanel.add(cancelButton);
        buttonPanel.add(submitButton);
        
        // Add panels to main panel
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add to dialog
        setContentPane(mainPanel);
        
        // Add action listeners
        browseButton.addActionListener(e -> browseForImage());
        submitButton.addActionListener(e -> submitPost());
        cancelButton.addActionListener(e -> dispose());
        
        setVisible(true);
    }
    
    /**
     * Create the form panel with modern styling
     */
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(BACKGROUND_COLOR);
        formPanel.setBorder(new EmptyBorder(8, 0, 8, 0));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 4, 8, 4);
        gbc.weightx = 1.0;
        
        // Title
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel titleLabel = createStyledLabel("Title");
        formPanel.add(titleLabel, gbc);
        
        gbc.gridy = 1;
        titleField = createStyledTextField(20);
        formPanel.add(titleField, gbc);
        
        // Description
        gbc.gridy = 2;
        JLabel descLabel = createStyledLabel("Description");
        formPanel.add(descLabel, gbc);
        
        gbc.gridy = 3;
        descriptionArea = createStyledTextArea(5, 20);
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        formPanel.add(scrollPane, gbc);
        
        // Location
        gbc.gridy = 4;
        JLabel locationLabel = createStyledLabel("Location");
        formPanel.add(locationLabel, gbc);
        
        gbc.gridy = 5;
        locationField = createStyledTextField(20);
        formPanel.add(locationField, gbc);
        
        // Date Found
        gbc.gridy = 6;
        JLabel dateLabel = createStyledLabel("Date Found");
        formPanel.add(dateLabel, gbc);
        
        gbc.gridy = 7;
        SpinnerDateModel dateModel = new SpinnerDateModel();
        dateSpinner = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        dateSpinner.setEditor(dateEditor);
        dateSpinner.setValue(new java.util.Date()); // Set current date as default
        dateSpinner.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        dateSpinner.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(dateSpinner, gbc);
        
        // Image Path
        gbc.gridy = 8;
        JLabel imageLabel = createStyledLabel("Image");
        formPanel.add(imageLabel, gbc);
        
        gbc.gridy = 9;
        JPanel imagePanel = new JPanel(new BorderLayout(10, 0));
        imagePanel.setBackground(BACKGROUND_COLOR);
        
        imagePathField = createStyledTextField(15);
        browseButton = createStyledButton("Browse");
        
        imagePanel.add(imagePathField, BorderLayout.CENTER);
        imagePanel.add(browseButton, BorderLayout.EAST);
        formPanel.add(imagePanel, gbc);
        
        return formPanel;
    }
    
    /**
     * Create a styled label
     */
    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        label.setForeground(TEXT_COLOR);
        return label;
    }
    
    /**
     * Create a styled text field
     */
    private JTextField createStyledTextField(int columns) {
        JTextField field = new JTextField(columns);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBackground(FIELD_BACKGROUND);
        field.setForeground(TEXT_COLOR);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        return field;
    }
    
    /**
     * Create a styled text area
     */
    private JTextArea createStyledTextArea(int rows, int columns) {
        JTextArea area = new JTextArea(rows, columns);
        area.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        area.setBackground(FIELD_BACKGROUND);
        area.setForeground(TEXT_COLOR);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        return area;
    }
    
    /**
     * Create a styled button
     */
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        button.setBackground(ACCENT_COLOR);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
    
    /**
     * Open file chooser to select an image
     */
    private void browseForImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
            "Image files", "jpg", "jpeg", "png", "gif"));
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            imagePathField.setText(selectedFile.getAbsolutePath());
        }
    }
    
    /**
     * Submit the post to the database
     */
    private void submitPost() {
        String title = titleField.getText().trim();
        String description = descriptionArea.getText().trim();
        String  imagePath = locationField.getText().trim();
        String  location = imagePathField.getText().trim();
        
        // Validate input
        if (title.isEmpty() || description.isEmpty() || location.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please fill in all required fields (Title, Description, Location)", 
                "Missing Information", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Convert java.util.Date from spinner to java.sql.Date
        java.util.Date utilDate = (java.util.Date) dateSpinner.getValue();
        LocalDate localDate = utilDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Date sqlDate = Date.valueOf(localDate);
        
        // Create post object
        Post post = new Post(
            currentUser.getUserId(),
            title,
            description,
            imagePath,
            sqlDate,
            location
        );
        
        // Save post to database
        if (postController.createPost(post)) {
            JOptionPane.showMessageDialog(this, 
                "Your post has been submitted and is pending approval", 
                "Post Submitted", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Failed to submit post. Please try again.", 
                "Submission Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}