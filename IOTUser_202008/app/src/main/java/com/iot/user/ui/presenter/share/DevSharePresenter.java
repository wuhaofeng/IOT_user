package com.iot.user.ui.presenter.share;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iot.user.app.IOTApplication;
import com.iot.user.base.BasePresenter;
import com.iot.user.constant.Constants;
import com.iot.user.http.NetWorkApi;
import com.iot.user.http.model.BaseResponse;
import com.iot.user.http.net.RxUtils;
import com.iot.user.http.request.main.UnitDevShareRequest;
import com.iot.user.ui.contract.share.DevShareContract;
import com.iot.user.ui.model.share.UnitShareDevModel;
import com.iot.user.utils.MyToast;
import com.iot.user.utils.PrefUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.observers.DisposableObserver;

public class DevSharePresenter extends BasePresenter<DevShareContract.View> implements DevShareContract.Presenter {
    public List<UnitShareDevModel> mDatas=new ArrayList<>();
    @Override
    public void postUnitDevShareList(int pageIndex) {
        if (!isViewAttached()){
            return;
        }
        UnitDevShareRequest request=new UnitDevShareRequest(PrefUtil.getLoginToken(IOTApplication.getIntstance()),
                PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()),pageIndex, Constants.PageSize);
        NetWorkApi.provideRepositoryData().postUnitDevShareList(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        if (pageIndex==1){
                            mDatas.clear();
                        }
                        if (entity.getCode()==0){
                            Map map=(Map) entity.getBody();
                            List dataArr=(List) map.get("list");
                            Gson gson=new Gson();
                            String jsonString = gson.toJson(dataArr);
                            Type type =new TypeToken<List<UnitShareDevModel>>() {}.getType();
                            mDatas.addAll((List<UnitShareDevModel>)gson.fromJson(jsonString, type));
                            if (isViewAttached())mView.onSuccess(entity,"share_dev_list");
                        }else{
                            MyToast.showShortToast(entity.getMessage());
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                       if (isViewAttached())mView.onError(e.getMessage());
                    }
                    @Override
                    public void onComplete() {
                        if (isViewAttached())mView.onComplete();
                    }
                });
    }
}
