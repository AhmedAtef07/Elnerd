package io.zarda.elnerd.src;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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
import io.zarda.elnerd.view.ScoreView;

/**
 * Created by atef & emad on 4 May, 2015.
 */

public class ViewManager {

    private final List<View> homeViewsList;
    private final List<View> quoteViewsList;
    private final List<View> gameViewsList;
    private final List<View> scoreViewsList;

    HomeViewNotifier hvn;
    GameViewNotifier gvn;
    ScoreViewNotifier svn;

    CountDownTimer timer;

    HomeView homeView;
    QuoteView quoteView;
    GameView gameView;
    ScoreView scoreView;

    boolean inHome;
    boolean inQuote;
    boolean inGame;

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
        btn.setText("صفحة الإدارة");
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

        // score View
        ArrayList<View> scoreViewsArray = new ArrayList<>();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView endGame = new TextView(context);
        endGame.setText("انتهت اللعبة");
        scoreViewsArray.add(endGame);

        TextView bestText = new TextView(context);
        bestText.setText("أفضل نتيجة:");
        scoreViewsArray.add(bestText);

        TextView bestScore = new TextView(context);
        scoreViewsArray.add(bestScore);

        TextView currentText = new TextView(context);
        currentText.setText("النتيجة:");
        scoreViewsArray.add(currentText);

        TextView currentScore = new TextView(context);
        scoreViewsArray.add(currentScore);

        Button retry = new Button(context);
        retry.setText("اعادة");
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) context).retry(v);
            }
        });
        scoreViewsArray.add(retry);

        Button home = new Button(context);
        home.setText("المنزل");
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) context).goHome(v);
            }
        });
        scoreViewsArray.add(home);

        scoreViewsList = Collections.unmodifiableList(scoreViewsArray);


        gvn = new GameViewNotifier(this, context);
        hvn = new HomeViewNotifier(this);
        svn = new ScoreViewNotifier(this);

        homeView = new HomeView(hvn);
        homeView.initializeView(context, homeViewsList);

        gameView = new GameView(gvn);
        gameView.initializeView(context, gameViewsList);

        quoteView = new QuoteView();
        quoteView.initializeView(context, quoteViewsList);

        scoreView = new ScoreView(svn);
        scoreView.initializeView(context, scoreViewsList);

        inHome = false;
        inQuote = false;
        inHome = false;
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
        inGame = true;
    }

    public void endGameView() {
        gameView.endView();
        inGame = false;
    }

    public void showQuestion(Question question) {
        ((TextView) gameViewsList.get(0)).setText(question.getHeader());
        for (int i = 1; i < gameViewsList.size(); ++i) {
            ((Button) gameViewsList.get(i)).setText(question.getChoices().get(i - 1));
            ((Button) gameViewsList.get(i)).setEnabled(true);
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

    public void startScoreView() {
        scoreView.startView();
        inQuote = true;
    }

    public void endScoreView() {
        scoreView.endView();
        inQuote = false;
    }

    public void setScoreView(View scoreView) {
        gameView.addInFrameLayout(scoreView);
        ((TextView) scoreViewsList.get(2)).setText("" + ((MainActivity) context).getBestPlayed());
        ((TextView) scoreViewsList.get(4)).setText("" + ((MainActivity) context).getCurrentPlayed());
        for (int i = 1; i < 5; i++) {
            ((Button) gameViewsList.get(i)).setEnabled(false);
        }
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

