package com.iot.user.ui.fragment.message;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.iot.user.R;
import com.iot.user.base.BaseFragment;
import com.iot.user.base.BaseMvpFragment;
import com.iot.user.constant.Constants;
import com.iot.user.databinding.FragmentUnitMessageBinding;
import com.iot.user.http.model.BaseResponse;
import com.iot.user.ui.activity.main.UnitMainActivity;
import com.iot.user.ui.adapter.login.LoginFragmentPagerAdapter;
import com.iot.user.ui.contract.message.UnitMessageContract;
import com.iot.user.ui.presenter.message.UnitMessagePresenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UnitMessageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UnitMessageFragment extends BaseMvpFragment<UnitMessagePresenter,FragmentUnitMessageBinding> implements UnitMessageContract.View{

    public UnitMessageFragment() {
        // Required empty public constructor
    }
    public static UnitMessageFragment newInstance() {
        return new UnitMessageFragment();
    }
    ViewPager viewPager;
    public TabLayout viewpagertab;
    private View tabLayoutView;

    private List fragments=new ArrayList<>();
    private List<String> fragmentTitles=new ArrayList<>();
    private int selectTablayout=0;
    public final String[] TITLES = {
            Constants.UnitMessageHistoryFragment,
            Constants.UnitMessageFamilyShareFragment,
            Constants.UnitMessageDevShareFragment
    };

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_unit_message;
    }

    @Override
    protected void initView(View view) {
        mPresenter=new UnitMessagePresenter();
        mPresenter.attachView(this);
     viewPager=dataBinding.messageViewPager;
     viewpagertab=dataBinding.messageTablayout;
     tabLayoutView=LayoutInflater.from(context).inflate(R.layout.view_unit_message_tab_count, null);
     initViewPager();
    }

    private void initViewPager() {
        for (int i = 0; i < TITLES.length; i++) {
            if (i == 0) {
                UnitMessageHistoryFragment fragment =new UnitMessageHistoryFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("status", i);
                fragment.setArguments(bundle);//数据传递到fragment中
                fragmentTitles.add(TITLES[i]);
                fragments.add(fragment);
            } else if (i == 1) {
                UnitMessageFamilyFragment fragment2 = UnitMessageFamilyFragment.newInstance();
                Bundle bundle = new Bundle();
                bundle.putInt("status", i);
                fragment2.setArguments(bundle);//数据传递到fragment中
                fragment2.setRefreshSuperFragment(new UnitMessageFamilyFragment.RefreshSuperFragment() {/**家庭共享刷新页面回调**/
                @Override
                public void refresh() {
                    mPresenter.postUnitFamilyNewCount();
                    if(UnitMainActivity.gtMsgHandler !=null){
                        Message msg = new Message();
                        msg.what=2;
                        UnitMainActivity.gtMsgHandler.sendMessage(msg);
                    }
                }
                });
                fragmentTitles.add(TITLES[i]);
                fragments.add(fragment2);
            } else {
                UnitMessageFamilyFragment fragment3 = UnitMessageFamilyFragment.newInstance();
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
        viewpagertab.getTabAt(1).setCustomView(tabLayoutView);
        final TextView tv_count_title=tabLayoutView.findViewById(R.id.tv_family_share_title);
        viewpagertab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selectTablayout = tab.getPosition();
                if (isAdded()) {/**判断当前的Fragment是否已经添加到Activity***/
                    if (selectTablayout == 1) {
                        tv_count_title.setTextColor(getResources().getColor(R.color.google_blue));
                    } else {
                        tv_count_title.setTextColor(getResources().getColor(R.color.black));
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

    }


    @Override
    public void onSuccess(BaseResponse bean, String type) {
        final TextView tv_count=tabLayoutView.findViewById(R.id.tv_tab_count);
        if (type.equals("family_new_count")) {
            Map body = (Map) bean.getBody();
            if (body==null){
                return;
            }
            int familyCount = Double.valueOf((Double) body.get("familyNewsCount")).intValue();
            if (isAdded()) {/**判断当前的Fragment是否已经添加到Activity***/
                if (familyCount > 0) {
                    tv_count.setVisibility(View.VISIBLE);
                    tv_count.setText("" + familyCount);
                } else {
                    tv_count.setVisibility(View.GONE);
                }
            }
        }
    }
}