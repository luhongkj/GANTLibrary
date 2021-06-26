package com.luhong.locwithlibrary.ui.equipment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.luhong.locwithlibrary.R;
import com.luhong.locwithlibrary.api.ApiClient;
import com.luhong.locwithlibrary.api.ApiServer;
import com.luhong.locwithlibrary.api.SCallBack;
import com.luhong.locwithlibrary.base.BaseActivity;
import com.luhong.locwithlibrary.base.BaseActivityTow;
import com.luhong.locwithlibrary.base.OnSeekBarChangeListener;
import com.luhong.locwithlibrary.entity.BrakeSensitivityParams;
import com.luhong.locwithlibrary.entity.DeviceEntity;
import com.luhong.locwithlibrary.entity.LightBrightnesParams;
import com.luhong.locwithlibrary.entity.LightModelParams;
import com.luhong.locwithlibrary.entity.LightShakeModelParams;
import com.luhong.locwithlibrary.entity.LightWordModelParams;
import com.luhong.locwithlibrary.entity.SettingEntity;
import com.luhong.locwithlibrary.entity.SettingEntityPublish;
import com.luhong.locwithlibrary.imagepicker.view.APPTittle;
import com.luhong.locwithlibrary.net.response.BaseResponse;
import com.luhong.locwithlibrary.utils.FromNetUtil;
import com.luhong.locwithlibrary.utils.ToastUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;


/**
 * 警示灯工作模式调整
 */
