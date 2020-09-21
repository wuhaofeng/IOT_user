package com.iot.user.ui.activity.shopping;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.iot.user.R;
import com.iot.user.base.BaseMvpActivity;
import com.iot.user.databinding.ActivityShoppingOrderDetailBinding;
import com.iot.user.http.model.BaseResponse;
import com.iot.user.skin.SkinManager;
import com.iot.user.ui.activity.main.UnitMainActivity;
import com.iot.user.wxapi.WXPayEntryActivity;
import com.iot.user.ui.adapter.shopping.AliPayJsonResp;
import com.iot.user.ui.adapter.shopping.PayInfo;
import com.iot.user.ui.adapter.shopping.PayJsonResp;
import com.iot.user.ui.adapter.shopping.ShoppingOrderDetailAdapter;
import com.iot.user.ui.adapter.shopping.ShoppingOrderPayAdapter;
import com.iot.user.ui.contract.shopping.ShoppingOrderDetailContract;
import com.iot.user.ui.model.shopping.PayResult;
import com.iot.user.ui.model.shopping.ShoppingOrderDetailModel;
import com.iot.user.ui.presenter.shopping.ShoppingOrderDetailPresenter;
import com.iot.user.utils.DateUtil;
import com.iot.user.utils.DialogUtils;
import com.iot.user.utils.PrefUtil;
import com.luck.picture.lib.decoration.GridSpacingItemDecoration;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class ShoppingOrderDetailActivity extends BaseMvpActivity<ShoppingOrderDetailPresenter, ActivityShoppingOrderDetailBinding> implements ShoppingOrderDetailContract.View {
    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_shopping_order_detail;
    }
    Toolbar mToolbar;
    RecyclerView mRecyclerView;
    TextView tv_order_num;
    TextView tv_order_create_time;
    TextView tv_order_status;

    private String trandeNo="";
    private boolean isReturnHome;
    private boolean isPaySuccess;/**如果是支付成功回来的**/
    private ShoppingOrderDetailAdapter shoppingOrderDetailAdapter=null;
    private View productListView;
    @Override
    public void initView() {
        mPresenter=new ShoppingOrderDetailPresenter();
        mPresenter.attachView(this);
        mToolbar=(Toolbar) dataBinding.toolbar;
        mRecyclerView=dataBinding.rvShoppingOrderDetail;
        tv_order_num=dataBinding.tvOrderNum;
        tv_order_create_time=dataBinding.tvOrderCreateTime;
        tv_order_status=dataBinding.tvOrderStatus;
        Intent intent=getIntent();
        if (intent!=null) {
            trandeNo = intent.getStringExtra("tradeno");
            isReturnHome=intent.getBooleanExtra("isReturnHome",false);
            isPaySuccess=intent.getBooleanExtra("isSuccess",false);
        }
        initMyToolBar();
        initViews();
        mPresenter.postNewOrderDetail(trandeNo);
    }
    /***toolbar触发事件**/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (isReturnHome){/**返回根activity**/
                        Intent intent=new Intent(ShoppingOrderDetailActivity.this, UnitMainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                }else {
                    finish();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /***捕获手机回退键**/
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (isReturnHome){/**返回根activity**/
                    Intent intent=new Intent(ShoppingOrderDetailActivity.this, UnitMainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
            }else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    private void initMyToolBar() {
        int currentSkinType = SkinManager.getCurrentSkinType(this);
        if (SkinManager.THEME_DAY == currentSkinType) {
            initToolBar(mToolbar, "订单详情", R.drawable.gank_ic_back_white);
        } else {
            initToolBar(mToolbar, "订单详情", R.drawable.gank_ic_back_night);
        }
    }
    private void initViews() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(1, 20, true));
        mRecyclerView.setVerticalScrollBarEnabled(true);
    }
    private void initAdapter() {
        if (shoppingOrderDetailAdapter == null ) {
            if(mRecyclerView !=null && mPresenter.mDatas!=null){
                shoppingOrderDetailAdapter = new ShoppingOrderDetailAdapter(this, mPresenter.mDatas);
                mRecyclerView.setAdapter(shoppingOrderDetailAdapter);
                shoppingOrderDetailAdapter.setOnItemClickLitener(new ShoppingOrderDetailAdapter.OnItemListener() {
                    @Override
                    public void onDeviceListClick() {
                            Intent intent=new Intent(ShoppingOrderDetailActivity.this, UnitMainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("goDeviceList",true);
                            startActivity(intent);
                    }
                    @Override
                    public void onConnectClick() {
                        setupPopWindow();
                    }
                    @Override
                    public void onCancelOrderClick(View view) {
                        cancelOrderNetWork();
                    }
                    @Override
                    public void onPayOrderClick() {
                        setupPayPopWindow();
                    }
                });
                if (mPresenter.mDatas.getStatus().equals("1")) {
                    createContentTimer();
                }
            }
        }else {
            shoppingOrderDetailAdapter.notifyDataSetChanged();
        }
    }

    private void cancelOrderNetWork() {
        DialogUtils.showMyDialog(this, "", "您确定要取消此订单吗", "取消订单", "先等等", new DialogUtils.OnDialogClickListener() {
            @Override
            public void onConfirm() {
               mPresenter.postCancelOrder(trandeNo);
            }
            @Override
            public void onCancel() {

            }
        });
    }

    /**
     * *设置联系商家弹窗*
     * */
    private PopupWindow customPopWindow=null;
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
        final View productListView = LayoutInflater.from(this).inflate(R.layout.popwindow_shopping_order_detail_connect, null);
        customPopWindow = new PopupWindow(productListView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        /**必须设置背景 即使为null或者透明 否则点击外部不会消失 */
        customPopWindow.setOutsideTouchable(true);
        customPopWindow.setBackgroundDrawable(getResources().getDrawable(R.color.translate));
        /**聚焦点击事件不会透传给下面的View */
        customPopWindow.setFocusable(true);
        //相对于父控件的位置（例如正中央Gravity.CENTER，下方Gravity.BOTTOM等），可以设置偏移或无偏移
        customPopWindow.showAtLocation(dataBinding.activityContentView, Gravity.CENTER, 0, 0);
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

    }

    /*****支付弹窗***/
    /**
     * *设置支付弹窗*
     * */
    private PopupWindow payPopWindow=null;
    public void setupPayPopWindow(){
        getCurTotalMoney();
        initPayInfo();
        productListView = LayoutInflater.from(this).inflate(R.layout.popwindow_shopping_order_submit, null);
        payPopWindow = new PopupWindow(productListView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        /**必须设置背景 即使为null或者透明 否则点击外部不会消失 */
        payPopWindow.setOutsideTouchable(true);
        payPopWindow.setBackgroundDrawable(getResources().getDrawable(R.color.translate));
        /**聚焦点击事件不会透传给下面的View */
        payPopWindow.setFocusable(true);
        //相对于父控件的位置（例如正中央Gravity.CENTER，下方Gravity.BOTTOM等），可以设置偏移或无偏移
        payPopWindow.showAtLocation(dataBinding.activityContentView, Gravity.BOTTOM, 0, 0);
        initPopWindowContentView(productListView);
    }

    /**
     * *跳转到订单详情*
     * */
    /**获取总价**/
    public double totalMoney=0.00;
    private void getCurTotalMoney(){
        totalMoney=0.0;
        for (int i=0; i<mPresenter.mDatas.getOrderDetail().size(); i++) {
            ShoppingOrderDetailModel dataModel=mPresenter.mDatas.getOrderDetail().get(i);
            totalMoney+=Double.parseDouble(dataModel.getTotal_fee());
        }
    }
    ShoppingOrderPayAdapter popwindowAdapter;
    private int selectPayStates=0;
    private void initPopWindowContentView(final View productListView) {
        ImageView clearBtn = productListView.findViewById(R.id.iv_pop_button_cancel);
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                stopTimer();
                payPopWindow.dismiss();
            }
        });
        RelativeLayout contentView = productListView.findViewById(R.id.popwindow_content_layout);
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                stopTimer();
                payPopWindow.dismiss();
            }
        });
        TextView tvPrice=productListView.findViewById(R.id.tv_popwindow_price);
        tvPrice.setText("¥"+totalMoney);
