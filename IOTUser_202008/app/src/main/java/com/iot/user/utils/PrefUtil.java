package com.iot.user.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.iot.user.app.IOTApplication;
import com.iot.user.constant.Constants;
import com.iot.user.http.net.RetrofitClient;
import com.iot.user.ui.model.main.UnitFamilyModel;

import java.util.HashMap;
import java.util.Map;

/**
 * 首选项工具类
 * @author
 * @version 1.0
 */
public class PrefUtil {

    private static Map<String, SharedPreferences> settingsMap = new HashMap<>();
    public static final String sharedPrefsFile = "com.iot.user_preferences";

    public static synchronized SharedPreferences getPrefSettings(Context context) {
        if (!settingsMap.containsKey(sharedPrefsFile)) {
            settingsMap.put(sharedPrefsFile, context.getSharedPreferences(sharedPrefsFile, Context.MODE_PRIVATE));
        }
        return settingsMap.get(sharedPrefsFile);
    }


    public static synchronized SharedPreferences getPrefSettings(Context context, String sharedPrefsFile) {
        if (!settingsMap.containsKey(sharedPrefsFile)) {
            settingsMap.put(sharedPrefsFile, context.getSharedPreferences(sharedPrefsFile, Context.MODE_PRIVATE));
        }
        return settingsMap.get(sharedPrefsFile);
    }

    /**
     * 设定登录账号uid
     * @param loginAccount
     * @param context
     */
    public static synchronized void setLoginAccountUid(String loginAccount, Context context) {
        getPrefSettings(context).edit().putString("loginAccountuid", loginAccount).commit();
    }

    /**
     * 获取登录账号uid
     * @param context
     * @return
     */
    public static synchronized String getLoginAccountUid(Context context) {
        return getPrefSettings(context).getString("loginAccountuid", "");
    }

    /**
     * 设定登录账号
     * @param loginAccount
     * @param context
     */
    public static synchronized void setLoginAccount(String loginAccount, Context context) {
        getPrefSettings(context).edit().putString("loginAccount", loginAccount).commit();
    }

    /**
     * 获取登录账号
     * @param context
     * @return
     */
    public static synchronized String getLoginAccount(Context context) {
        return getPrefSettings(context).getString("loginAccount", "");
    }
    /**
     * 设定账号vip等级
     * @param level
     * @param context
     */
    public static synchronized void setVIPLevel(String level, Context context) {
        getPrefSettings(context).edit().putString("level", level).commit();
    }

    /**
     * 获取账号vip等级
     * @param context
     * @return
     */
    public static synchronized String getVIPLevel(Context context) {
        return getPrefSettings(context).getString("level", "");
    }
    /**
     * 设定VIP到期时间
     * @param expire_time
     * @param context
     */
    public static synchronized void setExpireTime(String expire_time, Context context) {
        getPrefSettings(context).edit().putString("expire_time", expire_time).commit();
    }

    /**
     * 获取VIP到期时间
     * @param context
     * @return
     */
    public static synchronized String getExpireTime(Context context) {
        return getPrefSettings(context).getString("expire_time", "");
    }

    public static synchronized void setPushmark(String push_mark, Context context) {
        getPrefSettings(context).edit().putString("push_mark", push_mark).commit();
    }

    public static synchronized String getPushmark(Context context) {
        return getPrefSettings(context).getString("push_mark", "0");
    }

    public static synchronized void setSmsmark(String sms_mark, Context context) {
        getPrefSettings(context).edit().putString("sms_mark", sms_mark).commit();
    }

    public static synchronized String getSmsmark(Context context) {
        return getPrefSettings(context).getString("sms_mark", "0");
    }

    public static synchronized void setPhonemark(String phone_mark, Context context) {
        getPrefSettings(context).edit().putString("phone_mark", phone_mark).commit();
    }

    public static synchronized String getPhonemark(Context context) {
        return getPrefSettings(context).getString("phone_mark", "0");
    }
    /**
     * 设定登录账号
     * @param pwd
     * @param context
     */
    public static synchronized void setLoginPwd(String pwd, Context context) {
        getPrefSettings(context).edit().putString("loginpwd", pwd).commit();
    }

    /**
     * 获取登录账号
     * @param context
     * @return
     */
    public static synchronized String getLoginPwd(Context context) {
        return getPrefSettings(context).getString("loginpwd", "");
    }

