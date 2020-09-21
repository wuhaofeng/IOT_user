package com.iot.user.ui.activity.register;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huawei.android.hms.agent.HMSAgent;
import com.huawei.android.hms.agent.common.handler.ConnectHandler;
import com.huawei.android.hms.agent.push.handler.GetTokenHandler;
import com.igexin.sdk.PushManager;
import com.iot.user.R;
import com.iot.user.app.IOTApplication;
import com.iot.user.base.BaseMvpActivity;
import com.iot.user.databinding.ActivityRegisterBinding;
import com.iot.user.http.model.BaseResponse;
import com.iot.user.skin.SkinManager;
import com.iot.user.ui.activity.login.ForgetPsdActivity;
import com.iot.user.ui.activity.login.LoginActivity;
import com.iot.user.ui.activity.main.UnitMainActivity;
import com.iot.user.ui.contract.register.RegisterContract;
import com.iot.user.ui.presenter.register.RegisterPresenter;
import com.iot.user.ui.receiver.HuaweiPushReceiver;
import com.iot.user.utils.AppValidationMgr;
import com.iot.user.utils.HttpHeaderUtil;
import com.iot.user.utils.IntentUtils;
import com.iot.user.utils.KeyboardUtils;
import com.iot.user.utils.LogUtil;
import com.iot.user.utils.MyToast;
import com.iot.user.utils.PrefUtil;

import butterknife.BindView;

import static com.iot.user.ui.receiver.HuaweiPushReceiver.ACTION_TOKEN;

