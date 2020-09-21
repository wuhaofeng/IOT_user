package com.iot.user.ui.presenter.login;

import android.annotation.SuppressLint;

import com.iot.user.R;
import com.iot.user.app.IOTApplication;
import com.iot.user.base.BasePresenter;
import com.iot.user.http.NetWorkApi;
import com.iot.user.http.model.BaseResponse;
import com.iot.user.http.net.RxUtils;
import com.iot.user.http.request.login.ResetPwdJsonRequest;
import com.iot.user.http.request.login.VcodeCheckJsonRequest;
import com.iot.user.http.request.login.VcodeSendJsonRequest;
import com.iot.user.ui.activity.login.ForgetPsdActivity;
import com.iot.user.ui.contract.login.ForgetPsdContract;
import com.iot.user.utils.MD5Util;
import com.iot.user.utils.MyToast;
import com.iot.user.utils.PrefUtil;

import io.reactivex.observers.DisposableObserver;

public class ForgetPsdPresenter extends BasePresenter <ForgetPsdContract.View> implements ForgetPsdContract.Presenter{
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
        VcodeCheckJsonRequest request = new VcodeCheckJsonRequest(phone,"2",code);
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
    public void changePsd(final String phone, String newPsd) {
        if (!isViewAttached()){
            return;
        }
        if(isViewAttached())mView.showLoading("修改中...");
        ResetPwdJsonRequest request = new ResetPwdJsonRequest(phone, MD5Util.md5(newPsd));
        NetWorkApi.provideRepositoryData().forgetPwd(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        if (entity.getCode() == 0) {
                            PrefUtil.setLoginAccount(phone, IOTApplication.getIntstance());
                            //成功修改密码后重置密码记住状态
                            PrefUtil.setIsRememberPwd(false,IOTApplication.getIntstance());
                            PrefUtil.setLoginPwd("",IOTApplication.getIntstance());
                            MyToast.showShortToast("密码修改成功,请重新登录");
                            if(isViewAttached())mView.onSuccess(entity,"change_psd");
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
}
