package com.iot.user.http.request.mine;

public class EditUserInfoJsonRequest {

    private Body body;

    public EditUserInfoJsonRequest() {

    }

    public EditUserInfoJsonRequest(String uId, String token, String username, String phone, String nickname) {
        this.body = new Body(uId, token, username, phone, nickname);
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    class Body {
        /*"uId":"用户ID",
                "token":"TokenId",
                "username":"用户名",
                "phone":"手机号",
                "user"：{
            "nickname":"昵称"
        }*/

        private String uId;
        private String token;
        private String username;
        private String phone;
        private User user;

        public Body() {
        }

        public Body(String uId, String token, String username, String phone, String nickname) {
            this.uId = uId;
            this.token = token;
            this.username = username;
            this.phone = phone;
            this.user = new User(nickname);
        }
    }

    class User{
        private String nickname;

        public User(String nickname) {
            this.nickname = nickname;
        }

        public User() {
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }
    }

}

