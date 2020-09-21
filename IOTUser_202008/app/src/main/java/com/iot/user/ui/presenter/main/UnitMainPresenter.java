package com.iot.user.ui.presenter.main;

import com.igexin.sdk.PushManager;
import com.iot.user.app.IOTApplication;
import com.iot.user.base.BasePresenter;
import com.iot.user.http.NetWorkApi;
import com.iot.user.http.model.BaseResponse;
import com.iot.user.http.net.RxUtils;
import com.iot.user.http.request.login.PluginPushJsonRequest;
import com.iot.user.http.request.login.UnitFamilyListRequest;
import com.iot.user.ui.contract.main.UnitMainContract;
import com.iot.user.utils.PrefUtil;

import io.reactivex.observers.DisposableObserver;

public class UnitMainPresenter extends BasePresenter<UnitMainContract.View>  implements UnitMainContract.Presenter{
    @Override
    public void pluginPush(String hwToken) {
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

    @Override
    public void getFamilyShareCount() {
        if (!isViewAttached()) {
            return;
        }
        UnitFamilyListRequest request=new UnitFamilyListRequest(PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()),
                PrefUtil.getLoginToken(IOTApplication.getIntstance()));
        NetWorkApi.provideRepositoryData().postUnitFamilyNewCount(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        if (isViewAttached())
                        if(isViewAttached())mView.onSuccess(entity,"FamilyShareCount");
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isViewAttached())
                        if(isViewAttached())mView.onError(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        if (isViewAttached())
                        if(isViewAttached())mView.onComplete();
                    }
                });
    }
}
