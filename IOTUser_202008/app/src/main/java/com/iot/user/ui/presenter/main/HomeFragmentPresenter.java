package com.iot.user.ui.presenter.main;

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
import com.iot.user.http.request.main.UnitDevDetailRequest;
import com.iot.user.http.request.main.UnitDevShareRequest;
import com.iot.user.http.request.main.UnitFamilyRoomRequest;
import com.iot.user.http.request.main.UnitHomeDevStatusRequest;
import com.iot.user.http.request.main.UnitHomeFamilyDevRequest;
import com.iot.user.ui.contract.main.HomeFragmentContract;
import com.iot.user.ui.model.main.UnitDevDetailModel;
import com.iot.user.ui.model.main.UnitFamilyDevModel;
import com.iot.user.ui.model.main.UnitFamilyDevStatusModel;
import com.iot.user.ui.model.main.UnitFamilyModel;
import com.iot.user.ui.model.main.UnitFamilyRoomModel;
import com.iot.user.ui.service.SpeakerAudioPlayerManager;
import com.iot.user.utils.MyToast;
import com.iot.user.utils.PrefUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.internal.operators.observable.ObservableRetryWhen;
import io.reactivex.observers.DisposableObserver;

public class HomeFragmentPresenter extends BasePresenter<HomeFragmentContract.View> implements HomeFragmentContract.Presenter{
    public HomeFragmentPresenter(){

    }
    public List<UnitFamilyModel> mDatas =new ArrayList<>(); /***家庭列表**/
    public UnitFamilyModel familyModel;
    public List<UnitFamilyDevModel> mDevDatas =new ArrayList<>(); /**家庭设备列表**/
    public List<UnitFamilyDevModel> mAlertDevDatas =new ArrayList<>(); /**报警列表**/
    public List<UnitFamilyRoomModel> mRoomDatas =new ArrayList<>(); /**房间列表**/
    public UnitFamilyDevStatusModel statusModel=null;
    @Override
    public void getFamilyDevList(final int pageIndex) {
        if (!isViewAttached()) {
            return;
        }
        UnitHomeFamilyDevRequest request = new UnitHomeFamilyDevRequest(
                PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()),
                PrefUtil.getLoginToken(IOTApplication.getIntstance()),pageIndex,Constants.PageSize,familyModel.getFamily_id());
        NetWorkApi.provideRepositoryData().postUnitFamilyDevList(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        if (pageIndex==1){
                            mDevDatas.clear();
                        }
                        if (entity.getCode()==0) {
                            LinkedTreeMap map=(LinkedTreeMap) entity.getBody();
                            if (map==null){return;}
                            List dataArr=(List) map.get("list");
                            Gson gson=new Gson();
                            String jsonString = gson.toJson(dataArr);
                            Type type =new TypeToken<List<UnitFamilyDevModel>>() {}.getType();
                            mDevDatas.addAll((List<UnitFamilyDevModel>)gson.fromJson(jsonString, type));
                        }else {
                            MyToast.showShortToast(entity.getMessage());
                        }
                       if(isViewAttached())mView.onSuccess(entity,"family_dev_list");
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
    public void getFamilyListData() {
        if (!isViewAttached()) {
            return;
        }
        UnitFamilyListRequest request = new UnitFamilyListRequest(PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()),
                PrefUtil.getLoginToken(IOTApplication.getIntstance()));
        NetWorkApi.provideRepositoryData().postUnitFamilyList(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        mDatas.clear();
                        if (entity.getCode()==0) {
                            List dataArr=(List) entity.getBody();
                            Gson gson=new Gson();
                            String jsonString = gson.toJson(dataArr);
                            Type type =new TypeToken<List<UnitFamilyModel>>() {}.getType();
                            mDatas.addAll((List<UnitFamilyModel>)gson.fromJson(jsonString, type));
                            if (mDatas.size()==0){
                                MyToast.showShortToast("未获取到家庭的数据");
                            }else {
                                boolean isSelect=false;
                                for (int i = 0; i <mDatas.size() ; i++) {
                                    UnitFamilyModel data=mDatas.get(i);
                                    if (Double.valueOf(data.getFlag()).intValue()==2){
                                        isSelect=true;
                                        familyModel=data;break;
                                    }
                                }
                                if (isSelect==false){
                                    familyModel=mDatas.get(0);
                                }
                                PrefUtil.setUnitFamilyModel(familyModel);
                                entity.setBody(mDatas);
                                if(isViewAttached())mView.onSuccess(entity,"family_list");
                            }
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
    public void getAlertDevData(int pageIndex) {
        if (!isViewAttached()) {
            return;
        }
        UnitDevShareRequest request = new UnitDevShareRequest(
                PrefUtil.getLoginToken(IOTApplication.getIntstance()),
                PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()),pageIndex,Constants.PageSize);
        NetWorkApi.provideRepositoryData().postUnitAlertDevList(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        mAlertDevDatas.clear();
                        if (entity.getCode()==0) {
                            LinkedTreeMap map=(LinkedTreeMap) entity.getBody();
                            if (map==null){return;}
                            List dataArr=(List) map.get("list");
                            Gson gson=new Gson();
                            String jsonString = gson.toJson(dataArr);
                            Type type =new TypeToken<List<UnitFamilyDevModel>>() {}.getType();
                            mAlertDevDatas.addAll((List<UnitFamilyDevModel>)gson.fromJson(jsonString, type));
                            entity.setBody(mAlertDevDatas);

                            if (mAlertDevDatas.size()>0){
                                boolean isPlay=false;
                                int ringType=1;
                                for (int i=0;i<mAlertDevDatas.size();i++){
                                    UnitFamilyDevModel dataModel=mAlertDevDatas.get(i);
                                    if (Double.valueOf(dataModel.getDevStatus()).intValue()>=2){
                                        ringType=2;
                                        if (Double.valueOf(dataModel.getMute()).intValue()==0){
                                            isPlay=true;
                                        }
                                    }
                                }
                                if (isPlay==false){
                                    SpeakerAudioPlayerManager.getDefaultInstance().stopRing();
                                }else {
                                    if (ringType == 1) {
                                        SpeakerAudioPlayerManager.getDefaultInstance().playFaultRing();
                                    } else {
                                        SpeakerAudioPlayerManager.getDefaultInstance().playAlarmRing();
                                    }
                                }
                            }else {
                                SpeakerAudioPlayerManager.getDefaultInstance().stopRing();/***设备消音***/
                            }
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

    @Override
    public void getDevInfoWithDevNum(String devNum) {
        if (!isViewAttached()) {
            return;
        }
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
                            String jsonString = gson.toJson(map);
                            UnitDevDetailModel devDetailModel=gson.fromJson(jsonString, UnitDevDetailModel.class);
                            UnitFamilyDevModel devData=new UnitFamilyDevModel();
                            devData.setBindType(devDetailModel.getBind_type());
                            devData.setDevNum(devDetailModel.getDevNum());
                            devData.setDevStatus(devDetailModel.getStat());
                            if (devDetailModel.getStatus().equals("1")||devDetailModel.getStatus().equals("1.0")){
                                devData.setIsOnline("2");
                            }else{
                                devData.setIsOnline("1");
                            }
                            devData.setPlaceName(devDetailModel.getPlace_name());
                            devData.setPlaceId(devDetailModel.getFamily_place_id());
                            devData.setDevType(devDetailModel.getDevType());
                            devData.setFriendName(devDetailModel.getFriend_name());
                            devData.setNickname(devDetailModel.getNickname());
                            devData.setMute(devDetailModel.getMute());
                            entity.setBody(devData);
                            if(isViewAttached())mView.onSuccess(entity,"search_dev");

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
    public void getFamilyDevStatusNum() {
        if (!isViewAttached()) {
            return;
        }
        UnitHomeDevStatusRequest request=new UnitHomeDevStatusRequest(PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()),
                PrefUtil.getLoginToken(IOTApplication.getIntstance()), familyModel.getFamily_id());
        NetWorkApi.provideRepositoryData().postFamilyDevStatusNum(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        LinkedTreeMap map = (LinkedTreeMap) entity.getBody();
                        if (entity.getCode()==0) {
                            Gson gson = new Gson();
                            String jsonString = gson.toJson(map);
                            statusModel = gson.fromJson(jsonString, UnitFamilyDevStatusModel.class);
                            entity.setBody(statusModel);
                            if(isViewAttached())mView.onSuccess(entity,"family_dev_status");
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
    public void getFamilyRoomList(String familyType) {
        UnitFamilyRoomRequest request = new UnitFamilyRoomRequest(
                PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()),
                PrefUtil.getLoginToken(IOTApplication.getIntstance()), familyModel.getFamily_id(),familyType);
        NetWorkApi.provideRepositoryData().postUnitFamilyRoomList(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        if (entity.getCode()==0) {
                            mRoomDatas.clear();
                            List dataArr = (ArrayList) entity.getBody();
                            Gson gson=new Gson();
                            String jsonString = gson.toJson(dataArr);
                            Type type =new TypeToken<List<UnitFamilyRoomModel>>() {}.getType();
                            List dataList=(List<UnitFamilyRoomModel>)gson.fromJson(jsonString, type);
                            if (dataList!=null) {
                                mRoomDatas.addAll(dataList);
                            }
                            if(isViewAttached())mView.onSuccess(entity,"family_room_list");
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
