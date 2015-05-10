package io.zarda.elnerd.src;

import java.util.ArrayList;
import java.util.Random;

import io.zarda.elnerd.model.Question;
import io.zarda.elnerd.model.QuestionsDB;

/**
 * Created by atef & emad on 4 May, 2015.
 */
public class QuestionsManager {

    QuestionsDB questionsDB;
    ArrayList<Question> questionArrayList;

    int requestSize;

    public QuestionsManager(int requestSize) {
        questionsDB = QuestionsDB.getInstance();
        questionArrayList = new ArrayList<>();
        this.requestSize = requestSize;
        getRandomQuestions();
    }

    public ArrayList<Question> getQuestions(){
        questionArrayList = questionsDB.getQuestions();
        return questionArrayList;
    }

    public ArrayList<Question> getRandomQuestions(){
        if (questionArrayList.size() <= requestSize / 2) {
            questionArrayList.addAll(questionsDB.getRandomQuestions(requestSize));
        }
        return questionArrayList;
    }

    public void addQuestion(Question question) {
        questionsDB.addQuestion(question);
    }

    public Question getRandomQuestion() {
        if (questionArrayList.size() > 0) {
            if (questionArrayList.size() <= requestSize / 2) {
                getRandomQuestions();
            }
            Random rand = new Random();
            int randomIndex = rand.nextInt(questionArrayList.size());
            Question question = questionArrayList.get(randomIndex);
            questionsDB.updateViewCounter(question);
            questionArrayList.remove(randomIndex);
            return question;
        }

        return null;
    }

    public boolean containsQuestion() {
        return questionArrayList.size() > 0;
    }

    public int questionsSize() {
        return questionArrayList.size();
    }

}
