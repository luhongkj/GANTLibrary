package com.luhong.locwithlibrary.ui.equipment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.reflect.TypeToken;
import com.luhong.locwithlibrary.R;
import com.luhong.locwithlibrary.R2;
import com.luhong.locwithlibrary.apadter.TypePageAdapter;
import com.luhong.locwithlibrary.api.AppConstants;
import com.luhong.locwithlibrary.api.AppVariable;
import com.luhong.locwithlibrary.app.BaseConstants;
import com.luhong.locwithlibrary.base.BaseMvpActivity;
import com.luhong.locwithlibrary.contract.home.DeviceManageContract;
import com.luhong.locwithlibrary.entity.DeviceEntity;
import com.luhong.locwithlibrary.entity.PdfEntity;
import com.luhong.locwithlibrary.entity.SafeguardEntity;
import com.luhong.locwithlibrary.entity.UserEntity;
import com.luhong.locwithlibrary.event.SwitchDeviceEvent;
import com.luhong.locwithlibrary.fragment.DeviceManageFragment;
import com.luhong.locwithlibrary.listener.SingleClickListener;
import com.luhong.locwithlibrary.net.response.BasePageEntity;
import com.luhong.locwithlibrary.presenter.home.DeviceManagePresenter;
import com.luhong.locwithlibrary.ui.CaptureActivity;
import com.luhong.locwithlibrary.ui.LVehiclechoiceActivity;
import com.luhong.locwithlibrary.utils.EventBusUtils;
import com.luhong.locwithlibrary.utils.Logger;
import com.luhong.locwithlibrary.utils.SPUtils;
import com.luhong.locwithlibrary.view.NoScrollViewPager;
import com.zyq.easypermission.EasyPermissionResult;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import butterknife.BindView;

import static com.luhong.locwithlibrary.api.AppVariable.BACK_ACTIVITE;


