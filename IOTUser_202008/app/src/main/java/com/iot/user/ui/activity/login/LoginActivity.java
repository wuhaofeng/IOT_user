package com.iot.user.ui.activity.login;

import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.huawei.android.hms.agent.HMSAgent;
import com.huawei.android.hms.agent.common.handler.ConnectHandler;
import com.huawei.android.hms.agent.push.handler.GetTokenHandler;
import com.igexin.sdk.PushManager;
import com.iot.user.R;
import com.iot.user.app.IOTApplication;
import com.iot.user.base.BaseMvpActivity;
import com.iot.user.databinding.ActivityLoginBinding;
import com.iot.user.http.model.BaseResponse;
import com.iot.user.http.net.TokenInterceptor;
import com.iot.user.ui.activity.main.UnitMainActivity;
import com.iot.user.ui.activity.register.RegisterActivity;
import com.iot.user.ui.adapter.login.LoginFragmentPagerAdapter;
import com.iot.user.ui.contract.login.LoginContract;
import com.iot.user.ui.fragment.login.LoginCodeFragment;
import com.iot.user.ui.fragment.login.LoginPsdFragment;
import com.iot.user.ui.model.login.LoginModel;
import com.iot.user.ui.presenter.login.LoginPresenter;
import com.iot.user.ui.receiver.HuaweiPushReceiver;
import com.iot.user.utils.AppUpdateUtils;
import com.iot.user.utils.AppValidationMgr;
import com.iot.user.utils.DialogUtils;
import com.iot.user.utils.DoubleClickUtil;
import com.iot.user.utils.HttpHeaderUtil;
import com.iot.user.utils.KeyboardUtils;
import com.iot.user.utils.LogUtil;
import com.iot.user.utils.MyToast;
import com.iot.user.utils.PrefUtil;
import com.iot.user.utils.TabLayoutUtils;
import com.jaeger.library.StatusBarUtil;
import com.luck.picture.lib.tools.ScreenUtils;
import java.util.ArrayList;
import java.util.List;

import static com.iot.user.ui.receiver.HuaweiPushReceiver.ACTION_TOKEN;

