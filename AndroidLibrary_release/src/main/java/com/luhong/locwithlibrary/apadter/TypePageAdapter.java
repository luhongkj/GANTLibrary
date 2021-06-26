package com.luhong.locwithlibrary.apadter;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import com.luhong.locwithlibrary.base.BaseFragment;
import com.luhong.locwithlibrary.base.BaseFragmentTow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ITMG on 2018-08-06.
 */
public class TypePageAdapter extends FragmentPagerAdapter {
    private List<? extends Fragment> mFragmentList;
    private List<String> mTitleList = new ArrayList<>();
    private FragmentManager mFragmentManager;

    public TypePageAdapter(FragmentManager fm) {
        super(fm);
        this.mFragmentManager = fm;
    }

    public TypePageAdapter(FragmentManager fm, List<? extends Fragment> fragmentList, List<String> titleList) {
        super(fm);
        this.mFragmentManager = fm;
        this.mFragmentList = fragmentList;
        this.mTitleList = titleList;
    }

    public TypePageAdapter(FragmentManager fm, List<? extends Fragment> fragmentList) {
        super(fm);
        this.mFragmentManager = fm;
        this.mFragmentList = fragmentList;
    }

    public void setData(List<? extends BaseFragment> fragmentList, List<String> titleList) {
        this.mFragmentList = fragmentList;
        this.mTitleList = titleList;
        notifyDataSetChanged();
    }

    public void setData(List<? extends BaseFragmentTow> fragmentList) {
        this.mFragmentList = fragmentList;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList == null ? 0 : mFragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return (mTitleList == null || mTitleList.size() == 0) ? "" : mTitleList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mFragmentList.get(position).hashCode();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        if (mFragmentList.contains(object)) {
            return mFragmentList.indexOf(object);
        } else {
            return PagerAdapter.POSITION_NONE;
        }
    }
}
