package com.luhong.locwithlibrary.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.luhong.locwithlibrary.R;
import com.luhong.locwithlibrary.R2;
import com.luhong.locwithlibrary.api.ApiClient;
import com.luhong.locwithlibrary.api.ApiServer;
import com.luhong.locwithlibrary.api.AppConstants;
import com.luhong.locwithlibrary.api.SCallBack;
import com.luhong.locwithlibrary.base.BaseFragment;
import com.luhong.locwithlibrary.dialog.BaseDialog;
import com.luhong.locwithlibrary.dialog.LoadingDialog;
import com.luhong.locwithlibrary.dialog.PromptDialog;
import com.luhong.locwithlibrary.entity.AlarmSensitivityParams;
import com.luhong.locwithlibrary.entity.SettingEntity;
import com.luhong.locwithlibrary.entity.SettingEntityPublish;
import com.luhong.locwithlibrary.entity.VehicleViewModel;
import com.luhong.locwithlibrary.net.response.BaseResponse;
import com.luhong.locwithlibrary.ui.equipment.LightModelActivity;
import com.luhong.locwithlibrary.utils.Ext;
import com.luhong.locwithlibrary.utils.FromNetUtil;
import com.luhong.locwithlibrary.utils.ToastUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;


/**
 * 鹿卫士的设置面板
 */
public class DeviceSettingFragment extends BaseFragment implements AppConstants {
    private Switch switch_notice;
    private Switch switch_sound;
    private Switch switch_fortification;
    private Switch switch_light;
    private RadioGroup radio_group;

    // 告警通知
    private View notify_layout;
    // 告警声音
    private View sound_layout;
    // 自动设防
    private View auto_fortify_layout;
    // 状态灯
    private View status_light_layout;
    // 告警灵敏度
    private View sensitivity_layout;
    // 警示灯工作模式
    private View light_model_layout;
    // 灯工作模式的文字
    private TextView light_work_model_name;
    //遥控器配对
    private View d2_model_layout;

    private final int TYPE_NOTICE = 1;//通知
    private final int TYPE_SOUND = 2;//声音
    private final int TYPE_AUTO_FORTIFICATION = 3;//设防
    private final int TYPE_LIGHT = 4;//灯
    // 标示这个改动是代码调用/网络调用
    private final boolean IS_FROM_NET = true;

