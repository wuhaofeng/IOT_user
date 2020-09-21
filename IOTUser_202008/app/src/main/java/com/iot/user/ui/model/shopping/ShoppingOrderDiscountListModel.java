package com.iot.user.ui.model.shopping;

import java.util.List;

public class ShoppingOrderDiscountListModel {
    private List< ShoppingOrderDiscountModel> discount;
    public static class ShoppingOrderDiscountModel{
        private String dev_type;
        private String mark;
        private String renew_year;
        private String discount;

        public String getDev_type() {
            return dev_type;
        }

        public void setDev_type(String dev_type) {
            this.dev_type = dev_type;
        }

        public String getMark() {
            return mark;
        }

        public void setMark(String mark) {
            this.mark = mark;
        }

        public String getRenew_year() {
            return renew_year;
        }

        public void setRenew_year(String renew_year) {
            this.renew_year = renew_year;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }
    }

    public List<ShoppingOrderDiscountModel> getDiscount() {
        return discount;
    }

    public void setDiscount(List<ShoppingOrderDiscountModel> discount) {
        this.discount = discount;
    }
}

