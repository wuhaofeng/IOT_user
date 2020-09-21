package com.iot.user.ui.presenter.shopping;

import com.iot.user.app.IOTApplication;
import com.iot.user.base.BasePresenter;
import com.iot.user.http.NetWorkApi;
import com.iot.user.http.model.BaseResponse;
import com.iot.user.http.net.RxUtils;
import com.iot.user.http.request.shopping.ShoppingOrderApiPayRequest;
import com.iot.user.http.request.shopping.ShoppingOrderDetailRequest;
import com.iot.user.ui.adapter.shopping.AliPayJsonResp;
import com.iot.user.ui.adapter.shopping.PayJsonResp;
import com.iot.user.ui.contract.shopping.ShoppingOrderDetailContract;
import com.iot.user.ui.model.shopping.ShoppingOrderDetailResp;
import com.iot.user.utils.MyToast;
import com.iot.user.utils.PrefUtil;

import io.reactivex.observers.DisposableObserver;

public class ShoppingOrderDetailPresenter extends BasePresenter<ShoppingOrderDetailContract.View> implements ShoppingOrderDetailContract.Presenter {
    public ShoppingOrderDetailResp mDatas=null;
    @Override
    public void postCancelOrder(String tradeNo) {
        if (!isViewAttached()){
            return;
        }
        ShoppingOrderDetailRequest request=new ShoppingOrderDetailRequest(tradeNo, PrefUtil.getLoginToken(IOTApplication.getIntstance()),
                PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()));
        NetWorkApi.providePayRepositoryData().postCancelOrderWithTradeNo(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        if (entity.getCode() == 0){
                            MyToast.showShortToast("取消订单成功");
                            mDatas.setStatus("3");
                            if(isViewAttached())mView.onSuccess(entity,"cancel_order");
                        }else {
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
    public void postNewOrderDetail(String tradeNo) {
        if (!isViewAttached()){
            return;
        }
        if(isViewAttached())mView.showLoading(null);
        ShoppingOrderDetailRequest request=new ShoppingOrderDetailRequest(tradeNo, PrefUtil.getLoginToken(IOTApplication.getIntstance()),
                PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()));
        NetWorkApi.providePayRepositoryData().postNewOrderDetail(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse<ShoppingOrderDetailResp>>() {
                    @Override
                    public void onNext(BaseResponse<ShoppingOrderDetailResp> entity) {
                        if (entity.getCode() == 0){
                            mDatas = null;
                            mDatas = entity.getData();

                            if(isViewAttached())mView.onSuccess(entity,"order_detail");
                        }else {
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
    public void goApiPay(String tradeNo) {
        if (!isViewAttached()){
            return;
        }
        if(isViewAttached())mView.showLoading(null);
        ShoppingOrderApiPayRequest request = new ShoppingOrderApiPayRequest(tradeNo, PrefUtil.getLoginToken(IOTApplication.getIntstance()),PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()));
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
    public void goWeiChatPay(String tradeNo) {
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
