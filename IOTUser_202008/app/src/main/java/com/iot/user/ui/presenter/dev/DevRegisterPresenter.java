package com.iot.user.ui.presenter.dev;

import android.content.Intent;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iot.user.app.IOTApplication;
import com.iot.user.base.BasePresenter;
import com.iot.user.http.NetWorkApi;
import com.iot.user.http.model.BaseResponse;
import com.iot.user.http.net.RxUtils;
import com.iot.user.http.request.dev.CommonDevLowJsonRequest;
import com.iot.user.http.request.dev.UnitDevAddBindRequest;
import com.iot.user.http.request.dev.UnitDevUpdateBindRequest;
import com.iot.user.http.request.main.UnitFamilyRoomRequest;
import com.iot.user.ui.contract.dev.DevRegisterContract;
import com.iot.user.ui.model.main.UnitFamilyModel;
import com.iot.user.ui.model.main.UnitFamilyRoomModel;
import com.iot.user.utils.AppValidationMgr;
import com.iot.user.utils.MyToast;
import com.iot.user.utils.PrefUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.observers.DisposableObserver;

public class DevRegisterPresenter extends BasePresenter<DevRegisterContract.View> implements DevRegisterContract.Presenter {
    public UnitFamilyModel familyModel=null;
    public UnitFamilyRoomModel roomModel;
    public DevRegisterPresenter(){
        familyModel= PrefUtil.getUnitFamilyModel();
    }
    @Override
    public void registerAndBindDev(Double lat, Double lon, String address, String province, String city, String district, String street, String devName, String devNo) {
        if (!isViewAttached()){
            return;
        }
        UnitDevAddBindRequest request = new UnitDevAddBindRequest(
                PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()),PrefUtil.getLoginToken(IOTApplication.getIntstance()),
                lat,lon,address,province,city,district,street, devName,devNo,roomModel.getFamily_place_id());
        NetWorkApi.provideRepositoryData().postAddAndBindUnitDev(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        if (entity.getCode()==0) {
                            MyToast.showShortToast("设备注册成功");
                            if(isViewAttached())mView.onSuccess(entity,"register_bind");
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
    public void isDevReg_V902(String devNum) {
        if (!isViewAttached()){
            return;
        }
        if(isViewAttached())mView.showLoading(null);
        final String devNo= AppValidationMgr.removeStringSpace(devNum,0);
        CommonDevLowJsonRequest request=new CommonDevLowJsonRequest(devNo,PrefUtil.getLoginToken(IOTApplication.getIntstance()),
                PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()));
        NetWorkApi.provideRepositoryData().IsDevReg_V902(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        int errcode=entity.getCode();
                        if(isViewAttached())mView.successToJump(errcode,entity.getMessage());
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
    public void postUnitFamilyRoomList() {
        if (!isViewAttached()){
            return;
        }
        if (familyModel==null){
            MyToast.showShortToast("未获取到家庭数据");
            return;
        }
        String familyType="2";
        if (Double.valueOf(familyModel.getType()).intValue()==1){
            familyType="1";
        }
        UnitFamilyRoomRequest request = new UnitFamilyRoomRequest(
                PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()),
                PrefUtil.getLoginToken(IOTApplication.getIntstance()), familyModel.getFamily_id(),familyType);
        NetWorkApi.provideRepositoryData().postUnitFamilyRoomList(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        if (entity.getCode()==0) {
                            List dataArr = (ArrayList) entity.getBody();
                            Gson gson = new Gson();
                            String jsonString = gson.toJson(dataArr);
                            Type type = new TypeToken<List<UnitFamilyRoomModel>>() {
                            }.getType();
                            List<UnitFamilyRoomModel> mDatas=new ArrayList();
                            mDatas.addAll((List<UnitFamilyRoomModel>) gson.fromJson(jsonString, type));
                            if (mDatas.size()>0){
                                roomModel=mDatas.get(0);
                                if(isViewAttached())mView.onSuccess(entity,"room_list");
                            }
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
                        if(isViewAttached())mView.onComplete();
                    }
                });
    }
}
