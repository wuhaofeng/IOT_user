package com.iot.user.ui.activity.shopping;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.iot.user.R;
import com.iot.user.base.BaseMvpActivity;
import com.iot.user.databinding.ActivityShoppingOrderSubmitBinding;
import com.iot.user.http.model.BaseResponse;
import com.iot.user.skin.SkinManager;
import com.iot.user.wxapi.WXPayEntryActivity;
import com.iot.user.ui.adapter.shopping.AliPayJsonResp;
import com.iot.user.ui.adapter.shopping.PayInfo;
import com.iot.user.ui.adapter.shopping.PayJsonResp;
import com.iot.user.ui.adapter.shopping.ShoppingOrderPayAdapter;
import com.iot.user.ui.adapter.shopping.ShoppingOrderSubmitAdapter;
import com.iot.user.ui.contract.shopping.ShoppingOrderSubmitContract;
import com.iot.user.ui.model.shopping.PayResult;
import com.iot.user.ui.model.shopping.ShoppingDeviceSelectListModel;
import com.iot.user.ui.presenter.shopping.ShoppingOrderSubmitPresenter;
import com.iot.user.utils.PrefUtil;
import com.iot.user.utils.Util;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class ShoppingOrderSubmitActivity extends BaseMvpActivity<ShoppingOrderSubmitPresenter, ActivityShoppingOrderSubmitBinding> implements ShoppingOrderSubmitContract.View, IWXAPIEventHandler {

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_shopping_order_submit;
    }

    Toolbar mToolbar;
    RecyclerView mRecyclerView;
    private ShoppingOrderSubmitAdapter mAdapter;
    public double totalMoney=0.00;
    @Override
    public void initView() {
      mPresenter=new ShoppingOrderSubmitPresenter();
      mPresenter.attachView(this);
      mPresenter.selectList=getIntent().getParcelableArrayListExtra("ShoppingSelectList");
      mPresenter.orderSwitch=getIntent().getFloatExtra("ShoppingOrderSwitch",0.0f);
      mToolbar=(Toolbar) dataBinding.toolbar;
      mRecyclerView=dataBinding.swipeTarget;
      initMyToolBar();
        initViews();
        dataBinding.btnBottomSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {/**提交订单，首先会去创建订单**/
                createOrder();
            }
        });
        initPayInfo();
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
    private void initMyToolBar() {
        int currentSkinType = SkinManager.getCurrentSkinType(this);
        if (SkinManager.THEME_DAY == currentSkinType) {
            initToolBar(mToolbar, "提交订单", R.drawable.gank_ic_back_white);
        } else {
            initToolBar(mToolbar, "提交订单", R.drawable.gank_ic_back_night);
        }
    }
    private void initViews() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //添加分割线
        DividerItemDecoration divider = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(this,R.drawable.line_list_item_decoration));
        mRecyclerView.addItemDecoration(divider);
        mAdapter=new ShoppingOrderSubmitAdapter(ShoppingOrderSubmitActivity.this,mPresenter.selectList,mPresenter.orderSwitch);
        mRecyclerView.setAdapter(mAdapter);
        getCurTotalMoney();
        dataBinding.tvBottomPrice.setText("¥"+ Util.getTwoNumFloatWith(totalMoney,true));
    }

    /**获取总价**/
    private void getCurTotalMoney(){
        totalMoney=0.0;
        for (int i=0; i<mPresenter.selectList.size(); i++) {
            ShoppingDeviceSelectListModel dataModel=mPresenter.selectList.get(i);
            totalMoney+=Double.parseDouble(dataModel.getPrice());
        }
    }

    /***创建订单**/
    private void createOrder(){
        mPresenter.postCreateOrder();
    }
    /**
     * *设置支付弹窗*
     * */
    private PopupWindow customPopWindow=null;
    public void setupPopWindow(){
        final View productListView = LayoutInflater.from(this).inflate(R.layout.popwindow_shopping_order_submit, null);
        customPopWindow = new PopupWindow(productListView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        /**必须设置背景 即使为null或者透明 否则点击外部不会消失 */
//        customPopWindow.setOutsideTouchable(true);
//        customPopWindow.setBackgroundDrawable(null);
        /**true聚焦点击事件不会透传给下面的View */
        customPopWindow.setFocusable(true);
        //相对于父控件的位置（例如正中央Gravity.CENTER，下方Gravity.BOTTOM等），可以设置偏移或无偏移
        customPopWindow.showAtLocation(dataBinding.activityContentView, Gravity.BOTTOM, 0, 0);
        initPopWindowContentView(productListView);
    }

    /**
     * *跳转到订单详情*
     * */
    ShoppingOrderPayAdapter popwindowAdapter;
    private int selectPayStates=0;
    private void initPopWindowContentView(final View productListView) {
        ImageView clearBtn = productListView.findViewById(R.id.iv_pop_button_cancel);
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShoppingOrderSubmitActivity.this, ShoppingOrderDetailActivity.class);
                intent.putExtra("tradeno", mPresenter.tradeNo);
                intent.putExtra("isReturnHome",true);
                startActivity(intent);
                customPopWindow.dismiss();
            }
        });
        TextView tvPrice=productListView.findViewById(R.id.tv_popwindow_price);
        tvPrice.setText(""+mPresenter.totalFee);
        createTimer(productListView);
        RecyclerView recyclerView=productListView.findViewById(R.id.popwindow_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //添加分割线
        DividerItemDecoration divider = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(this,R.drawable.line_list_item_decoration));
        recyclerView.addItemDecoration(divider);
        popwindowAdapter=new ShoppingOrderPayAdapter(ShoppingOrderSubmitActivity.this);
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
            customPopWindow.dismiss();
        }
        });
    }

    /**
     * 倒计时
     * */
