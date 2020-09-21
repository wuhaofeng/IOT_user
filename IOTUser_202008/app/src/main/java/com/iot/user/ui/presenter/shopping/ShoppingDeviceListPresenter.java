package com.iot.user.ui.presenter.shopping;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.iot.user.app.IOTApplication;
import com.iot.user.base.BasePresenter;
import com.iot.user.constant.Constants;
import com.iot.user.http.NetWorkApi;
import com.iot.user.http.model.BaseResponse;
import com.iot.user.http.net.RxUtils;
import com.iot.user.http.request.login.PluginPushJsonRequest;
import com.iot.user.http.request.shopping.ShoppingDeviceListJsonRequest;
import com.iot.user.http.request.shopping.ShoppingDeviceSubmitRequest;
import com.iot.user.http.request.shopping.ShoppingOrderDiscountRequest;
import com.iot.user.http.request.shopping.ShoppingOrderSwitchRequest;
import com.iot.user.ui.contract.shopping.ShoppingDeviceListContract;
import com.iot.user.ui.model.shopping.ShoppingDeviceListModel;
import com.iot.user.ui.model.shopping.ShoppingDeviceSelectListModel;
import com.iot.user.ui.model.shopping.ShoppingDeviceSubmitInfo;
import com.iot.user.ui.model.shopping.ShoppingOrderDiscountListModel;
import com.iot.user.utils.LogUtil;
import com.iot.user.utils.MyToast;
import com.iot.user.utils.PrefUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.observers.DisposableObserver;

