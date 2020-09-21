package com.iot.user.ui.model.register;

public class RegisterModel {
    private String uId;
    private String token;
    private String nickName;
    private String username;
    private String phone;
    private String grade;
    private String expire_time;
    private String push_mark;
    private String sms_mark;
    private String phone_mark;

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

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getExpire_time() {
        return expire_time;
    }

    public void setExpire_time(String expire_time) {
        this.expire_time = expire_time;
    }

    public String getPush_mark() {
        return push_mark;
    }

    public void setPush_mark(String push_mark) {
        this.push_mark = push_mark;
    }

    public String getSms_mark() {
        return sms_mark;
    }

    public void setSms_mark(String sms_mark) {
        this.sms_mark = sms_mark;
    }

    public String getPhone_mark() {
        return phone_mark;
    }

    public void setPhone_mark(String phone_mark) {
        this.phone_mark = phone_mark;
    }
}
