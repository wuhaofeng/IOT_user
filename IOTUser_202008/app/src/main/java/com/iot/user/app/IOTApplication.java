package com.iot.user.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;

import androidx.multidex.MultiDex;

import com.blankj.utilcode.util.Utils;
import com.huawei.android.hms.agent.HMSAgent;
import com.igexin.sdk.PushManager;
import com.iot.user.BuildConfig;
import com.iot.user.R;
import com.iot.user.ui.activity.dev.DevDetailGasActivity;
import com.iot.user.ui.service.IOTIntentService;
import com.iot.user.ui.service.IOTPushService;
import com.iot.user.utils.ACache;
import com.iot.user.utils.PrefUtil;
import com.simple.spiderman.SpiderMan;
import com.socks.library.KLog;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.ArrayList;
import java.util.List;
public class IOTApplication extends Application {
    private static final String TAG = "okhttp";
    private static IOTApplication application;
    private static ACache aCache;
    public Context mContext;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);/***google提供处理dex中方法数65535超过**/
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initBase();
        //初始化Log
        initLog();
        PrefUtil.setAppSelectVersion(2,getApplicationContext());/**本地缓存**/
        //初始化异常捕获
        initCrash();
        //内存泄漏捕获
        initLeak();
        //初始化个推
        initGetui();
        //初始化华为推送
        HMSAgent.init(this);
        initBackgroundCallBack();
    }

    private void initLog() {
        KLog.init(BuildConfig.LOG_DEBUG, "---GankMM---");
    }

    private void initBase() {
        application = this;
        //初始化ACache类
        aCache = ACache.get(this);
        //开启违例检测:StrictMode
        if (BuildConfig.LOG_DEBUG && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
        }
        Utils.init(this);
    }

    public static ACache getACache() {
        return aCache;
    }

    private void initLeak(){
      /***  if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);


        AppWatcher.Config config = AppWatcher.getConfig().newBuilder()
                .watchFragmentViews(true)
                .watchActivities(true)
                .watchFragments(true)
                .watchViewModels(true)
                .build();
        AppWatcher.setConfig(config);

        LeakCanary.Config config1 = LeakCanary.getConfig().newBuilder()
                .retainedVisibleThreshold(3)
                .build();
        LeakCanary.setConfig(config1);

        BlockCanary.install(this,new AppBlockCanaryContext()).start();    */
    }

    private void initCrash() {
        /**
         * 初始化日志系统
         * context :    上下文
         * isDebug :    是不是Debug模式,true:崩溃后显示自定义崩溃页面 ;false:关闭应用,不跳转奔溃页面(默认)
         */
        SpiderMan.init(this).setTheme(R.style.SpiderManTheme_Dark);
        CrashReport.initCrashReport(getApplicationContext(), "0bae3bc833", false);/***QQBugly**/
//        MCrashMonitor.init(this, BuildConfig.LOG_DEBUG);
    }

    public static IOTApplication getIntstance() {
        return application;
    }

    //版本名
    public static String getVersionName() {
        return getPackageInfo().versionName;
    }
    //版本号
    public static int getVersionCode() {
        return getPackageInfo().versionCode;
    }

    private static PackageInfo getPackageInfo() {
        PackageInfo pi = null;
        try {
            PackageManager pm = application.getPackageManager();
            pi = pm.getPackageInfo(application.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);
            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pi;
    }

    private void initGetui(){
        //初始化个推服务
        PushManager.getInstance().initialize(getApplicationContext(), IOTPushService.class);
        // 注册 intentService 后 PushDemoReceiver 无效, sdk 会使用 DemoIntentService 传递数据,
        // AndroidManifest 对应保留一个即可(如果注册 DemoIntentService, 可以去掉 PushDemoReceiver, 如果注册了
        // IntentService, 必须在 AndroidManifest 中声明)
        PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), IOTIntentService.class);//注册服务接收消息
    }

    private void initBackgroundCallBack() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                Log.e("", "AppContext------->onActivityCreated");
            }
            @Override
            public void onActivityStarted(Activity activity) {
                Log.e("", "AppContext------->处于前台");
                if (activity instanceof DevDetailGasActivity){
                    if (((DevDetailGasActivity) activity).devStatus.equals("192")){
                        if (((DevDetailGasActivity) activity).selectTablayout==1){
                            if (((DevDetailGasActivity) activity).nodeFragment!=null){
                                ((DevDetailGasActivity) activity).nodeFragment.addNodeDataHeart();
                            }
                        }
                    }
                }
            }
            @Override
            public void onActivityResumed(Activity activity) {
                Log.e("", "AppContext------->onActivityResumed");
            }
            @Override
            public void onActivityPaused(Activity activity) {
                Log.e("", "AppContext------->onActivityPaused");
            }
            @Override
            public void onActivityStopped(Activity activity) {
                Log.e("", "AppContext------->处于后台");
                if (activity instanceof DevDetailGasActivity){
                    if (((DevDetailGasActivity) activity).devStatus.equals("192")){
                        if (((DevDetailGasActivity) activity).selectTablayout==1){
                            if (((DevDetailGasActivity) activity).nodeFragment!=null){
                                ((DevDetailGasActivity) activity).nodeFragment.removeNodeDataHeart();
                            }
                        }
                    }
                }
            }
            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                Log.e("", "AppContext------->onActivitySaveInstanceState");
            }
            @Override
            public void onActivityDestroyed(Activity activity) {
                Log.e("", "AppContext------->onActivityDestroyed");
            }
        });
    }

}

