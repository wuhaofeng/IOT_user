package com.iot.user.ui.activity.room;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.iot.user.R;
import com.iot.user.app.IOTApplication;
import com.iot.user.base.BaseActivity;
import com.iot.user.base.BaseMvpActivity;
import com.iot.user.databinding.ActivityUnitRoomListBinding;
import com.iot.user.http.model.BaseResponse;
import com.iot.user.skin.SkinManager;
import com.iot.user.ui.adapter.base.OnItemClickListener;
import com.iot.user.ui.adapter.room.UnitRoomListAdapter;
import com.iot.user.ui.contract.room.UnitRoomContract;
import com.iot.user.ui.model.main.UnitFamilyModel;
import com.iot.user.ui.model.main.UnitFamilyRoomModel;
import com.iot.user.ui.presenter.room.UnitRoomPresenter;
import com.iot.user.utils.DialogUtils;
import com.iot.user.utils.DoubleClickUtil;
import com.iot.user.utils.MyToast;
import com.iot.user.utils.PrefUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.observers.DisposableObserver;

public class UnitRoomListActivity extends BaseMvpActivity <UnitRoomPresenter, ActivityUnitRoomListBinding> implements UnitRoomContract.View {
    RecyclerView mRecyclerView;
    Toolbar mToolbar;
    Button btn_room_delete;

    private UnitFamilyModel familyModel=null;
    private UnitRoomListAdapter itemAdapter;
    private Boolean isEdit=false;/***是否处于编辑状态**/
    private Boolean isFromRegister=false; /***注册设备过来的时候不能删除**/
    public static String FAMILYMODEL="FamilyModel";
    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_unit_room_list;
    }
    @Override
    public void initView() {
        mPresenter=new UnitRoomPresenter();
        mPresenter.attachView(this);
        mRecyclerView=dataBinding.rvUnitFamilySelect;
        mToolbar=(Toolbar) dataBinding.toolbar;
        btn_room_delete=dataBinding.btnRoomDelete;
        Intent intent=getIntent();
        if (intent!=null) {
            familyModel = (UnitFamilyModel) getIntent().getSerializableExtra(FAMILYMODEL);
            isFromRegister=intent.getBooleanExtra("isFromRegister",false);
        }
        if (familyModel==null){
            familyModel=PrefUtil.getUnitFamilyModel();
        }
        initViews();
    }
    @Override
    protected void onResume() {
        super.onResume();
        initEdit();
        getRoomListData();
    }
    private void initViews(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //添加分割线
        DividerItemDecoration divider = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(this,R.drawable.line_list_item_decoration));
        mRecyclerView.addItemDecoration(divider);
        mRecyclerView.setVerticalScrollBarEnabled(true);
        initMyToolBar();
    }
    /***获取房间列表数据***/
    private void getRoomListData() {
        if (familyModel == null) {
            MyToast.showShortToast("未获取到家庭数据");
            return;
        }
        String familyType = "2";
        if (Double.valueOf(familyModel.getType()).intValue() == 1) {
            familyType = "1";
        }
        mPresenter.postUnitRoomList(familyModel.getFamily_id(),familyType);
    }
    private void initAdapter() {
        if (itemAdapter == null) {
            itemAdapter = new UnitRoomListAdapter(this, mPresenter.mDatas,isEdit);
            mRecyclerView.setAdapter(itemAdapter);
            itemAdapter.setOnItemClickLitener(new OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    if(!DoubleClickUtil.isFastDoubleClick(R.id.swipe_target)){
                        UnitFamilyRoomModel dataModel=mPresenter.mDatas.get(position);
                        if (isFromRegister==true){
                            Intent intent = new Intent();
                            intent.putExtra("respond", dataModel);
                            // 设置返回码和返回携带的数据
                            setResult(Activity.RESULT_OK, intent);
                            finish();
                            return;
                        }
                        if (isEdit==false){
                            jumpToRoomDetailVC(dataModel);
                        }else {

                        }
                    }
                }
            });
        } else {
            itemAdapter.updateDatas(mPresenter.mDatas,isEdit);
        }
    }

    private void initMyToolBar() {
        int currentSkinType = SkinManager.getCurrentSkinType(this);
        if (SkinManager.THEME_DAY == currentSkinType) {
            initToolBar(mToolbar, "房间管理", R.drawable.gank_ic_back_white);
        } else {
            initToolBar(mToolbar, "房间管理", R.drawable.gank_ic_back_night);
        }
        initToolBarRightBtn(mToolbar,"编辑",0,1);
    }

    private Button rightBtn;
    @Override
    protected void clickRightBtn(View button) {
        super.clickRightBtn(button);
        rightBtn=(Button)button;
        isEdit=!isEdit;
        if (isEdit==false) {
            rightBtn.setText("编辑");
            btn_room_delete.setVisibility(View.GONE);
        }else {
            rightBtn.setText("完成");
            btn_room_delete.setVisibility(View.VISIBLE);
        }
        initAdapter();
    }
    private void initEdit(){
        if (itemAdapter != null) {
            itemAdapter.selectMap.clear();
        }
        isEdit=false;
        if (rightBtn!=null) {
            rightBtn.setText("编辑");
        }
        btn_room_delete.setVisibility(View.GONE);
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
        if (type.equals("room_list")){
            initAdapter();
            if (mPresenter.mDatas.size()==0) {
                DialogUtils.showMyDialog(UnitRoomListActivity.this, "提示", "您还没有创建房间,是否现在去创建?", "确定", "取消", new DialogUtils.OnDialogClickListener() {
                    @Override
                    public void onConfirm() {
                        clickFamilyAddBtn();
                    }
                    @Override
                    public void onCancel() {
                    }
                });
            }
        }else if (type.equals("room_delete")){
            initEdit();
            getRoomListData();
        }

    }

    @Override
    protected void initClickBtn() {
        super.initClickBtn();
        dataBinding.btnRoomAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickFamilyAddBtn();
            }
        });
        dataBinding.btnRoomDelete.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                clickFamilyDeleteBtn();
            }
        });
    }
    void clickFamilyAddBtn(){
        Intent intent=new Intent(UnitRoomListActivity.this,UnitRoomAddActivity.class);
        intent.putExtra(FAMILYMODEL,familyModel);
        startActivity(intent);
    }
    public void jumpToRoomDetailVC(UnitFamilyRoomModel roomModel){
        Intent intent=new Intent(UnitRoomListActivity.this,UnitRoomDetailActivity.class);
        intent.putExtra("RoomModel",roomModel);
        intent.putExtra(FAMILYMODEL,familyModel);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    void clickFamilyDeleteBtn() {
        final List<String> dataList = new ArrayList<>();
        for (int i = 0; i < mPresenter.mDatas.size(); i++) {
            if (itemAdapter.selectMap.getOrDefault("" + i, false) == true) {
                UnitFamilyRoomModel roomModel = mPresenter.mDatas.get(i);
                dataList.add(roomModel.getFamily_place_id());
            }
        }
        if (dataList.size() == 0) {
            MyToast.showShortToast("请选择要删除的房间");
            return;
        }
        DialogUtils.showMyDialog(this, "提示", "确认要删除房间吗？", "确定","取消", new DialogUtils.OnDialogClickListener() {
            @Override
            public void onConfirm() {
                mPresenter.postUnitDeleteRoom(familyModel.getFamily_id(),dataList);
            }

            @Override
            public void onCancel() {

            }
        });
    }
}