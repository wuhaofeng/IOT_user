package com.iot.user.ui.fragment.main;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.iot.user.R;
import com.iot.user.app.IOTApplication;
import com.iot.user.base.BaseMvpFragment;
import com.iot.user.constant.Constants;
import com.iot.user.databinding.FragmentUnitHomeBinding;
import com.iot.user.http.model.BaseResponse;
import com.iot.user.ui.activity.dev.UnitDevListActivity;
import com.iot.user.ui.activity.dev.UnitProductAddActivity;
import com.iot.user.ui.adapter.login.LoginFragmentPagerAdapter;
import com.iot.user.ui.contract.main.HomeFragmentContract;
import com.iot.user.ui.model.main.UnitFamilyDevModel;
import com.iot.user.ui.model.main.UnitFamilyModel;
import com.iot.user.ui.presenter.login.LoginPresenter;
import com.iot.user.ui.presenter.main.HomeFragmentPresenter;
import com.iot.user.ui.view.main.MClearEditText;
import com.iot.user.ui.view.main.UnitHomeScrollView;
import com.iot.user.ui.view.main.UnitHomeViewPager;
import com.iot.user.ui.view.zxing.activity.CaptureActivity;
import com.iot.user.utils.AppValidationMgr;
import com.iot.user.utils.DoubleClickUtil;
import com.iot.user.utils.MyToast;
import com.iot.user.utils.PrefUtil;
import com.iot.user.utils.TabLayoutUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.iot.user.ui.activity.dev.UnitDevListActivity.DevStatus;
import static com.iot.user.ui.activity.dev.UnitDevListActivity.IsOnline;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UnitHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UnitHomeFragment extends BaseMvpFragment<HomeFragmentPresenter, FragmentUnitHomeBinding> implements HomeFragmentContract.View, OnRefreshListener, OnLoadMoreListener {
    public UnitHomeFragment() {
        // Required empty public constructor
    }
    public static UnitHomeFragment newInstance() {
        UnitHomeFragment fragment = new UnitHomeFragment();
        return fragment;
    }
    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_unit_home;
    }
    Button addBtn;
    Button scapeBtn;
    TextView tv_popwin;
    TextView tv_home_name;
    TabLayout tabLayout;
    MClearEditText et_dev_id;
    UnitHomeViewPager viewPager;
    SwipeToLoadLayout mSwipeToLoadLayout;
    UnitHomeScrollView swipe_scrollView;
    /***获取报警设备列表**/
    ImageView iv_unit_home_touch;
    RelativeLayout rl_unit_home_touch;

    private int selectTablayout=0; /**选中的tablayout**/
    private List fragments=new ArrayList<>();
    private List<String> fragmentTitles=new ArrayList<>();

    private  UnitHomeDevFragment oneFragment;
    private  UnitHomeRoomFragment twoFragment;

    private int pageIndex = 1;
    private static int QRCodeShare=1002;
    public static Handler selectExitHandler = null;
    @Override
    protected void initView(View view) {
        mPresenter = new HomeFragmentPresenter();
        mPresenter.attachView(this);
        addBtn=dataBinding.btnHomeAdd;
        scapeBtn=dataBinding.btnHomeScape;
        tv_popwin=dataBinding.tvPopwin;
        tv_home_name=dataBinding.tvHomeTitle;
        tabLayout=dataBinding.tablayout;
        et_dev_id=dataBinding.etDevId;
        viewPager=dataBinding.tabViewpager;
        mSwipeToLoadLayout=dataBinding.swipeToLoadLayout;
        swipe_scrollView=dataBinding.swipeTarget;
        iv_unit_home_touch=dataBinding.ivUnitHomeTouch;
        rl_unit_home_touch=dataBinding.rlUnitHomeTouch;
        initUpdateLayout();
        TabLayoutUtils.reflex(tabLayout);/**设置tablayout底部的横县的长度***/
        initViewPager();
        stopAlert();
        selectExitHandler=new SelectFamilyHandler();
        et_dev_id.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId== EditorInfo.IME_ACTION_DONE){/**搜索设备**/
                    getDevInfoWithDevNum(et_dev_id.getText().toString());
                }
                return false;
            }
        });
