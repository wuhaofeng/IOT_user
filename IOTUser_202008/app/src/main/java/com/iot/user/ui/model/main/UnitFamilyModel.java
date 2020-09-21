package com.iot.user.ui.model.main;

import java.io.Serializable;

public class UnitFamilyModel implements Serializable {
    public  UnitFamilyModel(){

    }
    private String devnumCount;
    private String family_address;
    private String type;//1管理员 3成员
    private String family_id;
    private String family_name;
    private String placeCount;
    private String flag;//1 未选中 2已选中
    private String user_id;
    private String create_time;

    public String getDevnumCount() {
        return devnumCount;
    }

    public void setDevnumCount(String devnumCount) {
        this.devnumCount = devnumCount;
    }

    public String getFamily_address() {
        return family_address;
    }

    public void setFamily_address(String family_address) {
        this.family_address = family_address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFamily_id() {
        return family_id;
    }

    public void setFamily_id(String family_id) {
        this.family_id = family_id;
    }

    public String getFamily_name() {
        return family_name;
    }

    public void setFamily_name(String family_name) {
        this.family_name = family_name;
    }

    public String getPlaceCount() {
        return placeCount;
    }

    public void setPlaceCount(String placeCount) {
        this.placeCount = placeCount;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }
}

