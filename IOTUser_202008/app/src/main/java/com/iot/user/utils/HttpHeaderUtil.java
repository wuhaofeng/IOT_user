package com.iot.user.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;
import com.iot.user.app.IOTApplication;
/**
 * @qingchen http协议头信息工具类
 */

public class HttpHeaderUtil {

    public static String PRODNAME = "IOT_APP";
    public static String OSNAME = "ANDROID";
    public static String CONTENTTYPE = "application/json";
    public static String APINAME = "v1";

    public static String getProdname(){
        return PRODNAME;
    }

    public static String getProdversion(Context context){
        PackageInfo packInfo = null;
        try {
            PackageManager packageManager = context.getPackageManager();
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        } catch (Exception e) {
            return "1.0.0";
        }
        return packInfo.versionName;
    }

    public static String getOsname(){
        return OSNAME;
    }
    public static String getOsversion(){
        return android.os.Build.VERSION.RELEASE;
    }

    public static String getDevicename(){
        String brand = android.os.Build.BRAND;
        if(brand == null){
            brand = "";
        }else if("HONOR".equals(brand.toUpperCase())){
            brand = "HUAWEI";
        }
        return brand;
    }

    public static String getImei(Context context){
        String imei = "";
        try{
            boolean permission = (PackageManager.PERMISSION_GRANTED == IOTApplication.getIntstance().getPackageManager()
                    .checkPermission("android.permission.READ_PHONE_STATE", IOTApplication.getIntstance().getPackageName()));
            if(permission){
                imei = ((TelephonyManager) context.getSystemService(
                        Context.TELEPHONY_SERVICE)).getDeviceId();
            }
        }catch (Exception e){
        }

        if(StringUtils.isBlank(imei))
            return "";

        if (StringUtils.isBlank(imei)){
            imei="";
        }
        return imei;
    }

    public static String getContenttype() {
        return CONTENTTYPE;
    }

    public static String getAPINAME() {
        return APINAME;
    }
}
