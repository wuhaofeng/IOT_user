package com.iot.user.ui.contract.shopping;

import com.iot.user.base.BaseView;
import com.iot.user.http.model.BaseResponse;

public interface ShoppingOrderSubmitContract {
    interface View extends BaseView {
        void onSuccess(BaseResponse bean, String type);
        void onPay(Object bean, String type);
    }

    interface Presenter {
        /**
         * 创建订单
         **/
        void postCreateOrder();
        /**支付宝支付***/
        void goApiPay();
        /**
         * 微信支付*
         * **/
        void goWeiChatPay();
    }
}
