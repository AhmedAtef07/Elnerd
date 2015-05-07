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

    private Button play;

    @Override
    public void initializeView(Context context , List<View> views) {
        this.context = context;
        play = (Button) (views.get(0));
        setLayout();
        setButtons();
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
}
