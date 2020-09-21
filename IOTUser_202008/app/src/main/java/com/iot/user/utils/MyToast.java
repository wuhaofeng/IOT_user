package com.iot.user.utils;
import android.text.TextUtils;
import android.view.Gravity;
import com.blankj.utilcode.util.ToastUtils;

/**
 * Created by Administrator on 2014/11/4.
 * ToastUtils
 */
public class MyToast {
    public static void showShortToast(String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        try {
            ToastUtils.setGravity(Gravity.CENTER, 0, 0);
            ToastUtils.showShort(text);
        }catch (Exception e){

        }
    }
}
