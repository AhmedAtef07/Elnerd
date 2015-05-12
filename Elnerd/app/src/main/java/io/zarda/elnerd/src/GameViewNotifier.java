package io.zarda.elnerd.src;

import io.zarda.elnerd.MainActivity;

/**
 * Created by atef & emad on 4 May, 2015.
 */
public class GameViewNotifier {
    private ViewManager vm;
    private MainActivity ma;

    public GameViewNotifier(ViewManager vm, MainActivity ma) {
        this.vm = vm;
        this.ma = ma;
    }

    public void notifyShowSuccessFinished() {
        vm.endGameView();
        vm.startQuoteView();
    }

    public void notifyShowFailureFinished() {
        vm.endGameView();
        vm.startHomeView();
    }

}
