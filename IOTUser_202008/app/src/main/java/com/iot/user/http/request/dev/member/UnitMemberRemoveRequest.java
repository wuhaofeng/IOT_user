package com.iot.user.http.request.dev.member;

import java.util.List;

public class UnitMemberRemoveRequest {
    public UnitMemberRemoveRequest() {
    }

    public UnitMemberRemoveRequest(String uId, String token, String devNum, List<String> focusList) {
        this.body = new Body(uId, token, devNum,focusList);
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
        private List<String> focusList;
        public Body(String uId, String token, String devNum,List<String> focusList) {
            this.uId = uId;
            this.token = token;
            this.devNum = devNum;
            this.focusList=focusList;
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

        public List<String> getFocusList() {
            return focusList;
        }

        public void setFocusList(List<String> focusList) {
            this.focusList = focusList;
        }
    }

}

