package com.iot.user.http.request.dev;

public class UnitDevDetailChartRequest {
    private Body body;

    public UnitDevDetailChartRequest() {

    }

    public UnitDevDetailChartRequest(String uId, String token, String devNum,String haddr,String daddr,String hour) {
        this.body = new Body(uId,token,devNum,haddr,daddr,hour);
    }

    class Body {
        private String uId;
        private String token;
        private String devNum;
        private String haddr;
        private String daddr;
        private String hour;
        public Body() {
        }

        public Body(String uId, String token, String devNum,String haddr,String daddr,String hour) {
            this.uId = uId;
            this.token = token;
            this.devNum = devNum;
            this.haddr = haddr;
            this.daddr = daddr;
            this.hour = hour;
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

        public String getHaddr() {
            return haddr;
        }

        public void setHaddr(String haddr) {
            this.haddr = haddr;
        }

        public String getDaddr() {
            return daddr;
        }

        public void setDaddr(String daddr) {
            this.daddr = daddr;
        }

        public String getHour() {
            return hour;
        }

        public void setHour(String hour) {
            this.hour = hour;
        }
    }
}

