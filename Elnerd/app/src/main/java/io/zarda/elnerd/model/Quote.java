package io.zarda.elnerd.model;

/**
 * Created by atef & emad on 4 May, 2015.
 */
public class Quote {
    private String content;
    private Question question;
    private String book;
    private int id;
    private String userName;
    private int categoryId;

    public Quote(String content, Question question, String book, String userName, int id,
                 int categoryId) {
        this.content = content;
        this.question = question;
        this.book = book;
        this.userName = userName;
        this.id = id;
        this.categoryId = categoryId;
    }

    public Quote(String content, Question question, String book, String userName) {
        this.content = content;
        this.question = question;
        this.book = book;
        this.userName = userName;
        this.id = 0;
        this.categoryId = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
