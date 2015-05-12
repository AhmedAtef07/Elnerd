package io.zarda.elnerd.src;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

import io.zarda.elnerd.model.Constants;
import io.zarda.elnerd.model.Constants.SharedMemory;
import io.zarda.elnerd.model.Question;
import io.zarda.elnerd.model.QuestionsDB;

/**
 * Created by atef & emad on 4 May, 2015.
 */
public class QuestionsManager implements Waitable {

    private QuestionsDB questionsDB;
    private ArrayList<Question> questionArrayList;
    private ApiManager apiManager;
    private Context context;
    private int requestSize;

    public QuestionsManager(Context context) {
        questionsDB = QuestionsDB.getInstance();
        apiManager = ApiManager.getInstance();
        this.context = context;
        questionArrayList = new ArrayList<>();
        this.requestSize = Constants.DB_REQUEST_COUNT;
        getRandomQuestions();
    }

    public ArrayList<Question> getRandomQuestions() {
        if (questionArrayList.size() <= requestSize / 2) {
            questionArrayList.addAll(questionsDB.getRandomQuestions(requestSize));
        }
        return questionArrayList;
    }

    public void addQuestion(Question question) {
        questionsDB.addQuestion(question);
    }

    public Question getRandomQuestion() {
        if (questionArrayList.size() <= requestSize / 2) {
            getRandomQuestions();
        }
        if (questionArrayList.size() > 0) {
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

    public int getQuestionsSize() {
        return questionArrayList.size();
    }

    /**
     * After this method be called the result will be send to receiveResponse as an object.
     *
     * @param count number of questions to be downloaded.
     */
    public void downloadQuestions(int count) {

        Log.e("downloadQuestions", "HERE");
        SharedPreferences sharedpreferences = context.getSharedPreferences(
                SharedMemory.NAME.toString(), Context.MODE_PRIVATE);
        long lastSyncTimeStamp = sharedpreferences.getLong(
                SharedMemory.LAST_SYNC_TIMESTAMP.toString(), 0);
        apiManager.downloadQuestions(lastSyncTimeStamp, count, this);
    }

    @Override
    public void receiveResponse(Object response) {
        Log.e("API MANAGER", "HERE");
        ArrayList<Question> questionsDownloaded = (ArrayList<Question>) response;
        for (Question question : questionsDownloaded) {
//            addQuestion(question);
            System.out.println(question.getHeader());
        }
    }
}
