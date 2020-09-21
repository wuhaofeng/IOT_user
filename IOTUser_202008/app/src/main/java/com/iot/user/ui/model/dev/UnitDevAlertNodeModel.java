package com.iot.user.ui.model.dev;

import android.os.Parcel;
import android.os.Parcelable;

public class UnitDevAlertNodeModel implements Parcelable {
    public UnitDevAlertNodeModel(){

    }
    private String alarmdesc;
    private String alarmnum;
    private String faultdesc;
    private String faultnum;
    private String gasvalue;
    private String nodeName;
    private String status;

    public String getAlarmdesc() {
        return alarmdesc;
    }

    public void setAlarmdesc(String alarmdesc) {
        this.alarmdesc = alarmdesc;
    }

    public String getAlarmnum() {
        return alarmnum;
    }

    public void setAlarmnum(String alarmnum) {
        this.alarmnum = alarmnum;
    }

    public String getFaultdesc() {
        return faultdesc;
    }

    public void setFaultdesc(String faultdesc) {
        this.faultdesc = faultdesc;
    }

    public String getFaultnum() {
        return faultnum;
    }

    public void setFaultnum(String faultnum) {
        this.faultnum = faultnum;
    }

    public String getGasvalue() {
        return gasvalue;
    }

    public void setGasvalue(String gasvalue) {
        this.gasvalue = gasvalue;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public UnitDevAlertNodeModel(Parcel in) {
        alarmdesc = in.readString();
        alarmnum = in.readString();
        faultdesc = in.readString();
        faultnum = in.readString();
        gasvalue = in.readString();
        nodeName = in.readString();
        status = in.readString();
    }

    public static final Parcelable.Creator<UnitDevAlertNodeModel> CREATOR = new Parcelable.Creator<UnitDevAlertNodeModel>() {

        @Override
        public UnitDevAlertNodeModel createFromParcel(Parcel source) {
            return new UnitDevAlertNodeModel(source);
        }

        @Override
        public UnitDevAlertNodeModel[] newArray(int size) {
            return new UnitDevAlertNodeModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(alarmdesc);
        parcel.writeString(alarmnum);
        parcel.writeString(faultdesc);
        parcel.writeString(faultnum);
        parcel.writeString(gasvalue);
        parcel.writeString(nodeName);
        parcel.writeString(status);
    }
}
