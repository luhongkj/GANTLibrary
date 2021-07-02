package com.luhong.locwithlibrary.ui.insurance;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.luhong.locwithlibrary.R;
import com.luhong.locwithlibrary.R2;
import com.luhong.locwithlibrary.api.AppConstants;
import com.luhong.locwithlibrary.app.BaseConstants;
import com.luhong.locwithlibrary.base.BaseMvpActivity;
import com.luhong.locwithlibrary.contract.SafeguardDetailContract;
import com.luhong.locwithlibrary.entity.SafeguardEntity;
import com.luhong.locwithlibrary.listener.SingleClickListener;
import com.luhong.locwithlibrary.presenter.SafeguardDetailPresenter;
import com.luhong.locwithlibrary.ui.BasePdfActivity;
import com.luhong.locwithlibrary.ui.insurance.claim.ClaimForensicsActivity;
import com.luhong.locwithlibrary.ui.insurance.claim.ClaimLossActivity;
import com.luhong.locwithlibrary.ui.insurance.claim.ClaimReparationActivity;
import com.luhong.locwithlibrary.utils.ImageLoadUtils;
import com.luhong.locwithlibrary.utils.Logger;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

public class SafeguardDetailActivity extends BaseMvpActivity<SafeguardDetailPresenter> implements SafeguardDetailContract.View {
    public static final String dataTypeKey = "dataTypeKey";
    @BindView(R2.id.tv_safeguardName_safeguardDetail)
    TextView tv_safeguardName;
    @BindView(R2.id.tv_safeguardNo_safeguardDetail)
    TextView tv_safeguardNo;
    @BindView(R2.id.tv_name_safeguardDetail)
    TextView tv_name;
    @BindView(R2.id.tv_phoneNo_safeguardDetail)
    TextView tv_phoneNo;
    @BindView(R2.id.ll_perfect_safeguardDetail)
    RelativeLayout llPerfect;
    @BindView(R2.id.tv_address_safeguardDetail)
    TextView tvAddress;
    @BindView(R2.id.tv_idCardNo_safeguardDetail)
    TextView tvIdCardNo;
    @BindView(R2.id.iv_safeguardStatus_safeguardDetail)
    ImageView iv_safeguardStatus;
    @BindView(R2.id.iv_idCardFront_safeguardDetail)
    ImageView ivIdCardFront;
    @BindView(R2.id.iv_idCardBack_safeguardDetail)
    ImageView ivIdCardBack;
    @BindView(R2.id.tv_device_safeguardDetail)
    TextView tvDevice;
    @BindView(R2.id.tv_brand_safeguardDetail)
    TextView tvBrand;
    @BindView(R2.id.tv_bicycleType_safeguardDetail)
    TextView tvBicycleType;
    @BindView(R2.id.tv_bicycleModel_safeguardDetail)
    TextView tvBicycleModel;
    @BindView(R2.id.tv_fivePass_safeguardDetail)
    TextView tvFivePass;
    @BindView(R2.id.tv_devicePrice_safeguardDetail)
    TextView tvDevicePrice;
    @BindView(R2.id.tv_deviceDate_safeguardDetail)
    TextView tvDeviceDate;
    @BindView(R2.id.tv_safeguard_safeguardDetail)
    TextView tvSafeguard;
    @BindView(R2.id.tv_valuation_safeguardDetail)
    TextView tvValuation;
    @BindView(R2.id.tv_insuranceFee_safeguardDetail)
    TextView tvInsuranceFee;
    @BindView(R2.id.tv_expiryDate_safeguardDetail)
    TextView tvExpiryDate;
    @BindView(R2.id.tv_protocol_safeguardDetail)
    TextView tvProtocol;
    @BindView(R2.id.btn_confirm_safeguardDetail)
    Button btn_confirm;

    private SafeguardEntity safeguardEntity;

