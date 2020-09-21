package com.iot.user.ui.activity.dev;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.iot.user.R;
import com.iot.user.app.IOTApplication;
import com.iot.user.base.BaseMvpActivity;
import com.iot.user.constant.DevConstants;
import com.iot.user.databinding.ActivityDevDetailGasBinding;
import com.iot.user.http.model.BaseResponse;
import com.iot.user.skin.SkinManager;
import com.iot.user.ui.activity.mine.ShareDeviceActivity;
import com.iot.user.ui.adapter.login.LoginFragmentPagerAdapter;
import com.iot.user.ui.contract.dev.DevDetailContract;
import com.iot.user.ui.contract.dev.DevNodeContract;
import com.iot.user.ui.fragment.dev.UnitDevChartFragment;
import com.iot.user.ui.fragment.dev.UnitDevDetailFragment;
import com.iot.user.ui.fragment.dev.UnitDevNodeFragment;
import com.iot.user.ui.fragment.dev.UnitDevNodeProductFragment;
import com.iot.user.ui.fragment.dev.UnitDevNodeSettingFragment;
import com.iot.user.ui.model.dev.push.UnitDevNodeProPushModel;
import com.iot.user.ui.model.dev.push.UnitDevNodePushModel;
import com.iot.user.ui.model.dev.push.UnitDevRoadPushModel;
import com.iot.user.ui.model.main.UnitFamilyModel;
import com.iot.user.ui.presenter.dev.DevDetailPresenter;
import com.iot.user.ui.presenter.dev.DevNodePresenter;
import com.iot.user.ui.service.SpeakerAudioPlayerManager;
import com.iot.user.utils.AppValidationMgr;
import com.iot.user.utils.DialogUtils;
import com.iot.user.utils.DoubleClickUtil;
import com.iot.user.utils.LogUtil;
import com.iot.user.utils.MyToast;
import com.iot.user.utils.PrefUtil;
import com.iot.user.utils.TabLayoutUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static com.iot.user.ui.activity.room.UnitRoomListActivity.FAMILYMODEL;

