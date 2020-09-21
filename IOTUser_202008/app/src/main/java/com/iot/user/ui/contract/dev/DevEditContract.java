package com.iot.user.ui.contract.dev;

import com.iot.user.base.BaseView;
import com.iot.user.http.model.BaseResponse;
import com.iot.user.ui.model.dev.UnitDevEditModel;

public interface DevEditContract {
    interface View extends BaseView {
        void onSuccess(BaseResponse bean, String type);
    }
    interface Presenter {
        /**
         * 获取设备信息
         **/
        void postUnitDevInfo(String devNum);
        /**
         * 修改设备信息并绑定
         **/
        void updateAndBindDev(Double lat, Double lon,String address,String province,String city
                ,String district,String street,String devName,String devNo);
        /**
         * 编辑设备信息
         **/
        void postUnitDevEdit(UnitDevEditModel reqModel);
        /**
         * 设备转移场所
         **/
        void removePlace(String devNum,String originRoomID,String currRoomID);
    }
}