    /**
     * 设定记住密码
     * @param isRemember
     * @param context
     */
    public static synchronized void setIsRememberPwd(boolean isRemember, Context context) {
        getPrefSettings(context).edit().putBoolean("isRemember", isRemember).commit();
    }

    /**
     * 获取是否记住密码
     * @param context
     * @return
     */
    public static synchronized boolean getIsRememberPwd(Context context) {
        return getPrefSettings(context).getBoolean("isRemember", false);
    }

    /**
     * 设定昵称
     * @param nickname
     * @param context
     */
    public static synchronized void setNickname(String nickname,Context context) {
        getPrefSettings(context).edit().putString("nickname", nickname).commit();
    }

    /**
     * 获取昵称
     * @param context
     * @return
     */
    public static synchronized String getNickname(Context context) {
        return getPrefSettings(context).getString("nickname", "");
    }
    /**
     * 设定用户名
     * @param username
     * @param context
     */
    public static synchronized void setUsername(String username,Context context) {
        getPrefSettings(context).edit().putString("username", username).commit();
    }

    /**
     * 获取用户名
     * @param context
     * @return
     */
    public static synchronized String getUsername(Context context) {
        return getPrefSettings(context).getString("username", "");
    }

    /**
     * 设定手机号
     * @param phone
     * @param context
     */
    public static synchronized void setPhone(String phone,Context context) {
        getPrefSettings(context).edit().putString("phone", phone).commit();
    }

    /**
     * 获取手机号
     * @param context
     * @return
     */
    public static synchronized String getPhone(Context context) {
        return getPrefSettings(context).getString("phone", "");
    }

    /**
     * 获取登陆身份认证
     * @param loginToken 身份token
     */
    public static synchronized void setLoginToken(String loginToken,Context context) {
        getPrefSettings(context).edit().putString("loginToken", loginToken).commit();
    }

    /**
     * 设置登录身份认证
     * @return 身份token ,不存在返回字符
     */
    public static synchronized String getLoginToken(Context context) {
        return getPrefSettings(context).getString("loginToken", "");
    }

    /**
     * 是否开启铃声报警
     * @param ismusic
     * @param context
     */
    public static synchronized void setIsMusicMode(boolean ismusic,Context context) {
        getPrefSettings(context).edit().putBoolean("ismusic", ismusic).commit();
    }

    public static synchronized boolean getIsMusicMode(Context context) {
        return getPrefSettings(context).getBoolean("ismusic", true);
    }
    /**
     * 是否开启震动报警
     * @param isShock
     * @param context
     */
    public static synchronized void setIsShockMode(boolean isShock,Context context) {
        getPrefSettings(context).edit().putBoolean("isShock", isShock).commit();
    }

    public static synchronized boolean getIsShockMode(Context context) {
        return getPrefSettings(context).getBoolean("isShock", true);
    }

    /**
     * 是否开启推送通知
     * @param isNotify
     * @param context
     */
    public static synchronized void setIsNotifyMode(boolean isNotify,Context context) {
        getPrefSettings(context).edit().putBoolean("isNotify", isNotify).commit();
    }

    public static synchronized boolean getIsNotifyMode(Context context) {
        return getPrefSettings(context).getBoolean("isNotify", true);
    }

    /**
     * 是否短信通知
     * @param isSMS
     * @param context
     */
    public static synchronized void setIsSMSMode(boolean isSMS,Context context) {
        getPrefSettings(context).edit().putBoolean("isSMS", isSMS).commit();
    }

    public static synchronized boolean getIsSMSMode(Context context) {
        return getPrefSettings(context).getBoolean("isSMS", true);
    }

    /**
     * 是否电话模式
     * @param IsPhone
     * @param context
     */
    public static synchronized void setIsPhoneMode(boolean IsPhone,Context context) {
        getPrefSettings(context).edit().putBoolean("IsPhone", IsPhone).commit();
    }

    public static synchronized boolean getIsPhoneMode(Context context) {
        return getPrefSettings(context).getBoolean("IsPhone", true);
    }


    public static synchronized void setCurrentLat(String lat,Context context) {
        getPrefSettings(context).edit().putString("lat", lat).commit();
    }

    public static synchronized String getCurrentLat(Context context) {
        return getPrefSettings(context).getString("lat", "31.238068");
    }

    public static synchronized void setCurrentLon(String lon,Context context) {
        getPrefSettings(context).edit().putString("lon", lon).commit();
    }

