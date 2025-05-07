-- Database creation script for Lost and Found App

CREATE DATABASE IF NOT EXISTS lost_and_found;
USE lost_and_found;

-- User table for authentication
CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    is_admin BOOLEAN DEFAULT FALSE,
    email VARCHAR(100),
    phone VARCHAR(20),
    registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Post table for lost/found items
CREATE TABLE IF NOT EXISTS posts (
    post_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    title VARCHAR(100) NOT NULL,
    description TEXT,
    image_path VARCHAR(255),
    location VARCHAR(100),
    date_found DATE,
    is_approved BOOLEAN DEFAULT FALSE,
    is_claimed BOOLEAN DEFAULT FALSE,
    post_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- Claims table for item claims
CREATE TABLE IF NOT EXISTS claims (
    claim_id INT AUTO_INCREMENT PRIMARY KEY,
    post_id INT,
    user_id INT,
    claim_reason TEXT,
    contact_info VARCHAR(255),
    is_approved BOOLEAN DEFAULT FALSE,
    claim_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (post_id) REFERENCES posts(post_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- Insert admin user
INSERT INTO users (username, password, is_admin, email) 
VALUES ('admin', 'admin123', TRUE, 'admin@lostandfound.com');

-- Insert regular user for testing
INSERT INTO users (username, password, is_admin, email) 
VALUES ('user1', 'user123', FALSE, 'user1@example.com');