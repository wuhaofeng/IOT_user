package com.iot.user.ui.contract.login;

import com.iot.user.base.BaseView;
import com.iot.user.http.model.BaseResponse;
import com.iot.user.ui.model.login.LoginModel;

public interface LoginContract {

    interface View extends BaseView {
        void onSuccess(BaseResponse bean,String type);
    }

    interface Presenter {
        /**
         * 登陆
         *
         * @param username
         * @param password
         */
        void loginWithPwd(String username, String password);

        void loginWithCode(String userPhone,String userCode);
        /**发送验证码**/
        void sendCodeBtn(String phone,String type);
        /**
         * 2、绑定个推cid和华为token
         */
        void pluginPush(String hwToken);
    }
}
