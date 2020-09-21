package com.iot.user.ui.presenter.room;

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
import com.iot.user.http.request.main.UnitFamilyRoomRequest;
import com.iot.user.http.request.main.UnitRoomDevRequest;
import com.iot.user.http.request.room.UnitCheckRoomNameRequest;
import com.iot.user.http.request.room.UnitRoomDeleteRequest;
import com.iot.user.ui.contract.room.UnitRoomContract;
import com.iot.user.ui.model.dev.UnitPublicRoomModel;
import com.iot.user.ui.model.main.UnitFamilyDevModel;
import com.iot.user.ui.model.main.UnitFamilyRoomModel;
import com.iot.user.utils.MyToast;
import com.iot.user.utils.PrefUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.observers.DisposableObserver;

public class UnitRoomPresenter extends BasePresenter<UnitRoomContract.View> implements UnitRoomContract.Presenter {
    public List<UnitFamilyRoomModel> mDatas=new ArrayList<>();/***房间列表**/
    public List<UnitPublicRoomModel> publicDatas=new ArrayList<>(); /**创建房间中的公共列表***/
    public List<UnitFamilyDevModel> devDatas=new ArrayList<>(); /**房间中的设备列表***/

    @Override
    public void postUnitRoomList(String familyId, String familyType) {
        if (!isViewAttached()){
            return;
        }
        UnitFamilyRoomRequest request = new UnitFamilyRoomRequest(
                PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()),
                PrefUtil.getLoginToken(IOTApplication.getIntstance()),familyId,familyType);
        NetWorkApi.provideRepositoryData().postUnitFamilyRoomList(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        mDatas.clear();
                        if (entity.getCode()==0){
                            List dataArr = (ArrayList) entity.getBody();
                            Gson gson = new Gson();
                            String jsonString = gson.toJson(dataArr);
                            Type type = new TypeToken<List<UnitFamilyRoomModel>>() {
                            }.getType();
                            mDatas.addAll((List<UnitFamilyRoomModel>) gson.fromJson(jsonString, type));
                            if(isViewAttached())mView.onSuccess(entity,"room_list");
                        }else{
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
    public void postUnitRoomDevList(int pageIndex, String familyId, String roomId) {
        if (!isViewAttached()){
            return;
        }
        UnitRoomDevRequest request = new UnitRoomDevRequest(
                PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()),
                PrefUtil.getLoginToken(IOTApplication.getIntstance()),pageIndex, Constants.PageSize,familyId,roomId);
        NetWorkApi.provideRepositoryData().postUnitRoomDevList(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        if (pageIndex==1){
                            devDatas.clear();
                        }
                        if (entity.getCode()==0) {
                            LinkedTreeMap map=(LinkedTreeMap) entity.getBody();
                            List dataArr=(List) map.get("list");
                            Gson gson=new Gson();
                            String jsonString = gson.toJson(dataArr);
                            Type type =new TypeToken<List<UnitFamilyDevModel>>() {}.getType();
                            devDatas.addAll((List<UnitFamilyDevModel>)gson.fromJson(jsonString, type));
                            if(isViewAttached())mView.onSuccess(entity,"room_dev");
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
    public void postUnitDeleteRoom(String familyId, List roomList) {
        if (!isViewAttached()){
            return;
        }
        UnitRoomDeleteRequest request = new UnitRoomDeleteRequest(
                PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()),
                PrefUtil.getLoginToken(IOTApplication.getIntstance()),familyId,roomList);
        NetWorkApi.provideRepositoryData().postUnitDeleteRoom(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        if (entity.getCode()==0) {
                            if(isViewAttached())mView.onSuccess(entity,"room_delete");
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
    public void postUnitPublicRoomList() {
        if (!isViewAttached()){
            return;
        }
        UnitFamilyListRequest request = new UnitFamilyListRequest(
                PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()),
                PrefUtil.getLoginToken(IOTApplication.getIntstance()));
        NetWorkApi.provideRepositoryData().postUnitPublicRoomList(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        publicDatas.clear();
                        if (entity.getCode()==0) {
                            List dataArr=(List) entity.getBody();
                            Gson gson=new Gson();
                            String jsonString = gson.toJson(dataArr);
                            Type type =new TypeToken<List<UnitPublicRoomModel>>() {}.getType();
                            publicDatas.addAll((List<UnitPublicRoomModel>)gson.fromJson(jsonString, type));
                            if(isViewAttached())mView.onSuccess(entity,"room_public_list");
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
    public void postUnitCheckRoom(String familyId,String roomName) {
        if (!isViewAttached()){
            return;
        }
        UnitCheckRoomNameRequest request = new UnitCheckRoomNameRequest(
                PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()),
                PrefUtil.getLoginToken(IOTApplication.getIntstance()),familyId,roomName);
        NetWorkApi.provideRepositoryData().postUnitCheckRoom(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        if (entity.getCode()==0) {
                            if(isViewAttached())mView.onSuccess(entity,"room_check_name");
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
    public void postUnitCreateRoom(String familyId,String roomName) {
        if (!isViewAttached()){
            return;
        }
        UnitCheckRoomNameRequest request = new UnitCheckRoomNameRequest(
                PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()),
                PrefUtil.getLoginToken(IOTApplication.getIntstance()),familyId,roomName);
        NetWorkApi.provideRepositoryData().postUnitCreateRoom(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        if (entity.getCode()==0) {
                            MyToast.showShortToast("房间创建成功");
                            if(isViewAttached())mView.onSuccess(entity,"room_create");
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
}
