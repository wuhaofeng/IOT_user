package com.iot.user.ui.model.message;

import android.os.Parcel;
import android.os.Parcelable;

import com.iot.user.ui.model.dev.UnitDevAlertAddressModel;

import java.util.List;

public class UnitDevAlertDescModel implements Parcelable {
    public  UnitDevAlertDescModel(){

    }
    private String cfaultdesc;
    private List<UnitDevAlertAddressModel> alertAddress;
    private String cfaultnum;
    private String devnum;
    private String title;

    public String getCfaultdesc() {
        return cfaultdesc;
    }

    public void setCfaultdesc(String cfaultdesc) {
        this.cfaultdesc = cfaultdesc;
    }

    public List<UnitDevAlertAddressModel> getAlertAddress() {
        return alertAddress;
    }

    public void setAlertAddress(List<UnitDevAlertAddressModel> alertAddress) {
        this.alertAddress = alertAddress;
    }

    public String getCfaultnum() {
        return cfaultnum;
    }

    public void setCfaultnum(String cfaultnum) {
        this.cfaultnum = cfaultnum;
    }

    public String getDevnum() {
        return devnum;
    }

    public void setDevnum(String devnum) {
        this.devnum = devnum;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public UnitDevAlertDescModel(Parcel in){
        cfaultdesc=in.readString();
        alertAddress=in.createTypedArrayList(UnitDevAlertAddressModel.CREATOR);
        cfaultnum=in.readString();
        devnum=in.readString();
        title=in.readString();
    }
    public static final Creator<UnitDevAlertDescModel> CREATOR = new Creator<UnitDevAlertDescModel>() {

        @Override
        public UnitDevAlertDescModel createFromParcel(Parcel source) {
            return new UnitDevAlertDescModel(source);
        }

        @Override
        public UnitDevAlertDescModel[] newArray(int size) {
            return new UnitDevAlertDescModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(cfaultdesc);
        parcel.writeTypedList(alertAddress);
        parcel.writeString(cfaultnum);
        parcel.writeString(devnum);
        parcel.writeString(title);
    }
}
