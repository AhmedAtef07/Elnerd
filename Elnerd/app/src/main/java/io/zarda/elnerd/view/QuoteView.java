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
import android.widget.ScrollView;
import android.widget.Space;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

import io.zarda.elnerd.R;

/**
 * Created by ahmed on 5/12/2015.
 */
public class QuoteView implements Viewable, Game {

    Context context;

    TextView quote;
    TextView bookName;
    int time;

    TextView bar;

    int screenWidth;
    int screenHeight;

    LinearLayout mainLayout;

    String[] colors = {"#e74c3c", "#2ecc71", "#3498db", "#1abc9c", "#9b59b6", "#f1c40f",
            "#e67e22"};

    @Override
    public void initializeView(Context context, List<View> views) {
        this.context = context;
        //view
        quote = (TextView) views.get(0);
        bookName = (TextView) views.get(1);

        Display screen = ((Activity) context).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        screen.getSize(size);
        this.screenWidth = size.x;
        this.screenHeight = size.y;

        intializeLayout();
    }

    @Override
    public void startView() {
        if (mainLayout != null) {
            mainLayout.setBackgroundColor(Color.parseColor(colors[new Random().nextInt(7)]));
        }
        ((Activity) context).setContentView(mainLayout);
        Animation goDown = new TranslateAnimation(0, 0, -screenHeight, 0);
        goDown.setDuration(500);
        mainLayout.startAnimation(goDown);
    }


    @Override
    public void endView() {
        Animation goDown = new TranslateAnimation(0, 0, 0, -screenHeight);
        goDown.setDuration(500);
        mainLayout.startAnimation(goDown);
    }

    @Override
    public void showSuccess(Button correctButton) {

    }

    @Override
    public void showFailure(Button correctButton, Button wrongButton) {

    }

    @Override
    public void showNextQuestion() {

    }

    @Override
    public void setTime(int msTime) {
        this.time = msTime;
        System.out.println("animation" + time);
        Animation timeAnimation = new TranslateAnimation(0, -screenWidth, 0, 0);
        timeAnimation.setDuration(time);
        bar.startAnimation(timeAnimation);
    }

    private void intializeLayout() {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(),
                "fonts/JF Flat regular.ttf");
        mainLayout = new LinearLayout(context);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setGravity(Gravity.RIGHT);
        mainLayout.setBackgroundColor(Color.parseColor(colors[new Random().nextInt(7)]));
        //scrollView
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setGravity(Gravity.RIGHT);
        //time bar
        RelativeLayout.LayoutParams barParam = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        bar = new TextView(context);
        barParam.setLayoutDirection(Gravity.TOP);
        bar.setLayoutParams(barParam);
        barParam.width = screenWidth;
        barParam.height = (int) (0.028153 * screenHeight);
        barParam.setMargins(0, 0, 0, (int) (0.185185 * screenWidth));
        bar.setBackgroundColor(Color.parseColor("#ffffff"));
        bar.setPadding(0, 0, 0, (int) (0.370370 * screenWidth));
        bar.setLayoutParams(barParam);
        mainLayout.addView(bar);
        //space
        Space space0 = new Space(context);
        space0.setMinimumHeight((int) (0.11261 * screenHeight));
        linearLayout.addView(space0);
        //quote
        quote.setBackground(context.getResources().getDrawable(R.drawable.display3));
        quote.setTextDirection(View.TEXT_DIRECTION_RTL);
        quote.setTextSize((float) (53.333 * screenWidth / screenHeight));
        quote.setTypeface(typeface);
        quote.setGravity(Gravity.CENTER);
        quote.setTextColor(Color.parseColor("#ecf0f1"));
        linearLayout.addView(quote);
        //bookName
        bookName.setBackground(context.getResources().getDrawable(R.drawable.display3));
        RelativeLayout.LayoutParams bookNameParam = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        bookName.setLayoutParams(bookNameParam);
        bookName.setTextDirection(View.TEXT_DIRECTION_RTL);
        bookName.setTextSize((float) (36.6667 * screenWidth / screenHeight));
        bookName.setTextColor(Color.parseColor("#ecf0f1"));
        bookName.setTypeface(typeface);
        linearLayout.addView(bookName);
        ScrollView scrollView = new ScrollView(context);
        scrollView.addView(linearLayout);
        mainLayout.addView(scrollView);
    }

    public void showQuote() {
        //TO-TO
    }

}
