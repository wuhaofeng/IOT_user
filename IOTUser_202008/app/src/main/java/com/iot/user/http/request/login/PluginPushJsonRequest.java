package com.iot.user.http.request.login;

public class PluginPushJsonRequest {
    private Body body;

    public PluginPushJsonRequest() {

    }

    public PluginPushJsonRequest(String uId, String token, String push_id, String getui_id) {
        this.body = new Body(uId,token,push_id,getui_id);
    }

    class Body{
        private String uId;
        private String token;
        private String push_id;//华为推送id
        private String getui_id;//个推cid

        public Body() {
        }

        public Body(String uId, String token, String push_id, String getui_id) {
            this.uId = uId;
            this.token = token;
            this.push_id = push_id;
            this.getui_id = getui_id;
        }
    }
}

