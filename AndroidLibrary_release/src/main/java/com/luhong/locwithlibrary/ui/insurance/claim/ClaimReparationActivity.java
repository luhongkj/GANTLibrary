package com.luhong.locwithlibrary.ui.insurance.claim;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.luhong.locwithlibrary.R;
import com.luhong.locwithlibrary.R2;
import com.luhong.locwithlibrary.api.AppConstants;
import com.luhong.locwithlibrary.base.BaseMvpActivity;
import com.luhong.locwithlibrary.contract.ClaimContract;
import com.luhong.locwithlibrary.entity.ClaimEntity;
import com.luhong.locwithlibrary.entity.UrlEntity;
import com.luhong.locwithlibrary.listener.SingleClickListener;
import com.luhong.locwithlibrary.presenter.ClaimPresenter;
import com.luhong.locwithlibrary.utils.BaseUtils;
import com.luhong.locwithlibrary.utils.ImageLoadUtils;
import com.luhong.locwithlibrary.utils.StringUtils;
import com.zyq.easypermission.EasyPermissionResult;

import java.util.List;

import butterknife.BindView;

/**
 * 理赔进度-理赔
 */
public class ClaimReparationActivity extends BaseMvpActivity<ClaimPresenter> implements ClaimContract.View {
    @BindView(R2.id.tv_forensics_claimSchedule)
    TextView tv_forensics;
    @BindView(R2.id.tv_verify_claimSchedule)
    TextView tv_verify;
    @BindView(R2.id.tv_reparation_claimSchedule)
    TextView tv_reparation;
    @BindView(R2.id.tv_claimQuota_claimReparation)
    TextView tv_claimQuota;
    @BindView(R2.id.tv_certificateText_claimReparation)
    TextView tv_certificateText;
    @BindView(R2.id.iv_certificateQR_claimReparation)
    ImageView iv_certificateQR;
    @BindView(R2.id.btn_confirm_claimReparation)
    Button btn_confirm;

    private Bitmap bitmapQR;
    private String claimId;

    @Override
    protected int initLayoutId() {
        return R.layout.activity_claim_reparation;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        initTitleView(true, "理赔进度");
        tv_forensics.setEnabled(true);
        tv_verify.setEnabled(true);
        tv_reparation.setEnabled(true);

        Bundle bundle = getIntent().getExtras();
        claimId = bundle.getString(AppConstants.dataKey);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onEventListener() {
        btn_confirm.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                requestCallPermissions(new EasyPermissionResult() {
                    @Override
                    public void onPermissionsAccess(int requestCode) {
                        super.onPermissionsAccess(requestCode);
                        BaseUtils.startCallPhone(mActivity, AppConstants.SERVER_ONLINE_NO, false);
                    }

                    @Override
                    public void onPermissionsDismiss(int requestCode, @NonNull List<String> permissions) {
                        super.onPermissionsDismiss(requestCode, permissions);
                        showToast("没有权限");
                    }
                });
            }
        });
    }

    @Override
    protected void fetchData() {
        if (!TextUtils.isEmpty(claimId)) mPresenter.getClaim(0, claimId);
    }

    @Override
    public void onGetClaimSuccess(int type, ClaimEntity resultEntity) {
        if (resultEntity == null) return;
        tv_claimQuota.setText("¥" + StringUtils.formatDoubleData(resultEntity.getAccount()));
        ImageLoadUtils.load(resultEntity.getQrCode(), iv_certificateQR);
        if (resultEntity.getStatus() == ClaimEntity.TYPE_ALREADYPAYED) {//已赔付
            iv_certificateQR.setAlpha(0.5f);
            btn_confirm.setVisibility(View.GONE);
            tv_certificateText.setText("理赔二维码凭证已使用");
        }
    }

    @Override
    public void onSaveClaimSuccess(ClaimEntity resultEntity) {

    }

    @Override
    public void onUpdateClaimSuccess(Object resultEntity) {

    }

    @Override
    public void onCancelClaimSuccess(Object resultEntity) {
        finish();
    }

    @Override
    public void onUploadSuccess(UrlEntity resultEntity, int position) {

    }

    @Override
    public void onFailure(int errType, String errMsg) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bitmapQR != null) bitmapQR.recycle();
    }
}
