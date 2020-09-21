package com.iot.user.ui.model.dev;

public class UnitDevEditModel {
    public UnitDevEditModel(){

    }
    public UnitDevEditModel(String devNum, String address,
                            String friend_name, String site_name,
                            String area_Name_1, String area_Name_2,
                            String area_Name_3, String area_Name_4, String lon, String lat) {
        devnum = devNum;
        this.address = address;
        this.friend_name = friend_name;
        this.site_name = site_name;
        Area_Name_1 = area_Name_1;
        Area_Name_2 = area_Name_2;
        Area_Name_3 = area_Name_3;
        Area_Name_4 = area_Name_4;
        Lon = lon;
        Lat = lat;
    }
    String devnum;
    String address;
    String friend_name;
    String site_name;
    String Area_Name_1;
    String Area_Name_2;
    String Area_Name_3;
    String Area_Name_4;
    String Lon;
    String Lat;

    public String getDevnum() {
        return devnum;
    }

    public void setDevnum(String devnum) {
        this.devnum = devnum;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFriend_name() {
        return friend_name;
    }

    public void setFriend_name(String friend_name) {
        this.friend_name = friend_name;
    }

    public String getSite_name() {
        return site_name;
    }

    public void setSite_name(String site_name) {
        this.site_name = site_name;
    }

    public String getArea_Name_1() {
        return Area_Name_1;
    }

    public void setArea_Name_1(String area_Name_1) {
        Area_Name_1 = area_Name_1;
    }

    public String getArea_Name_2() {
        return Area_Name_2;
    }

    public void setArea_Name_2(String area_Name_2) {
        Area_Name_2 = area_Name_2;
    }

    public String getArea_Name_3() {
        return Area_Name_3;
    }

    public void setArea_Name_3(String area_Name_3) {
        Area_Name_3 = area_Name_3;
    }

    public String getArea_Name_4() {
        return Area_Name_4;
    }

    public void setArea_Name_4(String area_Name_4) {
        Area_Name_4 = area_Name_4;
    }

    public String getLon() {
        return Lon;
    }

    public void setLon(String lon) {
        Lon = lon;
    }

    public String getLat() {
        return Lat;
    }

    public void setLat(String lat) {
        Lat = lat;
    }
}

