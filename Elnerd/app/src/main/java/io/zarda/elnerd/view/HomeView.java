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

    public HomeView(HomeViewNotifier hvn){
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
        ((Activity)context).setContentView(mainLayout);
    }



    @Override
    public void endView() {
        Animation goDown = new TranslateAnimation(0, 0, 0, screenHeight);
        goDown.setDuration(500);
        mainLayout.startAnimation(goDown);
        hvn.notifyHomeAnimationFinished();
        goDown.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
//                ((ViewGroup) mainLayout.getParent()).removeAllViews();
//                hvn.notifyHomeAnimationFinished();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
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
//        elnerd.setPadding(0 , 100 , 0 , 0);
        elnerd.setLayoutParams(elnerdParams);
        elnerd.setBackground(context.getResources().getDrawable(R.drawable.elnerd));
        elnerd.setTextSize(90);
        elnerd.setAlpha(1);
        elnerd.setTextColor(Color.parseColor("#2c3e50"));
        elnerd.setTypeface(typeface);
        mainLayout.addView(elnerd);
        //space
        Space space0 = new Space(context);
        space0.setMinimumHeight(150);
        mainLayout.addView(space0);
        //play button
        play.setBackground(context.getResources().getDrawable(R.drawable.pb));
        RelativeLayout.LayoutParams playParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        playParams.width = 375;
        playParams.height = 150;
        playParams.setMargins(0, 190, 0, 190);
        play.setLayoutParams(playParams);
        mainLayout.addView(play);
        //space
        Space space1 = new Space(context);
        space1.setMinimumHeight(100);
        mainLayout.addView(space1);
        //statistics bar
        LinearLayout statisticsLayout = new LinearLayout(context);
        statisticsLayout.setOrientation(LinearLayout.HORIZONTAL);
        RelativeLayout.LayoutParams statisticsParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        statisticsParams.setMargins(0, 200, 0, 200);
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
        counter.setTextSize(50);
        counter.setTypeface(typeface);
        counter.setTextColor(Color.parseColor("#fcfefe"));
        textLayout.addView(counter);
        //bestumber
        RelativeLayout.LayoutParams bestParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        best.setGravity(Gravity.RIGHT);
        best.setLayoutParams(bestParams);
        bestParams.width = screenWidth / 3 - 10;
        best.setTextSize(50);
        best.setTypeface(typeface);
        best.setTextColor(Color.parseColor("#fcfefe"));
        textLayout.addView(best);
        //numberLayout
        LinearLayout numberLayout = new LinearLayout(context);
        numberLayout.setOrientation(LinearLayout.VERTICAL);
        numberLayout.setPadding(0, 50, 0, 0);
        //answered
        RelativeLayout.LayoutParams answeredParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        answeredParams.setMargins(0 , 1000 , 0 , 0);
        TextView answered = new TextView(context);
        answered.setText("الأسئلة المجابة");
        answered.setGravity(Gravity.LEFT);
        answered.setLayoutParams(counterParams);
        answered.setTextSize(20);
        answered.setTypeface(typeface);
        answered.setTextColor(Color.parseColor("#fcfefe"));
        numberLayout.addView(answered);
        //best
        TextView bestScore = new TextView(context);
        bestScore.setText("أفضل نتيجة");
        bestScore.setGravity(Gravity.LEFT);
        bestScore.setPadding(0, 120, 0, 0);
        bestScore.setLayoutParams(counterParams);
        bestScore.setTextSize(20);
        bestScore.setTypeface(typeface);
        bestScore.setTextColor(Color.parseColor("#fcfefe"));
        numberLayout.addView(bestScore);
        //seperator
        TextView seperator = new TextView(context);
        seperator.setBackground(context.getResources().getDrawable(R.drawable.seperator));
        LinearLayout.LayoutParams seperatorParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        seperatorParams.width = 25;
        seperatorParams.height = 300;
        seperatorParams.setMargins(0, 35, 0, 0);
        seperator.setLayoutParams(seperatorParams);
        //
        statisticsLayout.addView(textLayout);
        statisticsLayout.addView(seperator);
        statisticsLayout.addView(numberLayout);
        //
        mainLayout.addView(statisticsLayout);
        //space
        Space space2 = new Space(context);
        space2.setMinimumHeight(50);
        mainLayout.addView(space2);
        //adminPanel
        RelativeLayout.LayoutParams adminPanelParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        adminPanel.setLayoutParams(adminPanelParams);
        adminPanelParams.width = 500;
        adminPanelParams.height = 190;
        adminPanel.setTypeface(typeface);
        adminPanel.setTextSize(20);
        adminPanel.setLayoutParams(adminPanelParams);
        adminPanel.setBackground(context.getResources().getDrawable(R.drawable.btn));
        mainLayout.addView(adminPanel);
    }

    public void fromQuestion(){
        Animation goUp = new TranslateAnimation(0, 0, -screenHeight, 0);
        goUp.setDuration(500);
        mainLayout.startAnimation(goUp);
        goUp.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
//                ((ViewGroup) mainLayout.getParent()).removeAllViews();
//                hvn.notifyHomeAnimationFinished();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void fromQuote(){
        Animation goUp = new TranslateAnimation(0, 0, screenHeight , 0);
        goUp.setDuration(500);
        mainLayout.startAnimation(goUp);
        goUp.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
//                ((ViewGroup) mainLayout.getParent()).removeAllViews();
//                hvn.notifyHomeAnimationFinished();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