/**软键盘弹出不会导致页面变形**/
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    private void initUpdateLayout(){
        mSwipeToLoadLayout.setOnRefreshListener(this);
        mSwipeToLoadLayout.setOnLoadMoreListener(this);
        mSwipeToLoadLayout.setRefreshEnabled(false);
        mSwipeToLoadLayout.setLoadMoreEnabled(true);
    }

    class SelectFamilyHandler extends Handler {/***切换家庭跳转***/
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what){
            case 1:
                tabLayout.getTabAt(0).select();//设置第一个为选中
                break;
            default:
                break;
        }
    }
    }

    private void initViewPager(){
        String[] titles={"全部设备","房间"};
        for (int i=0;i<titles.length;i++){
            if (i==0) {
                if (oneFragment==null) {
                    oneFragment = UnitHomeDevFragment.newInstance(viewPager,mPresenter);
                    Bundle bundle = new Bundle();
                    bundle.putInt("status", i);
                    oneFragment.setArguments(bundle);//数据传递到fragment中
                    fragmentTitles.add(titles[i]);
                    fragments.add(oneFragment);
                }
            }else {
                if (twoFragment==null) {
                    twoFragment = UnitHomeRoomFragment.newInstance(viewPager,mPresenter);
                    Bundle bundle = new Bundle();
                    bundle.putInt("status", i);
                    twoFragment.setArguments(bundle);//数据传递到fragment中
                    fragmentTitles.add(titles[i]);
                    fragments.add(twoFragment);
                }
            }
        }
        //每项只进入一次
        viewPager.setAdapter(new LoginFragmentPagerAdapter(getChildFragmentManager(),getContext(),fragments,fragmentTitles));
        viewPager.setOffscreenPageLimit(2);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).select();//设置第一个为选中
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selectTablayout=tab.getPosition();
                viewPager.resetHeight(selectTablayout); /***不同的子fragment高度不同需要动态加载高度 1.子fragment传入viewPager 2.子fragment中viewPager.setObjectForPosition(view,0); 3。刷新高度viewPager.resetHeight(selectTablayout);***/
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    @Override
    public void onLoadMore() {
        pageIndex+=1;
        getFamilyDevData();
    }

    @Override
    public void onRefresh() {
        pageIndex=1;
        getFamilyDevData();
    }
    private void stopRefresh(){
        if(mSwipeToLoadLayout != null){
            mSwipeToLoadLayout.setRefreshing(false);
            mSwipeToLoadLayout.setLoadingMore(false);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {/***fragment重新进入页面调用**/
        super.onHiddenChanged(hidden);
        if (hidden==false){
            pageIndex=1;
            getFamilyListData();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateFragment();
        if (PrefUtil.getServerIP(IOTApplication.getIntstance()).equals("pub.shmsiot.top")){
            scapeBtn.setVisibility(View.GONE);
        }else{
            scapeBtn.setVisibility(View.VISIBLE);
        }
    }
    public void updateFragment(){
        pageIndex=1;
        mPresenter.familyModel=PrefUtil.getUnitFamilyModel();
        getFamilyListData();
        getAlertDevData();
    }

    private void getFamilyDevData(){/***获取家庭设备列表数据**/
        if (mPresenter.familyModel==null){
            MyToast.showShortToast("未获取到家庭数据");
            return;
        }
        mPresenter.getFamilyDevList(pageIndex);
    }

    private void getFamilyRoomData() {/**家庭房间列表*/
        if (mPresenter.familyModel==null){
            MyToast.showShortToast("未获取到家庭数据");
            return;
        }
        String familyType=mPresenter.familyModel.getType();
        if (Double.valueOf(familyType).intValue()==1) {
            familyType="1";
        }else{
            familyType="2";
        }
        mPresenter.getFamilyRoomList(familyType);
    }

    private void getFamilyListData(){/***获取家庭列表数据***/
        mPresenter.getFamilyListData();
    }
    private void getAlertDevData(){ /***获取报警设备列表数据**/
        mPresenter.getAlertDevData(1);
    }
    private void getDevInfoWithDevNum(String devNum) {/**搜索设备***/
        String devNo= AppValidationMgr.removeStringSpace(devNum,0);
        mPresenter.getDevInfoWithDevNum(devNo);
    }
    @Override
    public void onSuccess(BaseResponse bean, String responseType) {
        if (responseType.equals("family_list")) {
            tv_home_name.setText(mPresenter.familyModel.getFamily_name());
            getFamilyDevData();
            getFamilyRoomData();
        }else if (responseType.equals("family_dev_list")) {
            mPresenter.getFamilyDevStatusNum();
        }else if (responseType.equals("family_dev_status")) {
            oneFragment.updateHomeOneFragmentData();
        }else if (responseType.equals("alert_dev_list")) {
            if (mPresenter.mAlertDevDatas.size()==0){/**没有报警的设备**/
                stopAlert();
            }else{
                startAnimation();
            }
        }else if (responseType.equals("search_dev")) {
            goToUnitDevList((UnitFamilyDevModel) bean.getBody());
        }else if (responseType.equals("family_room_list")) {
            twoFragment.updateWithFamilyModel();
        }
    }

    @Override
    public void onError(String errMessage) {
        super.onError(errMessage);
        stopRefresh();
    }

    @Override
    public void onComplete() {
        super.onComplete();
        stopRefresh();
    }

    private void startAnimation(){
        rl_unit_home_touch.setVisibility(View.VISIBLE);
        AnimationSet animationSet = new AnimationSet(true);
        TranslateAnimation translateAnimation = new TranslateAnimation(
                // X轴的开始位置
                android.view.animation.Animation.RELATIVE_TO_SELF, 0f,
                // X轴的结束位置
                android.view.animation.Animation.RELATIVE_TO_SELF, 0.6f,
                // Y轴的开始位置
                android.view.animation.Animation.RELATIVE_TO_SELF, 0f,
                // Y轴的结束位置
                android.view.animation.Animation.RELATIVE_TO_SELF, 0f);
        translateAnimation.setDuration(1000);
        translateAnimation.setRepeatCount(24*60*60);  //  设置动画重复次数
        translateAnimation.setRepeatMode(android.view.animation.Animation.REVERSE);
        //translateAnimation.setRepeatMode(Animation.RESTART);    //重新从头执行
        translateAnimation.setRepeatMode(Animation.REVERSE);  //反方向执行
        animationSet.addAnimation(translateAnimation);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {

            }
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        iv_unit_home_touch.setAnimation(animationSet);
    }
    private void stopAlert(){
        if (rl_unit_home_touch!=null) {
            rl_unit_home_touch.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initClickBtn() {
        super.initClickBtn();
        dataBinding.linearChangeFamily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFamilySelectData();
            }
        });
        dataBinding.ivUnitAllAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickUnitAllAlertDev();
            }
        });
        dataBinding.btnHomeScape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickScapeBtn();
            }
        });
        dataBinding.btnHomeAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMorePopupWindow();
            }
        });
    }


    void changeFamilySelectData(){/**切换家庭**/
        if(!DoubleClickUtil.isFastDoubleClick()) {/**防止重复点击*/
//            Intent intent = new Intent(getActivity(), UniteFamilySelectActivity.class);
//            getActivity().startActivity(intent);
        }
    }

    void clickUnitAllAlertDev(){/**报警设备列表**/
        if(!DoubleClickUtil.isFastDoubleClick()) {/**防止重复点击*/
            Intent intent = new Intent(getContext(), UnitDevListActivity.class);
            intent.putExtra(IsOnline, "");
            intent.putExtra(DevStatus, "");
            intent.putExtra("isFromAlert", true);
            startActivity(intent);
        }
    }
    void clickScapeBtn(){/**点击后跳转扫描页，如果是未注册的设备，就去注册设备，如果是已经注册的设备，就去解绑设备**/
        Intent intent = new Intent(getContext(), CaptureActivity.class);
        intent.putExtra(CaptureActivity.INTENT_TYPE,CaptureActivity.SCAN_DEV);
        startActivityForResult(intent,QRCodeShare);
    }

    private void goToUnitDevList(UnitFamilyDevModel familyDevModel){/***跳转设备列表**/
        Intent intent = new Intent(getContext(), UnitDevListActivity.class);
        intent.putExtra(IsOnline, familyDevModel.getIsOnline());
        intent.putExtra(DevStatus, familyDevModel.getDevStatus());
        intent.putExtra("FamilyDevModel",familyDevModel);
        startActivity(intent);
    }

    private PopupWindow moreMenuPw;
    void showMorePopupWindow(){
        View menuView = LayoutInflater.from(getContext()).inflate(R.layout.popwindow_home_more, null);
        LinearLayout pop_add_user = menuView.findViewById(R.id.pop_add_user);
        LinearLayout pop_remove = menuView.findViewById(R.id.pop_remove);
        LinearLayout pop_transform = menuView.findViewById(R.id.pop_transform);
        int familyType=2;
        if (mPresenter.familyModel!=null&&mPresenter.familyModel.getType()!=null){
            familyType = Double.valueOf(mPresenter.familyModel.getType()).intValue();
        }
        if (familyType!=1) {
            pop_add_user.setVisibility(View.GONE);
            pop_remove.setVisibility(View.GONE);
        }

        moreMenuPw = new PopupWindow(menuView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        moreMenuPw.setTouchable(true);
        moreMenuPw.setOutsideTouchable(true);
        /**
         * 添加设备
         */
        pop_add_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!DoubleClickUtil.isFastDoubleClick()){
                    Intent intent = new Intent(getContext(), UnitProductAddActivity.class);
                    startActivity(intent);
                    moreMenuPw.dismiss();
                }
            }
        });
        /**
         * 扫一扫
         */
        pop_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!DoubleClickUtil.isFastDoubleClick()){
                    moreMenuPw.dismiss();
                    /**扫码*/
                    Intent intent = new Intent(getContext(), CaptureActivity.class);
                    intent.putExtra(CaptureActivity.INTENT_TYPE,CaptureActivity.SCAN_DEV_DETAIL);
                    startActivityForResult(intent,QRCodeShare);
                }
            }
        });

        /**
         * 共享
         */
        pop_transform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!DoubleClickUtil.isFastDoubleClick()){
//                    Intent intent = new Intent(getContext(), UnitShareActivity.class);
//                    startActivity(intent);
                    moreMenuPw.dismiss();
                }
            }

        });

        moreMenuPw.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00FFFFFF")));
        moreMenuPw.showAsDropDown(tv_popwin,-450,0);
    }



}