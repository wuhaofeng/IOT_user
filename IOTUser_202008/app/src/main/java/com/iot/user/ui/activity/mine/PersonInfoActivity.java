package com.iot.user.ui.activity.mine;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.iot.user.R;
import com.iot.user.base.BaseActivity;
import com.iot.user.constant.HttpErrorCode;
import com.iot.user.databinding.ActivityPersonInfoBinding;
import com.iot.user.http.NetWorkApi;
import com.iot.user.http.model.BaseResponse;
import com.iot.user.http.net.RxUtils;
import com.iot.user.http.net.TokenInterceptor;
import com.iot.user.http.request.login.UnitFamilyListRequest;
import com.iot.user.skin.SkinManager;
import com.iot.user.ui.activity.login.LoginActivity;
import com.iot.user.ui.activity.main.UnitMainActivity;
import com.iot.user.ui.model.login.LoginModel;
import com.iot.user.ui.model.mine.UserInfoModel;
import com.iot.user.ui.receiver.HuaweiPushReceiver;
import com.iot.user.ui.service.SpeakerAudioPlayerManager;
import com.iot.user.utils.DateUtil;
import com.iot.user.utils.DialogUtils;
import com.iot.user.utils.HttpHeaderUtil;
import com.iot.user.utils.MyToast;
import com.iot.user.utils.PrefUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.observers.DisposableObserver;

public class PersonInfoActivity extends BaseActivity<ActivityPersonInfoBinding> {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tv_my_account)
    TextView tv_my_account;
    @BindView(R.id.tv_nickname)
    TextView tv_nickname;
    @BindView(R.id.tv_contact_phone)
    TextView tv_contact_phone;
    @BindView(R.id.tv_vip_date)
    TextView tv_vip_date;
    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_person_info;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        initMyToolBar();
        getUserInfo();
    }
    @Override
    protected void onResume() {
        super.onResume();
        initViews();
    }

    @SuppressLint("AutoDispose")
    private void getUserInfo(){
        showProgressDialog();
        UnitFamilyListRequest request=new UnitFamilyListRequest(PrefUtil.getLoginAccountUid(PersonInfoActivity.this),
                PrefUtil.getLoginToken(PersonInfoActivity.this));
        Observable obj=NetWorkApi.provideRepositoryData().uInfo(request)
                .compose(RxUtils.schedulersTransformer()); //线程调度
                obj.subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        if (entity.getCode()==0) {
                            try {
                                LinkedTreeMap map = (LinkedTreeMap) entity.getBody();
                                Gson gson = new Gson();
                                String jsonString = gson.toJson(map);
                                UserInfoModel resp = gson.fromJson(jsonString, UserInfoModel.class);
                                refreshView(resp);
//                        PrefUtil.setLoginAccount(resp.getUsername(),PersonInfoActivity.this);
                                PrefUtil.setNickname(resp.getNickName(), PersonInfoActivity.this);
                                PrefUtil.setPhone(resp.getPhone(), PersonInfoActivity.this);
                                PrefUtil.setUsername(resp.getUserName(), PersonInfoActivity.this);
                                PrefUtil.setPushmark(resp.getPush_mark(), PersonInfoActivity.this);
                                PrefUtil.setSmsmark(resp.getSms_mark(), PersonInfoActivity.this);
                            } catch (Exception e) {

                            }
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

    private void refreshView(UserInfoModel resp){
        tv_my_account.setText(resp.getUserName());
        tv_nickname.setText(resp.getNickName());
        tv_contact_phone.setText(resp.getPhone());
    }

    private void initViews(){
        tv_my_account.setText(PrefUtil.getUsername(PersonInfoActivity.this));
        tv_nickname.setText(PrefUtil.getNickname(PersonInfoActivity.this));
        tv_contact_phone.setText(PrefUtil.getPhone(PersonInfoActivity.this));
        tv_vip_date.setText(DateUtil.getYearDay(PrefUtil.getExpireTime(PersonInfoActivity.this)));
    }

    private void initMyToolBar() {
        int currentSkinType = SkinManager.getCurrentSkinType(this);
        if (SkinManager.THEME_DAY == currentSkinType) {
            initToolBar(mToolbar, "个人信息", R.drawable.gank_ic_back_white);
        } else {
            initToolBar(mToolbar, "个人信息", R.drawable.gank_ic_back_night);
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
     * 退出程序
     */
    @OnClick(R.id.btn_exit_account)
    public void btn_feedback() {
        DialogUtils.showMyDialog(PersonInfoActivity.this, "提示", "确定退出当前账号吗？", "确定", "取消", new DialogUtils.OnDialogClickListener() {
            @Override
            public void onConfirm() {
                TokenInterceptor.isRelogin = true;
                if(UnitMainActivity.gtMsgHandler!=null){
                    UnitMainActivity.gtMsgHandler.sendEmptyMessage(UnitMainActivity.EXIT_APP);
                }
                clickLoginOut();
                SpeakerAudioPlayerManager.getDefaultInstance().stopRing();
                PrefUtil.setIsInApp(false,PersonInfoActivity.this);
                Intent intent = new Intent(PersonInfoActivity.this, LoginActivity.class);
                intent.putExtra("isRelogin",false);
                PersonInfoActivity.this.startActivity(intent);
                /***1、检测华为设备，除了注册个推，还注册华为推送***/
                String brand = HttpHeaderUtil.getDevicename();
                if("HUAWEI".equals(brand.toUpperCase())){
                    Log.e("huawei-push","HMS connect destory");
                    HuaweiPushReceiver.unRegisterAllPushBacks();
                }
                //退出程序
//                ActivityUtils.finishAllActivities();
                PersonInfoActivity.this.finish();
            }

            @Override
            public void onCancel() {

            }
        });
    }


    @SuppressLint("AutoDispose")
    private void clickLoginOut(){
        UnitFamilyListRequest request=new UnitFamilyListRequest(PrefUtil.getLoginAccountUid(PersonInfoActivity.this),
                PrefUtil.getLoginToken(PersonInfoActivity.this));
        NetWorkApi.provideRepositoryData().logout(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
            @Override
            public void onNext(BaseResponse entity) {
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

    @OnClick(R.id.p_updatepwd)
    public void item_pwd() {
        startActivity(new Intent(PersonInfoActivity.this, UpdatePwdActivity.class));
    }

    @OnClick(R.id.p_nickname)
    public void updateNickname() {
        startActivity(new Intent(PersonInfoActivity.this, UpdateNicknameActivity.class));
    }


}