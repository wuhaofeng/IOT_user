package com.iot.user.ui.model.shopping;

import java.util.List;

public class ShoppingOrderDetailResp {
    private List<ShoppingOrderDetailModel> orderDetail;
    private String create_time;
    private String exprie_time;
    private String status;
    private String exprie_minute;

    public List<ShoppingOrderDetailModel> getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(List<ShoppingOrderDetailModel> orderDetail) {
        this.orderDetail = orderDetail;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getExprie_time() {
        return exprie_time;
    }

    public void setExprie_time(String exprie_time) {
        this.exprie_time = exprie_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getExprie_minute() {
        return exprie_minute;
    }

    public void setExprie_minute(String exprie_minute) {
        this.exprie_minute = exprie_minute;
    }
}

