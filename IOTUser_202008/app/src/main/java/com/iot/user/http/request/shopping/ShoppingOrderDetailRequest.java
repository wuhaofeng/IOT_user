package com.iot.user.http.request.shopping;

public class ShoppingOrderDetailRequest {
    public ShoppingOrderDetailRequest(){
    }

    public ShoppingOrderDetailRequest(String tradeno, String token, String uId) {
        this.uId = uId;
        this.token = token;
        this.tradeno = tradeno;
    }
    private String uId;
    private String token;
    private String tradeno;

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTradeno() {
        return tradeno;
    }

    public void setTradeno(String tradeno) {
        this.tradeno = tradeno;
    }
}
