package com.iot.user.ui.fragment.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iot.user.R;
import com.iot.user.base.BaseFragment;
import com.iot.user.databinding.FragmentUnitHomeDevBinding;
import com.iot.user.ui.activity.dev.DevDetailGasActivity;
import com.iot.user.ui.activity.dev.UnitDevListActivity;
import com.iot.user.ui.adapter.base.OnItemClickListener;
import com.iot.user.ui.adapter.main.UnitHomeDevAdapter;
import com.iot.user.ui.model.main.UnitFamilyDevModel;
import com.iot.user.ui.presenter.main.HomeFragmentPresenter;
import com.iot.user.ui.view.main.UnitHomeViewPager;
import com.iot.user.utils.DoubleClickUtil;
import com.iot.user.utils.MyToast;
import com.iot.user.utils.PXTransUtils;
import com.luck.picture.lib.decoration.GridSpacingItemDecoration;
import com.luck.picture.lib.tools.ScreenUtils;

import static com.iot.user.ui.activity.dev.UnitDevListActivity.DevStatus;
import static com.iot.user.ui.activity.dev.UnitDevListActivity.IsOnline;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UnitHomeDevFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UnitHomeDevFragment extends BaseFragment<FragmentUnitHomeDevBinding>{
    public UnitHomeDevFragment() {
    }
    RecyclerView mRecyclerView;
    TextView unit_home_alert_num;
    TextView unit_home_warn_num;
    TextView unit_home_online_num;
    TextView unit_home_offline_num;
    LinearLayout ll_fragment_dev_add;
    private UnitHomeDevAdapter recycleItemAdapter;
    private UnitHomeViewPager viewPager;
    private HomeFragmentPresenter mPresenter;
    public static UnitHomeDevFragment newInstance(UnitHomeViewPager homeViewPager,HomeFragmentPresenter presenter) {
        UnitHomeDevFragment fragment = new UnitHomeDevFragment();
        fragment.viewPager=homeViewPager;
        fragment.mPresenter=presenter;
        return fragment;
    }
    public void updateHomeOneFragmentData(){
        if (mPresenter==null){
            return;
        }
        initItemAdapter();
        if (mPresenter.statusModel!=null){
            unit_home_alert_num.setText("" + Double.valueOf(mPresenter.statusModel.getAlarm()).intValue());
            unit_home_warn_num.setText("" + Double.valueOf(mPresenter.statusModel.getFault()).intValue());
            unit_home_online_num.setText("" + Double.valueOf(mPresenter.statusModel.getOnline()).intValue());
            unit_home_offline_num.setText("" + Double.valueOf(mPresenter.statusModel.getOffline()).intValue());
        }
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_unit_home_dev;
    }

    @Override
    protected void initView(View view) {
     mRecyclerView=dataBinding.recyclerView;
     unit_home_alert_num=dataBinding.unitHomeAlertNum;
     unit_home_warn_num=dataBinding.unitHomeWarnNum;
     unit_home_online_num=dataBinding.unitHomeOnlineNum;
     unit_home_offline_num=dataBinding.unitHomeOfflineNum;
     ll_fragment_dev_add=dataBinding.llFragmentDevAdd;
     if (viewPager!=null) {
         viewPager.setObjectForPosition(view, 0);
     }
     if (mRecyclerView!=null) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(1, PXTransUtils.dp2px(getContext(), 10), true));
            mRecyclerView.setVerticalScrollBarEnabled(true);
            initItemAdapter();
     }
    }
    private void initItemAdapter(){
        if (ll_fragment_dev_add!=null) {
            if (mPresenter.mDatas.size() == 0) {
                ll_fragment_dev_add.setVisibility(View.VISIBLE);
            } else {
                ll_fragment_dev_add.setVisibility(View.GONE);
            }
        }
        if (recycleItemAdapter == null) {
            if (getContext()!=null) {
                recycleItemAdapter = new UnitHomeDevAdapter(getContext(), mPresenter.mDevDatas);
                if (mRecyclerView != null) {
                    mRecyclerView.setAdapter(recycleItemAdapter);
                }
                recycleItemAdapter.setOnItemClickLitener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                       jumpToDetailActivity(position);
                    }
                });
            }
        } else {
            recycleItemAdapter.updateDatas(mPresenter.mDevDatas);
        }
        initRecycleViewHeight();
    }


   private void jumpToDetailActivity(int position){
       if (!DoubleClickUtil.isFastDoubleClick()) {/**防止重复点击*/
           UnitFamilyDevModel devModel =mPresenter.mDevDatas.get(position);
           Intent intent = new Intent(getContext(), DevDetailGasActivity.class);
           intent.putExtra("DevNumDetail", devModel.getDevNum());
           startActivity(intent);
       }
    }

    @Override
    protected void initClickBtn() {
        super.initClickBtn();
        dataBinding.bgUnitHomeAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               clickHomeAlert();
            }
        });
        dataBinding.bgUnitHomeWarning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickHomeWarning();
            }
        });
        dataBinding.bgUnitHomeOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickHomeOnline();
            }
        });
        dataBinding.bgUnitHomeOffline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickHomeOffline();
            }
        });
        dataBinding.btnFragmentDevAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickAddDev();
            }
        });
    }
    void clickHomeAlert(){
        if (unit_home_alert_num.getText().toString().equals("0")){
            MyToast.showShortToast("没有报警设备");
            return;
        }
        if(!DoubleClickUtil.isFastDoubleClick()) {/**防止重复点击*/
            Intent intent = new Intent(getContext(), UnitDevListActivity.class);
            intent.putExtra(IsOnline, "");
            intent.putExtra(DevStatus, "2");
            startActivity(intent);
        }
    }
    void clickHomeWarning(){
        if (unit_home_warn_num.getText().toString().equals("0")){
            MyToast.showShortToast("没有故障设备");
            return;
        }
        if(!DoubleClickUtil.isFastDoubleClick()) {/**防止重复点击*/
            Intent intent = new Intent(getContext(), UnitDevListActivity.class);
            intent.putExtra(IsOnline, "");
            intent.putExtra(DevStatus, "1");
            startActivity(intent);
        }
    }
    void clickHomeOnline(){
        if (unit_home_online_num.getText().toString().equals("0")){
            MyToast.showShortToast("没有在线设备");
            return;
        }
        if(!DoubleClickUtil.isFastDoubleClick()) {/**防止重复点击*/
            Intent intent = new Intent(getContext(), UnitDevListActivity.class);
            intent.putExtra(IsOnline, "1");
            intent.putExtra(DevStatus, "");
            startActivity(intent);
        }
    }
    void clickHomeOffline(){
        if (unit_home_offline_num.getText().toString().equals("0")){
            MyToast.showShortToast("没有离线设备");
            return;
        }
        if(!DoubleClickUtil.isFastDoubleClick()) {/**防止重复点击*/
            Intent intent = new Intent(getContext(), UnitDevListActivity.class);
            intent.putExtra(IsOnline, "2");
            intent.putExtra(DevStatus, "");
            startActivity(intent);
        }
    }

    void clickAddDev(){
        if(!DoubleClickUtil.isFastDoubleClick()) {/**防止重复点击*/
//            Intent intent = new Intent(getContext(), UnitProductAddActivity.class);
//            intent.putExtra(IOTApplication.getIntstance().getFamilyModelTag(), familyModel);
//            startActivity(intent);
        }
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

//               int measuredHeight = (holder.itemView.getMeasuredHeight()+PXTransUtils.dp2px(getContext(),10))*(itemCount/2+itemCount%2);
            int measuredHeight = (holder.itemView.getMeasuredHeight()+PXTransUtils.dp2px(getContext(),10))*(itemCount)+PXTransUtils.dp2px(getContext(),10);
            int recycleHeight= ScreenUtils.getScreenHeight(getContext())-ScreenUtils.getStatusBarHeight(getContext())-PXTransUtils.dp2px(getContext(),50+30+155+30);
            if (measuredHeight<= recycleHeight){
                measuredHeight=recycleHeight;
            }
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    measuredHeight);
            mRecyclerView.setLayoutParams(layoutParams);
        }else {
            int recycleHeight=ScreenUtils.getScreenHeight(getContext())-ScreenUtils.getStatusBarHeight(getContext())-PXTransUtils.dp2px(getContext(),50+30+155+30);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    recycleHeight);
            mRecyclerView.setLayoutParams(layoutParams);
        }

    }
}