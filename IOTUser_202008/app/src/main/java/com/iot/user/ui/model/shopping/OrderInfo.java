package com.iot.user.ui.model.shopping;

public class OrderInfo {
/*
     "createTime": "1570276306",
                "devId": "118200190100316",
                "finishTime": "1570540437",
                "orderId": 301,
                "productId": 182,
                "productName": "融合可控燃气探测器",
                "remark": "融合智慧燃气远程监控终端",
                "status": 1,
                "totalFee": 0.02,
                "tradeNo": "10051951460001001933",
                "userId": "4",
                "year": 2
                */

    private String createTime;
    private String devId;
    private String finishTime;
    private int orderId;
    private int productId;
    private String productName;
    private String remark;
    private int status;
    private double totalFee;
    private String tradeNo;
    private String userId;
    private int year;

    public OrderInfo() {
    }

    public OrderInfo(String createTime, String devId,
                     String finishTime, int orderId, int productId,
                     String productName, String remark, int status,
                     double totalFee, String tradeNo, String userId, int year) {
        this.createTime = createTime;
        this.devId = devId;
        this.finishTime = finishTime;
        this.orderId = orderId;
        this.productId = productId;
        this.productName = productName;
        this.remark = remark;
        this.status = status;
        this.totalFee = totalFee;
        this.tradeNo = tradeNo;
        this.userId = userId;
        this.year = year;
    }

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(double totalFee) {
        this.totalFee = totalFee;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

