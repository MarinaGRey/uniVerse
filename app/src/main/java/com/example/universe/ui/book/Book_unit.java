package com.example.universe.ui.book;

import java.util.Objects;

// Create a Data Model: Define a class to represent a book with appropriate fields (like title, author, cover image,...)
public class Book_unit {
    private String title;
    private String author;
    private final String cover;
    private final String reviewer;
    private final float rating;
    private boolean isBookmarked;
    private final String postId;
    private final String userId;


    public Book_unit(String title, String author, String cover, String reviewer, float rating, boolean isBookmarked, String postId, String userId) {
        this.title = title;
        this.author = author;
        this.cover = cover;
        this.reviewer = reviewer;
        this.rating = rating;
        this.isBookmarked = isBookmarked;
        this.postId = postId;
        this.userId = userId;

    }

    // Create Getters and Setters for each variable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCover() {
        return cover;
    }

    public String getReviewer() {
        return reviewer;
    }

    public float getRating() {
        return rating;
    }


    // Getters and setters
    public boolean isBookmarked() {
        return isBookmarked; // Return bookmark state
    }

    public void setBookmarked(boolean isBookmarked) {
        this.isBookmarked = isBookmarked; // Set bookmark state
    }



    public String getPostId() {
        return postId;
    }

    public String getUserId() {
        return userId;
    }



    // ?????  Override equals and hashCode for correct behavior in hash-based collections
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Book_unit other = (Book_unit) obj;
        return title.equals(other.title) && author.equals(other.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, author);
    }









}