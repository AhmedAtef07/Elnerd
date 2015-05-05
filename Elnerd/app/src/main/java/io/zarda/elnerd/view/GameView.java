package io.zarda.elnerd.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

/**
 * Created by atef & emad on 4 May, 2015.
 * Implementing by magdy.
 */
public class GameView implements Viewable {
    Context context;

    int screenWidth;
    int screenHeight;

    TableLayout layout;

    RelativeLayout displayLayout;
    TextView card;

    Button firstChoice;
    Button secondChoice;
    Button thirdChoice;
    Button forthChoice;

    private float degree = 0;
    private int randomIndex = 0;

    @Override
    public void initializeView(Context context , List<View> views) {
        setFrame();
        setDimension();
        setLayout();
        setDisplayLayout();
        setButtons();
        addCard(card);
        card = (TextView) views.get(0);
        firstChoice = (Button) views.get(1);
        secondChoice = (Button) views.get(2);
        thirdChoice = (Button) views.get(3);
        forthChoice = (Button) views.get(4);

    }

    @Override
    public void startView() {
        ((Activity) context).setContentView(layout);
    }

    @Override
    public void endView() {

    }

    private void setFrame(){
        ((Activity) context).getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ((Activity) context).requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    private void setLayout(){
        layout = new TableLayout(context);
        layout.setGravity(Gravity.CENTER);
    }

    private void setDisplayLayout(){
        displayLayout = new RelativeLayout(context);
        RelativeLayout.LayoutParams displayParams = new RelativeLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT);
        displayParams.width = screenWidth;
        displayParams.height = (int)(screenHeight * 0.5);
        displayLayout.setLayoutParams(displayParams);
        displayLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        card = new TextView(context);
        card.setWidth(screenWidth);
        displayLayout.addView(card);
        layout.addView(displayLayout);

    }

    private void setDimension(){
        Display screen = ((Activity) context).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        screen.getSize(size);
        this.screenWidth = size.x;
        this.screenHeight = size.y;
    }

    private void setButtons(){
        firstChoice = new Button(context);
        secondChoice = new Button(context);
        thirdChoice = new Button(context);
        forthChoice = new Button(context);

        TableLayout.LayoutParams params = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT);

        params.setMargins(20 ,20 ,20 ,20);

        firstChoice.setLayoutParams(params);
        secondChoice.setLayoutParams(params);
        thirdChoice.setLayoutParams(params);
        forthChoice.setLayoutParams(params);

        layout.addView(firstChoice);
        layout.addView(secondChoice);
        layout.addView(thirdChoice);
        layout.addView(forthChoice);

    }

    private int getRandomIndex(int x){
        Random random = new Random();
        int randomValue = random.nextInt(x);
        while (randomValue == randomIndex){
            randomValue = random.nextInt(x);
        }
        return randomValue;
    }

    private float getRandomDegree(float x){
        Random random = new Random();
        float randomDegree = random.nextFloat() * x - (x / 2);
        while(randomDegree == degree){
            randomDegree = random.nextFloat() * x - (x / 2);
        }
        return randomDegree;
    }

    private void addCard(TextView card){
        getRandomIndex(3);
        getRandomDegree(10);

        AnimationSet dropAnimation = new AnimationSet(false);

        Animation rotateAnimation = new RotateAnimation(0.0f , degree , screenWidth/2 ,
                screenHeight/4);
        rotateAnimation.setDuration(200);

        Animation scaleAnimation = new ScaleAnimation( (float)1.0 , (float)0.85 , (float)1 ,
                (float)0.7 , screenWidth / 2 , screenHeight / 2);
        scaleAnimation.setDuration(200);

        dropAnimation.addAnimation(rotateAnimation);
        dropAnimation.addAnimation(scaleAnimation);
        dropAnimation.setFillAfter(true);

        displayLayout.addView(card);
        card.startAnimation(dropAnimation);

    }

}
