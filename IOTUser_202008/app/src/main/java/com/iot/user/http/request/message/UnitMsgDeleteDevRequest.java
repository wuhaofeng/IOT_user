package com.iot.user.http.request.message;

import java.util.List;

public class UnitMsgDeleteDevRequest {
    public UnitMsgDeleteDevRequest() {
    }
    public UnitMsgDeleteDevRequest(String userid, String token, List familyProductList) {
        this.body=new Body(userid,token,familyProductList);
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
        private List familyProductList;

        public Body(String uId, String token, List familyProductList) {
            this.uId = uId;
            this.token = token;
            this.familyProductList = familyProductList;
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

        public List getFamilyProductList() {
            return familyProductList;
        }

        public void setFamilyProductList(List familyProductList) {
            this.familyProductList = familyProductList;
        }
    }
}

