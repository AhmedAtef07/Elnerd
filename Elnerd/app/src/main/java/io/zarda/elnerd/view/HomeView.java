package io.zarda.elnerd.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;

import java.util.List;

import io.zarda.elnerd.R;

/**
 * Created by atef & emad on 4 May, 2015.
 * Implementing by magdy.
 */
public class HomeView implements Viewable {

    Context context;

    private FrameLayout mainLayout;

    private Button play;

    @Override
    public void initializeView(Context context , List<View> views) {
        this.context = context;
        play = (Button) (views.get(0));
        setFrame();
        setLayout();
        setButtons();
    }

    @Override
    public void startView() {
        ((Activity)context).setContentView(mainLayout);
    }

    @Override
    public void endView() {
        ((ViewGroup) mainLayout.getParent()).removeAllViews();
    }

    private void setFrame(){
        ((Activity) context).getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ((Activity) context).requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    private  void setLayout(){
        mainLayout = new FrameLayout(context);
    }

    private void setButtons(){
        play.setBackground(context.getResources().getDrawable(R.drawable.display3));
        play.setTextColor(Color.parseColor("#ecf0f1"));
        mainLayout.addView(play);
        play.setGravity(Gravity.CENTER);
    }
}
