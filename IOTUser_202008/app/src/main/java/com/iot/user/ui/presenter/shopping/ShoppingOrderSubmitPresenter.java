package com.iot.user.ui.presenter.shopping;

import com.google.gson.internal.LinkedTreeMap;
import com.iot.user.app.IOTApplication;
import com.iot.user.base.BasePresenter;
import com.iot.user.http.NetWorkApi;
import com.iot.user.http.model.BaseResponse;
import com.iot.user.http.net.RxUtils;
import com.iot.user.http.request.shopping.ShoppingDeviceSubmitRequest;
import com.iot.user.http.request.shopping.ShoppingOrderApiPayRequest;
import com.iot.user.ui.activity.shopping.ShoppingOrderSubmitActivity;
import com.iot.user.ui.adapter.shopping.AliPayJsonResp;
import com.iot.user.ui.adapter.shopping.PayJsonResp;
import com.iot.user.ui.contract.shopping.ShoppingOrderSubmitContract;
import com.iot.user.ui.model.shopping.ShoppingDeviceSelectListModel;
import com.iot.user.utils.LogUtil;
import com.iot.user.utils.MyToast;
import com.iot.user.utils.PrefUtil;
import com.tencent.mm.opensdk.modelpay.PayReq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.observers.DisposableObserver;

public class ShoppingOrderSubmitPresenter extends BasePresenter<ShoppingOrderSubmitContract.View> implements ShoppingOrderSubmitContract.Presenter {
    public List<ShoppingDeviceSelectListModel> selectList=new ArrayList<>();
    public Float orderSwitch=0.0f;
    public String tradeNo;
    public String totalFee="¥0.0";
    public String exprie_time;
    public String create_time;
    @Override
    public void postCreateOrder() {
        if (!isViewAttached()){
            return;
        }
        List submitArr=new ArrayList();
        for (int i=0;i<selectList.size();i++){
            Map<String,Object> dataMap=new HashMap<>();
            ShoppingDeviceSelectListModel listModel=selectList.get(i);
            dataMap.put("dev_type",listModel.getDev_type());
            dataMap.put("product_id",listModel.getProduct_id());
            dataMap.put("renew_year",listModel.getRenew_year());
            submitArr.add(dataMap);
        }
        ShoppingDeviceSubmitRequest request=new ShoppingDeviceSubmitRequest(submitArr,orderSwitch, PrefUtil.getLoginToken(IOTApplication.getIntstance()),PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()));
        NetWorkApi.providePayRepositoryData().postCreateOrder(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        if (entity.getCode() == 0){

                            try{
                                LinkedTreeMap treeMap=(LinkedTreeMap) entity.getData();
                                tradeNo=String.valueOf(treeMap.get("tradeNo"));
                                totalFee=String.valueOf(treeMap.get("totalFee"));
                                exprie_time=String.valueOf(treeMap.get("exprie_time"));
                                create_time=String.valueOf(treeMap.get("create_time"));
                                if(isViewAttached())mView.onSuccess(entity,"create_order");
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }else {
                            MyToast.showShortToast(entity.getMessage());
                            LogUtil.e("getCode!=0, entity.getMessage == " + entity.getMessage());
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
                    }
                });
    }

    @Override
    public void goApiPay() {
        if (!isViewAttached()){
            return;
        }
        if(isViewAttached())mView.showLoading(null);
        ShoppingOrderApiPayRequest request = new ShoppingOrderApiPayRequest(tradeNo,PrefUtil.getLoginToken(IOTApplication.getIntstance()),PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()));
        NetWorkApi.providePayRepositoryData().postApiPay(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<AliPayJsonResp>() {
                    @Override
                    public void onNext(AliPayJsonResp entity) {
                        if (entity.getCode() == 0){
                           if(isViewAttached())mView.onPay(entity,"api_pay");
                        }else {
                            MyToast.showShortToast(entity.getMsg());
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
    public void goWeiChatPay() {
        if (!isViewAttached()){
            return;
        }
        if(isViewAttached())mView.showLoading(null);
        ShoppingOrderApiPayRequest requst = new ShoppingOrderApiPayRequest(tradeNo,PrefUtil.getLoginToken(IOTApplication.getIntstance()),PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()));
        NetWorkApi.providePayRepositoryData().postWeixinPay(requst)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<PayJsonResp>() {
                    @Override
                    public void onNext(final PayJsonResp entity) {
                        if (entity.getCode()==0) {
                            if(isViewAttached())mView.onPay(entity,"weixin_pay");
                        }else {
                            MyToast.showShortToast(entity.getMsg());
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
}
