package io.zarda.elnerd.model;

import java.util.ArrayList;

/**
 * Created by atef & emad on 4 May, 2015.
 */

public class Question {

    private String header;
    private ArrayList<String> choices;
    private int correctIndex;
    private int id;

    public Question(String header, ArrayList<String> choices, int correctIndex, int id) {
        this.header = header;
        this.choices = choices;
        this.correctIndex = correctIndex;
        this.id = id;
    }

    public Question(String header, ArrayList<String> choices, int correctIndex) {
        this.header = header;
        this.choices = choices;
        this.correctIndex = correctIndex;
        this.id = 0;
    }

    public String getHeader() {
        return header;
    }

    public ArrayList<String> getChoices() {
        return choices;
    }

    public int getCorrectIndex() {
        return correctIndex;
    }
    public int getId() {
        return id;
    }

}
