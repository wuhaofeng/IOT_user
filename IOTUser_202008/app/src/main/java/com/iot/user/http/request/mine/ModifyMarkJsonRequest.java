package com.iot.user.http.request.mine;

public class ModifyMarkJsonRequest {
    private Body body;

    public ModifyMarkJsonRequest() {

    }

    public ModifyMarkJsonRequest(String uId, String token, String sms_mark, String phone_mark, String push_mark) {
        this.body = new Body(uId, token, sms_mark, phone_mark, push_mark);
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    class Body {
        /*"uId":"用户ID",
                "":"TokenId",
                "": "0 表示推送 1不推送  不传值传空串"" ",
                "": "0 表示推送 1不推送 不传值传空串"" ",
                "": "0 表示推送 1不推送 不传值传空串"" "*/
        private String uId;
        private String token;
        private String sms_mark;
        private String phone_mark;
        private String push_mark;

        public Body() {
        }

        public Body(String uId, String token, String sms_mark, String phone_mark, String push_mark) {
            this.uId = uId;
            this.token = token;
            this.sms_mark = sms_mark;
            this.phone_mark = phone_mark;
            this.push_mark = push_mark;
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

        public String getSms_mark() {
            return sms_mark;
        }

        public void setSms_mark(String sms_mark) {
            this.sms_mark = sms_mark;
        }

        public String getPhone_mark() {
            return phone_mark;
        }

        public void setPhone_mark(String phone_mark) {
            this.phone_mark = phone_mark;
        }

        public String getPush_mark() {
            return push_mark;
        }

        public void setPush_mark(String push_mark) {
            this.push_mark = push_mark;
        }
    }

}

