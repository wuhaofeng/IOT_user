package com.iot.user.ui.model.shopping;

import android.os.Parcel;
import android.os.Parcelable;

public class ShoppingDeviceSelectListModel implements Parcelable{
    private String price;
    private String mark;
    private String renew_year;
    private String discount;
    private int dev_type;

    private String address;
    private String executeTime;
    private String expireTime;

    private String send_year;
    private String total_fee;
    private String product_id;
    private String name;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getRenew_year() {
        return renew_year;
    }

    public void setRenew_year(String renew_year) {
        this.renew_year = renew_year;
    }

    public int getDev_type() {
        return dev_type;
    }

    public void setDev_type(int dev_type) {
        this.dev_type = dev_type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(String executeTime) {
        this.executeTime = executeTime;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    public String getSend_year() {
        return send_year;
    }

    public void setSend_year(String send_year) {
        this.send_year = send_year;
    }

    public String getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(String total_fee) {
        this.total_fee = total_fee;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }
    public  ShoppingDeviceSelectListModel(){

    }

    public ShoppingDeviceSelectListModel(Parcel in) {
        price = in.readString();
        mark = in.readString();
        renew_year = in.readString();
        discount = in.readString();
        address = in.readString();
        executeTime = in.readString();
        expireTime = in.readString();
        send_year = in.readString();
        total_fee = in.readString();
        product_id = in.readString();
        name = in.readString();
        dev_type = in.readInt();
    }
    public static final Parcelable.Creator<ShoppingDeviceSelectListModel> CREATOR = new Parcelable.Creator<ShoppingDeviceSelectListModel>() {
        @Override
        public ShoppingDeviceSelectListModel createFromParcel(Parcel in) {
            return new ShoppingDeviceSelectListModel(in);
        }

        @Override
        public ShoppingDeviceSelectListModel[] newArray(int size) {
            return new ShoppingDeviceSelectListModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(price);
        parcel.writeString(mark);
        parcel.writeString(renew_year);
        parcel.writeString(discount);
        parcel.writeString(address);
        parcel.writeString(executeTime);
        parcel.writeString(expireTime);
        parcel.writeString(send_year);
        parcel.writeString(total_fee);
        parcel.writeString(product_id);
        parcel.writeString(name);
        parcel.writeInt(dev_type);
    }
}

