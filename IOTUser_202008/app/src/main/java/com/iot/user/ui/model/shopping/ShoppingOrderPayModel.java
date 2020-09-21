package com.iot.user.ui.model.shopping;

public class ShoppingOrderPayModel {
    public ShoppingOrderPayModel(int icon,String title,String content,boolean isSelect){
        this.icon=icon;
        this.title=title;
        this.isSelect=isSelect;
        this.content=content;
    }
    private int icon;
    private String title;
    private String content;
    private boolean isSelect;

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