public class ShoppingDeviceListPresenter extends BasePresenter<ShoppingDeviceListContract.View> implements ShoppingDeviceListContract.Presenter {
    public List<ShoppingDeviceListModel> mDatas = new ArrayList<>();
    public Float orderSwitch=0.0f;
    public List<ShoppingDeviceSelectListModel> selectList=new ArrayList<>();
    @Override
    public void postShoppingDeviceList(final int pageIndex) {
       if (!isViewAttached()){
           return;
       }
        ShoppingDeviceListJsonRequest request=new ShoppingDeviceListJsonRequest(""+pageIndex, Constants.PageSize,PrefUtil.getLoginToken(IOTApplication.getIntstance()),
                PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()));
        NetWorkApi.providePayRepositoryData().postShoppingDeviceList(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        if (entity.getCode()==0) {
                            if(pageIndex == 1){
                                mDatas.clear();
                            }
                            LinkedTreeMap map=(LinkedTreeMap) entity.getData();
                            Gson gson=new Gson();
                            List dataArr=(List) map.get("list");
                            String jsonString = gson.toJson(dataArr);
                            Type type =new TypeToken<List<ShoppingDeviceListModel>>() {}.getType();
                            mDatas.addAll((List<ShoppingDeviceListModel>)gson.fromJson(jsonString, type));
                            if(isViewAttached())mView.onSuccess(entity,"device_list");
                        }else {
                            MyToast.showShortToast(entity.getMessage());
                            LogUtil.e("code!=0, entity.getMessage == " + entity.getMessage());
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
    public void getOrderSwitch() {
        if (!isViewAttached()){
            return;
        }
        if(isViewAttached())mView.showLoading(null);
        ShoppingOrderSwitchRequest requst=new ShoppingOrderSwitchRequest(PrefUtil.getLoginToken(IOTApplication.getIntstance()),PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()));
        NetWorkApi.providePayRepositoryData().postOrderSwitch(requst).compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse> () {
                    @Override
                    public void onNext(BaseResponse entity) {
                        try{
                            Gson gson=new Gson();
                            Map data=gson.fromJson(entity.getData().toString(),Map.class);
                            String switchStr=String.valueOf(data.get("orderSwitch"));
                            orderSwitch=Float.parseFloat(switchStr);
                            if(isViewAttached())mView.onSuccess(entity,"order_switch");
                            Log.e("加载orderSwitch", "Activity: "+orderSwitch+","+switchStr);
                        }catch (Exception e){
                            e.printStackTrace();
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
    public void postOrderDiscountList(int selectIndex, final String type) {
        if (!isViewAttached()){
            return;
        }
        if (type.equals("0")) {/**type为0的时候是在全部的续费设备中选择年份，为1则是在已选中的设备中选择年份**/
            final ShoppingDeviceListModel info = mDatas.get(selectIndex);
            final ShoppingOrderDiscountRequest[] request = {new ShoppingOrderDiscountRequest("" + info.getDevType(), orderSwitch, PrefUtil.getLoginToken(IOTApplication.getIntstance()), PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()))};
            NetWorkApi.providePayRepositoryData().postOrderDiscountList(request[0]).compose(RxUtils.schedulersTransformer()) //线程调度
                    .subscribe(new DisposableObserver<BaseResponse<ShoppingOrderDiscountListModel>>() {
                        @Override
                        public void onNext(BaseResponse<ShoppingOrderDiscountListModel> entity) {
                            if (entity.getCode() == 0) {
                                if(isViewAttached())mView.onSuccessSelect(entity, info,type);
                            } else {
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
        }else{
            final ShoppingDeviceSelectListModel selectListModel=selectList.get(selectIndex);
            final ShoppingOrderDiscountRequest request = new ShoppingOrderDiscountRequest("" + selectListModel.getDev_type(), orderSwitch, PrefUtil.getLoginToken(IOTApplication.getIntstance()), PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()));
            NetWorkApi.providePayRepositoryData().postOrderDiscountList(request).compose(RxUtils.schedulersTransformer()) //线程调度
                    .subscribe(new DisposableObserver<BaseResponse<ShoppingOrderDiscountListModel>>() {
                        @Override
                        public void onNext(BaseResponse<ShoppingOrderDiscountListModel> entity) {
                            if (entity.getCode() == 0) {
                                if(isViewAttached())mView.onSuccessSelect(entity,selectListModel,type);
                            } else {
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

    @Override
    public void postOrderAccountDetail() {
        if (!isViewAttached()){
            return;
        }
        if (selectList.size()<1){
            MyToast.showShortToast("您还没有选择设备续费");
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
        ShoppingDeviceSubmitRequest request=new ShoppingDeviceSubmitRequest(submitArr,orderSwitch,PrefUtil.getLoginToken(IOTApplication.getIntstance()),PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()));
        NetWorkApi.providePayRepositoryData().postOrderAccountDetail(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse<ShoppingDeviceSubmitInfo>>() {
                    @Override
                    public void onNext(BaseResponse<ShoppingDeviceSubmitInfo> entity) {
                        if (entity.getCode() == 0) {
                            List<ShoppingDeviceSelectListModel> dataArr=entity.getData().getAccountDetail();
                            for (int i=0;i<dataArr.size();i++){
                                ShoppingDeviceSelectListModel dataModel=dataArr.get(i);
                                ShoppingDeviceSelectListModel selectListModel=selectList.get(i);
                                selectListModel.setTotal_fee(dataModel.getTotal_fee());
                                selectListModel.setMark(dataModel.getMark());
                                selectListModel.setRenew_year(dataModel.getRenew_year());
                            }
                            if(isViewAttached())mView.onSuccess(entity,"submit_order");
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
                        if(isViewAttached())mView.hideLoading();
                    }
                });

    }

    public void addDeviceSelectModel(ShoppingDeviceSelectListModel selectModel){/**添加selectmodel*/
        if (selectList.size()>0){
            int isRepeat =0;/**判断是否重复添加*/
            for (int i=0; i<selectList.size(); i++) {
                ShoppingDeviceSelectListModel dataModel=selectList.get(i);
                if (dataModel.getProduct_id().equals(selectModel.getProduct_id())) {
                    isRepeat=1;
                    dataModel.setPrice(selectModel.getPrice());

                    dataModel.setMark(selectModel.getMark());
                    dataModel.setRenew_year(selectModel.getRenew_year());
                    break;
                }
                if (i==selectList.size()-1&&isRepeat==0) {
                    selectList.add(selectModel);
                }
            }
        }else {
            selectList.add(selectModel);
        }
        if(isViewAttached())mView.onSuccess(null,"deal_select_data");
    }

}
