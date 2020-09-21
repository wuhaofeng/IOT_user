package com.iot.user.ui.presenter.alert;

import com.iot.user.app.IOTApplication;
import com.iot.user.base.BasePresenter;
import com.iot.user.http.NetWorkApi;
import com.iot.user.http.base.IotHttpService;
import com.iot.user.http.model.BaseResponse;
import com.iot.user.http.net.RxUtils;
import com.iot.user.http.request.alert.ModifyOnlineDevJsonRequest;
import com.iot.user.http.request.alert.UnitDevCloseVoiceRequest;
import com.iot.user.ui.contract.alert.AlertNewContract;
import com.iot.user.utils.MyToast;
import com.iot.user.utils.PrefUtil;

import io.reactivex.observers.DisposableObserver;

public class AlertNewPresenter extends BasePresenter<AlertNewContract.View> implements AlertNewContract.Presenter {
    @Override
    public void dealDev(String devNum, String status) {
       if (!isViewAttached()){
           return;
       }
        ModifyOnlineDevJsonRequest request = new ModifyOnlineDevJsonRequest(
                PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()),
                PrefUtil.getLoginToken(IOTApplication.getIntstance()), devNum, status);
        NetWorkApi.provideRepositoryData().modifyOnlineDevDoStat(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        if (entity.getCode()==0){
                            if(isViewAttached())mView.onSuccess(entity,"know");
                        }else{
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
                        if(isViewAttached())mView.hideLoading();
                    }
                });

    }

    @Override
    public void dealUnitDev(String devNum, String status, String msgNum) {
        if (!isViewAttached()){
            return;
        }
        if(isViewAttached())mView.showLoading(null);
        UnitDevCloseVoiceRequest request=new UnitDevCloseVoiceRequest(PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()),
                PrefUtil.getLoginToken(IOTApplication.getIntstance()),devNum,status,msgNum);
       NetWorkApi.provideRepositoryData().postUnitDevCloseVoice(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        if (entity.getCode()==0){
                            if(isViewAttached())mView.onSuccess(entity,"unit_know");
                        }else{
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
                        if(isViewAttached())mView.hideLoading();
                    }
                });
    }
}
