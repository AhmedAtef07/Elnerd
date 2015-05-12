package io.zarda.elnerd.src;

import android.content.Context;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.login.widget.LoginButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.zarda.elnerd.MainActivity;
import io.zarda.elnerd.R;
import io.zarda.elnerd.model.Question;
import io.zarda.elnerd.model.Quote;
import io.zarda.elnerd.view.GameView;
import io.zarda.elnerd.view.HomeView;
import io.zarda.elnerd.view.QuoteView;
import io.zarda.elnerd.view.Viewable;

/**
 * Created by atef & emad on 4 May, 2015.
 */

public class ViewManager {

    private final List<View> gameViewsList;
    private final List<View> homeViewsList;
    private final List<View> quoteViewsList;

    GameViewNotifier gvn;
    HomeViewNotifier hvn;

    HomeView homeView;
    QuoteView quoteView;
    GameView gameView;

    Viewable currentView;

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

        LoginButton mButtonLogin = new LoginButton(context);
        mButtonLogin.setId(R.id.login_button);
        homeViewsArray.add(mButtonLogin);


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
            Button btn = new Button(context);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) context).answerClick(v);
                }
            });
            btn.setTag(i);
            gameViewsArray.add(btn);
        }

        gameViewsList = Collections.unmodifiableList(gameViewsArray);
        gvn = new GameViewNotifier(this, (MainActivity) context);
        hvn = new HomeViewNotifier(this);

        homeView = new HomeView(hvn);
        homeView.initializeView(context, homeViewsList);

        gameView = new GameView(gvn);
        gameView.initializeView(context, gameViewsList);

        quoteView = new QuoteView();
        quoteView.initializeView(context, quoteViewsList);
    }

    public void startHomeView() {
        ((MainActivity) context).updatePreferences();
        ((TextView) homeViewsList.get(1)).setText("" + bestPlayed);
        ((TextView) homeViewsList.get(2)).setText("" + allPlayed);
        homeView.startView();
        currentView = homeView;
    }

    public void endHomeView() {
        homeView.endView();
        currentView = null;
    }

    public void setScores(int best, int all) {
        bestPlayed = best;
        allPlayed = all;
    }

    public void startGameView() {
        gameView.startView();
        currentView = gameView;
    }

    public void endGameView() {
        gameView.endView();
        currentView = null;
    }

    public void showQuestion(Question question) {
        ((TextView) gameViewsList.get(0)).setText(question.getHeader());
        for (int i = 1; i < gameViewsList.size(); ++i) {
            ((Button) gameViewsList.get(i)).setText(question.getChoices().get(i - 1));
        }

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
        System.out.println("HHHH");
        ((MainActivity) context).setNewQuote();
        quoteView.startView();
        currentView = quoteView;
        quoteView.setTime(7000);
        CountDownTimer timer = new CountDownTimer(6000, 1000) {
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

    public void endQuoteView() {
        quoteView.endView();
        currentView = null;
    }

    public void showQuote(Quote quote) {
        quoteView.showQuote();
    }


    public boolean inHome() {
        return currentView == homeView;
    }

    public boolean inQuote() {
        return currentView == quoteView;
    }

}

