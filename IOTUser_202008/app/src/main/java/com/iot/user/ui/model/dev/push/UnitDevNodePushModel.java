package com.iot.user.ui.model.dev.push;

import java.util.List;

public class UnitDevNodePushModel {
    public  UnitDevNodePushModel(){

    }
    private List<UnitDevNodeOnoffsModel> onoffs;
    public List<UnitDevNodeOnoffsModel> getOnoffs() {
        return onoffs;
    }

    public void setOnoffs(List<UnitDevNodeOnoffsModel> onoffs) {
        this.onoffs = onoffs;
    }


    public class UnitDevNodeOnoffsModel{
        public UnitDevNodeOnoffsModel(){

        }
        private String haddr;
        private List<UnitNodeOnoffsNodeModel> nodes;

        public String getHaddr() {
            return haddr;
        }

        public void setHaddr(String haddr) {
            this.haddr = haddr;
        }

        public List<UnitNodeOnoffsNodeModel> getNodes() {
            return nodes;
        }

        public void setNodes(List<UnitNodeOnoffsNodeModel> nodes) {
            this.nodes = nodes;
        }
    }
    public class UnitNodeOnoffsNodeModel{
        public UnitNodeOnoffsNodeModel(){

        }
        private String onoff;
        private String daddr;

        public String getDaddr() {
            return daddr;
        }

        public void setDaddr(String daddr) {
            this.daddr = daddr;
        }

        public String getOnoff() {
            return onoff;
        }

        public void setOnoff(String onoff) {
            this.onoff = onoff;
        }
    }
}

