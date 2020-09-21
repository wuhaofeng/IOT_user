package com.iot.user.http.request.main;

public class UnitFamilyRoomRequest {
    public UnitFamilyRoomRequest() {
    }

    public UnitFamilyRoomRequest(String uId, String token, String familyId,String type) {
        this.body = new Body(uId, token, familyId,type);
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
        private String type;

        public Body(String uId, String token, String familyId, String type) {
            this.uId = uId;
            this.token = token;
            this.familyId = familyId;
            this.type = type;
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

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}

