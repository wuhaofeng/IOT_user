package com.iot.user.ui.model.notification;

import java.io.Serializable;

public class NoticeContentModel implements Serializable {
    /*"notice_id":"18",
        "notice_time":1557466173000, //公告时间
        "publish_text":"22发布的内容",//公告内容
        "publish_title":"22标题",	//公告标题
        "publish_type":4,		 //告你类型
        "readed":0,				//是否已读  0 为未读  1为已读
        "timeout":1				//超时天数*/
    private String notice_id;
    private long notice_time;
    private String publish_text;
    private String publish_title;
    private int publish_type;
    private int readed;
    private int timeout;
    private String nickName;

    private int nodata = 0;

    public NoticeContentModel() {
    }

    public NoticeContentModel(String notice_id, long notice_time, String publish_text, String publish_title, int publish_type, int readed, int timeout,String nickName) {
        this.notice_id = notice_id;
        this.notice_time = notice_time;
        this.publish_text = publish_text;
        this.publish_title = publish_title;
        this.publish_type = publish_type;
        this.readed = readed;
        this.timeout = timeout;
        this.nickName = nickName;
    }

    public String getNotice_id() {
        return notice_id;
    }

    public void setNotice_id(String notice_id) {
        this.notice_id = notice_id;
    }

    public long getNotice_time() {
        return notice_time;
    }

    public void setNotice_time(long notice_time) {
        this.notice_time = notice_time;
    }

    public String getPublish_text() {
        return publish_text;
    }

    public void setPublish_text(String publish_text) {
        this.publish_text = publish_text;
    }

    public String getPublish_title() {
        return publish_title;
    }

    public void setPublish_title(String publish_title) {
        this.publish_title = publish_title;
    }

    public int getPublish_type() {
        return publish_type;
    }

    public void setPublish_type(int publish_type) {
        this.publish_type = publish_type;
    }

    public int getReaded() {
        return readed;
    }

    public void setReaded(int readed) {
        this.readed = readed;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getNodata() {
        return nodata;
    }

    public void setNodata(int nodata) {
        this.nodata = nodata;
    }

}
