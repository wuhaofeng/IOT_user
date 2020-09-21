package com.iot.user.ui.contract.dev;

import com.iot.user.base.BaseView;
import com.iot.user.http.model.BaseResponse;

import java.util.List;

public interface DevNodeContract {
    interface View extends BaseView {
        void onNodeSuccess(BaseResponse bean, String type,String fragment);
    }

    interface Presenter {
        /**
         * 获取回路信息
         **/
        void postDevLoadList(String devNum,String fragment);
        /**
         * 获取节点信息
         **/
        void getDevLoadNodeList(String devNum,String loadId,String fragment);
        /**
         * 获取联动模块信息
         **/
        void getDevLoadProList(String devNum,String loadId,String fragment);
        /**
         * 获取回路下所有节点的信息
         **/
        void getDevLoadAllNodeList(String devNum,String loadId,String fragment);
        /**
         * 关闭回路
         **/
        void postUnitHArrCloseCode(String devNum, List loadId,String fragment);
        /**
         * 开启回路
         **/
        void postUnitHArrOpenCode(String devNum, List loadId,String fragment);
        /**
         * 关闭节点
         **/
        void postUnitNodeCloseCode(String devNum, List loadId,String fragment);
        /**
         * 开启节点
         **/
        void postUnitNodeOpenCode(String devNum, List loadId,String fragment);
        /**
         * settingFragment通知时获取数据
         **/
        void updateDevHaddrInfoNetWork(String devNum,String hArrID,String fragment);

        /**
         * 关闭/开启联动模块中的设备
         **/
        void clickOpenNodeProductBtn(String devNum, List loadId,String fragment);
    }
}
