package com.iot.user.ui.activity.location;
import android.Manifest;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Interpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.maps.model.animation.TranslateAnimation;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.google.gson.Gson;
import com.iot.user.R;
import com.iot.user.base.BaseActivity;
import com.iot.user.databinding.ActivityLocationBinding;
import com.iot.user.ui.adapter.location.SearchResultAdapter;
import com.iot.user.ui.model.location.MapBean;
import com.iot.user.utils.DialogUtils;
import com.iot.user.utils.KeyboardUtils;
import com.iot.user.utils.LocationServiceUtil;
import com.iot.user.utils.LocationUtils;
import com.iot.user.utils.PXTransUtils;
import com.iot.user.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class LocationActivity extends BaseActivity<ActivityLocationBinding> implements LocationSource,
        AMapLocationListener, GeocodeSearch.OnGeocodeSearchListener, PoiSearch.OnPoiSearchListener , EasyPermissions.PermissionCallbacks{
    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_location;
    }
    Toolbar toolbar;
    ListView listView;
    private List<PoiItem> resultData=new ArrayList<>();
    private SearchResultAdapter searchResultAdapter;
    MapView mapView;
    private AMap aMap;
    private boolean isItemClickAction;
    /****逆地理编码***/
    private GeocodeSearch geocoderSearch;
    /****定位相关**/
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private OnLocationChangedListener mListener;
    /****POI搜索**/
    private List<PoiItem> poiItems;// poi数据
    private PoiItem firstItem;
    private int currentPage = 0;// 当前页面，从0开始计数
    private PoiSearch.Query query;// Poi查询条件类
    private PoiSearch poiSearch;
    private String searchKey = "";
    /***地图大头针***/
    private Marker locationMarker;
/***点击右上角确认按钮的数据处理**/
    private String city;
    private String division;//行政区域
    private String addressDetail;//详细地址
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        mapView.onCreate(savedInstanceState);
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
    @Override
    public void initView() {
        toolbar=(Toolbar) dataBinding.toolbar;
        listView=dataBinding.listview;
        mapView=dataBinding.map;
        searchText=dataBinding.keyWord;
        initMyToolBar();
        methodRequiresTwoPermission();
        initAmap();
        initViews();
    }

    private void initViews() {
        searchResultAdapter = new SearchResultAdapter(LocationActivity.this);
        listView.setAdapter(searchResultAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (position != searchResultAdapter.getSelectedPosition()) {
                    PoiItem poiItem = (PoiItem) searchResultAdapter.getItem(position);
                    LatLng curLatlng = new LatLng(poiItem.getLatLonPoint().getLatitude(), poiItem.getLatLonPoint().getLongitude());
                    addressDetail = poiItem.getSnippet()+"("+poiItem.getTitle()+")";
                    isItemClickAction = true;
                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(curLatlng, 16f));
                    searchResultAdapter.setSelectedPosition(position);
                    searchResultAdapter.notifyDataSetChanged();
                }
            }
        });
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String newText = s.toString().trim();
                if (newText.length() > 0) {
                    InputtipsQuery inputquery = new InputtipsQuery(newText, "中国");
                    Inputtips inputTips = new Inputtips(LocationActivity.this, inputquery);
                    inputquery.setCityLimit(true);
                    inputTips.setInputtipsListener(inputtipsListener);
                    inputTips.requestInputtipsAsyn();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        searchText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("MY", "setOnItemClickListener");
                if (autoTips != null && autoTips.size() > position) {
                    Tip tip = autoTips.get(position);
                    searchPoi(tip);
                }
            }
        });
        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);
        KeyboardUtils.hideSoftInput(this);/***隐藏键盘***/
    }
    /**
     * 初始化
     */
    private void initAmap() {
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }
        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {/***地图缩放和移动的监听***/
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
            }
            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
                if (!isItemClickAction && !isInputKeySearch) {
                    geoAddress();
                    startJumpAnimation();
                }
                searchLatlonPoint = new LatLonPoint(cameraPosition.target.latitude, cameraPosition.target.longitude);
                isInputKeySearch = false;
                isItemClickAction = false;
            }
        });

        aMap.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
            @Override
            public void onMapLoaded() {
                addMarkerInScreenCenter(null);
            }
        });
    }
    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
    }


