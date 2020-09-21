package com.iot.user.ui.presenter.dev;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.iot.user.app.IOTApplication;
import com.iot.user.base.BasePresenter;
import com.iot.user.http.NetWorkApi;
import com.iot.user.http.model.BaseResponse;
import com.iot.user.http.net.RxUtils;
import com.iot.user.http.request.dev.UnitDevEditRequest;
import com.iot.user.http.request.dev.UnitDevRemovePlaceRequest;
import com.iot.user.http.request.dev.UnitDevUpdateBindRequest;
import com.iot.user.http.request.main.UnitDevDetailRequest;
import com.iot.user.ui.contract.dev.DevEditContract;
import com.iot.user.ui.model.dev.UnitDevEditModel;
import com.iot.user.ui.model.main.UnitDevDetailModel;
import com.iot.user.ui.model.main.UnitFamilyModel;
import com.iot.user.ui.model.main.UnitFamilyRoomModel;
import com.iot.user.utils.AppValidationMgr;
import com.iot.user.utils.MyToast;
import com.iot.user.utils.PrefUtil;

import io.reactivex.observers.DisposableObserver;

public class DevEditPresenter extends BasePresenter<DevEditContract.View> implements DevEditContract.Presenter {
    /***
     * 网络加载获取到的数据
     * **/
    public UnitDevDetailModel detailModel;
    public UnitFamilyModel familyModel=null;
    public UnitFamilyRoomModel roomModel;
    public DevEditPresenter(){
        familyModel = PrefUtil.getUnitFamilyModel();
    }
    @Override
    public void postUnitDevInfo(String devNum) {
        if (!isViewAttached()){
            return;
        }
        devNum= AppValidationMgr.removeStringSpace(devNum,0);/***去除空格***/
        UnitDevDetailRequest request=new UnitDevDetailRequest(PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()),
                PrefUtil.getLoginToken(IOTApplication.getIntstance()),devNum);
        NetWorkApi.provideRepositoryData().postUnitDevInfo(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        if (entity.getCode()==0){
                            LinkedTreeMap map=(LinkedTreeMap)entity.getBody();
                            Gson gson=new Gson();
                            String jsonString = gson.toJson(map);
                            detailModel=gson.fromJson(jsonString,UnitDevDetailModel.class);
                            if(isViewAttached())mView.onSuccess(entity,"dev_detail");
                        }else{
                            MyToast.showShortToast(entity.getMessage());
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        if(isViewAttached())mView.onError(e.getMessage());
                    }
                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void updateAndBindDev(Double lat, Double lon, String address, String province, String city, String district, String street, String devName, String devNo) {
        if (!isViewAttached()){
            return;
        }
        UnitDevUpdateBindRequest request = new UnitDevUpdateBindRequest(
                PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()),PrefUtil.getLoginToken(IOTApplication.getIntstance()),
                lat,lon,address,province,city,district,street, devName,devNo,roomModel.getPlace_name(),roomModel.getFamily_place_id(),"1",familyModel.getFamily_id());
        NetWorkApi.provideRepositoryData().postUpdateAndBindUnitDev(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        if (entity.getCode()==0) {
                            MyToast.showShortToast("修改注册成功");
                            if(isViewAttached())mView.onSuccess(entity,"update_bind");
                        }else {
                            MyToast.showShortToast(entity.getMessage());
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        if(isViewAttached())mView.onError(e.getMessage());
                    }
                    @Override
                    public void onComplete() {
                    }
                });
    }

    @Override
    public void postUnitDevEdit(UnitDevEditModel reqModel) {
        if (!isViewAttached()){
            return;
        }
        UnitDevEditRequest request=new UnitDevEditRequest(PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()),
                PrefUtil.getLoginToken(IOTApplication.getIntstance()),reqModel);
        NetWorkApi.provideRepositoryData().postUnitDevEdit(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        if (entity.getCode()==0) {
                            if(isViewAttached())mView.onSuccess(entity,"dev_edit");
                        }else {
                            MyToast.showShortToast(entity.getMessage());
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        if(isViewAttached())mView.onError(e.getMessage());
                    }
                    @Override
                    public void onComplete() {
                    }
                });
    }

    @Override
    public void removePlace(String devNum, String originRoomID, String currRoomID) {
        if (!isViewAttached()){
            return;
        }
        UnitDevRemovePlaceRequest request=new UnitDevRemovePlaceRequest(PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()),
                PrefUtil.getLoginToken(IOTApplication.getIntstance()),devNum,familyModel.getFamily_id(),originRoomID,roomModel.getFamily_place_id());
        NetWorkApi.provideRepositoryData().postUnitDevRemovePlace(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        if (entity.getCode() == 0) {
                            if(isViewAttached())mView.onSuccess(entity,"remove_room");
                        } else {
                            MyToast.showShortToast(entity.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if(isViewAttached())mView.onError(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
