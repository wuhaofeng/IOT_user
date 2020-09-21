package com.iot.user.http.request.mine;

public class FeedbackJsonRequest {
    private Body body;


    public FeedbackJsonRequest() {

    }

    public FeedbackJsonRequest(Body body) {
        this.body = body;
    }

    public FeedbackJsonRequest(String uId, String token, String score, String content) {
        this.body = new Body(uId, token, score, content);
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
        private String score;
        private String content;

        public Body(String uId, String token, String score, String content) {
            this.uId = uId;
            this.token = token;
            this.score = score;
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

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}

