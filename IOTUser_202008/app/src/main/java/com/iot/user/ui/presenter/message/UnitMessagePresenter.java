package com.iot.user.ui.presenter.message;

import android.view.View;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.iot.user.app.IOTApplication;
import com.iot.user.base.BasePresenter;
import com.iot.user.constant.Constants;
import com.iot.user.http.NetWorkApi;
import com.iot.user.http.model.BaseResponse;
import com.iot.user.http.net.RxUtils;
import com.iot.user.http.request.login.UnitFamilyListRequest;
import com.iot.user.http.request.message.DevHistoryJsonRequst;
import com.iot.user.http.request.message.UnitMessageFamilyAgreeRequest;
import com.iot.user.http.request.message.UnitMessageFamilyRequest;
import com.iot.user.http.request.message.UnitMessageHistoryRequest;
import com.iot.user.http.request.message.UnitMsgDeleteDevRequest;
import com.iot.user.http.request.message.UnitMsgDeleteFamilyRequest;
import com.iot.user.ui.contract.message.UnitMessageContract;
import com.iot.user.ui.model.message.UnitMessageFamilyModel;
import com.iot.user.ui.model.message.UnitMessageModel;
import com.iot.user.utils.MyToast;
import com.iot.user.utils.PrefUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.observers.DisposableObserver;

