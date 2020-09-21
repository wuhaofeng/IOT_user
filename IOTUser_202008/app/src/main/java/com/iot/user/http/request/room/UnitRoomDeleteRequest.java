package com.iot.user.http.request.room;

import java.util.List;

public class UnitRoomDeleteRequest {
    public UnitRoomDeleteRequest() {
    }
    public UnitRoomDeleteRequest(String uId, String token, String familyId, List<String> familyPlaceId) {
        this.body=new Body(uId,token,familyId,familyPlaceId);
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
        private List<String> familyPlaceId;

        public Body(String uId, String token, String familyId,List<String> familyPlaceId) {
            this.uId = uId;
            this.token = token;
            this.familyId = familyId;
            this.familyPlaceId=familyPlaceId;
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

        public List<String> getFamilyPlaceId() {
            return familyPlaceId;
        }

        public void setFamilyPlaceId(List<String> familyPlaceId) {
            this.familyPlaceId = familyPlaceId;
        }
    }
}


