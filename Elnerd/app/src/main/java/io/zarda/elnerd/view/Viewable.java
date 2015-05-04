package io.zarda.elnerd.view;

import android.content.Context;

/**
 * Created by atef & emad on 4 May, 2015.
 */
public interface Viewable {
    void initializeView(Context context);
    void startView();
    void endView();

}
