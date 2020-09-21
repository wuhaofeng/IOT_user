package com.iot.user.http.request.shopping;

public class ShoppingOrderDiscountRequest {
    public ShoppingOrderDiscountRequest(){
    }

    public ShoppingOrderDiscountRequest(String devType, Float orderSwitch, String token, String uId) {
        this.devType = devType;
        this.orderSwitch = orderSwitch;
        this.token = token;
        this.uId = uId;
    }
    private String devType;
    private Float orderSwitch;
    private String token;
    private String uId;

    public String getDevType() {
        return devType;
    }

    public void setDevType(String devType) {
        this.devType = devType;
    }

    public Float getOrderSwitch() {
        return orderSwitch;
    }

    public void setOrderSwitch(Float orderSwitch) {
        this.orderSwitch = orderSwitch;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }
}