//        createTimer(productListView);
        RecyclerView recyclerView=productListView.findViewById(R.id.popwindow_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //添加分割线
        DividerItemDecoration divider = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(this,R.drawable.line_list_item_decoration));
        recyclerView.addItemDecoration(divider);
        popwindowAdapter=new ShoppingOrderPayAdapter(ShoppingOrderDetailActivity.this);
        recyclerView.setAdapter(popwindowAdapter);
        popwindowAdapter.setOnItemClickLitener(new ShoppingOrderPayAdapter.OnItemListener() {
            @Override
            public void onItemClick(View view, int position) {
                selectPayStates=position;
            }
        });
        Button payBtn = productListView.findViewById(R.id.btn_popwindow_bottom_pay);
        payBtn.setOnClickListener(new View.OnClickListener() {/***跳转支付页面**/
        @Override
        public void onClick(View view) {
            clickPayBtnWithStates(selectPayStates);
//            stopTimer();
            payPopWindow.dismiss();
        }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //ButterKnife.unbind(this);
//        stopTimer();
        stopContentTimer();
        if (payHandler !=null){
            payHandler.removeCallbacksAndMessages(null);
        }
    }

    /*******/
    /**
     * 倒计时
     * */
// 初始化定时器
    Timer contentTimer = new Timer();
    private void createContentTimer(){
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            currentTime = df.parse(formatData("yyyy-MM-dd HH:mm:ss", Long.parseLong(mPresenter.mDatas.getCreate_time()) / 1000)).getTime();
        }catch (Exception e){
        }
        contentTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                currentTime+=1000;
                createContentTimerView();
            }
        }, 0,1000);
    }

    // 停止定时器
    private void stopContentTimer(){
        if(contentTimer != null){
            contentTimer.cancel();
            // 一定设置为null，否则定时器不会被回收
            contentTimer = null;
        }
    }
    private long currentTime;
    private void createContentTimerView(){
        /**时间戳的时间*/
        String date2 = formatData("yyyy-MM-dd HH:mm:ss", Long.parseLong(mPresenter.mDatas.getExprie_time())/1000);
        /** 计算的时间差*/
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date d2 = df.parse(date2);
            long diff = d2.getTime() - currentTime;// 这样得到的差值是微秒级别
            if (diff<=0){
                mPresenter.mDatas.setStatus("3");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        shoppingOrderDetailAdapter.updateTimerView("支付已过期");
                        if (null != payPopWindow){
                            if (payPopWindow.isShowing()){
                                ((TextView) productListView.findViewById(R.id.tv_pop_timer)).setText("支付已过期");
                                payPopWindow.dismiss();
                            }
                        }
                        shoppingOrderDetailAdapter.notifyDataSetChanged();
                    }
                });
