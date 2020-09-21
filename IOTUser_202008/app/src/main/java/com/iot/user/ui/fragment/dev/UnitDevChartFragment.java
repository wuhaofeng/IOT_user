package com.iot.user.ui.fragment.dev;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iot.user.R;
import com.iot.user.app.IOTApplication;
import com.iot.user.base.BaseFragment;
import com.iot.user.databinding.FragmentUnitDevChartBinding;
import com.iot.user.http.NetWorkApi;
import com.iot.user.http.model.BaseResponse;
import com.iot.user.http.net.RxUtils;
import com.iot.user.http.request.dev.CommonDevLowJsonRequest;
import com.iot.user.http.request.dev.UnitDevDetailChartRequest;
import com.iot.user.http.request.main.UnitDevDetailRequest;
import com.iot.user.ui.adapter.dev.DevChartAdapter;
import com.iot.user.ui.adapter.dev.UnitDevChartAdapter;
import com.iot.user.ui.model.dev.DevChartModel;
import com.iot.user.ui.model.dev.UnitDevChartModel;
import com.iot.user.ui.view.dev.DrawLineView;
import com.iot.user.utils.DateUtil;
import com.iot.user.utils.DialogUtils;
import com.iot.user.utils.LogUtil;
import com.iot.user.utils.MyToast;
import com.iot.user.utils.PXTransUtils;
import com.iot.user.utils.PrefUtil;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UnitDevChartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UnitDevChartFragment extends BaseFragment<FragmentUnitDevChartBinding> {
    public UnitDevChartFragment() {
        // Required empty public constructor
    }
    public static UnitDevChartFragment newInstance(String devNum) {
        UnitDevChartFragment fragment=new UnitDevChartFragment();
        fragment.devNum=devNum;
        return fragment;
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_unit_dev_chart;
    }
    HorizontalScrollView scroll_line_view;
    DrawLineView bgLineChart;
    RecyclerView mRecyclerView;
    TextView tv_maxValue;
    TextView tv_maxValue2;
    List<String> xValues;   //x轴数据集合
    List<Float> yValues;  //y轴数据集合
    List<DevChartModel> mDatas=new ArrayList<>();/****182探测器的趋势图***/
    List<UnitDevChartModel> mUnitDatas=new ArrayList<>();/****192探测器的趋势图***/
    public String devNum="";

    /***获取趋势图数据***/
    private Boolean isUnitChart=false;  /***false=182  true=192**/
    private String addressId="";
    private String nodeId="";

    @Override
    protected void initView(View view) {
     scroll_line_view=dataBinding.scrollLineView;
     bgLineChart=dataBinding.bgMarningChart;
     mRecyclerView=dataBinding.rvDevDetailMarning;
     tv_maxValue=dataBinding.tvMaxValue;
     tv_maxValue2=dataBinding.tvMaxValue2;
        if (getArguments()!=null){
            isUnitChart=true;
            addressId=getArguments().getString("DevAddrDetail");
            nodeId=getArguments().getString("DevNodeDetail");
            getUnitNetWorkData(devNum);
            getAlertNumData();
        }else {
            getNetWorkData(devNum);
        }
        initLineChart();
        initRecycleView();
    }
    private void initLineChart(){
        xValues = new ArrayList<>();
        yValues = new ArrayList<>();
        // xy轴集合自己添加数据
        bgLineChart.setXValues(xValues);
        bgLineChart.setYValues(yValues);
    }
    private DevChartAdapter adapter;
    private UnitDevChartAdapter unitAdapter;
    private void initRecycleView(){
        if (isUnitChart==true){
            unitAdapter=new UnitDevChartAdapter(context,mUnitDatas);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            mRecyclerView.setAdapter(unitAdapter);
        }else {
            adapter = new DevChartAdapter(context, mDatas);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            mRecyclerView.setAdapter(adapter);
        }
    }

    @SuppressLint("AutoDispose")
    private void getAlertNumData(){
        CommonDevLowJsonRequest request=new CommonDevLowJsonRequest(devNum,
                PrefUtil.getLoginToken(IOTApplication.getIntstance()),PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()));
        NetWorkApi.provideRepositoryData().postDevChartAlarmGasvalue(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        if (entity.getCode()==0){
                            Map body=(Map) entity.getBody();
                            String devHigh=(String) body.get("ah");
                            String devLow=(String) body.get("al");
                            tv_maxValue.setText("预警线:"+devLow);
                            tv_maxValue2.setText("   报警线:"+devHigh);
                            bgLineChart.setYHoldValue(Float.valueOf(devLow));
                            bgLineChart.setYHoldMaxValue(Float.valueOf(devHigh));
                            bgLineChart.requestLayout();
                            bgLineChart.invalidate();
                        }else {
                            MyToast.showShortToast(entity.getMessage());
                            LogUtil.e("getCode!=0, entity.getMessage == " + entity.getMessage());
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


    @SuppressLint("AutoDispose")
    private  void getNetWorkData(String devNum){/**182燃气趋势图***/
        showProgressDialog();
        UnitDevDetailRequest request = new UnitDevDetailRequest(
                PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()),
                PrefUtil.getLoginToken(IOTApplication.getIntstance()),
                devNum);
        NetWorkApi.provideRepositoryData().postDevGasAlarm(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        if (entity.getCode() == 0){

                            Gson gson=new Gson();
                            Map data=gson.fromJson(entity.getBody().toString(),Map.class);
                            Type type =new TypeToken<List<DevChartModel>>() {}.getType();
                            List<DevChartModel> userList = gson.fromJson(data.get("gasLine").toString(), type);
                            for (int i=userList.size()-1;i>=0;i--){
                                DevChartModel model=userList.get(i);
                                String dateStr=new BigDecimal(String.valueOf(model.getReport_time())).toPlainString();
                                model.setDateStr(DateUtil.getYearDayTime(Long.valueOf(dateStr)));
                                if (model.getDateStr().length()>11) {
                                    xValues.add(model.getDateStr().substring(11));
                                }
                                String gasNum=String.valueOf(model.getGasvalue());
                                yValues.add(Float.valueOf(gasNum));
                                bgLineChart.setXValues(xValues);
                                bgLineChart.setYValues(yValues);
                                bgLineChart.requestLayout();
                                bgLineChart.invalidate();
                            }
                            adapter.updateDataWith(userList);
                            scrollToRight();
                        }else {
                            MyToast.showShortToast(entity.getMessage());
                            LogUtil.e("getCode!=0, entity.getMessage == " + entity.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dissmissProgressDialog();
                    }

                    @Override
                    public void onComplete() {
                        dissmissProgressDialog();
                    }
                });
    }

    @SuppressLint("AutoDispose")
    private  void getUnitNetWorkData(String devNum){/**192商用趋势图***/
        showProgressDialog();
        UnitDevDetailChartRequest request = new UnitDevDetailChartRequest(
                PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()),
                PrefUtil.getLoginToken(IOTApplication.getIntstance()),
                devNum,addressId,nodeId,"48");
        NetWorkApi.provideRepositoryData().postUnitDevChart(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        if (entity.getCode() == 0){

                            List data=(List) entity.getBody();
                            Gson gson=new Gson();
                            String jsonString = gson.toJson(data);
                            Type type =new TypeToken<List<UnitDevChartModel>>() {}.getType();
                            List<UnitDevChartModel> userList = gson.fromJson(jsonString, type);
                            if (userList==null||userList.size()==0){
                                MyToast.showShortToast("未获取到数据");
                                return;
                            }
                            for (int i=userList.size()-1;i>=0;i--){
                                UnitDevChartModel model=userList.get(i);
                                String dateStr=new BigDecimal(String.valueOf(model.getCreatetime())).toPlainString();
                                model.setDateStr(DateUtil.getYearDayTime(Long.valueOf(dateStr)));
                                if (model.getDateStr().length()>11) {
                                    xValues.add(model.getDateStr().substring(11));
                                }
                                String gasNum=String.valueOf(model.getGasvalue());
                                yValues.add(Float.valueOf(gasNum));
                                bgLineChart.setXValues(xValues);
                                bgLineChart.setYValues(yValues);
                                bgLineChart.requestLayout();
                                bgLineChart.invalidate();
                            }
                            unitAdapter.updateDataWith(userList);
                            scrollToRight();
                        }else if(entity.getCode()==108){
                            DialogUtils.showMyDialog(getContext(), "提示", entity.getMessage(),
                                    "确定", "取消", new DialogUtils.OnDialogClickListener() {
                                        @Override
                                        public void onConfirm() {
                                        }
                                        @Override
                                        public void onCancel() {

                                        }
                                    });
                        }else {
                            MyToast.showShortToast(entity.getMessage());
                            LogUtil.e("getCode!=0, entity.getMessage == " + entity.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dissmissProgressDialog();
                    }

                    @Override
                    public void onComplete() {
                        dissmissProgressDialog();
                    }
                });

    }

    /**延迟2秒后刷新***/
    private void scrollToRight(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            @Override
            public void run() {
                scroll_line_view.scrollTo(xValues.size()* PXTransUtils.dp2px(IOTApplication.getIntstance(),50),0);
            }
        },1500);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        //ButterKnife.unbind(this);
        getActivity().finish();
    }

}