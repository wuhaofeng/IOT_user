package com.iot.user.ui.model.location;

import java.util.List;

public class GaoDeGetRegionAreaJsonResp {
    private String status;
    private String info;
    private String infocode;
    private String count;
    private Suggestion suggestion;
    private List<District> districts;


    public GaoDeGetRegionAreaJsonResp(){

    }

    public GaoDeGetRegionAreaJsonResp(String status, String info, String infocode, String count, Suggestion suggestion, List<District> districts) {
        this.status = status;
        this.info = info;
        this.infocode = infocode;
        this.count = count;
        this.suggestion = suggestion;
        this.districts = districts;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getInfocode() {
        return infocode;
    }

    public void setInfocode(String infocode) {
        this.infocode = infocode;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public Suggestion getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(Suggestion suggestion) {
        this.suggestion = suggestion;
    }

    public List<District> getDistricts() {
        return districts;
    }

    public void setDistricts(List<District> districts) {
        this.districts = districts;
    }

    class Suggestion{
        private List<String>  keywords;
        private List<String> cities;
    }
}