/****搜索地址**/
    private AutoCompleteTextView searchText;
    private boolean isInputKeySearch;
    private String inputSearchKey;
    private LatLonPoint searchLatlonPoint;
    private void searchPoi(Tip result) {
        isInputKeySearch = true;
        inputSearchKey = result.getName();//getAddress(); // + result.getRegeocodeAddress().getCity() + result.getRegeocodeAddress().getDistrict() + result.getRegeocodeAddress().getTownship();
        searchLatlonPoint = result.getPoint();
        firstItem = new PoiItem("tip", searchLatlonPoint, inputSearchKey, result.getAddress());
        firstItem.setCityName(result.getDistrict());
        firstItem.setAdName("");
        resultData.clear();
        searchResultAdapter.setSelectedPosition(0);
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(searchLatlonPoint.getLatitude(), searchLatlonPoint.getLongitude()), 16f));
        KeyboardUtils.hideSoftInput(this);
        doSearchQuery();
    }
/****搜索地址的反馈***/
    private List<Tip> autoTips;
    private boolean isfirstinput = true;
    Inputtips.InputtipsListener inputtipsListener = new Inputtips.InputtipsListener() {
        @Override
        public void onGetInputtips(List<Tip> list, int rCode) {
            if (rCode == AMapException.CODE_AMAP_SUCCESS) {// 正确返回
                autoTips = list;
                List<String> listString = new ArrayList<String>();
                for (int i = 0; i < list.size(); i++) {
                    listString.add(list.get(i).getName());
                }
                ArrayAdapter<String> aAdapter = new ArrayAdapter<String>(
                        getApplicationContext(),
                        R.layout.route_inputs_location, listString);
                searchText.setAdapter(aAdapter);
                aAdapter.notifyDataSetChanged();
                if (isfirstinput) {
                    isfirstinput = false;
                    searchText.showDropDown();
                }
            } else {
                Toast.makeText(LocationActivity.this, "erroCode " + rCode , Toast.LENGTH_SHORT).show();
            }
        }
    };



    private void initMyToolBar() {
        initToolBar(toolbar, "位置", R.drawable.gank_ic_back_white);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_location, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.item_confirm://确认位置
                if (null != poiItems && 0 < poiItems.size() && null != searchResultAdapter) {
                    int position = searchResultAdapter.getSelectedPosition();
                    if (position < 0) {
                        position = 0;
                    } else if (position > poiItems.size()) {
                        position = poiItems.size();
                    }
                    PoiItem poiItem = poiItems.get(position);
                    MapBean mapBean = new MapBean();
                    mapBean.setLatlon(poiItem.getLatLonPoint().getLatitude()+","+poiItem.getLatLonPoint().getLongitude());
                    mapBean.setAddr(poiItem.getSnippet()+poiItem.getTitle());
                    if(poiItem.getProvinceName().equals(poiItem.getCityName())){
                        mapBean.setArea(poiItem.getCityName()+"/"+poiItem.getAdName());
                    }else{
                        mapBean.setArea(poiItem.getProvinceName()+"/"+poiItem.getCityName()+"/"+poiItem.getAdName());
                    }
                    Intent intent = new Intent(getApplicationContext(), MapBean.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("mapBean",mapBean);
                    bundle.putString("division",division);
                    bundle.putString("addressDetail",addressDetail);
                    intent.putExtras(bundle);
                    setResult(100, intent);
//                    Toast.makeText(LocationActivity.this, "发送：" + poiItem.getTitle() + "  " + poiItem.getSnippet() + "  " + "纬度：" + poiItem.getLatLonPoint().getLatitude() + "  " + "经度：" + poiItem.getLatLonPoint().getLongitude(), Toast.LENGTH_SHORT).show();

                    LocationActivity.this.finish();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        aMap = null;
        initAmap();
        initView();
        if (!LocationUtils.isLocServiceEnable(LocationActivity.this)){
            DialogUtils.showMyDialog(LocationActivity.this,
                    "提示", "不能确定您的位置，您可以在设置中打开GPS", "去设置", "取消",
                    new DialogUtils.OnDialogClickListener() {
                        @Override
                        public void onConfirm() {
                            LocationServiceUtil.setLocationServiceStatus(LocationActivity.this);
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        deactivate();
    }
    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        //ButterKnife.unbind(this);
        if(null != mlocationClient){
            mlocationClient.onDestroy();
        }
    }

    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
//        Log.i("MY", "onLocationChanged");
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);
                Gson gson = new Gson();
                city =  gson.toJson(amapLocation);
                LatLng curLatlng = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());

                searchLatlonPoint = new LatLonPoint(curLatlng.latitude, curLatlng.longitude);

                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(curLatlng, 16f));

                isInputKeySearch = false;

                searchText.setText("");

            } else {
                String errText = "定位失败," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr",errText);
            }
        }
    }
    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setOnceLocation(true);
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }
    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }
    /**
     * 响应逆地理编码
     */
    public void geoAddress() {
//        Log.i("MY", "geoAddress"+ searchLatlonPoint.toString());
        searchText.setText("");
        if (searchLatlonPoint != null){
            RegeocodeQuery query = new RegeocodeQuery(searchLatlonPoint, 3000, GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
            geocoderSearch.getFromLocationAsyn(query);
        }
    }
    /**
     * 开始进行poi搜索
     */
    protected void doSearchQuery() {
//        Log.i("MY", "doSearchQuery");
        currentPage = 0;
        query = new PoiSearch.Query(searchKey, "", "");// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setCityLimit(true);
        query.setPageSize(20);
        query.setPageNum(currentPage);

        if (searchLatlonPoint != null) {
            poiSearch = new PoiSearch(this, query);
            poiSearch.setOnPoiSearchListener(this);
            poiSearch.setBound(new PoiSearch.SearchBound(searchLatlonPoint, 3000, true));//
            poiSearch.searchPOIAsyn();
        }
    }


    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {/***逆地理编码回调**/
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getRegeocodeAddress() != null
                    && result.getRegeocodeAddress().getFormatAddress() != null) {
                String address = result.getRegeocodeAddress().getProvince() + result.getRegeocodeAddress().getCity() + result.getRegeocodeAddress().getDistrict() + result.getRegeocodeAddress().getTownship();
                firstItem = new PoiItem("regeo", searchLatlonPoint, address, address);
                addressDetail = result.getRegeocodeAddress().getFormatAddress();//详细地址
                division =result.getRegeocodeAddress().getProvince()+"/"+ result.getRegeocodeAddress().getCity() +"/"+result.getRegeocodeAddress().getDistrict()+"/"+result.getRegeocodeAddress().getTownship();//行政区域
                doSearchQuery();
            }
        } else {
            Toast.makeText(LocationActivity.this, "error code is " + rCode, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {/*****地理编码回调**/

    }
    /**
     * POI信息查询回调方法
     */
    @Override
    public void onPoiSearched(PoiResult poiResult, int resultCode) {
        if (resultCode == AMapException.CODE_AMAP_SUCCESS) {
            if (poiResult != null && poiResult.getQuery() != null) {
                if (poiResult.getQuery().equals(query)) {
                    poiItems = poiResult.getPois();
                    if (poiItems != null && poiItems.size() > 0) {
                        updateListview(poiItems);
                    } else {
                        Toast.makeText(LocationActivity.this, "无搜索结果", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                Toast.makeText(LocationActivity.this, "无搜索结果", Toast.LENGTH_SHORT).show();
            }
        }
    }
    /**
     * 更新列表中的item
     * @param poiItems
     */
    private void updateListview(List<PoiItem> poiItems) {
        resultData.clear();
        searchResultAdapter.setSelectedPosition(0);
//        resultData.add(firstItem);
        resultData.addAll(poiItems);
        searchResultAdapter.setData(resultData);
        searchResultAdapter.notifyDataSetChanged();
    }
    /**
     * POI信息查询回调方法
     */
    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {
    }
    /**给地图插入大头针Marker***/
    private void addMarkerInScreenCenter(LatLng locationLatLng) {
        LatLng latLng = aMap.getCameraPosition().target;
        Point screenPosition = aMap.getProjection().toScreenLocation(latLng);
        locationMarker = aMap.addMarker(new MarkerOptions()
                .anchor(0.5f,0.5f)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.purple_pin)));
        //设置Marker在屏幕上,不跟随地图移动
        locationMarker.setPositionByPixels(screenPosition.x,screenPosition.y);
        locationMarker.setZIndex(1);
    }
    /**
     * 屏幕中心marker 跳动
     */
    public void startJumpAnimation() {

        if (locationMarker != null ) {
            //根据屏幕距离计算需要移动的目标点
            final LatLng latLng = locationMarker.getPosition();
            Point point =  aMap.getProjection().toScreenLocation(latLng);
            point.y -= PXTransUtils.dp2px(this,125);
            LatLng target = aMap.getProjection()
                    .fromScreenLocation(point);
            //使用TranslateAnimation,填写一个需要移动的目标点
            Animation animation = new TranslateAnimation(target);
            animation.setInterpolator(new Interpolator() {
                @Override
                public float getInterpolation(float input) {
                    // 模拟重加速度的interpolator
                    if(input <= 0.5) {
                        return (float) (0.5f - 2 * (0.5 - input) * (0.5 - input));
                    } else {
                        return (float) (0.5f - Math.sqrt((input - 0.5f)*(1.5f - input)));
                    }
                }
            });
            //整个移动所需要的时间
            animation.setDuration(600);
            //设置动画
            locationMarker.setAnimation(animation);
            //开始动画
            locationMarker.startAnimation();

        } else {
            Log.e("ama","screenMarker is null");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {/***当从软件设置界面，返回当前程序时候***/
            case AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE:
                mapView.onResume();
                aMap = null;
                initAmap();
                initView();
//             methodRequiresTwoPermission();
                //执行Toast显示或者其他逻辑处理操作
                break;
        }
    }

    /**定位权限申请*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
    private void methodRequiresTwoPermission() {
        String[] perms = {Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION};/**Manifest.permission.ACCESS_FINE_LOCATION**/
        if (EasyPermissions.hasPermissions(this, perms)) {
//            ToastUtil.showMessage("已经授权");
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, "应用时需要定位权限",
                    100, perms);
        }
    }
    /**
     * 请求权限成功。
     * 可以弹窗显示结果，也可执行具体需要的逻辑操作
     */
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        ToastUtil.showMessage("用户授权成功");
        mapView.onResume();
        aMap = null;
        initAmap();
        initView();
    }
    /**
     * 请求权限失败
     */
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
//        ToastUtil.showMessage("用户授权失败");
        /**
         　　* 若是在权限弹窗中，用户勾选了'NEVER ASK AGAIN.'或者'不在提示'，且拒绝权限。
         　　* 这时候，需要跳转到设置界面去，让用户手动开启。
         　　*/
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            DialogUtils.showMyDialog(LocationActivity.this,
                    "提示", "不能确定您的位置，您可以在设置中打开GPS", "去设置", "取消",
                    new DialogUtils.OnDialogClickListener() {
                        @Override
                        public void onConfirm() {
                            LocationServiceUtil.setLocationServiceStatus(LocationActivity.this);
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
        }
    }
}
