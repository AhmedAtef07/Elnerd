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
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.zarda.elnerd.R;
import io.zarda.elnerd.src.GameViewNotifier;

/**
 * Created by atef & emad on 4 May, 2015.
 * Implementing by magdy.
 */
public class GameView implements Viewable, Game {

    Context context;
    GameViewNotifier gvn;

    TextView questions;

    boolean clicked = false;

    FrameLayout frameLayout;

    TextView bar;

    ArrayList<Button> choices = new ArrayList<>();

    int time;

    LinearLayout mainLayout;

    int screenWidth;
    int screenHeight;

    Typeface typeface;

    String[] colors = {"#e74c3c", "#2ecc71", "#3498db", "#1abc9c", "#9b59b6",
            "#e67e22"};

    public GameView(GameViewNotifier gvn) {
        this.gvn = gvn;
    }

    @Override
    public void initializeView(Context context, List<View> views) {
        this.context = context;
        this.questions = (TextView) views.get(0);
        for (int i = 1; i < 5; ++i) {
            choices.add((Button) views.get(i));
        }
        Display screen = ((Activity) context).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        screen.getSize(size);
        this.screenWidth = size.x;
        this.screenHeight = size.y;
        intializeLayout();
    }

    @Override
    public void startView() {
        ((Activity) context).setContentView(frameLayout);
        Animation goDown = new TranslateAnimation(0, 0, screenHeight, 0);
        goDown.setDuration(500);
        frameLayout.startAnimation(goDown);
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

    @Override
    public void endView() {
        ((ViewGroup) frameLayout.getParent()).removeAllViews();
    }

    @Override
    public void showSuccess(final Button correctButton) {
        if(!clicked) {
            clicked = true;
            correctButton.setBackground(context.getResources().getDrawable(R.drawable.correct));
            Animation wait = new TranslateAnimation(0, 0, 0, 0);
            wait.setDuration(1000);
            bar.getAnimation().cancel();
            correctButton.startAnimation(wait);
            wait.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    gvn.notifyShowSuccessFinished();
                    correctButton.setBackground(context.getResources().getDrawable(R.drawable.btn));
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
    }

    @Override
    public void showFailure(final Button correctButton, final Button wrongButton) {
        if(!clicked) {
            clicked = true;
            correctButton.setBackground(context.getResources().getDrawable(R.drawable.correct));
            wrongButton.setBackground(context.getResources().getDrawable(R.drawable.wrong));
            Animation wait = new TranslateAnimation(0, 0, 0, 0);
            bar.getAnimation().cancel();
            wait.setDuration(1000);
            correctButton.startAnimation(wait);
            wait.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    gvn.notifyShowFailureFinished();
                    correctButton.setBackground(context.getResources().getDrawable(R.drawable.btn));
                    wrongButton.setBackground(context.getResources().getDrawable(R.drawable.btn));
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
    }

    @Override
    public void showNextQuestion() {

    }

    @Override
    public void setTime(int msTime) {
        clicked = false;
        this.time = msTime;
        TranslateAnimation timeAnimation = new TranslateAnimation(0, -screenWidth, 0, 0);
        timeAnimation.setDuration(time);
        timeAnimation.setFillAfter(true);

        timeAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Animation disappeare = new TranslateAnimation(0 , 0 , 0 , -50);
                disappeare.setFillAfter(true);
                bar.startAnimation(disappeare);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        bar.startAnimation(timeAnimation);
    }

    private void intializeLayout() {
        typeface = Typeface.createFromAsset(context.getAssets(),
                "fonts/JF Flat regular.ttf");
        mainLayout = new LinearLayout(context);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setGravity(Gravity.RIGHT);
        mainLayout.setBackgroundColor(Color.parseColor(colors[new Random().nextInt(6)]));
        RelativeLayout.LayoutParams barParam = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        bar = new TextView(context);
        barParam.setLayoutDirection(Gravity.TOP);
        bar.setLayoutParams(barParam);
        barParam.width = screenWidth;
        barParam.height = 50;
        barParam.setMargins(0, 0, 0, 200);
        bar.setBackgroundColor(Color.parseColor("#ffffff"));
        bar.setPadding(0, 0, 0, 400);
        bar.setLayoutParams(barParam);
        mainLayout.addView(bar);
        //space
        Space space0 = new Space(context);
        space0.setMinimumHeight(200);
        mainLayout.addView(space0);
        //question
        questions.setTextDirection(View.TEXT_DIRECTION_RTL);
        questions.setTextSize(32);
        questions.setAlpha(1);
        questions.setTextColor(Color.parseColor("#ecf0f1"));
        questions.setGravity(Gravity.CENTER);
        questions.setTypeface(typeface);
        mainLayout.addView(questions);
        //space
        Space space1 = new Space(context);
        space1.setMinimumHeight(100);
        mainLayout.addView(space1);
        //choices
        LinearLayout choicesLayout = new LinearLayout(context);
        choicesLayout.setOrientation(LinearLayout.VERTICAL);
        choicesLayout.setGravity(Gravity.CENTER);

        choicesLayout.addView(setChoicePro(choices.get(0)));
        Space space2 = new Space(context);
        space2.setMinimumHeight(20);
        choicesLayout.addView(space2);

        choicesLayout.addView(setChoicePro(choices.get(1)));
        Space space3 = new Space(context);
        space3.setMinimumHeight(20);
        choicesLayout.addView(space3);

        choicesLayout.addView(setChoicePro(choices.get(2)));
        Space space4 = new Space(context);
        space4.setMinimumHeight(20);
        choicesLayout.addView(space4);
        choicesLayout.addView(setChoicePro(choices.get(3)));

        mainLayout.addView(choicesLayout);

        frameLayout = new FrameLayout(context);
        frameLayout.addView(mainLayout);
    }

    public void addInFrameLayout(View view){
        frameLayout.addView(view);
    }

    private Button setChoicePro(Button choice) {
        RelativeLayout.LayoutParams choiceParam = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        choiceParam.width = 900;
        choiceParam.height = 190;
        choice.setTypeface(typeface);
        choice.setLayoutParams(choiceParam);
        choice.setBackground(context.getResources().getDrawable(R.drawable.btn));
        return choice;
    }
}
