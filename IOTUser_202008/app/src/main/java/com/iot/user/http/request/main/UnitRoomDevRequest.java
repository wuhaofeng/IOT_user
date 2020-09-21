package com.iot.user.http.request.main;

public class UnitRoomDevRequest {
    public UnitRoomDevRequest() {
    }
    public UnitRoomDevRequest(String uId, String token, int pageNum, int pageSize, String familyId,String placeId) {
        this.body=new Body(uId,token,pageNum,pageSize,familyId,placeId);
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
        private int pageNum;
        private int pageSize;
        private String familyId;
        private String placeId;

        public Body(String uId, String token, int pageNum, int pageSize, String familyId,String placeId) {
            this.uId = uId;
            this.token = token;
            this.pageNum = pageNum;
            this.pageSize = pageSize;
            this.familyId = familyId;
            this.placeId=placeId;
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

        public int getPageNum() {
            return pageNum;
        }

        public void setPageNum(int pageNum) {
            this.pageNum = pageNum;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public String getFamilyId() {
            return familyId;
        }

        public void setFamilyId(String familyId) {
            this.familyId = familyId;
        }

        public String getPlaceId() {
            return placeId;
        }

        public void setPlaceId(String placeId) {
            this.placeId = placeId;
        }
    }
}

