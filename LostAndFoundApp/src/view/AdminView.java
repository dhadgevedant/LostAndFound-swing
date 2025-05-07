package view;

import controller.AdminController;
import model.Claim;
import model.Post;
import model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.util.List;

/**
 * Admin view for post approval and claim management with a modern Notion-like UI
 */
public class AdminView extends JFrame {
    private User admin;
    private AdminController adminController;
    private JList<Post> pendingPostsList;
    private DefaultListModel<Post> pendingPostsModel;
    private JList<Post> approvedPostsList;
    private DefaultListModel<Post> approvedPostsModel;
    private JList<Claim> claimsList;
    private DefaultListModel<Claim> claimsModel;
    private JTextArea postDetailsArea;
    private JTabbedPane postsTabbedPane;
    
    // UI Colors for Notion-like theme
    private final Color BACKGROUND_COLOR = Color.WHITE;
    private final Color TEXT_COLOR = new Color(25, 25, 25);
    private final Color ACCENT_COLOR = new Color(55, 53, 47);
    private final Color BORDER_COLOR = new Color(235, 235, 235);
    private final Color HOVER_COLOR = new Color(245, 245, 245);
    private final Color BUTTON_COLOR = new Color(55, 53, 47);
    private final Color BUTTON_TEXT_COLOR = Color.WHITE;
    
    public AdminView(User admin) {
        this.admin = admin;
        this.adminController = new AdminController();
        
        setTitle("Lost and Found - Admin Panel");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Set UI look and feel for a more modern appearance
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
            // Set global UI properties
            UIManager.put("Panel.background", BACKGROUND_COLOR);
            UIManager.put("TextArea.background", BACKGROUND_COLOR);
            UIManager.put("TextField.background", BACKGROUND_COLOR);
            UIManager.put("List.background", BACKGROUND_COLOR);
            UIManager.put("Label.foreground", TEXT_COLOR);
            UIManager.put("Button.background", BUTTON_COLOR);
            UIManager.put("Button.foreground", BUTTON_TEXT_COLOR);
            UIManager.put("TabbedPane.background", BACKGROUND_COLOR);
            UIManager.put("TabbedPane.selected", HOVER_COLOR);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Create main panel with modern styling
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Create main split pane
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        mainSplitPane.setDividerLocation(400);
        mainSplitPane.setBorder(null);
        mainSplitPane.setBackground(BACKGROUND_COLOR);
        
        // Left panel for posts with tabbed interface
        JPanel leftPanel = createLeftPanel();
        
        // Right panel with post details and claims
        JPanel rightPanel = createRightPanel();
        
        // Add panels to main split pane
        mainSplitPane.setLeftComponent(leftPanel);
        mainSplitPane.setRightComponent(rightPanel);
        mainSplitPane.setResizeWeight(0.4);
        
        // Add components to frame
        JPanel topPanel = createTopPanel();
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(mainSplitPane, BorderLayout.CENTER);
        
        setContentPane(mainPanel);
        
        // Load posts
        loadPendingPosts();
        loadApprovedPosts();
        
        setVisible(true);
    }
    
