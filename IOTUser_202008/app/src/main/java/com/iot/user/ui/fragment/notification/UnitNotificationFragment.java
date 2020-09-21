package com.iot.user.ui.fragment.notification;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.iot.user.R;
import com.iot.user.base.BaseFragment;
import com.iot.user.constant.Constants;
import com.iot.user.databinding.FragmentUnitNotificationBinding;
import com.iot.user.ui.adapter.login.LoginFragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UnitNotificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UnitNotificationFragment extends BaseFragment<FragmentUnitNotificationBinding> {
    public UnitNotificationFragment() {
        // Required empty public constructor
    }
    public static UnitNotificationFragment newInstance() {
        return new UnitNotificationFragment();
    }
    ViewPager viewPager;
    TabLayout viewpagertab;
    private List fragments=new ArrayList<>();
    private List<String> fragmentTitles=new ArrayList<>();
    private int selectTablayout=0;
    public final String[] TITLES = {
            Constants.UnReadFragment,
            Constants.ReadFragment,
            Constants.AllFragment
    };

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_unit_notification;
    }

    @Override
    protected void initView(View view) {
     viewPager=dataBinding.notificationViewPager;
     viewpagertab=dataBinding.notificationTablayout;
        initViewPager();
    }
    private void initViewPager() {
        for (int i = 0; i < TITLES.length; i++) {
            if (i == 0) {
                UnitNotificationListFragment fragment = UnitNotificationListFragment.newInstance();
                Bundle bundle = new Bundle();
                bundle.putInt("status", i);
                fragment.setArguments(bundle);//数据传递到fragment中
                fragmentTitles.add(TITLES[i]);
                fragments.add(fragment);
            } else if (i == 1) {
                UnitNotificationListFragment fragment2 = UnitNotificationListFragment.newInstance();
                Bundle bundle = new Bundle();
                bundle.putInt("status", i);
                fragment2.setArguments(bundle);//数据传递到fragment中
                fragmentTitles.add(TITLES[i]);
                fragments.add(fragment2);
            } else {
                UnitNotificationListFragment fragment3 = UnitNotificationListFragment.newInstance();
                Bundle bundle = new Bundle();
                bundle.putInt("status", i);
                fragment3.setArguments(bundle);//数据传递到fragment中
                fragmentTitles.add(TITLES[i]);
                fragments.add(fragment3);
            }
        }
        //每项只进入一次
        viewPager.setAdapter(new LoginFragmentPagerAdapter(getChildFragmentManager(), getContext(), fragments, fragmentTitles));
        viewpagertab.setupWithViewPager(viewPager);
        viewpagertab.getTabAt(0).select();//设置第一个为选中
        viewpagertab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selectTablayout = tab.getPosition();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

    }

}