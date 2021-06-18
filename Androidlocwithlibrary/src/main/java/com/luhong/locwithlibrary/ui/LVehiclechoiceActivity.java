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
import com.luhong.locwithlibrary.entity.CarEntity;
import com.luhong.locwithlibrary.entity.VehicleListEntity;
import com.luhong.locwithlibrary.listener.SingleClickListener;
import com.luhong.locwithlibrary.presenter.home.LVehicleChoicePresenter;
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

    @Override
    protected void fetchData() {
        mPresenter.getVehicle();
    }

    List<VehicleListEntity> list;

    @Override

    protected int initLayoutId() {
        return R.layout.activity_lvehiclechoice;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        initTitleView(true, "爱车列表");
        rvList.setLayoutManager(new LinearLayoutManager(this));
        btnConfirm.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (cheVehicleList == null) {
                    ToastUtil.show("请选择爱车");
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable(LDeviceAddActivity.VEHICLECHOICE_DATA, cheVehicleList);
                startIntentActivityForResult(LDeviceAddActivity.class, ADDDEVICE_RESULT_, bundle);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ADDDEVICE_RESULT_) {
            setResult(ADDDEVICE_RESULT_);
        }
    }
}
