package com.iot.user.ui.activity.room;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.iot.user.R;
import com.iot.user.app.IOTApplication;
import com.iot.user.base.BaseMvpActivity;
import com.iot.user.databinding.ActivityUnitRoomAddBinding;
import com.iot.user.http.model.BaseResponse;
import com.iot.user.skin.SkinManager;
import com.iot.user.ui.adapter.base.OnItemClickListener;
import com.iot.user.ui.adapter.room.UnitAddRoomAdapter;
import com.iot.user.ui.contract.room.UnitRoomContract;
import com.iot.user.ui.model.dev.UnitPublicRoomModel;
import com.iot.user.ui.model.main.UnitFamilyModel;
import com.iot.user.ui.presenter.room.UnitRoomPresenter;
import com.iot.user.utils.PXTransUtils;
import com.luck.picture.lib.decoration.GridSpacingItemDecoration;

import static com.iot.user.ui.activity.room.UnitRoomListActivity.FAMILYMODEL;

public class UnitRoomAddActivity extends BaseMvpActivity<UnitRoomPresenter, ActivityUnitRoomAddBinding> implements UnitRoomContract.View {
    Toolbar mToolbar;
    RecyclerView mRecyclerView;
    private UnitAddRoomAdapter recycleItemAdapter;
    private UnitFamilyModel familyModel;
    private int REQUEST_CODE=1111;
    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_unit_room_add;
    }
    @Override
    public void initView() {
        mPresenter=new UnitRoomPresenter();
        mPresenter.attachView(this);
        mToolbar=(Toolbar)dataBinding.toolbar;
        mRecyclerView=dataBinding.rvRoomAdd;
        Intent intent=getIntent();
        if (intent!=null)
        familyModel=(UnitFamilyModel) intent.getSerializableExtra(FAMILYMODEL);
        initMyToolBar();
        initViews();
        getAddRoomList();
    }
    private void initViews(){
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,4));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(4, PXTransUtils.dp2px(this,10), true));
        mRecyclerView.setVerticalScrollBarEnabled(true);
        initItemAdapter();
    }
    private void getAddRoomList() {
        mPresenter.postUnitPublicRoomList();
    }
    private void initItemAdapter(){
        if (recycleItemAdapter == null ) {
            if(mPresenter.publicDatas!=null){
                recycleItemAdapter = new UnitAddRoomAdapter(this, mPresenter.publicDatas);
                if(mRecyclerView!=null){
                    mRecyclerView.setAdapter(recycleItemAdapter);
                }
                recycleItemAdapter.setOnItemClickLitener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        String roomName="";
                        if (position<mPresenter.publicDatas.size()){
                            UnitPublicRoomModel roomModel=mPresenter.publicDatas.get(position);
                            roomName=roomModel.getPlace_name();
                        }
                        Intent intent=new Intent(UnitRoomAddActivity.this,UnitRoomAddNextActivity.class);
                        intent.putExtra("RoomName",roomName);
                        intent.putExtra(FAMILYMODEL,familyModel);
                        startActivityForResult(intent,REQUEST_CODE);
                    }
                });
            }
        } else {
            if(mPresenter.publicDatas!=null){
                recycleItemAdapter.updateDatas(mPresenter.publicDatas);
            }
        }
    }
    private void initMyToolBar() {
        int currentSkinType = SkinManager.getCurrentSkinType(this);
        if (SkinManager.THEME_DAY == currentSkinType) {
            initToolBar(mToolbar, "添加房间", R.drawable.gank_ic_back_white);
        } else {
            initToolBar(mToolbar, "添加房间", R.drawable.gank_ic_back_night);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_CODE){
            if (data!=null) {
                String respond = data.getStringExtra("respond");
                finish();
            }
        }
    }

    @Override
    public void onSuccess(BaseResponse bean, String type) {
         if (type.equals("room_public_list")){
             initItemAdapter();
         }
    }
}