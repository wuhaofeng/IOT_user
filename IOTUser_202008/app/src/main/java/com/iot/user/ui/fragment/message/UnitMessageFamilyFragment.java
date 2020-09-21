package com.iot.user.ui.fragment.message;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.iot.user.R;
import com.iot.user.base.BaseMvpFragment;
import com.iot.user.databinding.FragmentUnitMessageFamilyBinding;
import com.iot.user.http.model.BaseResponse;
import com.iot.user.ui.adapter.message.UnitMessageFamilyAdapter;
import com.iot.user.ui.contract.message.UnitMessageContract;
import com.iot.user.ui.model.message.UnitMessageFamilyModel;
import com.iot.user.ui.presenter.message.UnitMessagePresenter;
import com.iot.user.utils.DialogUtils;
import com.iot.user.utils.DoubleClickUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UnitMessageFamilyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UnitMessageFamilyFragment extends BaseMvpFragment<UnitMessagePresenter, FragmentUnitMessageFamilyBinding> implements UnitMessageContract.View, OnRefreshListener, OnLoadMoreListener {
    public UnitMessageFamilyFragment() {
        // Required empty public constructor
    }
    RecyclerView mRecyclerView;
    SwipeToLoadLayout mSwipeToLoadLayout;
    private UnitMessageFamilyAdapter familyAdapter;
    private int pageIndex = 1;
    private int fragmentStatus=1;//1为家庭分享 ，2为设备共享
    public static UnitMessageFamilyFragment newInstance() {
        UnitMessageFamilyFragment fragment = new UnitMessageFamilyFragment();
        return fragment;
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_unit_message_family;
    }

    @Override
    protected void initView(View view) {
        Bundle bundle= getArguments();
        if (bundle!=null){
            fragmentStatus=bundle.getInt("status");
        }
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
        if (familyAdapter == null) {
            familyAdapter = new UnitMessageFamilyAdapter(getActivity(), mPresenter.familyDatas,fragmentStatus);
            mRecyclerView.setAdapter(familyAdapter);
            familyAdapter.setmOnItemClickLitener(new UnitMessageFamilyAdapter.OnClickItemListener() {
                @Override
                public void onItemClick(View view, int position) {
                    if(!DoubleClickUtil.isFastDoubleClick(R.id.swipe_target)){
                       mPresenter.postAgreeUnitMessageFamilyShare(position);
                    }
                }
                @Override
                public void onItemLongClick(View view, int position) {
                    deleteMessageWithPosition(position);
                }
            });
        } else {
            familyAdapter.updateDatas(mPresenter.familyDatas);
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
    public void onLoadMore() {
        pageIndex+=1;
        refreshDatas();
    }

    @Override
    public void onRefresh() {
        pageIndex=1;
        refreshDatas();
        if (refreshSuperFragment!=null){
            refreshSuperFragment.refresh();
        }
    }
    private void refreshDatas(){/***刷新数据**/
        if (fragmentStatus==1){
            mPresenter.refreshMessageFamilyList(pageIndex);
        }else{
            mPresenter.refreshMessageDevList(pageIndex);
        }
    }
    private void deleteMessageWithPosition(final int position){
        DialogUtils.showMyDialog(getContext(), "提示", "确定要删除此条消息吗？", "确定","取消",  new DialogUtils.OnDialogClickListener() {
            @Override
            public void onConfirm() {
                if (fragmentStatus==1) {/***家庭共享**/
                    mPresenter.postUnitDeleteFamilyMessage(position);
                }else {
                    mPresenter.postUnitDeleteDevMessage(position);
                }
            }
            @Override
            public void onCancel() {

            }
        });
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
/***回调**/
    public interface RefreshSuperFragment{
        void refresh();
    }
    private RefreshSuperFragment refreshSuperFragment;
    public void setRefreshSuperFragment(RefreshSuperFragment refreshSuperFragment) {
        this.refreshSuperFragment = refreshSuperFragment;
    }


    @Override
    public void onSuccess(BaseResponse bean, String type) {
      if (type.equals("family_share_agree")){
          onRefresh();
      }else if (type.equals("dev_share_delete")){
          onRefresh();
      }else if (type.equals("family_share_delete")){
          onRefresh();
      }else if (type.equals("family_share_list")){
          initAdapter();
      }else if (type.equals("family_dev_list")){
          initAdapter();
      }
    }
}