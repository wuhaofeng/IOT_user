package com.iot.user.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

public class URLImageParser implements Html.ImageGetter {

    TextView textView;

    public URLImageParser(TextView textView) {
        this.textView = textView;
    }

    @Override
    public Drawable getDrawable(String source) {
        final URLDrawable urlDrawable = new URLDrawable();

        Glide.with(textView).asBitmap().load(source).fitCenter().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                urlDrawable.bitmap = resource;
//                urlDrawable.setBounds(0, 0, resource.getWidth(), resource.getHeight());

                float width = textView.getWidth();
                if (resource.getWidth() > width) {
                    float scale = width / resource.getWidth();
                    int afterWidth = (int) (resource.getWidth() * scale);
                    int afterHeight = (int) (resource.getHeight() * scale);
                    urlDrawable.setBounds(0, 0, resource.getWidth(), resource.getHeight());
                    try {
                        urlDrawable.setBitmap(resizeBitmap(resource, afterWidth, afterHeight));
                    }catch (Exception e){

                    }

                } else {
                    urlDrawable.setBounds(0, 0, resource.getWidth(), resource.getHeight());
                    urlDrawable.setBitmap(resource);
                }

                textView.invalidate();
                textView.setText(textView.getText());
            }
        });

        return urlDrawable;
    }


    public static Bitmap resizeBitmap(Bitmap bitmap, int w, int h)
    {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        float scaleWidth = ((float) w) / width;
        float scaleHeight = ((float) h) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        return Bitmap.createBitmap(bitmap, 0, 0, width,
                height, matrix, true);
    }

}




class URLDrawable extends BitmapDrawable {
    protected Bitmap bitmap;

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public void draw(Canvas canvas) {
        if (bitmap != null) {
            canvas.drawBitmap(bitmap, 0, 0, getPaint());
        }
    }

}