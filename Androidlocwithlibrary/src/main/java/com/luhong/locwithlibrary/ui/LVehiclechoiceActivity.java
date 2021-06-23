package com.luhong.locwithlibrary.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.luhong.locwithlibrary.R;
import com.luhong.locwithlibrary.R2;
import com.luhong.locwithlibrary.apadter.VehiclechoiceAdapter;
import com.luhong.locwithlibrary.base.BaseMvpActivity;
import com.luhong.locwithlibrary.contract.home.LVehicleChoiceContract;
import com.luhong.locwithlibrary.dialog.BaseDialog;
import com.luhong.locwithlibrary.dialog.MessageDialog;
import com.luhong.locwithlibrary.entity.CarEntity;
import com.luhong.locwithlibrary.entity.DeviceEntity;
import com.luhong.locwithlibrary.entity.VehicleListEntity;
import com.luhong.locwithlibrary.listener.SingleClickListener;
import com.luhong.locwithlibrary.presenter.home.LVehicleChoicePresenter;
import com.luhong.locwithlibrary.ui.equipment.DeviceManageActivity;
import com.luhong.locwithlibrary.ui.equipment.LDeviceAddActivity;
import com.luhong.locwithlibrary.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.luhong.locwithlibrary.ui.equipment.LDeviceAddActivity.ADDDEVICE_RESULT_;

public class LVehiclechoiceActivity extends BaseMvpActivity<LVehicleChoicePresenter> implements LVehicleChoiceContract.View {

    @BindView(R2.id.tv_center_title)
    TextView tvCenterTitle;
    @BindView(R2.id.rv_list)
    RecyclerView rvList;
    @BindView(R2.id.btn_confirm)
    Button btnConfirm;
    VehiclechoiceAdapter adapter;
    boolean isHome = false;//是否是主页进来

    @Override
    protected void fetchData() {
        mPresenter.getVehicle();
        mPresenter.getDeviceList(this);
    }

    List<VehicleListEntity> list;

    @Override
    protected int initLayoutId() {
        return R.layout.activity_lvehiclechoice;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        initTitleView(true, "爱车列表");
        isHome = getIntent().getBooleanExtra("isHome", false);
        rvList.setLayoutManager(new LinearLayoutManager(this));
        btnConfirm.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (cheVehicleList == null) {
                    ToastUtil.show("请选择爱车");
                    return;
                }
                boolean isbdin = false;
                String carId = "";
                if (dataList != null) {
                    for (DeviceEntity deviceEntity : dataList) {
                        if (deviceEntity.getVin().equals(cheVehicleList.getVin())) {
                            isbdin = true;
                            carId = cheVehicleList.getVehicleName();
                        }
                    }
                }
                if (isbdin) {
                    MessageDialog.getInstance(mActivity).showDialog("爱车" + carId + "\n已绑定鹿卫士设备,是否要继续绑定?\n如需绑定其它车辆,请返回选择或新增爱车后选择爱车绑定", "立即绑定", new MessageDialog.IEventListener() {
                        @Override
                        public void onConfirm() {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(LDeviceAddActivity.VEHICLECHOICE_DATA, cheVehicleList);
                            startIntentActivityForResult(LDeviceAddActivity.class, ADDDEVICE_RESULT_, bundle);
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(LDeviceAddActivity.VEHICLECHOICE_DATA, cheVehicleList);
                    startIntentActivityForResult(LDeviceAddActivity.class, ADDDEVICE_RESULT_, bundle);
                }
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onEventListener() {

    }

    @Override
    public void onFailure(int errType, String errMsg) {

    }

    VehicleListEntity cheVehicleList;

    @Override
    public void onVehicleListSuccess(List<VehicleListEntity> resultEntity) {
        this.list = resultEntity;
        adapter = new VehiclechoiceAdapter(this, list, new VehiclechoiceAdapter.ISportRecordListener() {
            @Override
            public void onEdit(VehicleListEntity data, int position) {
                cheVehicleList = data;
                for (VehicleListEntity forData : list) {
                    forData.setFlag(false);
                }
                list.get(position).setFlag(true);
                adapter.notifyDataSetChanged();
            }
        });
        rvList.setAdapter(adapter);
    }

    List<DeviceEntity> dataList;

    @Override
    public void onDeviceListSuccess(List<DeviceEntity> dataList) {
        this.dataList = dataList;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ADDDEVICE_RESULT_) {
            if (isHome) {
                setResult(ADDDEVICE_RESULT_);
                finish();
                startIntentActivity(DeviceManageActivity.class);
            } else {
                finish();
            }
        }
    }
}
