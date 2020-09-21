package com.iot.user.ui.presenter.dev;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iot.user.app.IOTApplication;
import com.iot.user.base.BasePresenter;
import com.iot.user.http.NetWorkApi;
import com.iot.user.http.model.BaseResponse;
import com.iot.user.http.net.RxUtils;
import com.iot.user.http.request.dev.UnitDevDetailNodeRequest;
import com.iot.user.http.request.dev.UnitDevNodeOpenRequest;
import com.iot.user.http.request.main.UnitDevDetailRequest;
import com.iot.user.ui.contract.dev.DevNodeContract;
import com.iot.user.ui.model.dev.UnitDevDetailAddrModel;
import com.iot.user.ui.model.dev.UnitDevDetailNodeModel;
import com.iot.user.ui.model.dev.UnitNodeProductModel;
import com.iot.user.utils.DialogUtils;
import com.iot.user.utils.LogUtil;
import com.iot.user.utils.MyToast;
import com.iot.user.utils.PrefUtil;
import com.iot.user.utils.Util;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;

public class DevNodePresenter extends BasePresenter<DevNodeContract.View> implements DevNodeContract.Presenter {
    public List<UnitDevDetailAddrModel> loadDatas=new ArrayList<>();
    public List<UnitDevDetailNodeModel> nodeDatas=new ArrayList<>();
    public List<UnitNodeProductModel> nodeModeDatas=new ArrayList<>();

