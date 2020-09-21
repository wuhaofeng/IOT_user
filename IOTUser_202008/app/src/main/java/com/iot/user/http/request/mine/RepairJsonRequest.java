package com.iot.user.http.request.mine;

public class RepairJsonRequest {

    /* "body":{
     "uId":"用户ID",
     "token":"TokenId",
     "devNum":"设备编号",
      "content":"报修内容"
   }
     */
    private Body body;


    public RepairJsonRequest() {

    }

    public RepairJsonRequest(Body body) {
        this.body = body;
    }

    public RepairJsonRequest(String uId, String token, String devNum, String content) {
        this.body = new Body(uId, token, devNum, content);
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    class Body{
        private String uId;
        private String token;
        private String devNum;
        private String content;

        public Body(String uId, String token, String devNum, String content) {
            this.uId = uId;
            this.token = token;
            this.devNum = devNum;
            this.content = content;
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

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }



}

