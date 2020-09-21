package com.iot.user.ui.model.login;

public class LoginModel {
    public LoginModel(){

    }
    public String uId;
    public String token;
    public String nickName;
    public String username;
    public String phone;
    public String grade;//设备名称
    public String expire_time;//设备友好名
    public String push_mark;
    public String sms_mark;//房间
    public String phone_mark;

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
