package com.iot.user.ui.adapter.shopping;

public class AliPayJsonResp {
 /*{
       "code": 0,
    "msg": "",
    "data": {
        "orderString":
    }
    }*/

    private int code;

    private String msg;

    private Data data;

    public AliPayJsonResp() {

    }

    public AliPayJsonResp(int code, String msg, Data data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getOrderString() {
        if(data != null){
            return data.getOrderString();
        }
        return "";
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    class Data{
        private String orderString;

        public String getOrderString() {
            return orderString;
        }

        public void setOrderString(String orderString) {
            this.orderString = orderString;
        }
    }
}

