package com.iot.user.ui.activity.dev;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.iot.user.R;
import com.iot.user.app.IOTApplication;
import com.iot.user.base.BaseActivity;
import com.iot.user.base.BaseMvpActivity;
import com.iot.user.databinding.ActivityUnitDevEditBinding;
import com.iot.user.http.model.BaseResponse;
import com.iot.user.skin.SkinManager;
import com.iot.user.ui.activity.location.LocationActivity;
import com.iot.user.ui.activity.main.UnitMainActivity;
import com.iot.user.ui.activity.room.UnitRoomListActivity;
import com.iot.user.ui.contract.dev.DevEditContract;
import com.iot.user.ui.model.dev.UnitDevEditModel;
import com.iot.user.ui.model.location.MapBean;
import com.iot.user.ui.model.location.PickerAddressModel;
import com.iot.user.ui.model.main.UnitDevDetailModel;
import com.iot.user.ui.model.main.UnitFamilyModel;
import com.iot.user.ui.model.main.UnitFamilyRoomModel;
import com.iot.user.ui.presenter.dev.DevEditPresenter;
import com.iot.user.ui.view.location.AddressPickerUtil;
import com.iot.user.ui.view.main.MClearEditText;
import com.iot.user.utils.DoubleClickUtil;
import com.iot.user.utils.MyToast;
import com.iot.user.utils.PrefUtil;
import com.ymht.library.picker.address.AddressPicker;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

