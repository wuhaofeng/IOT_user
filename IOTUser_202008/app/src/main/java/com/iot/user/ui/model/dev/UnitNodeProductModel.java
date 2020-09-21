package com.iot.user.ui.model.dev;

public class UnitNodeProductModel {
    public  UnitNodeProductModel(){

    }
    private String maddr;
    private String nodemid;
    private String status;/***联动模块状态（Integer 0关闭 1开启）**/
    private String type;/***联动模块类型（Integer 1风机 2电磁阀 3报警灯）**/
    private String nodeID;
    private String nodeAddr;
    public String getMaddr() {
        return maddr;
    }

    public void setMaddr(String maddr) {
        this.maddr = maddr;
    }

    public String getNodemid() {
        return nodemid;
    }

    public void setNodemid(String nodemid) {
        this.nodemid = nodemid;
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

    public String getNodeID() {
        return nodeID;
    }

    public void setNodeID(String nodeID) {
        this.nodeID = nodeID;
    }

    public String getNodeAddr() {
        return nodeAddr;
    }

    public void setNodeAddr(String nodeAddr) {
        this.nodeAddr = nodeAddr;
    }
}
