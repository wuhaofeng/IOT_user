package com.iot.user.ui.receiver;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.huawei.hms.support.api.push.PushReceiver;
import com.iot.user.app.IOTApplication;
import com.iot.user.ui.activity.alert.AlertNewActivity;
import com.iot.user.ui.activity.dev.DevDetailGasActivity;
import com.iot.user.ui.activity.dialog.GlobalDialogActivity;
import com.iot.user.ui.activity.main.UnitMainActivity;
import com.iot.user.ui.model.alert.AlertInfo;
import com.iot.user.ui.model.dev.push.UnitDevNodeProPushModel;
import com.iot.user.ui.model.dev.push.UnitDevNodePushModel;
import com.iot.user.ui.model.dev.push.UnitDevRoadPushModel;
import com.iot.user.ui.service.SpeakerAudioPlayerManager;
import com.iot.user.utils.LogUtil;
import com.iot.user.utils.MyToast;
import com.iot.user.utils.PrefUtil;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static com.iot.user.constant.Constants.DEV_OK;

public class HuaweiPushReceiver extends PushReceiver {

    public static final String TAG = "HuaweiPushRevicer";


    public static final String ACTION_UPDATEUI = "action.updateUI";
    public static final String ACTION_TOKEN = "action.updateToken";

    private static List<IPushCallback> pushCallbacks = new ArrayList<IPushCallback>();
    private static final Object CALLBACK_LOCK = new Object();


    public interface IPushCallback {
        void onReceive(Intent intent);
    }

    @Override
    public void onToken(Context context, String s, Bundle bundle) {
        super.onToken(context, s, bundle);
    }

    public static void registerPushCallback(IPushCallback callback) {
        synchronized (CALLBACK_LOCK) {
            pushCallbacks.add(callback);
        }
    }

    public static void unRegisterPushCallback(IPushCallback callback) {
        synchronized (CALLBACK_LOCK) {
            pushCallbacks.remove(callback);
        }
    }
    public static void unRegisterAllPushBacks(){
        synchronized (CALLBACK_LOCK) {
            pushCallbacks.clear();
        }
    }

    @Override
    public boolean onPushMsg(Context context, byte[] bytes, Bundle bundle) {
        try {
            //CP可以自己解析消息内容，然后做相应的处理 | CP can parse message content on its own, and then do the appropriate processing
            String content = new String(bytes, StandardCharsets.UTF_8);
            LogUtil.e("HuaweiPushReceiver","onPushMsg =="+content);
            dispatchMessage(content);
        } catch (Exception e) {

        }
        return false;
    }

