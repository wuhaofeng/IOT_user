package com.iot.user.ui.activity.main;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

import com.afollestad.materialdialogs.MaterialDialog;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.ashokvarma.bottomnavigation.TextBadgeItem;
import com.huawei.android.hms.agent.HMSAgent;
import com.huawei.android.hms.agent.common.handler.ConnectHandler;
import com.huawei.android.hms.agent.push.handler.GetTokenHandler;
import com.iot.user.R;
import com.iot.user.app.IOTApplication;
import com.iot.user.base.BaseMvpActivity;
import com.iot.user.databinding.ActivityUnitMainBinding;
import com.iot.user.http.model.BaseResponse;
import com.iot.user.skin.SkinBroadcastReceiver;
import com.iot.user.ui.activity.login.LoginActivity;
import com.iot.user.ui.activity.shopping.ShoppingDeviceListActivity;
import com.iot.user.ui.contract.main.UnitMainContract;
import com.iot.user.ui.fragment.main.UnitHomeFragment;
import com.iot.user.ui.fragment.message.UnitMessageFragment;
import com.iot.user.ui.fragment.mine.UnitMineFragment;
import com.iot.user.ui.fragment.notification.UnitNotificationFragment;
import com.iot.user.ui.presenter.login.LoginPresenter;
import com.iot.user.ui.presenter.main.UnitMainPresenter;
import com.iot.user.ui.receiver.HuaweiPushReceiver;
import com.iot.user.ui.service.SpeakerAudioPlayerManager;
import com.iot.user.utils.AppUpdateUtils;
import com.iot.user.utils.DialogUtils;
import com.iot.user.utils.HttpHeaderUtil;
import com.iot.user.utils.IntentUtils;
import com.iot.user.utils.LogUtil;
import com.iot.user.utils.NotifyUtil;
import com.iot.user.utils.PrefUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import static com.iot.user.ui.receiver.HuaweiPushReceiver.ACTION_TOKEN;