public class LoginActivity extends BaseMvpActivity<LoginPresenter,ActivityLoginBinding> implements LoginContract.View {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_login;
    }

    public CheckBox boxRememberPsd;
    TextView btn_forget;
    TabLayout tabLayout;
    ViewPager viewPager;
    private int selectTablayout=0;
    private List fragments=new ArrayList<>();
    private List<String> fragmentTitles=new ArrayList<>();
    private LoginPsdFragment oneFragment;
    private LoginCodeFragment twoFragment;
    @Override
    public void initView() {
        mPresenter = new LoginPresenter();
        mPresenter.attachView(this);
        boxRememberPsd=dataBinding.loginRememberPasswordCb;
        btn_forget=dataBinding.btnForget;
        tabLayout=dataBinding.tablayout;
        viewPager=dataBinding.viewpager;
        initBase();
    }
    private void initBase(){
        StatusBarUtil.setColor(this, getResources().getColor(R.color.main_color_login), 0);/**设置状态栏颜色*/
        boolean isRelogin = getIntent().getBooleanExtra("isRelogin",false);
        if(isRelogin){
            DialogUtils.showMyConfirmDialog(LoginActivity.this, "提示", "账号过期，请重新登录", "确定",  new DialogUtils.OnDialogClickListener() {
                @Override
                public void onConfirm() {

                }
                @Override
                public void onCancel() {
                }
            });
        }
        initViewPager();
        setImageViewHeight();
    }

    /***切换账号密码登录和验证码登录**/
    private void initViewPager(){
        TabLayoutUtils.reflex(tabLayout);/**设置tablayout底部的横县的长度***/
        String[] titles={"密码登录","验证码登录"};
        for (int i=0;i<titles.length;i++){
            if (i==0) {
                oneFragment = LoginPsdFragment.newInstance();
                fragmentTitles.add(titles[i]);
                fragments.add(oneFragment);
            }else {
                twoFragment = LoginCodeFragment.newInstance();
                fragmentTitles.add(titles[i]);
                fragments.add(twoFragment);
            }
        }
        //每项只进入一次
        viewPager.setAdapter(new LoginFragmentPagerAdapter(getSupportFragmentManager(),this,fragments,fragmentTitles));
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).select();//设置第一个为选中
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selectTablayout=tab.getPosition();
                if (selectTablayout==0){
                    boxRememberPsd.setVisibility(View.VISIBLE);
                    btn_forget.setVisibility(View.VISIBLE);
                }else {
                    boxRememberPsd.setVisibility(View.GONE);
                    btn_forget.setVisibility(View.GONE);
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    AppUpdateUtils appUpdateUtils=null;/**app更新***/
    @Override
    protected void onResume() {
        super.onResume();
        if (appUpdateUtils==null){
            appUpdateUtils= new AppUpdateUtils(this);
            appUpdateUtils.appUpdateCheck();
        }else {
            if (appUpdateUtils.isCheckUpdate==false){
                appUpdateUtils.appUpdateCheck();
            }
        }
    }

    public void clickIvConfig(){/**长按配置服务器端口**/
        if(DoubleClickUtil.getClickTimes() >= 5){
            DoubleClickUtil.clearCount();
            startActivity(new Intent(LoginActivity.this,ServerConfigActivity.class));
        }
    }

    public void btn_login() {/***登录**/
        //隐藏输入法
        KeyboardUtils.hideSoftInput(this);
        if(selectTablayout==0){
            passwdLogin();
        }else {
            codeLogin();
        }
    }
    private void passwdLogin() {/***密码登录**/
        if (oneFragment == null || oneFragment.mEtUserName == null) {
            return;
        }
        //获取数据
        final String userName = AppValidationMgr.removeStringSpace(oneFragment.mEtUserName.getText().toString(), 0);
        final String userPsd = oneFragment.mEtPassword.getText().toString();
        mPresenter.loginWithPwd(userName,userPsd);
    }
    private void codeLogin() {/***验证码登录**/
        if (twoFragment == null || twoFragment.et_user_phone == null) {
            return;
        }
        final String userPhone = AppValidationMgr.removeStringSpace(twoFragment.et_user_phone.getText().toString(), 0);
        final String userCode = twoFragment.et_unit_code.getText().toString();
        mPresenter.loginWithCode(userPhone,userCode);
    }


    @Override
    public void onSuccess(BaseResponse bean, String type) {
      if (type.equals("codelogin")){/***验证码登录**/
          updateLoginData(bean,false);
      }else if (type.equals("pwdlogin")){
          updateLoginData(bean,true);
      }
    }

    private void updateLoginData(BaseResponse bean,boolean isSavePwd){
        final String userName = AppValidationMgr.removeStringSpace(oneFragment.mEtUserName.getText().toString(), 0);
        final String userPsd = oneFragment.mEtPassword.getText().toString();
        final boolean isRember = boxRememberPsd.isChecked();
        int errCode=bean.getCode();
        if (errCode==0){
        LoginModel resp=(LoginModel) bean.getBody();
        String userId = resp.getuId();
        String loginToken = resp.getToken();
        String nickname = resp.getNickName();
        String phone = resp.getPhone();
        String username = resp.getUsername();
        PrefUtil.setLoginAccount(userName,LoginActivity.this);
        PrefUtil.setLoginAccountUid(userId,LoginActivity.this);
        PrefUtil.setLoginToken(loginToken,LoginActivity.this);
        PrefUtil.setNickname(nickname,LoginActivity.this);
        PrefUtil.setPhone(phone,LoginActivity.this);
        PrefUtil.setUsername(username,LoginActivity.this);
        PrefUtil.setIsRememberPwd(isRember,LoginActivity.this);
        PrefUtil.setVIPLevel(resp.getGrade(),LoginActivity.this);
        PrefUtil.setExpireTime(resp.getExpire_time(),LoginActivity.this);

        PrefUtil.setPushmark(resp.getPush_mark(),LoginActivity.this);
        PrefUtil.setSmsmark(resp.getSms_mark(),LoginActivity.this);
        PrefUtil.setPhonemark(resp.getPhone_mark(),LoginActivity.this);
        if (isSavePwd) {/***密码登录**/
            if (isRember) {
                PrefUtil.setLoginPwd(userPsd, LoginActivity.this);
            } else {
                PrefUtil.setLoginPwd("", LoginActivity.this);
            }
        }
        PrefUtil.setIsInApp(true,LoginActivity.this);
        PushManager.getInstance().bindAlias(LoginActivity.this, PrefUtil.getLoginAccount(LoginActivity.this));
        LogUtil.e("qingchen-token","uid="+userId+"--token="+loginToken);

        TokenInterceptor.isRelogin = false;//重置状态
        //关闭页面
        closeLogin();
        initHuaweiPush();
    }else{
        /****/
        PrefUtil.setLoginAccount(userName,LoginActivity.this);
        MyToast.showShortToast(bean.getMessage());
    }
}

    /**延迟2秒后刷新***/
    private  void closeLogin(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            @Override
            public void run() {
                startActivity(new Intent(LoginActivity.this, UnitMainActivity.class));
                LoginActivity.this.finish();
            }
        },1000);
    }

    private void setImageViewHeight(){
        ImageView imageView=findViewById(R.id.iv_bg_login);
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.height= ScreenUtils.getScreenWidth(this)*2560/1440;
        imageView.setLayoutParams(lp);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
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
        dataBinding.ivConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickIvConfig();
            }
        });
        dataBinding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_login();
            }
        });
        dataBinding.btnForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               btn_forget();
            }
        });
        dataBinding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_register();
            }
        });
        dataBinding.llBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KeyboardUtils.hideSoftInput(LoginActivity.this);
            }
        });
    }
    public void btn_forget() {
        if(!DoubleClickUtil.isFastDoubleClick(R.id.btn_forget)){
            Intent intent = new Intent(this, ForgetPsdActivity.class);
            intent.putExtra(ForgetPsdActivity.IntentKey_Mode, 2);
            startActivity(intent);
        }
    }
    public void btn_register() {
        if(!DoubleClickUtil.isFastDoubleClick(R.id.btn_register)){
            startActivity(new Intent(this, RegisterActivity.class));
        }

    }

}