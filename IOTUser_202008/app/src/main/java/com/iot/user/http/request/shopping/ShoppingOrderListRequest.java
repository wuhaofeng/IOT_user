package com.iot.user.http.request.shopping;

public class ShoppingOrderListRequest {
    public ShoppingOrderListRequest(){
    }

    public ShoppingOrderListRequest(int status, String product_id,int pageNo, int pageSize, String token, String uId) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.status = status;
        this.uId = uId;
        this.token = token;
        this.product_id = product_id;
    }
    private int pageNo;
    private int pageSize;
    private int status;
    private String token;
    private String uId;
    private String product_id;

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }
}