// 初始化定时器
    Timer timer = new Timer();
    private void createTimer(final View productListView){
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            currentTime = df.parse(formatData("yyyy-MM-dd HH:mm:ss", Long.parseLong(mPresenter.create_time) / 1000)).getTime();
        }catch (Exception e){
        }
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                currentTime+=1000;
                createTimerView(productListView);
            }
        }, 0,1000);
    }

    // 停止定时器
    private void stopTimer(){
        if(timer != null){
            timer.cancel();
            // 一定设置为null，否则定时器不会被回收
            timer = null;
        }
    }
    private long currentTime;
    private void createTimerView(View productListView){
        final TextView timerView=productListView.findViewById(R.id.tv_pop_timer);
        /**时间戳的时间*/
        String date2 = formatData("yyyy-MM-dd HH:mm:ss", Long.parseLong(mPresenter.exprie_time)/1000);
        /** 计算的时间差*/
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date d2 = df.parse(date2);
            long diff = d2.getTime() -currentTime;// 这样得到的差值是微秒级别
            if (diff<=0){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        timerView.setText("支付已过期");
                    }
                });
//                if (null != customPopWindow){
//                    if (customPopWindow.isShowing()) {
//                        customPopWindow.dismiss();
//                    }
//                }
                Intent intent = new Intent(ShoppingOrderSubmitActivity.this, ShoppingOrderDetailActivity.class);
                intent.putExtra("tradeno", mPresenter.tradeNo);
                intent.putExtra("isReturnHome",true);
                startActivity(intent);
                stopTimer();
                return;
            }
            long days = diff / (1000 * 60 * 60 * 24);
            long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
            final long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
            final long seconds=(diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)-minutes*(1000*60)) / 1000;
            /**主线程更新UI**/
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    timerView.setText("支付剩余时间 "+String.format("%0"+2+"d", minutes)+":"+String.format("%0"+2+"d", seconds));
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

    /**
     * 支付宝和微信支付
     * */
    private IWXAPI api;
    public static final int MSG_PAYED = 101;
    public static final int MSG_ALI_PAYED = 102;
    private void initPayInfo(){
        api = WXAPIFactory.createWXAPI(this, "wx7b23d0f800a068fa");
        api.handleIntent(getIntent(), this);
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }
    private void clickPayBtnWithStates(int states){
        if (states==0){/**支付宝*/
            goApiPay();
        }else {/**微信*/
            goWeiChatPay();
        }
    }

    private void goApiPay(){
        mPresenter.goApiPay();
    }

    private void goWeiChatPay(){
        mPresenter.goWeiChatPay();
    }


    @Override
    public void onSuccess(final BaseResponse bean, String type) {
      if (type.equals("create_order")){
         setupPopWindow();
      }
    }

    @Override
    public void onPay(final Object bean, String type) {
        if (type.equals("api_pay")){
            final Runnable payRunnable = new Runnable() {

                @Override
                public void run() {
                    AliPayJsonResp entity=(AliPayJsonResp)bean;
                    PayTask alipay = new PayTask(ShoppingOrderSubmitActivity.this);
                    Map<String, String> result = alipay.payV2(entity.getOrderString(), true);
                    Log.i("msp", result.toString());
                    Message msg = new Message();
                    msg.what = MSG_ALI_PAYED;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
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
            PrefUtil.setSaveTradeNo(mPresenter.tradeNo, ShoppingOrderSubmitActivity.this);
            PrefUtil.setIsReturnHome(true,ShoppingOrderSubmitActivity.this);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("支付结果");
            builder.setMessage(getString(R.string.pay_result_callback_msg, String.valueOf(baseResp.errCode)));
            builder.show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //ButterKnife.unbind(this);
        stopTimer();
        if (mHandler!=null)
            mHandler.removeCallbacksAndMessages(null);
    }
    @SuppressLint("HandlerLeak")
    private Handler mHandler=new Handler(){
        @SuppressWarnings("unused")
        @Override
        public void dispatchMessage(@NonNull Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what){
                case MSG_PAYED:
                    ShoppingOrderSubmitActivity.this.finish();
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
                    Log.i("alipay","resultStatus:"+resultStatus+"-- errmsg:"+errmsg+"---resultInfo:"+resultInfo);
                    Intent intent = new Intent(ShoppingOrderSubmitActivity.this, WXPayEntryActivity.class);
                    intent.putExtra(WXPayEntryActivity.INTENT_ERRCODE,resultStatus);
                    intent.putExtra(WXPayEntryActivity.INTENT_ERRMSG,errmsg);
                    intent.putExtra(WXPayEntryActivity.INTENT_ALI,true);
                    startActivityForResult(intent,102);
                    PrefUtil.setSaveTradeNo(mPresenter.tradeNo,ShoppingOrderSubmitActivity.this);
                    PrefUtil.setIsReturnHome(true,ShoppingOrderSubmitActivity.this);
                    ShoppingOrderSubmitActivity.this.finish();
                    break;
            }
        }
    };

}