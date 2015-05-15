package io.zarda.elnerd.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import io.zarda.elnerd.R;
import io.zarda.elnerd.src.ScoreViewNotifier;

/**
 * Created by atef & emad on 4 May, 2015.
 */

public class ScoreView implements Viewable {

    Context context;
    TextView endGame;

    ScoreViewNotifier svn;

    TextView bestText;
    TextView bestScore;

    TextView currentText;
    TextView currentScore;

    Button retry;
    Button home;

    LinearLayout mainLayout;
    LinearLayout verticalScoresLayout;

    int screenWidth;
    int screenHeight;

    public ScoreView(ScoreViewNotifier svn) {
        this.svn = svn;
    }

    @Override
    public void initializeView(Context context, List<View> views) {
        this.context = context;

        endGame = (TextView) views.get(0);

        bestText = (TextView) views.get(1);
        bestScore = (TextView) views.get(2);

        currentText = (TextView) views.get(3);
        currentScore = (TextView) views.get(4);

        retry = (Button) views.get(5);
        home = (Button) views.get(6);

        Display screen = ((Activity) context).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        screen.getSize(size);
        this.screenWidth = size.x;
        this.screenHeight = size.y;

        intializeLayout();
    }

    @Override
    public void startView() {
//        ((Activity) context).setContentView(mainLayout);
        svn.notifyDoneLoad(mainLayout);

        ViewGroup.LayoutParams params = verticalScoresLayout.getLayoutParams();

        params.width = screenWidth - screenWidth / 5;
        params.height = screenHeight - 3 * screenHeight / 5;
    }

    @Override
    public void endView() {

        ((ViewGroup) mainLayout.getParent()).removeView(mainLayout);
    }

    private void intializeLayout() {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(),
                "fonts/AraJozoor-Regular.ttf");

        mainLayout = new LinearLayout(context);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setGravity(Gravity.CENTER);

        verticalScoresLayout = new LinearLayout(context);
        verticalScoresLayout.setOrientation(LinearLayout.VERTICAL);
        verticalScoresLayout.setGravity(Gravity.CENTER);
        verticalScoresLayout.setBackgroundColor(Color.parseColor("#2c3e50"));
        LinearLayout.LayoutParams scoresLayoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        verticalScoresLayout.setLayoutParams(scoresLayoutParams);

        LinearLayout.LayoutParams endGameParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        endGameParams.setMargins((int) (0.01851 * screenWidth), (int) (0.028153 * screenHeight),
                (int) (0.01851 * screenWidth), (int) (0.0731981 * screenHeight));
        endGame.setLayoutParams(endGameParams);
        endGame.setGravity(Gravity.CENTER);
        endGame.setTextSize((float) ((66.6667 * screenWidth) / screenHeight));
        endGame.setAlpha(1);
        endGame.setTextColor(Color.parseColor("#ecf0f1"));
        endGame.setTypeface(typeface);
        verticalScoresLayout.addView(endGame);

        LinearLayout horizontalScoresLayout1 = new LinearLayout(context);
        horizontalScoresLayout1.setOrientation(LinearLayout.HORIZONTAL);
        horizontalScoresLayout1.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams horizontalScoresParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        horizontalScoresLayout1.setLayoutParams(horizontalScoresParams);

        LinearLayout.LayoutParams scoresParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        scoresParams.setMargins((int) (0.01851 * screenWidth), (int) (0.0112612 * screenHeight),
                (int) (0.027778 * screenWidth), (int) (0.0112612 * screenHeight));

        currentScore.setGravity(Gravity.CENTER);
        currentScore.setTextSize((float) (33.333 * screenWidth / screenHeight));
        currentScore.setAlpha(1);
        currentScore.setTextColor(Color.parseColor("#ecf0f1"));
        currentScore.setTypeface(typeface);
        currentScore.setLayoutParams(scoresParams);

        currentText.setGravity(Gravity.CENTER);
        currentText.setTextSize((float) (33.333 * screenWidth / screenHeight));
        currentText.setAlpha(1);
        currentText.setTextColor(Color.parseColor("#ecf0f1"));
        currentText.setTypeface(typeface);
        currentText.setLayoutParams(scoresParams);

        horizontalScoresLayout1.addView(currentScore);
        horizontalScoresLayout1.addView(currentText);

        verticalScoresLayout.addView(horizontalScoresLayout1);

        LinearLayout horizontalScoresLayout2 = new LinearLayout(context);
        horizontalScoresLayout2.setOrientation(LinearLayout.HORIZONTAL);
        horizontalScoresLayout2.setGravity(Gravity.CENTER);
        horizontalScoresLayout2.setLayoutParams(horizontalScoresParams);

        bestScore.setGravity(Gravity.CENTER);
        bestScore.setTextSize((float) (33.333 * screenWidth / screenHeight));
        bestScore.setAlpha(1);
        bestScore.setTextColor(Color.parseColor("#ecf0f1"));
        bestScore.setTypeface(typeface);
        bestScore.setLayoutParams(scoresParams);

        bestText.setGravity(Gravity.CENTER);
        bestText.setTextSize((float) (33.333 * screenWidth / screenHeight));
        bestText.setAlpha(1);
        bestText.setTextColor(Color.parseColor("#ecf0f1"));
        bestText.setTypeface(typeface);
        bestText.setLayoutParams(scoresParams);

        horizontalScoresLayout2.addView(bestScore);
        horizontalScoresLayout2.addView(bestText);

        verticalScoresLayout.addView(horizontalScoresLayout2);

        mainLayout.addView(verticalScoresLayout);

        LinearLayout horizontalButtonsLayout = new LinearLayout(context);
        horizontalButtonsLayout.setOrientation(LinearLayout.HORIZONTAL);
        horizontalButtonsLayout.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams horizontalButtonsParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        horizontalButtonsLayout.setLayoutParams(horizontalButtonsParams);

        retry.setGravity(Gravity.CENTER);
        retry.setAlpha(1);
        retry.setTextColor(Color.parseColor("#ecf0f1"));
        retry.setTypeface(typeface);
        retry.setLayoutParams(scoresParams);
        scoresParams.height = (int) (0.084459 * screenHeight);
        scoresParams.width = (int) (0.3240740 * screenWidth);
        retry.setBackground(context.getResources().getDrawable(R.drawable.retry));

        home.setGravity(Gravity.CENTER);
        home.setAlpha(1);
        home.setTextColor(Color.parseColor("#ecf0f1"));
        home.setTypeface(typeface);
        home.setLayoutParams(scoresParams);
        home.setBackground(context.getResources().getDrawable(R.drawable.home));

        horizontalButtonsLayout.addView(retry);
        horizontalButtonsLayout.addView(home);

        mainLayout.addView(horizontalButtonsLayout);
    }

}
