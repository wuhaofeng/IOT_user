package com.iot.user.http.request.mine;

public class ChangePasswordsonRequest {

    private Body body;

    public ChangePasswordsonRequest() {

    }

    public ChangePasswordsonRequest(String uId,String token,String pwd,String np) {
        body = new Body(uId,token,pwd,np);
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    class Body{
        /*
            参数名	是否必填	说明
            oldpassword	是	旧密码（MD5加密）
            password	是	新密码（MD5加密）
        */
        private String uId;
        private String token;
        private String pwd;
        private String np;

        public Body() {
        }

        public Body(String uId, String token, String pwd, String np) {
            this.uId = uId;
            this.token = token;
            this.pwd = pwd;
            this.np = np;
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

        public String getPwd() {
            return pwd;
        }

        public void setPwd(String pwd) {
            this.pwd = pwd;
        }

        public String getNp() {
            return np;
        }

        public void setNp(String np) {
            this.np = np;
        }
    }
}

