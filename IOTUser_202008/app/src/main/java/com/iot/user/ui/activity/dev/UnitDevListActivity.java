package com.iot.user.ui.activity.dev;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.iot.user.R;
import com.iot.user.app.IOTApplication;
import com.iot.user.base.BaseMvpActivity;
import com.iot.user.databinding.ActivityUnitDevListBinding;
import com.iot.user.http.model.BaseResponse;
import com.iot.user.skin.SkinManager;
import com.iot.user.ui.adapter.base.OnItemClickListener;
import com.iot.user.ui.adapter.dev.UnitDevListAdapter;
import com.iot.user.ui.adapter.main.UnitHomeDevAdapter;
import com.iot.user.ui.contract.dev.DevListContract;
import com.iot.user.ui.model.main.UnitFamilyDevModel;
import com.iot.user.ui.presenter.dev.DevListPresenter;
import com.iot.user.ui.view.dev.InputDialog;
import com.iot.user.utils.AppValidationMgr;
import com.iot.user.utils.MyToast;
import com.iot.user.utils.PXTransUtils;
import com.luck.picture.lib.decoration.GridSpacingItemDecoration;

public class UnitDevListActivity extends BaseMvpActivity<DevListPresenter,ActivityUnitDevListBinding> implements DevListContract.View, OnRefreshListener, OnLoadMoreListener {
    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_unit_dev_list;
    }
    Toolbar mToolbar;
    RecyclerView mRecyclerView;
    SwipeToLoadLayout mSwipeToLoadLayout;
    private String isOnline="";
    private String devStatus="";
    private Boolean isFromAlert=false;
    public static String IsOnline="isOnline";
    public static String DevStatus="devStatus";
    private int pageIndex = 1;
    private RecyclerView.Adapter itemAdapter;
    @Override
    public void initView() {
        mPresenter=new DevListPresenter();
        mPresenter.attachView(this);
        mToolbar=(Toolbar) dataBinding.toolbar;
        mRecyclerView=dataBinding.swipeTarget;
        mSwipeToLoadLayout=dataBinding.swipeToLoadLayout;
        isOnline=getIntent().getStringExtra(IsOnline);
        devStatus=getIntent().getStringExtra(DevStatus);
        isFromAlert=getIntent().getBooleanExtra("isFromAlert",false);
        initMyToolBar();
        UnitFamilyDevModel familyDevModel=(UnitFamilyDevModel) getIntent().getSerializableExtra("FamilyDevModel");
        initViews();
        if (familyDevModel==null){
            initLoadLayout();
            mSwipeToLoadLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mSwipeToLoadLayout.setRefreshing(true);
                }
            }, 100);
        }else{
            mPresenter.mDatas.add(familyDevModel);
            initItemAdapter();
        }
    }
    private void initViews() {
        if (isFromAlert==true) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            mRecyclerView.setLayoutManager(linearLayoutManager);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setVerticalScrollBarEnabled(true);
        }else {
            if (mRecyclerView!=null) {
                mRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(1, PXTransUtils.dp2px(this, 10), true));
                mRecyclerView.setVerticalScrollBarEnabled(true);
                initItemAdapter();
            }
        }
    }
    private void initLoadLayout(){
        mSwipeToLoadLayout.setOnRefreshListener(this);
        mSwipeToLoadLayout.setOnLoadMoreListener(this);
        mSwipeToLoadLayout.setRefreshEnabled(true);
        mSwipeToLoadLayout.setLoadMoreEnabled(true);
    }
    /***获取家庭设备列表数据**/
    private void getDevListData(){
        mPresenter.postUnitDevList(pageIndex,isOnline,devStatus);
    }
    /***获取报警设备列表数据**/
    private void getAlertDevData(){
        mPresenter.postUnitAlertDevList(pageIndex);
    }

    private void initItemAdapter(){
        if (itemAdapter == null ) {
            if(mPresenter.mDatas!=null && mPresenter.mDatas.size()>0){
                if (isFromAlert==true) {
                    itemAdapter = new UnitDevListAdapter(this, mPresenter.mDatas, isFromAlert);
                    UnitDevListAdapter devListAdapter=(UnitDevListAdapter)itemAdapter;
                    devListAdapter.setOnItemClickLitener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            UnitFamilyDevModel devModel=mPresenter.mDatas.get(position);
                            goToDevDetail(devModel.getDevNum());
                        }
                    });
                }else{
                    itemAdapter = new UnitHomeDevAdapter(this, mPresenter.mDatas);
                    UnitHomeDevAdapter devListAdapter=(UnitHomeDevAdapter)itemAdapter;
                    devListAdapter.setOnItemClickLitener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            UnitFamilyDevModel devModel=mPresenter.mDatas.get(position);
                            goToDevDetail(devModel.getDevNum());
                        }
                    });
                }
                if(mRecyclerView!=null){
                    mRecyclerView.setAdapter(itemAdapter);
                }
            }
        } else {
            if(mPresenter.mDatas!=null){
                if (isFromAlert==true) {
                    UnitDevListAdapter devListAdapter=(UnitDevListAdapter)itemAdapter;
                    devListAdapter.updateDatas(mPresenter.mDatas);
                }else{
                    UnitHomeDevAdapter devListAdapter=(UnitHomeDevAdapter)itemAdapter;
                    devListAdapter.updateDatas(mPresenter.mDatas);
                }
            }
        }
    }

    private void goToDevDetail(String devNum){
        Intent intent=new Intent(this, DevDetailGasActivity.class);
        intent.putExtra("DevNumDetail",devNum);
        startActivity(intent);
    }
    private void initMyToolBar() {
        int currentSkinType = SkinManager.getCurrentSkinType(this);
        if (SkinManager.THEME_DAY == currentSkinType) {
            initToolBar(mToolbar, "设备列表", R.drawable.gank_ic_back_white);
        } else {
            initToolBar(mToolbar, "设备列表", R.drawable.gank_ic_back_night);
        }
    }

    @Override
    protected void clickRightBtn(View button) {
        super.clickRightBtn(button);
        final InputDialog inputDialog = new InputDialog(UnitDevListActivity.this,"搜索设备","","请输入设备的");
        inputDialog.setCancelListerner(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputDialog.dismiss();
            }
        });
        inputDialog.setConfirmListerner(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = inputDialog.getContent();
                if(account == null || !AppValidationMgr.isPhone(account)){
                    MyToast.showShortToast("请输入正确的手机号");
                }else{
                    inputDialog.dismiss();
                }
            }
        });
        inputDialog.show();
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
    public void onLoadMore() {
        pageIndex+=1;
        if (isFromAlert==false) {
            getDevListData();
        }else {
            getAlertDevData();
        }
    }

    @Override
    public void onRefresh() {
        pageIndex=1;
        if (isFromAlert==false) {
            getDevListData();
        }else {
            getAlertDevData();
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

    private void stopRefresh(){
        if(mSwipeToLoadLayout != null){
            mSwipeToLoadLayout.setRefreshing(false);
            mSwipeToLoadLayout.setLoadingMore(false);
        }
    }

    @Override
    public void onSuccess(BaseResponse bean, String type) {
        if (type.equals("dev_list")) {
            initItemAdapter();
        }else if (type.equals("alert_dev_list")) {
            initItemAdapter();
        }
    }


}