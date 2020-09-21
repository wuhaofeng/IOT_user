package com.iot.user.http.request.shopping;

/**
 * @Author: 订单通用请求参数
 * @Date: 2019/3/24
 */
public class OrderInfoJsonRequst {

    /*{
    "userid":"4",
    "status":1,  //订单状态 0:全部 1:未支付 2:已支付
    "lastorderid":"", //最后一条订单id，传""从最新一条开始
    "pagesize":"5" //查询的记录数
    }*/
    private String userid;
    private int status;
    private String lastorderid;
    private String pagesize;

    public OrderInfoJsonRequst() {
    }

    public OrderInfoJsonRequst(String userid, int status, String lastorderid, String pagesize) {
        this.userid = userid;
        this.status = status;
        this.lastorderid = lastorderid;
        this.pagesize = pagesize;
    }

    public String getLastorderid() {
        return lastorderid;
    }

    public void setLastorderid(String lastorderid) {
        this.lastorderid = lastorderid;
    }

    public String getPagesize() {
        return pagesize;
    }

    public void setPagesize(String pagesize) {
        this.pagesize = pagesize;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