    private ApiServer apiServer = ApiClient.getInstance().getApiServer();
    // 选中车辆的viewmodel
    private VehicleViewModel viewModel;
    private String currentVehicleId;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_device_setting;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        viewModel = Ext.create(this, VehicleViewModel.class);
        dialog = new LoadingDialog(getActivity());
    }

    @Override
    public void initView() {
        switch_notice = findViewById(R.id.switch_notice_setting);
        switch_sound = findViewById(R.id.switch_sound_setting);
        switch_fortification = findViewById(R.id.switch_fortification_setting);
        switch_light = findViewById(R.id.switch_light_setting);
        radio_group = findViewById(R.id.radio_group);
        d2_model_layout = findViewById(R.id.d2_model_layout);
        notify_layout = findViewById(R.id.notify_layout);
        sound_layout = findViewById(R.id.sound_layout);
        auto_fortify_layout = findViewById(R.id.auto_fortify_layout);
        status_light_layout = findViewById(R.id.status_light_layout);
        sensitivity_layout = findViewById(R.id.sensitivity_layout);
        light_model_layout = findViewById(R.id.light_model_layout);
        light_work_model_name = findViewById(R.id.light_work_model_name);
    }

    @Override
    public void initEvent() {
        // 监听数据改变的通知
        viewModel.device.observe(this, deviceEntity ->
        {
            if (deviceEntity != null) {
                currentVehicleId = deviceEntity.getId();
                String unitType = deviceEntity.getUnitType();
                String deviceType = deviceEntity.getUnitModelCn();
                // 隐藏所有选项
                // 告警通知
                notify_layout.setVisibility(View.GONE);
                // 告警声音
                sound_layout.setVisibility(View.GONE);
                // 自动设防
                auto_fortify_layout.setVisibility(View.GONE);
                // 状态灯
                status_light_layout.setVisibility(View.GONE);
                // 告警灵敏度
                sensitivity_layout.setVisibility(View.GONE);
                // 告警灵敏度-选项
                radio_group.setVisibility(View.GONE);
                // 警示灯工作模式
                light_model_layout.setVisibility(View.GONE);
                // 根据设备类型显示对应的设置项
                switch (unitType) {
                    case UNITTYPE_4: // 车载定位器
                    {
                        if (UNITMODELCN_S1.equals(deviceType)) {//D2--S1设备
                            d2_model_layout.setVisibility(View.VISIBLE);
                        } else {
                            d2_model_layout.setVisibility(View.GONE);
                        }
                        notify_layout.setVisibility(View.VISIBLE);
                        sound_layout.setVisibility(View.VISIBLE);
                        auto_fortify_layout.setVisibility(View.VISIBLE);
                        status_light_layout.setVisibility(View.VISIBLE);
                        sensitivity_layout.setVisibility(View.VISIBLE);
                        radio_group.setVisibility(View.VISIBLE);
                    }
                    break;
                    case UNITTYPE_5: // 前灯
                    case UNITTYPE_6: // 尾灯
                    {
                        d2_model_layout.setVisibility(View.GONE);
                        notify_layout.setVisibility(View.VISIBLE);
                        sound_layout.setVisibility(View.VISIBLE);
                        auto_fortify_layout.setVisibility(View.VISIBLE);
                        status_light_layout.setVisibility(View.VISIBLE);
                        sensitivity_layout.setVisibility(View.VISIBLE);
                        radio_group.setVisibility(View.VISIBLE);
                        light_model_layout.setVisibility(View.VISIBLE);
                    }
                    break;
                    case UNITTYPE_7: // 头盔
                    {
                        status_light_layout.setVisibility(View.VISIBLE);
                        d2_model_layout.setVisibility(View.GONE);
                        light_model_layout.setVisibility(View.VISIBLE);
                    }
                    break;
                }
                getSetting();
            }
        });

        // 告警通知
        switch_notice.setOnCheckedChangeListener((buttonView, isChecked) ->
        {
            // 如果这个回调来自网络，那么直接不响应
            boolean isFromNet = buttonView.getTag() != null && ((boolean) buttonView.getTag());
            buttonView.setTag(null);
            if (isFromNet) return;

            // 如果是打开，就直接跳过
            if (isChecked) {
                setSetting(TYPE_NOTICE, switch_notice);
                return;
            }
            // 关闭才提示
            PromptDialog.getInstance(requireActivity()).showDialog("是否关闭告警通知?", new BaseDialog.IEventListener() {
                @Override
                public void onConfirm() {
                    setSetting(TYPE_NOTICE, switch_notice);
                }

                @Override
                public void onCancel() {
                    buttonView.setTag(IS_FROM_NET);
                    buttonView.setChecked(!isChecked);
                }
            });
        });
        // 告警声音
        switch_sound.setOnCheckedChangeListener((buttonView, isChecked) ->
        {
            // 如果这个回调来自网络，那么直接不响应
            boolean isFromNet = buttonView.getTag() != null && ((boolean) buttonView.getTag());
            buttonView.setTag(null);
            if (isFromNet) return;

            // 如果是打开，就直接跳过
            if (isChecked) {
                setSetting(TYPE_SOUND, switch_sound);
                return;
            }
            // 关闭才提示
            PromptDialog.getInstance(requireActivity()).showDialog("是否关闭告警声音?", new BaseDialog.IEventListener() {
                @Override
                public void onConfirm() {
                    setSetting(TYPE_SOUND, switch_sound);
                }

                @Override
                public void onCancel() {
                    buttonView.setTag(IS_FROM_NET);
                    buttonView.setChecked(!isChecked);
                }
            });
        });
        // 自动设防
        switch_fortification.setOnCheckedChangeListener((buttonView, isChecked) ->
        {
            // 如果这个回调来自网络，那么直接不响应
            boolean isFromNet = buttonView.getTag() != null && ((boolean) buttonView.getTag());
            buttonView.setTag(null);
            if (isFromNet) return;

            // 如果是打开，就直接跳过
            if (isChecked) {
                setSetting(TYPE_AUTO_FORTIFICATION, switch_fortification);
                return;
            }
            // 关闭才提示
            PromptDialog.getInstance(requireActivity()).showDialog("是否关闭自动设防?", new BaseDialog.IEventListener() {
                @Override
                public void onConfirm() {
                    setSetting(TYPE_AUTO_FORTIFICATION, switch_fortification);
                }

                @Override
                public void onCancel() {
                    buttonView.setTag(IS_FROM_NET);
                    buttonView.setChecked(!isChecked);
                }
            });
        });
        // 状态灯
        switch_light.setOnCheckedChangeListener((buttonView, isChecked) ->
        {
            // 如果这个回调来自网络，那么直接不响应
            boolean isFromNet = buttonView.getTag() != null && ((boolean) buttonView.getTag());
            buttonView.setTag(null);
            if (isFromNet) return;
            // 如果是打开，就直接跳过
            if (isChecked) {
                setSetting(TYPE_LIGHT, switch_light);
                return;
            }
            // 关闭才提示
            PromptDialog.getInstance(requireActivity()).showDialog("是否关闭状态灯?", new BaseDialog.IEventListener() {
                @Override
                public void onConfirm() {
                    setSetting(TYPE_LIGHT, switch_light);
                }

                @Override
                public void onCancel() {
                    buttonView.setTag(IS_FROM_NET);
                    buttonView.setChecked(!isChecked);
                }
            });
        });
        // 告警灵敏度
        radio_group.setOnCheckedChangeListener((group, checkedId) ->
        {
            // 网络代码点击的就不响应呗
            boolean isFromNet = group.getTag() != null && ((boolean) group.getTag());
            group.setTag(null);
            if (isFromNet) return;

            AlarmSensitivityParams params = new AlarmSensitivityParams(currentVehicleId);
            if (checkedId == R.id.radio_height) {
                params.setParamValue(2);
                //params.put("paramValue", 2);//指令参数,必填。0:低,1:中,2:高
            } else if (checkedId == R.id.radio_middle) {
                params.setParamValue(1);//指令参数,必填。0:低,1:中,2:高
            } else if (checkedId == R.id.radio_low) {
                params.setParamValue(0);//指令参数,必填。0:低,1:中,2:高
            }
            Call<BaseResponse<String>> call = apiServer.homeCommand(params);
            call.enqueue(new SCallBack<BaseResponse<String>>() {
                @Override
                public void onSuccess(@NonNull BaseResponse<String> result) {
                    ToastUtil.show(result.getMsg());
                }
            });
        });


        d2_model_layout.setOnClickListener(v ->
        {
        /*    Intent intent = new Intent(requireActivity(), D2PairingActivity.class);
            intent.putExtra("device", viewModel.device.getValue());
            startActivity(intent);*/
        });
        // 点击警示灯工作模式
        light_model_layout.setOnClickListener(v ->
        {
         Intent intent = new Intent(requireActivity(), LightModelActivity.class);
            intent.putExtra("device", viewModel.device.getValue());
            startActivity(intent);
        });
    }


    @Override
    public void onResume() {
        if (viewModel != null && viewModel.device.getValue() != null) {
            getSetting();
        }
        super.onResume();
    }

    @Override
    public void loadData() {
        // getSetting();
    }

    private LoadingDialog dialog;

    // 获取设置
    private void getSetting() {
        Map<String, Object> bodyParams = new HashMap<>();
        bodyParams.put("vehicleId", currentVehicleId);
        dialog.show();
        Call<BaseResponse<List<SettingEntity>>> call = apiServer.getSettingCall(bodyParams);
        call.enqueue(new SCallBack<BaseResponse<List<SettingEntity>>>() {
            @Override
            public void onSuccess(@NonNull BaseResponse<List<SettingEntity>> result) {
                if (result.isSuccess()) {
                    List<SettingEntity> entitys = result.getData();
                    SettingEntityPublish publish = new SettingEntityPublish(entitys);

                    // 告警通知
                    publish.setOnNotify(value ->
                    {
                        FromNetUtil.setCheckedFromNet(switch_notice, value == 1);
                    });
                    // 告警声音
                    publish.setOnNotifyVoice(value ->
                    {
                        FromNetUtil.setCheckedFromNet(switch_sound, value == 1);
                    });
                    // 自动设防
                    publish.setOnAutoFortify(value ->
                    {
                        FromNetUtil.setCheckedFromNet(switch_fortification, value == 1);
                    });
                    // 状态灯
                    publish.setOnStateLight(value ->
                    {
                        FromNetUtil.setCheckedFromNet(switch_light, value == 1);
                    });
                    // 告警灵敏度
                    publish.setOnAlarmSensitivity(value ->
                    {
                        int id = 0;
                        switch (value) {
                            case 0:
                                id = R.id.radio_low;
                                break;
                            case 1:
                                id = R.id.radio_middle;
                                break;
                            case 2:
                                id = R.id.radio_height;
                                break;
                        }
                        FromNetUtil.setCheckedFromNet(radio_group, id);
                    });

                    // 灯的工作模式
                    publish.setOnWarningLightWorkModel(value ->
                    {
                        //0：自动；1：手动
                        light_work_model_name.setText(value == 0 ? "自动" : "手动");
                    });
                    // 开始部署
                    publish.start();
                } else {
                    ToastUtil.show(result.getMsg());
                }
            }

            @Override
            public void onFinally() {
                dialog.dismiss();
            }
        });
    }

    // 设置
    private void setSetting(int type, Switch sweeh) {
        boolean isChecked = sweeh.isChecked();

        Call<BaseResponse<Object>> call;
        // 自动设防，状态灯
        if (type == TYPE_AUTO_FORTIFICATION || type == TYPE_LIGHT) {
            Map<String, Object> params = new HashMap<>();
            params.put("vehicleId", currentVehicleId);
            params.put("flowId", "A" + System.currentTimeMillis());
            params.put("msgId", type == TYPE_AUTO_FORTIFICATION ? AppConstants.MSG_AUTO_FORTIFICATION : AppConstants.MSG_LIGHT);
            params.put("paramValue", isChecked ? 1 : 0);
            call = apiServer.sendCommandCall(params);
        } else {
            Map<String, Object> params = new HashMap<>();
            params.put("vehicleId", currentVehicleId);
            params.put("type", type);
            params.put("value", isChecked ? 1 : 0);
            call = apiServer.setSettingCall(params);
        }
        // 统一执行
        sweeh.setEnabled(false);
        call.enqueue(new SCallBack<BaseResponse<Object>>() {
            @Override
            public void onSuccess(@NonNull BaseResponse<Object> result) {
                // 执行失败切换按钮返回
                if (!result.isSuccess()) {
                    sweeh.setTag(IS_FROM_NET);
                    sweeh.setChecked(!isChecked);
                }
                ToastUtil.show(result.getMsg());
            }

            @Override
            public void onFail(Call<BaseResponse<Object>> call, Throwable t) {
                // 执行失败切换按钮返回
                sweeh.setTag(IS_FROM_NET);
                sweeh.setChecked(!isChecked);
                super.onFail(call, t);
            }

            @Override
            public void onFinally() {
                sweeh.setEnabled(true);
            }
        });
    }
}
