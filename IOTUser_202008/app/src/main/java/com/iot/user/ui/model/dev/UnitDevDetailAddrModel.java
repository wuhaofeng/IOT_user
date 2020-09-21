package com.iot.user.ui.model.dev;

import java.io.Serializable;

public class UnitDevDetailAddrModel implements Serializable {
    public  UnitDevDetailAddrModel(){

    }
    private String haddr;
    private String hname;
    private String devHid;
    private String status;/**1开启 2屏蔽**/


    public String getHaddr() {
        return haddr;
    }

    public void setHaddr(String haddr) {
        this.haddr = haddr;
    }

    public String getHname() {
        return hname;
    }

    public void setHname(String hname) {
        this.hname = hname;
    }

    public String getDevHid() {
        return devHid;
    }

    public void setDevHid(String devHid) {
        this.devHid = devHid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
