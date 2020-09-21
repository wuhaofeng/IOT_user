package com.iot.user.ui.contract.message;

import com.iot.user.base.BaseView;
import com.iot.user.http.model.BaseResponse;

public interface UnitMessageContract {
    interface View extends BaseView {
        void onSuccess(BaseResponse bean, String type);
    }

    interface Presenter {
        /**刷新未读消息的数字**/
        void postUnitFamilyNewCount();
        /**刷新历史消息列表**/
        void refreshMessagezHistoryList(int pageIndex);
        /**获取历史消息详情**/
        void postMessagezHistoryDetail(String packRecodId);
        /**刷新家庭共享消息列表**/
        void refreshMessageFamilyList(int pageIndex);
        /**刷新设备共享消息列表**/
        void refreshMessageDevList(int pageIndex);
        /**同意家庭共享消息**/
        void postAgreeUnitMessageFamilyShare(int selectIndex);
        /**删除家庭共享消息**/
        void postUnitDeleteFamilyMessage(int selectIndex);
        /**删除设备共享消息**/
        void postUnitDeleteDevMessage(int selectIndex);

    }
}
