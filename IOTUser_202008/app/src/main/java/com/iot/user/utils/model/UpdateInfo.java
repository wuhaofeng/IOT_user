package com.iot.user.utils.model;

import java.io.Serializable;

/* "body": {
        "description": "升级内容",
        "isForceUpdate": "是否强制更新，1-是，0-否",
        "type": "app的类型，1-客户端，2-运维端",
        "url": "下载url路径",
        "vname": "版本名",
        "vnum": "版本号",
        "publish_time": 1566954644000,
    },
    */
public class UpdateInfo implements Serializable{

    private String description;
    private String isForceUpdate;
    private String type;
    private String url;
    private String vname;
    private String vnum;
    private String md5;
    private long publish_time;

    public UpdateInfo() {
    }

    public UpdateInfo(String description, String isForceUpdate, String type, String url, String vname, String vnum, long publish_time, String md5) {
        this.description = description;
        this.isForceUpdate = isForceUpdate;
        this.type = type;
        this.url = url;
        this.vname = vname;
        this.vnum = vnum;
        this.md5 = md5;
        this.publish_time = publish_time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIsForceUpdate() {
        return isForceUpdate;
    }

    public void setIsForceUpdate(String isForceUpdate) {
        this.isForceUpdate = isForceUpdate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVname() {
        return vname;
    }

    public void setVname(String vname) {
        this.vname = vname;
    }

    public String getVnum() {
        return vnum;
    }

    public void setVnum(String vnum) {
        this.vnum = vnum;
    }

    public long getPublish_time() {
        return publish_time;
    }

    public void setPublish_time(long publish_time) {
        this.publish_time = publish_time;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }
}
