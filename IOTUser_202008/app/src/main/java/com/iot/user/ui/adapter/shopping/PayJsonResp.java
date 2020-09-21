package com.iot.user.ui.adapter.shopping;

public class PayJsonResp {
 /*{
        "code": 0,
            "msg": "",
            "data": {
        "noncestr": "1aFOwvGlbu8rgyR7",
                "package": "Sign=WXPay",
                "partnerid": "1549038411",
                "prepayid": "wx15002954506398a2bdea0c7e1512291300",
                "sign": "C618DC677E1CDDAE30B4CB6AD38FC7D8",
                "timestamp": "1565796970"
    }
    }*/

    private int code;

    private String msg;

    private PayInfo data;

    public PayJsonResp() {

    }

    public PayJsonResp(int code, String msg, PayInfo data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
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

    public PayInfo getData() {
        return data;
    }

    public void setData(PayInfo data) {
        this.data = data;
    }
}

