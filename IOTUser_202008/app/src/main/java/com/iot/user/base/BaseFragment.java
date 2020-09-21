package com.iot.user.base;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.iot.user.R;
import com.iot.user.ui.receiver.NetworkChangeReceiver;
import com.maning.mndialoglibrary.MProgressDialog;
import com.maning.mndialoglibrary.MStatusDialog;


public abstract class BaseFragment<V extends ViewDataBinding> extends Fragment{
    protected  V dataBinding;
    //统计名字判断
    public String className;
    public Context context;
    private NetworkChangeReceiver netBroadcastReceiver;
    /**
     * 网络类型
     */
    private int netType;
    private MaterialDialog materialDialog = null;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        className = this.getClass().getSimpleName();
        context = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        initNetWorkCheck();
        dataBinding = DataBindingUtil.inflate(inflater, initContentView(inflater, container, savedInstanceState), container, false);
        initView(dataBinding.getRoot());
        initClickBtn();
        return dataBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (dataBinding != null) {
            dataBinding.unbind();
        }
    }

    protected void initClickBtn(){

    }

    public void showProgressDialog() {
        MProgressDialog.showProgress(context);
    }

    public void showProgressDialog(String message) {
        MProgressDialog.showProgress(context, message);
    }

    public void showProgressSuccess(String message) {
        new MStatusDialog(context).show(message, getResources().getDrawable(R.drawable.mn_icon_dialog_success));
    }

    public void showProgressError(String message) {
        new MStatusDialog(context).show(message, getResources().getDrawable(R.drawable.mn_icon_dialog_fail));
    }

    public void dissmissProgressDialog() {
        MProgressDialog.dismissProgress();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
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
                    BaseFragment.this.netType = status;
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
            context.registerReceiver(netBroadcastReceiver, filter);
        }
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

    public void showNetDialog(){
        if (context==null){
            return;
        }
        if(materialDialog == null){
            materialDialog = new MaterialDialog.Builder(context)
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

    public void showProgressDialogWitnTime(int time){
        showProgressDialog();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2*1000);//让他显示10秒后，取消ProgressDialog
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                dissmissProgressDialog();
            }
        });
        t.start();
    }

    /**
     * 初始化根布局
     *
     * @return 布局layout的id
     */
    public abstract int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    /**
     * 初始化视图
     *
     * @param view
     */
    protected abstract void initView(View view);
}
