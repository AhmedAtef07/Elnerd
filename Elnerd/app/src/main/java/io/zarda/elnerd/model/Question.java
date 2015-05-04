package io.zarda.elnerd.model;

import java.util.ArrayList;

/**
 * Created by atef & emad on 4 May, 2015.
 */
public class Question {
    private String header;
    private ArrayList<String> choices;
    private int correctIndex;

    public Question(String header, ArrayList<String> choices, int correctIndex) {
        this.header = header;
        this.choices = choices;
        this.correctIndex = correctIndex;
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
}
