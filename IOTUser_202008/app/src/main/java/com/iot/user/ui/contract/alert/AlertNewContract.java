package com.iot.user.ui.contract.alert;

import com.iot.user.base.BaseView;
import com.iot.user.http.model.BaseResponse;

public interface AlertNewContract {
    interface View extends BaseView {
        void onSuccess(BaseResponse bean, String type);
    }

    interface Presenter {
        /**
         * 182消音 10000消音 11000我知道了
         */
        void dealDev(String devNum, String status);
        /**
         * 122消音 2消音 1我知道了
         */
        void dealUnitDev(String devNum, String status,String msgNum);
    }
}
