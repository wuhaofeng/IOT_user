package com.iot.user.ui.fragment.login;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.iot.user.R;
import com.iot.user.app.IOTApplication;
import com.iot.user.base.BaseFragment;
import com.iot.user.databinding.FragmentLoginPsdBinding;
import com.iot.user.ui.activity.login.LoginActivity;
import com.iot.user.utils.PrefUtil;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginPsdFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginPsdFragment extends BaseFragment<FragmentLoginPsdBinding> {
    public EditText mEtUserName;
    public EditText mEtPassword;
    public LoginPsdFragment() {
    }
    public static LoginPsdFragment newInstance() {
        LoginPsdFragment fragment = new LoginPsdFragment();
        return fragment;
    }

    @Override
    protected void initView(View view) {
        mEtUserName=dataBinding.etUserName;
        mEtPassword=dataBinding.etPassword;
        mEtUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if(mEtPassword !=null){
                    mEtPassword.setText("");
                }
                if(mEtPassword !=null){
                    ((LoginActivity)getActivity()).boxRememberPsd.setChecked(false);
                }
            }
        });
        initData();
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_login_psd;
    }
    private void initData(){
        String username = PrefUtil.getLoginAccount(IOTApplication.getIntstance());
        if(username!=null && !username.equals("")){
            mEtUserName.setText(username);
            mEtUserName.setSelection(username.length());
        }else {
            String phone = PrefUtil.getPhone(IOTApplication.getIntstance());
            if (phone != null && !phone.equals("")) {
                mEtUserName.setText(phone);
                mEtUserName.setSelection(phone.length());
            }
        }
        boolean isRemember = PrefUtil.getIsRememberPwd(IOTApplication.getIntstance());
        ((LoginActivity)getActivity()).boxRememberPsd.setChecked(isRemember);
        if(isRemember){
            mEtPassword.setText(PrefUtil.getLoginPwd(IOTApplication.getIntstance()));
        }else{
            mEtPassword.setText("");
        }
    }
}