package com.iot.user.http.request.message;

public class UnitMessageHistoryRequest {
    private String uId;
    private String token;
    private int pageNum;
    private int pageSize;

    public UnitMessageHistoryRequest() {
    }

    public UnitMessageHistoryRequest(String userid, String token, int pageNum, int pagesize) {
        this.uId = userid;
        this.token = token;
        this.pageNum = pageNum;
        this.pageSize = pagesize;
    }

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

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}

