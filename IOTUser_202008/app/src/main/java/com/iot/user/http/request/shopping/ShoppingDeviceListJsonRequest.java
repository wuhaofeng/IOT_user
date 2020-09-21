package com.iot.user.http.request.shopping;

public class ShoppingDeviceListJsonRequest {
    public ShoppingDeviceListJsonRequest(){
    }

    public ShoppingDeviceListJsonRequest(String pageNo, int pageSize, String token, String uId) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.token = token;
        this.uId = uId;
    }
    private String pageNo;
    private int pageSize;
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

    public String getPageNo() {
        return pageNo;
    }

    public void setPageNo(String pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
