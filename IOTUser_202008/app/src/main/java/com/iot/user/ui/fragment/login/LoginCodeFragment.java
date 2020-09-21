package com.iot.user.ui.fragment.login;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.iot.user.R;
import com.iot.user.base.BaseFragment;
import com.iot.user.base.BaseMvpFragment;
import com.iot.user.databinding.FragmentLoginCodeBinding;
import com.iot.user.http.NetWorkApi;
import com.iot.user.http.model.BaseResponse;
import com.iot.user.http.request.login.VcodeSendJsonRequest;
import com.iot.user.ui.contract.login.LoginContract;
import com.iot.user.ui.model.login.LoginModel;
import com.iot.user.ui.presenter.login.LoginPresenter;
import com.iot.user.utils.AppValidationMgr;
import com.iot.user.utils.KeyboardUtils;
import com.iot.user.utils.MyToast;
import com.iot.user.utils.PrefUtil;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginCodeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginCodeFragment extends BaseMvpFragment<LoginPresenter,FragmentLoginCodeBinding> implements LoginContract.View{
    public EditText et_user_phone;
    public EditText et_unit_code;
    public Button btn_click_code_send;
    private MyCountDownTimer mMyCountDownTimer;
    public static LoginCodeFragment newInstance() {
        LoginCodeFragment fragment = new LoginCodeFragment();
        return fragment;
    }

    @Override
    protected void initView(View view) {
        mPresenter = new LoginPresenter();
        mPresenter.attachView(this);
        et_user_phone=dataBinding.etUserPhone;
        et_unit_code=dataBinding.etUnitCode;
        btn_click_code_send=dataBinding.btnClickCodeSend;
        dataBinding.btnClickCodeSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickCodeSend();/***发送验证码**/
            }
        });
        initData();
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_login_code;
    }

    private void initData(){
        mMyCountDownTimer = new MyCountDownTimer(60000, 1000);
        String phone = PrefUtil.getPhone(context);
        if(phone!=null && !phone.equals("")){
            et_user_phone.setText(phone);
            et_user_phone.setSelection(phone.length());
        }
    }

    void clickCodeSend(){/**获取验证码**/
        KeyboardUtils.hideSoftInput(getActivity());
        //获取用户名
        String userName = et_user_phone.getText().toString().trim();
        if (!AppValidationMgr.isPhone(userName)) {
            MyToast.showShortToast("手机号不能为空");
            return;
        }
        //获取验证码
        mMyCountDownTimer.start();
        btn_click_code_send.setClickable(false);
        btn_click_code_send.setBackgroundColor(getActivity().getResources().getColor(R.color.gray_light));
        btn_click_code_send.setText("60s 后再次发送");
        mPresenter.sendCodeBtn(userName,"4");
    }

    @Override
    public void onSuccess(BaseResponse bean,String type) {
        if (type.equals("sendcode")) {
            MyToast.showShortToast(bean.getMessage());
            mMyCountDownTimer.cancel();
            mMyCountDownTimer.onFinish();
        }
    }

    @Override
    public void onError(String errMessage) {
        super.onError(errMessage);
        MyToast.showShortToast(getString(R.string.send_vcode_fail));
        mMyCountDownTimer.cancel();
        mMyCountDownTimer.onFinish();
    }

    private class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }
        @Override
        public void onTick(long l) {
            if (btn_click_code_send!=null) {
                btn_click_code_send.setText(l / 1000 + "s 后再次发送");
            }
        }
        @Override
        public void onFinish() {
            resetBtn();

        }
    }
    private void resetBtn() {
        if (getActivity()!=null&&btn_click_code_send!=null) {
            //重新给Button设置文字
            btn_click_code_send.setText("重新获取验证码");
            //设置可点击
            btn_click_code_send.setClickable(true);
            //恢复
            btn_click_code_send.setBackgroundColor(getActivity().getResources().getColor(R.color.main_color));
        }
    }

}