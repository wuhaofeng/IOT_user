package com.iot.user.http.request.main;

public class UnitHomeDevStatusRequest {
    public UnitHomeDevStatusRequest() {
    }

    public UnitHomeDevStatusRequest(String uId, String token, String familyId) {
        this.body = new Body(uId, token, familyId);
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
        private String familyId;

        public Body(String uId, String token, String familyId) {
            this.uId = uId;
            this.token = token;
            this.familyId = familyId;
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

        public String getFamilyId() {
            return familyId;
        }

        public void setFamilyId(String familyId) {
            this.familyId = familyId;
        }
    }

}
