package com.iot.user.ui.model.location;

public class PickerAddressModel {
    public PickerAddressModel(){

    }
    public PickerAddressModel(String province,String city,String district,String street
            ,Double lon,Double lat,String addressInfo,String detailAddressInfo){
        this.addressInfo=addressInfo;
        this.detailAddressInfo=detailAddressInfo;
        this.province=province;
        this.city=city;
        this.district=district;
        this.street=street;
        this.lon=lon;
        this.lat=lat;
    }
    private String addressInfo;
    private String detailAddressInfo;
    private String province;
    private String city;
    private String district;
    private String street;
    private Double lon;
    private Double lat;

    public String getAddressInfo() {
        return addressInfo;
    }

    public void setAddressInfo(String addressInfo) {
        this.addressInfo = addressInfo;
    }

    public String getDetailAddressInfo() {
        return detailAddressInfo;
    }

    public void setDetailAddressInfo(String detailAddressInfo) {
        this.detailAddressInfo = detailAddressInfo;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
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
}

