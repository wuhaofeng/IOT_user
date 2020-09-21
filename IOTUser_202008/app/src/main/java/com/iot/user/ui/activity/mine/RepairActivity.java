package com.iot.user.ui.activity.mine;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.iot.user.R;
import com.iot.user.base.BaseActivity;
import com.iot.user.constant.HttpErrorCode;
import com.iot.user.databinding.ActivityRepairBinding;
import com.iot.user.http.NetWorkApi;
import com.iot.user.http.model.BaseResponse;
import com.iot.user.http.net.RxUtils;
import com.iot.user.http.request.mine.RepairJsonRequest;
import com.iot.user.skin.SkinManager;
import com.iot.user.ui.view.main.MClearEditText;
import com.iot.user.utils.DoubleClickUtil;
import com.iot.user.utils.KeyboardUtils;
import com.iot.user.utils.MyToast;
import com.iot.user.utils.PrefUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.observers.DisposableObserver;

public class RepairActivity extends BaseActivity<ActivityRepairBinding> {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_dev_id)
    MClearEditText et_dev_id;
    @BindView(R.id.et_repair_content)
    EditText et_repair_content;
    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_repair;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        initMyToolBar();
    }
    private void initMyToolBar() {
        int currentSkinType = SkinManager.getCurrentSkinType(this);
        if (SkinManager.THEME_DAY == currentSkinType) {
            initToolBar(toolbar, "设备报修", R.drawable.gank_ic_back_white);
        } else {
            initToolBar(toolbar, "设备报修", R.drawable.gank_ic_back_night);
        }
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


    @SuppressLint("AutoDispose")
    @OnClick(R.id.btn_repair)
    public void repair() {
        KeyboardUtils.hideSoftInput(this);
        RepairJsonRequest repairJsonRequest = new RepairJsonRequest(PrefUtil.getLoginAccountUid(RepairActivity.this),
                PrefUtil.getLoginToken(RepairActivity.this),
                et_dev_id.getText().toString(),et_repair_content.getText().toString());
        NetWorkApi.provideRepositoryData().devRepair(repairJsonRequest)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        if (entity.getCode()==0){
                            Toast.makeText(RepairActivity.this,"您的设备已经报修成功，我们尽快安排维修人员上门服务",Toast.LENGTH_SHORT).show();
                            RepairActivity.this.finish();
                        }else{
                            MyToast.showShortToast(entity.getMessage());
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                    }
                    @Override
                    public void onComplete() {
                    }
                });
    }

    @OnClick(R.id.scan_iv)
    public void scan() {
        /////扫码
        if(!DoubleClickUtil.isFastDoubleClick(R.id.scan_iv)){
//            Intent intent = new Intent(RepairActivity.this, CaptureActivity.class);
//            intent.putExtra(CaptureActivity.INTENT_TYPE,CaptureActivity.SCAN_REPAIR);
//            startActivityForResult(intent,BARCODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            String barcode = data.getExtras().getString("code");
            et_dev_id.setText(barcode);
            if(barcode!=null){
                et_dev_id.setSelection(barcode.length());
            }
        }


    }
}