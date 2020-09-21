package com.iot.user.ui.presenter.share;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.google.gson.Gson;
import com.iot.user.app.IOTApplication;
import com.iot.user.base.BasePresenter;
import com.iot.user.http.NetWorkApi;
import com.iot.user.http.model.BaseResponse;
import com.iot.user.http.net.RxUtils;
import com.iot.user.http.request.dev.member.UnitMemberAddRequest;
import com.iot.user.http.request.share.UnitFamilyShareRequest;
import com.iot.user.http.request.share.UnitMemberInfoRequest;
import com.iot.user.ui.contract.share.FamilyShareContract;
import com.iot.user.ui.model.share.UnitMemberInfoModel;
import com.iot.user.utils.MyToast;
import com.iot.user.utils.PrefUtil;

import java.util.List;
import java.util.Map;

import io.reactivex.observers.DisposableObserver;

public class FamilySharePresenter extends BasePresenter<FamilyShareContract.View> implements FamilyShareContract.Presenter {
    public UnitMemberInfoModel memberInfoModel=null;
    @Override
    public void postUnitMemberInfo(String memberStr) {
        if (!isViewAttached()){
            return;
        }
        UnitMemberInfoRequest request = new UnitMemberInfoRequest(
                PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()),
                PrefUtil.getLoginToken(IOTApplication.getIntstance()),memberStr);
        NetWorkApi.provideRepositoryData().postUnitMemberInfo(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        if (entity.getCode()==0) {
                            Map dataMap=(Map) entity.getBody();
                            Gson gson=new Gson();
                            String jsonString = gson.toJson(dataMap);
                            memberInfoModel=gson.fromJson(jsonString,UnitMemberInfoModel.class);
                            if(isViewAttached())mView.onSuccess(entity,"member_info");
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

    @Override
    public void postUnitFamilyShare(String memberStr, String familyId) {/**家庭共享**/
        if (!isViewAttached()){
            return;
        }
        UnitFamilyShareRequest request = new UnitFamilyShareRequest(
                PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()),
                PrefUtil.getLoginToken(IOTApplication.getIntstance()), memberStr, familyId);
        NetWorkApi.provideRepositoryData().postUnitFamilyShare(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        if (entity.getCode() == 0) {
                            MyToast.showShortToast("家庭共享成功");
                            if(isViewAttached())mView.onSuccess(entity,"family_share");
                        } else {
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

    @Override
    public void postUnitAddMemberList(String devNum, String memberStr) {/**设备共享**/
        if (!isViewAttached()){
            return;
        }
        UnitMemberAddRequest request= new UnitMemberAddRequest(PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()),
                PrefUtil.getLoginToken(IOTApplication.getIntstance()),devNum,memberStr);
        NetWorkApi.provideRepositoryData().postUnitAddMemberList(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        if (entity.getCode() == 0) {
                            MyToast.showShortToast("设备共享成功");
                            if(isViewAttached())mView.onSuccess(entity,"dev_share");
                        } else {
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
}
