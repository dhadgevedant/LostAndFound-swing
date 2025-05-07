package view;

import controller.PostController;
import controller.ClaimController;
import model.Post;
import model.User;
import model.Claim;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.io.File;
import java.util.List;

/**
 * Main view showing approved posts and allowing users to create posts and claims
 * with modern Notion-like UI
 */
public class MainView extends JFrame {
    private User currentUser;
    private PostController postController;
    private JList<Post> postsList;
    private DefaultListModel<Post> postsModel;
    private JPanel detailsContentPanel;
    private JScrollPane detailsScrollPane;
    
    // UI Colors for Notion-like theme
    private final Color BACKGROUND_COLOR = Color.WHITE;
    private final Color TEXT_COLOR = new Color(25, 25, 25);
    private final Color ACCENT_COLOR = new Color(55, 53, 47);
    private final Color BORDER_COLOR = new Color(235, 235, 235);
    private final Color HOVER_COLOR = new Color(245, 245, 245);
    private final Color BUTTON_COLOR = new Color(55, 53, 47);
    private final Color FIELD_BACKGROUND = new Color(251, 251, 250);
    private final Color CARD_BACKGROUND = new Color(250, 250, 250);
    
    public MainView(User user) {
        this.currentUser = user;
        this.postController = new PostController();
        
        setTitle("Lost and Found");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Set UI properties
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            UIManager.put("Panel.background", BACKGROUND_COLOR);
            UIManager.put("TextArea.background", FIELD_BACKGROUND);
            UIManager.put("Label.foreground", TEXT_COLOR);
            UIManager.put("Button.background", BUTTON_COLOR);
            UIManager.put("Button.foreground", Color.WHITE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Create main container with border
        JPanel contentPanel = new JPanel(new BorderLayout(0, 0));
        contentPanel.setBackground(BACKGROUND_COLOR);
        contentPanel.setBorder(new EmptyBorder(16, 16, 16, 16));
        
        // Create header panel with user info and actions
        JPanel headerPanel = createHeaderPanel();
        
        // Create main split pane
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        mainSplitPane.setDividerLocation(350);
        mainSplitPane.setDividerSize(1);
        mainSplitPane.setBorder(null);
        
        // Left panel for posts list
        JPanel leftPanel = createPostsListPanel();
        
        // Right panel for post details
        JPanel rightPanel = createPostDetailsPanel();
        
        // Add panels to split pane
        mainSplitPane.setLeftComponent(leftPanel);
        mainSplitPane.setRightComponent(rightPanel);
        
        // Add components to main content panel
        contentPanel.add(headerPanel, BorderLayout.NORTH);
        contentPanel.add(mainSplitPane, BorderLayout.CENTER);
        
        // Set as content pane
        setContentPane(contentPanel);
        
        // Load approved posts
        loadApprovedPosts();
        
        setVisible(true);
    }
    
    /**
     * Create header panel with user info and actions
     */
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BACKGROUND_COLOR);
        headerPanel.setBorder(new MatteBorder(0, 0, 1, 0, BORDER_COLOR));
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(0, 0, 1, 0, BORDER_COLOR),
            new EmptyBorder(0, 0, 12, 0)
        ));
        
        JLabel welcomeLabel = new JLabel("Welcome, " + currentUser.getUsername());
        welcomeLabel.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        welcomeLabel.setForeground(TEXT_COLOR);
        
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionPanel.setBackground(BACKGROUND_COLOR);
        
        JButton myPostsButton = createStyledButton("My Posts");
        JButton logoutButton = createStyledButton("Logout");
        logoutButton.setBackground(new Color(240, 240, 240));
        logoutButton.setForeground(TEXT_COLOR);
        
        actionPanel.add(myPostsButton);
        actionPanel.add(logoutButton);
        
        headerPanel.add(welcomeLabel, BorderLayout.WEST);
        headerPanel.add(actionPanel, BorderLayout.EAST);
        
        // Add action listeners
        myPostsButton.addActionListener(e -> showMyPosts());
        logoutButton.addActionListener(e -> logout());
        
        return headerPanel;
    }
    
    /**
     * Create panel containing posts list
     */
    private JPanel createPostsListPanel() {
        JPanel postsPanel = new JPanel(new BorderLayout(0, 12));
        postsPanel.setBackground(BACKGROUND_COLOR);
        postsPanel.setBorder(new EmptyBorder(16, 0, 0, 12));
        
        JLabel titleLabel = new JLabel("Found Items");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(TEXT_COLOR);
        
        postsModel = new DefaultListModel<>();
        postsList = new JList<>(postsModel);
        postsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        postsList.setBackground(BACKGROUND_COLOR);
        postsList.setForeground(TEXT_COLOR);
        postsList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        postsList.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        
        JScrollPane scrollPane = new JScrollPane(postsList);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        scrollPane.getViewport().setBackground(BACKGROUND_COLOR);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        
        JButton newPostButton = createStyledButton("Report Found Item");
        JButton refreshButton = createStyledButton("Refresh");
        refreshButton.setBackground(new Color(240, 240, 240));
        refreshButton.setForeground(TEXT_COLOR);
        
        buttonPanel.add(newPostButton);
        buttonPanel.add(refreshButton);
        
        postsPanel.add(titleLabel, BorderLayout.NORTH);
        postsPanel.add(scrollPane, BorderLayout.CENTER);
        postsPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add action listeners
        postsList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateSelectedPostDetails();
            }
        });
        
        newPostButton.addActionListener(e -> openPostCreationView());
        refreshButton.addActionListener(e -> loadApprovedPosts());
        
        return postsPanel;
    }
    
    /**
     * Create panel containing post details
     */
    private JPanel createPostDetailsPanel() {
        JPanel detailsPanel = new JPanel(new BorderLayout(0, 12));
        detailsPanel.setBackground(BACKGROUND_COLOR);
        detailsPanel.setBorder(new EmptyBorder(16, 12, 0, 0));
        
        JLabel titleLabel = new JLabel("Item Details");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(TEXT_COLOR);
        
        // Create a panel for details content with BoxLayout
        detailsContentPanel = new JPanel();
        detailsContentPanel.setLayout(new BoxLayout(detailsContentPanel, BoxLayout.Y_AXIS));
        detailsContentPanel.setBackground(BACKGROUND_COLOR);
        
        // Add scroll pane for the details content
        detailsScrollPane = new JScrollPane(detailsContentPanel);
        detailsScrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        detailsScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        actionPanel.setBackground(BACKGROUND_COLOR);
        
        JButton claimButton = createStyledButton("This is Mine");
        actionPanel.add(claimButton);
        
        detailsPanel.add(titleLabel, BorderLayout.NORTH);
        detailsPanel.add(detailsScrollPane, BorderLayout.CENTER);
        detailsPanel.add(actionPanel, BorderLayout.SOUTH);
        
        // Add action listener
        claimButton.addActionListener(e -> createClaim());
        
        return detailsPanel;
    }
    
    /**
     * Create a styled button with Notion-like appearance
     */
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        button.setBackground(BUTTON_COLOR);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorderPainted(true);
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
    
    /**
     * Load approved posts from database
     */
    private void loadApprovedPosts() {
        postsModel.clear();
        List<Post> approvedPosts = postController.getApprovedPosts();
        for (Post post : approvedPosts) {
            postsModel.addElement(post);

        }
        
        // Clear details when posts are reloaded
        clearDetailsPanel();
    }
    
    /**
     * Show only the current user's posts
     */
    private void showMyPosts() {
        postsModel.clear();
        List<Post> myPosts = postController.getPostsByUserId(currentUser.getUserId());
        for (Post post : myPosts) {
            postsModel.addElement(post);
        }
        
        // Clear details when posts are reloaded
        clearDetailsPanel();
    }
    
    /**
     * Clear the details panel content
     */
    private void clearDetailsPanel() {
        detailsContentPanel.removeAll();
        detailsContentPanel.revalidate();
        detailsContentPanel.repaint();
    }
    
    /**
     * Update the details display for the selected post
     */
    private void updateSelectedPostDetails() {
        Post selectedPost = postsList.getSelectedValue();
        if (selectedPost != null) {
            clearDetailsPanel();
            
            // Create a card-like panel for details
            JPanel cardPanel = new JPanel();
            cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
            cardPanel.setBackground(CARD_BACKGROUND);
            cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)
            ));
            cardPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            // Add title
            JLabel titleLabel = new JLabel(selectedPost.getTitle());
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
            titleLabel.setForeground(TEXT_COLOR);
            titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            cardPanel.add(titleLabel);
            
            cardPanel.add(Box.createRigidArea(new Dimension(0, 12)));
            
            // Add user and date info
            JPanel infoPanel = new JPanel(new GridLayout(3, 1, 0, 4));
            infoPanel.setBackground(CARD_BACKGROUND);
            infoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            infoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 75));
            
            JLabel userLabel = new JLabel("Posted by: " + selectedPost.getUsername());
            userLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            
            JLabel dateLabel = new JLabel("Date Found: " + selectedPost.getDateFound());
            dateLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            
            JLabel locationLabel = new JLabel("Location: " + selectedPost.getLocation());
            locationLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            
            infoPanel.add(userLabel);
            infoPanel.add(dateLabel);
            infoPanel.add(locationLabel);
            
            cardPanel.add(infoPanel);
            cardPanel.add(Box.createRigidArea(new Dimension(0, 16)));
            
            // Add description
            JLabel descTitle = new JLabel("Description:");
            descTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
            descTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
            cardPanel.add(descTitle);
            cardPanel.add(Box.createRigidArea(new Dimension(0, 6)));
            
            JTextArea descriptionArea = new JTextArea(selectedPost.getDescription());
            descriptionArea.setEditable(false);
            descriptionArea.setLineWrap(true);
            descriptionArea.setWrapStyleWord(true);
            descriptionArea.setBackground(CARD_BACKGROUND);
            descriptionArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            descriptionArea.setAlignmentX(Component.LEFT_ALIGNMENT);
            descriptionArea.setBorder(null);
            
            cardPanel.add(descriptionArea);
            cardPanel.add(Box.createRigidArea(new Dimension(0, 16)));
            
            // Add image if available
            if (selectedPost.getImagePath() != null && !selectedPost.getImagePath().isEmpty()) {
                JLabel imageTitle = new JLabel("Image:");
                imageTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
                imageTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
                cardPanel.add(imageTitle);
                cardPanel.add(Box.createRigidArea(new Dimension(0, 8)));
                
                // Create image panel
                JPanel imagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                imagePanel.setBackground(CARD_BACKGROUND);
                imagePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
                
                try {
                    // Load the image
                    ImageIcon originalIcon = new ImageIcon(selectedPost.getImagePath());
                    Image originalImage = originalIcon.getImage();
                    
                    // Scale the image to fit reasonably in the panel (max 300px height)
                    int maxHeight = 300;
                    int width = originalImage.getWidth(null);
                    int height = originalImage.getHeight(null);
                    
                    if (height > maxHeight) {
                        width = (int) ((double) width / height * maxHeight);
                        height = maxHeight;
                    }
                    
                    Image scaledImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                    ImageIcon scaledIcon = new ImageIcon(scaledImage);
                    
                    // Create a bordered image label
                    JLabel imageLabel = new JLabel(scaledIcon);
                    imageLabel.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
                    imagePanel.add(imageLabel);
                } catch (Exception e) {
                    JLabel errorLabel = new JLabel("Image not found: " + selectedPost.getImagePath());
                    errorLabel.setForeground(Color.RED);
                    imagePanel.add(errorLabel);
                    e.printStackTrace();
                }
                
                cardPanel.add(imagePanel);
                cardPanel.add(Box.createRigidArea(new Dimension(0, 16)));
            }
            
            // Add claimed status if applicable
            if (selectedPost.isClaimed()) {
                JPanel claimedPanel = new JPanel();
                claimedPanel.setBackground(new Color(255, 240, 240));
                claimedPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(255, 200, 200)),
                    BorderFactory.createEmptyBorder(8, 12, 8, 12)
                ));
                claimedPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
                claimedPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
                
                JLabel claimedLabel = new JLabel("THIS ITEM HAS BEEN CLAIMED");
                claimedLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
                claimedLabel.setForeground(new Color(200, 0, 0));
                
                claimedPanel.add(claimedLabel);
                cardPanel.add(claimedPanel);
            }
            
            // Add the card to the details panel
            detailsContentPanel.add(cardPanel);
            detailsContentPanel.add(Box.createVerticalGlue());
            
            // Update UI
            detailsContentPanel.revalidate();
            detailsContentPanel.repaint();
        }
    }
    
    /**
     * Open view to create a new post
     */
    private void openPostCreationView() {
        new PostView(currentUser, this);
    }
    
    /**
     * Create a new claim for the selected post
     */
    private void createClaim() {
        Post selectedPost = postsList.getSelectedValue();
        if (selectedPost == null) {
            JOptionPane.showMessageDialog(this, "Please select a post first", 
                                         "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (selectedPost.isClaimed()) {
            JOptionPane.showMessageDialog(this, "This item has already been claimed", 
                                         "Item Claimed", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Show claim dialog
        JDialog claimDialog = new JDialog(this, "Claim Item", true);
        claimDialog.setSize(450, 320);
        claimDialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Claim This Item");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setBorder(new MatteBorder(0, 0, 1, 0, BORDER_COLOR));
        titleLabel.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(0, 0, 1, 0, BORDER_COLOR),
            new EmptyBorder(0, 0, 10, 0)
        ));
        
        JPanel formPanel = new JPanel(new GridLayout(4, 1, 8, 8));
        formPanel.setBackground(BACKGROUND_COLOR);
        
        JLabel infoLabel = new JLabel("Please explain why you believe this item belongs to you:");
        infoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        infoLabel.setForeground(TEXT_COLOR);
        
        JTextArea reasonArea = new JTextArea(5, 30);
        reasonArea.setBackground(FIELD_BACKGROUND);
        reasonArea.setForeground(TEXT_COLOR);
        reasonArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        reasonArea.setLineWrap(true);
        reasonArea.setWrapStyleWord(true);
        reasonArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        JScrollPane reasonScroll = new JScrollPane(reasonArea);
        reasonScroll.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        
        JLabel contactLabel = new JLabel("Contact information for the finder to reach you:");
        contactLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        contactLabel.setForeground(TEXT_COLOR);
        
        JTextField contactField = new JTextField(20);
        contactField.setBackground(FIELD_BACKGROUND);
        contactField.setForeground(TEXT_COLOR);
        contactField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        contactField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        
        formPanel.add(infoLabel);
        formPanel.add(reasonScroll);
        formPanel.add(contactLabel);
        formPanel.add(contactField);
        
        JButton submitButton = createStyledButton("Submit Claim");
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(submitButton, BorderLayout.SOUTH);
        
        submitButton.addActionListener(e -> {
            String reason = reasonArea.getText();
            String contact = contactField.getText();
            
            if (reason.isEmpty()) {
                JOptionPane.showMessageDialog(claimDialog, "Please provide a reason for your claim", 
                                             "Missing Information", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Create claim in database
            ClaimController claimController = new ClaimController();
            Claim claim = new Claim(
                selectedPost.getPostId(),
                currentUser.getUserId(),
                reason,
                contact
            );
            
            if (claimController.createClaim(claim)) {
                JOptionPane.showMessageDialog(claimDialog, 
                    "Your claim has been submitted and is pending approval", 
                    "Claim Submitted", JOptionPane.INFORMATION_MESSAGE);
                claimDialog.dispose();
            } else {
                JOptionPane.showMessageDialog(claimDialog, 
                    "Failed to submit claim. Please try again.", 
                    "Submission Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        claimDialog.setContentPane(panel);
        claimDialog.setVisible(true);
    }
    
    /**
     * Logout and return to login screen
     */
    private void logout() {
        dispose();
        new LoginView();
    }
}