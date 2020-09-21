package com.iot.user.ui.contract.notification;

import com.iot.user.base.BaseView;
import com.iot.user.http.model.BaseResponse;

public interface UnitNotificationContract {
    interface View extends BaseView {
        void onSuccess(BaseResponse bean, String type);
    }

    interface Presenter {
        /**
         * 获取所有公告
         **/
        void getUnitNotificationAll(int pageIndex);
        /**
         * 获取已读公告
         **/
        void getUnitNotificationRead(int pageIndex);
        /**
         * 获取未读公告
         **/
        void getUnitNotificationUnread(int pageIndex);

        /**
         * 设置已读
         **/
        void setReadNotice(String noticeId);
    }
}
