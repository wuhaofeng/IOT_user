package com.iot.user.http.request.message;

public class UnitMessageFamilyRequest {
    public UnitMessageFamilyRequest() {
    }
    public UnitMessageFamilyRequest(String userid, String token, int pageNum, int pagesize) {
        this.body=new Body(userid,token,pageNum,pagesize);
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
        private int pageNo;
        private int pageSize;
        public Body(String uId, String token, int pageNum, int pagesize) {
            this.uId = uId;
            this.token = token;
            this.pageNo = pageNum;
            this.pageSize = pagesize;
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

        public int getPageNo() {
            return pageNo;
        }

        public void setPageNo(int pageNo) {
            this.pageNo = pageNo;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }
    }

}