public class DevDetailGasActivity extends BaseMvpActivity<DevDetailPresenter, ActivityDevDetailGasBinding> implements DevDetailContract.View, DevNodeContract.View {

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_dev_detail_gas;
    }
    Toolbar mToolbar;
    TextView tv_popwin;
    TabLayout tablayout;
    ViewPager tab_viewpager;
    private String devNum="";
    private int selectTab=0;
    private List fragments=new ArrayList<>();
    private List<String> fragmentTitles=new ArrayList<>();
    public int selectTablayout=0;
    public String devStatus="182";
    public static Handler nodeHandler =null;
    private UnitFamilyModel familyModel;
    private UnitDevDetailFragment detailFragment;

    private DevNodePresenter nodePresenter;
    public UnitDevNodeFragment nodeFragment;/**切换到后台关闭心跳***/
    private UnitDevNodeSettingFragment settingFragment;
    private UnitDevNodeProductFragment productFragment;
    @Override
    public void initView() {
        mPresenter=new DevDetailPresenter();
        mPresenter.attachView(this);
        nodePresenter=new DevNodePresenter();
        nodePresenter.attachView(this);
        mToolbar=(Toolbar)dataBinding.toolbar;
        tv_popwin=dataBinding.tvPopwin;
        tablayout=dataBinding.tablayout;
        tab_viewpager=dataBinding.tabViewpager;
        TabLayoutUtils.reflex(tablayout);/**设置tablayout底部的横县的长度***/
        devNum=getIntent().getStringExtra("DevNumDetail");
        if (devNum==null){
            MyToast.showShortToast("未获取到设备信息");
            finish();
        }
        devNum= AppValidationMgr.removeStringSpace(devNum,0);/***去除空格***/
        if (devNum.length()>4){
            devStatus=devNum.substring(1,4);
        }
        selectTab=getIntent().getIntExtra("SelectTab",0);
        familyModel=(UnitFamilyModel) getIntent().getSerializableExtra(FAMILYMODEL);
        if (familyModel==null){
            familyModel=PrefUtil.getUnitFamilyModel();
        }
        if (nodeHandler==null) {
            nodeHandler = new ChangeDevNodeHandler(this);
        }
        initMyToolBar();
        if (devStatus.equals("192")) {
            mPresenter.postUnitDevPermission(devNum);
        }else{
            initViewPager(false);
        }
    }
    private void initViewPager(Boolean isUnit){
        if (isUnit){
            if (PrefUtil.getUnitOperationPermission(IOTApplication.getIntstance())<2){
                String[]  titles={"商用控制器","回路与节点"};
                for (int i=0;i<titles.length;i++){
                    if (i==0) {
                        detailFragment = UnitDevDetailFragment.newInstance(devNum,mPresenter);
                        fragmentTitles.add(titles[i]);
                        fragments.add(detailFragment);
                    }else if(i==1){
                        nodeFragment= UnitDevNodeFragment.newInstance(devNum,nodePresenter);
                        fragmentTitles.add(titles[i]);
                        fragments.add(nodeFragment);
                    }
                }
            }else {
                String[] titles={"商用控制器","回路与节点","节点设置","设备监控"};
                for (int i=0;i<titles.length;i++){
                    if (i==0) {
                        detailFragment =UnitDevDetailFragment.newInstance(devNum,mPresenter);
                        fragmentTitles.add(titles[i]);
                        fragments.add(detailFragment);
                    }else if(i==1){
                        nodeFragment= UnitDevNodeFragment.newInstance(devNum,nodePresenter);
                        fragmentTitles.add(titles[i]);
                        fragments.add(nodeFragment);
                    }else if(i==2){
                        settingFragment= UnitDevNodeSettingFragment.newInstance(devNum,nodePresenter);
                        fragmentTitles.add(titles[i]);
                        fragments.add(settingFragment);
                    }else if (i==3){
                        productFragment= UnitDevNodeProductFragment.newInstance(devNum,nodePresenter);
                        fragmentTitles.add(titles[i]);
                        fragments.add(productFragment);
                    }
                }
            }

        }else {
            String[] titles={"可燃气体报警器","燃气趋势图"};
            for (int i=0;i<2;i++){
                if (i==0) {
                    detailFragment =UnitDevDetailFragment.newInstance(devNum,mPresenter);
                    fragmentTitles.add(titles[i]);
                    fragments.add(detailFragment);
                }else {
                    UnitDevChartFragment warningFragment= UnitDevChartFragment.newInstance(devNum);
                    fragmentTitles.add(titles[i]);
                    fragments.add(warningFragment);
                }
            }
        }

        //每项只进入一次
        tab_viewpager .setOffscreenPageLimit(2);
        tab_viewpager.setAdapter(new LoginFragmentPagerAdapter(getSupportFragmentManager(),this,fragments,fragmentTitles));
        tablayout.setupWithViewPager(tab_viewpager);
        tablayout.getTabAt(selectTab).select();//设置第一个为选中
        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selectTablayout=tab.getPosition();
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private static class ChangeDevNodeHandler extends Handler {/***回路节点刷新跳转***/

    private WeakReference<DevDetailGasActivity> reference;

        public ChangeDevNodeHandler(DevDetailGasActivity activity){
            reference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            DevDetailGasActivity activity = reference.get();
            if (null != activity){
                super.handleMessage(msg);
                switch (msg.what){
                    case 1: {
                        UnitDevRoadPushModel pushModel = (UnitDevRoadPushModel) msg.obj;
                        if (activity.devStatus.equals("192") && PrefUtil.getUnitOperationPermission(IOTApplication.getIntstance()) == 2) {
                            activity.tablayout.getTabAt(2).select();//设置第3个
                            activity.settingFragment.notificationRoadData(pushModel);
                        }
                        break;
                    }
                    case 2: {
                        UnitDevNodePushModel pushModel2 = (UnitDevNodePushModel) msg.obj;
                        if (activity.devStatus.equals("192") && PrefUtil.getUnitOperationPermission(IOTApplication.getIntstance()) == 2) {
                            activity.tablayout.getTabAt(2).select();//设置第3个
                            activity.settingFragment.notificationNodeData(pushModel2);
                        }
                        break;
                    }
                    case 3: {
                        UnitDevNodeProPushModel pushModel3 = (UnitDevNodeProPushModel) msg.obj;
                        if (activity.devStatus.equals("192") && PrefUtil.getUnitOperationPermission(IOTApplication.getIntstance()) == 2) {
                            activity.tablayout.getTabAt(3).select();//设置第4个为选中
                            activity.productFragment.notificationNodeProData(pushModel3);
                        }
                        break;
                    }
                    default:
                        break;
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (nodePresenter!=null)
            nodePresenter.detachView();
        if (nodeHandler!=null)
            nodeHandler.removeCallbacksAndMessages(null);
    }
    private void initMyToolBar() {
        int currentSkinType = SkinManager.getCurrentSkinType(this);
        if (SkinManager.THEME_DAY == currentSkinType) {
            initToolBar(mToolbar, "设备详情", R.drawable.gank_ic_back_white);
        } else {
            initToolBar(mToolbar, "设备详情", R.drawable.gank_ic_back_night);
        }
        initToolBarRightBtn(mToolbar,"更多操作",0,1);
    }
    @Override
    protected void clickRightBtn(View button) {/***更多操作***/
        super.clickRightBtn(button);
        showMorePopupWindow();
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

    /**点击更多操作***/
    private PopupWindow moreMenuPw;
    private void showMorePopupWindow(){
        if (mPresenter.devDetailModel==null){
            MyToast.showShortToast("未获取到设备数据");
            return;
        }
        View menuView = LayoutInflater.from(this).inflate(R.layout.popwindow_unit_dev_more, null);
        LinearLayout pop_add_user = menuView.findViewById(R.id.pop_add_user);
        LinearLayout pop_edit = menuView.findViewById(R.id.pop_edit);
        LinearLayout pop_remove = menuView.findViewById(R.id.pop_remove);
        LinearLayout pop_share = menuView.findViewById(R.id.pop_unit_share);
        TextView tv_pop_remove=menuView.findViewById(R.id.tv_pop_remove);

        if (mPresenter.isOwner==false) {
            pop_edit.setVisibility(View.GONE);
            pop_remove.setVisibility(View.GONE);
            int familyType=2;
            if (familyModel!=null&&familyModel.getType()!=null){
                familyType = Double.valueOf(familyModel.getType()).intValue();
            }
            if (familyType==1){
                pop_remove.setVisibility(View.VISIBLE);
                tv_pop_remove.setText("取消关注");
            }
        }

        moreMenuPw = new PopupWindow(menuView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        moreMenuPw.setTouchable(true);
        moreMenuPw.setOutsideTouchable(true);
        /**
         * 查看联系人
         */
        pop_add_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!DoubleClickUtil.isFastDoubleClick(R.id.toolbar)){
                    Intent intent=new Intent(DevDetailGasActivity.this, UnitDevMemberActivity.class);
                    intent.putExtra("DevNum",devNum);
                    intent.putExtra("isFromDev",false);
                    intent.putExtra("isOwner",mPresenter.isOwner);
                    startActivity(intent);
                    moreMenuPw.dismiss();
                }
            }
        });
        /**
         * 编辑设备
         */
        pop_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!DoubleClickUtil.isFastDoubleClick(R.id.toolbar)){
                    mPresenter.isDevReg_V902(devNum);
                    moreMenuPw.dismiss();
                }
            }
        });
        /**
         * 解绑设备
         */
        pop_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!DoubleClickUtil.isFastDoubleClick(R.id.toolbar)){
                    if (mPresenter.isOwner==true){//解绑并注销
                        DialogUtils.showMyDialog(DevDetailGasActivity.this, "解绑设备", "不再接收该设备的所有消息",
                                "解绑并注销", "取消", new DialogUtils.OnDialogClickListener() {
                                    @Override
                                    public void onConfirm() {
                                        mPresenter.postUnitDevUnBinder(devNum);
                                    }

                                    @Override
                                    public void onCancel() {

                                    }
                                });
                    }else {//取消关注
                        DialogUtils.showMyDialog(DevDetailGasActivity.this, "取消关注此设备", "不再接收该设备的所有消息",
                                "取消关注", "取消", new DialogUtils.OnDialogClickListener() {
                                    @Override
                                    public void onConfirm() {
                                        mPresenter.postUnitDevUnAttention(devNum);
                                    }

                                    @Override
                                    public void onCancel() {

                                    }
                                });
                    }
                    moreMenuPw.dismiss();
                }
            }
        });
        /**
         * 分享设备
         */
        pop_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!DoubleClickUtil.isFastDoubleClick(R.id.toolbar)){
                    Intent intent = new Intent(DevDetailGasActivity.this, ShareDeviceActivity.class);
                    intent.putExtra(ShareDeviceActivity.DEV_NUM, devNum);
                    intent.putExtra(ShareDeviceActivity.STATUS, "1");
                    startActivity(intent);
                    moreMenuPw.dismiss();
                }
            }
        });
        moreMenuPw.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00FFFFFF")));
        moreMenuPw.showAsDropDown(tv_popwin,-450,0);
    }


    @Override
    public void onSuccess(BaseResponse bean, String type) {
       if (type.equals("dev_permission")){
           initViewPager(true);
       }else if (type.equals("dev_unbind")||type.equals("dev_unAttention")){
           finish();
       }else if (type.equals("dev_detail_info")){
           detailFragment.updateViews(mPresenter.devDetailModel);
       }else if (type.equals("unit_know")){
           SpeakerAudioPlayerManager.getDefaultInstance().stopRing();/***设备消音***/
           detailFragment.updateData((String) bean.getBody());
       }
    }

    @Override
    public void successToJump(int errorCode) {
        if(errorCode == DevConstants.DEV_UNREGIST) {//设备未注册
            goToDevRegister(devNum,errorCode);
        }else{
            goToDevEdit(devNum,errorCode);
        }
    }

    @Override
    public void onNodeSuccess(BaseResponse bean, String type,String fragment) {/***回路以及回路下节点的操作****/
        if (fragment.equals("load_node")){
            nodeFragment.onNodeSuccess(bean,type);
        }else if (fragment.equals("node_setting")){
            settingFragment.onNodeSuccess(bean,type);
        }else if (fragment.equals("node_pro")){
            productFragment.onNodeSuccess(bean,type);
        }
    }


    private void goToDevRegister(String devNum, int errorCode){/**跳转注册设备页面***/
        Intent intent = new Intent(DevDetailGasActivity.this, UnitDevRegisterActivity.class);
        intent.putExtra(UnitDevRegisterActivity.DEV_ID,devNum);
        intent.putExtra(UnitDevRegisterActivity.REGIST_STATUS,errorCode);
        startActivity(intent);
    }
    private void goToDevEdit(String devNum,int errorCode) {/**跳转编辑设备页面***/
        Intent intent = new Intent(DevDetailGasActivity.this, UnitDevEditActivity.class);
        intent.putExtra(UnitDevEditActivity.DEV_ID,devNum);
        if (errorCode==114) {
            intent.putExtra(UnitDevEditActivity.STATUS_ID, true);
        }else {
            intent.putExtra(UnitDevEditActivity.STATUS_ID, false);
        }
        startActivity(intent);
    }
}