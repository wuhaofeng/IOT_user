package com.iot.user.http.request.dev;

import com.iot.user.ui.model.dev.UnitDevEditModel;

public class UnitDevEditRequest {
    public UnitDevEditRequest() {
    }
    public UnitDevEditRequest(String uId, String token, UnitDevEditModel device) {
        body = new Body(uId, token, device);
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
        private UnitDevEditModel device;

        public Body(String uId, String token, UnitDevEditModel device) {
            this.uId = uId;
            this.token = token;
            this.device = device;
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

        public UnitDevEditModel getDevice() {
            return device;
        }

        public void setDevice(UnitDevEditModel device) {
            this.device = device;
        }
    }
}

