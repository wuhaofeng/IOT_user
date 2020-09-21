package com.iot.user.http.request.main;

public class UnitDevDetailRequest {
    private Body body;

    public UnitDevDetailRequest() {

    }

    public UnitDevDetailRequest(String uId, String token, String devNum) {
        this.body = new Body(uId,token,devNum);
    }

    class Body{
        private String uId;
        private String token;
        private String devNum;

        public Body() {
        }

        public Body(String uId, String token, String devNum) {
            this.uId = uId;
            this.token = token;
            this.devNum = devNum;
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
    }
}

