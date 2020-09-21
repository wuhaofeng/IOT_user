package com.iot.user.ui.contract.share;

import com.iot.user.base.BaseView;
import com.iot.user.http.model.BaseResponse;

public interface DevShareContract {
    interface View extends BaseView {
        void onSuccess(BaseResponse bean, String type);
    }
    interface Presenter {
        /**
         * 获取设备共享的设备列表
         **/
        void postUnitDevShareList(int pageIndex);
    }
}
