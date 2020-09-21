package com.iot.user.ui.presenter.dev;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iot.user.app.IOTApplication;
import com.iot.user.base.BasePresenter;
import com.iot.user.http.NetWorkApi;
import com.iot.user.http.model.BaseResponse;
import com.iot.user.http.net.RxUtils;
import com.iot.user.http.request.dev.member.UnitMemberAddRequest;
import com.iot.user.http.request.dev.member.UnitMemberRemoveRequest;
import com.iot.user.http.request.main.UnitDevDetailRequest;
import com.iot.user.ui.contract.dev.DevMemberContract;
import com.iot.user.ui.model.dev.UnitDevMemberModel;
import com.iot.user.utils.AppValidationMgr;
import com.iot.user.utils.MyToast;
import com.iot.user.utils.PrefUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.observers.DisposableObserver;

public class DevMemberPresenter extends BasePresenter<DevMemberContract.View> implements DevMemberContract.Presenter {
    public List<UnitDevMemberModel> mDatas=new ArrayList<>();
    @Override
    public void postUnitDevMemberList(String devNum) {
        if (!isViewAttached()){
            return;
        }
        devNum= AppValidationMgr.removeStringSpace(devNum,0);/***去除空格***/
        UnitDevDetailRequest request=new UnitDevDetailRequest(PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()),
                PrefUtil.getLoginToken(IOTApplication.getIntstance()),devNum);
        NetWorkApi.provideRepositoryData().postUnitDevMemberList(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        mDatas.clear();
                        if (entity.getCode()==0) {
                            List dataArr=(List) entity.getBody();
                            Gson gson=new Gson();
                            String jsonString = gson.toJson(dataArr);
                            Type type =new TypeToken<List<UnitDevMemberModel>>() {}.getType();
                            mDatas.addAll((List<UnitDevMemberModel>)gson.fromJson(jsonString, type));
                            if(isViewAttached())mView.onSuccess(entity,"member_list");
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
    public void UnitMemberRemoveRequest(String devNum, List memberId) {
        if (!isViewAttached()){
            return;
        }
        UnitMemberRemoveRequest request=new UnitMemberRemoveRequest(PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()),
                PrefUtil.getLoginToken(IOTApplication.getIntstance()),devNum,memberId);
        NetWorkApi.provideRepositoryData().postUnitRemoveMemberList(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        if (entity.getCode()==0) {
                            if(isViewAttached())mView.onSuccess(entity,"member_delete");
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
    public void postUnitAddMemberList(String devNum, String memberName) {
        if (!isViewAttached()){
            return;
        }
        UnitMemberAddRequest request=new UnitMemberAddRequest(PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()),
                PrefUtil.getLoginToken(IOTApplication.getIntstance()),devNum,memberName);
        NetWorkApi.provideRepositoryData().postUnitAddMemberList(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        if (entity.getCode()==0) {
                            if(isViewAttached())mView.onSuccess(entity,"member_add");
                        }else {
                            MyToast.showShortToast(entity.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public int findCanDeleteMemberNumber(){
        if (!isViewAttached()){
            return 0;
        }
        int subNum=0;
        for (int i=0;i<mDatas.size();i++){
            UnitDevMemberModel memberModel=mDatas.get(i);
            if (Double.valueOf(memberModel.getBindType()).intValue()==2){//1.主绑人 2.设备分享 3.家庭分享
                subNum++;
            }
        }
        return subNum;
    }
}
