package com.iot.user.ui.model.message;

public class ReatineInfoMap {
    private String reatineTitle;
    private String reatineContent;

    public ReatineInfoMap(String reatineTitle, String reatineContent) {
        this.reatineTitle = reatineTitle;
        this.reatineContent = reatineContent;
    }

    public ReatineInfoMap() {
    }

    public String getReatineTitle() {
        return reatineTitle;
    }

    public void setReatineTitle(String reatineTitle) {
        this.reatineTitle = reatineTitle;
    }

    public String getReatineContent() {
        return reatineContent;
    }

    public void setReatineContent(String reatineContent) {
        this.reatineContent = reatineContent;
    }
}