public class UnitDevEditActivity extends BaseMvpActivity<DevEditPresenter,ActivityUnitDevEditBinding> implements DevEditContract.View {

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_unit_dev_edit;
    }
    Toolbar mToolbar;
    EditText etDevnum;
    MClearEditText et_dev_name;
    TextView tv_areaname;/**所在地区**/
    MClearEditText etAddr;/***详细地址**/
    TextView tv_site_name;/***场所**/
    Button btnRegist;
    private String devNum="";
    private Boolean bindStatus=false;
    public static final String DEV_ID = "devId";
    public static final String STATUS_ID = "statusId";
    /**
     *从下一个activity回传的房间数据*
     * */
    public static final int LOCATION_RESULT = 1001;
    public static final int ROOM_RESULT = 1002;
    private String originRoomID="";/***当前类型**/
    /***获取定位的数据**/
    private PickerAddressModel pickerAddressModel;

    @Override
    public void initView() {
        mPresenter=new DevEditPresenter();
        mPresenter.attachView(this);
        mToolbar = (Toolbar) dataBinding.toolbar;
        etDevnum = dataBinding.etDevnum;
        et_dev_name = dataBinding.etDevName;
        tv_areaname = dataBinding.tvAreaname;
        etAddr = dataBinding.etAddr;
        tv_site_name = dataBinding.tvSiteName;
        btnRegist = dataBinding.btnRegist;
        initMyToolBar();
        Intent intent =getIntent();
        if (intent!=null) {
            devNum = intent.getStringExtra(DEV_ID);
            bindStatus=intent.getBooleanExtra(STATUS_ID,false);
            if (TextUtils.isEmpty(devNum)){
                MyToast.showShortToast("未获取到设备数据");
                finish();
            }else {
                etDevnum.setText(devNum);
                etDevnum.setEnabled(false);
                initDevInfo();
            }
        }
    }
    private void initDevInfo() {/***获取设备信息***/
       mPresenter.postUnitDevInfo(etDevnum.getText().toString());
    }
    private void updateDevEdit(){/***修改设备信息=**/
        if (bindStatus==false) {/**修改以及绑定设备*/
           mPresenter.updateAndBindDev(pickerAddressModel.getLat()
                   , pickerAddressModel.getLon(), etAddr.getText().toString(), pickerAddressModel.getProvince()
                   , pickerAddressModel.getCity(), pickerAddressModel.getDistrict(), pickerAddressModel.getStreet(),
                   et_dev_name.getText().toString(), etDevnum.getText().toString());
        }else{/**编辑设备:需要调用编辑设备和移动设备两个接口*/
            UnitDevEditModel reqModel=new UnitDevEditModel(devNum,etAddr.getText().toString(),
                    et_dev_name.getText().toString(), tv_site_name.getText().toString()
                    ,pickerAddressModel.getProvince(),pickerAddressModel.getCity(),pickerAddressModel.getDistrict(),pickerAddressModel.getStreet(),
                    String.valueOf(pickerAddressModel.getLon()),String.valueOf(pickerAddressModel.getLat()));
           mPresenter.postUnitDevEdit(reqModel);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==ROOM_RESULT){
            if (data!=null){
                mPresenter.roomModel=(UnitFamilyRoomModel)data.getSerializableExtra("respond");
                tv_site_name.setText(mPresenter.roomModel.getPlace_name());
            }
        }
        if (requestCode == LOCATION_RESULT) {
            try {
                MapBean mapBean = (MapBean) data.getSerializableExtra("mapBean");
                String division = data.getStringExtra("division");
                String addressDetail = data.getStringExtra("addressDetail");
                if(division != null &&!"".equals(division)){
                    tv_areaname.setText(division);
                }else{
                    tv_areaname.setText(mapBean.getArea());
                }
                String address = mapBean.getAddr();
                if(address!=null){
                    etAddr.setText(address);
                    etAddr.setSelection(address.length());
                }
                List<String> areaList= Arrays.asList(division.split("/"));
                List<String>latlon=Arrays.asList(mapBean.getLatlon().split(","));
                String province="";
                String city = "";
                String district="";
                String street="";
                String lon="";
                String lat="";
                if (areaList.size()>0) {
                    province=areaList.get(0);
                }
                if (areaList.size()>1) {
                    city=areaList.get(1);
                }
                if (areaList.size()>2) {
                    district=areaList.get(2);
                }
                if (areaList.size()>3) {
                    street=areaList.get(3);
                }
                if (latlon.size()>0) {
                    lat=latlon.get(0);
                }
                if (latlon.size()>1) {
                    lon=latlon.get(1);
                }
                pickerAddressModel=new PickerAddressModel(province,city,
                        district,street,Double.valueOf(lon),Double.valueOf(lat),
                        division,mapBean.getAddr());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void initMyToolBar() {
        int currentSkinType = SkinManager.getCurrentSkinType(this);
        if (SkinManager.THEME_DAY == currentSkinType) {
            initToolBar(mToolbar, "编辑设备信息", R.drawable.gank_ic_back_white);
        } else {
            initToolBar(mToolbar, "编辑设备信息", R.drawable.gank_ic_back_night);
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
       if (type.equals("dev_detail")){
           dealDevDetailInfo(mPresenter.detailModel);
       }else if (type.equals("update_bind")){
           Intent intent=new Intent(this, UnitMainActivity.class);
           intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
           startActivity(intent);
       }else  if (type.equals("dev_edit")){
           if (mPresenter.roomModel.getFamily_place_id().equals(originRoomID)){
               MyToast.showShortToast("修改成功");
               finish();
           }else {
               mPresenter.removePlace(devNum,originRoomID,mPresenter.roomModel.getFamily_place_id());
           }
       }else  if (type.equals("remove_room")){
           MyToast.showShortToast("修改成功");
           finish();
       }
    }

    private void dealDevDetailInfo(UnitDevDetailModel devDetailModel){
        if (devDetailModel.getPlace_name()!=null&&devDetailModel.getFamily_place_id()!=null) {
            mPresenter.roomModel = new UnitFamilyRoomModel(mPresenter.familyModel.getFamily_id(), devDetailModel.getPlace_name(), devDetailModel.getFamily_place_id(), "", "");
            tv_site_name.setText(devDetailModel.getPlace_name());
            originRoomID=devDetailModel.getFamily_place_id();
        }
        if (devDetailModel.getFriend_name()!=null){
            et_dev_name.setText(devDetailModel.getFriend_name());
        }
        String devAddress=devDetailModel.getArea_Name_1()+"/"+devDetailModel.getArea_Name_2()+"/"+devDetailModel.getArea_Name_3()+"/"+devDetailModel.getArea_Name_4();
        pickerAddressModel=new PickerAddressModel(devDetailModel.getArea_Name_1(),devDetailModel.getArea_Name_2(),
                devDetailModel.getArea_Name_3(),devDetailModel.getArea_Name_4(),Double.valueOf(devDetailModel.getLon()),Double.valueOf(devDetailModel.getLat()),
                devAddress,devDetailModel.getAddress());
        if (devAddress!=null) {
            tv_areaname.setText(devAddress);
        }
        if (devDetailModel.getAddress()!=null) {
            etAddr.setText(devDetailModel.getAddress());
        }
    }

    @Override
    protected void initClickBtn() {
        super.initClickBtn();
        dataBinding.ivLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, LocationActivity.class);
                startActivityForResult(intent, LOCATION_RESULT);
            }
        });
        tv_areaname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickTvAreaname();
            }
        });
        tv_site_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickedPlace();
            }
        });
        btnRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickRegister();
            }
        });
    }
    void clickTvAreaname() {
        final AddressPicker addressPicker = new AddressPicker(this);
        new AddressPickerUtil(addressPicker,this).setOnPickerLocationListener(new AddressPickerUtil.OnPickerLocationListener() {
            @Override
            public void onPickerStringAndLocation(PickerAddressModel addressModel) {
                tv_areaname.setText(addressModel.getAddressInfo());
                pickerAddressModel=addressModel;
            }
        });
        addressPicker.show();
    }
    void onClickedPlace() {
        if (DoubleClickUtil.isFastClick()) {
            Intent intent = new Intent(mContext, UnitRoomListActivity.class);
            intent.putExtra("isFromRegister", true);
            startActivityForResult(intent, ROOM_RESULT);
        }
    }
    void clickRegister(){
        if (TextUtils.isEmpty(etDevnum.getText().toString())){
            MyToast.showShortToast("请输入设备编号");
            return;
        }
        if (TextUtils.isEmpty(et_dev_name.getText().toString())){
            MyToast.showShortToast("请输入设备名");
            return;
        }
        if (et_dev_name.getText().toString().length()>20){
            MyToast.showShortToast("设备名称最多可20位");
            return;
        }
        if (pickerAddressModel==null){
            MyToast.showShortToast("请选择所在地区");
            return;
        }
        if (TextUtils.isEmpty(etAddr.getText().toString())){
            MyToast.showShortToast("请输入详细地址");
            return;
        }
        if (etAddr.getText().toString().length()>100){
            MyToast.showShortToast("详细地址最多可100位");
            return;
        }
        if (mPresenter.roomModel==null){
            MyToast.showShortToast("请给设备选择安装场所");
            return;
        }
        /***修改设备信息**/
        updateDevEdit();
    }

}