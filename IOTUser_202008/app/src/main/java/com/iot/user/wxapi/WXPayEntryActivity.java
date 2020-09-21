package com.iot.user.wxapi;

import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.iot.user.R;
import com.iot.user.app.IOTApplication;
import com.iot.user.base.BaseActivity;
import com.iot.user.databinding.ActivityPayResultBinding;
import com.iot.user.skin.SkinManager;
import com.iot.user.ui.activity.shopping.ShoppingOrderDetailActivity;
import com.iot.user.utils.PrefUtil;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends BaseActivity<ActivityPayResultBinding> implements IWXAPIEventHandler {
    private static final String TAG = "WXPayEntryActivity";
    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_pay_result;
    }
    private IWXAPI api;
    private String tradeNo;
    private boolean isReturnHome;
    Toolbar mToolbar;
    TextView pay_result;//支付结果
    Button btn_ok;//支付结果
    ImageView iv_pay_result;//支付结果图片
    public static final String INTENT_ALI = "is_alipay";
    public static final String INTENT_ERRCODE = "errcode";
    public static final String INTENT_ERRMSG = "errmsg";
    @Override
    public void initView() {
      mToolbar=(Toolbar) dataBinding.toolbar;
      pay_result=dataBinding.payResult;
      btn_ok=dataBinding.btnOk;
      iv_pay_result=dataBinding.ivPayResult;
        initMyToolBar();
        tradeNo= PrefUtil.getSaveTradeNo(IOTApplication.getIntstance());
        isReturnHome=PrefUtil.getIsReturnHome(IOTApplication.getIntstance());
        boolean isAli = getIntent().getBooleanExtra(INTENT_ALI,false);
        if(isAli){
            /**
             * 支付宝
             * **/
            String errcode = getIntent().getStringExtra(INTENT_ERRCODE);
            String errmsg = getIntent().getStringExtra(INTENT_ERRMSG);
            boolean isSuccess=false;
            if(errcode!=null && errcode.equals("9000")){//支付成功
                pay_result.setText("支付成功");
                isSuccess=true;
                iv_pay_result.setImageResource(R.drawable.pay_success);
                pay_result.setVisibility(View.GONE);
            }else if(errcode!=null && errcode.equals("6001")){//用户取消
                iv_pay_result.setImageResource(R.drawable.pay_fail);
                pay_result.setText("您取消了支付");
                pay_result.setVisibility(View.VISIBLE);
            }else{//其他支付失败
                iv_pay_result.setImageResource(R.drawable.pay_fail);
                pay_result.setText(errmsg);
                pay_result.setVisibility(View.VISIBLE);
            }
            goToOrderDetailActivity(isSuccess);
        }
        api = WXAPIFactory.createWXAPI(this, "wx7b23d0f800a068fa");
        api.handleIntent(getIntent(), this);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToOrderDetailActivity(true);
                WXPayEntryActivity.this.finish();
            }
        });
//        if(PayManageActivity.payHandler != null){
//            PayManageActivity.payHandler.sendEmptyMessage(PayManageActivity.MSG_PAYED);
//        }
    }
    private void initMyToolBar() {
        int currentSkinType = SkinManager.getCurrentSkinType(this);
        if (SkinManager.THEME_DAY == currentSkinType) {
            initToolBar(mToolbar, "支付结果", R.drawable.gank_ic_back_white);
        } else {
            initToolBar(mToolbar, "支付结果", R.drawable.gank_ic_back_night);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }


    @Override
    public void onReq(BaseReq baseReq) {

    }

    /***
     * 支付宝支付未执行到此接口,微信回调在这执行
     * **/
    @Override
    public void onResp(BaseResp resp) {
        Log.d(TAG, "onPayFinish, errCode = " + resp.errCode+"errStr ="+resp.errStr);
        boolean isSuccess=false;
        switch (resp.errCode){
            case BaseResp.ErrCode.ERR_OK:
                pay_result.setText("支付成功");
                isSuccess=true;
                iv_pay_result.setImageResource(R.drawable.pay_success);
                pay_result.setVisibility(View.GONE);
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                iv_pay_result.setImageResource(R.drawable.pay_fail);
                pay_result.setText("您取消了支付");
                pay_result.setVisibility(View.VISIBLE);
                break;
            default:
                iv_pay_result.setImageResource(R.drawable.pay_fail);
                pay_result.setText("支付失败");
                pay_result.setVisibility(View.VISIBLE);
                break;
        }
        goToOrderDetailActivity(isSuccess);
    }
    private void goToOrderDetailActivity(boolean isSuccess){
        Intent intent = new Intent(WXPayEntryActivity.this, ShoppingOrderDetailActivity.class);
        intent.putExtra("tradeno", tradeNo);
        intent.putExtra("isReturnHome",isReturnHome);
        intent.putExtra("isSuccess",isSuccess);
        startActivity(intent);
        this.finish();
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


}