    /**
     * Create the top panel with welcome message and logout button
     */
    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout(15, 15));
        topPanel.setBackground(BACKGROUND_COLOR);
        topPanel.setBorder(new MatteBorder(0, 0, 1, 0, BORDER_COLOR));
        
        JLabel welcomeLabel = new JLabel("Welcome, Admin " + admin.getUsername());
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        welcomeLabel.setForeground(TEXT_COLOR);
        
        JButton logoutButton = createStyledButton("Logout");
        logoutButton.addActionListener(e -> logout());
        
        JPanel welcomePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        welcomePanel.setBackground(BACKGROUND_COLOR);
        welcomePanel.add(welcomeLabel);
        
        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        logoutPanel.setBackground(BACKGROUND_COLOR);
        logoutPanel.add(logoutButton);
        
        topPanel.add(welcomePanel, BorderLayout.WEST);
        topPanel.add(logoutPanel, BorderLayout.EAST);
        
        return topPanel;
    }
    
    /**
     * Create the left panel with tabbed interface for pending and approved posts
     */
    private JPanel createLeftPanel() {
        JPanel leftPanel = new JPanel(new BorderLayout(10, 10));
        leftPanel.setBackground(BACKGROUND_COLOR);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10));
        
        // Create tabbed pane for pending and approved posts
        postsTabbedPane = new JTabbedPane();
        postsTabbedPane.setBackground(BACKGROUND_COLOR);
        postsTabbedPane.setForeground(TEXT_COLOR);
        
        // Pending posts tab
        JPanel pendingPostsPanel = new JPanel(new BorderLayout(10, 10));
        pendingPostsPanel.setBackground(BACKGROUND_COLOR);
        
        pendingPostsModel = new DefaultListModel<>();
        pendingPostsList = createStyledList(pendingPostsModel);
        JScrollPane pendingScrollPane = new JScrollPane(pendingPostsList);
        pendingScrollPane.setBorder(BorderFactory.createEmptyBorder());
        pendingPostsPanel.add(pendingScrollPane, BorderLayout.CENTER);
        
        JPanel pendingButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        pendingButtonPanel.setBackground(ACCENT_COLOR);
        JButton approveButton = createStyledButton("Approve");
        JButton rejectButton = createStyledButton("Reject");
        JButton refreshButton = createStyledButton("Refresh");
        
        pendingButtonPanel.add(approveButton);
        pendingButtonPanel.add(rejectButton);
        pendingButtonPanel.add(refreshButton);
        pendingPostsPanel.add(pendingButtonPanel, BorderLayout.SOUTH);
        
        // Approved posts tab
        JPanel approvedPostsPanel = new JPanel(new BorderLayout(10, 10));
        approvedPostsPanel.setBackground(BACKGROUND_COLOR);
        
        approvedPostsModel = new DefaultListModel<>();
        approvedPostsList = createStyledList(approvedPostsModel);
        JScrollPane approvedScrollPane = new JScrollPane(approvedPostsList);
        approvedScrollPane.setBorder(BorderFactory.createEmptyBorder());
        approvedPostsPanel.add(approvedScrollPane, BorderLayout.CENTER);
        
        JPanel approvedButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        approvedButtonPanel.setBackground(BACKGROUND_COLOR);
        JButton refreshApprovedButton = createStyledButton("Refresh");
        approvedButtonPanel.add(refreshApprovedButton);
        approvedPostsPanel.add(approvedButtonPanel, BorderLayout.SOUTH);
        
        // Add tabs to tabbed pane
        postsTabbedPane.addTab("Pending Posts", pendingPostsPanel);
        postsTabbedPane.addTab("Approved Posts", approvedPostsPanel);
        
        leftPanel.add(postsTabbedPane, BorderLayout.CENTER);
        
        // Add action listeners
        pendingPostsList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                if (pendingPostsList.getSelectedValue() != null) {
                    updateSelectedPostDetails(pendingPostsList.getSelectedValue());
                    // Clear selection in the other list
                    approvedPostsList.clearSelection();
                }
            }
        });
        
        approvedPostsList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                if (approvedPostsList.getSelectedValue() != null) {
                    updateSelectedPostDetails(approvedPostsList.getSelectedValue());
                    // Clear selection in the other list
                    pendingPostsList.clearSelection();
                }
            }
        });
        
        approveButton.addActionListener(e -> approveSelectedPost(true));
        rejectButton.addActionListener(e -> approveSelectedPost(false));
        refreshButton.addActionListener(e -> loadPendingPosts());
        refreshApprovedButton.addActionListener(e -> loadApprovedPosts());
        
        return leftPanel;
    }
    
    /**
     * Create the right panel with post details and claims
     */
    private JPanel createRightPanel() {
        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));
        rightPanel.setBackground(BACKGROUND_COLOR);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        // Details panel
        JPanel detailsPanel = new JPanel(new BorderLayout(10, 10));
        detailsPanel.setBackground(BACKGROUND_COLOR);
        detailsPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(BORDER_COLOR),
            "Post Details",
            0,
            0,
            new Font("Segoe UI", Font.BOLD, 14),
            TEXT_COLOR
        ));
        
        postDetailsArea = new JTextArea(10, 30);
        postDetailsArea.setEditable(false);
        postDetailsArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        postDetailsArea.setForeground(TEXT_COLOR);
        postDetailsArea.setLineWrap(true);
        postDetailsArea.setWrapStyleWord(true);
        postDetailsArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JScrollPane detailsScrollPane = new JScrollPane(postDetailsArea);
        detailsScrollPane.setBorder(BorderFactory.createEmptyBorder());
        detailsPanel.add(detailsScrollPane, BorderLayout.CENTER);
        
        // Claims panel
        JPanel claimsPanel = new JPanel(new BorderLayout(10, 10));
        claimsPanel.setBackground(BACKGROUND_COLOR);
        claimsPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(BORDER_COLOR),
            "Claims",
            0,
            0,
            new Font("Segoe UI", Font.BOLD, 14),
            TEXT_COLOR
        ));
        
        claimsModel = new DefaultListModel<>();
        claimsList = createStyledList(claimsModel);
        claimsList.setCellRenderer(new ClaimListCellRenderer());
        
        JScrollPane claimsScrollPane = new JScrollPane(claimsList);
        claimsScrollPane.setBorder(BorderFactory.createEmptyBorder());
        claimsPanel.add(claimsScrollPane, BorderLayout.CENTER);
        
        JPanel claimButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        claimButtonPanel.setBackground(ACCENT_COLOR);
        JButton approveClaimButton = createStyledButton("Approve Claim");
        JButton rejectClaimButton = createStyledButton("Reject Claim");
        JButton refreshClaimsButton = createStyledButton("Refresh Claims");
        
        claimButtonPanel.add(approveClaimButton);
        claimButtonPanel.add(rejectClaimButton);
        claimButtonPanel.add(refreshClaimsButton);
        claimsPanel.add(claimButtonPanel, BorderLayout.SOUTH);
        
        // Combine details and claims in the right panel with a vertical split
        JSplitPane rightSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, detailsPanel, claimsPanel);
        rightSplitPane.setDividerLocation(350);
        rightSplitPane.setBorder(null);
        rightSplitPane.setBackground(BACKGROUND_COLOR);
        rightPanel.add(rightSplitPane, BorderLayout.CENTER);
        
        // Add action listeners
        approveClaimButton.addActionListener(e -> approveSelectedClaim());
        rejectClaimButton.addActionListener(e -> rejectSelectedClaim());
        refreshClaimsButton.addActionListener(e -> {
            Post post = null;
            if (postsTabbedPane.getSelectedIndex() == 0) {
                post = pendingPostsList.getSelectedValue();
            } else {
                post = approvedPostsList.getSelectedValue();
            }
            
            if (post != null) {
                loadClaimsForPost(post.getPostId());
            }
        });
        
        return rightPanel;
    }
    
    /**
     * Create a styled JList with custom renderer
     */
    private <T> JList<T> createStyledList(DefaultListModel<T> model) {
        JList<T> list = new JList<>(model);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        list.setForeground(TEXT_COLOR);
        list.setBackground(BACKGROUND_COLOR);
        list.setSelectionBackground(HOVER_COLOR);
        list.setSelectionForeground(TEXT_COLOR);
        
        // Custom renderer for posts
        if (model == pendingPostsModel || model == approvedPostsModel) {
            list.setCellRenderer(new PostListCellRenderer());
        }
        
        return list;
    }
    
    /**
     * Create a styled JButton
     */
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setBackground(BUTTON_COLOR);
        button.setForeground(BUTTON_TEXT_COLOR);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        
        return button;
    }
    
    /**
     * Load pending posts from database
     */
    private void loadPendingPosts() {
        pendingPostsModel.clear();
        List<Post> pendingPosts = adminController.getPendingPosts();
        for (Post post : pendingPosts) {
            pendingPostsModel.addElement(post);
        }
        
        if (pendingPostsModel.isEmpty()) {
            postDetailsArea.setText("No pending posts to approve.");
        }
    }
    
    /**
     * Load approved posts from database
     */
    private void loadApprovedPosts() {
        approvedPostsModel.clear();
        List<Post> approvedPosts = adminController.getApprovedPosts();
        for (Post post : approvedPosts) {
            approvedPostsModel.addElement(post);
        }
        
        if (approvedPostsModel.isEmpty() && postsTabbedPane.getSelectedIndex() == 1) {
            postDetailsArea.setText("No approved posts available.");
        }
    }
    
    /**
     * Update the details display for the selected post
     */
    private void updateSelectedPostDetails(Post selectedPost) {
        if (selectedPost != null) {
            StringBuilder details = new StringBuilder();
            details.append("Title: ").append(selectedPost.getTitle()).append("\n\n");
            details.append("Posted by: ").append(selectedPost.getUsername()).append("\n");
            details.append("Date Found: ").append(selectedPost.getDateFound()).append("\n");
            details.append("Location: ").append(selectedPost.getLocation()).append("\n\n");
            details.append("Description: ").append(selectedPost.getDescription()).append("\n\n");
            
            if (selectedPost.getImagePath() != null && !selectedPost.getImagePath().isEmpty()) {
                details.append("Image: ").append(selectedPost.getImagePath()).append("\n");
            }
            
            if (selectedPost.isClaimed()) {
                details.append("\n*** THIS ITEM HAS BEEN CLAIMED ***\n");
            }
            
            postDetailsArea.setText(details.toString());
            
            // Load claims for this post
            loadClaimsForPost(selectedPost.getPostId());
        }
    }
    
    /**
     * Load claims for the selected post
     */
    private void loadClaimsForPost(int postId) {
        claimsModel.clear();
        List<Claim> claims = adminController.getClaimsForPost(postId);
        for (Claim claim : claims) {
            claimsModel.addElement(claim);
        }
        
        if (claimsModel.isEmpty()) {
            JPanel noClaims = new JPanel();
            noClaims.add(new JLabel("No claims for this post"));
            claimsList.setPrototypeCellValue(new Claim(0, 0, 0, "No claims available", "", rootPaneCheckingEnabled, null));
        }
    }
    
    /**
     * Approve or reject the selected post
     */
    private void approveSelectedPost(boolean approve) {
        Post selectedPost = pendingPostsList.getSelectedValue();
        if (selectedPost != null) {
            if (adminController.approvePost(selectedPost.getPostId(), approve)) {
                JOptionPane.showMessageDialog(this, 
                    "Post " + (approve ? "approved" : "rejected") + " successfully", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                loadPendingPosts();
                loadApprovedPosts();
                postDetailsArea.setText("");
                claimsModel.clear();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Failed to " + (approve ? "approve" : "reject") + " post", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a pending post first", 
                                         "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    /**
     * Approve the selected claim
     */
    private void approveSelectedClaim() {
        Claim selectedClaim = claimsList.getSelectedValue();
        if (selectedClaim != null) {
            if (adminController.approveClaim(selectedClaim.getClaimId())) {
                JOptionPane.showMessageDialog(this, "Claim approved successfully", 
                                             "Success", JOptionPane.INFORMATION_MESSAGE);
                
                // Refresh claims list and both posts lists
                Post post = null;
                if (postsTabbedPane.getSelectedIndex() == 0) {
                    post = pendingPostsList.getSelectedValue();
                } else {
                    post = approvedPostsList.getSelectedValue();
                }
                
                if (post != null) {
                    loadClaimsForPost(post.getPostId());
                }
                
                loadPendingPosts();
                loadApprovedPosts();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to approve claim", 
                                             "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a claim first", 
                                         "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    /**
     * Reject (remove) the selected claim
     */
    private void rejectSelectedClaim() {
        Claim selectedClaim = claimsList.getSelectedValue();
        if (selectedClaim != null) {
            if (adminController.rejectClaim(selectedClaim.getClaimId())) {
                JOptionPane.showMessageDialog(this, "Claim rejected successfully", 
                                             "Success", JOptionPane.INFORMATION_MESSAGE);
                
                // Refresh claims list
                Post post = null;
                if (postsTabbedPane.getSelectedIndex() == 0) {
                    post = pendingPostsList.getSelectedValue();
                } else {
                    post = approvedPostsList.getSelectedValue();
                }
                
                if (post != null) {
                    loadClaimsForPost(post.getPostId());
                }
            } else {
                JOptionPane.showMessageDialog(this, "Failed to reject claim", 
                                             "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a claim first", 
                                         "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    /**
     * Logout and return to login screen
     */
    private void logout() {
        dispose();
        new LoginView();
    }
    
    /**
     * Custom cell renderer for Post items
     */
    private class PostListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            
            if (value instanceof Post) {
                Post post = (Post) value;
                setText("<html><b>" + post.getTitle() + "</b><br>" + 
                      "<small>By: " + post.getUsername() + " | Location: " + post.getLocation() + 
                      (post.isClaimed() ? " | <i>CLAIMED</i>" : "") + "</small></html>");
                
                setBorder(BorderFactory.createCompoundBorder(
                    new MatteBorder(0, 0, 1, 0, BORDER_COLOR),
                    new EmptyBorder(10, 10, 10, 10)
                ));
                
                if (isSelected) {
                    setBackground(HOVER_COLOR);
                    setForeground(TEXT_COLOR);
                } else {
                    setBackground(BACKGROUND_COLOR);
                    setForeground(TEXT_COLOR);
                }
            }
            
            return c;
        }
    }
    
    /**
     * Custom cell renderer for Claim items
     */
    private class ClaimListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            
            if (value instanceof Claim) {
                Claim claim = (Claim) value;
                setText("<html><b>Claim #" + claim.getClaimId() + "</b><br>" + 
                      "<small>User ID: " + claim.getUserId() + "</small><br>" +
                      "<i>" + (claim.getClaimReason().length() > 50 ? claim.getClaimReason().substring(0, 50) + "..." : claim.getClaimReason()) + "</i></html>");
                
                setBorder(BorderFactory.createCompoundBorder(
                    new MatteBorder(0, 0, 1, 0, BORDER_COLOR),
                    new EmptyBorder(10, 10, 10, 10)
                ));
                
                if (isSelected) {
                    setBackground(HOVER_COLOR);
                    setForeground(TEXT_COLOR);
                } else {
                    setBackground(BACKGROUND_COLOR);
                    setForeground(TEXT_COLOR);
                }
            }
            
            return c;
        }
    }
}