//                cancelOrderNetWork();

                stopContentTimer();
//                return;
            }
            long days = diff / (1000 * 60 * 60 * 24);
            long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
            final long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
            final long seconds=(diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)-minutes*(1000*60)) / 1000;
            /**主线程更新UI**/
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (null != payPopWindow){
                        if (payPopWindow.isShowing()){
                            ((TextView) productListView.findViewById(R.id.tv_pop_timer)).setText("支付剩余时间 "+String.format("%0"+2+"d", minutes)+":"+String.format("%0"+2+"d", seconds));
                        }
                    }
                    shoppingOrderDetailAdapter.updateTimerView("支付剩余时间 "+String.format("%0"+2+"d", minutes)+":"+String.format("%0"+2+"d", seconds));
                }
            });
        } catch (Exception e) {
        }
    }

    public static String formatData(String dataFormat, long timeStamp) {
        if (timeStamp == 0) {
            return "";
        }
        timeStamp = timeStamp * 1000;
        String result = "";
        SimpleDateFormat format = new SimpleDateFormat(dataFormat);
        result = format.format(new Date(timeStamp));
        return result;
    }
    @Override
    public void onSuccess(BaseResponse bean, String type) {
      if (type.equals("cancel_order")){
          shoppingOrderDetailAdapter.notifyDataSetChanged();
      }else if (type.equals("order_detail")){
          if (isPaySuccess==true){
              mPresenter.mDatas.setStatus("2");
          }
          initAdapter();
          tv_order_num.setText(trandeNo);
          tv_order_create_time.setText(DateUtil.getYearDayTime(mPresenter.mDatas.getCreate_time()));
      }
    }

    /**
     * 支付宝和微信支付
     * */
    private IWXAPI api;
    public static final int MSG_PAYED = 101;
    public static final int MSG_ALI_PAYED = 102;
    private void initPayInfo(){
        api = WXAPIFactory.createWXAPI(this, "wx7b23d0f800a068fa");
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }
    private void clickPayBtnWithStates(int states){
        if (states==0){/**支付宝*/
            goApiPay();
        }else {/**微信*/
            goWeiChatPay();
        }
    }
    private void goApiPay(){
        mPresenter.goApiPay(trandeNo);
    }

    private void goWeiChatPay(){
        mPresenter.goWeiChatPay(trandeNo);
    }

    @Override
    public void onPay(final Object bean, String type) {
        if (type.equals("api_pay")){
            final Runnable payRunnable = new Runnable() {

                @Override
                public void run() {
                    AliPayJsonResp entity=(AliPayJsonResp)bean;
                    PayTask alipay = new PayTask(ShoppingOrderDetailActivity.this);
                    Map<String, String> result = alipay.payV2(entity.getOrderString(), true);
                    Log.i("msp", result.toString());
                    Message msg = new Message();
                    msg.what = MSG_ALI_PAYED;
                    msg.obj = result;
                    payHandler.sendMessage(msg);
                }
            };
            // 必须异步调用
            Thread payThread = new Thread(payRunnable);
            payThread.start();
        }
        else if(type.equals("weixin_pay")){
            PayReq req = new PayReq();
            PayJsonResp entity=(PayJsonResp)bean;
            PayInfo payInfo = entity.getData();
            req.appId         = "wx7b23d0f800a068fa";
            req.partnerId		= payInfo.getPartnerid();//"1549038411";
            req.prepayId		= payInfo.getPrepayid();//"wx1520251908604887e0fbebef1232836300";
            req.nonceStr		= payInfo.getNoncestr();//"BIlkIb101sLDZ84u";
            req.timeStamp		= payInfo.getTimestamp();//"1565871919";
            req.packageValue	= "Sign=WXPay";//payInfo.getPackagestr();
            req.sign			= payInfo.getSign();//"ECC2EBF25E908C34F96BAF3B9DC5D47C";
            // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
            api.sendReq(req);
            PrefUtil.setSaveTradeNo(trandeNo, this);
            PrefUtil.setIsReturnHome(true,this);
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler payHandler=new Handler(){
        @SuppressWarnings("unused")
        @Override
        public void dispatchMessage(@NonNull Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case MSG_PAYED:
                    ShoppingOrderDetailActivity.this.finish();
                    break;
                case MSG_ALI_PAYED:
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();//错误码
                    String errmsg = payResult.getMemo();//错误提示
                    Log.i("alipay", "resultStatus:" + resultStatus + "-- errmsg:" + errmsg + "---resultInfo:" + resultInfo);
                    Intent intent = new Intent(ShoppingOrderDetailActivity.this, WXPayEntryActivity.class);
                    intent.putExtra(WXPayEntryActivity.INTENT_ERRCODE, resultStatus);
                    intent.putExtra(WXPayEntryActivity.INTENT_ERRMSG, errmsg);
                    intent.putExtra(WXPayEntryActivity.INTENT_ALI, true);
                    ShoppingOrderDetailActivity.this.startActivityForResult(intent, 102);
                    if (ShoppingOrderDetailActivity.this.getIntent() != null) {
                        PrefUtil.setSaveTradeNo(trandeNo, ShoppingOrderDetailActivity.this);
                        PrefUtil.setIsReturnHome(isReturnHome, ShoppingOrderDetailActivity.this);
                    }
                    ShoppingOrderDetailActivity.this.finish();
                    break;
            }
        }
    };

}