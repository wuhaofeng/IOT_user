package com.iot.user.ui.model.alert;

import java.io.Serializable;

public class AlertInfo implements Serializable {

    private String title;
    private String devNum;
    private String area_Name_1;
    private String area_Name_2;
    private String area_Name_3;
    private String area_Name_4;
    private String address;
    private Double lon;
    private Double lat;
    //设置类型
    private int devType;
    //设备名称
    private String devName;
    //故障、报警时间
    private long at;
    //用户名
    private String username;
    private String nickname;
    //手机号
    private String mobile;
    //设备状态 0  正常 1 故障 2 报警 3
    private int devStat;

    //报警代码
    private int alarm_num;

    //故障代码
    private int fault_num;

    private String msgnum;

    public AlertInfo() {
    }

    public AlertInfo(String title, String devNum,
                     String area_Name_1, String area_Name_2,
                     String area_Name_3, String area_Name_4,
                     String address, Double lon, Double lat,
                     int devType, String devName, long at,
                     String username,String nickname, String mobile,
                     int devStat, int alarm_num, int fault_num,String msgnum) {
        this.title = title;
        this.devNum = devNum;
        this.area_Name_1 = area_Name_1;
        this.area_Name_2 = area_Name_2;
        this.area_Name_3 = area_Name_3;
        this.area_Name_4 = area_Name_4;
        this.address = address;
        this.lon = lon;
        this.lat = lat;
        this.devType = devType;
        this.devName = devName;
        this.at = at;
        this.username = username;
        this.nickname=nickname;
        this.mobile = mobile;
        this.devStat = devStat;
        this.alarm_num = alarm_num;
        this.fault_num = fault_num;
        this.msgnum=msgnum;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDevNum() {
        return devNum;
    }

    public void setDevNum(String devNum) {
        this.devNum = devNum;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public int getDevType() {
        return devType;
    }

    public void setDevType(int devType) {
        this.devType = devType;
    }

    public String getDevName() {
        return devName;
    }

    public void setDevName(String devName) {
        this.devName = devName;
    }

    public long getAt() {
        return at;
    }

    public void setAt(long at) {
        this.at = at;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getDevStat() {
        return devStat;
    }

    public void setDevStat(int devStat) {
        this.devStat = devStat;
    }

    public int getAlarm_num() {
        return alarm_num;
    }

    public void setAlarm_num(int alarm_num) {
        this.alarm_num = alarm_num;
    }

    public int getFault_num() {
        return fault_num;
    }

    public void setFault_num(int fault_num) {
        this.fault_num = fault_num;
    }

    public String getMsgnum() {
        return msgnum;
    }

    public void setMsgnum(String msgnum) {
        this.msgnum = msgnum;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
