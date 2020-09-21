package com.iot.user.ui.activity.welcome;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.iot.user.R;
import com.iot.user.app.IOTApplication;
import com.iot.user.base.BaseActivity;
import com.iot.user.databinding.ActivityWelcomeBinding;
import com.iot.user.http.net.RetrofitClient;
import com.iot.user.skin.SkinManager;
import com.iot.user.ui.activity.login.LoginActivity;
import com.iot.user.ui.activity.main.UnitMainActivity;
import com.iot.user.utils.MyToast;
import com.iot.user.utils.NetUtil;
import com.iot.user.utils.PrefUtil;

public class WelcomeActivity extends BaseActivity<ActivityWelcomeBinding> {

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_welcome;
    }
    TextView tv_app_version;
    TextView shadeBg;
    @Override
    public void initView() {
     tv_app_version= dataBinding.tvAppVersion;
     shadeBg=dataBinding.shadeBg;
        int currentSkinType = SkinManager.getCurrentSkinType(this);
        if (currentSkinType == SkinManager.THEME_NIGHT) {
            shadeBg.setVisibility(View.VISIBLE);
        }

        /***判断版本号，确定是否重新读取数据*/
        Log.e("33333", "onCreate: "+ IOTApplication.getVersionCode()+","+ PrefUtil.getSaveVersion(IOTApplication.getIntstance()));
        if (IOTApplication.getVersionCode()>PrefUtil.getSaveVersion(IOTApplication.getIntstance())){
            PrefUtil.setServerIP(RetrofitClient.DEFAULT_IP, this);
            PrefUtil.setServerPort(RetrofitClient.DEFAULT_PORT,this);
            PrefUtil.setServerVersion(RetrofitClient.DEFAULT_VERSION,this);
            PrefUtil.setSaveVersion(IOTApplication.getVersionCode(),IOTApplication.getIntstance());
        }
//防止切换后台再启动时总是回到启动页
        if((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0){
            finish();
            return;
        }
        goToDetailActivity();
        tv_app_version.setText("V " + IOTApplication.getVersionName());
    }
    private void goToDetailActivity(){
        if (NetUtil.getNetWorkState(this)<0) {
            MyToast.showShortToast("未获取到网络，请检查网络链接");
            startActivity(new Intent(this, UnitMainActivity.class));
        }else {
            boolean isFirstLogin = PrefUtil.getIsFirstLogin(this);
            boolean isInAPP = PrefUtil.getIsInApp(this);
            if (isFirstLogin) {
                PrefUtil.setIsFirstLogin(false, this);
                startActivity(new Intent(this, GuidePageActivity.class));
                finish();
            } else {
                if (isInAPP) {
                    startActivity(new Intent(this, UnitMainActivity.class));
                } else {
                    startActivity(new Intent(this, LoginActivity.class));
                }
                this.finish();
            }
        }
    }

}