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
    Typeface typeface;

    public ViewManager(final Context context) {
        this.context = context;

        typeface = Typeface.createFromAsset(context.getAssets(),
                "fonts/AraJozoor-Regular.ttf");

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

        // score View
        ArrayList<View> scoreViewsArray = new ArrayList<>();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView endGame = new TextView(context);
        endGame.setGravity(Gravity.CENTER);
        endGame.setText("انتهت اللعبة");
        endGame.setTextSize(40);
        endGame.setAlpha(1);
        endGame.setLayoutParams(params);
        endGame.setTextColor(Color.parseColor("#ecf0f1"));
        endGame.setTypeface(typeface);
        scoreViewsArray.add(endGame);

        TextView bestText = new TextView(context);
        bestText.setGravity(Gravity.CENTER);
        bestText.setText("أفضل نتيجة:");
        bestText.setTextSize(25);
        bestText.setAlpha(1);
        bestText.setLayoutParams(params);
        bestText.setTextColor(Color.parseColor("#ecf0f1"));
        bestText.setTypeface(typeface);
        scoreViewsArray.add(bestText);

        TextView bestScore = new TextView(context);
        bestScore.setGravity(Gravity.CENTER);
        bestScore.setTextSize(25);
        bestScore.setAlpha(1);
        bestScore.setLayoutParams(params);
        bestScore.setTextColor(Color.parseColor("#ecf0f1"));
        bestScore.setTypeface(typeface);
        scoreViewsArray.add(bestScore);

        TextView currentText = new TextView(context);
        currentText.setGravity(Gravity.CENTER);
        currentText.setText("النتيجة:");
        currentText.setTextSize(25);
        currentText.setAlpha(1);
        currentText.setLayoutParams(params);
        currentText.setTextColor(Color.parseColor("#ecf0f1"));
        currentText.setTypeface(typeface);
        scoreViewsArray.add(currentText);

        TextView currentScore = new TextView(context);
        currentScore.setGravity(Gravity.CENTER);
        currentScore.setTextSize(25);
        currentScore.setAlpha(1);
        currentScore.setLayoutParams(params);
        currentScore.setTextColor(Color.parseColor("#ecf0f1"));
        currentScore.setTypeface(typeface);
        scoreViewsArray.add(currentScore);

        Button retry = new Button(context);
        retry.setGravity(Gravity.CENTER);
        retry.setText("اعادة");
        retry.setTextSize(20);
        retry.setAlpha(1);
        retry.setLayoutParams(params);
        retry.setTextColor(Color.parseColor("#ecf0f1"));
        retry.setTypeface(typeface);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) context).retry(v);
            }
        });
        retry.setBackgroundColor(Color.parseColor("#51678b"));
        scoreViewsArray.add(retry);

        Button home = new Button(context);
        home.setGravity(Gravity.CENTER);
        home.setText("المنزل");
        home.setTextSize(20);
        home.setAlpha(1);
        home.setLayoutParams(params);
        home.setTextColor(Color.parseColor("#ecf0f1"));
        home.setTypeface(typeface);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) context).goHome(v);
            }
        });
        home.setBackgroundColor(Color.parseColor("#51678b"));
        scoreViewsArray.add(home);

        scoreViewsList = Collections.unmodifiableList(scoreViewsArray);


        gvn = new GameViewNotifier(this);
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

