package com.iot.user.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.math.BigDecimal;
import java.text.NumberFormat;

/**
 * Author:Tiger
 * <p>
 * CrateTime:2017-02-04 14:18
 * <p>
 * Description:使用到的工具类
 */
public class Util {

    public static int dip2px(Context context, float dipValue) {
        return (int) (dipValue * context.getResources().getDisplayMetrics().density);
    }

    public static String getShoppingSelectYear(String year) {
        String yearStr="";
        switch (year){
            case "1":yearStr="续费一年";break;
            case "2":yearStr="续费两年";break;
            case "3":yearStr="续费三年";break;
            case "4":yearStr="续费四年";break;
            case "5":yearStr="续费五年";break;
            case "6":yearStr="续费六年";break;
            case "7":yearStr="续费七年";break;
            case "8":yearStr="续费八年";break;
            case "9":yearStr="续费九年";break;
            case "10":yearStr="续费十年";break;
        }
        return yearStr;
    }
    /***Double转化为String,保留两位有效数字*/
    public  static String getTwoNumFloatWith(Object f,boolean isHaveZero){
        if (isHaveZero) {
            BigDecimal b = new BigDecimal(String.valueOf(f));
            return b.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        }
        BigDecimal b = new BigDecimal(String.valueOf(f));
        return b.setScale(0, BigDecimal.ROUND_HALF_UP).toString();
    }


    public static String getOrderListStatus(String status) {
        String statusStr="";
        switch (status){
            case "1":statusStr="待支付";break;
            case "2":statusStr="已支付";break;
            default:statusStr="已取消";break;
        }
        return statusStr;
    }

    public static String getDevStatusStatus(String status) {
        String statusStr="";
        switch (status){
            case "0":statusStr="设备正常";   break;
            case "1":statusStr="设备故障";   break;
            case "2":statusStr="设备报警";   break;
            case "3":statusStr="设备报警";   break;
            case "4":statusStr="设备自检";   break;
            case "5":statusStr="设备恢复正常";   break;
            case "6":statusStr="设备消音";   break;
            case "7":statusStr="我知道了";   break;
            default:statusStr="设备正常";break;
        }
        return statusStr;
    }
    public static String getMemberStatusStatus(String status) {
        String statusStr="";
        switch (Double.valueOf(status).intValue()){
            case 1:statusStr="管理员";   break;
            case 2:statusStr="待接受";   break;
            default:statusStr="成员";break;
        }
        return statusStr;
    }

    //根据宽高比设置控件宽高, 如设置宽高比为5:4，那么widthRatio为5，heightRatio为4
    public static void setWidthHeightWithRatio(View view, int width, int widthRatio, int heightRatio) {
        if (width <= 0) width = view.getWidth();
        int height = width * heightRatio / widthRatio;
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams != null) {
            layoutParams.height = height;
            layoutParams.width = width;
            view.setLayoutParams(layoutParams);
        }
    }

    public static String getDevNodeType(String status) {
        String statusStr="";
        switch (Double.valueOf(status).intValue()){
            case 3:statusStr="联动模块";   break;
            case 2:statusStr="中继器";   break;
            default:statusStr="传感器";break;
        }
        return statusStr;
    }

    public static String getDevNodeStatus(String status) {
        String statusStr="";
        switch (Double.valueOf(status).intValue()){
            case 1:statusStr="开启";   break;
            default:statusStr="屏蔽";break;
        }
        return statusStr;
    }

    public static String getNodeProductType(String type) {
        String statusStr="";
        switch (Double.valueOf(type).intValue()){
            case 1:statusStr="风机";   break;
            case 2:statusStr="电磁阀";   break;
            default:statusStr="报警灯";break;
        }
        return statusStr;
    }

    public static String getNodeProductStatus(String status) {
        String statusStr="";
        switch (Double.valueOf(status).intValue()){
            case 1:statusStr="开启";   break;
            default:statusStr="关闭";break;
        }
        return statusStr;
    }

}
