package io.zarda.elnerd.model;

/**
 * Created by atef & emad on 4 May, 2015.
 */
public class Quote {
    private String content;
    private int id;
    private int userId;
    private int bookId;
    private int categoryId;

    public Quote(String content, int id, int userId, int bookId, int categoryId) {
        this.content = content;
        this.id = id;
        this.userId = userId;
        this.bookId = bookId;
        this.categoryId = categoryId;
    }

    public Quote(String content) {
        this.content = content;
        this.id = 0;
        this.userId = 0;
        this.bookId = 0;
        this.categoryId = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
}
