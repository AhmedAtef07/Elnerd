package io.zarda.elnerd.src;

import io.zarda.elnerd.MainActivity;

/**
 * Created by atef & emad on 4 May, 2015.
 */
public class HomeViewNotifier {

    private ViewManager vm;

    public HomeViewNotifier(ViewManager vm) {
        this.vm = vm;
    }

    public void notifyHomeAnimationFinished() {
        vm.startGameView();
    }

}
