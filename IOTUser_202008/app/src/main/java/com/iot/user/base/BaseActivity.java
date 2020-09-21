package com.iot.user.base;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.iot.user.R;
import com.iot.user.skin.SkinManager;
import com.iot.user.ui.receiver.NetworkChangeReceiver;
import com.iot.user.utils.NetUtil;
import com.iot.user.utils.PrefUtil;
import com.jaeger.library.StatusBarUtil;
import com.maning.mndialoglibrary.MProgressDialog;
import com.maning.mndialoglibrary.MStatusDialog;
import com.socks.library.KLog;


public abstract class BaseActivity<V extends ViewDataBinding> extends AppCompatActivity {

    protected  V dataBinding;
    public Context mContext;
    private NetworkChangeReceiver netBroadcastReceiver;
    /**
     * 网络类型
     */
    private int netType;
    private MaterialDialog materialDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //设置主题
        SkinManager.onActivityCreateSetSkin(this);
        super.onCreate(savedInstanceState);
        mContext = this;
        initStatus();
        initNetWorkCheck();
        if (savedInstanceState!=null) {
            if (savedInstanceState.getString("Activity").equals("BaseMvpActivity"))
            return;
        }
        initViewDataBinding(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void initStatus() {
        //设置状态栏的颜色
        int currentSkinType = SkinManager.getCurrentSkinType(this);
        if (SkinManager.THEME_DAY == currentSkinType) {
            StatusBarUtil.setColor(this, getResources().getColor(R.color.main_color), 0);
        } else {
            StatusBarUtil.setColor(this, getResources().getColor(R.color.main_color_night), 0);
        }
    }

    public void initUnitStatus(){
        if (PrefUtil.getAppSelectVersion(this)==2){
            StatusBarUtil.setColor(this, getResources().getColor(R.color.unit_main_color), 0);
        }
    }


    public void showProgressDialog() {
        MProgressDialog.showProgress(this);
    }

    public void showProgressDialog(String message) {
        MProgressDialog.showProgress(this, message);
    }

    public void showProgressSuccess(String message) {
        new MStatusDialog(this).show(message, getResources().getDrawable(R.drawable.mn_icon_dialog_success));
    }

    public void showProgressError(String message) {
        new MStatusDialog(this).show(message, getResources().getDrawable(R.drawable.mn_icon_dialog_fail));
    }

    public void dissmissProgressDialog() {
        MProgressDialog.dismissProgress();
    }

    public void initToolBar(Toolbar toolbar, String title) {
        toolbar.setTitle(title);// 标题的文字需在setSupportActionBar之前，不然会无效
        setSupportActionBar(toolbar);
        int currentSkinType = SkinManager.getCurrentSkinType(this);
        if (SkinManager.THEME_DAY == currentSkinType) {
            toolbar.setTitleTextColor(getResources().getColor(R.color.gank_text1_color));
            toolbar.setPopupTheme(R.style.ToolBarPopupThemeDay);
        } else {
            toolbar.setTitleTextColor(getResources().getColor(R.color.gank_text1_color_night));
            toolbar.setPopupTheme(R.style.ToolBarPopupThemeNight);
        }
    }
    public void initToolBar(Toolbar toolbar, String title, int icon) {
        TextView tb_title=toolbar.findViewById(R.id.tv_toolbar_title);
        tb_title.setText(title);
        toolbar.setTitle("");// 标题的文字需在setSupportActionBar之前，不然会无效
        toolbar.setNavigationIcon(icon);
        setSupportActionBar(toolbar);
        int currentSkinType = SkinManager.getCurrentSkinType(this);
        if (SkinManager.THEME_DAY == currentSkinType) {
            toolbar.setTitleTextColor(getResources().getColor(R.color.gank_text1_color));
            toolbar.setPopupTheme(R.style.ToolBarPopupThemeDay);
        } else {
            toolbar.setTitleTextColor(getResources().getColor(R.color.gank_text1_color_night));
            toolbar.setPopupTheme(R.style.ToolBarPopupThemeNight);
        }
    }
    public void initToolBarRightBtn(Toolbar toolbar, String title, int icon,int status){
        if (status==1){
            Button rightBtn=toolbar.findViewById(R.id.btn_toolbar_right_title);
            rightBtn.setText(title);
            rightBtn.setTextSize(14);
            rightBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickRightBtn(view);
                }
            });
        }else {
            ImageView rightBtn=toolbar.findViewById(R.id.iv_toolbar_right_title);
            rightBtn.setBackgroundDrawable(ContextCompat.getDrawable(this,icon));
            rightBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickRightBtn(view);
                }
            });
        }
    }

    protected void clickRightBtn(View button){

    }

    protected void initClickBtn(){

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == android.view.KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (MProgressDialog.isShowing()) {
                MProgressDialog.dismissProgress();
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);

    }

    @Override
    protected void onDestroy() {
        dissmissProgressDialog();
        Glide.with(this.getApplicationContext()).pauseRequests();
        if(netBroadcastReceiver!=null){
            unregisterReceiver(netBroadcastReceiver);
        }
        if(dataBinding != null){
            dataBinding.unbind();
        }
        super.onDestroy();
        KLog.i("BaseActivity","\n=====stop====:"+getClass().getSimpleName());
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkNet();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void initNetWorkCheck(){
        //Android 7.0以上需要动态注册
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //实例化IntentFilter对象
            IntentFilter filter = new IntentFilter();
            filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
            filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            netBroadcastReceiver = new NetworkChangeReceiver();
            netBroadcastReceiver.setNetChangeListener(new NetworkChangeReceiver.NetChangeListener() {
                @Override
                public void onChangeListener(int status) {
                    BaseActivity.this.netType = status;
                    if (!isNetConnect()) {
                        showNetDialog();
                    } else {
                        /**如果获取到网络就加载主页的数据***/
//                        if (MainFragment.mainHandler != null) {
//                            MainFragment.mainHandler.sendEmptyMessage(MainFragment.REFRESH_MSG);
//                        }
                        hideNetDialog();
                    }
                }
            });
            //注册广播接收
            registerReceiver(netBroadcastReceiver, filter);
        }
    }
    /**
     * 初始化时判断有没有网络
     */
    public boolean checkNet() {
        this.netType = NetUtil.getNetWorkState(BaseActivity.this);
        if (!isNetConnect()) {
            //网络异常，请检查网络
            showNetDialog();
        }
        return isNetConnect();

    }

    /**
     * 判断有无网络 。
     *
     * @return true 有网, false 没有网络.
     */
    public boolean isNetConnect() {
        if (netType == 1) {
            return true;
        } else if (netType == 0) {
            return true;
        } else if (netType == -1) {
            return false;
        }
        return false;
    }

    private void showNetDialog(){
        if (mContext==null){
            return;
        }
        if(materialDialog == null){
            materialDialog = new MaterialDialog.Builder(BaseActivity.this)
                    .title("提示")
                    .content("检测到设备断网，请检查网络")
                    .positiveText("前往设置")
                    .negativeText("取消")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                            startActivity(intent);
                        }
                    })
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        }
                    }).build();
        }
        if(materialDialog!=null){
            try {
                materialDialog .show();
            }catch (Exception e){

            }
        }
    }

    private void hideNetDialog(){
        if(materialDialog!=null){
            materialDialog.dismiss();
        }
    }


    /***dataBinding***/
    /**
     * 注入绑定
     */
    private void initViewDataBinding(Bundle savedInstanceState) {
        //DataBindingUtil类需要在project的build中配置 dataBinding {enabled true }, 同步后会自动关联android.databinding包
        dataBinding = DataBindingUtil.setContentView(this, initContentView(savedInstanceState));
        initView();
        initClickBtn();/**点击事件的控制***/
    }

    /**
     * 初始化根布局
     *
     * @return 布局layout的id
     */
    public abstract int initContentView(Bundle savedInstanceState);

    /**
     * 初始化视图
     */
    public abstract void initView();


}
