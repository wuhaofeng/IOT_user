package com.iot.user.http.request.share;

public class UnitFamilyShareRequest {
    public UnitFamilyShareRequest() {
    }

    public UnitFamilyShareRequest(String uId, String token, String username,String familyId) {
        this.body = new Body(uId, token, username,familyId);
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
        private String familyId;
        public Body(String uId, String token, String username,String familyId) {
            this.uId = uId;
            this.token = token;
            this.username = username;
            this.familyId=familyId;
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

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getFamilyId() {
            return familyId;
        }

        public void setFamilyId(String familyId) {
            this.familyId = familyId;
        }
    }
}

