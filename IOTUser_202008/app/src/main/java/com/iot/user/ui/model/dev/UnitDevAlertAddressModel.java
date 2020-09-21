package com.iot.user.ui.model.dev;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class UnitDevAlertAddressModel implements Parcelable {
    public  UnitDevAlertAddressModel(){

    }
    private String addrName;
    private List<UnitDevAlertNodeModel> node;

    public String getAddrName() {
        return addrName;
    }

    public void setAddrName(String addrName) {
        this.addrName = addrName;
    }

    public List<UnitDevAlertNodeModel> getNode() {
        return node;
    }

    public void setNode(List<UnitDevAlertNodeModel> node) {
        this.node = node;
    }




    public UnitDevAlertAddressModel(Parcel in) {
        addrName = in.readString();
        node = in.createTypedArrayList(UnitDevAlertNodeModel.CREATOR);
    }

    public static final Creator<UnitDevAlertAddressModel> CREATOR = new Creator<UnitDevAlertAddressModel>() {

        @Override
        public UnitDevAlertAddressModel createFromParcel(Parcel source) {
            return new UnitDevAlertAddressModel(source);
        }

        @Override
        public UnitDevAlertAddressModel[] newArray(int size) {
            return new UnitDevAlertAddressModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(addrName);
        parcel.writeTypedList(node);
    }
}