public class RegisterActivity extends BaseMvpActivity<RegisterPresenter, ActivityRegisterBinding> implements RegisterContract.View {
    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_register;
    }
    Toolbar mToolbar;
    EditText mEtPhone;/***手机号**/
    RelativeLayout rl_reg_vcode;
    EditText mEtOldPassword;/***验证码**/
    Button mBtnGetCode;/***获取验证码**/
    CheckBox cb_user_law;
    LinearLayout ll_user_law;
    TextView tv_law;/***使用协议**/
    Button btn_register_next;/***下一步**/

    LinearLayout ll_reg_pwd;
    EditText mEtPassword;/***请输入密码**/
    LinearLayout ll_reg_nickname;
    EditText et_nickname;/***请输入真实姓名**/
    Button btn_register;/***注册**/
    @Override
    public void initView() {
        mPresenter=new RegisterPresenter();
        mPresenter.attachView(this);
      mToolbar=(Toolbar) dataBinding.toolbar;
      mEtPhone=dataBinding.etUserName;
      rl_reg_vcode=dataBinding.rlRegVcode;
      mEtOldPassword=dataBinding.etOldPassword;
      mEtPassword=dataBinding.etPassword;
      mBtnGetCode=dataBinding.btnGetCode;
      cb_user_law=dataBinding.cbUserLaw;
      ll_user_law=dataBinding.llUserLaw;
      tv_law=dataBinding.tvLaw;
      btn_register_next=dataBinding.btnRegisterNext;
      ll_reg_nickname=dataBinding.llRegNickname;
      ll_reg_pwd=dataBinding.llRegPwd;
      et_nickname=dataBinding.etNickname;
      btn_register=dataBinding.btnRegister;
        initViews();
        initMyToolBar();
    }
    private void initViews() {
        mEtOldPassword.setHint("请输入验证码");
        mBtnGetCode.setVisibility(View.VISIBLE);
        btn_register_next.setEnabled(false);
        mMyCountDownTimer = new MyCountDownTimer(60000, 1000);
        cb_user_law.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    btn_register_next.setEnabled(true);
                }else{
                    btn_register_next.setEnabled(false);
                }
            }
        });
    }


    private void initMyToolBar() {
        int currentSkinType = SkinManager.getCurrentSkinType(this);
        if (SkinManager.THEME_DAY == currentSkinType) {
            initToolBar(mToolbar, "注册", R.drawable.gank_ic_back_white);
        } else {
            initToolBar(mToolbar, "注册", R.drawable.gank_ic_back_night);
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

    private void changeToRegMode(){
        btn_register_next.setVisibility(View.GONE);
        ll_user_law.setVisibility(View.GONE);
        rl_reg_vcode.setVisibility(View.GONE);
        btn_register.setVisibility(View.VISIBLE);
        ll_reg_pwd.setVisibility(View.VISIBLE);
        ll_reg_nickname.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSuccess(BaseResponse bean, String type) {
        if (type.equals("register")){
            initHuaweiPush();
            //关闭页面
            closeLogin();
        }else if (type.equals("check_code")){
            changeToRegMode();
        }else if (type.equals("sendcode")){
            resetTimer();
        }
    }

    @Override
    public void resetTimer() {
        mMyCountDownTimer.cancel();
        mMyCountDownTimer.onFinish();
    }

    private MyCountDownTimer mMyCountDownTimer;
    private class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }
        @Override
        public void onTick(long l) {
            mBtnGetCode.setText(l / 1000 + "s 后再次发送");
        }
        @Override
        public void onFinish() {
            resetBtn();
        }
    }
    private void resetBtn() {
        //重新给Button设置文字
        mBtnGetCode.setText("重新获取验证码");
        //设置可点击
        mBtnGetCode.setClickable(true);
        //恢复
        mBtnGetCode.setBackgroundColor(mContext.getResources().getColor(R.color.main_color));
    }
    /**延迟2秒后刷新***/
    private  void closeLogin(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            @Override
            public void run() {
                startActivity(new Intent(RegisterActivity.this, UnitMainActivity.class));
                RegisterActivity.this.finish();
            }
        },1000);
    }

    /***华为处理***/
    private void initHuaweiPush(){
        /***1、检测华为设备，除了注册个推，还注册华为推送***/
        String brand = HttpHeaderUtil.getDevicename();
        if("HUAWEI".equals(brand.toUpperCase())){
            pluginPushData("");
            HMSAgent.connect(this, new ConnectHandler() {
                @Override
                public void onConnect(int rst) {
                    Log.e("huawei-push","HMS connect end1:" + rst);
                    HMSAgent.Push.getToken(new GetTokenHandler() {
                        @Override
                        public void onResult(int rst) {
                            LogUtil.e("huawei-push","HMS connect end2:"+rst);
                        }
                    });
                }
            });
            HuaweiPushReceiver.registerPushCallback(new HuaweiPushReceiver.IPushCallback() {
                @Override
                public void onReceive(Intent intent) {
                    if (intent != null) {
                        String action = intent.getAction();
                        Bundle b = intent.getExtras();
                        if (b != null && ACTION_TOKEN.equals(action)) {
                            String token = b.getString(ACTION_TOKEN);
                            LogUtil.e("huawei-push","huawei push token login= "+token);
                            pluginPushData(token);
                        }
            /*else if (b != null && ACTION_UPDATEUI.equals(action)) {
                String log = b.getString("log");
                LogUtil.e("huawei-push","huawei push log = "+log);
            }*/
                    }
                }
            });
        }else{
            pluginPushData("");
        }
    }
    private void pluginPushData(String token){/***绑定个推cid和华为token**/
        mPresenter.pluginPush(token);
    }

    @Override
    protected void initClickBtn() {
        super.initClickBtn();
        mBtnGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_get_code();
            }
        });
        btn_register_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_checkCode();
            }
        });
        tv_law.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_law();
            }
        });
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_register();
            }
        });
    }

    public void btn_get_code() {
        //隐藏键盘
        KeyboardUtils.hideSoftInput(this);
        //获取用户名
        String userName = AppValidationMgr.removeStringSpace(mEtPhone.getText().toString(), 0);
        if (!AppValidationMgr.isPhone(userName)) {
//            MySnackbar.makeSnackBarRed(mToolbar, "用户名不能为空");
            MyToast.showShortToast("请输入正确的手机号");
            return;
        }
        //获取验证码
        mMyCountDownTimer.start();
        mBtnGetCode.setClickable(false);
        mBtnGetCode.setBackgroundColor(mContext.getResources().getColor(R.color.gray_light));
        mBtnGetCode.setText("60s 后再次发送");
        mPresenter.sendCodeBtn(userName,"1");
    }
    public void btn_checkCode() {/**下一步，验证验证码*/
        //获取用户名
        String userName= AppValidationMgr.removeStringSpace(mEtPhone.getText().toString(),0);
        if (!AppValidationMgr.isPhone(userName)) {
            MyToast.showShortToast("请输入正确的手机号");
            return;
        }
        if(TextUtils.isEmpty(mEtOldPassword.getText().toString())){
            MyToast.showShortToast("请输入验证码");
            return;
        }
        if(cb_user_law != null && !cb_user_law.isChecked()){
            MyToast.showShortToast("请先仔细阅读并同意《用户使用协议及隐私政策》");
            return;
        }
        mPresenter.checkCodeBtn(mEtPhone.getText().toString().trim(),mEtOldPassword.getText().toString());
    }
    public void tv_law() {/***查看协议**/
        IntentUtils.startToWebActivity(RegisterActivity.this, "", "用户协议及隐私", "http://www.shmsiot.cn/隐私政策.html");
    }
    public void btn_register() {
        //隐藏输入法
        KeyboardUtils.hideSoftInput(this);

        final String userName = AppValidationMgr.removeStringSpace(mEtPhone.getText().toString(), 0);
        String userPsd = mEtPassword.getText().toString();
        String nickname = et_nickname.getText().toString();
        //获取数据
        if (!AppValidationMgr.isPhone(userName)) {
            MyToast.showShortToast("手机号不可为空");
            return;
        }
        if (TextUtils.isEmpty(userPsd)) {
            MyToast.showShortToast("密码不可为空");
            return;
        }
        if (userPsd.length() < 6) {
            MyToast.showShortToast("密码不能少于6位");
            return;
        }
        if (TextUtils.isEmpty(nickname)) {
            MyToast.showShortToast("真实姓名不可为空");
            return;
        }
        mPresenter.register(userName,userPsd,nickname);
    }
}