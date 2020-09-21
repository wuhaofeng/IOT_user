package com.iot.user.ui.contract.dev;

import com.iot.user.base.BaseView;
import com.iot.user.http.model.BaseResponse;

public interface DevListContract {
    interface View extends BaseView {
        void onSuccess(BaseResponse bean, String type);
    }

    interface Presenter {
        /**
         * 获取设备列表
         **/
        void postUnitDevList(int indexPage,String isOnline,String devStatus);

        /**
         * 获取报警设备列表
         **/
        void postUnitAlertDevList(int indexPage);
    }
}
