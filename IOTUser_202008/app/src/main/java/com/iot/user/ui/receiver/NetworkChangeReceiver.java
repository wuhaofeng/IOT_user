package com.iot.user.ui.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;
import com.iot.user.utils.NetUtil;

/**
 * 监听网络状态变化
 * Created by Travis on 2017/10/11.
 */

public class NetworkChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // 如果相等的话就说明网络状态发生了变化
        Log.i("NetBroadcastReceiver", "NetBroadcastReceiver changed");
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            int netWorkState = NetUtil.getNetWorkState(context);
            // 当网络发生变化，判断当前网络状态，并通过NetEvent回调当前网络状态
            if (netChangeListener != null) {
                netChangeListener.onChangeListener(netWorkState);
            }
        }
    }
    // 自定义接口
    public interface NetChangeListener {
        void onChangeListener(int status);
    }
    private NetChangeListener netChangeListener;
    public void setNetChangeListener(NetChangeListener netChangeListener) {
        this.netChangeListener = netChangeListener;
    }
}

