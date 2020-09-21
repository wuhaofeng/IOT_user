package com.iot.user.http.request.dev.member;

public class UnitMemberAddRequest {
    public UnitMemberAddRequest() {
    }

    public UnitMemberAddRequest(String uId, String token, String devnum, String username) {
        this.body = new Body(uId, token, devnum, username);
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
        private String devnum;
        private String username;

        public Body(String uId, String token, String devnum, String username) {
            this.uId = uId;
            this.token = token;
            this.devnum = devnum;
            this.username = username;
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

        public String getDevnum() {
            return devnum;
        }

        public void setDevnum(String devnum) {
            this.devnum = devnum;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}