public class UnitMessagePresenter extends BasePresenter<UnitMessageContract.View> implements UnitMessageContract.Presenter {
    public List<UnitMessageModel> historyDatas = new ArrayList<>();
    public List<UnitMessageFamilyModel> familyDatas=new ArrayList<>();
    @Override
    public void postUnitFamilyNewCount() {
        if (!isViewAttached()) {
            return;
        }
        UnitFamilyListRequest request = new UnitFamilyListRequest(
                PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()),
                PrefUtil.getLoginToken(IOTApplication.getIntstance()));
        NetWorkApi.provideRepositoryData().postUnitFamilyNewCount(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        if (entity.getCode()==0) {
                            if(isViewAttached())mView.onSuccess(entity,"family_new_count");
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
    public void refreshMessagezHistoryList(final int pageIndex) {
        if (!isViewAttached()) {
            return;
        }
        UnitMessageHistoryRequest request = new UnitMessageHistoryRequest(
                PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()),
                PrefUtil.getLoginToken(IOTApplication.getIntstance()),
                pageIndex, Constants.PageSize);
        NetWorkApi.provideRepositoryData().postUnitMessageHistorylist(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {

                        if (pageIndex==1){
                            historyDatas.clear();
                        }
                        if (entity.getCode()==0) {
                            LinkedTreeMap map=(LinkedTreeMap) entity.getBody();
                            List dataArr=(List) map.get("list");
                            Gson gson=new Gson();
                            Type type =new TypeToken<List<UnitMessageModel>>() {}.getType();
                            String jsonString = gson.toJson(dataArr);
                            historyDatas.addAll((List<UnitMessageModel>)gson.fromJson(jsonString, type));
                            if(isViewAttached())mView.onSuccess(entity,"message_history");
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
    public void postMessagezHistoryDetail(String packRecodId) {
        if (!isViewAttached()) {
            return;
        }
        if(isViewAttached())mView.showLoading(null);
        DevHistoryJsonRequst request = new DevHistoryJsonRequst(
                PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()),
                PrefUtil.getLoginToken(IOTApplication.getIntstance()),
                packRecodId);
        NetWorkApi.provideRepositoryData().postUnitMessageHistoryDetail(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        if (entity.getCode()==0) {
                            if(isViewAttached())mView.onSuccess(entity, "message_history_detail");
                        }else {
                            MyToast.showShortToast(entity.getMessage());
                            if(isViewAttached())mView.hideLoading();
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
    public void refreshMessageFamilyList(final int pageIndex) {
        if (!isViewAttached()) {
            return;
        }
        UnitMessageFamilyRequest request = new UnitMessageFamilyRequest(
                PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()),
                PrefUtil.getLoginToken(IOTApplication.getIntstance()),
                pageIndex,Constants.PageSize);
        NetWorkApi.provideRepositoryData().postUnitMessageFamilyShareList(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        if (pageIndex==1){
                            familyDatas.clear();
                        }
                        if (entity.getCode()==0) {
                            LinkedTreeMap map=(LinkedTreeMap) entity.getBody();
                            List dataArr=(List) map.get("list");
                            Gson gson=new Gson();
                            Type type =new TypeToken<List<UnitMessageFamilyModel>>() {}.getType();
                            String jsonString = gson.toJson(dataArr);
                            familyDatas.addAll((List<UnitMessageFamilyModel>)gson.fromJson(jsonString, type));
                            if(isViewAttached())mView.onSuccess(entity,"family_share_list");
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
    public void refreshMessageDevList(final int pageIndex) {
        if (!isViewAttached()) {
            return;
        }
        UnitMessageFamilyRequest request = new UnitMessageFamilyRequest(
                PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()),
                PrefUtil.getLoginToken(IOTApplication.getIntstance()),
                pageIndex,Constants.PageSize);
        NetWorkApi.provideRepositoryData().postUnitMessageDevShareList(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        if (pageIndex==1){
                            familyDatas.clear();
                        }
                        if (entity.getCode()==0) {
                            LinkedTreeMap map = (LinkedTreeMap) entity.getBody();
                            List dataArr = (List) map.get("list");
                            Gson gson = new Gson();
                            Type type = new TypeToken<List<UnitMessageFamilyModel>>() {
                            }.getType();
                            String jsonString = gson.toJson(dataArr);
                            familyDatas.addAll((List<UnitMessageFamilyModel>) gson.fromJson(jsonString, type));
                            if(isViewAttached())mView.onSuccess(entity,"family_dev_list");
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
    public void postAgreeUnitMessageFamilyShare(int selectIndex) {
        if (!isViewAttached()) {
            return;
        }
        UnitMessageFamilyModel familyModel=familyDatas.get(selectIndex);
        UnitMessageFamilyAgreeRequest request = new UnitMessageFamilyAgreeRequest(
                PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()),
                PrefUtil.getLoginToken(IOTApplication.getIntstance()),
                familyModel.getFamily_id(),"1",familyModel.getFamily_shared_news_id());
        NetWorkApi.provideRepositoryData().postAgreeUnitMessageFamilyShare(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        if (entity.getCode() == 0) {
                            MyToast.showShortToast(entity.getMessage());
                        } else {
                            MyToast.showShortToast(entity.getMessage());
                        }
                        if(isViewAttached())mView.onSuccess(entity,"family_share_agree");
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
    public void postUnitDeleteFamilyMessage(int selectIndex) {
        if (!isViewAttached()) {
            return;
        }
        UnitMessageFamilyModel familyModel=familyDatas.get(selectIndex);
        String messageId=familyModel.getFamily_shared_news_id();
        List messageArr=new ArrayList<>();
        messageArr.add(messageId);
        UnitMsgDeleteFamilyRequest request = new UnitMsgDeleteFamilyRequest(
                PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()),
                PrefUtil.getLoginToken(IOTApplication.getIntstance()),
                messageArr);
        NetWorkApi.provideRepositoryData().postUnitDeleteFamilyMessage(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        if (entity.getCode() == 0) {
                            MyToast.showShortToast(entity.getMessage());
                            if(isViewAttached())mView.onSuccess(entity,"family_share_delete");
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
                        if(isViewAttached())mView.onComplete();
                    }
                });
    }

    @Override
    public void postUnitDeleteDevMessage(int selectIndex) {
        if (!isViewAttached()) {
            return;
        }
        UnitMessageFamilyModel familyModel=familyDatas.get(selectIndex);
        String messageId=familyModel.getDev_shared_news_id();
        List messageArr=new ArrayList<>();
        messageArr.add(messageId);
        UnitMsgDeleteDevRequest request = new UnitMsgDeleteDevRequest(
                PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()),
                PrefUtil.getLoginToken(IOTApplication.getIntstance()),
                messageArr);
        NetWorkApi.provideRepositoryData().postUnitDeleteDevMessage(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        if (entity.getCode() == 0) {
                            MyToast.showShortToast(entity.getMessage());
                            if(isViewAttached())mView.onSuccess(entity,"dev_share_delete");
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
                        if(isViewAttached())mView.onComplete();
                    }
                });
    }

}
