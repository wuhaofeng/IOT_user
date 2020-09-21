package com.iot.user.ui.fragment.notification;

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
import com.iot.user.R;
import com.iot.user.app.IOTApplication;
import com.iot.user.base.BaseMvpFragment;
import com.iot.user.databinding.FragmentUnitNotificationListBinding;
import com.iot.user.http.model.BaseResponse;
import com.iot.user.ui.activity.notification.UnitNotificationDetailActivity;
import com.iot.user.ui.adapter.base.OnItemClickListener;
import com.iot.user.ui.adapter.notification.RecycleNoticeItemAdapter;
import com.iot.user.ui.contract.notification.UnitNotificationContract;
import com.iot.user.ui.model.notification.NoticeContentModel;
import com.iot.user.ui.presenter.notification.UnitNotificationPresenter;
import com.iot.user.utils.DoubleClickUtil;
import com.iot.user.utils.PrefUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UnitNotificationListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UnitNotificationListFragment extends BaseMvpFragment<UnitNotificationPresenter, FragmentUnitNotificationListBinding> implements UnitNotificationContract.View, OnRefreshListener, OnLoadMoreListener {

    public UnitNotificationListFragment() {
        // Required empty public constructor
    }
    public static UnitNotificationListFragment newInstance() {
        return new UnitNotificationListFragment();
    }
    RecyclerView mRecyclerView;
    SwipeToLoadLayout mSwipeToLoadLayout;
    private int notificationStatus=0;/**0未读1已读2全部***/
    private int pageIndex = 1;
    private RecycleNoticeItemAdapter recycleNoticeItemAdapter;

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_unit_notification_list;
    }

    @Override
    protected void initView(View view) {
        mPresenter=new UnitNotificationPresenter();
        mPresenter.attachView(this);
        mRecyclerView=dataBinding.swipeTarget;
        mSwipeToLoadLayout=dataBinding.swipeToLoadLayout;
        Bundle bundle=getArguments();
        if (bundle!=null){
            notificationStatus=bundle.getInt("status");
        }
        initViews();
        mSwipeToLoadLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeToLoadLayout.setRefreshing(true);
            }
        }, 100);
    }
    private void initViews() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setVerticalScrollBarEnabled(true);

        mSwipeToLoadLayout.setOnRefreshListener(this);
        mSwipeToLoadLayout.setOnLoadMoreListener(this);
        mSwipeToLoadLayout.setRefreshEnabled(true);
        mSwipeToLoadLayout.setLoadMoreEnabled(true);
    }
    @Override
    public void onResume() {
        mSwipeToLoadLayout.setRefreshing(true);
        super.onResume();
    }

    @Override
    public void onLoadMore() {
        pageIndex ++;
        loadDatas();
    }

    @Override
    public void onRefresh() {
        pageIndex = 1;
        loadDatas();
    }

    private void loadDatas() {
        switch (notificationStatus){
            case 0:mPresenter.getUnitNotificationUnread(pageIndex);break;
            case 1:mPresenter.getUnitNotificationRead(pageIndex);break;
            case 2:mPresenter.getUnitNotificationAll(pageIndex);break;
            default:
                break;
        }
    }

    private void initAdapter() {
        if (recycleNoticeItemAdapter == null) {
            if (getActivity() != null){
                recycleNoticeItemAdapter = new RecycleNoticeItemAdapter(getActivity(), mPresenter.mDatas);
                mRecyclerView.setAdapter(recycleNoticeItemAdapter);
                recycleNoticeItemAdapter.setOnItemClickLitener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if(!DoubleClickUtil.isFastDoubleClick()){
                            NoticeContentModel noticeContent = mPresenter.mDatas.get(position);
                            Intent intent = new Intent(IOTApplication.getIntstance(), UnitNotificationDetailActivity.class);
                            intent.putExtra(UnitNotificationDetailActivity.NOTICE_INFO, noticeContent);
                            startActivity(intent);
                        }
                    }
                });
            }
        } else {
            recycleNoticeItemAdapter.updateDatas(mPresenter.mDatas);
            recycleNoticeItemAdapter.notifyDataSetChanged();
        }
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
        if (type.equals("notification")){
            initAdapter();
        }
    }


}