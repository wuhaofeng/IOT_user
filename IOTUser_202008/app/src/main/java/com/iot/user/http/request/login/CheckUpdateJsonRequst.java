package com.iot.user.http.request.login;

public class CheckUpdateJsonRequst {
    public CheckUpdateJsonRequst(){
        this.body= new Body();
    }

    public CheckUpdateJsonRequst(String uId, String token, String type) {
        this.body = new Body(uId,token,type);
    }

    private Body body;

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    class Body {
        private String uId;
        private String token;
        private String type;

        public Body(String uId, String token, String type) {
            this.uId = uId;
            this.token = token;
            this.type = type;
        }

        public Body() {
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

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}

