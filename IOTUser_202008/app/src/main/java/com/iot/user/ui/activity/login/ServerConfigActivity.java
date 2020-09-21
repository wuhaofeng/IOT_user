package com.iot.user.ui.activity.login;

import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.iot.user.R;
import com.iot.user.base.BaseActivity;
import com.iot.user.databinding.ActivityServerConfigBinding;
import com.iot.user.http.NetWorkApi;
import com.iot.user.http.net.RetrofitClient;
import com.iot.user.skin.SkinManager;
import com.iot.user.utils.AppValidationMgr;
import com.iot.user.utils.DialogUtils;
import com.iot.user.utils.KeyboardUtils;
import com.iot.user.utils.MyToast;
import com.iot.user.utils.PrefUtil;

public class ServerConfigActivity extends BaseActivity<ActivityServerConfigBinding> {
    Toolbar mToolbar;
    EditText et_server_address;
    EditText et_server_port;
    EditText et_server_version;
    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_server_config;
    }

    @Override
    public void initView() {
      mToolbar=(Toolbar) dataBinding.toolbar;
      et_server_address=dataBinding.etServerAddress;
      et_server_port=dataBinding.etServerPort;
      et_server_version=dataBinding.etServerVersion;
        initMyToolBar();
        String server = PrefUtil.getServerIP(ServerConfigActivity.this);
        et_server_address.setText(server);
        if(server!=null&&server.length()>0){
            et_server_address.setSelection(server.length());
        }
        String port = PrefUtil.getServerPort(ServerConfigActivity.this);
        et_server_port.setText(port);
        String serverVersion = PrefUtil.getServerVersion(ServerConfigActivity.this);
        et_server_version.setText(serverVersion);
    }
    private void initMyToolBar() {
        int currentSkinType = SkinManager.getCurrentSkinType(this);
        if (SkinManager.THEME_DAY == currentSkinType) {
            initToolBar(mToolbar, "服务器配置", R.drawable.gank_ic_back_white);
        } else {
            initToolBar(mToolbar, "服务器配置", R.drawable.gank_ic_back_night);
        }
    }

    @Override
    protected void initClickBtn() {
        super.initClickBtn();
        dataBinding.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_ok();
            }
        });
    }
    public void btn_ok() {
        //隐藏键盘
        KeyboardUtils.hideSoftInput(this);
        DialogUtils.showMyDialog(mContext, "提示", "确定要修改服务器配置吗?", "确定", "取消", new DialogUtils.OnDialogClickListener() {
            @Override
            public void onConfirm() {
                configServer();

            }
            @Override
            public void onCancel() {
            }
        });

    }

    private void configServer(){
        //获取服务器
        String server = et_server_address.getText().toString().trim();
        //获取端口
        String port = et_server_port.getText().toString().trim();
        //服务版本号
        String serverVersion = et_server_version.getText().toString();

        if(server == null || "".equals(server)){
            MyToast.showShortToast("服务器IP不能为空");
            return;
        }
        if(port == null || "".equals(port)){
            MyToast.showShortToast("服务器端口不能为空");
            return;
        }
        if(serverVersion == null || "".equals(serverVersion)){
            MyToast.showShortToast("服务器协议版本不能为空");
            return;
        }
        if (AppValidationMgr.isIpAddress(server)||AppValidationMgr.isIpUrlAddress(server)){
            PrefUtil.setServerIP(server,ServerConfigActivity.this);
            PrefUtil.setServerPort(port,ServerConfigActivity.this);
            PrefUtil.setServerVersion(serverVersion,ServerConfigActivity.this);
            /**保存后的处理***/
            RetrofitClient.restoreInstance();//重置IOTHelper
            NetWorkApi.destroyInstance();
            MyToast.showShortToast("服务器保存成功,请重新登录");
            finish();
        }else{
            MyToast.showShortToast("请输入正确的服务器IP");
        }
    }

}