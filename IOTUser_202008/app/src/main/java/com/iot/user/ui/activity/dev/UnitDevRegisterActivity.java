package com.iot.user.ui.activity.dev;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.iot.user.R;
import com.iot.user.app.IOTApplication;
import com.iot.user.base.BaseMvpActivity;
import com.iot.user.constant.DevConstants;
import com.iot.user.databinding.ActivityUnitDevRegisterBinding;
import com.iot.user.http.model.BaseResponse;
import com.iot.user.skin.SkinManager;
import com.iot.user.ui.activity.location.LocationActivity;
import com.iot.user.ui.activity.main.UnitMainActivity;
import com.iot.user.ui.activity.room.UnitRoomListActivity;
import com.iot.user.ui.contract.dev.DevRegisterContract;
import com.iot.user.ui.model.dev.UnitPublicRoomModel;
import com.iot.user.ui.model.location.MapBean;
import com.iot.user.ui.model.location.PickerAddressModel;
import com.iot.user.ui.model.main.UnitFamilyRoomModel;
import com.iot.user.ui.presenter.dev.DevRegisterPresenter;
import com.iot.user.ui.view.location.AddressPickerUtil;
import com.iot.user.ui.view.main.MClearEditText;
import com.iot.user.utils.AppValidationMgr;
import com.iot.user.utils.DoubleClickUtil;
import com.iot.user.utils.MyToast;
import com.iot.user.utils.PrefUtil;
import com.ymht.library.picker.address.AddressPicker;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

