package com.iot.user.ui.contract.dev;

import com.iot.user.base.BaseView;
import com.iot.user.http.model.BaseResponse;

import java.util.List;

public interface DevMemberContract {
    interface View extends BaseView {
        void onSuccess(BaseResponse bean, String type);
    }

    interface Presenter {
        /**
         * 获取联系人列表
         **/
        void postUnitDevMemberList(String devNum);

        /**
         * 删除联系人
         **/
        void UnitMemberRemoveRequest(String devNum, List memberId);

        /**
         * 添加联系人
         **/
        void postUnitAddMemberList(String devNum,String memberName);
    }
}
