package com.iot.user.ui.activity.mine;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.iot.user.R;
import com.iot.user.app.IOTApplication;
import com.iot.user.base.BaseActivity;
import com.iot.user.databinding.ActivityAboutUsBinding;
import com.iot.user.http.NetWorkApi;
import com.iot.user.http.model.BaseResponse;
import com.iot.user.http.net.RxUtils;
import com.iot.user.skin.SkinManager;
import com.iot.user.ui.model.login.LoginModel;
import com.iot.user.ui.model.mine.UnitAboutModel;
import com.iot.user.ui.model.mine.VersionJsonModel;
import com.iot.user.utils.DoubleClickUtil;
import com.iot.user.utils.MyToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.observers.DisposableObserver;
import retrofit2.Response;

public class AboutUsActivity extends BaseActivity<ActivityAboutUsBinding> {
    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_about_us;
    }
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_app_version)
    TextView tvAppVersion;
    @BindView(R.id.tv_company)
    TextView tv_company;
    @BindView(R.id.tv_website1)
    TextView tv_website1;
    @BindView(R.id.tv_website2)
    TextView tv_website2;
    @BindView(R.id.tv_email)
    TextView tv_email;
    @BindView(R.id.tv_tel)
    TextView tv_tel;//客服
    @BindView(R.id.tv_tel1)
    TextView tv_tel1;//监控

    @Override
    public void initView() {
        ButterKnife.bind(this);
        initToolBar();
        initAppVersionName();
        getDetailInfo();
    }
    private void initAppVersionName() {
        tvAppVersion.setText("version " + IOTApplication.getVersionName());
    }

    private void initToolBar() {
        int currentSkinType = SkinManager.getCurrentSkinType(this);
        if (SkinManager.THEME_DAY == currentSkinType) {
            initToolBar(toolbar, getString(R.string.about), R.drawable.gank_ic_back_white);
        } else {
            initToolBar(toolbar, getString(R.string.about), R.drawable.gank_ic_back_night);
        }
    }
    @SuppressLint("AutoDispose")
    private void getDetailInfo(){
        NetWorkApi.provideRepositoryData().getEnterpriseInfo()
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<Response<BaseResponse>>(){
                    @Override
                    public void onNext(Response<BaseResponse> response) {
                        BaseResponse entity=response.body();
                        if (entity.getCode() == 0) {
                            LinkedTreeMap map=(LinkedTreeMap)entity.getBody();
                            Gson gson=new Gson();
                            String jsonString = gson.toJson(map);
                            UnitAboutModel resp=gson.fromJson(jsonString,UnitAboutModel.class);
                            tv_company.setText(resp.getEnterpriseName());
                            int subIndex=resp.getEnterpriseWebsite().indexOf(";");
                            String web1=resp.getEnterpriseWebsite().substring(0,subIndex);
                            String web2="";
                            if (resp.getEnterpriseWebsite().length()>=subIndex+1) {
                                web2=resp.getEnterpriseWebsite().substring(subIndex + 1);
                            }
                            tv_website1.setText(web1);
                            if (web2==null){
                                tv_website2.setVisibility(View.GONE);
                            }else{
                                tv_website2.setText(web2);
                                tv_website2.setVisibility(View.VISIBLE);
                            }
                            tv_email.setText(resp.getEnterpriseEmail());
                            tv_tel.setText(resp.getServicePhone());
                            tv_tel1.setText(resp.getMonitorPhone());
                        } else {
                            MyToast.showShortToast(entity.getMessage());
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

    /**
     * 点击5次获取服务版本
     */
    @SuppressLint("AutoDispose")
    @OnClick(R.id.iv_logo)
    public void iv_logo(){
        if(DoubleClickUtil.getClickTimes() >= 5){
            DoubleClickUtil.clearCount();
            NetWorkApi.provideRepositoryData().getVersion()
                    .compose(RxUtils.schedulersTransformer()) //线程调度
                    .subscribe(new DisposableObserver<BaseResponse>() {
                        @Override
                        public void onNext(BaseResponse entity) {
                            if (entity.getCode()==0){
                                LinkedTreeMap map=(LinkedTreeMap)entity.getBody();
                                Gson gson=new Gson();
                                String jsonString = gson.toJson(map);
                                VersionJsonModel resp=gson.fromJson(jsonString, VersionJsonModel.class);
                                AlertDialog.Builder builder = new AlertDialog.Builder(AboutUsActivity.this);
                                builder.setTitle("后台服务版本号");
                                builder.setMessage(resp.toString());
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
                            }else{
                                MyToast.showShortToast(entity.getMessage());
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

}