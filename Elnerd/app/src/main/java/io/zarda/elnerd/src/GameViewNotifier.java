package io.zarda.elnerd.src;

import android.content.Context;
import android.os.Vibrator;

/**
 * Created by atef & emad on 4 May, 2015.
 */

public class GameViewNotifier {

    Context context;
    private ViewManager vm;

    public GameViewNotifier(ViewManager vm, Context context) {
        this.vm = vm;
        this.context = context;
    }

    public void notifyShowSuccessFinished() {
        vm.endGameView();
        vm.startQuoteView();
    }

    public void notifyShowFailureFinished() {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(500);
        vm.startScoreView();
    }

}
