package com.iot.user.ui.contract.room;

import com.iot.user.base.BaseView;
import com.iot.user.http.model.BaseResponse;

import java.util.List;

public interface UnitRoomContract {
    interface View extends BaseView {
        void onSuccess(BaseResponse bean, String type);
    }
    interface Presenter {
        /**
         * 获取房间列表
         **/
        void postUnitRoomList(String familyId,String familyType);
        /**
         * 获取房间设备列表
         **/
        void postUnitRoomDevList(int pageIndex,String familyId,String roomId);
        /**
         * 删除房间
         **/
        void postUnitDeleteRoom(String familyId, List roomList);
        /**
         * 添加房间默认的列表
         **/
        void postUnitPublicRoomList();
        /**
         * 判断房间是否重复
         **/
        void postUnitCheckRoom(String familyId,String roomName);
        /**
         * 添加房间
         **/
        void postUnitCreateRoom(String familyId,String roomName);

    }
}
