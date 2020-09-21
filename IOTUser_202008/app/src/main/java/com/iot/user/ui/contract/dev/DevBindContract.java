package com.iot.user.ui.contract.dev;

import com.iot.user.base.BaseView;
import com.iot.user.http.model.BaseResponse;

public interface DevBindContract {
    interface View extends BaseView {
        void onSuccess(BaseResponse bean, String type);
    }

    interface Presenter {
        /**
         * 绑定设备
         **/
        void postUnitDevBinder(String devNum);

        /**
         * 获取设备绑定信息
         **/
        void getDevBindInfo(String devNum);
    }
}
