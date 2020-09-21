package com.iot.user.http.request.alert;

public class UnitDevCloseVoiceRequest {
    public UnitDevCloseVoiceRequest(){
    }
    public UnitDevCloseVoiceRequest(String uId, String token,String devnum, String mute, String msgnum) {
        this.body = new Body(uId, token,devnum, mute,msgnum);
    }
    private Body body;

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    class Body {
        private String devnum;
        private String mute;
        private String token;
        private String uId;
        private String msgnum;

        public Body(String uId, String token, String devnum, String mute,String msgnum) {
            this.devnum = devnum;
            this.mute = mute;
            this.token = token;
            this.uId = uId;
            this.msgnum=msgnum;
        }

        public String getDevnum() {
            return devnum;
        }

        public void setDevnum(String devnum) {
            this.devnum = devnum;
        }

        public String getMute() {
            return mute;
        }

        public void setMute(String mute) {
            this.mute = mute;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getuId() {
            return uId;
        }

        public void setuId(String uId) {
            this.uId = uId;
        }

        public String getMsgnum() {
            return msgnum;
        }

        public void setMsgnum(String msgnum) {
            this.msgnum = msgnum;
        }
    }
}

