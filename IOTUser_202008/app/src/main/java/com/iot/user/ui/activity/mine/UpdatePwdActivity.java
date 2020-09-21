package com.iot.user.ui.activity.mine;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.iot.user.R;
import com.iot.user.app.IOTApplication;
import com.iot.user.base.BaseActivity;
import com.iot.user.databinding.ActivityForgetPsdBinding;
import com.iot.user.http.NetWorkApi;
import com.iot.user.http.model.BaseResponse;
import com.iot.user.http.net.RxUtils;
import com.iot.user.http.request.mine.ChangePasswordsonRequest;
import com.iot.user.skin.SkinManager;
import com.iot.user.utils.KeyboardUtils;
import com.iot.user.utils.MD5Util;
import com.iot.user.utils.MyToast;
import com.iot.user.utils.PrefUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.observers.DisposableObserver;

public class UpdatePwdActivity extends BaseActivity <ActivityForgetPsdBinding>{
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.et_old_pwd)
    EditText oldpwd;
    @BindView(R.id.et_new_pwd)
    EditText newpwd;
    @BindView(R.id.et_check_new_pwd)
    EditText checkNewpwd;
    @BindView(R.id.btn_update)
    Button btn_update;
    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_update_pwd;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        initMyToolBar();
    }
    private void initMyToolBar() {
        int currentSkinType = SkinManager.getCurrentSkinType(this);
        if (SkinManager.THEME_DAY == currentSkinType) {
            initToolBar(mToolbar, "修改密码", R.drawable.gank_ic_back_white);
        } else {
            initToolBar(mToolbar, "修改密码", R.drawable.gank_ic_back_night);
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

    @OnClick(R.id.ll_bg)
    public void ll_bg() {
        //隐藏输入法
        KeyboardUtils.hideSoftInput(this);
    }


    @SuppressLint("AutoDispose")
    @OnClick(R.id.btn_update)
    public void btn_update() {
        //隐藏输入法
        KeyboardUtils.hideSoftInput(this);

        String oldpwdstr = oldpwd.getText().toString();
        String newpwdstr = newpwd.getText().toString();
        String checkNewpwdstr = checkNewpwd.getText().toString();

        //获取数据
        if (TextUtils.isEmpty(oldpwdstr)) {
            MyToast.showShortToast("原密码不能为空");
            return;
        }

        if (TextUtils.isEmpty(newpwdstr)) {
            MyToast.showShortToast("原密码不能为空");
            return;
        }

        if(oldpwdstr.length()>16||newpwdstr.length()>16){
            MyToast.showShortToast("密码长度不超过16位");
            return;
        }
        if (!newpwdstr.equals(checkNewpwdstr)){
            MyToast.showShortToast("新密码输入不一致,请确认密码");
            return;
        }

        showProgressDialog("正在修改密码...");
        ChangePasswordsonRequest request = new ChangePasswordsonRequest(
                PrefUtil.getLoginAccountUid(UpdatePwdActivity.this),
                PrefUtil.getLoginToken(UpdatePwdActivity.this),
                MD5Util.md5(oldpwdstr),
                MD5Util.md5(newpwdstr));
        NetWorkApi.provideRepositoryData().changePassword(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        if(entity.getCode() == 0){
//                    MySnackbar.makeSnackBarRed(mToolbar, "密码修改成功");
                            MyToast.showShortToast("密码修改成功");
                            UpdatePwdActivity.this.finish();
                        }else{
//                    MySnackbar.makeSnackBarRed(mToolbar, msg);
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

}
