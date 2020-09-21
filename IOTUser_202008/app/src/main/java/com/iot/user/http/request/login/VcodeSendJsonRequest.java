package com.iot.user.http.request.login;

public class VcodeSendJsonRequest {
    private Body body;


    public VcodeSendJsonRequest() {

    }

    public VcodeSendJsonRequest(String mobile,String type) {
        this.body = new Body(mobile,type);
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    class Body{
        private String mobile;
        private String type;

        public Body() {
        }

        public Body(String mobile, String type) {
            this.mobile = mobile;
            this.type = type;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

}
