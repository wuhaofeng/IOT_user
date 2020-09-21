package com.iot.user.ui.contract.main;

import com.iot.user.base.BaseView;
import com.iot.user.http.model.BaseResponse;

public interface UnitMainContract {
    interface View extends BaseView {
        void onSuccess(BaseResponse bean, String type);
    }

    interface Presenter {
        /**
         * 2、绑定个推cid和华为token
         */
        void pluginPush(String hwToken);
        /**刷新未读消息的数字**/
        void getFamilyShareCount();
    }
}
