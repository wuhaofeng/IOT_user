package com.iot.user.http.request.shopping;

import java.util.List;

public class ShoppingDeviceSubmitRequest {
    public ShoppingDeviceSubmitRequest(){
    }

    public ShoppingDeviceSubmitRequest(List products, Float orderSwitch, String token, String uId) {
        this.products = products;
        this.orderSwitch = orderSwitch.intValue();
        this.token = token;
        this.uId = uId;
    }
    private int orderSwitch;
    private List products;
    private String token;
    private String uId;

    public int getOrderSwitch() {
        return orderSwitch;
    }

    public void setOrderSwitch(int orderSwitch) {
        this.orderSwitch = orderSwitch;
    }

    public List getProducts() {
        return products;
    }

    public void setProducts(List products) {
        this.products = products;
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
