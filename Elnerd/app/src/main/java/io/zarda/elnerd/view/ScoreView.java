package io.zarda.elnerd.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

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
        ((ViewGroup) mainLayout.getParent()).removeAllViews();
    }

    private void intializeLayout() {
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
        endGameParams.setMargins(10, 50, 10, 150);
        endGame.setLayoutParams(endGameParams);
        verticalScoresLayout.addView(endGame);

        LinearLayout horizontalScoresLayout1 = new LinearLayout(context);
        horizontalScoresLayout1.setOrientation(LinearLayout.HORIZONTAL);
        horizontalScoresLayout1.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams horizontalScoresParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        horizontalScoresLayout1.setLayoutParams(horizontalScoresParams);

        LinearLayout.LayoutParams scoresParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        scoresParams.setMargins(20, 20, 30, 20);

        currentScore.setLayoutParams(scoresParams);
        currentScore.setLayoutParams(scoresParams);

        horizontalScoresLayout1.addView(currentScore);
        horizontalScoresLayout1.addView(currentText);

        verticalScoresLayout.addView(horizontalScoresLayout1);

        LinearLayout horizontalScoresLayout2 = new LinearLayout(context);
        horizontalScoresLayout2.setOrientation(LinearLayout.HORIZONTAL);
        horizontalScoresLayout2.setGravity(Gravity.CENTER);
        horizontalScoresLayout2.setLayoutParams(horizontalScoresParams);

        bestScore.setLayoutParams(scoresParams);
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

        retry.setLayoutParams(scoresParams);
        home.setLayoutParams(scoresParams);

        horizontalButtonsLayout.addView(retry);
        horizontalButtonsLayout.addView(home);

        mainLayout.addView(horizontalButtonsLayout);
    }

}
