package com.luhong.locwithlibrary.ui.my;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.luhong.locwithlibrary.BuildConfig;
import com.luhong.locwithlibrary.R;
import com.luhong.locwithlibrary.R2;
import com.luhong.locwithlibrary.api.AppVariable;
import com.luhong.locwithlibrary.app.BaseConstants;
import com.luhong.locwithlibrary.base.BaseActivity;
import com.luhong.locwithlibrary.base.BaseMvpActivity;
import com.luhong.locwithlibrary.contract.FlowAccountContract;
import com.luhong.locwithlibrary.dialog.DeviceManageDialog;
import com.luhong.locwithlibrary.entity.FlowAccountEntity;
import com.luhong.locwithlibrary.entity.FlowBillEntity;
import com.luhong.locwithlibrary.listener.SingleClickListener;
import com.luhong.locwithlibrary.net.response.BasePageEntity;
import com.luhong.locwithlibrary.presenter.FlowAccountPresenter;
import com.luhong.locwithlibrary.ui.BasePdfActivity;
import com.luhong.locwithlibrary.ui.equipment.DeviceManageActivity;
import com.luhong.locwithlibrary.ui.equipment.FlowAccountActivity;
import com.luhong.locwithlibrary.ui.equipment.SpecificationActivity;
import com.luhong.locwithlibrary.ui.insurance.SafeguardActivity;
import com.luhong.locwithlibrary.utils.AppUtils;
import com.luhong.locwithlibrary.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.luhong.locwithlibrary.api.AppVariable.BACK_ACTIVITE;

public class MyActivity extends BaseMvpActivity<FlowAccountPresenter> implements FlowAccountContract.View {

    @BindView(R2.id.tv_left_title)
    TextView tvLeftTitle;
    @BindView(R2.id.tv_center_title)
    TextView tvCenterTitle;
    @BindView(R2.id.iv_icon_title)
    ImageView ivIconTitle;
    @BindView(R2.id.tv_right_title)
    TextView tvRightTitle;
    @BindView(R2.id.ll_titleBar_title)
    LinearLayout llTitleBarTitle;
    @BindView(R2.id.light_work_model_name)
    TextView lightWorkModelName;
    @BindView(R2.id.ll_flow)
    LinearLayout llFlow;
    @BindView(R2.id.ll_safeguard)
    LinearLayout llSafeguard;
    @BindView(R2.id.ll_equipment)
    LinearLayout llEquipment;
    @BindView(R2.id.ll_urgency)
    LinearLayout llUrgency;
    @BindView(R2.id.ll_specification)
    LinearLayout llSpecification;
    @BindView(R2.id.ll_opinion)
    LinearLayout llOpinion;

    @BindView(R2.id.ll_agreement)
    LinearLayout ll_agreement;

    @Override
    protected int initLayoutId() {
        return R.layout.activity_my;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        tvCenterTitle.setText("我的");
        tvLeftTitle.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                finish();
            }
        });
        llSafeguard.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (!AppVariable.GIANT_ISBIN) {
                    ToastUtil.show("请添加设备");
                    return;
                }
                startIntentActivity(SafeguardActivity.class);
            }
        });
        llEquipment.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (!AppVariable.GIANT_ISBIN) {
                    ToastUtil.show("请添加设备");
                    return;
                }
                startIntentActivityForResult(DeviceManageActivity.class, 100);
            }
        });

//https://app.luhongkj.com:9443/doc/privacy-lib.pdf
        ll_agreement.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(BaseConstants.WEB_TITLE_KEY, "隐私协议");
                bundle.putString(BaseConstants.WEB_URL_KEY, BuildConfig.BASE_WEB_URL + "privacy-lib.pdf");
                startIntentActivity(BasePdfActivity.class, bundle);
            }
        });

        llFlow.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                startIntentActivity(FlowAccountActivity.class);
            }
        });
        llUrgency.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (!AppVariable.GIANT_ISBIN) {
                    ToastUtil.show("请添加设备");
                    return;
                }
                startIntentActivity(RescueActivity.class);
            }
        });
        llOpinion.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                startIntentActivity(FeedbackActivity.class);
            }
        });
        llSpecification.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (!AppVariable.GIANT_ISBIN) {
                    ToastUtil.show("请添加设备");
                    return;
                }
                startIntentActivity(SpecificationActivity.class);
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
    protected void fetchData() {
        mPresenter.getFlowAccountInfo();
    }

    @Override
    public void onFlowAccountInfoSuccess(FlowAccountEntity resultEntity) {
        if (resultEntity != null || resultEntity.getMoney() < resultEntity.getFlowFee()) {
            float moneys = resultEntity.getMoney();
            float flowFee = (float) resultEntity.getFlowFee();
            lightWorkModelName.setText("剩余" + (int) (moneys / flowFee) + "个月");
        }
    }

    @Override
    public void onBillSuccess(BasePageEntity<FlowBillEntity> dataList) {

    }

    @Override
    public void onFailure(int errType, String errMsg) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.getFlowAccountInfo();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == BACK_ACTIVITE) {
            setResult(BACK_ACTIVITE);
            finish();
        }

        if (requestCode == Intent.FLAG_ACTIVITY_NEW_TASK && AppUtils.checkStorageManagerPermission()) {
            initData();
        }
    }


}
