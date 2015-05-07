package io.zarda.elnerd.view;

import android.widget.Button;

/**
 * Created by atef & emad on 5 May, 2015.
 */
public interface Game {
    void showSuccess(Button correctButton);
    void showFailure(Button correctButton, Button wrongButton);
    void showNextQuestion();
}
