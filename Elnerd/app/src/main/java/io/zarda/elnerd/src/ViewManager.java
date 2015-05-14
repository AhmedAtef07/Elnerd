package io.zarda.elnerd.src;

import android.content.Context;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.zarda.elnerd.MainActivity;
import io.zarda.elnerd.model.Constants;
import io.zarda.elnerd.model.Question;
import io.zarda.elnerd.model.Quote;
import io.zarda.elnerd.view.GameView;
import io.zarda.elnerd.view.HomeView;
import io.zarda.elnerd.view.QuoteView;

/**
 * Created by atef & emad on 4 May, 2015.
 */

public class ViewManager {

    private final List<View> gameViewsList;
    private final List<View> homeViewsList;
    private final List<View> quoteViewsList;

    GameViewNotifier gvn;
    HomeViewNotifier hvn;

    CountDownTimer timer;

    HomeView homeView;
    QuoteView quoteView;
    GameView gameView;

    boolean inHome;
    boolean inQuote;

    int bestPlayed;
    int allPlayed;

    private Context context;

    public ViewManager(final Context context) {
        this.context = context;

        // homeView
        ArrayList<View> homeViewsArray = new ArrayList<>();
        Button playButton = new Button(context);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) context).playClick(v);
            }
        });
        homeViewsArray.add(playButton);
        homeViewsArray.add(new TextView(context));
        homeViewsArray.add(new TextView(context));

        Button btn = new Button(context);
        btn.setText("Admin Panel");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) context).adminClick(v);
            }
        });
        homeViewsArray.add(btn);

        homeViewsList = Collections.unmodifiableList(homeViewsArray);

        // quoteView
        ArrayList<View> quoteViewsArray = new ArrayList<>();
        quoteViewsArray.add(new TextView(context));
        quoteViewsArray.add(new TextView(context));

        quoteViewsList = Collections.unmodifiableList(quoteViewsArray);

        // game View
        ArrayList<View> gameViewsArray = new ArrayList<>();
        gameViewsArray.add(new TextView(context));
        for (int i = 0; i < 4; ++i) {
            Button btn2 = new Button(context);
            btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) context).answerClick(v);
                }
            });
            btn2.setTag(i);
            gameViewsArray.add(btn2);
        }

        gameViewsList = Collections.unmodifiableList(gameViewsArray);

        gvn = new GameViewNotifier(this);
        hvn = new HomeViewNotifier(this);

        homeView = new HomeView(hvn);
        homeView.initializeView(context, homeViewsList);

        gameView = new GameView(gvn);
        gameView.initializeView(context, gameViewsList);

        quoteView = new QuoteView();
        quoteView.initializeView(context, quoteViewsList);

        inHome = false;
        inQuote = false;
    }

    public void startHomeView() {
        ((MainActivity) context).updatePreferences();
        ((TextView) homeViewsList.get(1)).setText("" + bestPlayed);
        ((TextView) homeViewsList.get(2)).setText("" + allPlayed);
        homeView.startView();
        inHome = true;
    }

    public void endHomeView() {
        homeView.endView();
        inHome = false;
    }

    public void setScores(int best, int all) {
        bestPlayed = best;
        allPlayed = all;
    }

    public void startGameView() {
        gameView.startView();
        ((MainActivity) context).setNewQuestion();
    }

    public void endGameView() {
        gameView.endView();
    }

    public void showQuestion(Question question) {
        ((TextView) gameViewsList.get(0)).setText(question.getHeader());
        for (int i = 1; i < gameViewsList.size(); ++i) {
            ((Button) gameViewsList.get(i)).setText(question.getChoices().get(i - 1));
        }

        gameView.setTime(Constants.QUESTION_TIME);
        gameView.showNextQuestion();
    }

    public void showSuccess(int correctButtonIndex) {
        gameView.showSuccess((Button) gameViewsList.get(correctButtonIndex + 1));
    }

    public void showFailure(int correctButtonIndex, int clickedButtonIndex) {
        gameView.showFailure((Button) gameViewsList.get(correctButtonIndex + 1),
                (Button) gameViewsList.get(clickedButtonIndex + 1));
    }

    public void startQuoteView() {
        inQuote = true;
        quoteView.startView();
        quoteView.setTime(Constants.QUOTE_TIME);
        ((MainActivity) context).setNewQuote();
        timer = new CountDownTimer(Constants.QUOTE_TIME, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                System.out.println("seconds remaining: " + (double) millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                endQuoteView();
                startGameView();
            }
        };
        timer.start();
    }

    public void cancelTimer() {
        if (timer != null) {
            timer.cancel();
        }
    }

    public void endQuoteView() {
        quoteView.endView();
        inQuote = false;
    }

    public void showQuote(Quote quote) {
        ((TextView) quoteViewsList.get(0)).setText(quote.getContent());
        ((TextView) quoteViewsList.get(1)).setText(quote.getBook());
        quoteView.showQuote();
    }


    public boolean inHome() {
        return inHome;
    }

    public boolean inQuote() {
        return inQuote;
    }

}

