package com.iot.user.ui.presenter.register;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.igexin.sdk.PushManager;
import com.iot.user.app.IOTApplication;
import com.iot.user.base.BasePresenter;
import com.iot.user.http.NetWorkApi;
import com.iot.user.http.model.BaseResponse;
import com.iot.user.http.net.RxUtils;
import com.iot.user.http.request.login.PluginPushJsonRequest;
import com.iot.user.http.request.login.VcodeCheckJsonRequest;
import com.iot.user.http.request.login.VcodeSendJsonRequest;
import com.iot.user.http.request.register.RegistJsonRequest;
import com.iot.user.ui.contract.register.RegisterContract;
import com.iot.user.ui.model.login.LoginModel;
import com.iot.user.ui.model.register.RegisterModel;
import com.iot.user.utils.MD5Util;
import com.iot.user.utils.MyToast;
import com.iot.user.utils.PrefUtil;

import io.reactivex.observers.DisposableObserver;

public class RegisterPresenter extends BasePresenter<RegisterContract.View> implements RegisterContract.Presenter{

    @Override
    public void sendCodeBtn(String phone, String type) {
        if (!isViewAttached()){
            return;
        }
        if(isViewAttached())mView.showLoading(null);
        VcodeSendJsonRequest request = new VcodeSendJsonRequest(phone,type);//发送
        NetWorkApi.provideRepositoryData().vCodeSend(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        if (entity.getCode() == 0) {
                            MyToast.showShortToast("验证码已发送到您手机");
                        } else {
                            MyToast.showShortToast(entity.getMessage());
                            if(isViewAttached())mView.onSuccess(entity,"sendcode");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if(isViewAttached())mView.onError(e.getMessage());
                        if(isViewAttached())mView.resetTimer();
                        if(isViewAttached())mView.hideLoading();
                    }

                    @Override
                    public void onComplete() {
                        if(isViewAttached())mView.onComplete();
                        if(isViewAttached())mView.hideLoading();
                    }
                });
    }

    @Override
    public void checkCodeBtn(String phone, String code) {
        if (!isViewAttached()){
            return;
        }
        if(isViewAttached())mView.showLoading("验证中...");
        VcodeCheckJsonRequest request = new VcodeCheckJsonRequest(phone,"1",code);
        NetWorkApi.provideRepositoryData().vCodeCheck(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        if (entity.getCode() == 0) {
                            if(isViewAttached())mView.onSuccess(entity,"check_code");
                        } else {
                            MyToast.showShortToast("验证码有误，请重新获取");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if(isViewAttached())mView.onError(e.getMessage());
                        if(isViewAttached())mView.hideLoading();
                    }

                    @Override
                    public void onComplete() {
                        if(isViewAttached())mView.onComplete();
                        if(isViewAttached())mView.hideLoading();
                    }
                });
    }

    @Override
    public void register(String phone, String psd, String nickName) {
        if (!isViewAttached()){
            return;
        }
        if(isViewAttached())mView.showLoading("注册中...");
        String userPsd = MD5Util.md5(psd);
        RegistJsonRequest request = new RegistJsonRequest(phone,userPsd,nickName);
        NetWorkApi.provideRepositoryData().regist(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        if (entity.getCode() == 0) {
                            LinkedTreeMap map=(LinkedTreeMap)entity.getBody();
                            Gson gson=new Gson();
                            String jsonString = gson.toJson(map);
                            RegisterModel resp=gson.fromJson(jsonString,RegisterModel.class);
                            entity.setBody(resp);
                            PrefUtil.setLoginToken(resp.getToken(), IOTApplication.getIntstance());
                            PrefUtil.setLoginAccountUid(resp.getuId(),IOTApplication.getIntstance());
                            PrefUtil.setLoginAccount(resp.getUsername(),IOTApplication.getIntstance());
                            PrefUtil.setNickname(resp.getNickName(),IOTApplication.getIntstance());
                            PrefUtil.setUsername(resp.getUsername(),IOTApplication.getIntstance());
                            PrefUtil.setPhone(resp.getPhone(),IOTApplication.getIntstance());
                            //成功修改密码后重置密码记住状态
                            PrefUtil.setIsRememberPwd(false,IOTApplication.getIntstance());
                            PrefUtil.setLoginPwd("",IOTApplication.getIntstance());
                            MyToast.showShortToast("注册成功,即将关闭页面!");
                            if(isViewAttached())mView.onSuccess(entity,"register");
                        } else {
                            MyToast.showShortToast(entity.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if(isViewAttached())mView.onError(e.getMessage());
                        if(isViewAttached())mView.hideLoading();
                    }

                    @Override
                    public void onComplete() {
                        if(isViewAttached())mView.onComplete();
                        if(isViewAttached())mView.hideLoading();
                    }
                });
    }
    /**个推**/
    @Override
    public void pluginPush(String hwToken){
        if (!isViewAttached()) {
            return;
        }
        String cid = PushManager.getInstance().getClientid(IOTApplication.getIntstance());/**个推id**/
        PluginPushJsonRequest request=new  PluginPushJsonRequest(PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()),
                PrefUtil.getLoginToken(IOTApplication.getIntstance()),hwToken, cid);
        NetWorkApi.provideRepositoryData().pluginPush(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if(isViewAttached())mView.onError(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        if(isViewAttached())mView.onComplete();
                    }
                });
    }
}
