package io.zarda.elnerd.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.List;

import io.zarda.elnerd.R;

/**
 * Created by atef & emad on 4 May, 2015.
 * Implementing by magdy.
 */
public class HomeView implements Viewable {

    Context context;

    private FrameLayout mainLayout;
    private TableLayout layout;

    private TextView textView1;
    private TextView textView2;

    private Button play;

    @Override
    public void initializeView(Context context , List<View> views) {
        this.context = context;
        play = (Button) (views.get(0));
        textView1 = (TextView) (views.get(1));
        textView2 = (TextView) (views.get(2));
        setLayout();
        setButtons();
        setTextViews();
    }

    @Override
    public void startView() {
//        mainLayout.addView(layout);
        ((Activity)context).setContentView(mainLayout);
    }

    @Override
    public void endView() {
        ((ViewGroup) mainLayout.getParent()).removeAllViews();
    }

    private  void setLayout(){
        mainLayout = new FrameLayout(context);
        layout = new TableLayout(context);
        mainLayout.addView(layout);
        layout.setGravity(Gravity.CENTER);
    }

    private void setButtons(){
        play.setBackground(context.getResources().getDrawable(R.drawable.display3));
        play.setTextColor(Color.parseColor("#ecf0f1"));
        layout.addView(play);
        play.setGravity(Gravity.CENTER);
    }

    private void setTextViews(){
        textView1.setBackground(context.getResources().getDrawable(R.drawable.display1));
        textView2.setBackground(context.getResources().getDrawable(R.drawable.display2));
        play.setTextColor(Color.parseColor("#ecf0f1"));
        layout.addView(textView1);
        layout.addView(textView2);
        textView1.setGravity(Gravity.CENTER);
        textView2.setGravity(Gravity.CENTER);
    }
}