    public List<UnitDevDetailNodeModel> nodeAllDatas=new ArrayList<>();
    @Override
    public void postDevLoadList(String devNum,String fragment) {
        if (!isViewAttached()){
            return;
        }
        UnitDevDetailRequest request=new UnitDevDetailRequest(PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()),
                PrefUtil.getLoginToken(IOTApplication.getIntstance()),devNum);
        NetWorkApi.provideRepositoryData().postUnitDevHAddr(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        loadDatas.clear();
                        if (entity.getCode()==0) {
                            List dataArr = (List) entity.getBody();
                            Gson gson = new Gson();
                            String jsonString = gson.toJson(dataArr);
                            Type type = new TypeToken<List<UnitDevDetailAddrModel>>() {
                            }.getType();
                            loadDatas.addAll((List<UnitDevDetailAddrModel>) gson.fromJson(jsonString, type));
                            if(isViewAttached())mView.onNodeSuccess(entity,"load_list",fragment);
                        }else if(entity.getCode()==108){
                            if(isViewAttached())mView.onNodeSuccess(entity,"dialog",fragment);
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
    public void getDevLoadNodeList(String devNum, String loadId,String fragment) {
        if (!isViewAttached()){
            return;
        }
        UnitDevDetailNodeRequest request=new UnitDevDetailNodeRequest(PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()),
                PrefUtil.getLoginToken(IOTApplication.getIntstance()),devNum,loadId,"2");
        NetWorkApi.provideRepositoryData().postUnitDevNodeAddr(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        nodeDatas.clear();
                        if (entity.getCode()==0) {
                            Map dataDic=(Map) entity.getBody();
                            if (dataDic==null){
                                return;
                            }
                            List dataArr=(List) dataDic.get("findDevDaddrDtos");
                            Gson gson=new Gson();
                            String jsonString = gson.toJson(dataArr);
                            Type type = new TypeToken<List<UnitDevDetailNodeModel>>() {
                            }.getType();
                            nodeDatas.addAll((List<UnitDevDetailNodeModel>) gson.fromJson(jsonString, type));
                            if(isViewAttached())mView.onNodeSuccess(entity,"node_list",fragment);
                        }else if(entity.getCode()==108){
                            if(isViewAttached())mView.onNodeSuccess(entity,"dialog",fragment);
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
    public void getDevLoadProList(String devNum, String loadId,String fragment) {
        if (!isViewAttached()){
            return;
        }
        UnitDevDetailNodeRequest request=new UnitDevDetailNodeRequest(PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()),
                PrefUtil.getLoginToken(IOTApplication.getIntstance()),devNum,loadId,"3");
        NetWorkApi.provideRepositoryData().postUnitDevNodeAddr(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        nodeModeDatas.clear();
                        if (entity.getCode()==0) {
                            Map dataDic=(Map) entity.getBody();
                            if (dataDic==null){
                                return;
                            }
                            List dataArr=(List) dataDic.get("findDevDaddrDtos");
                            Gson gson = new Gson();
                            String jsonString = gson.toJson(dataArr);
                            Type type = new TypeToken<List<UnitDevDetailNodeModel>>() {
                            }.getType();
                            List<UnitDevDetailNodeModel> dataSource = new ArrayList<>();
                            dataSource.addAll((List<UnitDevDetailNodeModel>) gson.fromJson(jsonString, type));
                            for (int i = 0; i < dataSource.size(); i++) {
                                UnitDevDetailNodeModel devDetailNodeModel = dataSource.get(i);
                                for (int j = 0; j < devDetailNodeModel.getNodem().size(); j++) {
                                    UnitNodeProductModel productModel = devDetailNodeModel.getNodem().get(j);
                                    productModel.setNodeID(devDetailNodeModel.getDaddr());
                                    productModel.setNodeAddr(devDetailNodeModel.getDname());
                                    nodeModeDatas.add(productModel);
                                }
                            }
                            if(isViewAttached())mView.onNodeSuccess(entity,"node_pro_list",fragment);
                        }else if(entity.getCode()==108){
                            if(isViewAttached())mView.onNodeSuccess(entity,"dialog",fragment);
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
    public void getDevLoadAllNodeList(String devNum, String loadId,String fragment) {
        if (!isViewAttached()){
            return;
        }
        UnitDevDetailNodeRequest request=new UnitDevDetailNodeRequest(PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()),
                PrefUtil.getLoginToken(IOTApplication.getIntstance()),devNum,loadId,"1");
        NetWorkApi.provideRepositoryData().postUnitDevNodeAddr(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        nodeAllDatas.clear();
                        if (entity.getCode()==0) {
                            Map dataDic=(Map) entity.getBody();
                            if (dataDic==null){
                                return;
                            }
                            List dataArr=(List) dataDic.get("findDevDaddrDtos");
                            Gson gson=new Gson();
                            String jsonString = gson.toJson(dataArr);
                            Type type = new TypeToken<List<UnitDevDetailNodeModel>>() {
                            }.getType();
                            nodeAllDatas.addAll((List<UnitDevDetailNodeModel>) gson.fromJson(jsonString, type));
                            if(isViewAttached())mView.onNodeSuccess(entity,"node_list_all",fragment);
                        }else if(entity.getCode()==108){
                            if(isViewAttached())mView.onNodeSuccess(entity,"dialog",fragment);
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
    public void postUnitHArrCloseCode(String devNum, List loadId,String fragment) {
        if (!isViewAttached()){
            return;
        }
        UnitDevNodeOpenRequest request = new UnitDevNodeOpenRequest(PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()),
                PrefUtil.getLoginToken(IOTApplication.getIntstance()), devNum, loadId);
        NetWorkApi.provideRepositoryData().postUnitHArrCloseCode(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        if (entity.getCode() == 0) {
                            if(isViewAttached())mView.onNodeSuccess(entity,"close_load",fragment);
                        } else {
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
    public void postUnitHArrOpenCode(String devNum, List loadId,String fragment) {
        if (!isViewAttached()){
            return;
        }
        UnitDevNodeOpenRequest request = new UnitDevNodeOpenRequest(PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()),
                PrefUtil.getLoginToken(IOTApplication.getIntstance()), devNum, loadId);
        NetWorkApi.provideRepositoryData().postUnitHArrOpenCode(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        if (entity.getCode() == 0) {
                            if(isViewAttached())mView.onNodeSuccess(entity,"open_load",fragment);
                        } else {
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
    public void postUnitNodeCloseCode(String devNum, List loadId,String fragment) {
        if (!isViewAttached()){
            return;
        }
        UnitDevNodeOpenRequest request = new UnitDevNodeOpenRequest(PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()),
                PrefUtil.getLoginToken(IOTApplication.getIntstance()), devNum, loadId);
        NetWorkApi.provideRepositoryData().postUnitNodeCloseCode(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        if (entity.getCode() == 0) {
                        } else {
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
    public void postUnitNodeOpenCode(String devNum, List loadId,String fragment) {
        if (!isViewAttached()){
            return;
        }
        UnitDevNodeOpenRequest request = new UnitDevNodeOpenRequest(PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()),
                PrefUtil.getLoginToken(IOTApplication.getIntstance()), devNum, loadId);
        NetWorkApi.provideRepositoryData().postUnitNodeOpenCode(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        if (entity.getCode() == 0) {
                        } else {
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
    public void updateDevHaddrInfoNetWork(String devNum,String hArrID,String fragment) {
        if (!isViewAttached()){
            return;
        }
        String hArrId= Util.getTwoNumFloatWith(hArrID,false);
        UnitDevDetailNodeRequest request=new UnitDevDetailNodeRequest(PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()),
                PrefUtil.getLoginToken(IOTApplication.getIntstance()),devNum,hArrId,"1");
        NetWorkApi.provideRepositoryData().postUnitDevNodeAddr(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        if (entity.getCode() == 0) {
                            if(isViewAttached())mView.onNodeSuccess(entity,"setting_notification",fragment);
                        }else if(entity.getCode()==108){
                            if(isViewAttached())mView.onNodeSuccess(entity,"dialog",fragment);
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
    public void clickOpenNodeProductBtn(String devNum, List loadId,String fragment) {
        if (!isViewAttached()){
            return;
        }
        UnitDevNodeOpenRequest request = new UnitDevNodeOpenRequest(PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()),
                PrefUtil.getLoginToken(IOTApplication.getIntstance()), devNum, loadId);
        NetWorkApi.provideRepositoryData().postUnitNodeProductCloseCode(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        if (entity.getCode() == 0) {
                            if(isViewAttached())mView.onNodeSuccess(entity,"close_node_pro",fragment);
                        } else {
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
}
