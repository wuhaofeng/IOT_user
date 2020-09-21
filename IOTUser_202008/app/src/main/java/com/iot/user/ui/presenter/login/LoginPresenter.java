package com.iot.user.ui.presenter.login;
import android.text.TextUtils;

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
import com.iot.user.ui.contract.login.LoginContract;
import com.iot.user.ui.model.login.LoginModel;
import com.iot.user.http.request.login.LoginRequest;
import com.iot.user.http.request.login.LoginSignCodeRequest;
import com.iot.user.utils.AppValidationMgr;
import com.iot.user.utils.MD5Util;
import com.iot.user.utils.MyToast;
import com.iot.user.utils.PrefUtil;

import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

public class LoginPresenter extends BasePresenter<LoginContract.View> implements LoginContract.Presenter{
    @Override
    public void loginWithPwd(final String username, final String password) {
        //View是否绑定 如果没有绑定，就不执行网络请求
        if (!isViewAttached()) {
            return;
        }
        if (TextUtils.isEmpty(username)) {
            MyToast.showShortToast("用户名不能为空");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            MyToast.showShortToast("密码不能为空");
            return;
        }

        if(password.length()>16){
            MyToast.showShortToast("密码长度不超过16位");
            return;
        }
        if(isViewAttached())mView.showLoading("正在登录...");
 /**************用户登录，第一层直接返回数据给biz处理数据，存数据库等操作******************/
        LoginSignCodeRequest request=new LoginSignCodeRequest(username);
        NetWorkApi.provideRepositoryData().signInCode(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        if (entity.getCode() == 0) {
                            Map dataDic=(Map) entity.getBody();
                            String ranCode =(String) dataDic.get("code");
                            String md5pwd = MD5Util.md5(ranCode+MD5Util.md5(password));
                            signInCode(username,md5pwd);
                        }
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
/**账号密码登录**/
    private void signInCode(String userName,String psd){
        LoginRequest loginRequest=new LoginRequest(userName,psd);
        NetWorkApi.provideRepositoryData().login(loginRequest)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        if (entity.getCode() == 0) {
                            /****保存用户信息***/
                            LinkedTreeMap map=(LinkedTreeMap)entity.getBody();
                            Gson gson=new Gson();
                            String jsonString = gson.toJson(map);
                            LoginModel resp=gson.fromJson(jsonString,LoginModel.class);
                            entity.setBody(resp);
                            if(isViewAttached())mView.onSuccess(entity,"pwdlogin");
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
    /**账号验证码登录**/
    @Override
    public void loginWithCode(String userPhone,String userCode){
        if (!AppValidationMgr.isPhone(userPhone)) {
            MyToast.showShortToast("手机号不正确");
            return;
        }
        if (TextUtils.isEmpty(userCode)) {
            MyToast.showShortToast("验证码不能为空");
            return;
        }
        if(isViewAttached())mView.showLoading("正在登录...");
        VcodeCheckJsonRequest request=new VcodeCheckJsonRequest(userPhone,"4",userCode);
        NetWorkApi.provideRepositoryData().postUnitLoginWithCode(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        if (entity.getCode() == 0) {
                            /****保存用户信息***/
                            LinkedTreeMap map=(LinkedTreeMap)entity.getBody();
                            Gson gson=new Gson();
                            String jsonString = gson.toJson(map);
                            LoginModel resp=gson.fromJson(jsonString,LoginModel.class);
                            entity.setBody(resp);
                            if(isViewAttached())mView.onSuccess(entity,"codelogin");
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

    /**发送验证码**/
    public void sendCodeBtn(String phone,String type){
        if (!isViewAttached()) {
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
                            if(isViewAttached())mView.onSuccess(entity,"sendcode");
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
