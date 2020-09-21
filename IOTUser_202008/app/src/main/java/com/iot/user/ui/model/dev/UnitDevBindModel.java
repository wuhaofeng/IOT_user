package com.iot.user.ui.model.dev;

public class UnitDevBindModel {
    private String title;
    private String context;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
    public String toString() {
        return "ArraylistBean{" +
                "title='" + title + '\'' +
                ", context='" + context + '\'' +
                '}';
    }
}
