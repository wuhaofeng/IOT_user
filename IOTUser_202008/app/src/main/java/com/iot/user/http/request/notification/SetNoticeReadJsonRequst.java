package com.iot.user.http.request.notification;

public class SetNoticeReadJsonRequst {
    public SetNoticeReadJsonRequst(){
        this.body= new Body();
    }

    public SetNoticeReadJsonRequst(String uId, String token, String notice_id, int readed, String ack_text) {
        this.body = new Body(uId, token, notice_id, readed, ack_text);
    }

    private Body body;

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    class Body {
        /*"body":{
        "token":"7e0d44b20dd94913a9a59a2f4f439c6b",
                "uId":"6",
                "notice_id":"6",
                "readed":1,			  //0 代表未读   1 已读
                "ack_text":"已读"		 //反馈内容
    }*/
        private String uId;
        private String token;
        private String notice_id;
        private int readed;
        private String ack_text;

        public Body() {
        }

        public Body(String uId, String token, String notice_id, int readed, String ack_text) {
            this.uId = uId;
            this.token = token;
            this.notice_id = notice_id;
            this.readed = readed;
            this.ack_text = ack_text;
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

        public String getNotice_id() {
            return notice_id;
        }

        public void setNotice_id(String notice_id) {
            this.notice_id = notice_id;
        }

        public int getReaded() {
            return readed;
        }

        public void setReaded(int readed) {
            this.readed = readed;
        }

        public String getAck_text() {
            return ack_text;
        }

        public void setAck_text(String ack_text) {
            this.ack_text = ack_text;
        }
    }
}

