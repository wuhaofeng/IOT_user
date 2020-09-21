package com.iot.user.ui.fragment.mine;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.internal.LinkedTreeMap;
import com.iot.user.R;
import com.iot.user.app.IOTApplication;
import com.iot.user.base.BaseMvpFragment;
import com.iot.user.databinding.FragmentUnitMineBinding;
import com.iot.user.http.model.BaseResponse;
import com.iot.user.ui.activity.mine.AboutUsActivity;
import com.iot.user.ui.activity.mine.AlertSettingActivity;
import com.iot.user.ui.activity.mine.CommonSettingActivity;
import com.iot.user.ui.activity.mine.PersonInfoActivity;
import com.iot.user.ui.activity.mine.RepairActivity;
import com.iot.user.ui.activity.mine.ShareDeviceActivity;
import com.iot.user.ui.activity.shopping.ShoppingDeviceListActivity;
import com.iot.user.ui.activity.shopping.ShoppingOrderListActivity;
import com.iot.user.ui.contract.mine.UnitMineContract;
import com.iot.user.ui.presenter.mine.UnitMinePresenter;
import com.iot.user.ui.view.mine.MySettingItemView;
import com.iot.user.utils.AppUpdateUtils;
import com.iot.user.utils.DoubleClickUtil;
import com.iot.user.utils.IntentUtils;
import com.iot.user.utils.PXTransUtils;
import com.iot.user.utils.PrefUtil;
import com.iot.user.utils.ZXingUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UnitMineFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UnitMineFragment extends BaseMvpFragment<UnitMinePresenter, FragmentUnitMineBinding> implements UnitMineContract.View {

    public UnitMineFragment() {
    }
    public static UnitMineFragment newInstance() {
        UnitMineFragment fragment = new UnitMineFragment();
        return fragment;
    }
    LinearLayout linearL_mine_bg;
    TextView tv_nickname;
    TextView tv_my_phone;
    TextView tv_my_test;
    TextView tv_my_family_dev_num;
    Button btn_right_qrcode;
    MySettingItemView item_app_setting;

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_unit_mine;
    }

    @Override
    protected void initView(View view) {
        mPresenter=new UnitMinePresenter();
        mPresenter.attachView(this);
      linearL_mine_bg=dataBinding.linearLMineBg;
      tv_nickname=dataBinding.tvMyNickname;
      tv_my_phone=dataBinding.tvMyPhone;
      tv_my_test=dataBinding.tvMyTest;
      tv_my_family_dev_num=dataBinding.tvMyFamilyDevNum;
      btn_right_qrcode=dataBinding.btnRightQrcode;
      item_app_setting=dataBinding.itemAppSetting;
    }
    @Override
    public void onResume() {
        super.onResume();
        initOtherViews();
    }
    private void initOtherViews() {
        int newversion = PrefUtil.getNewVersion(IOTApplication.getIntstance());
        if (IOTApplication.getVersionCode() < newversion) {
            item_app_setting.setRedDot(true);
        } else {
            item_app_setting.setRedDot(false);
        }
        mPresenter.postUnitMineUserInfo();
    }

    @Override
    public void onSuccess(BaseResponse bean, String type) {
     if (type.equals("user_info")){
         LinkedTreeMap dataDic=(LinkedTreeMap) bean.getBody();
         tv_nickname.setText(PrefUtil.getNickname(IOTApplication.getIntstance()));
         tv_my_phone.setText(PrefUtil.getPhone(IOTApplication.getIntstance()));
         if (PrefUtil.getServerIP(IOTApplication.getIntstance()).equals("pub.shmsiot.top")){
             tv_my_test.setVisibility(View.GONE);
         }else{
             tv_my_test.setVisibility(View.VISIBLE);
         }
         tv_my_family_dev_num.setText(((Double)dataDic.get("familyCount")).intValue()+"个家庭|"+((Double)dataDic.get("devCount")).intValue()+"个设备");
         Bitmap bm= ZXingUtil.createQRCodeBitmap((String)dataDic.get("phone"), PXTransUtils.dp2px(context,65),PXTransUtils.dp2px(context,65),"UTF-8","H", "1", Color.WHITE, Color.TRANSPARENT);
         BitmapDrawable bd=new BitmapDrawable(bm);
         btn_right_qrcode.setBackground(bd);
     }
    }


    @Override
    protected void initClickBtn() {
        super.initClickBtn();
        btn_right_qrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scopeBtn();
            }
        });
        dataBinding.selfInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoSelfInfo();
            }
        });
        dataBinding.itemContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item_contact_us();
            }
        });
        dataBinding.itemDevBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item_dev_buy();
            }
        });
        dataBinding.itemOrderManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item_order_manager();
            }
        });
        dataBinding.itemRepairSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item_repair_setting();
            }
        });
        dataBinding.itemAlertSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item_alert_setting();
            }
        });
        dataBinding.itemFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item_feedback();
            }
        });
        dataBinding.itemAppUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAppUpdate();
            }
        });
        dataBinding.itemDevSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item_dev_support();
            }
        });
        dataBinding.itemAppSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item_app_setting();
            }
        });


    }

    AppUpdateUtils appUpdateUtils=null;/**app更新***/
    void checkAppUpdate(){
        if (appUpdateUtils==null){
            appUpdateUtils= new AppUpdateUtils(context);
            appUpdateUtils.appUpdateCheck();
        }else {
            if (appUpdateUtils.isCheckUpdate==false){
                appUpdateUtils.appUpdateCheck();
            }
        }
    }
    void scopeBtn(){/**成员二维码**/
        if(!DoubleClickUtil.isFastDoubleClick(R.id.btn_right_qrcode)){
            Intent intent =new Intent(getActivity(), ShareDeviceActivity.class);
            intent.putExtra(ShareDeviceActivity.STATUS,"2");
            intent.putExtra(ShareDeviceActivity.DEV_NUM,PrefUtil.getPhone(IOTApplication.getIntstance()));
            getActivity().startActivity(intent);
        }
    }
    /**
     * 跳转个人设置
     */
    void gotoSelfInfo() {
        if(!DoubleClickUtil.isFastDoubleClick(R.id.self_info)){
            getActivity().startActivity(new Intent(getActivity(), PersonInfoActivity.class));
        }
    }
    /**
     * 跳转联系我们界面
     */
    void item_contact_us() {
        if(!DoubleClickUtil.isFastDoubleClick(R.id.item_contact_us)){
            getActivity().startActivity(new Intent(getActivity(), AboutUsActivity.class));
        }

    }
    /**
     * 跳转设备续费界面
     */
    void item_dev_buy() {
        if(!DoubleClickUtil.isFastDoubleClick(R.id.item_dev_buy)){
            getActivity().startActivity(new Intent(getActivity(), ShoppingDeviceListActivity.class));
        }
    }

    /**
     * 跳转订单管理界面
     */
    void item_order_manager() {
        if(!DoubleClickUtil.isFastDoubleClick(R.id.item_order_manager)){
            getActivity().startActivity(new Intent(getActivity(), ShoppingOrderListActivity.class));
        }
    }

    /**
     * 跳转报修界面
     */
    void item_repair_setting() {
        if(!DoubleClickUtil.isFastDoubleClick(R.id.item_repair_setting)){
            getActivity().startActivity(new Intent(getActivity(), RepairActivity.class));
        }
    }

    /**
     * 跳转报警设置界面
     */
    void item_alert_setting() {
        if(!DoubleClickUtil.isFastDoubleClick(R.id.item_alert_setting)){
            getActivity().startActivity(new Intent(getActivity(), AlertSettingActivity.class));
        }
    }

    /**
     * 跳转用户反馈界面
     */
    void item_feedback() {
        if(!DoubleClickUtil.isFastDoubleClick(R.id.item_feedback)){
//            getActivity().startActivity(new Intent(getActivity(), FeedbackActivity.class));
        }
    }
    /**常见问题**/
    void item_dev_support() {
        IntentUtils.startToWebActivity(getActivity(), "", "常见问题", "http://www.shmsiot.icoc.bz");
    }
    /**常见问题**/
    void item_app_setting() {
        Intent intent = new Intent(getActivity(), CommonSettingActivity.class);
        startActivity(intent);
    }
}