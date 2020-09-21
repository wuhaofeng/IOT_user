package com.iot.user.http.request.register;

public class RegistJsonRequest {
    private Body body;
    public RegistJsonRequest() {
    }
    public RegistJsonRequest(String mobile, String pwd,String nickName) {
        this.body = new Body(mobile,pwd,nickName);
    }
    public Body getBody() {
        return body;
    }
    public void setBody(Body body) {
        this.body = body;
    }

    class Body{
        private String mobile;
        private String pwd;
        private String nickName;
        public Body() {
        }
        public Body(String mobile, String pwd,String nickName) {
            this.mobile = mobile;
            this.pwd = pwd;
            this.nickName=nickName;
        }
        public String getMobile() {
            return mobile;
        }
        public void setMobile(String mobile) {
            this.mobile = mobile;
        }
        public String getPwd() {
            return pwd;
        }
        public void setPwd(String pwd) {
            this.pwd = pwd;
        }
        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }
    }
}
