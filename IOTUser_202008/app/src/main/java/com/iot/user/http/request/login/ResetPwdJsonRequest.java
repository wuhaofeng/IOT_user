package com.iot.user.http.request.login;

public class ResetPwdJsonRequest {
    private Body body;

    public ResetPwdJsonRequest() {

    }

    public ResetPwdJsonRequest(String mobile,String np) {
        this.body = new Body(mobile,np);
    }

    class Body{
        private String mobile;
        private String np;

        public Body() {
        }

        public Body(String mobile, String np) {
            this.mobile = mobile;
            this.np = np;
        }
    }
}