    public void dispatchMessage(String message) {
        try{
            JSONObject jsonObject = new JSONObject(message);
            if(jsonObject.has("type")){
                String type = jsonObject.getString("type");
                String content = jsonObject.getString("content");
                switch (type){
                    case "1": {//设备通知类（报警、故障等）
                        Gson gson = new Gson();
                        AlertInfo alertInfo = gson.fromJson(content, AlertInfo.class);
                        if (alertInfo.getDevStat()==DEV_OK){//设备恢复正常
                            if(UnitMainActivity.gtMsgHandler !=null){
                                Message msg = new Message();
                                msg.what=3;
                                UnitMainActivity.gtMsgHandler.sendMessage(msg);
                            }
                            return;
                        }
                        long updateTime = alertInfo.getAt();
                        long leftTime = System.currentTimeMillis() - updateTime;
                        Log.e(TAG, "updateTime = " + updateTime);
                        Log.e(TAG, "System.currentTimeMillis() = " + System.currentTimeMillis());
                        Log.e(TAG, "leftTime >= 30 * 60 * 1000) = " + (leftTime >= 30 * 60 * 1000));
                        if (leftTime >= 30 * 60 * 1000) {//半小时超时机制,报警发生在半小时前，则认为该报警为过期报警，不提示
                            return;
                        }

                        if (alertInfo != null) {
                            Intent intent = new Intent(IOTApplication.getIntstance().getApplicationContext(), AlertNewActivity.class);
                            intent.putExtra(AlertNewActivity.ALARM, alertInfo);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            IOTApplication.getIntstance().getApplicationContext().startActivity(intent);
                        }
                    }
                    break;
                    case "4": {/***设备共享**/
                        Intent intent=new Intent(IOTApplication.getIntstance().getApplicationContext(), UnitMainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        IOTApplication.getIntstance().getApplicationContext().startActivity(intent);
                        if(UnitMainActivity.gtMsgHandler !=null){
                            Message msg = new Message();
                            msg.what=4;
                            UnitMainActivity.gtMsgHandler.sendMessage(msg);
                        }
                    }
                    break;
                    case "5": {/****家庭共享*/
                        Intent intent=new Intent(IOTApplication.getIntstance().getApplicationContext(), UnitMainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        IOTApplication.getIntstance().getApplicationContext().startActivity(intent);
                        if(UnitMainActivity.gtMsgHandler !=null){
                            Message msg = new Message();
                            msg.what=5;
                            UnitMainActivity.gtMsgHandler.sendMessage(msg);
                        }
                    }
                    break;
                    case "8": {/****联动模块下 设备控制的推送*/
                        Gson gson = new Gson();
                        final UnitDevNodeProPushModel alertInfo = gson.fromJson(jsonObject.getString("data"), UnitDevNodeProPushModel.class);
                        if (getTopAcitivyIsDevDetail() ){
                            if (DevDetailGasActivity.nodeHandler != null) {
                                Message msg = new Message();
                                msg.what = 3;
                                msg.obj = alertInfo;
                                DevDetailGasActivity.nodeHandler.handleMessage(msg);
                            }
                        }else{
                            if (IOTApplication.getIntstance().mContext==null|| PrefUtil.getUnitFamilyModel() ==null){
                                MyToast.showShortToast("未获取到家庭数据");
                                return;
                            }
                            Intent intent = new Intent(IOTApplication.getIntstance().getApplicationContext(), GlobalDialogActivity.class);
                            intent.putExtra("CONTENT",content);
                            intent.putExtra("DEVNUM",alertInfo.getDevnum());
                            intent.putExtra("SELECTTAB",3);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            IOTApplication.getIntstance().getApplicationContext().startActivity(intent);
                        }
                    }
                    break;
                    case "6": {/****回路屏蔽*/
                        Gson gson = new Gson();
                        final UnitDevRoadPushModel alertInfo = gson.fromJson(jsonObject.getString("data"), UnitDevRoadPushModel.class);
                        if (getTopAcitivyIsDevDetail() ){
                            if (DevDetailGasActivity.nodeHandler != null) {
                                Message msg = new Message();
                                msg.what = 1;
                                msg.obj = alertInfo;
                                DevDetailGasActivity.nodeHandler.handleMessage(msg);
                            }
                        }else{
                            if (IOTApplication.getIntstance().mContext==null||PrefUtil.getUnitFamilyModel()==null){
                                MyToast.showShortToast("未获取到家庭数据");
                                return;
                            }
                            Intent intent = new Intent(IOTApplication.getIntstance().getApplicationContext(), GlobalDialogActivity.class);
                            intent.putExtra("CONTENT",content);
                            intent.putExtra("DEVNUM",jsonObject.getString("devnum"));
                            intent.putExtra("SELECTTAB",2);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            IOTApplication.getIntstance().getApplicationContext().startActivity(intent);
                        }
                    }
                    break;
                    case "7": {/***节点屏蔽**/
                        Gson gson = new Gson();
                        UnitDevNodePushModel alertInfo = gson.fromJson(jsonObject.getString("data"), UnitDevNodePushModel.class);
                        if (getTopAcitivyIsDevDetail() ){
                            if (DevDetailGasActivity.nodeHandler != null) {
                                Message msg = new Message();
                                msg.what = 2;
                                msg.obj = alertInfo;
                                DevDetailGasActivity.nodeHandler.handleMessage(msg);
                            }
                        }else{
                            if (IOTApplication.getIntstance().mContext==null||PrefUtil.getUnitFamilyModel()==null){
                                MyToast.showShortToast("未获取到家庭数据");
                                return;
                            }
                            Intent intent = new Intent(IOTApplication.getIntstance().getApplicationContext(), GlobalDialogActivity.class);
                            intent.putExtra("CONTENT",content);
                            intent.putExtra("DEVNUM",jsonObject.getString("devnum"));
                            intent.putExtra("SELECTTAB",2);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            IOTApplication.getIntstance().getApplicationContext().startActivity(intent);
                        }
                    }
                    break;
                    default:
                        break;
                }
            }
        }catch (Exception e){

        }

    }

    @Override
    public void onEvent(Context context, Event event, Bundle extras) {
        super.onEvent(context, event, extras);
        int notifyId = extras.getInt(BOUND_KEY.pushNotifyId, 0);
        String message = extras.getString(BOUND_KEY.pushMsgKey);

        LogUtil.e("HuaweiPushReceiver","华为【onEvent】event=" + event + "  notifyId=" + notifyId + "  message=" + message);
    }

    @Override
    public void onPushState(Context context, boolean b) {
        Intent intent = new Intent();
        intent.setAction(ACTION_UPDATEUI);
        intent.putExtra("log", "The Push connection status is:" + b);
        callBack(intent);
    }

    @Override
    public void onToken(Context context, String s) {

        Intent intent = new Intent();
        intent.setAction(ACTION_TOKEN);
        intent.putExtra(ACTION_TOKEN, s);
        callBack(intent);

        intent = new Intent();
        intent.setAction(ACTION_UPDATEUI);
        intent.putExtra("log", "Token is:" + s);
        callBack(intent);
    }



    private static void callBack(Intent intent) {
        synchronized (CALLBACK_LOCK) {
            for (IPushCallback callback : pushCallbacks) {
                if (callback != null) {
                    callback.onReceive(intent);
                }
            }
        }
    }


    private boolean getTopAcitivyIsDevDetail() {
        boolean isDevDetail = false;
        ActivityManager am = (ActivityManager)IOTApplication.getIntstance().getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String activityName = am.getRunningTasks(1).get(0).topActivity.getClassName();
        try {
            Class topActivity = Class.forName(activityName);
            if (topActivity.equals(DevDetailGasActivity.class)) {
                isDevDetail = true;
                Log.d("@@@@", "是设备详情" + activityName);
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return isDevDetail;
    }
}
