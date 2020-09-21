package com.iot.user.http.request.message;

public class DevHistoryJsonRequst {
    public DevHistoryJsonRequst(){
        this.body= new Body();
    }

    public DevHistoryJsonRequst(String uId, String token, String packRecodId) {
        this.body = new Body(uId,token,packRecodId);
    }

    private Body body;

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public   class Body {
        private String uId;
        private String token;
        private String packRecodId;

        public Body(String uId, String token, String packRecodId) {
            this.uId = uId;
            this.token = token;
            this.packRecodId = packRecodId;
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

        public String getPackRecodId() {
            return packRecodId;
        }

        public void setPackRecodId(String packRecodId) {
            this.packRecodId = packRecodId;
        }
    }
}

