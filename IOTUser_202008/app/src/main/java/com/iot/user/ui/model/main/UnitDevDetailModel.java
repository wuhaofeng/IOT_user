package com.iot.user.ui.model.main;

public class UnitDevDetailModel {
    public UnitDevDetailModel(){

    }
    public String address;
    public String area_Name_1;
    public String area_Name_2;
    public String area_Name_3;
    public String area_Name_4;
    public String name;//设备名称
    public String friend_name;//设备友好名
    public String devNum;
    public String family_place_id;//房间
    public String place_name;

    public String lat;
    public String lon;
    public String nickname;//绑定人
    public String phone;

    public String at;//设备告警上报时间
    public String loginDataTime;//设备上线时间
    public String reg_time;//设备注册时间
    public String updateDataTime;//设备上线更新时间
    public String expireTime;//设备失效时间

    public String bind_type;//1 绑定者  2 关注者（String
    public String devType;//192，182
    public String stat;//报警器的状态0 表示正常 1表示故障 2 表示报警  3表示复合故障报警（Integer）
    public String status;//0(在线)  1（下线）
    public String mute;//默认0 1我知道 2已消音 3=1+2

    public String station;//基站编号
    public String csq;//信号强度
    public String gasvalue;//燃气值
    public String hum;//湿度
    public String tem;//温度

    public String msgNum;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getArea_Name_1() {
        return area_Name_1;
    }

    public void setArea_Name_1(String area_Name_1) {
        this.area_Name_1 = area_Name_1;
    }

    public String getArea_Name_2() {
        return area_Name_2;
    }

    public void setArea_Name_2(String area_Name_2) {
        this.area_Name_2 = area_Name_2;
    }

    public String getArea_Name_3() {
        return area_Name_3;
    }

    public void setArea_Name_3(String area_Name_3) {
        this.area_Name_3 = area_Name_3;
    }

    public String getArea_Name_4() {
        return area_Name_4;
    }

    public void setArea_Name_4(String area_Name_4) {
        this.area_Name_4 = area_Name_4;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFriend_name() {
        return friend_name;
    }

    public void setFriend_name(String friend_name) {
        this.friend_name = friend_name;
    }

    public String getDevNum() {
        return devNum;
    }

    public void setDevNum(String devNum) {
        this.devNum = devNum;
    }

    public String getFamily_place_id() {
        return family_place_id;
    }

    public void setFamily_place_id(String family_place_id) {
        this.family_place_id = family_place_id;
    }

    public String getPlace_name() {
        return place_name;
    }

    public void setPlace_name(String place_name) {
        this.place_name = place_name;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAt() {
        return at;
    }

    public void setAt(String at) {
        this.at = at;
    }

    public String getLoginDataTime() {
        return loginDataTime;
    }

    public void setLoginDataTime(String loginDataTime) {
        this.loginDataTime = loginDataTime;
    }

    public String getReg_time() {
        return reg_time;
    }

    public void setReg_time(String reg_time) {
        this.reg_time = reg_time;
    }

    public String getUpdateDataTime() {
        return updateDataTime;
    }

    public void setUpdateDataTime(String updateDataTime) {
        this.updateDataTime = updateDataTime;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    public String getBind_type() {
        return bind_type;
    }

    public void setBind_type(String bind_type) {
        this.bind_type = bind_type;
    }

    public String getDevType() {
        return devType;
    }

    public void setDevType(String devType) {
        this.devType = devType;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMute() {
        return mute;
    }

    public void setMute(String mute) {
        this.mute = mute;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getCsq() {
        return csq;
    }

    public void setCsq(String csq) {
        this.csq = csq;
    }

    public String getGasvalue() {
        return gasvalue;
    }

    public void setGasvalue(String gasvalue) {
        this.gasvalue = gasvalue;
    }

    public String getHum() {
        return hum;
    }

    public void setHum(String hum) {
        this.hum = hum;
    }

    public String getTem() {
        return tem;
    }

    public void setTem(String tem) {
        this.tem = tem;
    }

    public String getMsgNum() {
        return msgNum;
    }

    public void setMsgNum(String msgNum) {
        this.msgNum = msgNum;
    }
}

