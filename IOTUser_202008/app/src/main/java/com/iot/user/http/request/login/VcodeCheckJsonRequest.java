package com.iot.user.http.request.login;

public class VcodeCheckJsonRequest {
    private Body body;

    public VcodeCheckJsonRequest() {

    }

    public VcodeCheckJsonRequest(String mobile,String type,String code) {
        this.body = new Body(mobile,type,code);
    }

    class Body{
        private String mobile;
        private String type;
        private String code;

        public Body() {
        }

        public Body(String mobile, String type, String code) {
            this.mobile = mobile;
            this.type = type;
            this.code = code;
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

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }
}
