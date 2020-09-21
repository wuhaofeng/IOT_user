package com.iot.user.ui.adapter.shopping;

import com.alibaba.fastjson.annotation.JSONField;

public class PayInfo {

    /*"noncestr": "1aFOwvGlbu8rgyR7",
            "package": "Sign=WXPay",
            "partnerid": "1549038411",
            "prepayid": "wx15002954506398a2bdea0c7e1512291300",
            "sign": "C618DC677E1CDDAE30B4CB6AD38FC7D8",
            "timestamp": "1565796970"*/
    private String noncestr;
    @JSONField(name="package") private String packagestr;
    private String partnerid;
    private String prepayid;
    private String sign;
    private String timestamp;

    public PayInfo() {
    }

    public PayInfo(String noncestr, String packagestr, String partnerid, String prepayid, String sign, String timestamp) {
        this.noncestr = noncestr;
        this.packagestr = packagestr;
        this.partnerid = partnerid;
        this.prepayid = prepayid;
        this.sign = sign;
        this.timestamp = timestamp;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getPackagestr() {
        return packagestr;
    }

    public void setPackagestr(String packagestr) {
        this.packagestr = packagestr;
    }
}

