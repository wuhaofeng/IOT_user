package com.iot.user.ui.activity.alert;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.iot.user.R;
import com.iot.user.app.IOTApplication;
import com.iot.user.base.BaseMvpActivity;
import com.iot.user.cluster.clustering.ClusterManager;
import com.iot.user.cluster.model.MarkerInfo;
import com.iot.user.constant.Constants;
import com.iot.user.constant.DevConstants;
import com.iot.user.databinding.ActivityAlertNewBinding;
import com.iot.user.http.model.BaseResponse;
import com.iot.user.http.net.TokenInterceptor;
import com.iot.user.ui.activity.login.LoginActivity;
import com.iot.user.ui.activity.main.UnitMainActivity;
import com.iot.user.ui.contract.alert.AlertNewContract;
import com.iot.user.ui.model.alert.AlertInfo;
import com.iot.user.ui.presenter.alert.AlertNewPresenter;
import com.iot.user.ui.service.SpeakerAudioPlayerManager;
import com.iot.user.utils.DateUtil;
import com.iot.user.utils.DialogUtils;
import com.iot.user.utils.DoubleClickUtil;
import com.iot.user.utils.MyToast;
import com.iot.user.utils.PrefUtil;
import com.iot.user.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import static com.iot.user.ui.activity.main.UnitMainActivity.EXIT_TO_LOGIN;