public class LightModelActivity extends BaseActivityTow {
    private DeviceEntity device;
    // 标题
    private APPTittle appTittle;
    // 工作模式的提示
    private TextView work_model_tip;
    // 工作模式
    private RadioGroup work_model;
    // 亮度调节
    private SeekBar intensity_seekbar;
    // 刹车灵敏度
    private RadioGroup brake_sensitivity;
    // 闪光模式
    private RadioGroup light_model;
    // 闪光模式布局
    private View light_model_layout;
    // 前灯的闪光模式
    private RadioGroup front_light_model;
    // 尾灯的闪光模式
    private RadioGroup tail_light_model;
    // 闪光间隔布局
    private View light_delay_layout;
    // 闪光间隔
    private RadioGroup light_delay;
    // 网络请求
    private ApiServer apiServer = ApiClient.getInstance().getApiServer();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_light_model);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        Intent intent = getIntent();
        device = (DeviceEntity) intent.getSerializableExtra("device");
        if (device == null) {
            ToastUtil.show("传入车辆参数出错");
            finish();
        }
    }

    @Override
    public void initView() {
        appTittle = findViewById(R.id.app_title);
        work_model = findViewById(R.id.work_model);
        intensity_seekbar = findViewById(R.id.intensity_seekbar);
        brake_sensitivity = findViewById(R.id.brake_sensitivity);
        light_model = findViewById(R.id.light_model);
        light_model_layout = findViewById(R.id.light_model_layout);
        front_light_model = findViewById(R.id.front_light_model);
        tail_light_model = findViewById(R.id.tail_light_model);
        light_delay_layout = findViewById(R.id.light_delay_layout);
        light_delay = findViewById(R.id.light_delay);
        work_model_tip = findViewById(R.id.work_model_tip);
        appTittle.setTitleText("警示灯设置");
        switch (device.getUnitType()) {
            case "5": // 前灯
            {
                front_light_model.setVisibility(View.VISIBLE);
                tail_light_model.setVisibility(View.GONE);
            }
            break;
            case "7": // 头盔
            case "6": // 尾灯
            {
                front_light_model.setVisibility(View.GONE);
                tail_light_model.setVisibility(View.VISIBLE);
            }
            break;
            default: {
                ToastUtil.show("传入车辆参数出错");
            }
            break;
        }
    }

    @Override
    public void initEvent() {
        appTittle.onLeftImgClick(v -> finish());

        // 切换工作模式
        work_model.setOnCheckedChangeListener((group, checkedId) ->
        {
            // View view = light_model.findViewById(R.id.light_model_shake);
            // RadioButton alwaysView = light_model.findViewById(R.id.light_model_always);
            LightWordModelParams params = new LightWordModelParams(device.getId());
            if (checkedId == R.id.work_model_auto) {          // 自动
                work_model_tip.setText("自动根据环境光检测调节警示灯");
                // view.setVisibility(View.GONE);
                // alwaysView.setChecked(true);
                params.setParamValue(0);
            } else if (checkedId == R.id.work_model_motion) {        // 手动
                work_model_tip.setText("自定义调节警示灯工作模式参数");
                // view.setVisibility(View.VISIBLE);
                params.setParamValue(1);
            }
            if (!FromNetUtil.isFromNet(group)) {
                Call<BaseResponse<String>> call = apiServer.lightWorkModel(params);
                call.enqueue(new SCallBack<BaseResponse<String>>() {
                    @Override
                    public void onSuccess(@NonNull BaseResponse<String> result) {
                        ToastUtil.show(result.getMsg());
                    }
                });
            }
        });
        // 亮度调节
        intensity_seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (FromNetUtil.isFromNet(seekBar))
                    return;
                {
                    int progress = seekBar.getProgress();
                    LightBrightnesParams params = new LightBrightnesParams(device.getId());
                    params.setParamValue(progress);
                    Call<BaseResponse<String>> call = apiServer.lightBrightness(params);
                    call.enqueue(new SCallBack<BaseResponse<String>>() {
                        @Override
                        public void onSuccess(@NonNull BaseResponse<String> result) {
                            ToastUtil.show(result.getMsg());
                        }
                    });
                }
            }
        });

        // 刹车灵敏度
        brake_sensitivity.setOnCheckedChangeListener((group, checkedId) ->
        {
            if (FromNetUtil.isFromNet(group))
                return;
            {
                BrakeSensitivityParams param = new BrakeSensitivityParams(device.getId());
                if (checkedId == R.id.brake_sensitivity_high) {   // 高
                    param.setParamValue(2);
                } else if (checkedId == R.id.brake_sensitivity_middle) { // 中
                    param.setParamValue(1);
                } else if (checkedId == R.id.brake_sensitivity_low) {    // 低
                    param.setParamValue(0);
                }
                Call<BaseResponse<String>> call = apiServer.brakeSensitivity(param);
                call.enqueue(new SCallBack<BaseResponse<String>>() {
                    @Override
                    public void onSuccess(@NonNull BaseResponse<String> result) {
                        ToastUtil.show(result.getMsg());
                    }
                });
            }
        });
        // 闪光模式
        light_model.setOnCheckedChangeListener((group, checkedId) ->
        {
            // 0：灯常亮,1灯常灭,2闪烁
            LightModelParams params = new LightModelParams(device.getId());
            if (checkedId == R.id.light_model_always) {       // 常亮
                params.setParamValue(0);
                // 闪光模式布局
                light_model_layout.setVisibility(View.GONE);
                // 闪光间隔布局
                light_delay_layout.setVisibility(View.GONE);
            } else if (checkedId == R.id.light_model_off) {          // 常灭
                params.setParamValue(1);
                // 闪光模式布局
                light_model_layout.setVisibility(View.GONE);
                // 闪光间隔布局
                light_delay_layout.setVisibility(View.GONE);
            } else if (checkedId == R.id.light_model_shake) { // 闪烁
                params.setParamValue(2);
                // 选择闪烁，应该按照当前设备类型，显示闪烁类型，头盔的设置和尾灯一致

                // 闪光模式布局
                light_model_layout.setVisibility(View.VISIBLE);
                switch (device.getUnitType()) {
                    case "5": // 前灯
                    {
                        front_light_model.setVisibility(View.VISIBLE);
                        tail_light_model.setVisibility(View.GONE);
                    }
                    break;
                    case "7": // 头盔
                    case "6": // 尾灯
                    {
                        front_light_model.setVisibility(View.GONE);
                        tail_light_model.setVisibility(View.VISIBLE);
                    }
                }
                // 闪光间隔布局
                light_delay_layout.setVisibility(View.VISIBLE);
            }

            if (!FromNetUtil.isFromNet(group)) {
                Call<BaseResponse<String>> call = apiServer.lightModel(params);
                call.enqueue(new SCallBack<BaseResponse<String>>() {
                    @Override
                    public void onSuccess(@NonNull BaseResponse<String> result) {
                        ToastUtil.show(result.getMsg());
                    }
                });
                if (checkedId == R.id.light_model_shake) {
                    setShakeModel();
                }
            }
        });
        // 前灯的闪光模式
        front_light_model.setOnCheckedChangeListener((group, checkedId) ->
        {
            if (FromNetUtil.isFromNet(group))
                return;
            setShakeModel();
        });
        // 尾灯的闪光模式
        tail_light_model.setOnCheckedChangeListener((group, checkedId) ->
        {
            if (FromNetUtil.isFromNet(group))
                return;
            setShakeModel();
        });
        // 闪光间隔
        light_delay.setOnCheckedChangeListener((group, checkedId) ->
        {
            if (FromNetUtil.isFromNet(group))
                return;
            setShakeModel();
        });
    }

    @Override
    public void loadData() {
        Map<String, Object> bodyParams = new HashMap<>();
        bodyParams.put("vehicleId", device.getId());
        Call<BaseResponse<List<SettingEntity>>> call = apiServer.getSettingCall(bodyParams);
        call.enqueue(new SCallBack<BaseResponse<List<SettingEntity>>>() {
            @Override
            public void onSuccess(@NonNull BaseResponse<List<SettingEntity>> result) {
                if (result.isSuccess()) {
                    List<SettingEntity> entitys = result.getData();
                    SettingEntityPublish publish = new SettingEntityPublish(entitys);
                    // 灯光工作模式
                    publish.setOnWarningLightWorkModel(value ->
                    {
                        //0：自动；1：手动
                        int id = 0;
                        switch (value) {
                            case 0:
                                id = R.id.work_model_auto;
                                break;
                            case 1:
                                id = R.id.work_model_motion;
                                break;
                        }
                        FromNetUtil.setCheckedFromNet(work_model, id);
                    });
                    // 亮度调节
                    publish.setOnLightRevise(value ->
                    {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            intensity_seekbar.setProgress(value, true);
                        } else {
                            intensity_seekbar.setProgress(value);
                        }
                    });
                    // 刹车灵敏度
                    publish.setOnBrakeSensitivity(value ->
                    {
                        //0:低,1:中,2:高
                        int id = 0;
                        switch (value) {
                            case 0:
                                id = R.id.brake_sensitivity_low;
                                break;
                            case 1:
                                id = R.id.brake_sensitivity_middle;
                                break;
                            case 2:
                                id = R.id.brake_sensitivity_high;
                                break;
                        }
                        FromNetUtil.setCheckedFromNet(brake_sensitivity, id);
                    });
                    // 灯光模式
                    publish.setOnLightModel(value ->
                    {
                        // 0：灯常亮,1灯常灭,2闪烁
                        int id = 0;
                        switch (value) {
                            case 0:
                                id = R.id.light_model_always;
                                break;
                            case 1:
                                id = R.id.light_model_off;
                                break;
                            case 2:
                                id = R.id.light_model_shake;
                                break;
                        }
                        FromNetUtil.setCheckedFromNet(light_model, id);
                    });
                    // 闪烁模式
                    publish.setOnShakeModel(value ->
                    {
                        // 1:SOS;2:闪烁模式1；3：闪烁模式2；4:闪灯；5呼吸灯
                        int id = 0;
                        switch (value) {
                            case 1:
                                id = R.id.front_light_model_sos;
                                break;
                            case 2:
                                id = R.id.front_light_model_a;
                                break;
                            case 3:
                                id = R.id.front_light_model_b;
                                break;
                            case 4:
                                id = R.id.tail_light_model_shake;
                                break;
                            case 5:
                                id = R.id.tail_light_model_breath;
                                break;
                        }
                        if (front_light_model.getVisibility() == View.VISIBLE) {
                            FromNetUtil.setCheckedFromNet(front_light_model, id);
                        } else if (tail_light_model.getVisibility() == View.VISIBLE) {
                            FromNetUtil.setCheckedFromNet(tail_light_model, id);
                        }
                    });
                    // 闪烁频率
                    publish.setOnShakeRate(value ->
                    {
                        //0:慢速；1:正常；2:快速
                        int id = 0;
                        switch (value) {
                            case 0:
                                id = R.id.light_delay_slow;
                                break;
                            case 1:
                                id = R.id.light_delay_normal;
                                break;
                            case 2:
                                id = R.id.light_delay_fast;
                                break;
                        }
                        FromNetUtil.setCheckedFromNet(light_delay, id);
                    });
                    publish.start();
                }
            }
        });
    }

    /**
     * 设置闪烁模式+闪烁频率
     */
    private void setShakeModel() {
        LightShakeModelParams params = new LightShakeModelParams(device.getId());
        int hzId = light_delay.getCheckedRadioButtonId();
        // 0:慢速；1:正常；2:快速
        // 获取频率值
        int hzValue = 0;
        if (hzId == R.id.light_delay_fast) {
            hzValue = 2;
        } else if (hzId == R.id.light_delay_normal) {
            hzValue = 1;
        } else if (hzId == R.id.light_delay_slow) {
            hzValue = 0;
        }

        // 获取模式值
        int modelId = 0;
        if (front_light_model.getVisibility() == View.VISIBLE) {
            modelId = front_light_model.getCheckedRadioButtonId();
        } else if (tail_light_model.getVisibility() == View.VISIBLE) {
            modelId = tail_light_model.getCheckedRadioButtonId();
        }

        if (modelId == R.id.front_light_model_sos) {    // SOS        1
            params.setParamValue(10 + hzValue);
        } else if (modelId == R.id.front_light_model_a) {      // 模式一      2
            params.setParamValue(20 + hzValue);
        } else if (modelId == R.id.front_light_model_b) {      // 模式二      3
            params.setParamValue(30 + hzValue);
        } else if (modelId == R.id.tail_light_model_shake) {   // 闪灯        4
            params.setParamValue(40 + hzValue);
        } else if (modelId == R.id.tail_light_model_breath) {  // 呼吸        5
            params.setParamValue(50 + hzValue);
        }
        Call<BaseResponse<String>> call = apiServer.lightShakeModel(params);
        call.enqueue(new SCallBack<BaseResponse<String>>() {
            @Override
            public void onSuccess(@NonNull BaseResponse<String> result) {
                ToastUtil.show(result.getMsg());
            }
        });
    }
}
