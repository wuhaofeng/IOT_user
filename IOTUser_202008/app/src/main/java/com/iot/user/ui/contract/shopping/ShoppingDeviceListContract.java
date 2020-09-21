package com.iot.user.ui.contract.shopping;

import com.iot.user.base.BaseView;
import com.iot.user.http.model.BaseResponse;
import com.iot.user.ui.model.shopping.ShoppingDeviceListModel;
import com.iot.user.ui.model.shopping.ShoppingDeviceSelectListModel;
import com.iot.user.ui.model.shopping.ShoppingOrderDiscountListModel;

public interface ShoppingDeviceListContract {
    interface View extends BaseView {
        void onSuccess(BaseResponse bean, String type);
        void onSuccessSelect(BaseResponse bean, Object selectInfo,String type);
    }

    interface Presenter {
        /**
         * 获取续费设备列表
         **/
        void postShoppingDeviceList(int pageIndex);
        /**获取折扣开关***/
        void getOrderSwitch();
        /**
         * 选择年份的弹窗*
         * **/
        void postOrderDiscountList(int selectIndex,String type);/**type为0的时候是在全部的续费设备中选择年份，为1则是在已选中的设备中选择年份**/
        /**
         * 去结算
         * **/
        void postOrderAccountDetail();
    }
}
