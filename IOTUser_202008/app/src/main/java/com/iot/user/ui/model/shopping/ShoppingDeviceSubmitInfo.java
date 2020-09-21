package com.iot.user.ui.model.shopping;

import java.util.List;

public class ShoppingDeviceSubmitInfo {
    List<ShoppingDeviceSelectListModel> accountDetail;
    public List<ShoppingDeviceSelectListModel> getAccountDetail() {
        return accountDetail;
    }
    public void setAccountDetail(List<ShoppingDeviceSelectListModel> accountDetail) {
        this.accountDetail = accountDetail;
    }
}
