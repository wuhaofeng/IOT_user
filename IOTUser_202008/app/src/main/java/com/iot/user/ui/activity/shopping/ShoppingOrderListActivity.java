package com.iot.user.ui.activity.shopping;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;
import com.iot.user.R;
import com.iot.user.base.BaseActivity;
import com.iot.user.databinding.ActivityShoppingDeviceListBinding;
import com.iot.user.databinding.ActivityShoppingOrderListBinding;
import com.iot.user.skin.SkinManager;
import com.iot.user.ui.adapter.login.LoginFragmentPagerAdapter;
import com.iot.user.ui.fragment.shopping.ShoppingOrderListFragment;
import com.iot.user.utils.TabLayoutUtils;

import java.util.ArrayList;
import java.util.List;

public class ShoppingOrderListActivity extends BaseActivity<ActivityShoppingOrderListBinding> {
    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_shopping_order_list;
    }
    Toolbar mToolbar;
    private List<ShoppingOrderListFragment> fragments=new ArrayList<>();
    private List<String> fragmentTitles=new ArrayList<>();
    private int selectTablayout=0;
    @Override
    public void initView() {
        mToolbar=(Toolbar) dataBinding.toolbar;
        TabLayoutUtils.reflex(dataBinding.tablayout);/**设置tablayout底部的横县的长度***/
        initMyToolBar();
        initViewPager();
    }
    private boolean isFirstResume=false;
    @Override
    protected void onResume() {/**只要页面显示的时候就会调用**/
        super.onResume();
        if (isFirstResume) {
            for (int i = 0; i < fragments.size(); i++) {
                ShoppingOrderListFragment fragment = fragments.get(i);
                fragment.updateOrderList(i);
            }
        }else {
            isFirstResume=true;
        }
    }

    private void initViewPager(){
        String[] titles={"全部订单","待支付","已完成","已取消"};
        for (int i=0;i<4;i++){
            ShoppingOrderListFragment fragment=ShoppingOrderListFragment.newInstance();
            Bundle bundle = new Bundle();
            bundle.putInt("status",i);
            fragment.setArguments(bundle);//数据传递到fragment中
            fragmentTitles.add(titles[i]);
            fragments.add(fragment);
        }
        //每项只进入一次
        dataBinding.tabViewpager.setAdapter(new LoginFragmentPagerAdapter(getSupportFragmentManager(),this,fragments,fragmentTitles));
        dataBinding.tablayout.setupWithViewPager(dataBinding.tabViewpager);
        dataBinding.tablayout.getTabAt(0).select();//设置第一个为选中
        dataBinding.tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selectTablayout=tab.getPosition();
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    /***toolbar触发事件**/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initMyToolBar() {
        int currentSkinType = SkinManager.getCurrentSkinType(this);
        if (SkinManager.THEME_DAY == currentSkinType) {
            initToolBar(mToolbar, "订单", R.drawable.gank_ic_back_white);
        } else {
            initToolBar(mToolbar, "订单", R.drawable.gank_ic_back_night);
        }
    }

}
