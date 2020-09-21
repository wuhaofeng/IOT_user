package com.iot.user.ui.contract.mine;

import com.iot.user.base.BaseView;
import com.iot.user.http.model.BaseResponse;

public interface UnitMineContract {
    interface View extends BaseView {
        void onSuccess(BaseResponse bean, String type);
    }

    interface Presenter {
        /**
         * 获取用户信息
         **/
        void postUnitMineUserInfo();
    }
}
