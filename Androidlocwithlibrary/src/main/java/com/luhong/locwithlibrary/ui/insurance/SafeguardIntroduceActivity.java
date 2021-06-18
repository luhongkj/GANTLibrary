package com.luhong.locwithlibrary.ui.insurance;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.luhong.locwithlibrary.R;
import com.luhong.locwithlibrary.R2;
import com.luhong.locwithlibrary.apadter.SafeguardIntroduceAdapter;
import com.luhong.locwithlibrary.app.BaseConstants;
import com.luhong.locwithlibrary.base.BaseMvpActivity;
import com.luhong.locwithlibrary.contract.SafeguardIntroduceContract;
import com.luhong.locwithlibrary.dialog.BaseDialog;
import com.luhong.locwithlibrary.dialog.DevicePromptDialog;
import com.luhong.locwithlibrary.entity.SafeguardIntroduceEntity;
import com.luhong.locwithlibrary.listener.SingleClickListener;
import com.luhong.locwithlibrary.presenter.SafeguardIntroducePresenter;
import com.luhong.locwithlibrary.ui.BasePdfActivity;
import com.luhong.locwithlibrary.utils.ResUtils;
import com.luhong.locwithlibrary.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class SafeguardIntroduceActivity extends BaseMvpActivity<SafeguardIntroducePresenter> implements SafeguardIntroduceContract.View {
    @BindView(R2.id.tv_check_safeguardIntroduce)
    TextView tv_check;
    @BindView(R2.id.tv_protocol_safeguardIntroduce)
    TextView tv_protocol;
    @BindView(R2.id.btn_confirm_safeguardIntroduce)
    TextView btn_confirm;
    @BindView(R2.id.rv_bicycle_safeguard_introduce)
    RecyclerView rvBicycle;
    @BindView(R2.id.rv_electric_safeguard_introduce)
    RecyclerView rvElectric;

    private SafeguardIntroduceAdapter bicycleAdapter;
    private SafeguardIntroduceAdapter electricAdapter;
    private boolean isCheck = false;
    private String insureSetId, protocol;

    @Override
    protected int initLayoutId() {
        return R.layout.activity_safeguard_introduce;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        initTitleView(true, "保障服务");
        bicycleAdapter = new SafeguardIntroduceAdapter(mActivity, new ArrayList<SafeguardIntroduceEntity>());
        rvBicycle.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        rvBicycle.setAdapter(bicycleAdapter);

        electricAdapter = new SafeguardIntroduceAdapter(mActivity, new ArrayList<SafeguardIntroduceEntity>());
        rvElectric.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        rvElectric.setAdapter(electricAdapter);
        Bundle extras = getIntent().getExtras();
        protocol = extras.getString("protocol");
        insureSetId = extras.getString("insureSetId");
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void fetchData() {
        Map<String, Object> bodyParams = new HashMap<>();
        mPresenter.getElectricIntroduce(bodyParams);
        mPresenter.getBicycleIntroduce(bodyParams);
    }

    @Override
    public void onGetElectricIntroduceSuccess(List<SafeguardIntroduceEntity> dataList) {
        electricAdapter.setNewData(dataList);
    }

    @Override
    public void onGetBicycleIntroduceSuccess(List<SafeguardIntroduceEntity> dataList) {
        bicycleAdapter.setNewData(dataList);
    }

    @Override
    public void onFailure(int errType, String errMsg) {
    }

    @Override
    protected void onEventListener() {
        tv_check.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                isCheck = !isCheck;
                if (isCheck) {
                    tv_check.setCompoundDrawablesRelativeWithIntrinsicBounds(ResUtils.resToDrawable(mActivity, R.mipmap.verification_protocol_foc), null, null, null);
                } else {
                    tv_check.setCompoundDrawablesRelativeWithIntrinsicBounds(ResUtils.resToDrawable(mActivity, R.mipmap.verification_protocol_nor), null, null, null);
                }
                btn_confirm.setEnabled(isCheck);
            }
        });
        tv_protocol.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(BaseConstants.WEB_TITLE_KEY, "丢车保障服务协议");
                bundle.putString(BaseConstants.WEB_URL_KEY, protocol/*"bicycle-agreement.pdf"*/);
                startIntentActivity(BasePdfActivity.class, bundle);
            }
        });

        btn_confirm.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                DevicePromptDialog.getInstance(mActivity).showDialog(DevicePromptDialog.TYPE_SAFEGUARD, new BaseDialog.IEventListener() {
                    @Override
                    public void onConfirm() {
                        Bundle bundle = new Bundle();
                        bundle.putString(SafeguardEditActivity.insureSetIdKey, insureSetId);
                        bundle.putInt(SafeguardEditActivity.dataTypeKey, SafeguardEditActivity.dataType_edit);
                        startIntentActivity(SafeguardEditActivity.class, bundle);
                        finish();
                    }

                    @Override
                    public void onCancel() {

                    }
                });

            }
        });
    }
}
