package com.iot.user.http.request.message;

import java.util.List;

public class UnitMsgDeleteFamilyRequest {
    public UnitMsgDeleteFamilyRequest() {
    }
    public UnitMsgDeleteFamilyRequest(String userid, String token, List familySharedList) {
        this.body=new Body(userid,token,familySharedList);
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
        private List familySharedList;

        public Body(String uId, String token, List familySharedList) {
            this.uId = uId;
            this.token = token;
            this.familySharedList = familySharedList;
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

        public List getFamilySharedList() {
            return familySharedList;
        }

        public void setFamilySharedList(List familySharedList) {
            this.familySharedList = familySharedList;
        }
    }
}

