package com.iot.user.ui.contract.share;

import com.iot.user.base.BaseView;
import com.iot.user.http.model.BaseResponse;

import java.util.List;

public interface FamilyShareContract {
    interface View extends BaseView {
        void onSuccess(BaseResponse bean, String type);
    }

    interface Presenter {
        /**
         * 获取共享的成员信息
         **/
        void postUnitMemberInfo(String memberStr);

        /**
         * 家庭共享
         **/
        void postUnitFamilyShare(String devNum, String familyId);

        /**
         * 成员共享
         **/
        void postUnitAddMemberList(String devNum, String memberStr);
    }
}
