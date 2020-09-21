package com.iot.user.ui.model.mine;

public class VersionJsonModel {
    private String httpServerMark;
    private String httpUpdateTime;
    private String httpName;
    private String version;
    private String httpIp;
    public String toString() {
        return "httpServerMark:"+httpServerMark+",httpUpdateTime:"+httpUpdateTime
                +",httpName:"+httpName+",version:"+version+",httpIp:"+httpIp;
    }
    public String getHttpServerMark() {
        return httpServerMark;
    }

    public void setHttpServerMark(String httpServerMark) {
        this.httpServerMark = httpServerMark;
    }

    public String getHttpUpdateTime() {
        return httpUpdateTime;
    }

    public void setHttpUpdateTime(String httpUpdateTime) {
        this.httpUpdateTime = httpUpdateTime;
    }

    public String getHttpName() {
        return httpName;
    }

    public void setHttpName(String httpName) {
        this.httpName = httpName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getHttpIp() {
        return httpIp;
    }

    public void setHttpIp(String httpIp) {
        this.httpIp = httpIp;
    }
}
