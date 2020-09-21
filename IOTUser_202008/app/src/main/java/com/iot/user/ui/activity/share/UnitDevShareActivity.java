package com.iot.user.ui.activity.share;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.iot.user.R;
import com.iot.user.base.BaseActivity;
import com.iot.user.base.BaseMvpActivity;
import com.iot.user.databinding.ActivityUnitDevShareBinding;
import com.iot.user.http.model.BaseResponse;
import com.iot.user.skin.SkinManager;
import com.iot.user.ui.activity.dev.UnitDevMemberActivity;
import com.iot.user.ui.adapter.base.OnItemClickListener;
import com.iot.user.ui.adapter.share.UnitShareDevAdapter;
import com.iot.user.ui.contract.share.DevShareContract;
import com.iot.user.ui.presenter.share.DevSharePresenter;
import com.iot.user.utils.MyToast;

public class UnitDevShareActivity extends BaseMvpActivity<DevSharePresenter, ActivityUnitDevShareBinding> implements OnRefreshListener, OnLoadMoreListener, DevShareContract.View {
    Toolbar mToolbar;
    RecyclerView mRecyclerView;
    SwipeToLoadLayout mSwipeToLoadLayout;
    private int pageIndex = 1;
    private UnitShareDevAdapter itemAdapter;
    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_unit_dev_share;
    }
    @Override
    public void initView() {
        mPresenter=new DevSharePresenter();
        mPresenter.attachView(this);
        mToolbar=(Toolbar)dataBinding.toolbar;
        mRecyclerView=dataBinding.swipeTarget;
        mSwipeToLoadLayout=dataBinding.swipeToLoadLayout;
        initMyToolBar();
        initViews();
        getUnitDevShareList();
    }
    private void stopRefresh(){
        if(mSwipeToLoadLayout != null){
            mSwipeToLoadLayout.setRefreshing(false);
            mSwipeToLoadLayout.setLoadingMore(false);
        }
    }

    private void initViews() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setVerticalScrollBarEnabled(true);

        mSwipeToLoadLayout.setOnRefreshListener(this);
        mSwipeToLoadLayout.setOnLoadMoreListener(this);
        mSwipeToLoadLayout.setRefreshEnabled(true);
        mSwipeToLoadLayout.setLoadMoreEnabled(true);
    }
    private void getUnitDevShareList(){
        mPresenter.postUnitDevShareList(pageIndex);
    }
    private void initAdapter() {
        if (itemAdapter == null) {
            itemAdapter = new UnitShareDevAdapter(this, mPresenter.mDatas);
            mRecyclerView.setAdapter(itemAdapter);
            itemAdapter.setOnItemClickLitener(new OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Log.d("UnitDevShare", "onItemClick: "+position);
                }
            });
        } else {
            itemAdapter.updateDatas(mPresenter.mDatas);
        }
    }

    private void initMyToolBar() {
        int currentSkinType = SkinManager.getCurrentSkinType(this);
        if (SkinManager.THEME_DAY == currentSkinType) {
            initToolBar(mToolbar, "设备共享", R.drawable.gank_ic_back_white);
        } else {
            initToolBar(mToolbar, "设备共享", R.drawable.gank_ic_back_night);
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
    public void onRefresh() {
        pageIndex=1;
        getUnitDevShareList();
    }

    @Override
    public void onLoadMore() {
        pageIndex+=1;
        getUnitDevShareList();
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
        if (type.equals("share_dev_list")){
            initAdapter();
        }
    }

    @Override
    protected void initClickBtn() {
        super.initClickBtn();
        dataBinding.btnDevShareSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               clickDevShareBtn();
            }
        });

    }
    void clickDevShareBtn(){
        if (itemAdapter==null||itemAdapter.selectModel==null){
            MyToast.showShortToast("请选择您要共享的设备");
            return;
        }
        Intent intent=new Intent(UnitDevShareActivity.this, UnitDevMemberActivity.class);
        intent.putExtra("DevNum",String.valueOf(itemAdapter.selectModel.getDevNum()));
        intent.putExtra("isFromDev",true);
        intent.putExtra("isOwner",true);
        startActivity(intent);
    }
}