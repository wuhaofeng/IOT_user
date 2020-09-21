package com.iot.user.ui.fragment.dev;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iot.user.R;
import com.iot.user.app.IOTApplication;
import com.iot.user.base.BaseFragment;
import com.iot.user.databinding.FragmentUnitDevDetailBinding;
import com.iot.user.http.NetWorkApi;
import com.iot.user.http.model.BaseResponse;
import com.iot.user.http.net.RxUtils;
import com.iot.user.http.request.dev.CommonDevLowJsonRequest;
import com.iot.user.ui.model.main.UnitDevDetailModel;
import com.iot.user.ui.presenter.dev.DevDetailPresenter;
import com.iot.user.utils.DateUtil;
import com.iot.user.utils.MyToast;
import com.iot.user.utils.PrefUtil;
import com.iot.user.utils.Util;

import butterknife.BindView;
import io.reactivex.observers.DisposableObserver;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UnitDevDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UnitDevDetailFragment extends BaseFragment<FragmentUnitDevDetailBinding> {
    public UnitDevDetailFragment() {
    }
    public static UnitDevDetailFragment newInstance(String devNum, DevDetailPresenter presenter) {
        UnitDevDetailFragment fragment = new UnitDevDetailFragment();
        fragment.devNum=devNum;
        fragment.mPresenter=presenter;
        if (devNum.length()>4){
            fragment.devStatus=devNum.substring(1,4);
        }
        return fragment;
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_unit_dev_detail;
    }
    TextView btn_dev_clicket_close;
    TextView tv_user_name;
    TextView tv_user_phone;
    TextView tv_user_address;
    TextView tv_dev_detail_num;
    TextView tv_dev_detail_name;

    TextView tv_dev_detail_status;
    ImageView iv_dev_detail_status;
    TextView tv_dev_detail_csq;
    TextView tv_dev_detail_temp;
    TextView tv_dev_detail_temp_title;
    TextView tv_dev_detail_gas;
    TextView tv_dev_detail_gas_title;

    TextView tv_dev_detail_update;
    TextView tv_dev_detail_online;
    TextView tv_dev_detail_expire;
    LinearLayout ll_close_voice;
    LinearLayout ll_submit_know;

    private DevDetailPresenter mPresenter;
    private String devNum="";
    private String devStatus="182";

    @Override
    protected void initView(View view) {
        btn_dev_clicket_close=dataBinding.btnDevClicketClose;
        tv_user_name=dataBinding.tvUserName;
        tv_user_phone=dataBinding.tvUserPhone;
        tv_user_address=dataBinding.tvUserAddress;
        tv_dev_detail_num=dataBinding.tvDevDetailNum;
        tv_dev_detail_name=dataBinding.tvDevDetailName;
        tv_dev_detail_status=dataBinding.tvDevDetailStatus;
        iv_dev_detail_status=dataBinding.ivDevDetailStatus;
        tv_dev_detail_csq=dataBinding.tvDevDetailCsq;
        tv_dev_detail_temp=dataBinding.tvDevDetailTemp;
        tv_dev_detail_temp_title=dataBinding.tvDevDetailTempTitle;
        tv_dev_detail_gas=dataBinding.tvDevDetailGas;
        tv_dev_detail_gas_title=dataBinding.tvDevDetailGasTitle;
        tv_dev_detail_update=dataBinding.tvDevDetailUpdate;
        tv_dev_detail_online=dataBinding.tvDevDetailOnline;
        tv_dev_detail_expire=dataBinding.tvDevDetailExpire;
        ll_close_voice=dataBinding.llCloseVoice;
        ll_submit_know=dataBinding.llSubmitKnow;
    }
    @Override
    public void onResume() {
        super.onResume();
        mPresenter.getDevInfoWithDevNum(devNum);
    }

    public void updateViews(UnitDevDetailModel devDetailModel){
        tv_user_name.setText(devDetailModel.getNickname()==null?"":devDetailModel.getNickname());
        tv_user_phone.setText(devDetailModel.getPhone()==null?"":devDetailModel.getPhone());
        tv_user_address.setText(devDetailModel.getAddress()==null?"":devDetailModel.getAddress());
        tv_dev_detail_num.setText(devDetailModel.getDevNum()==null?"":devDetailModel.getDevNum());
        tv_dev_detail_name.setText(devDetailModel.getFriend_name()==null?"":devDetailModel.getFriend_name());
        tv_dev_detail_csq.setText(devDetailModel.getCsq()==null?"":devDetailModel.getCsq());
        if (devStatus.equals("192")) {
            btn_dev_clicket_close.setVisibility(View.GONE);
            tv_dev_detail_temp_title.setText("场所");
            tv_dev_detail_gas_title.setText("基站编号");
            tv_dev_detail_gas.setText(devDetailModel.getStation()==null?"":devDetailModel.getStation());
            tv_dev_detail_temp.setText(devDetailModel.getPlace_name()==null?"":devDetailModel.getPlace_name());
            iv_dev_detail_status.setVisibility(View.VISIBLE);
            tv_dev_detail_status.setText("点击查看全部");
        }else {
            btn_dev_clicket_close.setVisibility(View.VISIBLE);
            tv_dev_detail_gas.setText(devDetailModel.getGasvalue()==null?"":devDetailModel.getGasvalue());
            tv_dev_detail_temp.setText(devDetailModel.getTem()==null?"":devDetailModel.getTem());
            iv_dev_detail_status.setVisibility(View.VISIBLE);
            tv_dev_detail_status.setText(mPresenter.devDescStr);
        }
        if (devDetailModel.getUpdateDataTime()!=null){
            String updateTime= Util.getTwoNumFloatWith(devDetailModel.getUpdateDataTime(),false);
            tv_dev_detail_update.setText(DateUtil.getYearDayTime(updateTime));
        }else {
            tv_dev_detail_update.setText("");
        }
        if (devDetailModel.getLoginDataTime()!=null){
            String updateTime=Util.getTwoNumFloatWith(devDetailModel.getLoginDataTime(),false);
            tv_dev_detail_online.setText(DateUtil.getYearDayTime(updateTime));
        }else {
            tv_dev_detail_online.setText("");
        }
        if (devDetailModel.getExpireTime()!=null){
            String updateTime=Util.getTwoNumFloatWith(devDetailModel.getExpireTime(),false);
            tv_dev_detail_expire.setText(DateUtil.getYearDayTime(updateTime));
        }else {
            tv_dev_detail_expire.setText("");
        }
/***报警设置**/
        if (mPresenter.alertStatus.equals("1")){
            ll_close_voice.setVisibility(View.VISIBLE);
            ll_submit_know.setVisibility(View.VISIBLE);
            if (mPresenter.alertType.equals("1")){/***点击了我知道了**/
                ll_submit_know.setVisibility(View.GONE);
            }else if (mPresenter.alertType.equals("2")){
                ll_close_voice.setVisibility(View.GONE);
                ll_submit_know.setVisibility(View.GONE);
            }else {
                ll_close_voice.setVisibility(View.VISIBLE);
                ll_submit_know.setVisibility(View.VISIBLE);
            }
        }else {
            ll_close_voice.setVisibility(View.GONE);
            ll_submit_know.setVisibility(View.GONE);
        }
    }

    /**延迟3秒后刷新***/
    public   void updateData(final String mute){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dissmissProgressDialog();
                if (mute.equals("1")){
                    mPresenter.alertType="1";
                    updateViews(mPresenter.devDetailModel);
                }else {
                    mPresenter.alertType="2";
                    updateViews(mPresenter.devDetailModel);
                }
                mPresenter.getDevInfoWithDevNum(devNum);
            }
        },2000);
    }

    @Override
    protected void initClickBtn() {
        super.initClickBtn();
        tv_dev_detail_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickDevDetailStatus();
            }
        });
        btn_dev_clicket_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickDevClicketCloseBtn();
            }
        });
        dataBinding.btnCloseVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            mPresenter.dealUnitDev(devNum,"2",mPresenter.devDetailModel.getMsgNum());
            }
        });
        dataBinding.btnSubmitKnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.dealUnitDev(devNum,"1",mPresenter.devDetailModel.getMsgNum());
            }
        });
    }
    void clickDevDetailStatus(){/***点击设备状态***/
        if (mPresenter.devDescModel==null||mPresenter.devDescModel.getTitle()==null){
            if (mPresenter.devDescStr==null||mPresenter.devDescStr.equals("")) {
                MyToast.showShortToast("未获取到数据");
                return;
            }
        }
        UnitDevAlertDescFragment descFragment=UnitDevAlertDescFragment.newInstance(mPresenter.devDescStr,mPresenter.devDescModel);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.rl_unit_dev_detail,descFragment, null)
                .addToBackStack(null)
                .commit();
    }
    void clickDevClicketCloseBtn(){
        mPresenter.clickDevClicketCloseBtn(devNum);
    }

}