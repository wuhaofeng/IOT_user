package com.iot.user.utils;

import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.iot.user.app.IOTApplication;
import com.iot.user.skin.SkinManager;

/**
 * Created by maning on 16/1/18.
 * <p/>
 * 提示框
 */
public class MySnackbar {

    public static void makeSnackBarBlack(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        int currentSkinType = SkinManager.getCurrentSkinType(IOTApplication.getIntstance().getApplicationContext());
        if (SkinManager.THEME_DAY == currentSkinType) {
            ColoredSnackbar.defaultInfo(snackbar).show();
        } else {
            ColoredSnackbar.defaultInfoNight(snackbar).show();
        }
    }

    public static void makeSnackBarRed(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        ColoredSnackbar.alert(snackbar).show();
    }

    public static void makeSnackBarBlue(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        ColoredSnackbar.info(snackbar).show();
    }

    public static void makeSnackBarOrange(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        ColoredSnackbar.warning(snackbar).show();
    }

    public static void makeSnackBarGreen(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        ColoredSnackbar.confirm(snackbar).show();
    }

}
