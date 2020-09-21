package com.iot.user.ui.activity.dev;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;

import com.iot.user.R;
import com.iot.user.base.BaseActivity;
import com.iot.user.databinding.ActivityUnitDevChartBinding;
import com.iot.user.skin.SkinManager;
import com.iot.user.ui.fragment.dev.UnitDevChartFragment;

public class UnitDevChartActivity extends BaseActivity<ActivityUnitDevChartBinding> {
    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_unit_dev_chart;
    }
    Toolbar mToolbar;
    String devNum="";
    String hAddrId="";
    String nodeId="";
    @Override
    public void initView() {
        mToolbar=(Toolbar) dataBinding.toolbar;
        devNum=getIntent().getStringExtra("DevNumDetail");
        hAddrId=getIntent().getStringExtra("DevAddrDetail");
        nodeId=getIntent().getStringExtra("DevNodeDetail");
        jumpToDevChart();
        initMyToolBar();
    }
    private void jumpToDevChart(){/***跳转到趋势图**/
        UnitDevChartFragment fragment=UnitDevChartFragment.newInstance(devNum);
        Bundle bundle = new Bundle();
        bundle.putString("DevAddrDetail", hAddrId);
        bundle.putString("DevNodeDetail", nodeId);
        fragment.setArguments(bundle);
        this.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.ll_dev_node_bg,fragment, null)
                .addToBackStack(null)
                .commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //ButterKnife.unbind(this);
    }
    private void initMyToolBar() {
        int currentSkinType = SkinManager.getCurrentSkinType(this);
        if (SkinManager.THEME_DAY == currentSkinType) {
            initToolBar(mToolbar, "趋势图", R.drawable.gank_ic_back_white);
        } else {
            initToolBar(mToolbar, "趋势图", R.drawable.gank_ic_back_night);
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