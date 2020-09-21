package com.iot.user.http.request.shopping;

public class ShoppingOrderSwitchRequest {
    public ShoppingOrderSwitchRequest(){
    }

    public ShoppingOrderSwitchRequest(String token, String uId) {
        this.token = token;
        this.uId = uId;
    }
    private String token;
    private String uId;

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
