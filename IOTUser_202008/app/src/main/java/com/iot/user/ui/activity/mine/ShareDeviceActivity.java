package com.iot.user.ui.activity.mine;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.iot.user.R;
import com.iot.user.base.BaseActivity;
import com.iot.user.databinding.ActivityShareDeviceBinding;
import com.iot.user.skin.SkinManager;
import com.iot.user.utils.BitmapUtils;

public class ShareDeviceActivity extends BaseActivity<ActivityShareDeviceBinding> {
    Toolbar mToolbar;
    ImageView iv_dev_barcode;
    TextView tv_devnum;
    public static final String DEV_NUM = "devNum";
    public static final String STATUS = "status";/****1为设备分享，2为成员分享**/
    public String fragmentStatus="";
    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_share_device;
    }

    @Override
    public void initView() {
        if (getIntent().getStringExtra(STATUS)!=null) {
            fragmentStatus = getIntent().getStringExtra(STATUS);
        }else {
            fragmentStatus ="1";
        }
        mToolbar=(Toolbar)dataBinding.toolbar;
        iv_dev_barcode=dataBinding.ivDevBarcode;
        tv_devnum=dataBinding.tvDevnum;
        initMyToolBar();
        initBarcode();
    }
    private void initBarcode(){
        if (fragmentStatus.equals("2")) {//分享成员
            String devNum = getIntent().getStringExtra(DEV_NUM);
            tv_devnum.setText("用户电话:" + devNum);
            if (devNum != null && !"".equals(devNum)) {
                iv_dev_barcode.setImageBitmap(BitmapUtils.createQrBitmapFromString(devNum));
            } else {
                //设置一个默认失败的图片
            }
        }else {//设备分享
            String devNum = getIntent().getStringExtra(DEV_NUM);
            tv_devnum.setText("设备编号:" + devNum);
            if (devNum != null && !"".equals(devNum)) {
                iv_dev_barcode.setImageBitmap(BitmapUtils.createQrBitmapFromString(devNum));
            } else {
                //设置一个默认失败的图片
            }
        }
    }

    private void initMyToolBar() {
        int currentSkinType = SkinManager.getCurrentSkinType(this);
        String title="";
        if (fragmentStatus.equals("2")){
            title="成员分享";
        }else {
            title="设备分享";
        }
        if (SkinManager.THEME_DAY == currentSkinType) {

            initToolBar(mToolbar, title, R.drawable.gank_ic_back_white);
        } else {
            initToolBar(mToolbar, title, R.drawable.gank_ic_back_night);
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

}