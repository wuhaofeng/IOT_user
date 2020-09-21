package com.iot.user.constant;

import android.os.Environment;

import java.io.File;

/**
 * 常量类
 * 一些接口地址等常量
 */
public class Constants {

    //高德省市区街道查询接口
    public static final String GAODE_REGION_AREA_URL = "https://restapi.amap.com/v3/config/district?key=cc51ff6bf1e3db9c89aced11843b6901&subdistrict=1&extensions=base";
    //高德地名反推经纬度接口
    public static final String GAODE_GET_LONLAT_URL = "https://restapi.amap.com/v3/geocode/geo?output=JSON&key=cc51ff6bf1e3db9c89aced11843b6901";



    //接口请求的Url
    public static final String BASEURL = "http://gank.io/api/";

    //干活历史日期
    public static final String URL_HistoryDate = "http://gank.io/api/day/history";

    //获取APKinfo的地址：fir.im
    public static final String URL_AppUpdateInfo = "http://api.fir.im/apps/latest/56dd4bb7e75e2d27f2000046?api_token=78a2cea8e63eb7a96ba6ca2a3e500af2&type=android";

    //Mob官网API
    public static final String URL_Mob = "http://apicloud.mob.com/";
    //Mob ApiKey
    public static final String URL_APP_Key = "1c9dccb9a2434";

    //保存图片的地址
    public static final String BasePath = Environment.getExternalStorageDirectory() + "/GankMM";

    //更新APK地址
    public static final String UpdateAPKPath = Environment.getExternalStorageDirectory() + "/GankMM/update";


    //标签
    public static final String FlagFragment = "Flag";
    public static final String FlagWelFare = "福利";
    public static final String FlagAndroid = "Android";
    public static final String FlagIOS = "iOS";
    public static final String FlagVideo = "休息视频";
    public static final String FlagJS = "前端";
    public static final String FlagExpand = "拓展资源";
    public static final String AllFragment = "全部公告";
    public static final String ReadFragment = "已读公告";
    public static final String UnReadFragment = "未读公告";

    public static final String UserReatingFragment = "用户消息";
    public static final String DevReatingFragment = "设备消息";
    public static final String AllReatingFragment = "全部消息";

    public static final String UnitMessageHistoryFragment = "历史消息";
    public static final String UnitMessageFamilyShareFragment = "家庭共享";
    public static final String UnitMessageDevShareFragment = "设备共享";

    //SharePreferences  ---- Key
    public static final String SPFeedback = "feedback";
    public static final String SPJpush = "jpush";
    public static final String SPAppUpdate = "update";
    public static final String SPCodesMenu = "codeMenu";
    public static final String SPSwitcherDataType = "SPSwitcherDataType"; //保存首页头条


    public static final int DEV_OK = 0;
    /**
     * 警告类型
     */
    public static final int DEV_WARN = 1;
    /**
     * 报警类型
     */
    public static final int DEV_ALARM = 2;
    /**
     * 复合类型
     */
    public static final int DEV_COMPLEX = 3;


    /**更新类型，强制、非强制**/
    public static final String UPDATE_FORCE = "1";
    public static final String UPDATE_NORMAL = "0";

    public static final int PageSize = 10;
    public static final String Family_Model = "FamilyModel";
}
