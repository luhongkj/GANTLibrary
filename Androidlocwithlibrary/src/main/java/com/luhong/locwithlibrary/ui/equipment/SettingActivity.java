package com.luhong.locwithlibrary.ui.equipment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.reflect.TypeToken;
import com.luhong.locwithlibrary.R;
import com.luhong.locwithlibrary.R2;
import com.luhong.locwithlibrary.api.AppConstants;
import com.luhong.locwithlibrary.api.AppVariable;
import com.luhong.locwithlibrary.app.BaseConstants;
import com.luhong.locwithlibrary.app.BaseVariable;
import com.luhong.locwithlibrary.base.BaseMvpActivity;
import com.luhong.locwithlibrary.contract.SettingContract;
import com.luhong.locwithlibrary.dialog.SelectListDialog;
import com.luhong.locwithlibrary.entity.DeviceEntity;
import com.luhong.locwithlibrary.entity.SelectListEntity;
import com.luhong.locwithlibrary.entity.UserEntity;
import com.luhong.locwithlibrary.entity.VehicleViewModel;
import com.luhong.locwithlibrary.event.SwitchDeviceEvent;
import com.luhong.locwithlibrary.fragment.DeviceSettingFragment;
import com.luhong.locwithlibrary.listener.SingleClickListener;
import com.luhong.locwithlibrary.presenter.home.SettingPresenter;
import com.luhong.locwithlibrary.ui.CaptureActivity;
import com.luhong.locwithlibrary.ui.LVehiclechoiceActivity;
import com.luhong.locwithlibrary.utils.EventBusUtils;
import com.luhong.locwithlibrary.utils.Ext;
import com.luhong.locwithlibrary.utils.SPUtils;
import com.zyq.easypermission.EasyPermissionResult;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class SettingActivity extends BaseMvpActivity<SettingPresenter> implements SettingContract.View {
    @BindView(R2.id.iv_changeVehicle_setting)
    ImageView iv_changeVehicle;
    @BindView(R2.id.tv_currentVehicle_setting)
    TextView tv_currentVehicle;

    private List<SelectListEntity> selectList = new ArrayList<>();
    private UserEntity userEntity;

    private DeviceSettingFragment deviceSettingFragment = new DeviceSettingFragment();

    private VehicleViewModel vehicleViewModel;

    @Override
    protected int initLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        initTitleView(true, "设置", "", new View.OnClickListener() {
            @RequiresApi(api = 30)
            @Override
            public void onClick(View v) {
                requestStorageAndCameraPermissions(new EasyPermissionResult() {
                    @Override
                    public void onPermissionsAccess(int requestCode) {
                        super.onPermissionsAccess(requestCode);
                        startIntentActivityForResult(LVehiclechoiceActivity.class, CaptureActivity.resultDecode);
                    }

                    @Override
                    public void onPermissionsDismiss(int requestCode, @NonNull List<String> permissions) {
                        super.onPermissionsDismiss(requestCode, permissions);
                        showToast("没有权限");
                    }
                });
            }
        });
        userEntity = SPUtils.getObject(this,
                BaseConstants.LOGIN_KEY,
                new TypeToken<UserEntity>() {
                }.getType());
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void fetchData() {
        mPresenter.getDeviceList();
    }

    @Override
    protected void onEventListener() {
        tv_currentVehicle.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (selectList == null || selectList.size() == 0) return;
                new SelectListDialog(mActivity, iv_changeVehicle, selectList, new SelectListDialog.ISelectListener() {
                    @Override
                    public void onDialogClick(SelectListEntity data) {
                        if (data == null || TextUtils.isEmpty(data.getDeviceSn()) || TextUtils.isEmpty(data.getVehicleId())) {
                            showToast("无效设备");
                            return;
                        }
                        if (!TextUtils.isEmpty(AppVariable.currentDeviceId) && data.getDeviceSn().equals(AppVariable.currentDeviceId))
                            return;

                        AppVariable.currentDeviceId = data.getDeviceSn();
                        SPUtils.put(SettingActivity.this, "currentDeviceId", AppVariable.currentDeviceId);
                        AppVariable.currentVehicleId = data.getVehicleId();
                        SPUtils.put(mActivity, AppConstants.LAST_DEVICE_ID_KEY + BaseVariable.phone, data.getDeviceSn());
                        EventBusUtils.post(new SwitchDeviceEvent());
                        fetchData();//刷新
                    }
                });
            }
        });
    }

    @Override
    public void onDeviceListSuccess(List<DeviceEntity> dataList) {
        if (dataList == null || dataList.size() == 0) {
            tv_currentVehicle.setText("当前车辆：");
            return;
        }
        List<UserEntity.VehicleList> vehicleList = new ArrayList<>();
        selectList.clear();
        boolean isMatch = false;
        for (DeviceEntity deviceEntity : dataList) {
            if (deviceEntity == null || TextUtils.isEmpty(deviceEntity.getSn()) || TextUtils.isEmpty(deviceEntity.getId()))
                continue;
            if (TextUtils.isEmpty(AppVariable.currentDeviceId) || (!TextUtils.isEmpty(AppVariable.currentDeviceId) && AppVariable.currentDeviceId.equals(deviceEntity.getSn()))) {
                choseVehicle(deviceEntity);
                isMatch = true;
            }
            selectList.add(new SelectListEntity(deviceEntity.getNickName(), deviceEntity.getSn(), deviceEntity.getId(), deviceEntity.getBrandName()));
            vehicleList.add(new UserEntity.VehicleList(deviceEntity.getId(), deviceEntity.getVin(), deviceEntity.getSn(), deviceEntity.getNickName(), deviceEntity.getUserId(), deviceEntity.getBrandName()));
        }
        if (!isMatch) {
            DeviceEntity deviceEntity = dataList.get(0);
            choseVehicle(deviceEntity);
        }
        if (userEntity != null) {
            userEntity.setVehicles(vehicleList);
            SPUtils.putObject(this, BaseConstants.LOGIN_KEY, userEntity);
        }
    }

    /**
     * 选择了车辆走的函数
     *
     * @param deviceEntity
     */
    private void choseVehicle(DeviceEntity deviceEntity) {
        AppVariable.currentDeviceId = deviceEntity.getSn();
        SPUtils.put(this, "currentDeviceId", AppVariable.currentDeviceId);
        AppVariable.currentVehicleId = deviceEntity.getId();
        tv_currentVehicle.setText("当前车辆：" + deviceEntity.getNickName());

        String vehicleType = deviceEntity.getVehicleType();
        if (vehicleType == null) return;

        if (deviceSettingFragment.isAdded()) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.show(deviceSettingFragment);
            transaction.commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
        } else {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.setting_layout, deviceSettingFragment);
            transaction.commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
        }
        if (vehicleViewModel == null) {
            vehicleViewModel = Ext.create(deviceSettingFragment, VehicleViewModel.class);
        }
        vehicleViewModel.device.postValue(deviceEntity);
    }

    @Override
    public void onFailure(int errType, String errMsg) {

    }
}
