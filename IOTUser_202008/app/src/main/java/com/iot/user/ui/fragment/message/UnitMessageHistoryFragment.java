package com.iot.user.ui.fragment.message;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.iot.user.R;
import com.iot.user.app.IOTApplication;
import com.iot.user.base.BaseMvpFragment;
import com.iot.user.databinding.FragmentUnitMessageHistoryBinding;
import com.iot.user.http.model.BaseResponse;
import com.iot.user.ui.activity.message.UnitMessageDetailActivity;
import com.iot.user.ui.adapter.base.OnItemClickListener;
import com.iot.user.ui.adapter.message.UnitMessageHistoryAdapter;
import com.iot.user.ui.contract.message.UnitMessageContract;
import com.iot.user.ui.model.message.UnitDevAlertDescModel;
import com.iot.user.ui.model.message.UnitMessageHistoryModel;
import com.iot.user.ui.model.message.UnitMessageModel;
import com.iot.user.ui.presenter.message.UnitMessagePresenter;
import com.iot.user.utils.DoubleClickUtil;
import com.iot.user.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.iot.user.ui.activity.message.UnitMessageDetailActivity.REATINE_DescInfo;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UnitMessageHistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UnitMessageHistoryFragment extends BaseMvpFragment<UnitMessagePresenter, FragmentUnitMessageHistoryBinding> implements UnitMessageContract.View, OnRefreshListener, OnLoadMoreListener {

    public UnitMessageHistoryFragment() {
        // Required empty public constructor
    }
    public static UnitMessageHistoryFragment newInstance() {
        UnitMessageHistoryFragment fragment = new UnitMessageHistoryFragment();
        return fragment;
    }
    RecyclerView mRecyclerView;
    SwipeToLoadLayout mSwipeToLoadLayout;
    private UnitMessageHistoryAdapter historyAdapter;
    private UnitMessageModel reatineInfo=null;
    private int pageIndex = 1;

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_unit_message_history;
    }

    @Override
    protected void initView(View view) {
        mPresenter=new UnitMessagePresenter();
        mPresenter.attachView(this);
     mRecyclerView=dataBinding.swipeTarget;
     mSwipeToLoadLayout=dataBinding.swipeToLoadLayout;
     initRecycleView();
        //加载数据
        mSwipeToLoadLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeToLoadLayout.setRefreshing(true);
            }
        }, 100);
    }
    private void initRecycleView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setVerticalScrollBarEnabled(true);

        mSwipeToLoadLayout.setOnRefreshListener(this);
        mSwipeToLoadLayout.setOnLoadMoreListener(this);
        mSwipeToLoadLayout.setRefreshEnabled(true);
        mSwipeToLoadLayout.setLoadMoreEnabled(true);
    }
    private void initAdapter() {
        if (historyAdapter == null) {
            if (getActivity() != null){
                historyAdapter = new UnitMessageHistoryAdapter(getActivity(), mPresenter.historyDatas);
                mRecyclerView.setAdapter(historyAdapter);
                historyAdapter.setOnItemClickLitener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if(!DoubleClickUtil.isFastDoubleClick(R.id.swipe_target)){
                            reatineInfo = mPresenter.historyDatas.get(position);
                            if(reatineInfo!=null && !StringUtils.isBlank(reatineInfo.getRecordId()) && !reatineInfo.getRecordId().equals("0") ){
                               mPresenter.postMessagezHistoryDetail(reatineInfo.getRecordId());
                            }
                        }
                    }
                });
            }
        } else {
            historyAdapter.updateDatas(mPresenter.historyDatas);
        }
        stopRefresh();
    }




    @Override
    public void onLoadMore() {
        pageIndex+=1;
        mPresenter.refreshMessagezHistoryList(pageIndex);
    }

    @Override
    public void onRefresh() {
        pageIndex=1;
        mPresenter.refreshMessagezHistoryList(pageIndex);
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
    public void onResume() {
        super.onResume();
        onRefresh();
    }
    @Override
    public void onHiddenChanged(boolean hidden) {/***fragment重新进入页面调用**/
        super.onHiddenChanged(hidden);
        onRefresh();
    }


    @Override
    public void onSuccess(BaseResponse bean, String type) {
         if (type.equals("message_history")){/**历史消息**/
             initAdapter();
         }else if(type.equals("message_history_detail")) {
             LinkedTreeMap map = (LinkedTreeMap) bean.getBody();
             Gson gson = new Gson();
             String jsonString = gson.toJson(map);
             UnitMessageHistoryModel detailModel = gson.fromJson(jsonString, UnitMessageHistoryModel.class);

             if (detailModel != null) {
                 UnitDevAlertDescModel devDescModel = new UnitDevAlertDescModel();
                 if (detailModel.getDevType().equals("192")) {
                     LinkedTreeMap devDesc = (LinkedTreeMap) map.get("dev192Desc");
                     String devString = gson.toJson(devDesc);
                     if (devDesc.get("alertAddress") instanceof String) {
                     } else {
                         devDescModel = gson.fromJson(devString, UnitDevAlertDescModel.class);
                     }
                 }
                 Intent intent = new Intent(IOTApplication.getIntstance(), UnitMessageDetailActivity.class);
                 intent.putExtra(UnitMessageDetailActivity.REATINE_HISTORY, detailModel);
                 intent.putExtra(UnitMessageDetailActivity.REATINE_INFO, reatineInfo);
                 intent.putExtra(REATINE_DescInfo, devDescModel);
                 startActivity(intent);
             }
         }
    }
}