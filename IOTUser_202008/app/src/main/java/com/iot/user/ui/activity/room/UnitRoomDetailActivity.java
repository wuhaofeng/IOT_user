package com.iot.user.ui.activity.room;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.iot.user.R;
import com.iot.user.base.BaseMvpActivity;
import com.iot.user.databinding.ActivityUnitRoomDetailBinding;
import com.iot.user.http.model.BaseResponse;
import com.iot.user.skin.SkinManager;
import com.iot.user.ui.activity.dev.DevDetailGasActivity;
import com.iot.user.ui.adapter.base.OnItemClickListener;
import com.iot.user.ui.adapter.main.UnitHomeDevAdapter;
import com.iot.user.ui.contract.room.UnitRoomContract;
import com.iot.user.ui.model.main.UnitFamilyDevModel;
import com.iot.user.ui.model.main.UnitFamilyModel;
import com.iot.user.ui.model.main.UnitFamilyRoomModel;
import com.iot.user.ui.presenter.room.UnitRoomPresenter;
import com.iot.user.utils.PXTransUtils;
import com.luck.picture.lib.decoration.GridSpacingItemDecoration;

import static com.iot.user.ui.activity.room.UnitRoomListActivity.FAMILYMODEL;

public class UnitRoomDetailActivity extends BaseMvpActivity<UnitRoomPresenter,ActivityUnitRoomDetailBinding> implements UnitRoomContract.View, OnRefreshListener, OnLoadMoreListener {
    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_unit_room_detail;
    }
    Toolbar mToolbar;
    RecyclerView mRecyclerView;
    TextView room_name;
    TextView room_dev_num;
    SwipeToLoadLayout mSwipeToLoadLayout;
    private UnitHomeDevAdapter recycleItemAdapter;
    private UnitFamilyModel familyModel;
    private UnitFamilyRoomModel roomModel;
    private int pageIndex=1;
    @Override
    public void initView() {
        mPresenter=new UnitRoomPresenter();
        mPresenter.attachView(this);
        mToolbar=(Toolbar)dataBinding.toolbar;
        mRecyclerView=dataBinding.swipeTarget;
        mSwipeToLoadLayout=dataBinding.swipeToLoadLayout;
        room_dev_num=dataBinding.roomDevNum;
        room_name=dataBinding.roomName;
        familyModel=(UnitFamilyModel)getIntent().getSerializableExtra(FAMILYMODEL);
        roomModel=(UnitFamilyRoomModel) getIntent().getSerializableExtra("RoomModel");
        initViews();
        initMyToolBar();
        initUpdateLayout();
    }
    @Override
    protected void onResume() {
        super.onResume();
        onRefresh();
    }

    private void initViews(){
        room_name.setText(roomModel.getPlace_name());
        room_dev_num.setText(Double.valueOf(roomModel.getDevnumCount()).intValue()+"个设备");
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,1));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(1, PXTransUtils.dp2px(this,10), true));
        mRecyclerView.setVerticalScrollBarEnabled(true);
        initItemAdapter();
    }
    private void initUpdateLayout(){/***刷新**/
        mSwipeToLoadLayout.setOnRefreshListener(this);
        mSwipeToLoadLayout.setOnLoadMoreListener(this);
        mSwipeToLoadLayout.setRefreshEnabled(true);
        mSwipeToLoadLayout.setLoadMoreEnabled(true);
    }

    private void initItemAdapter(){
        if (recycleItemAdapter == null ) {
            if(mPresenter.devDatas!=null && mPresenter.devDatas.size()>0){
                recycleItemAdapter = new UnitHomeDevAdapter(this, mPresenter.devDatas);
                if(mRecyclerView!=null){
                    mRecyclerView.setAdapter(recycleItemAdapter);
                }
                recycleItemAdapter.setOnItemClickLitener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        UnitFamilyDevModel devModel=mPresenter.devDatas.get(position);
                        goToDevDetail(devModel.getDevNum());
                    }
                });
            }
        } else {
            if(mPresenter.devDatas!=null){
                recycleItemAdapter.updateDatas(mPresenter.devDatas);
            }
        }
    }
    private void goToDevDetail(String devNum){
        Intent intent=new Intent(this, DevDetailGasActivity.class);
        intent.putExtra("DevNumDetail",devNum);
        intent.putExtra(FAMILYMODEL,familyModel);
        startActivity(intent);
    }
    /***获取家庭设备列表数据**/
    private void getFamilyDevData(){
        mPresenter.postUnitRoomDevList(pageIndex,familyModel.getFamily_id(),roomModel.getFamily_place_id());
    }
    private void initMyToolBar() {
        int currentSkinType = SkinManager.getCurrentSkinType(this);
        if (SkinManager.THEME_DAY == currentSkinType) {
            initToolBar(mToolbar, "房间详情", R.drawable.gank_ic_back_white);
        } else {
            initToolBar(mToolbar, "房间详情", R.drawable.gank_ic_back_night);
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
    public void onLoadMore() {
        pageIndex++;
        getFamilyDevData();
    }

    @Override
    public void onRefresh() {
        pageIndex=1;
        getFamilyDevData();
    }
    private void stopRefresh(){
        if(mSwipeToLoadLayout!=null){
            mSwipeToLoadLayout.setRefreshing(false);
            mSwipeToLoadLayout.setLoadingMore(false);
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

    @Override
    public void onSuccess(BaseResponse bean, String type) {
        if (type.equals("room_dev")){
            initItemAdapter();
        }
    }
}