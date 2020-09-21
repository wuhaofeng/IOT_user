package com.iot.user.http.request.dev;

import java.util.List;

public class UnitDevNodeOpenRequest  {
    public UnitDevNodeOpenRequest() {
    }

    public UnitDevNodeOpenRequest(String uId, String token, String devnum, List haddrs) {
        body = new Body(uId, token, devnum, haddrs);
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
        private String devnum;
        private List haddrs;

        public Body(String uId, String token, String devnum, List haddrs) {
            this.uId = uId;
            this.token = token;
            this.devnum = devnum;
            this.haddrs = haddrs;
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

        public String getDevnum() {
            return devnum;
        }

        public void setDevnum(String devnum) {
            this.devnum = devnum;
        }

        public List getHaddrs() {
            return haddrs;
        }

        public void setHaddrs(List haddrs) {
            this.haddrs = haddrs;
        }
    }

}
