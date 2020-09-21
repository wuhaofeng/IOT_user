package com.iot.user.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.iot.user.R;
import com.iot.user.utils.view.AutoScaleRunnable;
import com.iot.user.utils.view.ZoomImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.xml.sax.XMLReader;

import java.util.Locale;

public class URLTagHandler implements Html.TagHandler {

    private Context mContext;
    private PopupWindow popupWindow;
    //需要放大的图片
    private ZoomImageView tecent_chat_image;
    private GestureDetector detector;

    private boolean isAutoScale;
    private float mMidScale;
    private float mMaxScale;
    private float mInitScale;

    public URLTagHandler(Context context) {
        mContext = context.getApplicationContext();
        View popView = LayoutInflater.from(context).inflate(R.layout.pub_zoom_popwindow_layout, null);
        tecent_chat_image = (ZoomImageView) popView.findViewById(R.id.image_scale_image);

        popView.findViewById(R.id.image_scale_rll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
            }
        });
        tecent_chat_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
            }
        });
        popupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);// 设置允许在外点击消失
        ColorDrawable dw = new ColorDrawable(0x50000000);
        popupWindow.setBackgroundDrawable(dw);
        iniGestureListener();
        tecent_chat_image.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return detector.onTouchEvent(event);
            }
        });

    }

    private void iniGestureListener() {
        GestureDetector.SimpleOnGestureListener listener = new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onDoubleTap(MotionEvent e) {

                if(isAutoScale == true) {
                    return true;
                }

                float x = e.getX();
                float y = e.getY();

                //�����ǰ���Ŵ�С<2�����ͷŴ�2��
                if(tecent_chat_image.getCurrentScale() < mMidScale) {
                    tecent_chat_image.postDelayed(new AutoScaleRunnable(mMidScale, x, y) , 16);
                    isAutoScale = true;
                }
                else { //�����ǰ���Ŵ�С>2��������С��1��
                    tecent_chat_image.postDelayed(new AutoScaleRunnable(mInitScale, x, y) , 16);
                    isAutoScale = true;
                }

                return true;
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (popupWindow.isShowing()){
                    popupWindow.dismiss();
                }
                return super.onSingleTapConfirmed(e);
            }

        };
        detector = new GestureDetector(mContext, listener);

    }

    @Override
    public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
        // 处理标签<img>
        if (tag.toLowerCase(Locale.getDefault()).equals("img")) {
            // 获取长度
            int len = output.length();
            // 获取图片地址
            ImageSpan[] images = output.getSpans(len - 1, len, ImageSpan.class);
            String imgURL = images[0].getSource();
            // 使图片可点击并监听点击事件
            output.setSpan(new ClickableImage(mContext, imgURL), len - 1, len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    private class ClickableImage extends ClickableSpan {
        private String url;
        private Context context;

        public ClickableImage(Context context, String url) {
            this.context = context;
            this.url = url;
        }

        @Override
        public void onClick(View widget) {

            if (popupWindow.isShowing()){
                popupWindow.dismiss();
            }else {
                // 进行图片点击之后的处理
                popupWindow.showAtLocation(widget, Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);

                final Target target = new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        tecent_chat_image.setImageBitmap(bitmap);
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                        tecent_chat_image.setImageDrawable(errorDrawable);
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                        tecent_chat_image.setImageDrawable(placeHolderDrawable);
                    }
                };
                tecent_chat_image.setTag(target);
                ImageLoad.loadPlaceholder(context, url, target);
            }
        }
    }
}
