package com.iot.user.ui.activity.message;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.iot.user.R;
import com.iot.user.base.BaseActivity;
import com.iot.user.databinding.ActivityUnitMessageDetailBinding;
import com.iot.user.ui.adapter.message.SingleReatineListAdapter;
import com.iot.user.ui.fragment.dev.UnitDevAlertDescFragment;
import com.iot.user.ui.model.message.ReatineInfoMap;
import com.iot.user.ui.model.message.UnitDevAlertDescModel;
import com.iot.user.ui.model.message.UnitMessageHistoryModel;
import com.iot.user.ui.model.message.UnitMessageModel;
import com.iot.user.utils.DateUtil;
import com.iot.user.utils.DoubleClickUtil;
import com.iot.user.utils.MyToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class UnitMessageDetailActivity extends BaseActivity<ActivityUnitMessageDetailBinding> {

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_unit_message_detail;
    }
    Toolbar mToolbar;
    ListView lv_reatine;
    TextView tv_content;
    TextView tv_address;
    TextView tv_devnum;
    TextView tv_time;
    LinearLayout ll_dev_content;
    UnitMessageHistoryModel detailModel=null;
    UnitMessageModel messageModel=null;
    UnitDevAlertDescModel devAlertDescModel=null;
    public static final String REATINE_HISTORY = "DevReatineHistory";
    public static final String REATINE_INFO = "ReatineInfo";
    public static final String REATINE_DescInfo = "ReatineDescInfo";
    private List<ReatineInfoMap> mDatas = new ArrayList<>();
    private ReatineInfoMap reatineInfoMap;
    private SingleReatineListAdapter singleReatineListAdapter;

    @Override
    public void initView() {
        detailModel = (UnitMessageHistoryModel)getIntent().getSerializableExtra(REATINE_HISTORY);
        messageModel =  (UnitMessageModel)getIntent().getSerializableExtra(REATINE_INFO);
        devAlertDescModel=(UnitDevAlertDescModel)getIntent().getParcelableExtra(REATINE_DescInfo);
        mToolbar=(Toolbar)dataBinding.toolbar;
        lv_reatine=dataBinding.lvReatine;
        tv_address=dataBinding.tvAddress;
        tv_content=dataBinding.tvContent;
        tv_devnum=dataBinding.tvDevnum;
        tv_time=dataBinding.tvTime;
        ll_dev_content=dataBinding.llDevContent;
        initMyToolBar();
        initBaseData();
        singleReatineListAdapter = new SingleReatineListAdapter(UnitMessageDetailActivity.this,mDatas);
        lv_reatine.setAdapter(singleReatineListAdapter);
    }
    private void initBaseData(){
        if(messageModel!=null){
            ll_dev_content.setVisibility(View.VISIBLE);
            tv_devnum.setText("编号:"+messageModel.getDevNum());
            tv_address.setText("位置:"+messageModel.getFinalAddress());
            if (messageModel.getDevType().equals("192")){
                tv_content.setText("内容:点击查看全部");
                tv_content.setTextColor(Color.RED);
                tv_content.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clickDevDetailStatus();
                    }
                });
            }else{
                if(messageModel.getInfoDescription()!=null && !"".equals(messageModel.getInfoDescription())){
                    tv_content.setText("内容:"+messageModel.getInfoDescription());
                    tv_content.setVisibility(View.VISIBLE);
                }else{
                    tv_content.setVisibility(View.GONE);
                }
            }
            tv_time.setText("时间:"+ DateUtil.getYearDayTime(messageModel.getAt()));
        }else{
            ll_dev_content.setVisibility(View.GONE);
        }
        reatineInfoMap = new ReatineInfoMap("上报时间", DateUtil.getYearDayTime(detailModel.getAt()));
        mDatas.add(reatineInfoMap);
        reatineInfoMap = new ReatineInfoMap("IMEI", detailModel.getImei());
        mDatas.add(reatineInfoMap);
        reatineInfoMap = new ReatineInfoMap("信号强度", detailModel.getCsq());
        mDatas.add(reatineInfoMap);
        if (!messageModel.getDevType().equals("192")) {
            reatineInfoMap = new ReatineInfoMap("燃气值(%LEL)", detailModel.getGasValue());
            mDatas.add(reatineInfoMap);
            reatineInfoMap = new ReatineInfoMap("温度(℃)", detailModel.getTemp());
            mDatas.add(reatineInfoMap);
            reatineInfoMap = new ReatineInfoMap("湿度(%)", detailModel.getHum());
            mDatas.add(reatineInfoMap);
        }
    }

    private void clickDevDetailStatus(){/***点击设备状态***/
        if (devAlertDescModel==null||devAlertDescModel.getTitle()==null){
            if (messageModel.getInfoDescription()!=null){
                devAlertDescModel=null;
            }else {
                MyToast.showShortToast("未获取到数据");
                return;
            }
        }
        UnitDevAlertDescFragment descFragment=UnitDevAlertDescFragment.newInstance(messageModel.getInfoDescription(),devAlertDescModel);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.ll_bg,descFragment, null)
                .addToBackStack(null)
                .commit();
    }

    private void initMyToolBar() {
        if(messageModel!=null && messageModel.getInfoType()!=null&&!"".equals(messageModel.getInfoType())){
            initToolBar(mToolbar, "消息详情", R.drawable.gank_ic_back_white);
        }else{
            initToolBar(mToolbar, "消息详情", R.drawable.gank_ic_back_white);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initClickBtn() {
        super.initClickBtn();
        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInfos();
            }
        });
    }
    void showInfos(){
        if(DoubleClickUtil.getClickTimes() >= 5){
            DoubleClickUtil.clearCount();
            if(detailModel!=null){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("实时信息详情");
                builder.setMessage(detailModel.getStation());
                //点击对话框以外的区域是否让对话框消失
                builder.setCancelable(false);
                //设置正面按钮
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                try {
                    dialog .show();
                }catch (Exception e){

                }

            }
        }
    }
}