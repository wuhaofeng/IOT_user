package com.iot.user.ui.fragment.main;

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
import android.widget.Button;
import android.widget.LinearLayout;

import com.iot.user.R;
import com.iot.user.app.IOTApplication;
import com.iot.user.base.BaseFragment;
import com.iot.user.databinding.FragmentUnitHomeRoomBinding;
import com.iot.user.ui.activity.room.UnitRoomListActivity;
import com.iot.user.ui.adapter.base.OnItemClickListener;
import com.iot.user.ui.adapter.main.UnitHomeRoomAdapter;
import com.iot.user.ui.model.main.UnitFamilyRoomModel;
import com.iot.user.ui.presenter.main.HomeFragmentPresenter;
import com.iot.user.ui.view.main.UnitHomeViewPager;
import com.iot.user.utils.MyToast;
import com.iot.user.utils.PXTransUtils;
import com.luck.picture.lib.tools.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UnitHomeRoomFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UnitHomeRoomFragment extends BaseFragment<FragmentUnitHomeRoomBinding> {
    public UnitHomeRoomFragment() {
        // Required empty public constructor
    }
    RecyclerView mRecyclerView;
    Button btn_room_manage;
    LinearLayout ll_unit_home_two;

    private UnitHomeRoomAdapter recycleItemAdapter;
    private UnitHomeViewPager viewPager;
    private HomeFragmentPresenter mPresenter;
    public static UnitHomeRoomFragment newInstance(UnitHomeViewPager homeViewPager, HomeFragmentPresenter presenter) {
        UnitHomeRoomFragment fragment = new UnitHomeRoomFragment();
        fragment.viewPager=homeViewPager;
        fragment.mPresenter=presenter;
        return fragment;
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_unit_home_room;
    }
    public void updateWithFamilyModel(){
        initItemAdapter();
    }

    @Override
    protected void initView(View view) {
        mRecyclerView=dataBinding.recyclerView;
        btn_room_manage=dataBinding.btnRoomManage;
        ll_unit_home_two=dataBinding.llUnitHomeTwo;
        if (viewPager!=null) {
            viewPager.setObjectForPosition(view, 1);
        }
        if (mRecyclerView!=null) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            mRecyclerView.setLayoutManager(linearLayoutManager);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setVerticalScrollBarEnabled(true);
        }
        if (mPresenter.familyModel==null){
            return;
        }
        String familyType = mPresenter.familyModel.getType();
        if (Double.valueOf(familyType).intValue() == 1) {
            familyType = "1";
            btn_room_manage.setVisibility(View.VISIBLE);
        } else {
            familyType = "2";
            btn_room_manage.setVisibility(View.GONE);
        }
    }
    private void initItemAdapter(){
        if (recycleItemAdapter == null) {
            if (getContext()!=null) {
                recycleItemAdapter = new UnitHomeRoomAdapter(getContext(), mPresenter.mRoomDatas);
                if (mRecyclerView != null) {
                    mRecyclerView.setAdapter(recycleItemAdapter);
                }
                recycleItemAdapter.setOnItemClickLitener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        UnitFamilyRoomModel roomModel = (UnitFamilyRoomModel) mPresenter.mRoomDatas.get(position);
                       jumpToRoomDetail(roomModel);
                    }
                });
            }
        } else {
            recycleItemAdapter.updateDatas(mPresenter.mRoomDatas);
        }
        initRecycleViewHeight();
    }
    @Override
    public void onHiddenChanged(boolean hidden) {/***fragment重新进入页面调用**/
        super.onHiddenChanged(hidden);
        if (hidden==false){
            getFamilyRoomData();
        }
    }
    private void getFamilyRoomData() {/**家庭房间列表*/
        if (mPresenter.familyModel==null){
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

    private void jumpToRoomDetail(UnitFamilyRoomModel roomModel){
//        Intent intent = new Intent(getContext(), UnitRoomDetailActivity.class);
//        intent.putExtra("RoomModel", roomModel);
//        intent.putExtra(IOTApplication.getIntstance().getFamilyModelTag(), familyModel);
//        startActivity(intent);
    }

    @Override
    protected void initClickBtn() {
        super.initClickBtn();
        dataBinding.btnRoomManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickRoomManage();
            }
        });
    }
    void clickRoomManage(){
        if (mPresenter.familyModel==null){
            MyToast.showShortToast("未获取到家庭数据");
            return;
        }
        Intent intent=new Intent(getActivity(), UnitRoomListActivity.class);
        startActivity(intent);
    }
    /**   动态设置RecycleView高度*/
    private void initRecycleViewHeight(){
        if (mRecyclerView==null){
            return;
        }
        RecyclerView.Adapter adapter = mRecyclerView.getAdapter();
        int itemCount = adapter.getItemCount();
        if (itemCount >0){
            RecyclerView.ViewHolder holder = adapter.createViewHolder(mRecyclerView, adapter
                    .getItemViewType(0));
            adapter.onBindViewHolder(holder, 0);
            holder.itemView.measure(
                    View.MeasureSpec.makeMeasureSpec(mRecyclerView.getWidth(), View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            holder.itemView.layout(0, 0, holder.itemView.getMeasuredWidth(),
                    holder.itemView.getMeasuredHeight());
            holder.itemView.setDrawingCacheEnabled(true);
            holder.itemView.buildDrawingCache();

            int measuredHeight = (holder.itemView.getMeasuredHeight()+ PXTransUtils.dp2px(getContext(),10))*itemCount;
            int recycleHeight= ScreenUtils.getScreenHeight(getContext())-ScreenUtils.getStatusBarHeight(getContext())-PXTransUtils.dp2px(getContext(),50+10+72);
            if (measuredHeight<= recycleHeight){
                measuredHeight=recycleHeight;
            }
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    measuredHeight+PXTransUtils.dp2px(getContext(),50));
            ll_unit_home_two.setLayoutParams(layoutParams);
        }else {
            int recycleHeight=ScreenUtils.getScreenHeight(getContext())-ScreenUtils.getStatusBarHeight(getContext())-PXTransUtils.dp2px(getContext(),50+10+72);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    recycleHeight+PXTransUtils.dp2px(getContext(),50));
            ll_unit_home_two.setLayoutParams(layoutParams);
        }

    }
}