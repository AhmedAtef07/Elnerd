package io.zarda.elnerd.src;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

import io.zarda.elnerd.model.Constants;
import io.zarda.elnerd.model.Constants.SharedMemory;
import io.zarda.elnerd.model.QuestionsDB;
import io.zarda.elnerd.model.Quote;

/**
 * Created by atef & emad on 4 May, 2015.
 */
public class QuestionsManager implements Waitable {

    private QuestionsDB questionsDB;
    private ArrayList<Quote> quotesArrayList;
    private ApiManager apiManager;
    private Context context;
    private int requestSize;

    public QuestionsManager(Context context) {
        questionsDB = QuestionsDB.getInstance();
        apiManager = ApiManager.getInstance();
        this.context = context;
        quotesArrayList = new ArrayList<>();
        this.requestSize = Constants.DB_REQUEST_COUNT;
        getRandomQuotes();
    }

    public ArrayList<Quote> getRandomQuotes() {
        if (quotesArrayList.size() <= requestSize / 2) {
            quotesArrayList.addAll(questionsDB.getRandomQuotes(requestSize));
        }
        return quotesArrayList;
    }

    public void addQuote(Quote quote) {
        questionsDB.addQuote(quote);
    }

    public int addMode(String title) {
        return questionsDB.addMode(title);
    }

    public Quote getRandomQuote() {
        if (quotesArrayList.size() <= requestSize / 2) {
            getRandomQuotes();
        }
        if (quotesArrayList.size() > 0) {
            Random rand = new Random();
            int randomIndex = rand.nextInt(quotesArrayList.size());
            Quote quote = quotesArrayList.get(randomIndex);
            questionsDB.updateViewCounter(quote);
            quotesArrayList.remove(randomIndex);
            return quote;
        }
        return null;
    }

    public int getNumberOfQuestions() {
        return questionsDB.getNumberOfQuestions();
    }

    public boolean containsQuote() {
        return quotesArrayList.size() > 0;
    }

    public int getQuotesSize() {
        return quotesArrayList.size();
    }

    /**
     * After this method be called the result will be send to receiveResponse as an object.
     *
     * @param count number of questions to be downloaded.
     */
    public void downloadQuestions(int count) {
        long lastSyncTimeStamp = (long) SharedPreferencesManager.getInstance().getKey(
                SharedMemory.LAST_SYNC_TIMESTAMP, 0L);

        apiManager.downloadQuestions(lastSyncTimeStamp, count, this);
    }

    @Override
    public void receiveResponse(Object response) {
        ArrayList<Quote> quotesDownloaded = (ArrayList<Quote>) response;
        for (Quote quote : quotesDownloaded) {
            addQuote(quote);
            System.out.println(quote.getContent());
            Log.e("Quote Added", quote.getQuestion().getHeader());
        }
        getRandomQuotes();
    }
}
