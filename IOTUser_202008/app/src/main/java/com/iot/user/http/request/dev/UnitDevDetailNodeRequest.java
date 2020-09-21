package com.iot.user.http.request.dev;

public class UnitDevDetailNodeRequest {
    public UnitDevDetailNodeRequest() {
    }

    public UnitDevDetailNodeRequest(String uId, String token, String devNum, String haddr,String daddrType) {
        this.body = new Body(uId, token, devNum, haddr,daddrType);
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
        private String devNum;
        private String haddr;
        private String daddrType;

        public Body(String uId, String token, String devNum, String haddr,String daddrType) {
            this.uId = uId;
            this.token = token;
            this.devNum = devNum;
            this.haddr = haddr;
            this.daddrType=daddrType;
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

        public String getDevNum() {
            return devNum;
        }

        public void setDevNum(String devNum) {
            this.devNum = devNum;
        }

        public String getHaddr() {
            return haddr;
        }

        public void setHaddr(String haddr) {
            this.haddr = haddr;
        }

        public String getDaddrType() {
            return daddrType;
        }

        public void setDaddrType(String daddrType) {
            this.daddrType = daddrType;
        }
    }
}

