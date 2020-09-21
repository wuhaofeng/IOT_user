package com.iot.user.http.request.notification;

public class DeleteNotificationRequest {
    public DeleteNotificationRequest(){
        this.body= new Body();
    }

    public DeleteNotificationRequest(String uId, String token, String notice_id) {
        this.body = new Body(uId, token, notice_id);
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
        private String notice_id;

        public Body(String uId, String token, String notice_id) {
            this.uId = uId;
            this.token = token;
            this.notice_id = notice_id;
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

        public String getNotice_id() {
            return notice_id;
        }

        public void setNotice_id(String notice_id) {
            this.notice_id = notice_id;
        }
    }
}