public class UnitDevRegisterActivity extends BaseMvpActivity <DevRegisterPresenter, ActivityUnitDevRegisterBinding> implements DevRegisterContract.View {
    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_unit_dev_register;
    }
    Toolbar mToolbar;
    MClearEditText etDevnum;
    MClearEditText et_dev_name;
    TextView tv_areaname;/**所在地区**/
    MClearEditText etAddr;/***详细地址**/
    TextView tv_site_name;/***场所**/
    Button btnRegist;
    public static final String DEV_ID = "devId";
    public static final String REGIST_STATUS = "regist_status";
    public static final String PUBLIC_DEV = "public_dev";
    /**
     * *0为扫描进入，1为选择通用商品类型进入
     * */
    private int joinStatus=0;
    /**
     * *从上一个activity传过来的数据*
     * */
    private Boolean homeFragemntIsRegister;
    private String devNum="";
    /**
     *从下一个activity回传的房间数据*
     * */
    public static final int LOCATION_RESULT = 1001;
    public static final int ROOM_RESULT = 1002;

    private int regist_status = DevConstants.DEV_UNREGIST;
    /***获取定位的数据**/
    private PickerAddressModel pickerAddressModel;
    private UnitPublicRoomModel publicModel;//有燃气报警器和商用报警器两种model

    @Override
    public void initView() {
        mPresenter = new DevRegisterPresenter();
        mPresenter.attachView(this);
        mToolbar = (Toolbar) dataBinding.toolbar;
        etDevnum = dataBinding.etDevnum;
        et_dev_name = dataBinding.etDevName;
        tv_areaname = dataBinding.tvAreaname;
        etAddr = dataBinding.etAddr;
        tv_site_name = dataBinding.tvSiteName;
        btnRegist = dataBinding.btnRegist;
        initMyToolBar();
        Intent intent = getIntent();
        if (intent != null) {
            homeFragemntIsRegister = intent.getBooleanExtra("FragemntIsRegister", false);
            devNum = intent.getStringExtra(DEV_ID);
            if (TextUtils.isEmpty(devNum)) {/****通过选择产品类型而非扫码进来的***/
                publicModel = (UnitPublicRoomModel) intent.getSerializableExtra(PUBLIC_DEV);
                joinStatus = 1;
                et_dev_name.setText(publicModel.getPlace_name());
                etDevnum.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if (editable.toString().isEmpty())
                            return;
                        if (editable.toString().length() > 4) {
                            if (editable.toString().contains(publicModel.getPlace_dictionary_id())) {

                            } else {
                                MyToast.showShortToast("亲，您的探测器型号对不上哦，请检查输入");
                            }
                        }
                    }
                });
            }
            regist_status = intent.getIntExtra(REGIST_STATUS, 0);
            if (regist_status == DevConstants.DEV_REGISTED_NO_MASTER) {/**已注册但是未绑定，修改并绑定设备***/
                if (btnRegist != null) {
                    btnRegist.setText("修改并绑定设备");
                }
            }
            if (!TextUtils.isEmpty(devNum)) {
                etDevnum.setText(devNum);
                if (homeFragemntIsRegister == false) {
//                    initDevInfo();
                }
            }
            initDefaultData();/**加载默认数据**/
        }
    }
    private void registerAndBindDev() {/***注册并绑定设备**/
        String devNo = AppValidationMgr.removeStringSpace(etDevnum.getText().toString(), 0);
        mPresenter.registerAndBindDev(pickerAddressModel.getLat()
                ,pickerAddressModel.getLon(),etAddr.getText().toString(),pickerAddressModel.getProvince()
                ,pickerAddressModel.getCity(),pickerAddressModel.getDistrict(),pickerAddressModel.getStreet(),
                et_dev_name.getText().toString(),devNo);
    }
    private void updateAndBindDev() {/***修改设备信息并绑定**/
        String devNo = AppValidationMgr.removeStringSpace(etDevnum.getText().toString(), 0);
        mPresenter.updateAndBindDev(pickerAddressModel.getLat()
                ,pickerAddressModel.getLon(),etAddr.getText().toString(),pickerAddressModel.getProvince()
                ,pickerAddressModel.getCity(),pickerAddressModel.getDistrict(),pickerAddressModel.getStreet(),
                et_dev_name.getText().toString(),devNo);
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

    private void initDefaultData(){
        if (PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()).equals("4614662258988253238")||PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()).equals("3")){
            pickerAddressModel=new PickerAddressModel("上海市","上海市",
                    "闵行区","浦江镇",Double.valueOf(121.525668),Double.valueOf(31.075134),
                    "上海市/上海市/闵行区/浦江镇","生产车间");
            etAddr.setText(pickerAddressModel.getDetailAddressInfo());
            tv_areaname.setText(pickerAddressModel.getAddressInfo());
            mPresenter.postUnitFamilyRoomList();
        }
    }
    private void initMyToolBar() {
        int currentSkinType = SkinManager.getCurrentSkinType(this);
        if (SkinManager.THEME_DAY == currentSkinType) {
            initToolBar(mToolbar, "注册设备", R.drawable.gank_ic_back_white);
        } else {
            initToolBar(mToolbar, "注册设备", R.drawable.gank_ic_back_night);
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
       if (type.equals("register_bind")){
           Intent intent=new Intent(UnitDevRegisterActivity.this, UnitMainActivity.class);
           intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
           startActivity(intent);
       }else if (type.equals("update_bind")){
           Intent intent=new Intent(UnitDevRegisterActivity.this, UnitMainActivity.class);
           intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
           startActivity(intent);
       }else if (type.equals("room_list")){
           tv_site_name.setText(mPresenter.roomModel.getPlace_name());
       }
    }

    @Override
    public void successToJump(int errorCode,String msg) {
        String devNo = AppValidationMgr.removeStringSpace(etDevnum.getText().toString(), 0);
        if(errorCode == DevConstants.DEV_UNREGIST){//设备未注册
            registerAndBindDev();
        }else if(errorCode == DevConstants.DEV_REGISTED_HAS_MASTER){//设备注册，且有主绑人,但未关注,跳转关注设备页面
            goToDevAttention(devNo);
        }else if(errorCode == DevConstants.DEV_REGISTED_NO_MASTER){//设备注册，未有主绑人
            updateAndBindDev();
        }else if(errorCode == DevConstants.DEV_REGISTED_HAS_BIND){//设备已经绑定，跳转设备详情页面
            goToDevDetail(devNo);
        }else{
            MyToast.showShortToast(msg);
        }
    }
    private void goToDevAttention(String devNo){/**跳转关注设备页面***/
        Intent intent = new Intent(UnitDevRegisterActivity.this, UnitDevBindActivity.class);
        intent.putExtra(UnitDevBindActivity.DEV_ID,devNo);
        startActivity(intent);
    }

    private void goToDevDetail(String devNo) {/**跳转设备详情页面***/
        Intent intent=new Intent(this, DevDetailGasActivity.class);
        intent.putExtra("DevNumDetail",devNo);
        startActivity(intent);
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
                clickAreaName();
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
    private void clickAreaName(){
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
    void clickRegister() {
        String devNo = AppValidationMgr.removeStringSpace(etDevnum.getText().toString(), 0);/***去除空格***/
        if (TextUtils.isEmpty(devNo)) {
            MyToast.showShortToast("请输入设备编号");
            return;
        }
        if (joinStatus == 1) {
            if (devNo.contains(publicModel.getPlace_dictionary_id())) {
            } else {
                MyToast.showShortToast("亲，您的探测器型号对不上哦，请检查输入");
                return;
            }
        }
        if (TextUtils.isEmpty(et_dev_name.getText().toString())) {
            MyToast.showShortToast("请输入设备名");
            return;
        }
        if (et_dev_name.getText().toString().length() > 20) {
            MyToast.showShortToast("设备名称最多可20位");
            return;
        }
        if (pickerAddressModel == null) {
            MyToast.showShortToast("请选择所在地区");
            return;
        }
        if (TextUtils.isEmpty(etAddr.getText().toString())) {
            MyToast.showShortToast("请输入详细地址");
            return;
        }
        if (etAddr.getText().toString().length() > 100) {
            MyToast.showShortToast("详细地址最多可100位");
            return;
        }
        if (mPresenter.roomModel == null) {
            MyToast.showShortToast("请给设备选择安装场所");
            return;
        }
        if (regist_status == DevConstants.DEV_REGISTED_NO_MASTER) {/**修改设备并绑定**/
            updateAndBindDev();
        } else {
            mPresenter.isDevReg_V902(devNo);
        }
    }

}