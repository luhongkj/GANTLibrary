package com.luhong.locwithlibrary.ui.insurance;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioGroup;

import androidx.viewpager.widget.ViewPager;

import com.luhong.locwithlibrary.R;
import com.luhong.locwithlibrary.R2;
import com.luhong.locwithlibrary.apadter.TypePageAdapter;
import com.luhong.locwithlibrary.base.BaseActivity;
import com.luhong.locwithlibrary.base.BaseFragment;
import com.luhong.locwithlibrary.base.BaseFragmentTow;
import com.luhong.locwithlibrary.fragment.SafeguardFragment;
import com.luhong.locwithlibrary.view.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class SafeguardActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener
{
    @BindView(R2.id.viewPager_safeguard)
    NoScrollViewPager mViewPager;
    @BindView(R2.id.radioGroup_safeguard)
    RadioGroup radioGroup;

    private List<String> titleList = new ArrayList<>();
    private List<BaseFragmentTow> fragmentList = new ArrayList<>();
    private TypePageAdapter mTypeAdapter;

    @Override
    protected int initLayoutId()
    {
        return R.layout.activity_safeguard;
    }

    @Override
    protected void initView(Bundle savedInstanceState)
    {
        initTitleView(true, "安全保障");
        radioGroup.setOnCheckedChangeListener(this);

        fragmentList.add(SafeguardFragment.newInstance(SafeguardFragment.dataType_home));
        fragmentList.add(SafeguardFragment.newInstance(SafeguardFragment.dataType_min));
        mTypeAdapter = new TypePageAdapter(getSupportFragmentManager(), fragmentList, titleList);
        mViewPager.setAdapter(mTypeAdapter);
        mViewPager.setOffscreenPageLimit(fragmentList.size() - 1);
        mViewPager.setCurrentItem(0);
        radioGroup.check(R.id.rb_home_safeguard);
        mViewPager.isScroll(false);
    }

    @Override
    protected void initData()
    {

    }

    @Override
    protected void onEventListener()
    {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int i, float v, int i1)
            {

            }

            @Override
            public void onPageSelected(int i)
            {
                switch (i)
                {
                    case 0:
                        radioGroup.check(R.id.rb_home_safeguard);
                        break;
                    case 1:
                        radioGroup.check(R.id.rb_mine_safeguard);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i)
            {

            }
        });
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId)
    {
        showFragment(checkedId);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        fragmentList.get(1).onActivityResult(requestCode, resultCode, data);
    }

    public void showFragment(int checkedId)
    {
        if (checkedId == R.id.rb_home_safeguard) {
            notifyAdapterData(0);
            updateTitle("安全保障");
        } else if (checkedId == R.id.rb_mine_safeguard) {
            notifyAdapterData(1);
            updateTitle("我的保险");
        }
    }

    private void notifyAdapterData(int index)
    {
        mViewPager.setCurrentItem(index);
        mTypeAdapter.notifyDataSetChanged();
    }
}
