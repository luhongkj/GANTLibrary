package com.luhong.locwithlibrary.ui.insurance.claim;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.luhong.locwithlibrary.R;
import com.luhong.locwithlibrary.R2;
import com.luhong.locwithlibrary.api.AppConstants;
import com.luhong.locwithlibrary.base.BaseMvpActivity;
import com.luhong.locwithlibrary.contract.ClaimContract;
import com.luhong.locwithlibrary.entity.ClaimEntity;
import com.luhong.locwithlibrary.entity.UrlEntity;
import com.luhong.locwithlibrary.listener.SingleClickListener;
import com.luhong.locwithlibrary.presenter.ClaimPresenter;

import butterknife.BindView;

/**
 * 理赔进度-报失
 */
public class ClaimLossActivity extends BaseMvpActivity<ClaimPresenter> implements ClaimContract.View
{
    @BindView(R2.id.btn_confirm_claimLoss)
    Button btn_confirm;
    private String safeguardId, vehicleId;

    @Override
    protected int initLayoutId()
    {
        return R.layout.activity_claim_loss;
    }

    @Override
    protected void initView(Bundle savedInstanceState)
    {
        initTitleView(true, "理赔进度");
        Bundle bundle = getIntent().getExtras();
        safeguardId = bundle.getString(AppConstants.dataKey);
        vehicleId = bundle.getString(AppConstants.vehicleIdKey);
    }

    @Override
    protected void initData()
    {

    }

    @Override
    protected void onEventListener()
    {
        btn_confirm.setOnClickListener(new SingleClickListener()
        {
            @Override
            public void onSingleClick(View v)
            {
                mPresenter.saveClaim(safeguardId);
            }
        });
    }

    @Override
    protected void fetchData()
    {

    }

    @Override
    public void onGetClaimSuccess(int type, ClaimEntity resultEntity)
    {

    }

    @Override
    public void onSaveClaimSuccess(ClaimEntity resultEntity)
    {
        if (resultEntity == null) return;
        Bundle bundle = new Bundle();
        bundle.putInt(AppConstants.dataTypeKey, 1);
        bundle.putString(AppConstants.dataKey, resultEntity.getId());
        bundle.putString(AppConstants.vehicleIdKey, vehicleId);
        startIntentActivity(ClaimForensicsActivity.class, bundle);
        finish();
    }

    @Override
    public void onUpdateClaimSuccess(Object resultEntity)
    {

    }

    @Override
    public void onCancelClaimSuccess(Object resultEntity)
    {

    }

    @Override
    public void onUploadSuccess(UrlEntity resultEntity, int position)
    {

    }

    @Override
    public void onFailure(int errType, String errMsg)
    {

    }
}
