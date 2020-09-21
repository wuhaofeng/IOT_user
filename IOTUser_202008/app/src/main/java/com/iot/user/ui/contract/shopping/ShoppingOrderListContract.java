package com.iot.user.ui.contract.shopping;

import com.iot.user.base.BaseView;
import com.iot.user.http.model.BaseResponse;

public interface ShoppingOrderListContract {
    interface View extends BaseView {
        void onSuccess(BaseResponse bean, String type);
    }

    interface Presenter {
        /**
         * 获取订单列表
         **/
        void getShoppingOrderList(int pageIndex,int status);
    }
}
