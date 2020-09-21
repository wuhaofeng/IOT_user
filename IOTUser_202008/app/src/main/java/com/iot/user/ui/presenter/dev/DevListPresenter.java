package com.iot.user.ui.presenter.dev;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.iot.user.app.IOTApplication;
import com.iot.user.base.BasePresenter;
import com.iot.user.constant.Constants;
import com.iot.user.http.NetWorkApi;
import com.iot.user.http.model.BaseResponse;
import com.iot.user.http.net.RxUtils;
import com.iot.user.http.request.main.UnitDevListRequest;
import com.iot.user.http.request.main.UnitDevShareRequest;
import com.iot.user.ui.contract.dev.DevListContract;
import com.iot.user.ui.model.main.UnitFamilyDevModel;
import com.iot.user.ui.model.main.UnitFamilyModel;
import com.iot.user.ui.service.SpeakerAudioPlayerManager;
import com.iot.user.utils.MyToast;
import com.iot.user.utils.PrefUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.observers.DisposableObserver;

public class DevListPresenter extends BasePresenter<DevListContract.View> implements DevListContract.Presenter {
    public List<UnitFamilyDevModel> mDatas =new ArrayList<>(); /**家庭设备列表**/
    public UnitFamilyModel familyModel;
    public DevListPresenter(){
       familyModel=PrefUtil.getUnitFamilyModel();
    }
    @Override
    public void postUnitDevList(int indexPage, String isOnline, String devStatus) {
        if (!isViewAttached()) {
            return;
        }
        UnitDevListRequest request = new UnitDevListRequest(
                PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()),
                PrefUtil.getLoginToken(IOTApplication.getIntstance()),indexPage,Constants.PageSize,familyModel.getFamily_id(),
                isOnline,devStatus);
        NetWorkApi.provideRepositoryData().postUnitDevList(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        if (indexPage==1){
                            mDatas.clear();
                        }
                        if (entity.getCode()==0) {
                            LinkedTreeMap map=(LinkedTreeMap) entity.getBody();
                            if (map==null){return;}
                            List dataArr=(List) map.get("list");
                            Gson gson=new Gson();
                            String jsonString = gson.toJson(dataArr);
                            Type type =new TypeToken<List<UnitFamilyDevModel>>() {}.getType();
                            mDatas.addAll((List<UnitFamilyDevModel>)gson.fromJson(jsonString, type));
                            if(isViewAttached())mView.onSuccess(entity,"dev_list");
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
    public void postUnitAlertDevList(int indexPage) {
        if (!isViewAttached()) {
            return;
        }
        UnitDevShareRequest request = new UnitDevShareRequest(
                PrefUtil.getLoginToken(IOTApplication.getIntstance()),
                PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()),indexPage, Constants.PageSize);
        NetWorkApi.provideRepositoryData().postUnitAlertDevList(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        if (indexPage==1){
                            mDatas.clear();
                        }
                        if (entity.getCode()==0) {
                            LinkedTreeMap map=(LinkedTreeMap) entity.getBody();
                            if (map==null){return;}
                            List dataArr=(List) map.get("list");
                            Gson gson=new Gson();
                            String jsonString = gson.toJson(dataArr);
                            Type type =new TypeToken<List<UnitFamilyDevModel>>() {}.getType();
                            mDatas.addAll((List<UnitFamilyDevModel>)gson.fromJson(jsonString, type));
                            entity.setBody(mDatas);
                            if(isViewAttached())mView.onSuccess(entity,"alert_dev_list");
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
}
