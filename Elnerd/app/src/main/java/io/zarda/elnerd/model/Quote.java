package io.zarda.elnerd.model;

/**
 * Created by atef & emad on 4 May, 2015.
 */
public class Quote {
    private String content;
    private Question question;
    private String book;
    private int id;
    private int userId;
    private int categoryId;

    public Quote(String content, Question question, String book, int id, int userId,
                 int categoryId) {
        this.content = content;
        this.question = question;
        this.book = book;
        this.id = id;
        this.userId = userId;
        this.categoryId = categoryId;
    }

    public Quote(String content, Question question, String book) {
        this.content = content;
        this.question = question;
        this.book = book;
        this.id = 0;
        this.userId = 0;
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

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getBook() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
    }
}
