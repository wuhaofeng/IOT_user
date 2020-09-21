package com.iot.user.ui.presenter.shopping;

import com.iot.user.app.IOTApplication;
import com.iot.user.base.BasePresenter;
import com.iot.user.constant.Constants;
import com.iot.user.http.NetWorkApi;
import com.iot.user.http.model.BaseResponse;
import com.iot.user.http.net.RxUtils;
import com.iot.user.http.request.shopping.ShoppingOrderListRequest;
import com.iot.user.ui.contract.shopping.ShoppingOrderListContract;
import com.iot.user.ui.model.shopping.ShoppingOrderListModel;
import com.iot.user.ui.model.shopping.ShoppingOrderListResp;
import com.iot.user.utils.MyToast;
import com.iot.user.utils.PrefUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.observers.DisposableObserver;

public class ShoppingOrderListPresenter extends BasePresenter<ShoppingOrderListContract.View> implements ShoppingOrderListContract.Presenter {
    public List<ShoppingOrderListModel> mDatas = new ArrayList<>();
    @Override
    public void getShoppingOrderList(final int pageIndex, int status) {
        if (!isViewAttached()){
            return;
        }
        ShoppingOrderListRequest request=new ShoppingOrderListRequest(status,"",pageIndex, Constants.PageSize, PrefUtil.getLoginToken(IOTApplication.getIntstance()),
                PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()));
        NetWorkApi.providePayRepositoryData().postNewOrderList(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse<ShoppingOrderListResp>>() {
                    @Override
                    public void onNext(BaseResponse<ShoppingOrderListResp> entity) {
                        if (entity.getCode() == 0) {
                            if(pageIndex == 1){
                                mDatas.clear();
                            }
                            if (entity.getData().getList().size()==0&&pageIndex>1){
                                MyToast.showShortToast("没有更多了");
                            }else {
                                for (int i = 0; i < entity.getData().getList().size(); i++) {
                                    mDatas.add(entity.getData().getList().get(i));
                                }
                            }
                            if(isViewAttached())mView.onSuccess(entity,"order_list");
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
}
