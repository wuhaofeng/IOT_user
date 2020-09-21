package com.iot.user.ui.model.dev;

import java.util.List;

public class UnitDevDetailNodeModel  {
    public  UnitDevDetailNodeModel(){

    }
    private String daddr;
    private String dname;
    private String gasvalue;
    private String hnodeid;
    private String condStatus;
    private List<UnitNodeProductModel> nodem;
    private String status;/***1开启 2屏蔽**/
    private String type;/***1传感器 2中继器 3联动模块**/

    public String getDaddr() {
        return daddr;
    }

    public void setDaddr(String daddr) {
        this.daddr = daddr;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public String getHnodeid() {
        return hnodeid;
    }

    public void setHnodeid(String hnodeid) {
        this.hnodeid = hnodeid;
    }

    public List<UnitNodeProductModel> getNodem() {
        return nodem;
    }

    public void setNodem(List<UnitNodeProductModel> nodem) {
        this.nodem = nodem;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGasvalue() {
        return gasvalue;
    }

    public void setGasvalue(String gasvalue) {
        this.gasvalue = gasvalue;
    }

    public String getCondStatus() {
        return condStatus;
    }

    public void setCondStatus(String condStatus) {
        this.condStatus = condStatus;
    }
}
