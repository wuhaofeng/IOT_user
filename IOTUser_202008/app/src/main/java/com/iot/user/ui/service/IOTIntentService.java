package com.iot.user.ui.service;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.PushConsts;
import com.igexin.sdk.PushManager;
import com.igexin.sdk.message.BindAliasCmdMessage;
import com.igexin.sdk.message.FeedbackCmdMessage;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTNotificationMessage;
import com.igexin.sdk.message.GTTransmitMessage;
import com.igexin.sdk.message.SetTagCmdMessage;
import com.igexin.sdk.message.UnBindAliasCmdMessage;
import com.iot.user.app.IOTApplication;
import com.iot.user.ui.activity.alert.AlertNewActivity;
import com.iot.user.ui.activity.dev.DevDetailGasActivity;
import com.iot.user.ui.activity.dialog.GlobalDialogActivity;
import com.iot.user.ui.activity.main.UnitMainActivity;
import com.iot.user.ui.model.alert.AlertInfo;
import com.iot.user.ui.model.dev.push.UnitDevNodeProPushModel;
import com.iot.user.ui.model.dev.push.UnitDevNodePushModel;
import com.iot.user.ui.model.dev.push.UnitDevRoadPushModel;
import com.iot.user.utils.MyToast;
import com.iot.user.utils.PrefUtil;

import org.json.JSONObject;

import static com.iot.user.constant.Constants.DEV_OK;

/**
 * 继承 GTIntentService 接收来自个推的消息, 所有消息在线程中回调, 如果注册了该服务, 则务必要在 AndroidManifest中声明, 否则无法接受消息<br>
 * onReceiveMessageData 处理透传消息<br>
 * onReceiveClientId 接收 cid <br>
 * onReceiveOnlineState cid 离线上线通知 <br>
 * onReceiveCommandResult 各种事件处理回执 <br>
 */
