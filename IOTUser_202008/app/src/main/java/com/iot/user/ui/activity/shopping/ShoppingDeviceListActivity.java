package com.iot.user.ui.activity.shopping;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.iot.user.R;
import com.iot.user.base.BaseMvpActivity;
import com.iot.user.databinding.ActivityShoppingDeviceListBinding;
import com.iot.user.http.model.BaseResponse;
import com.iot.user.skin.SkinManager;
import com.iot.user.ui.adapter.shopping.ShoppingDeviceListAdapter;
import com.iot.user.ui.adapter.shopping.ShoppingSelectDeviceAdapter;
import com.iot.user.ui.adapter.shopping.ShoppingSelectYearAdapter;
import com.iot.user.ui.contract.shopping.ShoppingDeviceListContract;
import com.iot.user.ui.model.shopping.ShoppingDeviceListModel;
import com.iot.user.ui.model.shopping.ShoppingDeviceSelectListModel;
import com.iot.user.ui.model.shopping.ShoppingOrderDiscountListModel;
import com.iot.user.ui.presenter.shopping.ShoppingDeviceListPresenter;
import com.iot.user.utils.PXTransUtils;
import com.iot.user.utils.Util;
import com.luck.picture.lib.decoration.GridSpacingItemDecoration;

import java.util.ArrayList;

