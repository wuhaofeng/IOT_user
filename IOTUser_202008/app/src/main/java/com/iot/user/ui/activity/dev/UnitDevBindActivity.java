package com.iot.user.ui.activity.dev;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.google.gson.internal.LinkedTreeMap;
import com.iot.user.R;
import com.iot.user.app.IOTApplication;
import com.iot.user.base.BaseActivity;
import com.iot.user.base.BaseMvpActivity;
import com.iot.user.databinding.ActivityUnitDevBindBinding;
import com.iot.user.http.NetWorkApi;
import com.iot.user.http.model.BaseResponse;
import com.iot.user.http.net.RxUtils;
import com.iot.user.http.request.dev.CommonDevLowJsonRequest;
import com.iot.user.skin.SkinManager;
import com.iot.user.ui.activity.main.UnitMainActivity;
import com.iot.user.ui.adapter.dev.UnitDevBindAdapter;
import com.iot.user.ui.contract.dev.DevBindContract;
import com.iot.user.ui.model.dev.UnitDevBindModel;
import com.iot.user.ui.presenter.dev.DevBindPresenter;
import com.iot.user.utils.DialogUtils;
import com.iot.user.utils.MyToast;
import com.iot.user.utils.PrefUtil;

import java.util.Map;

import io.reactivex.observers.DisposableObserver;

public class UnitDevBindActivity extends BaseMvpActivity<DevBindPresenter,ActivityUnitDevBindBinding> implements DevBindContract.View {
    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_unit_dev_bind;
    }
    Toolbar mToolbar;
    ListView lv_reatine;

    public static final String DEV_ID = "devId";
    private String devId;
    private UnitDevBindAdapter scanDevInfoAdapter;
    @Override
    public void initView() {
      mPresenter=new DevBindPresenter();
      mPresenter.attachView(this);
      mToolbar=(Toolbar) dataBinding.toolbar;
      lv_reatine=dataBinding.lvReatine;
      initMyToolBar();
      devId = getIntent().getStringExtra(DEV_ID);
    }
    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.getDevBindInfo(devId);
    }
    private void initMyToolBar() {
        int currentSkinType = SkinManager.getCurrentSkinType(this);
        if (SkinManager.THEME_DAY == currentSkinType) {
            initToolBar(mToolbar, "关注设备", R.drawable.gank_ic_back_white);
        } else {
            initToolBar(mToolbar, "关注设备", R.drawable.gank_ic_back_night);
        }
        initToolBarRightBtn(mToolbar,"关注设备",0,1);
    }

    @Override
    protected void clickRightBtn(View button) {
        super.clickRightBtn(button);
        mPresenter.postUnitDevBinder(devId);
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

    @Override
    public void onSuccess(BaseResponse bean, String type) {
        if (type.equals("bind_info")){
            if(mPresenter.list!=null && mPresenter.list.size()> 0 ){
                scanDevInfoAdapter = new UnitDevBindAdapter(UnitDevBindActivity.this,mPresenter.list);
                lv_reatine.setAdapter(scanDevInfoAdapter);
            }else{
                showNoDev();
            }
        }else if (type.equals("dev_bind")){
            Intent intent=new Intent(UnitDevBindActivity.this, UnitMainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            MyToast.showShortToast("关注设备成功");
        }
    }

    @Override
    public void onError(String errMessage) {
        super.onError(errMessage);
        showNoDev();
    }

    @Override
    public void onComplete() {
        super.onComplete();
        showNoDev();
    }

    private void showNoDev(){
        DialogUtils.showMyConfirmDialog(UnitDevBindActivity.this, "提示", "查找不到设备信息", "确定",  new DialogUtils.OnDialogClickListener() {
            @Override
            public void onConfirm() {
                UnitDevBindActivity.this.finish();
            }
            @Override
            public void onCancel() {
                UnitDevBindActivity.this.finish();
            }
        });
    }
}