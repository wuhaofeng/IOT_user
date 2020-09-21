package com.iot.user.ui.view.location;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.iot.user.http.NetWorkApi;
import com.iot.user.http.net.RxUtils;
import com.iot.user.ui.model.location.District;
import com.iot.user.ui.model.location.GaoDeGetRegionAreaJsonResp;
import com.iot.user.ui.model.location.PickerAddressModel;
import com.ymht.library.picker.address.AddressPicker;
import com.ymht.library.picker.address.OnAddressPickerListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.observers.DisposableObserver;

public class AddressPickerUtil {
    private AddressPicker addressPicker;
    private Context context;
    private ArrayList<String> provinces = new ArrayList<>();
    private ArrayList<String> cities = new ArrayList<>();
    private ArrayList<String> districtList = new ArrayList<>();
    private ArrayList<String> streets = new ArrayList<>();
    public AddressPickerUtil(){

    }
    public AddressPickerUtil(AddressPicker addressPicker){
        this.addressPicker=addressPicker;
        initPicker();
    }
    public AddressPickerUtil(AddressPicker addressPicker,Context context){
        this.addressPicker=addressPicker;
        this.context=context;
        initPicker();
    }
    private void initPicker(){
        addressPicker.setOnAddressPickerListener(new OnAddressPickerListener() {
            @Override
            public void onProvinceStart() {
                NetWorkApi.provideRepositoryData().queryRegionArea("中国")
                        .compose(RxUtils.schedulersTransformer()) //线程调度
                        .subscribe(new DisposableObserver<GaoDeGetRegionAreaJsonResp>() {
                            @Override
                            public void onNext(GaoDeGetRegionAreaJsonResp result) {
                                if (result.getStatus().equals("1")) {
                                    provinces.clear();
                                    if (result != null) {
                                        GaoDeGetRegionAreaJsonResp resp = (GaoDeGetRegionAreaJsonResp) result;
                                        List<District> districts = resp.getDistricts();
                                        if (districts != null && districts.size() > 0) {
                                            List<District> subdistricts = districts.get(0).getDistricts();
                                            if (subdistricts != null) {
                                                for (int i = 0; i < subdistricts.size(); i++) {
                                                    if ("上海市".equals(subdistricts.get(i).getName()) || "北京市".equals(subdistricts.get(i).getName())) {
                                                        provinces.add(0, subdistricts.get(i).getName());
                                                    } else {
                                                        provinces.add(subdistricts.get(i).getName());
                                                    }

                                                }
                                            }
                                        }
                                        addressPicker.provinceSuccess(provinces);
                                    }
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
            @Override
            public void onCityStart(int provincePosition) {
                String province = provinces.get(provincePosition);
                NetWorkApi.provideRepositoryData().queryRegionArea(province)
                        .compose(RxUtils.schedulersTransformer()) //线程调度
                        .subscribe(new DisposableObserver<GaoDeGetRegionAreaJsonResp>() {
                            @Override
                            public void onNext(GaoDeGetRegionAreaJsonResp result) {
                                if (result.getStatus().equals("1")) {
                                    cities.clear();
                                    if(result!=null){
                                        GaoDeGetRegionAreaJsonResp resp = (GaoDeGetRegionAreaJsonResp)result;
                                        List<District> districts = resp.getDistricts();
                                        if(districts!=null && districts.size()>0){
                                            List<District> subdistricts = districts.get(0).getDistricts();
                                            if(subdistricts != null){
                                                for(int i = 0;i<subdistricts.size();i++){
                                                    cities.add(subdistricts.get(i).getName());
                                                }
                                            }
                                        }
                                        addressPicker.citySuccess(cities);
                                    }
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
            @Override
            public void onDistrictStart(int cityPosition) {
                String city = cities.get(cityPosition);
                NetWorkApi.provideRepositoryData().queryRegionArea(city)
                        .compose(RxUtils.schedulersTransformer()) //线程调度
                        .subscribe(new DisposableObserver<GaoDeGetRegionAreaJsonResp>() {
                            @Override
                            public void onNext(GaoDeGetRegionAreaJsonResp result) {
                                if (result.getStatus().equals("1")) {
                                    districtList.clear();
                                    if(result!=null){
                                        GaoDeGetRegionAreaJsonResp resp = (GaoDeGetRegionAreaJsonResp)result;
                                        List<District> districts = resp.getDistricts();
                                        if(districts!=null && districts.size()>0){
                                            List<District> subdistricts = districts.get(0).getDistricts();
                                            if(subdistricts != null){
                                                for(int i = 0;i<subdistricts.size();i++){
                                                    districtList.add(subdistricts.get(i).getName());
                                                }
                                            }
                                        }
                                        addressPicker.districtSuccess(districtList);
                                    }
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
            @Override
            public void onStreetStart(int districtPosition) {
                String street = districtList.get(districtPosition);
                NetWorkApi.provideRepositoryData().queryRegionArea(street)
                        .compose(RxUtils.schedulersTransformer()) //线程调度
                        .subscribe(new DisposableObserver<GaoDeGetRegionAreaJsonResp>() {
                            @Override
                            public void onNext(GaoDeGetRegionAreaJsonResp result) {
                                if (result.getStatus().equals("1")) {
                                    streets.clear();
                                    if(result!=null){
                                        GaoDeGetRegionAreaJsonResp resp = (GaoDeGetRegionAreaJsonResp)result;
                                        List<District> districts = resp.getDistricts();
                                        if(districts!=null && districts.size()>0){
                                            List<District> subdistricts = districts.get(0).getDistricts();
                                            if(subdistricts != null){
                                                for(int i = 0;i<subdistricts.size();i++){
                                                    streets.add(subdistricts.get(i).getName());
                                                }
                                            }
                                        }
                                        addressPicker.streetSuccess(streets);
                                    }
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
            @Override
            public void onEnsure(int streetPosition, String province, String city, String district, String street) {
                String region = "";
                String regionStr="";
                if (province.equals("上海市") || province.equals("北京市") || province.equals("重庆市") || province.equals("天津市")){
                    region = province+"/"+province+"/"+district+"/"+street;
                    regionStr=province+province+district+street;
                }else{
                    region = province+"/"+city+"/"+district+"/"+street;
                    regionStr=province+city+district+street;
                }
                if (context!=null) {
                    Address addressData = getGeoPointBystr(context,regionStr);
                    PickerAddressModel addressModel=new PickerAddressModel(province,city,district,street,0.0,0.0,region,"");
                    if (addressData!=null) {
                        addressModel.setLat(addressData.getLatitude());
                        addressModel.setLon(addressData.getLatitude());
                    }
                    if (onPickerLocationListener!=null){
                        onPickerLocationListener.onPickerStringAndLocation(addressModel);
                    }
                }

                if (onPickerListener!=null){
                    onPickerListener.onPickerString(region);
                }
            }
        });
    }

    /***只返回字符串**/
    public interface OnPickerListener{
        void onPickerString(String addressInfo);
    }
    private OnPickerListener onPickerListener;
    public void setOnPickerListener(OnPickerListener onPickerListener) {
        this.onPickerListener = onPickerListener;
    }
    /***返回所有的location数据**/
    public interface OnPickerLocationListener{
        void onPickerStringAndLocation(PickerAddressModel addressModel);
    }
    private OnPickerLocationListener onPickerLocationListener;

    public void setOnPickerLocationListener(OnPickerLocationListener onPickerLocationListener) {
        this.onPickerLocationListener = onPickerLocationListener;
    }

    /***地理编码**/
    public static Address getGeoPointBystr(Context context, String str) {
        Address address_temp = null;
        if (str != null) {
            Geocoder gc = new Geocoder(context, Locale.CHINA);
            List<Address> addressList = null;
            try {
                addressList = gc.getFromLocationName(str, 1);
                if (!addressList.isEmpty()) {
                    address_temp = addressList.get(0);
                    double Latitude = address_temp.getLatitude();
                    double Longitude = address_temp.getLongitude();
                    Log.d("zxc003", str + " Latitude = " + Latitude + " Longitude = " + Longitude);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return address_temp;
    }
}
