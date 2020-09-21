package com.iot.user.http.request.login;

public class UnitFamilyListRequest {
    public UnitFamilyListRequest() {
    }

    public UnitFamilyListRequest(String uId, String token) {
        this.body = new Body(uId, token);
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

        public Body(String uId, String token) {
            this.uId = uId;
            this.token = token;
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
    }
}
