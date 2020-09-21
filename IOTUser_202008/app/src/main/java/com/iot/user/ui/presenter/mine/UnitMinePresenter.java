package com.iot.user.ui.presenter.mine;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;

import com.google.gson.internal.LinkedTreeMap;
import com.iot.user.app.IOTApplication;
import com.iot.user.base.BasePresenter;
import com.iot.user.http.NetWorkApi;
import com.iot.user.http.model.BaseResponse;
import com.iot.user.http.net.RxUtils;
import com.iot.user.http.request.mine.UnitMineRequest;
import com.iot.user.ui.contract.mine.UnitMineContract;
import com.iot.user.utils.MyToast;
import com.iot.user.utils.PXTransUtils;
import com.iot.user.utils.PrefUtil;
import com.iot.user.utils.ZXingUtil;

import io.reactivex.observers.DisposableObserver;

public class UnitMinePresenter extends BasePresenter<UnitMineContract.View> implements UnitMineContract.Presenter {
    @Override
    public void postUnitMineUserInfo() {
        if (!isViewAttached()){
            return;
        }
        UnitMineRequest request=new UnitMineRequest(PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()),
                PrefUtil.getLoginToken(IOTApplication.getIntstance()),
                PrefUtil.getUsername(IOTApplication.getIntstance()));
        NetWorkApi.provideRepositoryData().postUnitMineUserInfo(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        if (entity.getCode()==0) {
                            LinkedTreeMap dataDic=(LinkedTreeMap) entity.getBody();
                            PrefUtil.setNickname((String)dataDic.get("nickName"),IOTApplication.getIntstance());
                            PrefUtil.setUsername((String)dataDic.get("userName"),IOTApplication.getIntstance());
                            PrefUtil.setPhone((String)dataDic.get("phone"),IOTApplication.getIntstance());
                            PrefUtil.setLoginAccountUid((String)dataDic.get("user_id"),IOTApplication.getIntstance());

                            PrefUtil.setPushmark((String)dataDic.get("push_mark"),IOTApplication.getIntstance());
                            PrefUtil.setSmsmark((String)dataDic.get("sms_mark"),IOTApplication.getIntstance());
                            PrefUtil.setPhonemark((String)dataDic.get("phone_mark"),IOTApplication.getIntstance());
                            if(isViewAttached())mView.onSuccess(entity,"user_info");
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
