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
import com.iot.user.databinding.ActivityUpdateNicknameBinding;
import com.iot.user.http.NetWorkApi;
import com.iot.user.http.model.BaseResponse;
import com.iot.user.http.net.RxUtils;
import com.iot.user.http.request.mine.EditUserInfoJsonRequest;
import com.iot.user.skin.SkinManager;
import com.iot.user.utils.KeyboardUtils;
import com.iot.user.utils.MyToast;
import com.iot.user.utils.PrefUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.observers.DisposableObserver;

public class UpdateNicknameActivity extends BaseActivity<ActivityUpdateNicknameBinding> {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.et_nickname)
    EditText et_nickname;
    @BindView(R.id.btn_update)
    Button btn_update;
    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_update_nickname;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        String nickname = PrefUtil.getNickname(UpdateNicknameActivity.this);
        et_nickname.setText(nickname);
        if(nickname!=null && nickname.length() > 0){
            et_nickname.setSelection(nickname.length());
        }
        initMyToolBar();
    }
    private void initMyToolBar() {
        int currentSkinType = SkinManager.getCurrentSkinType(this);
        if (SkinManager.THEME_DAY == currentSkinType) {
            initToolBar(mToolbar, "修改真实姓名", R.drawable.gank_ic_back_white);
        } else {
            initToolBar(mToolbar, "修改真实姓名", R.drawable.gank_ic_back_night);
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

        final String nickname = et_nickname.getText().toString();

        String original = PrefUtil.getNickname(UpdateNicknameActivity.this);

        //获取数据
        if (TextUtils.isEmpty(nickname)) {
//            MySnackbar.makeSnackBarRed(mToolbar, "原密码不能为空");
            MyToast.showShortToast("真实姓名不能为空");
            return;
        }

        if(nickname.equals(original)){
            MyToast.showShortToast("真实姓名未修改");
            return;
        }

        showProgressDialog("正在修改真实姓名...");
        EditUserInfoJsonRequest request = new EditUserInfoJsonRequest(
                PrefUtil.getLoginAccountUid(UpdateNicknameActivity.this),
                PrefUtil.getLoginToken(UpdateNicknameActivity.this),
                PrefUtil.getUsername(UpdateNicknameActivity.this),
                PrefUtil.getPhone(UpdateNicknameActivity.this),
                nickname);
        NetWorkApi.provideRepositoryData().edituInfo(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        if (entity.getCode()==0) {
                            MyToast.showShortToast("真实姓名修改成功");
                            PrefUtil.setNickname(nickname, UpdateNicknameActivity.this);
                            UpdateNicknameActivity.this.finish();
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
}
