package com.iot.user.http.request.alert;

public class ModifyOnlineDevJsonRequest {
    private Body body;

    public ModifyOnlineDevJsonRequest() {

    }

    public ModifyOnlineDevJsonRequest(String uId, String token, String devNum, String dealType) {
        this.body = new Body(uId,token,devNum,dealType);
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }


    /** "body":{
     "uId":"用户ID",
     "token":"TokenId",
     "devNum": "设备编号",
     "dealType":"处理状态"
     }*/
    class Body {
        String uId;
        String token;
        String devNum;
        String dealType;

        public Body() {
        }

        public Body(String uId, String token, String devNum, String dealType) {
            this.uId = uId;
            this.token = token;
            this.devNum = devNum;
            this.dealType = dealType;
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

        public String getDevNum() {
            return devNum;
        }

        public void setDevNum(String devNum) {
            this.devNum = devNum;
        }

        public String getDealType() {
            return dealType;
        }

        public void setDealType(String dealType) {
            this.dealType = dealType;
        }
    }

}

