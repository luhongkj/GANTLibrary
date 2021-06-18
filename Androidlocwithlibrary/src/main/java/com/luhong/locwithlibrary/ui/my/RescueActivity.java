package com.luhong.locwithlibrary.ui.my;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.luhong.locwithlibrary.R;
import com.luhong.locwithlibrary.apadter.RescueAdapter;
import com.luhong.locwithlibrary.api.ApiClient;
import com.luhong.locwithlibrary.api.ApiServer;
import com.luhong.locwithlibrary.api.AppVariable;
import com.luhong.locwithlibrary.api.SCallBack;
import com.luhong.locwithlibrary.app.BaseVariable;
import com.luhong.locwithlibrary.base.BaseActivity;
import com.luhong.locwithlibrary.base.BaseActivityTow;
import com.luhong.locwithlibrary.dialog.DefaultAskDialog;
import com.luhong.locwithlibrary.dialog.DefaultConfirmDialog;
import com.luhong.locwithlibrary.dialog.DeleteContactDialog;
import com.luhong.locwithlibrary.entity.DeviceEntity;
import com.luhong.locwithlibrary.entity.RescueBean;
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
 * 紧急救援设置
 */
public class RescueActivity extends BaseActivityTow {
    private RecyclerView recycler_view;
    private RescueAdapter adapter;
    private ApiServer apiServer;
    private DeleteContactDialog deleteContactDialog;
    private Switch switch_setting;
    private APPTittle app_title;
    private final int ADD_CONTACT_REQ = 0x01;
    // 确认框
    private DefaultConfirmDialog comfirmDIalog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_rescue);
        super.onCreate(savedInstanceState);
    }


    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        apiServer = ApiClient.getInstance().getApiServer();
        adapter = new RescueAdapter();
        comfirmDIalog = new DefaultConfirmDialog(this);
    }

    @Override
    public void initView() {
        recycler_view = findViewById(R.id.recycler_view);
        switch_setting = findViewById(R.id.switch_setting);
        app_title = findViewById(R.id.app_title);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.VERTICAL);
        recycler_view.setLayoutManager(manager);
        recycler_view.setAdapter(adapter);
        deleteContactDialog = new DeleteContactDialog(this);
    }

    @Override
    public void initEvent() {
        app_title.setTitleText("紧急救援设置");
        app_title.onLeftImgClick(v -> finish());

        // 添加紧急联系人
        adapter.setOnAddClickListener(new RescueAdapter.OnAddClickListener() {
            @Override
            public void onClick() {
                Intent intent = new Intent(RescueActivity.this, AddContactActivity.class);
                startActivityForResult(intent, ADD_CONTACT_REQ);
            }
        });
        // 删除按钮
        adapter.setOnDeleteItemClickListener(new RescueAdapter.OnDeleteItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                RescueBean bean = adapter.list.get(position);
                deleteContactDialog.setOnOkClickListener(v1 ->
                {
                    Map<String, Object> params = new HashMap<>();
                    params.put("contactName", bean.getContactName());
                    params.put("phone", bean.getPhone());
                    params.put("userId", BaseVariable.userId);
                    Call<BaseResponse<String>> call = ApiClient.getInstance().getApiServer().deleteContact(params);
                    call.enqueue(new SCallBack<BaseResponse<String>>() {
                        @Override
                        public void onSuccess(@NonNull BaseResponse<String> result) {
                            ToastUtil.show(result.getMsg());
                            if (result.isSuccess()) {
                                loadData();
                            }
                        }
                    });
                });
                deleteContactDialog.show();
            }
        });
        // 开关按钮
        switch_setting.setOnCheckedChangeListener((buttonView, isChecked) ->
        {
            if (FromNetUtil.isFromNet(switch_setting)) {
                return;
            }
            switch_setting.setEnabled(false);

            if (!isChecked) {
                DefaultAskDialog dialog = new DefaultAskDialog(this);
                dialog.setContent("是否关闭紧急救援？", "关闭后，出现紧急状态将无法启动紧急救援");
                dialog.setOnOkClickListener(v ->
                {
                    turnOnOrOffRescue(isChecked, switch_setting);
                });
                dialog.setOnCancelListener(v ->
                {
                    FromNetUtil.setCheckedFromNet(switch_setting, !isChecked);
                    switch_setting.setEnabled(true);
                });
                dialog.show();
            } else {
                turnOnOrOffRescue(isChecked, switch_setting);
            }
        });
    }

    /**
     * 加载数据
     */
    @Override
    public void loadData() {
        // 检查设备类型
        checkDeviceType();
    }

    /**
     * 检查设备类型
     */
    private void checkDeviceType() {
        Call<BaseResponse<List<DeviceEntity>>> call = apiServer.getDeviceListCall(new HashMap<>());
        call.enqueue(new SCallBack<BaseResponse<List<DeviceEntity>>>() {
            @Override
            public void onSuccess(@NonNull BaseResponse<List<DeviceEntity>> result) {
                if (result.isSuccess()) {
                    List<DeviceEntity> datas = result.getData();
                    if (datas == null || datas.isEmpty()) {
                        comfirmDIalog.setContent("此功能仅限指定设备使用");
                        comfirmDIalog.setOnOkClickListener(v ->
                        {
                            finish();
                        });
                        comfirmDIalog.show();
                        return;
                    }
                    DeviceEntity device = null;
                    for (DeviceEntity data : datas) {
                        if (data.getId().equals(AppVariable.currentVehicleId)) {
                            device = data;
                        }
                    }
                    if (device != null) {
                        switch (device.getUnitType()) {
                            case "5": // 前灯
                            case "7": // 头盔
                            case "4": // V3
                            case "6": // 尾灯
                            {
                                // 加载获取用户紧急救援设置信息
                                loadEmergencyInfo();
                                // 加载紧急联系人
                                loadContacts();
                            }
                            break;
                            default: {
                                comfirmDIalog.setContent("所选设备不支持此功能设置");
                                comfirmDIalog.setOnOkClickListener(v ->
                                {
                                    finish();
                                });
                                comfirmDIalog.show();
                            }
                            break;
                        }
                    }
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == ADD_CONTACT_REQ && resultCode == Activity.RESULT_OK) {
            loadData();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 加载获取用户紧急救援设置信息
     */
    private void loadEmergencyInfo() {
        Map<String, Object> bodyParams = new HashMap<>();
        bodyParams.put("vehicleId", AppVariable.currentVehicleId);
        Call<BaseResponse<List<SettingEntity>>> call = apiServer.getSettingCall(bodyParams);
        call.enqueue(new SCallBack<BaseResponse<List<SettingEntity>>>() {
            @Override
            public void onSuccess(@NonNull BaseResponse<List<SettingEntity>> result) {
                if (result.isSuccess()) {
                    List<SettingEntity> list = result.getData();
                    SettingEntityPublish publish = new SettingEntityPublish(list);
                    publish.setOnRescue(value ->
                    {
                        FromNetUtil.setCheckedFromNet(switch_setting, value == 1);
                    });
                    publish.start();
                } else {
                    ToastUtil.show(result.getMsg());
                }
            }
        });
    }

    /**
     * 获取紧急联系人
     */
    private void loadContacts() {
        RescueBean titleBean = new RescueBean();
        titleBean.setType(RescueBean.Type.TITLE);
        RescueBean addBean = new RescueBean();
        addBean.setType(RescueBean.Type.ADD);


        adapter.list.clear();
        adapter.list.add(addBean);
        adapter.notifyDataSetChanged();


        Map<String, Object> bodyParams = new HashMap<>();
        bodyParams.put("userId", BaseVariable.userId);
        Call<BaseResponse<List<RescueBean>>> call = apiServer.getContacts(bodyParams);
        call.enqueue(new SCallBack<BaseResponse<List<RescueBean>>>() {
            @Override
            public void onSuccess(@NonNull BaseResponse<List<RescueBean>> result) {
                if (result.isSuccess()) {
                    adapter.list.addAll(0, result.getData());
                    if (!result.getData().isEmpty())
                        adapter.list.add(0, titleBean);
                    adapter.notifyDataSetChanged();
                } else {
                    ToastUtil.show(result.getMsg());
                }
            }
        });
    }

    /**
     * 打开或者关闭紧急救援
     * @param onOrOff        打开或者关闭
     * @param switch_setting
     */
    private void turnOnOrOffRescue(boolean onOrOff, Switch switch_setting) {
        int count = 0;
        for (RescueBean bean : adapter.list) {
            if (bean.getType() == RescueBean.Type.DATA) {
                count++;
            }
        }
        if (onOrOff && count == 0) {
            DefaultConfirmDialog dialog = new DefaultConfirmDialog(this);
            dialog.setContent("紧急救援提醒", "此功能需先添加紧急联系人方可生效");
            dialog.show();
        }

        Map<String, Object> params = new HashMap<>();

        params.put("vehicleId", AppVariable.currentVehicleId);
        params.put("type", 12);
        params.put("value", onOrOff ? 1 : 0);

        Call<BaseResponse<Object>> call = apiServer.setSettingCall(params);
        call.enqueue(new SCallBack<BaseResponse<Object>>() {
            @Override
            public void onSuccess(@NonNull BaseResponse<Object> result) {
                if (!result.isSuccess()) {
                    FromNetUtil.setCheckedFromNet(switch_setting, !onOrOff);
                }
                ToastUtil.show(result.getMsg());
            }

            @Override
            public void onFinally() {
                switch_setting.setEnabled(true);
            }
        });
    }

}
