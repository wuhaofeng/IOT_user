package com.iot.user.ui.contract.register;

import com.iot.user.base.BaseView;
import com.iot.user.http.model.BaseResponse;

public interface RegisterContract {
    interface View extends BaseView {
        void onSuccess(BaseResponse bean, String type);
        void resetTimer();
    }

    interface Presenter {
        /**发送验证码**/
        void sendCodeBtn(String phone,String type);
        /**验证验证码**/
        void checkCodeBtn(String phone,String code);
        /**修改密码**/
        void register(String phone,String psd,String nickName);
        /**个推**/
        void pluginPush(String hwToken);
    }
}
