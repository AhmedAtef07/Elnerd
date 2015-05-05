package io.zarda.elnerd.view;

import android.content.Context;
import android.view.View;

import java.util.List;

/**
 * Created by atef & emad on 4 May, 2015.
 */
public interface Viewable {
    void initializeView(Context context , List<View> views);
    void startView();
    void endView();

}
