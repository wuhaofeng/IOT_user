package com.iot.user.http.request.dev;

public class UnitDevRemovePlaceRequest {
    public UnitDevRemovePlaceRequest(){
    }
    public UnitDevRemovePlaceRequest(String uId, String token,String devnum, String familyId,String oldPlaceId, String newPlaceId) {
        this.body = new Body(uId, token,devnum, familyId,oldPlaceId,newPlaceId);
    }
    private Body body;

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    class Body {
        private String devnum;
        private String familyId;
        private String token;
        private String uId;
        private String oldPlaceId;
        private String newPlaceId;

        public Body(String uId, String token, String devnum, String familyId, String oldPlaceId, String newPlaceId) {
            this.devnum = devnum;
            this.familyId = familyId;
            this.token = token;
            this.uId = uId;
            this.oldPlaceId = oldPlaceId;
            this.newPlaceId = newPlaceId;
        }

        public String getDevnum() {
            return devnum;
        }

        public void setDevnum(String devnum) {
            this.devnum = devnum;
        }

        public String getFamilyId() {
            return familyId;
        }

        public void setFamilyId(String familyId) {
            this.familyId = familyId;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getuId() {
            return uId;
        }

        public void setuId(String uId) {
            this.uId = uId;
        }

        public String getOldPlaceId() {
            return oldPlaceId;
        }

        public void setOldPlaceId(String oldPlaceId) {
            this.oldPlaceId = oldPlaceId;
        }

        public String getNewPlaceId() {
            return newPlaceId;
        }

        public void setNewPlaceId(String newPlaceId) {
            this.newPlaceId = newPlaceId;
        }
    }
}

