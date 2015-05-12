package io.zarda.elnerd.model;

/**
 * Created by atef & emad on 4 May, 2015.
 */
public class User {
    private String name;
    private String email;
    private int id;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
        this.id = 0;
    }

    public User(String name, String email, int id) {
        this.name = name;
        this.email = email;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
