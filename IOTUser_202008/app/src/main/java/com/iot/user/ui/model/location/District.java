package com.iot.user.ui.model.location;

import java.util.List;

public class District {
    private Object citycode;
    private String adcode;
    private String name;
    private String center;
    private String level;
    private List<District> districts;

    public District() {
    }

    public District(Object citycode, String adcode, String name, String center, String level, List<District> districts) {
        this.citycode = citycode;
        this.adcode = adcode;
        this.name = name;
        this.center = center;
        this.level = level;
        this.districts = districts;
    }

    public List<District> getDistricts() {
        return districts;
    }

    public void setDistricts(List<District> districts) {
        this.districts = districts;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

