package com.iot.user.utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.AppUtils;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.iot.user.R;
import com.iot.user.app.IOTApplication;
import com.iot.user.base.BaseActivity;
import com.iot.user.constant.Constants;
import com.iot.user.http.NetWorkApi;
import com.iot.user.http.model.BaseResponse;
import com.iot.user.http.net.RxUtils;
import com.iot.user.http.request.login.CheckUpdateJsonRequst;
import com.iot.user.ui.activity.login.LoginActivity;
import com.iot.user.ui.receiver.NetworkChangeReceiver;
import com.iot.user.ui.service.SpeakerAudioPlayerManager;
import com.iot.user.utils.model.UpdateInfo;
import com.maning.updatelibrary.InstallUtils;
import com.maning.updatelibrary.utils.MNUtils;
import com.socks.library.KLog;

import io.reactivex.observers.DisposableObserver;

public class AppUpdateUtils {
    private Context mContext;
    private String apkPath;
    public boolean isCheckUpdate=false;/***防止更新弹窗的重复添加**/
    public AppUpdateUtils(Context context){
        this.mContext=context;
    }
    public void appUpdateCheck(){
        if (isCheckUpdate==false) {
            checkNet();
            checkUpdate();
        }
    }
    /**
     * 检测版本更新
     */
    public void checkUpdate() {
        CheckUpdateJsonRequst requst = new CheckUpdateJsonRequst(
                PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()),
                PrefUtil.getLoginToken(IOTApplication.getIntstance()),
                "1"
        );
        NetWorkApi.provideRepositoryData().app_update(requst)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        if (entity.getCode() == 0) {
                            LinkedTreeMap map=(LinkedTreeMap)entity.getBody();
                            Gson gson=new Gson();
                            String jsonString = gson.toJson(map);
                            UpdateInfo updateInfo=gson.fromJson(jsonString, UpdateInfo.class);
                            if(updateInfo!=null){
                                checkAppUpdate(updateInfo);
                            }else{
                                PrefUtil.setNewVersion(0,IOTApplication.getIntstance());
                            }
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                    }
                    @Override
                    public void onComplete() {
                    }
                });
    }
    private void checkAppUpdate(UpdateInfo appUpdateInfo){
        if(appUpdateInfo !=null){
            int newVersion = 0 ;
            try{
                newVersion = Integer.parseInt(appUpdateInfo.getVnum());
            }catch (Exception e){
            }
            PrefUtil.setNewVersion(newVersion,IOTApplication.getIntstance());
            if (IOTApplication.getVersionCode() < newVersion) {
                //需要版本更新
                showAppUpdateDialog(appUpdateInfo);
            } else {
//                            MySnackbar.makeSnackBarGreen(tv_nickname, "已经是最新版本，无须更新");
//                            MyToast.showShortToast("已经是最新版本，无须更新"+IOTApplication.getVersionCode());
            }
        }
    }
    public void showAppUpdateDialog(final UpdateInfo updateInfo) {
        isCheckUpdate=true;
        String title = "检测到新版本:" + updateInfo.getVname();
//        Double appSize = Double.parseDouble(updateInfo.getFsize() + "") / 1024 / 1024;
//        DecimalFormat df = new DecimalFormat(".##");
//        String resultSize = df.format(appSize) + "M";
        boolean isWifi = NetUtils.isWifiConnected(mContext);
        String content = "";
        apkPath = MNUtils.getCachePath(mContext) + "/" + "明厦智慧燃气" + updateInfo.getVname() + ".apk";
        InstallUtils.with(mContext).setApkPath(apkPath);

        if(Constants.UPDATE_FORCE.equals(updateInfo.getIsForceUpdate())){
            content = "检测到新版本，请立即更新";
            content+= "\n"+ updateInfo.getDescription();
            SpeakerAudioPlayerManager.getDefaultInstance().stopRing();
            final Dialog dialog = new Dialog(mContext, R.style.dialog);
            View dView = DialogUtils.showDialog(dialog, (Activity) mContext, R.layout.dialog_update, Gravity.CENTER, 0.75,0.83, false);
            dView.findViewById(R.id.ll_two_btn).setVisibility(View.GONE);
            dView.findViewById(R.id.ll_one_btn).setVisibility(View.VISIBLE);
            TextView tvtop = dView.findViewById(R.id.tv_top);
            tvtop.setText(title);
            TextView tvContent = dView.findViewById(R.id.tv_content);
            tvContent.setMovementMethod(ScrollingMovementMethod.getInstance());
            tvContent.setText(content);
            TextView tvConfirm2 = dView.findViewById(R.id.tv_confirm2);
            if (AppUtil.isNeedDownload(updateInfo, apkPath)){
                tvConfirm2.setText("立即更新");
            }else {
                tvConfirm2.setText("立即安装");
            }
            tvConfirm2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!DoubleClickUtil.isFastDoubleClick()){
                        if (isNetConnect()){
                            if (AppUtil.isNeedDownload(updateInfo, apkPath)){
                                showDownloadDialog(true,updateInfo);
                            }else {
                                isCheckUpdate = false;
                                AppUtils.installApp(apkPath);
                            }
                            dialog.dismiss();
                        }else {
                            showNetDialog();
                        }
                    }
                }
            });
        }else{
            content ="当前网络为" + (isWifi ? "wifi" : "非wifi环境(注意)")+",是否更新";
            content+= "\n"+ updateInfo.getDescription();

            final Dialog dialog = new Dialog(mContext, R.style.dialog);
            View dView = DialogUtils.showDialog(dialog, (Activity)mContext, R.layout.dialog_update, Gravity.CENTER, 0.75,0.83, true);
            TextView tvtop = dView.findViewById(R.id.tv_top);
            tvtop.setText(title);
            TextView tvContent = dView.findViewById(R.id.tv_content);
            tvContent.setMovementMethod(ScrollingMovementMethod.getInstance());
            tvContent.setText(content);
            TextView tvConfirm = dView.findViewById(R.id.tv_confirm);
            if (AppUtil.isNeedDownload(updateInfo, apkPath)){
                tvConfirm.setText("立即更新");
            }else {
                tvConfirm.setText("立即安装");
            }
            dView.findViewById(R.id.ll_two_btn).setVisibility(View.VISIBLE);
            dView.findViewById(R.id.ll_one_btn).setVisibility(View.GONE);
            tvConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isNetConnect()){
                        if (AppUtil.isNeedDownload(updateInfo, apkPath)){
                            showDownloadDialog(false,updateInfo);
                        }else {
                            isCheckUpdate = false;
                            AppUtils.installApp(apkPath);
                        }
                        dialog.dismiss();
                    }else {
                        showNetDialog();
                    }
                }
            });
            dView.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }
    }

    private MaterialDialog dialogUpdate;
    private NotifyUtil notifyUtils;
    private void showDownloadDialog(final boolean isForce, final UpdateInfo updateInfo) {

        if(isForce){
            dialogUpdate = new MaterialDialog.Builder(mContext)
                    .title("正在下载最新版本")
                    .content("请稍等")
                    .canceledOnTouchOutside(false)
                    .cancelable(false)
                    .progress(false, 100, false)
                    .show();
        }else{
            dialogUpdate = new MaterialDialog.Builder(mContext)
                    .title("正在下载最新版本")
                    .content("请稍等")
                    .canceledOnTouchOutside(false)
                    .cancelable(false)
                    .progress(false, 100, false)
                    .negativeText("后台下载")
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            startNotifyProgress();
                        }
                    })
                    .show();
        }
        InstallUtils.with(mContext)
                .setApkUrl(updateInfo.getUrl())
                //    .setApkName("Mingsha")
                .setCallBack(new InstallUtils.DownloadCallBack() {
                    @Override
                    public void onStart() {
                        KLog.i("installAPK-----onStart");
                        if (dialogUpdate != null) {
                            dialogUpdate.setProgress(0);
                        }
                    }

                    @Override
                    public void onComplete(String path) {
                        isCheckUpdate=false;
                        KLog.i("installAPK----onComplete:" + path);
                        NotifyUtil.showNotificationProgress(mContext, "下载完成", "明厦智慧燃气", 0, "iot.user", 100, 100);
                        /**
                         * 安装APK工具类
                         * @param context       上下文
                         * @param filePath      文件路径
                         * @param authorities   ---------Manifest中配置provider的authorities字段---------
                         * @param callBack      安装界面成功调起的回调
                         */
                        InstallUtils.installAPK((Activity)mContext, path, new InstallUtils.InstallCallBack() {
                            @Override
                            public void onSuccess() {
                                Toast.makeText(mContext, "正在安装程序", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFail(Exception e) {
                                Toast.makeText(mContext, "安装失败:" + e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                        if (dialogUpdate != null && dialogUpdate.isShowing()) {
                            dialogUpdate.dismiss();
                        }
                        if (notifyUtils != null) {
                            notifyUtils.setNotifyProgressComplete();
                            notifyUtils.clear();
                        }
                    }

                    @Override
                    public void onLoading(long total, long current) {
                        KLog.i("installAPK-----onLoading:-----total:" + total + ",current:" + current);
                        int currentProgress = (int) (current * 100 / total);
                        if (dialogUpdate != null) {
                            dialogUpdate.setProgress(currentProgress);
                        }
                        NotifyUtil.showNotificationProgress(mContext, "正在下载", "明厦智慧燃气", 0, "iot.user", currentProgress, 100);
                        if (notifyUtils != null) {
                            notifyUtils.setNotifyProgress(100, currentProgress, false);
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        if (dialogUpdate != null && dialogUpdate.isShowing()) {
                            dialogUpdate.dismiss();
                            MyToast.showShortToast("更新失败，请重试");
                            if (isForce){
                                showAppUpdateDialog(updateInfo);
                            }
                        }
                        NotifyUtil.cancleNotification(mContext, 0);
                        if (notifyUtils != null) {
                            notifyUtils.clear();
                        }
                    }

                    @Override
                    public void cancle() {

                    }
                })
                .startDownload();

    }

    /**
     * 开启通知栏
     */
    private void startNotifyProgress() {
        //设置想要展示的数据内容
        Intent intent = new Intent(mContext, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent rightPendIntent = PendingIntent.getActivity(mContext,
                0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        int smallIcon = R.mipmap.ic_launcher;
        String ticker = "正在下载明厦智慧燃气更新包...";
        //实例化工具类，并且调用接口
        notifyUtils = new NotifyUtil(mContext, 0);
        notifyUtils.notify_progress(rightPendIntent, smallIcon, ticker, "明厦智慧燃气下载", "正在下载中...", false, false, false);
    }


    /**
     * 网络类型
     */
    private int netType;
    private MaterialDialog materialDialog = null;
    /**
     * 初始化时判断有没有网络
     */
    public boolean checkNet() {
        this.netType = NetUtil.getNetWorkState(mContext);
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
            materialDialog = new MaterialDialog.Builder(mContext)
                    .title("提示")
                    .content("检测到设备断网，请检查网络")
                    .positiveText("前往设置")
                    .negativeText("取消")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                            mContext.startActivity(intent);
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


}

