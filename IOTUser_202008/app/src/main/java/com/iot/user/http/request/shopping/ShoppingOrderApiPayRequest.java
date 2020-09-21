package com.iot.user.http.request.shopping;

public class ShoppingOrderApiPayRequest {
    public ShoppingOrderApiPayRequest(){
    }

    public ShoppingOrderApiPayRequest(String tradeno, String token, String uId) {
        this.tradeno = tradeno;
        this.token = token;
        this.uId = uId;
    }
    private String tradeno;
    private String token;
    private String uId;

    public String getTradeno() {
        return tradeno;
    }

    public void setTradeno(String tradeno) {
        this.tradeno = tradeno;
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
