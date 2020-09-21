package com.iot.user.ui.adapter.login;

import android.content.Context;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class LoginFragmentPagerAdapter extends FragmentPagerAdapter {
    private Context context;
    private List fragmentList;
    private List<String> list_Title;
    public LoginFragmentPagerAdapter(FragmentManager fm, Context context, List fragmentList, List<String> list_Title){
        super(fm);
        this.context = context;
        this.fragmentList = fragmentList;
        this.list_Title = list_Title;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment=(Fragment) fragmentList.get(position);
        return fragment;
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return list_Title.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {/**防止viewPager重构**/

    }
}
