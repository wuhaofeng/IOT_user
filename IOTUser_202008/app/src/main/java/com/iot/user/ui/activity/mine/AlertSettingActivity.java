package com.iot.user.ui.activity.mine;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import com.iot.user.R;
import com.iot.user.base.BaseActivity;
import com.iot.user.constant.HttpErrorCode;
import com.iot.user.databinding.ActivityAlertSettingBinding;
import com.iot.user.http.NetWorkApi;
import com.iot.user.http.model.BaseResponse;
import com.iot.user.http.net.RxUtils;
import com.iot.user.http.request.mine.ModifyMarkJsonRequest;
import com.iot.user.skin.SkinManager;
import com.iot.user.ui.service.SpeakerAudioPlayerManager;
import com.iot.user.utils.DialogUtils;
import com.iot.user.utils.DoubleClickUtil;
import com.iot.user.utils.MyToast;
import com.iot.user.utils.PrefUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.observers.DisposableObserver;

public class AlertSettingActivity extends BaseActivity<ActivityAlertSettingBinding> {
    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_alert_setting;
    }
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_alert_shock)
    ImageView iv_alert_shock;
    @BindView(R.id.iv_alert_music)
    ImageView iv_alert_music;
    @BindView(R.id.iv_notify_notice)
    ImageView iv_notify_notice;
    @BindView(R.id.iv_notify_sms)
    ImageView iv_notify_sms;
    @BindView(R.id.iv_notify_phone)
    ImageView iv_notify_phone;

    private String notifyMark;
    private String smsMark;
    private String phoneMark;

    @Override
    public void initView() {
        ButterKnife.bind(this);
        initMyToolBar();
        notifyMark = PrefUtil.getPushmark(AlertSettingActivity.this);
        smsMark = PrefUtil.getSmsmark(AlertSettingActivity.this);
        phoneMark = PrefUtil.getPhonemark(AlertSettingActivity.this);
        initViews();
    }
    private void initViews(){
        boolean isMusic = PrefUtil.getIsMusicMode(AlertSettingActivity.this);
        if(isMusic){
            openMusicMode();
        }else{
            closeMusicMode();
        }

        boolean isShock = PrefUtil.getIsShockMode(AlertSettingActivity.this);
        if(isShock){
            openShock();
        }else{
            closeShock();
        }

        if(notifyMark.equals("0")){
            openNotifyMode();
        }else{
            closeNotifyMode();
        }

        if(smsMark.equals("0")){
            openSMSMode();
        }else{
            closeSMSMode();
        }

        if(phoneMark.equals("0")){
            openPhoneMode();
        }else{
            closePhoneMode();
        }
    }

    private void initMyToolBar() {
        int currentSkinType = SkinManager.getCurrentSkinType(this);
        if (SkinManager.THEME_DAY == currentSkinType) {
            initToolBar(toolbar, "报警设置", R.drawable.gank_ic_back_white);
        } else {
            initToolBar(toolbar, "报警设置", R.drawable.gank_ic_back_night);
        }
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
    /**
     * 设置提示音是否开启
     */
    @OnClick(R.id.iv_alert_music)
    void iv_alert_music() {
        boolean isMusic = PrefUtil.getIsMusicMode(AlertSettingActivity.this);
        if(isMusic){
            closeMusicMode();
//            MySnackbar.makeSnackBarRed(iv_alert_music, "关闭报警声提示");
            MyToast.showShortToast("关闭报警声提示");
        }else{
            openMusicMode();
//            MySnackbar.makeSnackBarGreen(iv_alert_music, "开启报警声提示");
            MyToast.showShortToast("开启报警声提示");
        }
        PrefUtil.setIsMusicMode(!isMusic,AlertSettingActivity.this);
    }

    /**
     * 设置振动是否开启
     */
    @OnClick(R.id.iv_alert_shock)
    void iv_alert_shock() {
        boolean isShock = PrefUtil.getIsShockMode(AlertSettingActivity.this);
        if(isShock){
            closeShock();
//            MySnackbar.makeSnackBarRed(iv_alert_music, "");
            MyToast.showShortToast("关闭震动提示");
        }else{
            openShock();
//            MySnackbar.makeSnackBarGreen(iv_alert_music, "开启报警震动提示");
            MyToast.showShortToast("开启震动提示");
        }
        PrefUtil.setIsShockMode(!isShock,AlertSettingActivity.this);
    }

    /**
     * 推送通知是否开启
     */
    @OnClick(R.id.iv_notify_notice)
    void iv_notify_notice() {
        if(!DoubleClickUtil.isFastDoubleClick(R.id.iv_notify_notice)){
            final String _notify;
            if(notifyMark.equals("0")){
                _notify = "1";
                DialogUtils.showMyDialog(AlertSettingActivity.this, "提示", "关闭推送通知您将无法接收报警推送，确定这样操作吗？",
                        "确定", "取消", new DialogUtils.OnDialogClickListener() {
                            @Override
                            public void onConfirm() {
                                switchNotify(_notify);
                            }

                            @Override
                            public void onCancel() {

                            }
                        });
            }else{
                _notify = "0";
                switchNotify(_notify);
            }

        }
    }

    @SuppressLint("AutoDispose")
    private void switchNotify(final String _notify){
        showProgressDialog();
        ModifyMarkJsonRequest request = new ModifyMarkJsonRequest(PrefUtil.getLoginAccountUid(AlertSettingActivity.this),
                PrefUtil.getLoginToken(AlertSettingActivity.this),"","",_notify);
        NetWorkApi.provideRepositoryData().modifyMark(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        if (entity.getCode()==0){
                            notifyMark = _notify;
                            PrefUtil.setPushmark(notifyMark,AlertSettingActivity.this);
                            initViews();
                        }else{
                            MyToast.showShortToast(entity.getMessage());
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        dissmissProgressDialog();
                    }
                    @Override
                    public void onComplete() {
                        dissmissProgressDialog();
                    }
                });
    }


    /**
     * 推送通知是否开启
     */
    @OnClick(R.id.iv_notify_sms)
    void iv_notify_sms() {
        if(!DoubleClickUtil.isFastDoubleClick(R.id.iv_notify_sms)){
            final String _notify;
            if(smsMark.equals("0")){
                _notify = "1";
                DialogUtils.showMyDialog(AlertSettingActivity.this, "提示", "关闭短信通知您将无法接收报警短信，确定这样操作吗？",
                        "确定", "取消", new DialogUtils.OnDialogClickListener() {
                            @Override
                            public void onConfirm() {
                                switchSMSNotify(_notify);
                            }

                            @Override
                            public void onCancel() {

                            }
                        });
            }else{
                _notify = "0";
                switchSMSNotify(_notify);
            }

        }
    }

    @SuppressLint("AutoDispose")
    private void switchSMSNotify(final String _notify){
        showProgressDialog();
        ModifyMarkJsonRequest request = new ModifyMarkJsonRequest(PrefUtil.getLoginAccountUid(AlertSettingActivity.this),
                PrefUtil.getLoginToken(AlertSettingActivity.this),_notify,"","");
        NetWorkApi.provideRepositoryData().modifyMark(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        if (entity.getCode()==0){
                            smsMark = _notify;
                            PrefUtil.setSmsmark(smsMark,AlertSettingActivity.this);
                            initViews();
                        }else{
                            MyToast.showShortToast(entity.getMessage());
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        dissmissProgressDialog();
                    }
                    @Override
                    public void onComplete() {
                        dissmissProgressDialog();
                    }
                });
    }

    /**
     * 推送通知是否开启
     */
    @OnClick(R.id.iv_notify_phone)
    void setIv_notify_phone() {
        if(!DoubleClickUtil.isFastDoubleClick(R.id.iv_notify_phone)){
            final String _notify;
            if(phoneMark.equals("0")){
                _notify = "1";
                DialogUtils.showMyDialog(AlertSettingActivity.this, "提示", "关闭电话通知您将无法接收报警电话，确定这样操作吗？",
                        "确定", "取消", new DialogUtils.OnDialogClickListener() {
                            @Override
                            public void onConfirm() {
                                switchphoneNotify(_notify);
                            }

                            @Override
                            public void onCancel() {

                            }
                        });
            }else{
                _notify = "0";
                switchphoneNotify(_notify);
            }

        }
    }

    @SuppressLint("AutoDispose")
    private void switchphoneNotify(final String _notify){
        showProgressDialog();
        ModifyMarkJsonRequest request = new ModifyMarkJsonRequest(PrefUtil.getLoginAccountUid(AlertSettingActivity.this),
                PrefUtil.getLoginToken(AlertSettingActivity.this),"",_notify,"");
        NetWorkApi.provideRepositoryData().modifyMark(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        if (entity.getCode()==0){
                            phoneMark = _notify;
                            PrefUtil.setPhonemark(phoneMark,AlertSettingActivity.this);
                            initViews();
                        }else{
                            MyToast.showShortToast(entity.getMessage());
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        dissmissProgressDialog();
                    }
                    @Override
                    public void onComplete() {
                        dissmissProgressDialog();
                    }
                });
    }

    public void openShock() {
        iv_alert_shock.setImageResource(R.drawable.setting_open);
    }


    public void closeShock() {
        iv_alert_shock.setImageResource(R.drawable.setting_close);
        SpeakerAudioPlayerManager.getDefaultInstance().stopShock();
    }


    public void openMusicMode() {
        iv_alert_music.setImageResource(R.drawable.setting_open);
    }


    public void closeMusicMode() {
        iv_alert_music.setImageResource(R.drawable.setting_close);
        SpeakerAudioPlayerManager.getDefaultInstance().stopMusic();
    }

    public void openNotifyMode() {
        iv_notify_notice.setImageResource(R.drawable.setting_open);
    }


    public void closeNotifyMode() {
        iv_notify_notice.setImageResource(R.drawable.setting_close);
    }

    public void openSMSMode() {
        iv_notify_sms.setImageResource(R.drawable.setting_open);
    }


    public void closeSMSMode() {
        iv_notify_sms.setImageResource(R.drawable.setting_close);
    }

    public void openPhoneMode() {
        iv_notify_phone.setImageResource(R.drawable.setting_open);
    }


    public void closePhoneMode() {
        iv_notify_phone.setImageResource(R.drawable.setting_close);
    }

}