package com.iot.user.ui.activity.mine;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;

import com.iot.user.R;
import com.iot.user.app.IOTApplication;
import com.iot.user.base.BaseActivity;
import com.iot.user.databinding.ActivityCommonSettingBinding;
import com.iot.user.skin.SkinManager;
import com.iot.user.ui.view.mine.MySettingItemView;
import com.iot.user.utils.AppUpdateUtils;
import com.iot.user.utils.DoubleClickUtil;
import com.iot.user.utils.IntentUtils;
import com.iot.user.utils.MyToast;
import com.iot.user.utils.PrefUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CommonSettingActivity extends BaseActivity <ActivityCommonSettingBinding>{
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.item_app_update)
    MySettingItemView itemAppUpdate;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_common_setting;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        initMyToolBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        int newversion = PrefUtil.getNewVersion(IOTApplication.getIntstance());
        if (IOTApplication.getVersionCode() < newversion) {
            itemAppUpdate.setRedDot(true);
        } else {
            itemAppUpdate.setRedDot(false);
        }
    }

    private void initMyToolBar() {
        int currentSkinType = SkinManager.getCurrentSkinType(this);
        if (SkinManager.THEME_DAY == currentSkinType) {
            initToolBar(toolbar, "设置", R.drawable.gank_ic_back_white);
        } else {
            initToolBar(toolbar, "设置", R.drawable.gank_ic_back_night);
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
     * 跳转用户反馈界面
     */
    @OnClick(R.id.item_feedback)
    void item_feedback() {
        if (!DoubleClickUtil.isFastDoubleClick(R.id.item_alert_setting)) {
            startActivity(new Intent(CommonSettingActivity.this, FeedbackActivity.class));
        }

    }

    @OnClick(R.id.item_dev_support)
    void item_dev_support() {
        IntentUtils.startToWebActivity(CommonSettingActivity.this, "", "常见问题", "http://www.shmsiot.icoc.bz");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    AppUpdateUtils appUpdateUtils=null;/**app更新***/
    @OnClick(R.id.item_app_update)
    public void item_app_update() {
        if (appUpdateUtils==null){
            appUpdateUtils= new AppUpdateUtils(this);
            appUpdateUtils.appUpdateCheck();
        }else {
            if (appUpdateUtils.isCheckUpdate==false){
                appUpdateUtils.appUpdateCheck();
            }
        }
    }

}