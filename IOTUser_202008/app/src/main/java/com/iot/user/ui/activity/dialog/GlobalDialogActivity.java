package com.iot.user.ui.activity.dialog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.afollestad.materialdialogs.MaterialDialog;
import com.iot.user.R;
import com.iot.user.app.IOTApplication;
import com.iot.user.ui.activity.dev.DevDetailGasActivity;
import com.iot.user.utils.DialogUtils;
import com.iot.user.utils.PrefUtil;

public class GlobalDialogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_global_dialog);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        String content=getIntent().getStringExtra("CONTENT");
        final String devNum=getIntent().getStringExtra("DEVNUM");
        final int selectTab=getIntent().getIntExtra("SELECTTAB",0);

        MaterialDialog materialDialog= DialogUtils.showMyDialog(this, "提示", content,
                "查看详情", "我知道了", new DialogUtils.OnDialogClickListener() {
                    @Override
                    public void onConfirm() {
                        finish();
                        Intent intent=new Intent(IOTApplication.getIntstance().getApplicationContext(), DevDetailGasActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("DevNumDetail",devNum);
                        if (PrefUtil.getUnitOperationPermission(IOTApplication.getIntstance())==2) {
                            intent.putExtra("SelectTab", selectTab);
                        }
                        startActivity(intent);
                    }
                    @Override
                    public void onCancel() {
                        finish();
                    }
                });
        materialDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {/***点击空白触发**/
        @Override
        public void onDismiss(DialogInterface dialogInterface) {
            finish();
        }
        });
    }
}