public class IOTIntentService extends GTIntentService {
    private static final String TAG = "GetuiService";
    public IOTIntentService() {

    }
    @Override
    public void onReceiveServicePid(Context context, int pid) {
        Log.d(TAG, "onReceiveServicePid -> " + pid);
    }
    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage msg) {/***透传处理**/
        String appid = msg.getAppid();
        String taskid = msg.getTaskId();
        String messageid = msg.getMessageId();
        byte[] payload = msg.getPayload();
        String pkg = msg.getPkgName();
        String cid = msg.getClientId();
        // 第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行
        boolean result = PushManager.getInstance().sendFeedbackMessage(context, taskid, messageid, 90001);
        Log.d(TAG, "call sendFeedbackMessage = " + (result ? "success" : "failed"));
        Log.d(TAG, "onReceiveMessageData -> " + "appid = " + appid + "\ntaskid = " + taskid + "\nmessageid = " + messageid + "\npkg = " + pkg
                + "\ncid = " + cid);
        if (payload == null) {
            Log.e(TAG, "receiver payload = null");
        } else {
            String data = new String(payload);
            dispatchMessage(data,context);
            Log.d(TAG, "receiver payload = " + data);
        }

        Log.d(TAG, "----------------------------------------------------------------------------------------------");
    }

    public void dispatchMessage(String message,Context context) {
        try{
            Log.e(TAG, "dispatchMessage message = "+message);
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
                        startActivity(intent);
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
                        startActivity(intent);
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
                            startActivity(intent);
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
                            startActivity(intent);
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
                            startActivity(intent);
                        }
                    }
                    break;
                    default:
                        break;
                }
            }
        }catch (Exception e){
            Log.e(TAG, "onReceiveClientId -> " + "Exception = " + e.getMessage());
        }
    }

    @Override
    public void onReceiveClientId(Context context, String clientid) {
        Log.e(TAG, "onReceiveClientId -> " + "clientid = " + clientid);
        sendMessage(clientid, 1);
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean online) {
        Log.d(TAG, "onReceiveOnlineState -> " + (online ? "online" : "offline"));
    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage cmdMessage) {
        Log.d(TAG, "onReceiveCommandResult -> " + cmdMessage);

        int action = cmdMessage.getAction();

        if (action == PushConsts.SET_TAG_RESULT) {
            setTagResult((SetTagCmdMessage) cmdMessage);
        } else if(action == PushConsts.BIND_ALIAS_RESULT) {
            bindAliasResult((BindAliasCmdMessage) cmdMessage);
        } else if (action == PushConsts.UNBIND_ALIAS_RESULT) {
            unbindAliasResult((UnBindAliasCmdMessage) cmdMessage);
        } else if ((action == PushConsts.THIRDPART_FEEDBACK)) {
            feedbackResult((FeedbackCmdMessage) cmdMessage);
        }
    }

    @Override
    public void onNotificationMessageArrived(Context context, GTNotificationMessage message) {
        Log.d(TAG, "onNotificationMessageArrived -> " + "appid = " + message.getAppid() + "\ntaskid = " + message.getTaskId() + "\nmessageid = "
                + message.getMessageId() + "\npkg = " + message.getPkgName() + "\ncid = " + message.getClientId() + "\ntitle = "
                + message.getTitle() + "\ncontent = " + message.getContent());
    }

    @Override
    public void onNotificationMessageClicked(Context context, GTNotificationMessage message) {/***通知处理***/
        Log.d(TAG, "onNotificationMessageClicked -> " + "appid = " + message.getAppid() + "\ntaskid = " + message.getTaskId() + "\nmessageid = "
                + message.getMessageId() + "\npkg = " + message.getPkgName() + "\ncid = " + message.getClientId() + "\ntitle = "
                + message.getTitle() + "\ncontent = " + message.getContent());

    }

    private void setTagResult(SetTagCmdMessage setTagCmdMsg) {
        String sn = setTagCmdMsg.getSn();
        String code = setTagCmdMsg.getCode();
        Log.d(TAG, "settag result sn = " + sn + ", code = " + code + ", text = ");
    }

    private void bindAliasResult(BindAliasCmdMessage bindAliasCmdMessage) {
        String sn = bindAliasCmdMessage.getSn();
        String code = bindAliasCmdMessage.getCode();
        Log.d(TAG, "bindAlias result sn = " + sn + ", code = " + code + ", text = " );

    }

    private void unbindAliasResult(UnBindAliasCmdMessage unBindAliasCmdMessage) {
        String sn = unBindAliasCmdMessage.getSn();
        String code = unBindAliasCmdMessage.getCode();
        Log.d(TAG, "unbindAlias result sn = " + sn + ", code = " + code + ", text = ");

    }


    private void feedbackResult(FeedbackCmdMessage feedbackCmdMsg) {
        String appid = feedbackCmdMsg.getAppid();
        String taskid = feedbackCmdMsg.getTaskId();
        String actionid = feedbackCmdMsg.getActionId();
        String result = feedbackCmdMsg.getResult();
        long timestamp = feedbackCmdMsg.getTimeStamp();
        String cid = feedbackCmdMsg.getClientId();

        Log.d(TAG, "onReceiveCommandResult -> " + "appid = " + appid + "\ntaskid = " + taskid + "\nactionid = " + actionid + "\nresult = " + result
                + "\ncid = " + cid + "\ntimestamp = " + timestamp);
    }

    private void sendMessage(String data, int what) {
        Message msg = Message.obtain();
        msg.what = what;
        msg.obj = data;
    }


    /** 用来保存最新打开页面的context */


    private boolean getTopAcitivyIsDevDetail() {
        boolean isDevDetail = false;
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        String activityName = am.getRunningTasks(1).get(0).topActivity.getClassName();
        try {
            Class topActivity = Class.forName(activityName);
//            if (topActivity.equals(DevDetailGasActivity.class)) {
//                isDevDetail = true;
//                Log.d("@@@@", "是设备详情" + activityName);
//            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return isDevDetail;
    }

}

