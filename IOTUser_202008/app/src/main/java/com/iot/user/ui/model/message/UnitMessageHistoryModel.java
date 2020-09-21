package com.iot.user.ui.model.message;

import java.io.Serializable;

public class UnitMessageHistoryModel implements Serializable {
    public  UnitMessageHistoryModel(){

    }
    private String at;
    private String csq;
    private String gasValue;//设备编号
    private String hum;//信息描述
    private String imei;//信息描述
    private String nickName;//上报时毫秒级时间戳
    private String status;
    private String station;//信息描述
    private String temp;//信息描述
    private String devType;

    public String getAt() {
        return at;
    }

    public void setAt(String at) {
        this.at = at;
    }

    public String getCsq() {
        return csq;
    }

    public void setCsq(String csq) {
        this.csq = csq;
    }

    public String getGasValue() {
        return gasValue;
    }

    public void setGasValue(String gasValue) {
        this.gasValue = gasValue;
    }

    public String getHum() {
        return hum;
    }

    public void setHum(String hum) {
        this.hum = hum;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getDevType() {
        return devType;
    }

    public void setDevType(String devType) {
        this.devType = devType;
    }
}
