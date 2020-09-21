package com.iot.user.ui.presenter.notification;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.iot.user.app.IOTApplication;
import com.iot.user.base.BasePresenter;
import com.iot.user.constant.Constants;
import com.iot.user.http.NetWorkApi;
import com.iot.user.http.model.BaseResponse;
import com.iot.user.http.net.RxUtils;
import com.iot.user.http.request.message.UnitMessageHistoryRequest;
import com.iot.user.http.request.notification.SetNoticeReadJsonRequst;
import com.iot.user.http.request.notification.UnitNotificationListRequest;
import com.iot.user.ui.contract.notification.UnitNotificationContract;
import com.iot.user.ui.model.message.UnitMessageModel;
import com.iot.user.ui.model.notification.NoticeContentModel;
import com.iot.user.utils.MyToast;
import com.iot.user.utils.PrefUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.observers.DisposableObserver;

public class UnitNotificationPresenter extends BasePresenter<UnitNotificationContract.View> implements UnitNotificationContract.Presenter {
    public List<NoticeContentModel> mDatas=new ArrayList();
    @Override
    public void getUnitNotificationAll(final int pageIndex) {
        if (!isViewAttached()){
           return;
        }
        UnitNotificationListRequest request = new UnitNotificationListRequest(
                pageIndex, Constants.PageSize,PrefUtil.getLoginToken(IOTApplication.getIntstance()),
                PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()));
        NetWorkApi.provideRepositoryData().queryAllNotice(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        if (pageIndex==1){
                            mDatas.clear();
                        }
                        if (entity.getCode()==0){
                            dealNotificationData(entity);
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
    public void getUnitNotificationRead(final int pageIndex) {
        if (!isViewAttached()){
            return;
        }
        UnitNotificationListRequest request = new UnitNotificationListRequest(
                pageIndex, Constants.PageSize,PrefUtil.getLoginToken(IOTApplication.getIntstance()),
                PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()));
        NetWorkApi.provideRepositoryData().queryReadNotice(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        if (pageIndex==1){
                            mDatas.clear();
                        }
                        if (entity.getCode()==0){
                           dealNotificationData(entity);
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
    public void getUnitNotificationUnread(final int pageIndex) {
        if (!isViewAttached()){
            return;
        }
        UnitNotificationListRequest request = new UnitNotificationListRequest(
                pageIndex, Constants.PageSize,PrefUtil.getLoginToken(IOTApplication.getIntstance()),
                PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()));
        NetWorkApi.provideRepositoryData().queryNoReadNotice(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        if (pageIndex==1){
                            mDatas.clear();
                        }
                        if (entity.getCode()==0){
                            dealNotificationData(entity);
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
    public void setReadNotice(String noticeId) {
        if (!isViewAttached()){
            return;
        }
        SetNoticeReadJsonRequst request = new SetNoticeReadJsonRequst(
                PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()),
                PrefUtil.getLoginToken(IOTApplication.getIntstance()),
                noticeId,1, "已读");
        NetWorkApi.provideRepositoryData().setReadNotice(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        if (entity.getCode()==0){

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

    private void dealNotificationData(BaseResponse entity){
        LinkedTreeMap map=(LinkedTreeMap) entity.getBody();
        List dataArr=(List) map.get("list");
        Gson gson=new Gson();
        Type type =new TypeToken<List<NoticeContentModel>>() {}.getType();
        String jsonString = gson.toJson(dataArr);
        mDatas.addAll((List<NoticeContentModel>)gson.fromJson(jsonString, type));
        if(isViewAttached())mView.onSuccess(entity,"notification");
    }
}