public class ShoppingDeviceListActivity extends BaseMvpActivity<ShoppingDeviceListPresenter, ActivityShoppingDeviceListBinding> implements ShoppingDeviceListContract.View, OnRefreshListener, OnLoadMoreListener {
    RecyclerView mRecyclerView;
    SwipeToLoadLayout mSwipeToLoadLayout;
    Toolbar mToolbar;
    private int currentNo = 1;
    private ShoppingDeviceListAdapter recycleDevBuyItemAdapter;
    public double totalMoney=0.00;/**总价**/
    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_shopping_device_list;
    }

    @Override
    public void initView() {
        mPresenter=new ShoppingDeviceListPresenter();
        mPresenter.attachView(this);
        mRecyclerView=dataBinding.swipeTarget;
        mSwipeToLoadLayout=dataBinding.swipeToLoadLayout;
        mToolbar=(Toolbar) dataBinding.toolbar;
        initMyToolBar();
        initViews();
        getShoppingDeviceList();
        getOrderSwitch();
        dataBinding.activityBottomLayout.setOnClickListener(new View.OnClickListener() {/**点击底部购物车**/
        @Override
        public void onClick(View view) {
            if (mPresenter.selectList.size()>0) {
                setupPopWindow();
            }
        }
        });
        dataBinding.btnBottomSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitSelectDiscountList();
            }
        });
    }
    private void initMyToolBar() {
        int currentSkinType = SkinManager.getCurrentSkinType(this);
        if (SkinManager.THEME_DAY == currentSkinType) {
            initToolBar(mToolbar, "设备续费", R.drawable.gank_ic_back_white);
        } else {
            initToolBar(mToolbar, "设备续费", R.drawable.gank_ic_back_night);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //ButterKnife.unbind(this);
        if(customPopWindow!=null && customPopWindow.isShowing()){
            customPopWindow.dismiss();
        }
    }
    private void initViews() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
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

        if (recycleDevBuyItemAdapter == null ) {
            if(mRecyclerView !=null && mPresenter.mDatas!=null && mPresenter.mDatas.size()>0){
                recycleDevBuyItemAdapter = new ShoppingDeviceListAdapter(this,mPresenter.mDatas,mPresenter.selectList,mPresenter.orderSwitch);
                Log.e("测试orderSwitch", "Activity: "+mPresenter.orderSwitch);
                mRecyclerView.setAdapter(recycleDevBuyItemAdapter);
                recycleDevBuyItemAdapter.setOnItemClickLitener(new ShoppingDeviceListAdapter.OnItemListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        selectYearWithDevType(position);
                    }
                });
            }
        } else {
            if(mPresenter.mDatas!=null){
                recycleDevBuyItemAdapter.updateDatas(mPresenter.mDatas);
            }
        }
        if(mSwipeToLoadLayout !=null){
            mSwipeToLoadLayout.setRefreshing(false);
            mSwipeToLoadLayout.setLoadingMore(false);
        }
    }
    /****刷新和加载更多****/
    @Override
    public void onLoadMore() {
        currentNo++;
        getShoppingDeviceList();
    }

    @Override
    public void onRefresh() {
        currentNo=1;
        getShoppingDeviceList();
    }
    private void stopRefresh(){
        if(mSwipeToLoadLayout!=null){
            mSwipeToLoadLayout.setRefreshing(false);
            mSwipeToLoadLayout.setLoadingMore(false);
        }
    }
    private  void getShoppingDeviceList(){/**获取设备续费列表**/
        mPresenter.postShoppingDeviceList(currentNo);
    }
    /**获取折扣开关***/
    private void getOrderSwitch(){
        mPresenter.getOrderSwitch();
    }

    /**
     * 选择年份的弹窗*
     * **/
    private void selectYearWithDevType(int selectIndex) {/***续费设备的数据**/
       mPresenter.postOrderDiscountList(selectIndex,"0");
    }

    private void updateBottomView(){/***更新bottomView的数据**/
        totalMoney=0.0;
        for (int i=0; i<mPresenter.selectList.size(); i++) {
            ShoppingDeviceSelectListModel dataModel=mPresenter.selectList.get(i);
            totalMoney+=Double.parseDouble(dataModel.getPrice());
        }
        dataBinding.tvBottomNum.setText(""+(int)mPresenter.selectList.size());
        dataBinding.tvBottomPrice.setText("¥"+ Util.getTwoNumFloatWith(totalMoney,true));
    }
    /**
     * *设置购物车弹窗*
     * */
    private PopupWindow customPopWindow=null;
    private View productListView=null;
    public void setupPopWindow(){
        /** 设置背景变暗 */
        final Window mWindow =getWindow();
        WindowManager.LayoutParams params = mWindow.getAttributes();
        params.alpha = 0.5f;
        mWindow.setAttributes(params);
        Log.e("设置背景变暗", "setupPopWindow: ");
        /**获取屏幕宽高**/
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        // 设置弹出框
        productListView = LayoutInflater.from(this).inflate(R.layout.popwindow_shopping_device_shopcar, null);
        customPopWindow = new PopupWindow(productListView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        /**必须设置背景 即使为null或者透明 否则点击外部不会消失 */
        customPopWindow.setOutsideTouchable(true);
        customPopWindow.setBackgroundDrawable(getResources().getDrawable(R.color.translate));
        /**聚焦点击事件不会透传给下面的View */
        customPopWindow.setFocusable(true);
        //相对于父控件的位置（例如正中央Gravity.CENTER，下方Gravity.BOTTOM等），可以设置偏移或无偏移
        customPopWindow.showAtLocation(dataBinding.activityContentView, Gravity.BOTTOM, 0, 0);
        customPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (mWindow != null) {
                    WindowManager.LayoutParams params = mWindow.getAttributes();
                    params.alpha = 1.0f;
                    mWindow.setAttributes(params);
                }
            }
        });
        initPopWindowContentView();
    }
    /***
     * *处理popwindow中自定义的view*
     * **/
    private ShoppingSelectDeviceAdapter popwindowAdapter;
    private void initPopWindowContentView(){
        Button clearBtn=productListView.findViewById(R.id.btn_popwindow_delete);
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.selectList.clear();
                updateBottomView();
                recycleDevBuyItemAdapter.updateSelectList(mPresenter.selectList);
                customPopWindow.dismiss();
            }
        });
        RelativeLayout contentView=productListView.findViewById(R.id.popwindow_content_layout);
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customPopWindow.dismiss();
            }
        });
        Button submitBtn=productListView.findViewById(R.id.btn_popwindow_bottom_submit);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitSelectDiscountList();
            }
        });

        RecyclerView recyclerView=productListView.findViewById(R.id.popwindow_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //添加分割线
        DividerItemDecoration divider = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(this,R.drawable.line_list_item_decoration));
        recyclerView.addItemDecoration(divider);
        popwindowAdapter=new ShoppingSelectDeviceAdapter(ShoppingDeviceListActivity.this,mPresenter.selectList,mPresenter.orderSwitch);
        recyclerView.setAdapter(popwindowAdapter);
        updatepopWindowBottomView();
        popwindowAdapter.setOnItemClickLitener(new ShoppingSelectDeviceAdapter.OnItemListener() {
            @Override
            public void onItemClick(View view, int position) {/**选择年份**/
                selectYearWithPopwindow(position);
            }

            @Override
            public void deleteItemClick(int position) {
                if (mPresenter.selectList!=null&&mPresenter.selectList.size()>0) {
                    mPresenter.selectList.remove(position);
                }
                recycleDevBuyItemAdapter.notifyDataSetChanged();
                updateBottomView();
                updatepopWindowBottomView();
                popwindowAdapter.notifyDataSetChanged();
                if (mPresenter.selectList.size()==0){
                    customPopWindow.dismiss();
                }
            }
        });
    }

    /***
     * *更新popwindow中自定义的bottomView*
     * **/
    private void updatepopWindowBottomView(){
        TextView money=productListView.findViewById(R.id.tv_popwindow_bottom_price);
        TextView num=productListView.findViewById(R.id.tv_popwindow_bottom_num);
        totalMoney=0.0;
        for (int i=0; i<mPresenter.selectList.size(); i++) {
            ShoppingDeviceSelectListModel dataModel=mPresenter.selectList.get(i);
            totalMoney+=Double.parseDouble(dataModel.getPrice());
        }
        num.setText(""+(int)mPresenter.selectList.size());
        money.setText("¥"+ Util.getTwoNumFloatWith(totalMoney,true));
    }


    /***
     * *选择年份 popwindow*
     * **/
    private void selectYearWithPopwindow(int position){
       mPresenter.postOrderDiscountList(position,"1");
    }

    /**
     * 去结算
     * **/
    private void submitSelectDiscountList(){
        mPresenter.postOrderAccountDetail();
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
     if (type.equals("device_list")) {
         initAdapter();
     }else if (type.equals("order_switch")){
         recycleDevBuyItemAdapter.orderSwitch=mPresenter.orderSwitch;
     }else if(type.equals("deal_select_data")){/***添加selectData后刷新数据**/
         updateBottomView();
         recycleDevBuyItemAdapter.updateSelectList(mPresenter.selectList);
     }else if(type.equals("submit_order")){/***添加selectData后刷新数据**/
         /**跳转到提交订单**/
         Intent intent = new Intent(ShoppingDeviceListActivity.this, ShoppingOrderSubmitActivity.class);
         intent.putParcelableArrayListExtra("ShoppingSelectList", (ArrayList<? extends Parcelable>) mPresenter.selectList);
         intent.putExtra("ShoppingOrderSwitch",mPresenter.orderSwitch);
         startActivity(intent);
     }

    }

    @Override
    public void onSuccessSelect(BaseResponse bean, Object selectInfo,String type) {/**选择年份接口的回调***/
            dealOrderDiscountList(bean,selectInfo,type);
    }

    private void dealOrderDiscountList(BaseResponse bean, Object model,String type) {
        if (type.equals("0")) {
            final ShoppingDeviceListModel info=(ShoppingDeviceListModel)model;
            final int[] selectPosition = {0};
            final ShoppingOrderDiscountListModel discountData = (ShoppingOrderDiscountListModel) bean.getData();/**打折优惠的数据*/
            final MaterialDialog dialog = new MaterialDialog.Builder(ShoppingDeviceListActivity.this)
                    .customView(R.layout.dialog_shopping_select_year, false)
                    .backgroundColorRes(R.color.black_text4_color).build();
            try {
                dialog.show();
            } catch (Exception e) {

            }
            View customeView = dialog.getCustomView();
            RecyclerView recyclerView = customeView.findViewById(R.id.dialog_rv);
            recyclerView.setLayoutManager(new GridLayoutManager(ShoppingDeviceListActivity.this, 3));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, PXTransUtils.dp2px(ShoppingDeviceListActivity.this, 10), true));
            ShoppingSelectYearAdapter adapter = new ShoppingSelectYearAdapter(ShoppingDeviceListActivity.this, discountData.getDiscount(), mPresenter.orderSwitch, Float.parseFloat(info.getPrice()));
            recyclerView.setAdapter(adapter);
            /**获取adapter选择的数据**/
            adapter.setOnItemClickLitener(new ShoppingSelectYearAdapter.OnItemListener() {
                @Override
                public void onItemClick(View view, int position) {
                    selectPosition[0] = position;
                }
            });
            Button button = (Button) customeView.findViewById(R.id.dialog_btn);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ShoppingOrderDiscountListModel.ShoppingOrderDiscountModel dataModel = discountData.getDiscount().get(selectPosition[0]);
                    String price = "" + Integer.parseInt(dataModel.getRenew_year()) * Float.parseFloat(info.getPrice());
                    if (mPresenter.orderSwitch == 2) {
                        price = "" + Integer.parseInt(dataModel.getRenew_year()) * Float.parseFloat(info.getPrice()) * Float.parseFloat(dataModel.getDiscount());
                    }
                    ShoppingDeviceSelectListModel selectModel = new ShoppingDeviceSelectListModel();
                    selectModel.setAddress(info.getAddress());
                    selectModel.setDev_type(info.getDevType());
                    selectModel.setProduct_id(info.getDevNum());
                    selectModel.setExecuteTime(info.getExecuteTime());
                    selectModel.setExpireTime(info.getExpireTime());
                    selectModel.setName(info.getName());
                    selectModel.setPrice(price);
                    selectModel.setRenew_year(dataModel.getRenew_year());
                    selectModel.setMark(dataModel.getMark());
                    selectModel.setDiscount(dataModel.getDiscount());
                    mPresenter.addDeviceSelectModel(selectModel);
                    dialog.dismiss();
                }
            });
        }else {
            final int[] selectPosition = {0};
            final ShoppingDeviceSelectListModel selectListModel=(ShoppingDeviceSelectListModel)model;
            Float totalPrice=Float.parseFloat(selectListModel.getPrice())/Integer.parseInt(selectListModel.getRenew_year());
            if (mPresenter.orderSwitch==2){
                totalPrice=totalPrice/Float.parseFloat(selectListModel.getDiscount());
            }
            final Float finalTotalPrice =Float.parseFloat(Util.getTwoNumFloatWith(totalPrice,true));
            final ShoppingOrderDiscountListModel discountData = (ShoppingOrderDiscountListModel) bean.getData();/**打折优惠的数据*/
            final MaterialDialog dialog = new MaterialDialog.Builder(ShoppingDeviceListActivity.this)
                    .customView(R.layout.dialog_shopping_select_year, false)
                    .backgroundColorRes(R.color.black_text4_color).build();
            try {
                dialog .show();
            }catch (Exception e){

            }
            View customeView = dialog.getCustomView();
            RecyclerView recyclerView = customeView.findViewById(R.id.dialog_rv);
            recyclerView.setLayoutManager(new GridLayoutManager(ShoppingDeviceListActivity.this, 3));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, PXTransUtils.dp2px(ShoppingDeviceListActivity.this, 10), true));
            final ShoppingSelectYearAdapter adapter = new ShoppingSelectYearAdapter(ShoppingDeviceListActivity.this, discountData.getDiscount(), mPresenter.orderSwitch, finalTotalPrice);
            recyclerView.setAdapter(adapter);
            /**获取adapter选择的数据**/

            adapter.setOnItemClickLitener(new ShoppingSelectYearAdapter.OnItemListener() {
                @Override
                public void onItemClick(View view, int position) {
                    selectPosition[0] = position;
                }
            });
            Button button = (Button) customeView.findViewById(R.id.dialog_btn);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ShoppingOrderDiscountListModel.ShoppingOrderDiscountModel dataModel = discountData.getDiscount().get(selectPosition[0]);
                    String price = "" + Integer.parseInt(dataModel.getRenew_year()) * finalTotalPrice;
                    if (mPresenter.orderSwitch == 2) {
                        price = "" + Integer.parseInt(dataModel.getRenew_year()) * finalTotalPrice * Float.parseFloat(dataModel.getDiscount());
                    }
                    selectListModel.setPrice(price);
                    selectListModel.setRenew_year(dataModel.getRenew_year());
                    selectListModel.setMark(dataModel.getMark());
                    selectListModel.setDiscount(dataModel.getDiscount());
                    mPresenter.addDeviceSelectModel(selectListModel);
                    popwindowAdapter.notifyDataSetChanged();
                    updatepopWindowBottomView();
                    dialog.dismiss();
                }
            });
        }
    }
    /***toolbar触发事件**/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}