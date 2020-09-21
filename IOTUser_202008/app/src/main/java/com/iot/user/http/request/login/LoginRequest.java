package com.iot.user.http.request.login;

public class LoginRequest {
    private Body body;

    public LoginRequest() {

    }

    public LoginRequest(String user,String pwd) {
        this.body = new Body(user,pwd);
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    class Body {
        String user;
        String pwd;

        public Body() {
        }

        public Body(String user, String pwd) {
            this.user = user;
            this.pwd = pwd;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getPwd() {
            return pwd;
        }

        public void setPwd(String pwd) {
            this.pwd = pwd;
        }
    }
}
