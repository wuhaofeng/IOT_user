package com.iot.user.ui.activity.share;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.iot.user.R;
import com.iot.user.base.BaseActivity;
import com.iot.user.databinding.ActivityUnitShareBinding;
import com.iot.user.skin.SkinManager;

public class UnitShareActivity extends BaseActivity<ActivityUnitShareBinding> {
    RelativeLayout rl_family_share;
    RelativeLayout rl_dev_share;
    Toolbar mToolbar;
    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_unit_share;
    }

    @Override
    public void initView() {
        rl_dev_share=dataBinding.rlDevShare;
        rl_family_share=dataBinding.rlFamilyShare;
        mToolbar=(Toolbar)dataBinding.toolbar;
        initMyToolBar();
    }
    private void initMyToolBar() {
        int currentSkinType = SkinManager.getCurrentSkinType(this);
        if (SkinManager.THEME_DAY == currentSkinType) {
            initToolBar(mToolbar, "共享", R.drawable.gank_ic_back_white);
        } else {
            initToolBar(mToolbar, "共享", R.drawable.gank_ic_back_night);
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
        rl_family_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickFamilyShare();
            }
        });
        rl_dev_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickDevShare();
            }
        });
    }
    void clickFamilyShare(){
//        Intent intent = new Intent(UnitShareActivity.this, UnitFamilyListActivity.class);
//        startActivity(intent);
    }
    void clickDevShare(){
        Intent intent = new Intent(UnitShareActivity.this, UnitDevShareActivity.class);
        startActivity(intent);
    }
}