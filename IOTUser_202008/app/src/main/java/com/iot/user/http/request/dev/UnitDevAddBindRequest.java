package com.iot.user.http.request.dev;

public class UnitDevAddBindRequest {
    public UnitDevAddBindRequest() {
    }

    public UnitDevAddBindRequest(String uId, String token,
                                 double lat, double lon, String address,
                                 String area_Name_1, String area_Name_2,
                                 String area_Name_3, String area_Name_4,
                                 String friend_name, String devnum, String siteId) {
        this.body = new Body(uId,token,lat,lon,address,area_Name_1,area_Name_2,area_Name_3,area_Name_4,friend_name,devnum,siteId);
    }
    private Body body;

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    class Body {
        String uId;
        String token;
        double Lat;
        double Lon;
        String address;
        private String area_Name_1;
        private String area_Name_2;
        private String area_Name_3;
        private String area_Name_4;
        String friend_name;
        String devnum;
        String siteId;//场所名
        public Body(String uId, String token,
                    double lat, double lon, String address,
                    String area_Name_1, String area_Name_2,
                    String area_Name_3, String area_Name_4,
                    String friend_name, String devnum, String siteId) {
            this.uId = uId;
            this.token = token;
            Lat = lat;
            Lon = lon;
            this.address = address;
            this.area_Name_1 = area_Name_1;
            this.area_Name_2 = area_Name_2;
            this.area_Name_3 = area_Name_3;
            this.area_Name_4 = area_Name_4;
            this.friend_name = friend_name;
            this.devnum = devnum;
            this.siteId = siteId;
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

        public double getLat() {
            return Lat;
        }

        public void setLat(double lat) {
            Lat = lat;
        }

        public double getLon() {
            return Lon;
        }

        public void setLon(double lon) {
            Lon = lon;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getArea_Name_1() {
            return area_Name_1;
        }

        public void setArea_Name_1(String area_Name_1) {
            this.area_Name_1 = area_Name_1;
        }

        public String getArea_Name_2() {
            return area_Name_2;
        }

        public void setArea_Name_2(String area_Name_2) {
            this.area_Name_2 = area_Name_2;
        }

        public String getArea_Name_3() {
            return area_Name_3;
        }

        public void setArea_Name_3(String area_Name_3) {
            this.area_Name_3 = area_Name_3;
        }

        public String getArea_Name_4() {
            return area_Name_4;
        }

        public void setArea_Name_4(String area_Name_4) {
            this.area_Name_4 = area_Name_4;
        }

        public String getFriend_name() {
            return friend_name;
        }

        public void setFriend_name(String friend_name) {
            this.friend_name = friend_name;
        }

        public String getDevnum() {
            return devnum;
        }

        public void setDevnum(String devnum) {
            this.devnum = devnum;
        }

        public String getSiteId() {
            return siteId;
        }

        public void setSiteId(String siteId) {
            this.siteId = siteId;
        }
    }
}

