package com.iot.user.ui.presenter.dev;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.iot.user.app.IOTApplication;
import com.iot.user.base.BasePresenter;
import com.iot.user.http.NetWorkApi;
import com.iot.user.http.model.BaseResponse;
import com.iot.user.http.net.RxUtils;
import com.iot.user.http.request.dev.CommonDevLowJsonRequest;
import com.iot.user.http.request.main.UnitDevDetailRequest;
import com.iot.user.ui.contract.dev.DevBindContract;
import com.iot.user.ui.model.dev.UnitDevBindModel;
import com.iot.user.utils.MyToast;
import com.iot.user.utils.PrefUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.observers.DisposableObserver;

public class DevBindPresenter extends BasePresenter<DevBindContract.View> implements DevBindContract.Presenter {
    public  List<UnitDevBindModel> list=new ArrayList<>();
    @Override
    public void postUnitDevBinder(String devNum) {
        if (!isViewAttached()){
            return;
        }
        UnitDevDetailRequest request=new UnitDevDetailRequest(PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()),PrefUtil.getLoginToken(IOTApplication.getIntstance()),devNum);
        NetWorkApi.provideRepositoryData().postUnitDevBinder(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        if (entity.getCode()==0) {
                            if(isViewAttached())mView.onSuccess(entity,"dev_bind");
                        }else {
                            MyToast.showShortToast(entity.getMessage());
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        MyToast.showShortToast(e.getMessage());
                    }
                    @Override
                    public void onComplete() {
                    }
                });
    }

    @Override
    public void getDevBindInfo(String devNum) {
        if (!isViewAttached()){
            return;
        }
        if(isViewAttached())mView.showLoading("正在查询设备信息");
        CommonDevLowJsonRequest request=new CommonDevLowJsonRequest(devNum, PrefUtil.getLoginToken(IOTApplication.getIntstance()),PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()));
        NetWorkApi.provideRepositoryData().getDevBindInfo(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        if (entity.getCode()==0) {
                            LinkedTreeMap map=(LinkedTreeMap) entity.getBody();
                            if (map==null){return;}
                            List dataArr=(List) map.get("arraylist");
                            Gson gson=new Gson();
                            String jsonString = gson.toJson(dataArr);
                            Type type =new TypeToken<List<UnitDevBindModel>>() {}.getType();
                            list.addAll((List<UnitDevBindModel>)gson.fromJson(jsonString, type));
                            if(isViewAttached())mView.onSuccess(entity,"bind_info");
                        }else {
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