    @Override
    protected int initLayoutId() {
        return R.layout.activity_safeguard_detail;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        initTitleView(true, "保单详情");
        Bundle bundle = getIntent().getExtras();
        safeguardEntity = (SafeguardEntity) bundle.getSerializable(dataTypeKey);
        updateView(safeguardEntity);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onEventListener() {
        tvProtocol.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(BaseConstants.WEB_TITLE_KEY, "丢车保障服务协议");
                bundle.putString(BaseConstants.WEB_URL_KEY, safeguardEntity.getUrl());
                startIntentActivity(BasePdfActivity.class, bundle);
            }
        });
        btn_confirm.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (safeguardEntity != null) {
                    if (safeguardEntity.getClaimDto() != null && safeguardEntity.getClaimDto().getStatus() >= SafeguardEntity.ClaimDto.TYPE_WAITEVIDENCE) {
                        Bundle bundle = new Bundle();
                        bundle.putString(AppConstants.dataKey, safeguardEntity.getClaimDto().getId());
                        if (safeguardEntity.getClaimDto().getStatus() >= SafeguardEntity.ClaimDto.TYPE_WAITPAYFOR) {
                            startIntentActivity(ClaimReparationActivity.class, bundle);
                        } else {
                            bundle.putInt(AppConstants.dataTypeKey, 0);
                            bundle.putString(AppConstants.vehicleIdKey, safeguardEntity.getMyVehicleId());
                            startIntentActivity(ClaimForensicsActivity.class, bundle);
                        }
                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putString(AppConstants.dataKey, safeguardEntity.getId());
                        bundle.putString(AppConstants.vehicleIdKey, safeguardEntity.getMyVehicleId());
                          startIntentActivity(ClaimLossActivity.class, bundle);
                    }
                    finish();
                }
            }
        });
    }

    @Override
    protected void fetchData() {
        if (safeguardEntity != null) {
            Map<String, Object> bodyParams = new HashMap<>();
            bodyParams.put("id", safeguardEntity.getId());
            mPresenter.getSafeguardDetail(bodyParams);
        }
    }

    @Override
    public void onSafeguardDetailSuccess(SafeguardEntity resultEntity) {
        safeguardEntity = resultEntity;
        updateView(resultEntity);
    }

    @Override
    public void onFailure(int errType, String errMsg) {

    }

    private void updateView(SafeguardEntity resultEntity) {
        if (resultEntity == null) return;
        try {
            if (resultEntity.getStatus() == SafeguardEntity.TYPE_GUARANTEED) {
                iv_safeguardStatus.setImageResource(R.mipmap.safeguard_claim_ongoing);
            } else if (resultEntity.getStatus() == SafeguardEntity.TYPE_EXPIRED) {
                iv_safeguardStatus.setImageResource(R.mipmap.safeguard_claim_expired);
                btn_confirm.setVisibility(View.GONE);
            } else if (resultEntity.getStatus() == SafeguardEntity.TYPE_CLAIMED) {
                iv_safeguardStatus.setImageResource(R.mipmap.safeguard_claim_completed);
            }
            tv_safeguardNo.setText("保单号：" + resultEntity.getInsuranceNo());
            tv_name.setText(resultEntity.getRealName());
            tv_phoneNo.setText(resultEntity.getPhone());
            tvAddress.setText(safeguardEntity.getProvice() + safeguardEntity.getCity() + safeguardEntity.getArea() + resultEntity.getAddress());
            tvIdCardNo.setText(resultEntity.getIdCard());
            ImageLoadUtils.load(resultEntity.getCardFront(), ivIdCardFront);
            ImageLoadUtils.load(resultEntity.getCardBack(), ivIdCardBack);
            tvDevice.setText(resultEntity.getNickName());
            tvBrand.setText(resultEntity.getVehicleBrand());
            tvBicycleType.setText(resultEntity.getVehicleType());
            tvBicycleModel.setText(resultEntity.getVehicleModel());
            tvFivePass.setText(resultEntity.getVin());
            tvDevicePrice.setText("¥" + resultEntity.getPrice());
            tvDeviceDate.setText(resultEntity.getBuyDate().replace("-", "/"));
            tvValuation.setText("¥" + resultEntity.getValuation());
            tvInsuranceFee.setText("¥" + resultEntity.getInsureCost());
            tvExpiryDate.setText(resultEntity.getDeadlineStart().replace("-", "/") + "-" + resultEntity.getDeadlineEnd().replace("-", "/"));

            tv_safeguardName.setText(resultEntity.getInsureType());
            tvSafeguard.setText(resultEntity.getInsureSetName());
            if (resultEntity.getClaimDto() != null && resultEntity.getClaimDto().getStatus() >= SafeguardEntity.ClaimDto.TYPE_WAITEVIDENCE) {
                if (resultEntity.getClaimDto().getStatus() >= SafeguardEntity.ClaimDto.TYPE_WAITPAYFOR) {
                    if (resultEntity.getClaimDto().getStatus() == SafeguardEntity.ClaimDto.TYPE_ALREADYCANCEL) {
                        btn_confirm.setVisibility(View.GONE);
                    } else {
                        btn_confirm.setText("查看理赔");
                        btn_confirm.setVisibility(View.VISIBLE);
                    }
                } else {
                    btn_confirm.setText("查看理赔进度");
                }
            } else {
                btn_confirm.setText("申请理赔");
            }
        } catch (Exception e) {
            Logger.error("更新数据出错=" + e);
        }
    }
}
