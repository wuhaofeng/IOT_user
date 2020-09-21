package com.iot.user.ui.model.main;

public class UnitFamilyDevStatusModel {
    public  UnitFamilyDevStatusModel(){

    }
    private String alarm;//1位绑定 2 为关注
    private String fault;
    private String online;//1管理员 3成员
    private String offline;

    public String getAlarm() {
        return alarm;
    }

    public void setAlarm(String alarm) {
        this.alarm = alarm;
    }

    public String getFault() {
        return fault;
    }

    public void setFault(String fault) {
        this.fault = fault;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    public String getOffline() {
        return offline;
    }

    public void setOffline(String offline) {
        this.offline = offline;
    }
}
