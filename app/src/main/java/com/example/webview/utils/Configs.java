package com.example.webview.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

public class Configs {

    public static final int Horizontal = 0;
    public static final int Circular = 1;

    public static final int Bottom = 0;
    public static final int Center = 1;

    public static final boolean No = false;
    public static final boolean Yes = true;

    private Activity context;
    private int loadingStyle;
    private int downloadStyle;
    private boolean fullscreen;

    public Configs(Activity context) {
        this.context = context;
        Do_you_want_to_enable_fullscreen(Yes);

        What_is_the_loading_style_you_want(Horizontal);

        What_is_the_download_style_you_want(Bottom);

    }

    public int getLoadingStyle() {
        return loadingStyle;
    }

    public boolean isFullscreen() {
        return fullscreen;
    }

    private void What_is_the_loading_style_you_want(int style) {
        loadingStyle = style;
    }

    private void Do_you_want_to_enable_fullscreen(boolean answer) {
        fullscreen = answer;
    }

    private void What_is_the_download_style_you_want(int s) {
        downloadStyle = s;
    }
}