public class DeviceManageActivity extends BaseMvpActivity<DeviceManagePresenter> implements DeviceManageContract.View, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R2.id.swipeRefreshLayout_deviceManage)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R2.id.tv_errorLoad_deviceManage)
    TextView tv_errorLoad;
    @BindView(R2.id.viewPager_deviceManage)
    NoScrollViewPager mViewPager;
    @BindView(R2.id.radioGroup_deviceManage)
    RadioGroup radioGroup;

    public List<DeviceManageFragment> mFragmentList = new CopyOnWriteArrayList<>();
    private TypePageAdapter mTypeAdapter;
    private UserEntity userEntity;

    public DeviceManageActivity() {
    }

    @Override
    protected int initLayoutId() {
        return R.layout.activity_device_manage;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        initTitleView(true, "设备管理", "添加设备", new SingleClickListener() {
            @RequiresApi(api = 30)
            @Override
            public void onSingleClick(View v) {
                requestStorageAndCameraPermissions(new EasyPermissionResult() {
                    @Override
                    public void onPermissionsAccess(int requestCode) {
                        super.onPermissionsAccess(requestCode);
                        startIntentActivityForResult(LVehiclechoiceActivity.class,100);
                    }

                    @Override
                    public void onPermissionsDismiss(int requestCode, @NonNull List<String> permissions) {
                        super.onPermissionsDismiss(requestCode, permissions);
                        showToast("没有权限");
                    }
                });
            }
        });
        swipeRefreshLayout.setColorSchemeResources(R.color.theme_color);
        swipeRefreshLayout.setOnRefreshListener(this);
        mTypeAdapter = new TypePageAdapter(getSupportFragmentManager(), mFragmentList);
        mViewPager.setAdapter(mTypeAdapter);
        mViewPager.setPageMargin(-50);
    }

    @Override
    protected void initData() {
        userEntity = SPUtils.getObject(this, BaseConstants.LOGIN_KEY, new TypeToken<UserEntity>() {
        }.getType());

        List<DeviceEntity> dataList = SPUtils.getObject(this,
                AppConstants.DEVICE_MANAGE_KEY,
                new TypeToken<List<DeviceEntity>>() {
                }.getType());
        if (dataList != null && dataList.size() != 0) onDeviceListSuccess(dataList);
    }

    @Override
    protected void fetchData() {
        mPresenter.getDeviceList(this);
    }

    @Override
    protected void onEventListener() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }

            @Override
            public void onPageSelected(int i) {
                radioGroup.check(i);
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (mViewPager != null) mViewPager.setCurrentItem(i);
            }
        });
    }

    //添加解绑的是否是当前车辆
    @Override
    public void onRefresh() {
        fetchData();
    }

    @Override
    public void onDeviceListSuccess(List<DeviceEntity> dataList) {
        mFragmentList.clear();
        if (dataList == null || dataList.size() == 0) {
            AppVariable.currentDeviceId = "";
            AppVariable.currentVehicleId = "";
            EventBusUtils.post(new SwitchDeviceEvent());
            notifyAdapterData();
            return;
        }

        try {
            List<UserEntity.VehicleList> vehicleList = new ArrayList<>();
            if (TextUtils.isEmpty(AppVariable.currentDeviceId) || TextUtils.isEmpty(AppVariable.currentVehicleId)) {
                DeviceEntity deviceEntity = dataList.get(0);
                if (deviceEntity != null) {
                    SPUtils.put(this, "currentDeviceId", AppVariable.currentDeviceId);
                    AppVariable.currentDeviceId = deviceEntity.getSn();
                    AppVariable.currentVehicleId = deviceEntity.getId();
                }
                EventBusUtils.post(new SwitchDeviceEvent());
            }
            for (int i = 0; i < dataList.size(); i++) {
                DeviceEntity deviceEntity = dataList.get(i);
                mFragmentList.add(DeviceManageFragment.newInstance(i, deviceEntity));
                vehicleList.add(new UserEntity.VehicleList(deviceEntity.getId(), deviceEntity.getVin(), deviceEntity.getSn(), deviceEntity.getNickName(), deviceEntity.getUserId(), deviceEntity.getBrandName()));
            }
            notifyAdapterData();

            if (userEntity != null) {
                userEntity.setVehicles(vehicleList);
                SPUtils.putObject(this, BaseConstants.LOGIN_KEY, userEntity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDeviceUpdateSuccess(DeviceEntity deviceEntity) { }

    @Override
    public void onDeviceDeleteSuccess(boolean isCurrentDevice, DeviceEntity deviceEntity) { }

    @Override
    public void onProductDescriptionSuccess(PdfEntity resultEntity) { }

    @Override
    public void onSafeguardMineSuccess(BasePageEntity<SafeguardEntity> pageList) { }

    @Override
    public void onVehicleConfirmPaySuccess(Object resultEntity) {}

    @Override
    public void onFailure(int errType, String errMsg) {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       if (requestCode == AppConstants.REQUEST_CODE_ADD) {
            fetchData();
        }
        if (resultCode == BACK_ACTIVITE) {
            setResult(BACK_ACTIVITE);
            finish();
        }
    }

    /**
     * 移除View
     *
     * @param index 下标
     */
    public void removeView(int index) {
        mFragmentList.remove(index);
        mTypeAdapter.setData(mFragmentList);//重新设置适配器中的List
        notifyAdapterData();
    }

    /**
     *
     */
    public void notifyAdapterData() {
        swipeRefreshLayout.setRefreshing(false);
        mTypeAdapter.notifyDataSetChanged();
        mViewPager.setCurrentItem(0);
        if (mFragmentList.size() != 0) {
            mViewPager.setOffscreenPageLimit(mFragmentList.size() - 1);
            swipeRefreshLayout.setEnabled(false);
            tv_errorLoad.setVisibility(View.GONE);
        } else {
            swipeRefreshLayout.setEnabled(true);
            tv_errorLoad.setVisibility(View.VISIBLE);
        }

        addRadioButton();
    }

    private void addRadioButton() {
        radioGroup.removeAllViews();
        if (mFragmentList == null || mFragmentList.size() == 0) return;
        int dimension = (int) (getResources().getDisplayMetrics().density * 10 + 0.5f);
        for (int i = 0; i < mFragmentList.size(); i++) {
            RadioButton radioButton = new RadioButton(mActivity);
            radioButton.setPadding(0, 0, 0, 0);
            radioButton.setButtonDrawable(null);
            radioButton.setBackgroundResource(R.drawable.selector_guide_index);
            radioButton.setId(i); //必须有ID，否则默认选中的选项会一直是选中状态
            RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(dimension, dimension);//layoutParams 设置margin值
            if (i != 0) {
                layoutParams.setMargins(dimension, 0, 0, 0);
            } else {
                layoutParams.setMargins(0, 0, 0, 0);
                radioButton.setChecked(true);//默认选中
            }
            radioGroup.addView(radioButton, layoutParams);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchData();
    }
}
