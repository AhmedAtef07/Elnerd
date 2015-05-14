package io.zarda.elnerd.src;

import android.view.View;

/**
 * Created by atef & emad on 4 May, 2015.
 */
public class ScoreViewNotifier {

    private ViewManager vm;

    public ScoreViewNotifier(ViewManager vm) {
        this.vm = vm;
    }

    public void notifyDoneLoad(View scoreView) {
        vm.setScoreView(scoreView);
    }


}

