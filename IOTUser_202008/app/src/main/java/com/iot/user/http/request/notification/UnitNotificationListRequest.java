package com.iot.user.http.request.notification;

public class UnitNotificationListRequest {
    public UnitNotificationListRequest(){
        this.body= new Body();
    }

    public UnitNotificationListRequest(int pageNum, int pageSize, String token, String uId) {
        this.body = new Body(pageNum, pageSize, token, uId);
    }

    private Body body;

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    class Body {
        private int pageNum;
        private int pageSize;
        private String token;
        private String uId;

        public Body(int pageNum, int pageSize, String token, String uId) {
            this.pageNum = pageNum;
            this.pageSize = pageSize;
            this.token = token;
            this.uId = uId;
        }

        public Body() {
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
    }
}

