package io.zarda.elnerd.src;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.zarda.elnerd.model.Question;
import io.zarda.elnerd.view.GameView;

/**
 * Created by atef & emad on 4 May, 2015.
 */
public class ViewManager {

    private final List<View> views;

    private Context context;

    public ViewManager(Context context) {
        this.context = context;
        ArrayList<View> viewsList = new ArrayList<>();

        viewsList.add(new TextView(context));
        for (int i = 0; i < 4; ++i) {
            viewsList.add(new Button(context));
        }

        views = Collections.unmodifiableList(viewsList);
    }

    public void showQuestion(Question question) {
        GameView gameView = new GameView();
        ((TextView)views.get(0)).setText(question.getHeader());
        for (int i = 0; i < 4; ++i) {
            ((Button)views.get(i + 1)).setText(question.getChoices().get(i));
            ((Button)views.get(i + 1)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    click(v);
                }
            });

            if (i == question.getCorrectIndex()) {
                ((Button)views.get(i + 1)).setTag(true);
            }
            else {
                ((Button)views.get(i + 1)).setTag(false);
            }
        }
        gameView.initializeView(context, views);
        gameView.startView();
    }

    public void click(View v) {
        if ((boolean)v.getTag()) {
            System.out.println("True answer Clicked");
        }
        else {
            System.out.println("False answer Clicked");

        }
    }

}

