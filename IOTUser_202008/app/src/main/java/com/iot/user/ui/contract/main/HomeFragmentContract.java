package com.iot.user.ui.contract.main;

import com.iot.user.base.BaseView;
import com.iot.user.http.model.BaseResponse;

public interface HomeFragmentContract {
    interface View extends BaseView {
        void onSuccess(BaseResponse bean, String type);
    }

    interface Presenter {
        /**获取家庭设备列表**/
        void getFamilyDevList(int pageIndex);
        /**获取家庭列表**/
        void getFamilyListData();
        /**获取报警设备列表**/
        void getAlertDevData(int pageIndex);
        /**搜索设备***/
        void getDevInfoWithDevNum(String devNum);
        /**获取设备各个状态的数量**/
        void getFamilyDevStatusNum();
        /**获取设备各个状态的数量**/
        void getFamilyRoomList(String familyType);
    }
}
