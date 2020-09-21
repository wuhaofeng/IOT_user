package com.iot.user.utils;

import android.content.Context;
import android.text.style.ClickableSpan;
import android.view.View;

import androidx.annotation.NonNull;

public class ClickableImage extends ClickableSpan {

    private String url;
    private Context context;

    public ClickableImage(Context context, String url) {
        this.context = context;
        this.url = url;
    }

    @Override
    public void onClick(@NonNull View widget) {

    }
}
