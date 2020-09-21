package com.iot.user.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.iot.user.app.IOTApplication;

/**
 * Created by maning on 16/6/22.
 */
public class DialogUtils {

    public static MaterialDialog showMyDialog(Context context, String title, String content, String positiveBtnText, String negativeBtnText, final OnDialogClickListener onDialogClickListener) {
        if (context==null){
            context= IOTApplication.getIntstance().mContext;
            LogUtil.e("Dialog Context已经被释放");
        }
        MaterialDialog materialDialog = new MaterialDialog.Builder(context)
                .title(title)
                .content(content)
                .positiveText(positiveBtnText)
                .negativeText(negativeBtnText)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (onDialogClickListener != null) {
                            onDialogClickListener.onConfirm();
                        }
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (onDialogClickListener != null) {
                            onDialogClickListener.onCancel();
                        }
                    }
                }).build();
        materialDialog.setCancelable(true);
        try {
//            if (((Activity) context).hasWindowFocus()) {
                materialDialog.show();
//            }
        }catch (Exception e){

        }
        return materialDialog;
    }

    public static MaterialDialog materialConfirmDialog = null;
    public static MaterialDialog showMyConfirmDialog(Context context, String title, String content, String positiveBtnText, final OnDialogClickListener onDialogClickListener) {
        if (context==null){
            context= IOTApplication.getIntstance().mContext;
        }
        if(materialConfirmDialog!=null &&materialConfirmDialog.isShowing()){
            return materialConfirmDialog;
        }
        materialConfirmDialog = new MaterialDialog.Builder(context)
                .title(title)
                .content(content)
                .positiveText(positiveBtnText)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (onDialogClickListener != null) {
                            onDialogClickListener.onConfirm();
                        }
                    }
                }).build();
        materialConfirmDialog.setCancelable(false);
        try {
//            if (((Activity) context).hasWindowFocus()) {
                materialConfirmDialog.show();
//            }
        }catch (Exception e){

        }
        return materialConfirmDialog;
    }

    public static MaterialDialog showMustConfirmDialog(Context context, String title, String content, String positiveBtnText, final OnDialogClickListener onDialogClickListener) {
        if (context==null){
            context= IOTApplication.getIntstance().mContext;
        }
        MaterialDialog confirmDialog = new MaterialDialog.Builder(context)
                .title(title)
                .content(content)
                .positiveText(positiveBtnText)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (onDialogClickListener != null) {
                            onDialogClickListener.onConfirm();
                        }
                    }
                }).build();
        confirmDialog.setCancelable(false);
        try {
//            if (((Activity) context).hasWindowFocus()) {
                confirmDialog.show();
//            }
        }catch (Exception e){

        }
        return confirmDialog;
    }
    public interface OnDialogClickListener {
        void onConfirm();
        void onCancel();
    }

    public static MaterialDialog showMyListDialog(final Context context,String title, int contents, final OnDialogListCallback onDialogListCallback){
        MaterialDialog materialDialog = new MaterialDialog.Builder(context)
                .title(title)
                .items(contents)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        onDialogListCallback.onSelection(dialog,itemView,position,text);
                    }
                }).build();
        try {
//            if (((Activity) context).hasWindowFocus()) {
                materialDialog.show();
//            }
        }catch (Exception e){

        }
        return materialDialog;
    }

    public interface OnDialogListCallback {
        void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text);
    }

    public static View showDialog(Dialog dialog, Activity context, int layout, int gravity, double widthInt,double heightInt, boolean outCanClick){
//        dialog = new Dialog(context, R.style.dialog);
        View dialogView = LayoutInflater.from(context).inflate(layout,null);
        //获得dialog的window窗口
        Window window = dialog.getWindow();
        //设置dialog在屏幕底部
        window.setGravity(gravity);
        //设置dialog弹出时的动画效果，从屏幕底部向上弹出
//        window.setWindowAnimations(R.style.dialog);
        window.getDecorView().setPadding(0, 0, 0, 0);
        //获得window窗口的属性
        WindowManager.LayoutParams lp = window.getAttributes();
        //设置窗口宽度为充满全屏
        WindowManager windowManager = context.getWindowManager();
        lp.width = (int) (windowManager.getDefaultDisplay().getWidth() * widthInt);
        //设置窗口高度为包裹内容
        lp.height = (int) (windowManager.getDefaultDisplay().getWidth() * heightInt);
        //将设置好的属性set回去
        window.setAttributes(lp);
        if (outCanClick){
            dialog.setCanceledOnTouchOutside(true);
        }else {
            dialog.setCanceledOnTouchOutside(false);
            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    return true;
                }
            });
        }
        dialog.setContentView(dialogView);
        dialog.show();
        return dialogView;
    }

    public static View showDialog(Dialog dialog, Activity context, int layout, int gravity, double widthInt, boolean outCanClick){
//        dialog = new Dialog(context, R.style.dialog);
        View dialogView = LayoutInflater.from(context).inflate(layout,null);
        //获得dialog的window窗口
        Window window = dialog.getWindow();
        //设置dialog在屏幕底部
        window.setGravity(gravity);
        //设置dialog弹出时的动画效果，从屏幕底部向上弹出
//        window.setWindowAnimations(R.style.dialog);
        window.getDecorView().setPadding(0, 0, 0, 0);
        //获得window窗口的属性
        WindowManager.LayoutParams lp = window.getAttributes();
        //设置窗口宽度为充满全屏
        WindowManager windowManager = context.getWindowManager();
        lp.width = (int) (windowManager.getDefaultDisplay().getWidth() * widthInt);
        //设置窗口高度为包裹内容
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //将设置好的属性set回去
        window.setAttributes(lp);
        if (outCanClick){
            dialog.setCanceledOnTouchOutside(true);
        }else {
            dialog.setCanceledOnTouchOutside(false);
            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    return true;
                }
            });
        }
        dialog.setContentView(dialogView);
        dialog.show();
        return dialogView;
    }

}
