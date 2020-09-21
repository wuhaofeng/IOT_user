package com.iot.user.cluster.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.iot.user.R;
import com.iot.user.cluster.clustering.ClusterItem;

import java.util.ArrayList;
import java.util.List;



public class MarkerInfo implements Parcelable , ClusterItem {

//    private String markerId;
    private double markerLat;
    private double markerLon;
    private String markerIcon;
//    private String markerName;
    /**
     * 设备id
     */
    private String deviceId;
    /**
     * 设备类型
     */
    private int deviceType;
    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 设备昵称
     */
    private String devNickname;
    /**
     * 用户名
     */
    private String username;
    /**
     * 用户联系电话
     */
    private String tel;

    /**
     * 用户通讯地址
     */
    private String address;

    /**
     * 设备状态
     */
    private int state;

    private int doType;

    /**
     * 设备注册时间
     */
    private long time;

    private String remark;

    public double getMarkerLat() {
        return markerLat;
    }

    public void setMarkerLat(double markerLat) {
        this.markerLat = markerLat;
    }

    public double getMarkerLon() {
        return markerLon;
    }

    public void setMarkerLon(double markerLon) {
        this.markerLon = markerLon;
    }

    public String getMarkerIcon() {
        return markerIcon;
    }

    public void setMarkerIcon(String markerIcon) {
        this.markerIcon = markerIcon;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getDoType() {
        return doType;
    }

    public void setDoType(int doType) {
        this.doType = doType;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public MarkerInfo() {
    }

    public MarkerInfo(double markerLat, double markerLon,
                      String markerIcon, String deviceId,
                      int deviceType, String deviceName,
                      String username, String tel,
                      String address, int state,
                      int doType, long time, String remark,String devNickname) {
        this.markerLat = markerLat;
        this.markerLon = markerLon;
        this.markerIcon = markerIcon;
        this.deviceId = deviceId;
        this.deviceType = deviceType;
        this.deviceName = deviceName;
        this.username = username;
        this.tel = tel;
        this.address = address;
        this.state = state;
        this.doType = doType;
        this.time = time;
        this.remark = remark;
        this.devNickname = devNickname;
    }

    protected MarkerInfo(Parcel in) {
        markerLat = in.readDouble();
        markerLon = in.readDouble();
        markerIcon = in.readString();

        deviceId = in.readString();
        deviceType = in.readInt();
        deviceName = in.readString();
        username = in.readString();
        tel = in.readString();
        address = in.readString();
        state = in.readInt();
        doType = in.readInt();
        time = in.readLong();
        remark = in.readString();
        devNickname = in.readString();
    }

    public String getDevNickname() {
        return devNickname;
    }

    public void setDevNickname(String devNickname) {
        this.devNickname = devNickname;
    }

    @Override
    public String toString() {
        return "-----------经度："+markerLon+"维度："+markerLat+"设备id"+deviceId+"tel："+tel+"地址："+address+"-----------";
    }

    public static final Creator<MarkerInfo> CREATOR = new Creator<MarkerInfo>() {
        @Override
        public MarkerInfo createFromParcel(Parcel in) {
            return new MarkerInfo(in);
        }

        @Override
        public MarkerInfo[] newArray(int size) {
            return new MarkerInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(markerLat);
        dest.writeDouble(markerLon);
        dest.writeString(markerIcon);

        dest.writeString(deviceId);
        dest.writeInt(deviceType);
        dest.writeString(deviceName);
        dest.writeString(username);
        dest.writeString(tel);
        dest.writeString(address);
        dest.writeInt(state);
        dest.writeInt(doType);
        dest.writeLong(time);
        dest.writeString(remark);
        dest.writeString(devNickname);
    }

    @Override
    public LatLng getPosition() {
        return new LatLng(markerLat, markerLon, false);
    }

    @Override
    public BitmapDescriptor getBitmapDescriptor() {
        int markerIconResource=-1;
        float markerIconDefault=0.0F;
        int drawableId = R.drawable.icon_openmap_mark;
        if(deviceType == 101){
            drawableId = R.drawable.d101;
        }else if(deviceType == 102){
            drawableId = R.drawable.d102;
        }else if(deviceType == 181){
            drawableId = R.drawable.d181;
        }else if(deviceType == 182){
            drawableId = R.drawable.d181;
//            drawableId = R.drawable.d182;
        }else if(deviceType == 201){
            drawableId = R.drawable.d201;
        }else{
            drawableId=R.drawable.d192;
        }
//        if(state == 1){//warn
//            drawableId = R.drawable.gps_warn;
//        }else if(state == 2){//alert
//            drawableId = R.drawable.gps_alert;
//        }
        return BitmapDescriptorFactory
                .fromResource(drawableId);

       /* if (markerIcon != null) {
               *//* try {
                    return BitmapDescriptorFactory.fromBitmap(Ion.with(LocationActivity.this)
                            .load(markerIconUrl).asBitmap().get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }*//*

//            return BitmapDescriptorFactory
//                    .defaultMarker(markerIconDefault);
        } else if (markerIconResource != -1) {
            return BitmapDescriptorFactory
                    .fromResource(markerIconResource);
        }
        return BitmapDescriptorFactory
                .defaultMarker(markerIconDefault);
        */
    }

    @Override
    public String getTitle() {
        return getDeviceName();
    }

    @Override
    public String getSnippet() {
        return "";
    }

    @Override
    public int getDevState(){
        return state;
    }
}
