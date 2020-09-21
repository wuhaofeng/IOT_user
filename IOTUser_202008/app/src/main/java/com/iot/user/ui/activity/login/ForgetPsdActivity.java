package com.iot.user.ui.activity.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.iot.user.R;
import com.iot.user.app.IOTApplication;
import com.iot.user.base.BaseActivity;
import com.iot.user.base.BaseMvpActivity;
import com.iot.user.databinding.ActivityForgetPsdBinding;
import com.iot.user.http.model.BaseResponse;
import com.iot.user.skin.SkinManager;
import com.iot.user.ui.activity.main.UnitMainActivity;
import com.iot.user.ui.contract.login.ForgetPsdContract;
import com.iot.user.ui.fragment.login.LoginCodeFragment;
import com.iot.user.ui.presenter.login.ForgetPsdPresenter;
import com.iot.user.utils.AppValidationMgr;
import com.iot.user.utils.KeyboardUtils;
import com.iot.user.utils.MyToast;

import butterknife.BindView;

public class ForgetPsdActivity extends BaseMvpActivity<ForgetPsdPresenter, ActivityForgetPsdBinding> implements ForgetPsdContract.View {

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_forget_psd;
    }

    public static final String IntentKey_Mode = "IntentKey_Mode";
    Toolbar mToolbar;
    EditText mEtUserName;
    Button mBtnGetCode;
    EditText mEtOldPassword;
    EditText mEtNewPassword;
    Button mBtnOk;
    Button btn_ok_next;
    ImageView mIvYzm;
    RelativeLayout rl_forget_pwd_code;
    LinearLayout ll_forget_pwd_txt;
    @Override
    public void initView() {
        mPresenter=new ForgetPsdPresenter();
        mPresenter.attachView(this);
     mToolbar=(Toolbar) dataBinding.toolbar;
     mEtUserName=dataBinding.etUserName;
     mBtnGetCode=dataBinding.btnGetCode;
     mEtOldPassword=dataBinding.etOldPassword;
     mEtNewPassword=dataBinding.etNewPassword;
     mBtnOk=dataBinding.btnOk;
     btn_ok_next=dataBinding.btnOkNext;
     mIvYzm=dataBinding.ivYzm;
     rl_forget_pwd_code=dataBinding.rlForgetPwdCode;
     ll_forget_pwd_txt=dataBinding.llForgetPwdTxt;
        initMyToolBar();
        initViews();
    }
    private void initViews() {
        mIvYzm.setBackgroundResource(R.drawable.icon_user_yzm);
        mEtOldPassword.setHint("请输入验证码");
        mBtnGetCode.setVisibility(View.VISIBLE);
        mMyCountDownTimer = new MyCountDownTimer(60000, 1000);
    }

    private void initMyToolBar() {
        int currentSkinType = SkinManager.getCurrentSkinType(this);
        if (SkinManager.THEME_DAY == currentSkinType) {
            initToolBar(mToolbar, "找回密码", R.drawable.gank_ic_back_white);
        } else {
            initToolBar(mToolbar, "找回密码", R.drawable.gank_ic_back_night);
        }
    }
    private void changeToFogetMode(){
        btn_ok_next.setVisibility(View.GONE);
        mBtnOk.setVisibility(View.VISIBLE);
        rl_forget_pwd_code.setVisibility(View.GONE);
        ll_forget_pwd_txt.setVisibility(View.VISIBLE);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSuccess(BaseResponse bean, String type) {
        if (type.equals("change_psd")){
            //关闭页面
            closeLogin();
        }else if (type.equals("check_code")){
            changeToFogetMode();
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

    @Override
    protected void initClickBtn() {
        super.initClickBtn();
        mBtnGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_get_code();
            }
        });
        btn_ok_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_ok_next();
            }
        });
        mBtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_ok();
            }
        });
    }
    public void btn_get_code() {/**发送验证码***/
        //隐藏键盘
        KeyboardUtils.hideSoftInput(this);
        //获取用户名
        String userName = AppValidationMgr.removeStringSpace(mEtUserName.getText().toString(), 0);
        if (TextUtils.isEmpty(userName)) {
            MyToast.showShortToast("用户名不能为空");
            return;
        }
        //获取验证码
        mMyCountDownTimer.start();
        mBtnGetCode.setClickable(false);
        mBtnGetCode.setBackgroundColor(mContext.getResources().getColor(R.color.gray_light));
        mBtnGetCode.setText("60s 后再次发送");
        mPresenter.sendCodeBtn(userName,"2");
    }
    public void btn_ok_next() {/**验证验证码**/
        mPresenter.checkCodeBtn(mEtUserName.getText().toString().trim(),mEtOldPassword.getText().toString());
    }

    public void btn_ok() {/**修改密码**/
        //隐藏键盘
        KeyboardUtils.hideSoftInput(this);
        //获取用户名
        final String userName = AppValidationMgr.removeStringSpace(mEtUserName.getText().toString(), 0);
        //获取新密码
        String newPsd = mEtNewPassword.getText().toString();
        if (TextUtils.isEmpty(userName)) {
            MyToast.showShortToast("用户名不能为空");
            return;
        }
        if (TextUtils.isEmpty(newPsd)) {
            MyToast.showShortToast("新密码不能为空");
            return;
        }
        if (newPsd.length() > 16) {
            MyToast.showShortToast("密码长度不超过16位");
            return;
        }
        mPresenter.changePsd(userName,newPsd);
    }

    /**延迟2秒后刷新***/
    private  void closeLogin(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            @Override
            public void run() {
                startActivity(new Intent(ForgetPsdActivity.this, LoginActivity.class));
                ForgetPsdActivity.this.finish();
            }
        },1000);
    }
    private void resetBtn() {
        //重新给Button设置文字
        mBtnGetCode.setText("重新获取验证码");
        //设置可点击
        mBtnGetCode.setClickable(true);
        //恢复
        mBtnGetCode.setBackgroundColor(mContext.getResources().getColor(R.color.main_color));
    }

}