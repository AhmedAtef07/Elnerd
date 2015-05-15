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
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;

import java.util.List;

import io.zarda.elnerd.R;
import io.zarda.elnerd.src.HomeViewNotifier;

/**
 * Created by atef & emad on 4 May, 2015.
 * Implementing by magdy.
 */
public class HomeView implements Viewable {

    Context context;
    HomeViewNotifier hvn;

    TextView elnerd;
    Button play;
    TextView best;
    TextView counter;
    Button adminPanel;

    int screenWidth;
    int screenHeight;

    LinearLayout mainLayout;

    public HomeView(HomeViewNotifier hvn) {
        this.hvn = hvn;
    }

    @Override
    public void initializeView(Context context, List<View> views) {
        //context
        this.context = context;
        //views
        play = (Button) views.get(0);
        best = (TextView) views.get(1);
        counter = (TextView) views.get(2);
        adminPanel = (Button) views.get(3);

        Display screen = ((Activity) context).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        screen.getSize(size);
        this.screenWidth = size.x;
        this.screenHeight = size.y;

        intializeLayout();
    }

    @Override
    public void startView() {
        ((Activity) context).setContentView(mainLayout);
    }


    @Override
    public void endView() {
        Animation goDown = new TranslateAnimation(0, 0, 0, screenHeight);
        goDown.setDuration(500);
        mainLayout.startAnimation(goDown);
        hvn.notifyHomeAnimationFinished();

    }

    private void intializeLayout() {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(),
                "fonts/AraJozoor-Regular.ttf");

        mainLayout = new LinearLayout(context);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setGravity(Gravity.CENTER);
        mainLayout.setBackgroundColor(Color.parseColor("#2c3e50"));
        //elnerd
        LinearLayout.LayoutParams elnerdParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        elnerd = new TextView(context);
        elnerd.setGravity(Gravity.CENTER);
        elnerd.setText("النّيرد");
        elnerd.setLayoutParams(elnerdParams);
        elnerd.setBackground(context.getResources().getDrawable(R.drawable.elnerd));
        elnerd.setTextSize(150 * screenWidth / screenHeight);
        elnerd.setAlpha(1);
        elnerd.setTextColor(Color.parseColor("#2c3e50"));
        elnerd.setTypeface(typeface);
        mainLayout.addView(elnerd);
        //space
        Space space0 = new Space(context);
        space0.setMinimumHeight((int) (0.084459 * screenHeight));
        mainLayout.addView(space0);
        //play button
        play.setBackground(context.getResources().getDrawable(R.drawable.pb));
        RelativeLayout.LayoutParams playParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        playParams.width = (int) (0.347222 * screenWidth);
        playParams.height = (int) (0.0844594 * screenHeight);
        playParams.setMargins(0, (int) (0.106981 * screenHeight), 0,
                (int) (0.106981 * screenHeight));
        play.setLayoutParams(playParams);
        mainLayout.addView(play);
        //space
        Space space1 = new Space(context);
        space1.setMinimumHeight((int) (0.05630 * screenHeight));
        mainLayout.addView(space1);
        //statistics bar
        LinearLayout statisticsLayout = new LinearLayout(context);
        statisticsLayout.setOrientation(LinearLayout.HORIZONTAL);
        RelativeLayout.LayoutParams statisticsParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        statisticsParams.setMargins(0, (int) (0.1126126 * screenHeight), 0,
                (int) (0.1126126 * screenHeight));
        statisticsLayout.setLayoutParams(statisticsParams);
        //textLayout
        LinearLayout textLayout = new LinearLayout(context);
        textLayout.setOrientation(LinearLayout.VERTICAL);
        textLayout.setGravity(Gravity.RIGHT);
        //counterNumber
        RelativeLayout.LayoutParams counterParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        counter.setGravity(Gravity.RIGHT);
        counter.setLayoutParams(counterParams);
        counter.setTextSize((float) ((83.333 * screenWidth) / screenHeight));
        counter.setTypeface(typeface);
        counter.setTextColor(Color.parseColor("#fcfefe"));
        textLayout.addView(counter);
        //bestumber
        RelativeLayout.LayoutParams bestParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        best.setGravity(Gravity.RIGHT);
        best.setLayoutParams(bestParams);
        bestParams.width = (int) (screenWidth / 3 - 0.009259 * screenWidth);
        best.setTextSize((float) ((83.333 * screenWidth) / screenHeight));
        best.setTypeface(typeface);
        best.setTextColor(Color.parseColor("#fcfefe"));
        textLayout.addView(best);
        //numberLayout
        LinearLayout numberLayout = new LinearLayout(context);
        numberLayout.setOrientation(LinearLayout.VERTICAL);
        numberLayout.setPadding(0, (int) (0.0281531 * screenHeight), 0, 0);
        //answered
        TextView answered = new TextView(context);
        answered.setText("الأسئلة المجابة");
        answered.setGravity(Gravity.LEFT);
        answered.setLayoutParams(counterParams);
        answered.setTextSize((float) (33.333 * screenWidth / screenHeight));
        answered.setTypeface(typeface);
        answered.setTextColor(Color.parseColor("#fcfefe"));
        numberLayout.addView(answered);
        //best
        TextView bestScore = new TextView(context);
        bestScore.setText("أفضل نتيجة");
        bestScore.setGravity(Gravity.LEFT);
        bestScore.setPadding(0, (int) (0.0675675 * screenHeight), 0, 0);
        bestScore.setLayoutParams(counterParams);
        bestScore.setTextSize((float) (33.333 * screenWidth / screenHeight));
        bestScore.setTypeface(typeface);
        bestScore.setTextColor(Color.parseColor("#fcfefe"));
        numberLayout.addView(bestScore);
        //seperator
        TextView seperator = new TextView(context);
        seperator.setBackground(context.getResources().getDrawable(R.drawable.seperator));
        LinearLayout.LayoutParams seperatorParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        seperatorParams.width = (int) (0.018515 * screenWidth);
        seperatorParams.height = (int) (0.16891 * screenHeight);
        seperatorParams.setMargins(0, (int) (0.01970 * screenHeight), 0, 0);
        seperator.setLayoutParams(seperatorParams);
        //
        statisticsLayout.addView(textLayout);
        statisticsLayout.addView(seperator);
        statisticsLayout.addView(numberLayout);
        //
        mainLayout.addView(statisticsLayout);
        //space
        Space space2 = new Space(context);
        space2.setMinimumHeight((int) (0.0281513 * screenHeight));
        mainLayout.addView(space2);
        //adminPanel
        RelativeLayout.LayoutParams adminPanelParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        adminPanel.setLayoutParams(adminPanelParams);
        adminPanelParams.width = (int) (0.4629629 * screenWidth);
        adminPanelParams.height = (int) (0.106981 * screenHeight);
        adminPanel.setTypeface(typeface);
        adminPanel.setTextSize((float) ((33.333 * screenWidth) / screenHeight));
        adminPanel.setLayoutParams(adminPanelParams);
        adminPanel.setBackground(context.getResources().getDrawable(R.drawable.btn));
        mainLayout.addView(adminPanel);
    }

    public void fromQuestion() {
        Animation goUp = new TranslateAnimation(0, 0, -screenHeight, 0);
        goUp.setDuration(500);
        mainLayout.startAnimation(goUp);
    }

    public void fromQuote() {
        Animation goUp = new TranslateAnimation(0, 0, screenHeight, 0);
        goUp.setDuration(500);
        mainLayout.startAnimation(goUp);
    }
}
