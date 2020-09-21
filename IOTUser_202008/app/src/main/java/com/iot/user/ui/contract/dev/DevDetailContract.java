package com.iot.user.ui.contract.dev;

import com.iot.user.base.BaseView;
import com.iot.user.http.model.BaseResponse;

public interface DevDetailContract {
    interface View extends BaseView {
        void onSuccess(BaseResponse bean, String type);
        void successToJump(int errorCode);
    }

    interface Presenter {
        /**
         * 获取192设备操作权限
         **/
        void postUnitDevPermission(String devNum);

        /**
         * 获取设备信息
         **/
        void getDevInfoWithDevNum(String devNum);
        /**
         * 解绑并注销
         **/
        void postUnitDevUnBinder(String devNum);
        /**
         * 取消关注
         **/
        void postUnitDevUnAttention(String devNum);
        /**
         * 判断设备是否注册
         */
        void isDevReg_V902(String devNum);
        /**
         * 182屏蔽设备
         */
        void clickDevClicketCloseBtn(String devNum);
        /**
         * 消音和我知道了  消音mute 2 我知道了mute 1
         */
        void dealUnitDev(String devNum, String status, String msgNum);
    }
}
