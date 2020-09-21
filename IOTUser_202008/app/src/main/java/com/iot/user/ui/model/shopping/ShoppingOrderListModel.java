package com.iot.user.ui.model.shopping;

import java.util.List;

public class ShoppingOrderListModel {
    private List< ShoppingOrderDetailModel> orderDetailList;
    private String create_time;
    private String finish_time;
    private String product_id;
    private String status;
    private String user_id;
    private String total_fee;
    private String trade_no;

    public List<ShoppingOrderDetailModel> getOrderDetailList() {
        return orderDetailList;
    }

    public void setOrderDetailList(List<ShoppingOrderDetailModel> orderDetailList) {
        this.orderDetailList = orderDetailList;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getFinish_time() {
        return finish_time;
    }

    public void setFinish_time(String finish_time) {
        this.finish_time = finish_time;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(String total_fee) {
        this.total_fee = total_fee;
    }

    public String getTrade_no() {
        return trade_no;
    }

    public void setTrade_no(String trade_no) {
        this.trade_no = trade_no;
    }
}

