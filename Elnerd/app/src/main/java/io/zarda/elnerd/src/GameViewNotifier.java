package io.zarda.elnerd.src;

/**
 * Created by atef & emad on 4 May, 2015.
 */

public class GameViewNotifier {
    private ViewManager vm;

    public GameViewNotifier(ViewManager vm) {
        this.vm = vm;
    }

    public void notifyShowSuccessFinished() {
        vm.endGameView();
        vm.startQuoteView();
    }

    public void notifyShowFailureFinished() {
//        vm.endGameView();
//        vm.startHomeView();
        vm.startScoreView();
    }

}
