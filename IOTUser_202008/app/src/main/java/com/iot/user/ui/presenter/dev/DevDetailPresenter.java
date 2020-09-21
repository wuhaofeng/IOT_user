package com.iot.user.ui.presenter.dev;

import android.content.Intent;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.iot.user.app.IOTApplication;
import com.iot.user.base.BasePresenter;
import com.iot.user.constant.DevConstants;
import com.iot.user.http.NetWorkApi;
import com.iot.user.http.model.BaseResponse;
import com.iot.user.http.net.RxUtils;
import com.iot.user.http.request.alert.UnitDevCloseVoiceRequest;
import com.iot.user.http.request.dev.CommonDevLowJsonRequest;
import com.iot.user.http.request.main.UnitDevDetailRequest;
import com.iot.user.ui.contract.dev.DevDetailContract;
import com.iot.user.ui.model.main.UnitDevDetailModel;
import com.iot.user.ui.model.main.UnitFamilyDevModel;
import com.iot.user.ui.model.main.UnitFamilyModel;
import com.iot.user.ui.model.message.UnitDevAlertDescModel;
import com.iot.user.utils.AppValidationMgr;
import com.iot.user.utils.MyToast;
import com.iot.user.utils.PrefUtil;

import io.reactivex.observers.DisposableObserver;

public class DevDetailPresenter extends BasePresenter<DevDetailContract.View> implements DevDetailContract.Presenter {
    public UnitDevDetailModel devDetailModel=null;
    public Boolean isOwner=false;/**isOwner代表着主绑人***/

    public String devDescStr="";
    public UnitDevAlertDescModel devDescModel;

    public String alertStatus="0";/**当为1的时候代表有警报***/
    public String alertType="0";/**1为点击了我知道了，2为点击了消音***/
    @Override
    public void postUnitDevPermission(String devNum) {
        if (!isViewAttached()) {
            return;
        }
        devNum= AppValidationMgr.removeStringSpace(devNum,0);/***去除空格***/
        CommonDevLowJsonRequest request=new CommonDevLowJsonRequest(devNum,PrefUtil.getLoginToken(IOTApplication.getIntstance()),PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()));
        NetWorkApi.provideRepositoryData().postUnitDevPermission(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        if (entity.getCode()==0) {
                            LinkedTreeMap map=(LinkedTreeMap)entity.getBody();
                            int unitOperationPermission= ((Double)map.get("operationType")).intValue();
                            PrefUtil.setUnitOperationPermission(unitOperationPermission,IOTApplication.getIntstance());
                            if(isViewAttached())mView.onSuccess(entity,"dev_permission");
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
                        if(isViewAttached())mView.onComplete();
                    }
                });
    }

    @Override
    public void getDevInfoWithDevNum(String devNum) {
        if (!isViewAttached()) {
            return;
        }
        devNum= AppValidationMgr.removeStringSpace(devNum,0);/***去除空格***/
        String devStatus=devNum.substring(1,4);
        UnitDevDetailRequest request=new UnitDevDetailRequest(PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()),
                PrefUtil.getLoginToken(IOTApplication.getIntstance()),devNum);
        NetWorkApi.provideRepositoryData().postUnitDevInfo(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        if (entity.getCode()==0) {
                            LinkedTreeMap map=(LinkedTreeMap)entity.getBody();
                            Gson gson=new Gson();
                            if (devStatus.equals("192")){
                                LinkedTreeMap devDesc=(LinkedTreeMap) map.get("devdesc");
                                String devString = gson.toJson(devDesc);
                                if (devDesc.get("alertAddress") instanceof String){
                                    devDescModel=new UnitDevAlertDescModel();
                                }else {
                                    devDescModel = gson.fromJson(devString, UnitDevAlertDescModel.class);
                                }
                            }else {
                                devDescStr=(String) ((LinkedTreeMap) entity.getBody()).get("devdesc");
                            }
                            String jsonString = gson.toJson(map);
                            devDetailModel=gson.fromJson(jsonString, UnitDevDetailModel.class);
                            entity.setBody(devDetailModel);
                            if (Double.valueOf(devDetailModel.getBind_type()).intValue()==1){/***设备主绑人***/
                                isOwner=true;
                            }
                            if (devDetailModel.stat==null){
                                alertStatus="0";
                            }else if (Double.valueOf(devDetailModel.stat).intValue()>=1){
                                alertStatus="1";
                            }else {
                                alertStatus="0";
                            }
                            if (devDetailModel.mute==null){
                                alertType="0";
                            }else if (Double.valueOf(devDetailModel.mute).intValue()==1){
                                alertType="1";
                            }else if (Double.valueOf(devDetailModel.mute).intValue()>1){
                                alertType="2";
                            }else {
                                alertType="0";
                            }
                            if(isViewAttached())mView.onSuccess(entity,"dev_detail_info");
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

    @Override
    public void postUnitDevUnBinder(String devNum) {
        if (!isViewAttached()) {
            return;
        }
        devNum= AppValidationMgr.removeStringSpace(devNum,0);/***去除空格***/
        UnitDevDetailRequest request=new UnitDevDetailRequest(PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()),
                PrefUtil.getLoginToken(IOTApplication.getIntstance()),devNum);
        NetWorkApi.provideRepositoryData().postUnitDevUnBinder(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        if (entity.getCode()==0) {
                            MyToast.showShortToast("解绑成功");
                            if(isViewAttached())mView.onSuccess(entity,"dev_unbind");
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
                        if(isViewAttached())mView.onComplete();
                    }
                });
    }

    @Override
    public void postUnitDevUnAttention(String devNum) {
        if (!isViewAttached()) {
            return;
        }
        devNum= AppValidationMgr.removeStringSpace(devNum,0);/***去除空格***/
        UnitDevDetailRequest request=new UnitDevDetailRequest(PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()),
                PrefUtil.getLoginToken(IOTApplication.getIntstance()),devNum);
        NetWorkApi.provideRepositoryData().postUnitDevUnAttention(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        if (entity.getCode()==0) {
                            MyToast.showShortToast("取消关注成功");
                            if(isViewAttached())mView.onSuccess(entity,"dev_unAttention");
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
                        if(isViewAttached())mView.onComplete();
                    }
                });
    }

    @Override
    public void isDevReg_V902(String devNum) {
        if (!isViewAttached()) {
            return;
        }
        final String devNo= AppValidationMgr.removeStringSpace(devNum,0);
        CommonDevLowJsonRequest request=new CommonDevLowJsonRequest(devNo,PrefUtil.getLoginToken(IOTApplication.getIntstance()),
                PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()));
        NetWorkApi.provideRepositoryData().IsDevReg_V902(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        int errcode=entity.getCode();
                        if(isViewAttached())mView.successToJump(errcode);
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
    public void clickDevClicketCloseBtn(String devNum) {
        if (!isViewAttached()) {
            return;
        }
        if(isViewAttached())mView.showLoading(null);
        CommonDevLowJsonRequest request = new CommonDevLowJsonRequest(devNum, PrefUtil.getLoginToken(IOTApplication.getIntstance()),
                PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()));
        NetWorkApi.provideRepositoryData().postUnitCloseClicket(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        if (entity.getCode() == 0) {
                            MyToast.showShortToast("屏蔽成功");
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
                            entity.setBody(status);
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
