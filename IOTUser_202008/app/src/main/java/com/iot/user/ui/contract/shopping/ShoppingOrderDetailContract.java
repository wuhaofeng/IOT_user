package com.iot.user.ui.contract.shopping;

import com.iot.user.base.BaseView;
import com.iot.user.http.model.BaseResponse;

public interface ShoppingOrderDetailContract {
    interface View extends BaseView {
        void onSuccess(BaseResponse bean, String type);
        void onPay(Object bean, String type);
    }

    interface Presenter {
        /**
         * 取消订单
         **/
        void postCancelOrder(String tradeNo);
        /**
         * 获取订单详情
         **/
        void postNewOrderDetail(String tradeNo);
        /**支付宝支付***/
        void goApiPay(String tradeNo);
        /**
         * 微信支付*
         * **/
        void goWeiChatPay(String tradeNo);
    }
}
