package com.iot.user.ui.model.dev.push;

import java.util.List;

public class UnitDevNodeProPushModel {
    public  UnitDevNodeProPushModel(){

    }
    private String devnum;
    private List<UnitNodeProOnoffsModel> onoffs;

    public class UnitNodeProOnoffsModel{
        public UnitNodeProOnoffsModel(){

        }
        private String haddr;
        private List<UnitProOnoffsNodeModel> nodes;

        public String getHaddr() {
            return haddr;
        }

        public void setHaddr(String haddr) {
            this.haddr = haddr;
        }

        public List<UnitProOnoffsNodeModel> getNodes() {
            return nodes;
        }

        public void setNodes(List<UnitProOnoffsNodeModel> nodes) {
            this.nodes = nodes;
        }
    }
    public class UnitProOnoffsNodeModel{
        public UnitProOnoffsNodeModel(){

        }
        private String type;
        private String daddr;
        private List<UnitOnoffsNodeChannelModel> channels;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDaddr() {
            return daddr;
        }

        public void setDaddr(String daddr) {
            this.daddr = daddr;
        }

        public List<UnitOnoffsNodeChannelModel> getChannels() {
            return channels;
        }

        public void setChannels(List<UnitOnoffsNodeChannelModel> channels) {
            this.channels = channels;
        }
    }

    public class UnitOnoffsNodeChannelModel{
        public UnitOnoffsNodeChannelModel(){

        }
        private String caddr;
        private String onoff;

        public String getCaddr() {
            return caddr;
        }

        public void setCaddr(String caddr) {
            this.caddr = caddr;
        }

        public String getOnoff() {
            return onoff;
        }

        public void setOnoff(String onoff) {
            this.onoff = onoff;
        }
    }

    public String getDevnum() {
        return devnum;
    }

    public void setDevnum(String devnum) {
        this.devnum = devnum;
    }

    public List<UnitNodeProOnoffsModel> getOnoffs() {
        return onoffs;
    }

    public void setOnoffs(List<UnitNodeProOnoffsModel> onoffs) {
        this.onoffs = onoffs;
    }
}

