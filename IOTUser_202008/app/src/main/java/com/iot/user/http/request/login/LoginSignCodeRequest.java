package com.iot.user.http.request.login;

public class LoginSignCodeRequest {
    private Body body;

    public LoginSignCodeRequest() {
    }

    public LoginSignCodeRequest(String user) {
        this.body = new Body(user);
    }

    public Body getBody() {
        return body;
    }

    public void setBody(String user) {
        this.body = new Body(user);
    }

    class Body{
        private String user;

        public Body(String user) {
            this.user = user;
        }

    }
}
