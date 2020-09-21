package com.iot.user.ui.model.main;

import java.io.Serializable;

public class UnitFamilyRoomModel implements Serializable {
    public  UnitFamilyRoomModel(){

    }
    public UnitFamilyRoomModel(String family_id,String place_name,String family_place_id,String devnumCount,String place_reg_time){
        this.family_id=family_id;
        this.place_name=place_name;
        this.family_place_id=family_place_id;
        this.devnumCount=devnumCount;
        this.place_reg_time=place_reg_time;
    }

    private String family_id;
    private String place_name;
    private String family_place_id;//1管理员 3成员
    private String devnumCount;
    private String place_reg_time;

    public String getFamily_id() {
        return family_id;
    }

    public void setFamily_id(String family_id) {
        this.family_id = family_id;
    }

    public String getPlace_name() {
        return place_name;
    }

    public void setPlace_name(String place_name) {
        this.place_name = place_name;
    }

    public String getFamily_place_id() {
        return family_place_id;
    }

    public void setFamily_place_id(String family_place_id) {
        this.family_place_id = family_place_id;
    }

    public String getDevnumCount() {
        return devnumCount;
    }

    public void setDevnumCount(String devnumCount) {
        this.devnumCount = devnumCount;
    }

    public String getPlace_reg_time() {
        return place_reg_time;
    }

    public void setPlace_reg_time(String place_reg_time) {
        this.place_reg_time = place_reg_time;
    }
}