public class UnitMainActivity extends BaseMvpActivity<UnitMainPresenter, ActivityUnitMainBinding> implements UnitMainContract.View, BottomNavigationBar.OnTabSelectedListener{
    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_unit_main;
    }
    BottomNavigationBar bottomNavigationBar;
    public static Handler gtMsgHandler =null;/***家庭共享与设备共享跳转***/
    public static final int EXIT_APP = 0x11;
    public static final int EXIT_TO_LOGIN = 0x12;
    private static final String savedInstanceStateItemId = "navigationCheckedItemId";
    private static final String savedInstanceStateTitle = "navigationCheckedTitle";
    private int navigationCheckedItemId = R.id.nav_fuli;
    private String navigationCheckedTitle = "明厦智慧燃气";
    private boolean goDevice=false;

    private UnitHomeFragment mainFragment;
    private UnitMessageFragment messageFragment;/**消息**/
    private UnitNotificationFragment noticeListFragment;/**公告**/
    private UnitMineFragment settingsFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initOtherDatas(savedInstanceState);
    }

    @Override
    public void initView() {
        mPresenter = new UnitMainPresenter();
        mPresenter.attachView(this);
        bottomNavigationBar=dataBinding.bottomNavigationBar;
        if (IOTApplication.getIntstance().mContext==null)
            IOTApplication.getIntstance().mContext=this;
        initHuaweiPush();
        if (gtMsgHandler==null)
            gtMsgHandler = new GTMsgHandler(this);
        initIntent();
        setDefaultFragment();

        bottomNavigationBar.setTabSelectedListener(this);
        refresh();
        initUnitStatus();
    }
    private void initIntent() {
        Intent intent = getIntent();
        String pushMessage = intent.getStringExtra(IntentUtils.PushMessage);
        if (!TextUtils.isEmpty(pushMessage)) {
            DialogUtils.showMyDialog(this,
                    getString(R.string.gank_dialog_title_notify),
                    pushMessage,
                    getString(R.string.gank_dialog_confirm),
                    "", null);
        }
    }
    private void initOtherDatas(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.getInt(savedInstanceStateItemId) != 0) {
            navigationCheckedItemId = savedInstanceState.getInt(savedInstanceStateItemId);
            navigationCheckedTitle = savedInstanceState.getString(savedInstanceStateTitle);
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(savedInstanceStateItemId, navigationCheckedItemId);
        outState.putString(savedInstanceStateTitle, navigationCheckedTitle);
        super.onSaveInstanceState(outState);
    }
    /**
     * 设置默认的Fragment显示：如果savedInstanceState不是空，证明activity被后台销毁重建了，之前有fragment，就不再创建了
     */
    private void setDefaultFragment() {
        // 开启一个Fragment事务
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(fragmentTransaction);
        if (mainFragment == null) {
            mainFragment = UnitHomeFragment.newInstance();
            fragmentTransaction.add(R.id.frame_content, mainFragment);
        } else {
            fragmentTransaction.show(mainFragment);
        }
        fragmentTransaction.commit();
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (settingsFragment != null) {
            transaction.hide(settingsFragment);
        }
        if (noticeListFragment != null) {
            transaction.hide(noticeListFragment);
        }
        if(mainFragment != null){
            transaction.hide(mainFragment);
        }
        if(messageFragment != null){
            transaction.hide(messageFragment);
        }
    }
    private TextBadgeItem mBadgeItem;
    private void refresh() {
        bottomNavigationBar.clearAll();
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);

        mBadgeItem = new TextBadgeItem()
                .setBorderWidth(4)
                .setAnimationDuration(0)
                .setBackgroundColorResource(R.color.red)
                .setHideOnSelect(false)
                .setText("");
        mBadgeItem.hide();
        BottomNavigationItem messageItem=new BottomNavigationItem(R.drawable.ic_launch_white_24dp, "消息").setActiveColorResource(R.color.colorPrimary);
        messageItem.setBadgeItem(mBadgeItem);
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.ic_home_white_24dp, "主页").setActiveColorResource(R.color.colorPrimary))
                .addItem(messageItem)
                .addItem(new BottomNavigationItem(R.drawable.ic_book_white_24dp, "公告").setActiveColorResource(R.color.colorPrimary))
                .addItem(new BottomNavigationItem(R.drawable.ic_favorite_white_24dp, "我的").setActiveColorResource(R.color.colorPrimary))
                .setFirstSelectedPosition(goDevice?3:0)
                .initialise();
        if (goDevice){
            onTabSelected(3);
        }
    }
    private void setBadgeNum(int num) {
        if (mBadgeItem!=null) {
            if (num == 0) {
                mBadgeItem.setText("");
                mBadgeItem.hide();
            } else {
                mBadgeItem.setText(String.valueOf(num));
                mBadgeItem.show();
            }
        }
    }
    public void getFamilyShareCount() {/**刷新未读消息的数字**/
        mPresenter.getFamilyShareCount();
    }
    private void setScrollableText(int position) {
        // 开启一个Fragment事务
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(fragmentTransaction);
        switch (position) {
            case 0:
                if (mainFragment == null) {
                    mainFragment = UnitHomeFragment.newInstance();
                    fragmentTransaction.add(R.id.frame_content, mainFragment);
                } else {
                    fragmentTransaction.show(mainFragment);
                }
                break;
            case 1:
                if (messageFragment == null) {
                    messageFragment = UnitMessageFragment.newInstance();
                    fragmentTransaction.add(R.id.frame_content, messageFragment);
                } else {
                    fragmentTransaction.show(messageFragment);
                }
                break;

            case 2:
                if (noticeListFragment == null) {
                    noticeListFragment = UnitNotificationFragment.newInstance();
                    fragmentTransaction.add(R.id.frame_content, noticeListFragment);
                } else {
                    fragmentTransaction.show(noticeListFragment);
                }
                break;

            case 3:
                if (settingsFragment == null) {
                    settingsFragment = UnitMineFragment.newInstance();
                    fragmentTransaction.add(R.id.frame_content, settingsFragment);
                } else {
                    fragmentTransaction.show(settingsFragment);
                }
                break;

        }
        fragmentTransaction.commitAllowingStateLoss();
    }

    private static class GTMsgHandler extends Handler {/***家庭共享和设备共享跳转***/
      private WeakReference<UnitMainActivity> weakReference;
        public GTMsgHandler(UnitMainActivity activity){
            weakReference = new WeakReference<>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            UnitMainActivity activity = weakReference.get();
            if (null != activity){
                super.handleMessage(msg);
                switch (msg.what){
                    case 4:/**切换到消息中的设备共享列表**/
                        activity.scrollTabSelected(1);
                        activity.updateData(2);
                        break;
                    case 5:/**切换到消息中的家庭共享列表**/
                        activity.scrollTabSelected(1);
                        activity.updateData(1);
                        break;
                    case 3:/***刷新首页**/
                        if (activity.mainFragment!=null) {
                            activity.mainFragment.updateFragment();
                        }
                        break;
                    case 2:/**刷新未读消息的数字**/
                        activity.getFamilyShareCount();
                        break;
                    case EXIT_APP:/**退出app**/
                        activity.finish();
                        break;
                    case EXIT_TO_LOGIN:/**退出登录**/
                        SpeakerAudioPlayerManager.getDefaultInstance().stopRing();
                        PrefUtil.setIsInApp(false,activity);
                        Intent intent = new Intent(activity, LoginActivity.class);
                        intent.putExtra("isRelogin",true);
                        activity.startActivity(intent);
                        //退出程序
                        activity.finish();
                        break;
                    default:
                        break;
                }
            }
        }
    }
    /**延迟3秒后刷新***/
    private  void updateData(final int mute){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (messageFragment.viewpagertab!=null) {
                    messageFragment.viewpagertab.getTabAt(mute).select();
                }
            }
        },1500);
    }
    public  void scrollTabSelected(int position){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                invalidateOptionsMenu();
                setScrollableText(position);
                bottomNavigationBar.setFirstSelectedPosition(position).initialise();
            }
        },500);

    }

    /***ListenDelegate***/
    @Override
    public void onTabSelected(int position) {
        scrollTabSelected(position);
        if (position==1){
            getFamilyShareCount();
        }
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }
    @Override
    protected void onNewIntent(Intent intent) {/***重新进入此页面，Manifest中对Activity设置lanuchMode=“singleTask”**/
        super.onNewIntent(intent);
        /***判断是否要跳转到设备续费**/
        if (intent!=null){
            goDevice=intent.getBooleanExtra("goDeviceList",false);
            if (goDevice){
                Intent deviceIntent= new Intent(mContext, ShoppingDeviceListActivity.class);
                startActivity(deviceIntent);
            }
        }
        getFamilyShareCount();
    }

    AppUpdateUtils appUpdateUtils=null;/**app更新***/
    @Override
    protected void onResume() {
        super.onResume();
        getFamilyShareCount();
        if (appUpdateUtils==null){
            appUpdateUtils= new AppUpdateUtils(this);
            appUpdateUtils.appUpdateCheck();
        }else {
            if (appUpdateUtils.isCheckUpdate==false){
                appUpdateUtils.appUpdateCheck();
            }
        }
    }

    @Override
    public void onSuccess(BaseResponse bean, String type) {
        if (type.equals("FamilyShareCount")){
            Map body = (Map) bean.getBody();
            if (body!=null) {
                int familyCount = Double.valueOf((Double) body.get("familyNewsCount")).intValue();
                setBadgeNum(familyCount);
            }
        }
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
}