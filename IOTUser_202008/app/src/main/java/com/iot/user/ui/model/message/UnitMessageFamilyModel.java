package com.iot.user.ui.model.message;

public class UnitMessageFamilyModel {
    public  UnitMessageFamilyModel(){

    }
    private String user_id;
    private String news;
    private String type;//1发送方 2接收方
    private String news_status;//1代表等待接受 2已同意 3已失效
    private String dev_type_name;//信息描述
    private String create_time;//上报时毫秒级时间戳
    private String flag;//1 未删除 2已删除
    /*家庭共享*/
    private String family_id;//信息描述
    private String family_shared_news_id;//信息描述
    /*设备共享*/
    private String dev_shared_news_id;//信息描述
    private String devnum;//信息描述

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getNews() {
        return news;
    }

    public void setNews(String news) {
        this.news = news;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNews_status() {
        return news_status;
    }

    public void setNews_status(String news_status) {
        this.news_status = news_status;
    }

    public String getDev_type_name() {
        return dev_type_name;
    }

    public void setDev_type_name(String dev_type_name) {
        this.dev_type_name = dev_type_name;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getFamily_id() {
        return family_id;
    }

    public void setFamily_id(String family_id) {
        this.family_id = family_id;
    }

    public String getFamily_shared_news_id() {
        return family_shared_news_id;
    }

    public void setFamily_shared_news_id(String family_shared_news_id) {
        this.family_shared_news_id = family_shared_news_id;
    }

    public String getDev_shared_news_id() {
        return dev_shared_news_id;
    }

    public void setDev_shared_news_id(String dev_shared_news_id) {
        this.dev_shared_news_id = dev_shared_news_id;
    }

    public String getDevnum() {
        return devnum;
    }

    public void setDevnum(String devnum) {
        this.devnum = devnum;
    }
}

