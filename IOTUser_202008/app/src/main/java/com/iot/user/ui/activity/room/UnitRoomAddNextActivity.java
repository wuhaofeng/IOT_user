package com.iot.user.ui.activity.room;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.gson.internal.LinkedTreeMap;
import com.iot.user.R;
import com.iot.user.app.IOTApplication;
import com.iot.user.base.BaseMvpActivity;
import com.iot.user.databinding.ActivityUnitRoomAddNextBinding;
import com.iot.user.http.model.BaseResponse;
import com.iot.user.skin.SkinManager;
import com.iot.user.ui.contract.room.UnitRoomContract;
import com.iot.user.ui.model.main.UnitFamilyModel;
import com.iot.user.ui.presenter.room.UnitRoomPresenter;
import com.iot.user.utils.AppValidationMgr;
import com.iot.user.utils.DoubleClickUtil;
import com.iot.user.utils.MyToast;

import static com.iot.user.ui.activity.room.UnitRoomListActivity.FAMILYMODEL;

public class UnitRoomAddNextActivity extends BaseMvpActivity<UnitRoomPresenter, ActivityUnitRoomAddNextBinding> implements UnitRoomContract.View {

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_unit_room_add_next;
    }
    Toolbar mToolbar;
    EditText et_room_name;
    private String roomName="";
    private UnitFamilyModel familyModel;
    @Override
    public void initView() {
        mPresenter=new UnitRoomPresenter();
        mPresenter.attachView(this);
        mToolbar=(Toolbar)dataBinding.toolbar;
        et_room_name=dataBinding.etRoomName;
        Intent intent=getIntent();
        roomName=intent.getStringExtra("RoomName");
        familyModel=(UnitFamilyModel) intent.getSerializableExtra(FAMILYMODEL);
        initMyToolBar();
        checkRoomName();
    }
    /***核对房间名称是否重复**/
    private void checkRoomName() {
        if (TextUtils.isEmpty(roomName)) {
            return;
        }
        mPresenter.postUnitCheckRoom(familyModel.getFamily_id(),roomName);
    }
    /***创建房间**/
    private void createRoom() {
        if (familyModel == null) {
            MyToast.showShortToast("未获取到家庭数据");
            return;
        }
        String roomName = AppValidationMgr.removeStringSpace(et_room_name.getText().toString(), 0);
        if (TextUtils.isEmpty(roomName)) {
            MyToast.showShortToast("请输入房间名称");
            return;
        }
        if (roomName.length() > 20) {
            MyToast.showShortToast("房间名称最多可20位");
            return;
        }
        mPresenter.postUnitCreateRoom(familyModel.getFamily_id(),roomName);
    }
    private void initMyToolBar() {
        int currentSkinType = SkinManager.getCurrentSkinType(this);
        if (SkinManager.THEME_DAY == currentSkinType) {
            initToolBar(mToolbar, "添加房间", R.drawable.gank_ic_back_white);
        } else {
            initToolBar(mToolbar, "添加房间", R.drawable.gank_ic_back_night);
        }
        initToolBarRightBtn(mToolbar,"确认",0,1);
    }

    @Override
    protected void clickRightBtn(View button) {
        super.clickRightBtn(button);
        if (DoubleClickUtil.isFastClick()) {
            createRoom();
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
    public void onSuccess(BaseResponse bean, String type) {
        if (type.equals("room_check_name")){
            LinkedTreeMap map=(LinkedTreeMap) bean.getBody();
            et_room_name.setText((String)map.get("placeName"));
        }else if (type.equals("room_create")){
            Intent intent = new Intent();
            intent.putExtra("respond", "finish");
            // 设置返回码和返回携带的数据
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    }
}