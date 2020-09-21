package com.iot.user.http.request.room;

public class UnitCheckRoomNameRequest {
    public UnitCheckRoomNameRequest() {
    }
    public UnitCheckRoomNameRequest(String uId, String token, String familyId,String placeName) {
        this.body=new Body(uId,token,familyId,placeName);
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
        private String placeName;

        public Body(String uId, String token, String familyId,String placeName) {
            this.uId = uId;
            this.token = token;
            this.familyId = familyId;
            this.placeName=placeName;
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

        public String getPlaceName() {
            return placeName;
        }

        public void setPlaceName(String placeName) {
            this.placeName = placeName;
        }
    }
}