public class AlertNewActivity extends BaseMvpActivity<AlertNewPresenter, ActivityAlertNewBinding> implements AMap.OnMapLoadedListener, AlertNewContract.View, EasyPermissions.PermissionCallbacks {
    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_alert_new;
    }

    RelativeLayout location_container_rl;
    MapView mMapView;
    Toolbar mToolbar;
    TextView tv_title;
    TextView tv_alarmdevnum;
    TextView tv_alarmcontent;
    TextView tv_location;
    TextView tv_alarmtime;
    TextView tv_username;
    TextView tv_tel;
    ImageView iv_dev_type;
    Button btn_silent;
    Button btn_known;
    public static Handler exitHandler = null;
    public static final int USER_SILENT = 10000;
    public static final int DEV_SILENT = 11000;
    AMap mAMap;
    private ClusterManager<MarkerInfo> mClusterManager;
    //    private AlarmRecord alarmRecord;
    public static final String ALARM = "alarminfo";
    private LinearLayout.LayoutParams mParams;
    private List<MarkerInfo> markerDataList = new ArrayList<>();

    //地图热点和热点列表同时存在的情况下和列表联动的MarkerItemMap
    private Map<Integer, MarkerInfo> mMarkerItemMap = new HashMap<>();

    private AlertInfo alertInfo;

    /**
     * 屏幕资源
     */
    private KeyguardManager.KeyguardLock mKeyguardLock = null;
    private KeyguardManager mKeyguardManager = null;
    private PowerManager.WakeLock mWakeLock;
    private PowerManager powerManager = null;

    /**
     * 唤醒屏幕资源
     */
    private void enterInAlertMode() {
        if (mWakeLock == null || mKeyguardManager == null) {
            initProwerManager();
        }
        mWakeLock.setReferenceCounted(false);
        mWakeLock.acquire();
        mKeyguardLock = this.mKeyguardManager.newKeyguardLock("user:AlertNewActivity");
        mKeyguardLock.disableKeyguard();
    }

    /**
     * 初始化资源
     */
    private void initProwerManager() {
        powerManager = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        mWakeLock = powerManager.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP
                | PowerManager.SCREEN_DIM_WAKE_LOCK, "user:AlertNewActivity");//最后的参数是LogCat里用的Tag
        mKeyguardManager = ((KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE));
    }

    /**
     * 释放资源
     */
    private void releaseWakeLock() {
        try {
            if (this.mKeyguardLock != null) {
                this.mKeyguardLock.reenableKeyguard();
                this.mKeyguardLock = null;
            }
            this.mWakeLock.release();
            this.mWakeLock = null;
            powerManager = null;
        } catch (Exception e) {
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMapView.onCreate(savedInstanceState);
        // 处理未登录情况下，收到报警，无法跳转登录界面
        TokenInterceptor.isRelogin = false;//重置状态
        exitHandler = new ExitHandler();
        initMyToolBar();
        init();
        initViews();
        initProwerManager();
        methodRequiresTwoPermission();
    }

    @Override
    public void initView() {
        mPresenter = new AlertNewPresenter();
        mPresenter.attachView(this);
        location_container_rl = dataBinding.locationContainerRl;
        mMapView = dataBinding.map;
        mToolbar =(Toolbar) dataBinding.toolbar;
        tv_title = dataBinding.tvTitle;
        tv_alarmdevnum = dataBinding.tvAlarmdevnum;
        tv_alarmcontent = dataBinding.tvAlarmcontent;
        tv_location = dataBinding.tvLocation;
        tv_alarmtime = dataBinding.tvAlarmtime;
        tv_username = dataBinding.tvUsername;
        tv_tel = dataBinding.tvTel;
        iv_dev_type = dataBinding.ivDevType;
        btn_silent = dataBinding.btnSilent;
        btn_known = dataBinding.btnKnown;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        alertInfo = (AlertInfo) getIntent().getSerializableExtra(ALARM);
    }

    @Override
    protected void onResume() {
        super.onResume();
        enterInAlertMode();
    }

    /***
     * stat 0 正常 1 故障 2 报警 3 复合
     *     Emcode 0 正常 1 燃气报警类 2 烟雾报警类
     */
    private void initMyToolBar() {
        String title = "";
        SpeakerAudioPlayerManager.getDefaultInstance().stopRing();
        if (alertInfo != null) {
            title = alertInfo.getTitle();
            if (alertInfo.getDevStat() == Constants.DEV_WARN) {
//                StatusBarUtil.setColor(this, getResources().getColor(R.color.type_02), 0);/***设置状态栏颜色**/
//                toolbar.setPopupTheme(R.style.ToolBarOrangeTheme);
                btn_silent.setBackgroundResource(R.drawable.bg_orange_btn);
                btn_known.setBackgroundResource(R.drawable.bg_orange_btn);
                SpeakerAudioPlayerManager.getDefaultInstance().playFaultRing();
                initToolBar(mToolbar, "设备故障", R.drawable.gank_ic_back_white);
            } else if (alertInfo.getDevStat() == Constants.DEV_ALARM) {
                if (DevConstants.DEV_ALARM_GAS == alertInfo.getAlarm_num()) {
                    SpeakerAudioPlayerManager.getDefaultInstance().playGasRing();
                } else if (DevConstants.DEV_ALARM_SMOKE == alertInfo.getAlarm_num()) {
                    SpeakerAudioPlayerManager.getDefaultInstance().playSmokeRing();
                } else if (DevConstants.DEV_ALARM_GAS_SMOKE == alertInfo.getAlarm_num()) {
                    SpeakerAudioPlayerManager.getDefaultInstance().playCombineRing();
                } else if (DevConstants.DEV_ALARM_SIMULATION == alertInfo.getAlarm_num()) {
                    SpeakerAudioPlayerManager.getDefaultInstance().playSimulationRing();
                } else {
                    SpeakerAudioPlayerManager.getDefaultInstance().playAlarmRing();
                }
//                StatusBarUtil.setColor(this, getResources().getColor(R.color.red), 0);
//                toolbar.setPopupTheme(R.style.ToolBarRedTheme);
                btn_silent.setBackgroundResource(R.drawable.bg_red_btn);
                btn_known.setBackgroundResource(R.drawable.bg_red_btn);
                initToolBar(mToolbar, "设备报警", R.drawable.gank_ic_back_white);
            } else {
                if (DevConstants.DEV_ALARM_GAS == alertInfo.getAlarm_num()) {
                    SpeakerAudioPlayerManager.getDefaultInstance().playGasRing();
                } else if (DevConstants.DEV_ALARM_SMOKE == alertInfo.getAlarm_num()) {
                    SpeakerAudioPlayerManager.getDefaultInstance().playSmokeRing();
                } else if (DevConstants.DEV_ALARM_GAS_SMOKE == alertInfo.getAlarm_num()) {
                    SpeakerAudioPlayerManager.getDefaultInstance().playCombineRing();
                } else if (DevConstants.DEV_ALARM_SIMULATION == alertInfo.getAlarm_num()) {
                    SpeakerAudioPlayerManager.getDefaultInstance().playSimulationRing();
                } else {
                    SpeakerAudioPlayerManager.getDefaultInstance().playAlarmRing();
                }
//                StatusBarUtil.setColor(this, getResources().getColor(R.color.red), 0);
//                toolbar.setPopupTheme(R.style.ToolBarRedTheme);
                btn_silent.setBackgroundResource(R.drawable.bg_red_btn);
                btn_known.setBackgroundResource(R.drawable.bg_red_btn);
                initToolBar(mToolbar, "设备报警", R.drawable.gank_ic_back_white);
            }
        } else {
            title = "设备故障";
//            StatusBarUtil.setColor(this, getResources().getColor(R.color.red), 0);
//            toolbar.setPopupTheme(R.style.ToolBarRedTheme);
            btn_silent.setBackgroundResource(R.drawable.bg_red_btn);
            btn_known.setBackgroundResource(R.drawable.bg_red_btn);
            SpeakerAudioPlayerManager.getDefaultInstance().playAlarmRing();
            initToolBar(mToolbar, "设备报警", R.drawable.gank_ic_back_white);
        }
        tv_title.setText(title);
//        toolbar.setTitle("故障报警");// 标题的文字需在setSupportActionBar之前，不然会无效
//        setSupportActionBar(toolbar);
//        toolbar.setTitleTextColor(getResources().getColor(R.color.gank_text1_color));
    }

    /**
     * 初始化地图
     */
    private void init() {
        if (mAMap == null) {
            // 初始化地图
            mAMap = mMapView.getMap();
            mAMap.setOnMapLoadedListener(this);
            mAMap.setMaxZoomLevel(18);
            mAMap.getUiSettings().setMyLocationButtonEnabled(false);//设置默认定位按钮是否显示，非必需设置。
            mAMap.getUiSettings().setZoomControlsEnabled(false);
        }
        // 定义点聚合管理类ClusterManager
        mClusterManager = new ClusterManager<MarkerInfo>(AlertNewActivity.this, mAMap);
        MarkerInfo info = null;


        if (alertInfo != null) {
//            int state = devLocation.getStat();
            info = new MarkerInfo(alertInfo.getLat(), alertInfo.getLon(), "", alertInfo.getDevNum(),
                    alertInfo.getDevType(), alertInfo.getDevName(), alertInfo.getUsername(), alertInfo.getMobile(),
                    alertInfo.getAddress(), alertInfo.getDevStat(), alertInfo.getDevStat(), alertInfo.getAt(), "", alertInfo.getDevName());
            markerDataList.add(info);
            markerItemLogic(markerDataList);
            mClusterManager.clearItems();
            mClusterManager.addItems(markerDataList);
            mClusterManager.cluster();
        }
    }

    /**
     * 组装高德需要的item
     */
    private void markerItemLogic(List<MarkerInfo> list) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        if (list.size() <= 0) {
            mMarkerItemMap.clear();
        }
        for (int i = 0; i < list.size(); i++) {
            MarkerInfo markerInfo = list.get(i);
            LatLng latLng = new LatLng(markerInfo.getMarkerLat(), markerInfo.getMarkerLon());
            mMarkerItemMap.put(i, markerInfo);
            builder.include(latLng);
        }

        mAMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 100));
    }

    private void initViews() {
        if (alertInfo != null) {
            String devNickname = alertInfo.getDevName();
            ////设置设备昵称
            tv_alarmcontent.setText(devNickname);
            tv_alarmdevnum.setText("设备ID:" + alertInfo.getDevNum());
            tv_location.setText("设备位置:" + alertInfo.getAddress());
            tv_alarmtime.setText("发生时间:" + DateUtil.getYearDayTime(alertInfo.getAt()));
            tv_username.setText(alertInfo.getNickname());
            tv_tel.setText(alertInfo.getMobile());
            int drawableId = R.drawable.pic_gray_bg;
            if (alertInfo.getDevType() == 101) {
                drawableId = R.drawable.d101;
            } else if (alertInfo.getDevType() == 102) {
                drawableId = R.drawable.d102;
            } else if (alertInfo.getDevType() == 181) {
                drawableId = R.drawable.d181;
            } else if (alertInfo.getDevType() == 182) {
                drawableId = R.drawable.d182;
            } else if (alertInfo.getDevType() == 201) {
                drawableId = R.drawable.d201;
            } else {
                drawableId = R.drawable.d192;
            }
            iv_dev_type.setImageResource(drawableId);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMapView != null) {
            mMapView.onDestroy();
        }
        if (mAMap != null) {
            mAMap.removecache();
            mAMap.clear();
            mAMap = null;

        }
        mClusterManager = null;
        releaseWakeLock();
    }

    class ExitHandler extends Handler {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case EXIT_TO_LOGIN:
                    SpeakerAudioPlayerManager.getDefaultInstance().stopRing();
                    PrefUtil.setIsInApp(false, AlertNewActivity.this);
                    Intent intent = new Intent(AlertNewActivity.this, LoginActivity.class);
                    intent.putExtra("isRelogin", true);
                    AlertNewActivity.this.startActivity(intent);
                    //退出程序
                    AlertNewActivity.this.finish();
                    break;
            }
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

    @Override
    public void onMapLoaded() {

    }

    @Override
    public void onSuccess(BaseResponse bean, String type) {
        if (type.equals("unit_know")) {
            SpeakerAudioPlayerManager.getDefaultInstance().stopRing();/***设备消音***/
            updateData();
        } else {
            SpeakerAudioPlayerManager.getDefaultInstance().stopRing();/***设备消音***/
            updateData();
        }
    }

    @Override
    protected void initClickBtn() {
        super.initClickBtn();
        btn_known.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_known();
            }
        });
        btn_silent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_silent();
            }
        });
    }

    void btn_known() {/**我知道了**/
        if (!DoubleClickUtil.isFastDoubleClick(R.id.btn_known)) {
            String loginToken = PrefUtil.getLoginToken(AlertNewActivity.this);
            if (loginToken == null || "".equals(loginToken)) {
                MyToast.showShortToast("请登录账号");
                startActivity(new Intent(AlertNewActivity.this, LoginActivity.class));
                AlertNewActivity.this.finish();
                return;
            }
            if (alertInfo != null) {
                if (PrefUtil.getAppSelectVersion(AlertNewActivity.this) == 2) {/***商用控制器***/
                    mPresenter.dealUnitDev(alertInfo.getDevNum(), "1", alertInfo.getMsgnum());
                    return;
                }
                mPresenter.dealDev(alertInfo.getDevNum(), String.valueOf(USER_SILENT));
            }
        }
    }

    void btn_silent() {/**消音**/
        if (!DoubleClickUtil.isFastDoubleClick(R.id.btn_silent)) {
            DialogUtils.showMyDialog(AlertNewActivity.this, "提示", "确定要设备消音吗？",
                    "确定", "取消", new DialogUtils.OnDialogClickListener() {
                        @Override
                        public void onConfirm() {
                            String loginToken = PrefUtil.getLoginToken(AlertNewActivity.this);
                            if (loginToken == null || "".equals(loginToken)) {
                                MyToast.showShortToast("请登录账号");
                                startActivity(new Intent(AlertNewActivity.this, LoginActivity.class));
                                AlertNewActivity.this.finish();
                                return;
                            }
                            if (alertInfo != null) {
                                if (PrefUtil.getAppSelectVersion(AlertNewActivity.this) == 2) {/***商用控制器***/
                                    mPresenter.dealUnitDev(alertInfo.getDevNum(), "2", alertInfo.getMsgnum());
                                    return;
                                }
                                mPresenter.dealDev(alertInfo.getDevNum(), String.valueOf(DEV_SILENT));
                            }
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
        }
    }

    /**
     * 延迟2秒后刷新
     ***/
    private void updateData() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dissmissProgressDialog();
                startActivity(new Intent(AlertNewActivity.this, UnitMainActivity.class));
                AlertNewActivity.this.finish();
            }
        }, 2000);
    }

    /**
     * 定位权限申请
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    private void methodRequiresTwoPermission() {
        String[] perms = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};/**Manifest.permission.ACCESS_FINE_LOCATION**/
        if (EasyPermissions.hasPermissions(this, perms)) {
//            ToastUtil.showMessage("已经授权");
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, "应用时需要定位权限",
                    100, perms);
        }
    }

    /**
     * 请求权限成功。
     * 可以弹窗显示结果，也可执行具体需要的逻辑操作
     */
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        ToastUtil.showMessage("用户授权成功");
    }

    /**
     * 请求权限失败
     */
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
//        ToastUtil.showMessage("用户授权失败");
        /**
         　　* 若是在权限弹窗中，用户勾选了'NEVER ASK AGAIN.'或者'不在提示'，且拒绝权限。
         　　* 这时候，需要跳转到设置界面去，让用户手动开启。
         　　*/
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            //当从软件设置界面，返回当前程序时候
            case AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE:
                mMapView.onResume();
                mAMap = null;
                init();
                initView();
//                methodRequiresTwoPermission();
                //执行Toast显示或者其他逻辑处理操作
                break;
        }
    }
}