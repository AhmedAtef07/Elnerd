package io.zarda.elnerd.src;

import java.util.ArrayList;

import io.zarda.elnerd.model.Question;
import io.zarda.elnerd.model.QuestionsDB;

/**
 * Created by atef & emad on 4 May, 2015.
 */
public class QuestionsManager {

    QuestionsDB questionsDB;

    public QuestionsManager() {
        questionsDB = QuestionsDB.getInstance();
    }

    public ArrayList<Question> getQuestions(){
        return questionsDB.getQuestions();
    }

    public void addQuestion(Question question) {
        questionsDB.addQuestion(question);
    }

}
