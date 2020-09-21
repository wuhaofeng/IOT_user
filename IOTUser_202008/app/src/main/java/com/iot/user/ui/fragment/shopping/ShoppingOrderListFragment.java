package com.iot.user.ui.fragment.shopping;

import android.content.Intent;
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
import com.iot.user.databinding.FragmentShoppingOrderListBinding;
import com.iot.user.http.model.BaseResponse;
import com.iot.user.ui.activity.shopping.ShoppingOrderDetailActivity;
import com.iot.user.ui.adapter.shopping.ShoppingOrderListAdapter;
import com.iot.user.ui.contract.shopping.ShoppingOrderListContract;
import com.iot.user.ui.model.shopping.ShoppingOrderListModel;
import com.iot.user.ui.presenter.shopping.ShoppingOrderListPresenter;
import com.luck.picture.lib.decoration.GridSpacingItemDecoration;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShoppingOrderListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShoppingOrderListFragment extends BaseMvpFragment<ShoppingOrderListPresenter,FragmentShoppingOrderListBinding> implements OnRefreshListener, OnLoadMoreListener, ShoppingOrderListContract.View {

    public ShoppingOrderListFragment() {
        // Required empty public constructor
    }
    public static ShoppingOrderListFragment newInstance() {
        return new ShoppingOrderListFragment();
    }
    RecyclerView mRecyclerView;
    SwipeToLoadLayout mSwipeToLoadLayout;
    private int pageIndex = 1;
    private int status=-1;
    private ShoppingOrderListAdapter shoppingOrderListAdapter;

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_shopping_order_list;
    }

    @Override
    protected void initView(View view) {
        mPresenter=new ShoppingOrderListPresenter();
        mPresenter.attachView(this);
        mRecyclerView=dataBinding.swipeTarget;
        mSwipeToLoadLayout=dataBinding.swipeToLoadLayout;
        if (status==-1) {
            Bundle bundle = this.getArguments();//得到从Activity传来的数据
            if (bundle != null) {
                status = bundle.getInt("status");
            }
        }
        Log.e("orderlistfragment", "onCreateView: "+status);
        initViews();
        //加载数据
        mSwipeToLoadLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeToLoadLayout.setRefreshing(true);
            }
        }, 100);
    }

    private void initViews() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(1, 20, true));
        mRecyclerView.setVerticalScrollBarEnabled(true);
        mSwipeToLoadLayout.setOnRefreshListener(this);
        mSwipeToLoadLayout.setOnLoadMoreListener(this);
        mSwipeToLoadLayout.setRefreshEnabled(true);
        mSwipeToLoadLayout.setLoadMoreEnabled(true);
    }

    private void initAdapter() {
        if (shoppingOrderListAdapter == null ) {
            if(mRecyclerView !=null && mPresenter.mDatas!=null && mPresenter.mDatas.size()>0 &&getContext()!=null){
                shoppingOrderListAdapter = new ShoppingOrderListAdapter(getContext(),mPresenter.mDatas);
                mRecyclerView.setAdapter(shoppingOrderListAdapter);
                shoppingOrderListAdapter.setOnItemClickLitener(new ShoppingOrderListAdapter.OnItemListener() {
                    @Override
                    public void onItemClick(View view, int position) {
/****防多次点击       if(!DoubleClickUtil.isFastDoubleClick(R.id.toolbar)){*/
                        ShoppingOrderListModel dataModel=mPresenter.mDatas.get(position);
                        Intent intent = new Intent(context, ShoppingOrderDetailActivity.class);
                        intent.putExtra("tradeno", dataModel.getTrade_no());
                        getActivity().startActivity(intent);
                    }
                });
            }
        } else {
            shoppingOrderListAdapter.notifyDataSetChanged();
        }
        if(mSwipeToLoadLayout !=null){
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

    private void stopRefresh(){
        if(mSwipeToLoadLayout != null){
            mSwipeToLoadLayout.setRefreshing(false);
            mSwipeToLoadLayout.setLoadingMore(false);
        }
    }

    @Override
    public void onLoadMore() {
        pageIndex++;
        mPresenter.getShoppingOrderList(pageIndex,status);
    }

    @Override
    public void onRefresh() {
        pageIndex=1;
        mPresenter.getShoppingOrderList(pageIndex,status);
    }

    public void updateOrderList(int status){
        this.status=status;
        onRefresh();
    }

    @Override
    public void onSuccess(BaseResponse bean, String type) {
        if (type.equals("order_list")){
            initAdapter();
        }
    }
}