    public static synchronized String getCurrentLon(Context context) {
        return getPrefSettings(context).getString("lon", "121.501654");
    }

    public static synchronized boolean getIsFirstLogin(Context context) {
        return getPrefSettings(context).getBoolean("isFirstLogin", true);
    }

    public static synchronized void setIsFirstLogin(boolean logined,Context context) {
        getPrefSettings(context).edit().putBoolean("isFirstLogin", logined).commit();
    }

    public static synchronized boolean getIsInApp(Context context) {
        return getPrefSettings(context).getBoolean("isInApp", false);
    }

    public static synchronized void setIsInApp(boolean isIn,Context context) {
        getPrefSettings(context).edit().putBoolean("isInApp", isIn).commit();
    }

    /**
     * 配置服务器ip和port
     */
    public static synchronized String getServerIP(Context context) {
        String serverIP=getPrefSettings(context).getString("serverIP", RetrofitClient.DEFAULT_IP);
        return serverIP;
    }

    public static synchronized void setServerIP(String serverIP,Context context) {
        getPrefSettings(context).edit().putString("serverIP", serverIP).commit();
    }
    public static synchronized String getServerPort(Context context) {
        return getPrefSettings(context).getString("serverPort", RetrofitClient.DEFAULT_PORT);
    }

    public static synchronized void setServerPort(String serverPort,Context context) {
        getPrefSettings(context).edit().putString("serverPort", serverPort).commit();
    }

    public static synchronized String getServerVersion(Context context) {
        return getPrefSettings(context).getString("serverVersion", RetrofitClient.DEFAULT_VERSION);
    }

    public static synchronized void setServerVersion(String serverVersion,Context context) {
        getPrefSettings(context).edit().putString("serverVersion", serverVersion).commit();
    }

    public static synchronized int getNewVersion(Context context) {
        return getPrefSettings(context).getInt("newVersion", 1);
    }

    public static synchronized void setNewVersion(int newVersion,Context context) {
        getPrefSettings(context).edit().putInt("newVersion", newVersion).commit();
    }
/**本地存入的版本**/
    public static synchronized int getSaveVersion(Context context) {
        return getPrefSettings(context).getInt("saveVersion", 1);
    }

    public static synchronized void setSaveVersion(int saveVersion,Context context) {
        getPrefSettings(context).edit().putInt("saveVersion", saveVersion).commit();
    }

    /***三方支付的时候保存一个tradeno***/
    public static synchronized String getSaveTradeNo(Context context) {
        return getPrefSettings(context).getString("saveTradeNo", "");
    }

    public static synchronized void setSaveTradeNo(String saveVersion,Context context) {
        getPrefSettings(context).edit().putString("saveTradeNo", saveVersion).commit();
    }
    public static synchronized boolean getIsReturnHome(Context context) {
        return getPrefSettings(context).getBoolean("isReturnHome", false);
    }

    public static synchronized void setIsReturnHome(boolean isReturnHome,Context context) {
        getPrefSettings(context).edit().putBoolean("isReturnHome", isReturnHome).commit();
    }


    /***Unit**/
    /**App选择加载的页面的版本 0为初始版本，1为添加了燃气图的版本，2为Unit商用版本**/
    public static synchronized int getAppSelectVersion(Context context) {
        return getPrefSettings(context).getInt("appSelectVersion", 1);
    }

    public static synchronized void setAppSelectVersion(int saveVersion,Context context) {
        getPrefSettings(context).edit().putInt("appSelectVersion", saveVersion).commit();
    }


    /***Unit查看用户192设备权限  0未拥有 1只读 2可读可控制**/
    public static synchronized int getUnitOperationPermission(Context context) {
        return getPrefSettings(context).getInt("unit_operationPermission", 1);
    }

    public static synchronized void setUnitOperationPermission(int unit_operationPermission,Context context) {
        getPrefSettings(context).edit().putInt("unit_operationPermission", unit_operationPermission).commit();
    }

    /***家庭数据***/
    public static synchronized UnitFamilyModel getUnitFamilyModel() {
        return (UnitFamilyModel) IOTApplication.getACache().getAsObject(Constants.Family_Model);
    }

    public static synchronized void setUnitFamilyModel(UnitFamilyModel familyModel) {
        IOTApplication.getACache().put(Constants.Family_Model,familyModel);
    }
}
