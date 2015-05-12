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
    private int quoteId;
    private int modeId;
    private String mode;

    public Question(String header, ArrayList<String> choices, int correctIndex, int id,
                    int quoteId, String mode) {
        this.header = header;
        this.choices = choices;
        this.correctIndex = correctIndex;
        this.id = id;
        this.quoteId = quoteId;
        this.mode = mode;
    }

    public Question(String header, ArrayList<String> choices, int correctIndex) {
        this.header = header;
        this.choices = choices;
        this.correctIndex = correctIndex;
        this.id = 0;
        this.quoteId = 0;
        this.modeId = 0;
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

    public int getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(int quoteId) {
        this.quoteId = quoteId;
    }

    public int getModeId() {
        return modeId;
    }

    public void setModeId(int modeId) {
        this.modeId = modeId;
    }
}
