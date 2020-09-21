package com.iot.user.http.request.dev;

public class CommonDevLowJsonRequest {
    public CommonDevLowJsonRequest(){
        this.body = new Body();
    }

    public CommonDevLowJsonRequest(String devnum, String token, String uId) {
        this.body = new Body(devnum,token, uId);
    }

    private Body body;

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public class Body{
        /* "body": {
    "devnum": "1182001900000031",
    "token": "8c142466710f46cf9394f7d4bf807afc",
    "uId": "6"
  }*/
        private String token;
        private String uId;
        private String devnum;

        public Body() {
        }

        public Body(String devNum, String token, String uId) {
            this.devnum = devNum;
            this.token = token;
            this.uId = uId;
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
            return devnum;
        }

        public void setDevNum(String devNum) {
            this.devnum = devNum;
        }
    }
}

