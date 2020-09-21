package com.iot.user.ui.contract.dev;

import com.iot.user.base.BaseView;
import com.iot.user.http.model.BaseResponse;

public interface DevRegisterContract {
    interface View extends BaseView {
        void onSuccess(BaseResponse bean, String type);
        void successToJump(int errorCode,String errorMsg);
    }
    interface Presenter {
        /**
         * 注册并绑定设备
         **/
        void registerAndBindDev(Double lat, Double lon,String address,String province,String city
                ,String district,String street,String devName,String devNo);
        /**
         * 修改设备信息并绑定
         **/
        void updateAndBindDev(Double lat, Double lon,String address,String province,String city
                ,String district,String street,String devName,String devNo);
        /**
         * 获取设备注册信息
         **/
        void isDevReg_V902(String devNum);
        /**
         * 获取房间列表数据
         **/
        void postUnitFamilyRoomList();
    }
}