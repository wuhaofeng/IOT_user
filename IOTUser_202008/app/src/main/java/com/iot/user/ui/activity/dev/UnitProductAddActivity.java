package com.iot.user.ui.activity.dev;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.iot.user.R;
import com.iot.user.base.BaseActivity;
import com.iot.user.databinding.ActivityUnitProductAddBinding;
import com.iot.user.skin.SkinManager;
import com.iot.user.ui.adapter.base.OnItemClickListener;
import com.iot.user.ui.adapter.dev.UnitProductAdpater;
import com.iot.user.ui.model.dev.UnitPublicRoomModel;
import com.iot.user.ui.view.zxing.activity.CaptureActivity;
import com.iot.user.utils.DoubleClickUtil;
import com.iot.user.utils.PXTransUtils;
import com.luck.picture.lib.decoration.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class UnitProductAddActivity extends BaseActivity<ActivityUnitProductAddBinding> {
    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_unit_product_add;
    }
    Toolbar mToolbar;
    RecyclerView mRecyclerView;
    private UnitProductAdpater itemAdapter;
    private List<UnitPublicRoomModel> mDatas=new ArrayList<>();
    private static int QRCodeShare=1002;
    @Override
    public void initView() {
        mToolbar=(Toolbar) dataBinding.toolbar;
        mRecyclerView=dataBinding.rvProductAdd;
        initMyToolBar();
        initViews();
    }
    private void initViews(){
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,3));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(3, PXTransUtils.dp2px(this,10), true));
        mRecyclerView.setVerticalScrollBarEnabled(true);
        initDatas();
        initItemAdapter();
    }

    private void initDatas(){
        UnitPublicRoomModel roomModel1 =new UnitPublicRoomModel("182","燃气探测器",""+R.drawable.d182);
        mDatas.add(roomModel1);
        UnitPublicRoomModel roomModel2 =new UnitPublicRoomModel("192","商用探测器",""+R.drawable.d192);
        mDatas.add(roomModel2);
    }

    private void initItemAdapter(){
        if (itemAdapter == null ) {
            if(mDatas!=null){
                itemAdapter = new UnitProductAdpater(this, mDatas);
                if(mRecyclerView!=null){
                    mRecyclerView.setAdapter(itemAdapter);
                }
                itemAdapter.setOnItemClickLitener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        UnitPublicRoomModel selectModel =mDatas.get(position);
                        registerDev(selectModel);
                    }
                });
            }
        } else {
            if(mDatas!=null){
                itemAdapter.updateDatas(mDatas);
            }
        }
    }
    private void initMyToolBar() {
        int currentSkinType = SkinManager.getCurrentSkinType(this);
        if (SkinManager.THEME_DAY == currentSkinType) {
            initToolBar(mToolbar, "添加设备", R.drawable.gank_ic_back_white);
        } else {
            initToolBar(mToolbar, "添加设备", R.drawable.gank_ic_back_night);
        }
        initToolBarRightBtn(mToolbar,"",R.drawable.scan_white,0);
    }

    @Override
    protected void clickRightBtn(View button) {
        super.clickRightBtn(button);
        /**扫码*/
        if(!DoubleClickUtil.isFastDoubleClick(button.getId())){
            Intent intent = new Intent(UnitProductAddActivity.this, CaptureActivity.class);
            intent.putExtra(CaptureActivity.INTENT_TYPE,CaptureActivity.SCAN_DEV_DETAIL);
            startActivityForResult(intent,QRCodeShare);
        }
    }
    private void registerDev(UnitPublicRoomModel selectModel){/***注册设备**/
        if (selectModel!=null){/**点击商品的列表进入**/
            Intent intent = new Intent(UnitProductAddActivity.this, UnitDevRegisterActivity.class);
            intent.putExtra(UnitDevRegisterActivity.REGIST_STATUS,0);/**未注册***/
            intent.putExtra(UnitDevRegisterActivity.PUBLIC_DEV,selectModel);/**产品类型***/
            startActivity(intent);
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