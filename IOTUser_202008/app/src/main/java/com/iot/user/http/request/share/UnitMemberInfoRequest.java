package com.iot.user.http.request.share;

public class UnitMemberInfoRequest {
    public UnitMemberInfoRequest() {
    }
    public UnitMemberInfoRequest(String uId, String token, String username) {
        this.body=new Body(uId,token,username);
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
        private String username;

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

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public Body(String uId, String token, String username) {
            this.uId = uId;
            this.token = token;
            this.username = username;

        }
    }
}

