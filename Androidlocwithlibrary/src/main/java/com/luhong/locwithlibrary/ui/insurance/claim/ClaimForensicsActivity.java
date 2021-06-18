package com.luhong.locwithlibrary.ui.insurance.claim;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.luhong.locwithlibrary.R;
import com.luhong.locwithlibrary.R2;
import com.luhong.locwithlibrary.api.AppConstants;
import com.luhong.locwithlibrary.base.BaseMvpActivity;
import com.luhong.locwithlibrary.contract.ClaimContract;
import com.luhong.locwithlibrary.dialog.DateDIYDialog;
import com.luhong.locwithlibrary.dialog.PhotoDialog;
import com.luhong.locwithlibrary.entity.ClaimEntity;
import com.luhong.locwithlibrary.entity.UrlEntity;
import com.luhong.locwithlibrary.imagepicker.ImagePicker;
import com.luhong.locwithlibrary.listener.SingleClickListener;
import com.luhong.locwithlibrary.presenter.ClaimPresenter;
import com.luhong.locwithlibrary.utils.BaseUtils;
import com.luhong.locwithlibrary.utils.DateUtils;
import com.luhong.locwithlibrary.utils.FileUtils;
import com.luhong.locwithlibrary.utils.ImageLoadUtils;
import com.luhong.locwithlibrary.utils.Logger;
import com.luhong.locwithlibrary.utils.LuBanUtils;
import com.luhong.locwithlibrary.utils.ResUtils;
import com.luhong.locwithlibrary.utils.StringUtils;
import com.zyq.easypermission.EasyPermissionResult;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import top.zibin.luban.OnCompressListener;

//import com.lzy.imagepicker.ImagePicker;
//import com.lzy.imagepicker.bean.ImageItem;

/**
 * 理赔进度-取证/审核
 * "待取证":"1",
 * "待审核":"2",
 * "审核未过":"3",
 * "待赔付":"4",
 * "已理赔":"5",
 * "已取消":"6"
 */
public class ClaimForensicsActivity extends BaseMvpActivity<ClaimPresenter> implements ClaimContract.View
{
    @BindView(R2.id.rl_description_claimForensics)
    RelativeLayout rl_description;
    @BindView(R2.id.tv_description_claimForensics)
    TextView tv_description;
    @BindView(R2.id.tv_evidenceTime_claimForensics)
    TextView tv_evidenceTime;
    @BindView(R2.id.tv_forensics_claimSchedule)
    TextView tv_forensics;
    @BindView(R2.id.tv_verify_claimSchedule)
    TextView tv_verify;
    @BindView(R2.id.iv_evidence_claimForensics)
    ImageView iv_evidence;
    @BindView(R2.id.tv_evidenceText_claimForensics)
    TextView tv_evidenceText;
    @BindView(R2.id.iv_filingReceipt_claimForensics)
    ImageView iv_filingReceipt;
    @BindView(R2.id.tv_filingReceiptText_claimForensics)
    TextView tv_filingReceiptText;
    @BindView(R2.id.tv_filingDate_claimForensics)
    TextView tv_filingDate;
    @BindView(R2.id.rl_cancel_claimForensics)
    RelativeLayout rl_cancel;
    @BindView(R2.id.btn_confirm_claimForensics)
    Button btn_confirm;
    private String claimId, vehicleId, filingReceiptPath, filingTime, evidenceTime, distance;
    private String evidenceUrl, filingReceiptUrl;
    private double phoneLat, phoneLng, deviceLat, deviceLng;
    private File picFile;
    private CountDownTimer countDownTimer;
    private int dataType;
    private boolean isEvidenceEnabled = true;
    private boolean isEvidenced = false;
    private boolean isSubmit = true;

    @Override
    protected int initLayoutId()
    {
        return R.layout.activity_claim_forensics;
    }

    @Override
    protected void initView(Bundle savedInstanceState)
    {
        initTitleView(true, "理赔进度");
        tv_forensics.setEnabled(true);
        Bundle bundle = getIntent().getExtras();
        dataType = bundle.getInt(AppConstants.dataTypeKey);
        claimId = bundle.getString(AppConstants.dataKey);
        vehicleId = bundle.getString(AppConstants.vehicleIdKey);
    }

    @Override
    protected void initData()
    {

    }

    @Override
    protected void onEventListener()
    {
        tv_evidenceText.setOnClickListener(new SingleClickListener()
        {
            @Override
            public void onSingleClick(View v)
            {
                Bundle bundle = new Bundle();
                bundle.putString(AppConstants.dataKey, claimId);
                bundle.putString(AppConstants.vehicleIdKey, vehicleId);
                startIntentActivityForResult(ClaimOnSiteActivity.class, AppConstants.REQUEST_CODE_QUERY, bundle);
            }
        });
        tv_filingReceiptText.setOnClickListener(new SingleClickListener()
        {
            @Override
            public void onSingleClick(View v)
            {
                PhotoDialog.getInstance(mActivity).showDialog(new PhotoDialog.IPhotoListener()
                {
                    @Override
                    public void onPhotoCallback()
                    {
                        PhotoDialog.selectPhoto(mActivity, 1);
                    }

                    @Override
                    public void onCameraCallback()
                    {
                        picFile = FileUtils.createSDFile(FileUtils.getRootPicDirImg(), DateUtils.formatCurrentDateTime() + ".jpg");
                        FileUtils.deleteFile(picFile.getAbsolutePath());
                        Uri picUri = FileUtils.getUriForFile(mActivity, picFile);
                        Logger.error("拍照file=" + picFile);
                        PhotoDialog.takePhoto(mActivity, picUri);
                    }
                });
            }
        });
        tv_filingDate.setOnClickListener(new SingleClickListener()
        {
            @Override
            public void onSingleClick(View v)
            {
                DateDIYDialog.getInstance(mActivity).showDialog(new DateDIYDialog.IResultListener()
                {
                    @Override
                    public void onConfirm(int year, int month, int day)
                    {
                        filingTime = year + "-" + StringUtils.formatInt(month) + "-" + StringUtils.formatInt(day);
                        tv_filingDate.setText(filingTime);
                    }
                });
            }
        });
        rl_cancel.setOnClickListener(new SingleClickListener()
        {
            @Override
            public void onSingleClick(View v)
            {
                //                mPresenter.cancelClaim(claimId);
                requestCallPermissions(new EasyPermissionResult()
                {
                    @Override
                    public void onPermissionsAccess(int requestCode)
                    {
                        super.onPermissionsAccess(requestCode);
                        BaseUtils.startCallPhone(mActivity, AppConstants.SERVER_ONLINE_NO, false);
                    }

                    @Override
                    public void onPermissionsDismiss(int requestCode, @NonNull List<String> permissions)
                    {
                        super.onPermissionsDismiss(requestCode, permissions);
                        showToast("没有权限");
                    }
                });
            }
        });
        btn_confirm.setOnClickListener(new SingleClickListener()
        {
            @Override
            public void onSingleClick(View v)
            {
                if (!isSubmit)
                {
                    isSubmit = true;
                    btn_confirm.setText("提交资料");
                    if (isEvidenceEnabled)
                    {
                        tv_evidenceText.setVisibility(View.VISIBLE);
                    } else
                    {
                        tv_evidenceText.setVisibility(View.GONE);
                    }
                    tv_filingDate.setEnabled(true);
                    tv_filingReceiptText.setVisibility(View.VISIBLE);
                    return;
                }

                if (TextUtils.isEmpty(evidenceUrl))
                {
                    showToast("请点击现场拍照取证");
                    return;
                }
                if (TextUtils.isEmpty(filingTime))
                {
                    showToast("请选择报警或立案时间");
                    return;
                }
                if (TextUtils.isEmpty(filingReceiptPath) && TextUtils.isEmpty(filingReceiptUrl))
                {
                    showToast("请上传报警或立案回执");
                    return;
                }
                btn_confirm.setEnabled(false);
                showLoading("提交中...");
                if (!TextUtils.isEmpty(filingReceiptPath))
                {
                    mPresenter.uploadFile(mActivity, 1, 0, filingReceiptPath);
                } else
                {
                    updateClaim();
                }
            }
        });
    }

    @Override
    protected void fetchData()
    {
        mPresenter.getClaim(0, claimId);
    }

    @Override
    public void onUploadSuccess(UrlEntity resultEntity, int position)
    {
        btn_confirm.setEnabled(true);
        Logger.error("图片类型=" + resultEntity.getPicType() + ",url=" + resultEntity.getUrl());
        filingReceiptUrl = resultEntity.getUrl();
        updateClaim();
    }

    @Override
    public void onGetClaimSuccess(int type, ClaimEntity resultEntity)
    {
        if (resultEntity == null)
        {
            rl_description.setVisibility(View.GONE);
            return;
        }

        try
        {
            phoneLat = resultEntity.getEvidenceLat();
            phoneLng = resultEntity.getEvidenceLon();
            deviceLat = resultEntity.getLat();
            deviceLng = resultEntity.getLon();
            distance = resultEntity.getDistance();

            filingReceiptUrl = resultEntity.getFilingReceipt();
            if (type == 2)
            {
                tv_verify.setEnabled(false);
                if (!TextUtils.isEmpty(filingReceiptPath) || (!TextUtils.isEmpty(filingTime) && !filingTime.equals(resultEntity.getFilingTime())))
                {
                    //                    ImageLoadUtils.load(new File(filingReceiptPath), R.mipmap.default_banner, iv_filingReceipt);
                    btn_confirm.setText("提交资料");
                    isSubmit = true;
                }
            } else
            {
                filingTime = resultEntity.getFilingTime();
                if (!TextUtils.isEmpty(filingTime))
                {
                    tv_filingDate.setText(filingTime);
                }
                if (!TextUtils.isEmpty(filingReceiptUrl))
                {
                    ImageLoadUtils.load(filingReceiptUrl, R.mipmap.default_banner, iv_filingReceipt);
                    tv_filingReceiptText.setTextColor(ResUtils.resToColor(mActivity, R.color.white));
                }
            }

            evidenceUrl = resultEntity.getEvidence();
            if (!TextUtils.isEmpty(evidenceUrl))
            {//已取证
                isEvidenced = true;
                ImageLoadUtils.load(evidenceUrl, R.mipmap.default_banner, iv_evidence);
                tv_evidenceText.setTextColor(ResUtils.resToColor(mActivity, R.color.white));
            } else
            {
                isEvidenced = false;
            }
            evidenceTime = resultEntity.getEvidenceTime();
            if (!TextUtils.isEmpty(evidenceTime))
            {
                tv_evidenceTime.setText("(拍摄时间:" + evidenceTime + ")");
            }

            if (resultEntity.getStatus() == ClaimEntity.TYPE_WAITEVIDENCE)
            {//待取证(含过期状态)
                if (!TextUtils.isEmpty(resultEntity.getMessage()))
                {
                    rl_description.setVisibility(View.VISIBLE);
                    tv_description.setText(resultEntity.getMessage());
                } else
                {
                    rl_description.setVisibility(View.GONE);
                }
                tv_verify.setEnabled(false);
                if (type > 0) return;
                if (!TextUtils.isEmpty(resultEntity.getClaimStartTime()) && !TextUtils.isEmpty(resultEntity.getClaimCurrentTime()))
                {//倒计时
                    long lastTime = DateUtils.dateCalculate(resultEntity.getClaimStartTime(), resultEntity.getClaimCurrentTime());
                    long subTime = 24 * 60 * 60 * 1000 - lastTime;//86400000
                    if (subTime > 0)
                    {
                        if (isEvidenced)
                        {
                            tv_evidenceText.setText("点击可重拍");
                        }
                        isEvidenceEnabled = true;
                        countDownTimer = new CountDownTimer(subTime, 1000)
                        {
                            @Override
                            public void onTick(long millisUntilFinished)
                            {
                                if (isEvidenced) return;
                                String formatMiss = DateUtils.formatSecond(millisUntilFinished / 1000);
                                if (!TextUtils.isEmpty(formatMiss))
                                {
                                    String[] time = formatMiss.split(":");
                                    if (time != null && time.length > 2)
                                        updateEvidenceTime(time[0], time[1], time[2]);
                                }
                            }

                            @Override
                            public void onFinish()
                            {
                                showToast("已过期");
                                setExpiredEnable();
                            }
                        };
                        countDownTimer.start();
                    } else
                    {
                        setExpiredEnable();
                    }
                } else
                {
                    setExpiredEnable();
                }
            } else
            {
                rl_description.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(resultEntity.getMessage()))
                {
                    tv_description.setText(resultEntity.getMessage());
                }

                if (type == 2)
                {
                    tv_verify.setEnabled(false);
                    tv_filingDate.setEnabled(true);
                    tv_evidenceText.setVisibility(View.VISIBLE);
                    tv_filingReceiptText.setVisibility(View.VISIBLE);
                    if (!TextUtils.isEmpty(filingReceiptPath) || (!TextUtils.isEmpty(filingTime) && !filingTime.equals(resultEntity.getFilingTime())))
                    {
                        btn_confirm.setText("提交资料");
                        isSubmit = true;
                    }
                } else
                {
                    tv_verify.setEnabled(true);
                    tv_filingDate.setEnabled(false);
                    tv_evidenceText.setVisibility(View.GONE);
                    tv_filingReceiptText.setVisibility(View.GONE);
                    btn_confirm.setText("修改资料");
                    isSubmit = false;
                }
                if (resultEntity.getStatus() == ClaimEntity.TYPE_WAITAUDIT)
                {//审核中
                    tv_verify.setEnabled(true);
                    if (!TextUtils.isEmpty(resultEntity.getClaimStartTime()) && !TextUtils.isEmpty(resultEntity.getClaimCurrentTime()))
                    {//24小时内可修改全部资料，24小时后不能再修改取证资料，只能修改报警资料
                        if (countDownTimer != null) countDownTimer.cancel();
                        long lastTime = DateUtils.dateCalculate(resultEntity.getClaimStartTime(), resultEntity.getClaimCurrentTime());
                        long subTime = 24 * 60 * 60 * 1000 - lastTime;//86400000
                        if (subTime > 0)
                        {
                            isEvidenceEnabled = true;
                            countDownTimer = new CountDownTimer(subTime, 1000)
                            {
                                @Override
                                public void onTick(long millisUntilFinished)
                                {
                                }

                                @Override
                                public void onFinish()
                                {
                                    setWaitAuditEnable();
                                }
                            };
                            countDownTimer.start();
                        } else
                        {
                            setWaitAuditEnable();
                        }
                    } else
                    {
                        setWaitAuditEnable();
                    }
                } else if (resultEntity.getStatus() == ClaimEntity.TYPE_AUDITNOTPASS)
                {//审核未过

                } else if (resultEntity.getStatus() == ClaimEntity.TYPE_ALREADYCANCEL)
                {//已取消（未使用）
                    tv_verify.setEnabled(true);
                    setExpiredEnable();
                    rl_description.setVisibility(View.GONE);
                }
            }
        } catch (Exception e)
        {
            Logger.error("数据处理出错= " + e);
        }
    }

    private void setWaitAuditEnable()
    {
        isEvidenceEnabled = false;
        tv_evidenceText.setVisibility(View.GONE);
    }

    private void setExpiredEnable()
    {
        isEvidenceEnabled = false;
        tv_filingDate.setEnabled(isEvidenced);
        tv_filingReceiptText.setVisibility(isEvidenced ? View.VISIBLE : View.GONE);
        btn_confirm.setEnabled(isEvidenced);
        tv_evidenceText.setVisibility(View.GONE);

        if (!isEvidenced)
        {
            tv_evidenceTime.setVisibility(View.GONE); //取证已过期
            rl_description.setVisibility(View.VISIBLE);
            tv_description.setText(getString(R.string.claim_forensics_expired));
        }
    }

    @Override
    public void onSaveClaimSuccess(ClaimEntity resultEntity)
    {

    }

    @Override
    public void onUpdateClaimSuccess(Object resultEntity)
    {
        filingReceiptPath = "";
        btn_confirm.setEnabled(true);
        if (countDownTimer != null) countDownTimer.cancel();
        cancelLoading();
        mPresenter.getClaim(1, claimId);
    }

    @Override
    public void onCancelClaimSuccess(Object resultEntity)
    {
        finish();
    }

    @Override
    public void onFailure(int errType, String errMsg)
    {
        btn_confirm.setEnabled(true);
        cancelLoading();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode)
        {
            case RESULT_OK:
                if (requestCode == PhotoDialog.REQ_CODE_GALLERY)
                {// 从相册返回
                    List<String> mImagePaths = data.getStringArrayListExtra(ImagePicker.EXTRA_SELECT_IMAGES);
                    if (mImagePaths != null && mImagePaths.size() != 0)
                    {
                        String path = mImagePaths.get(0);
                        Logger.error("图库图片路径= " + path);
                        LuBanUtils.load(mActivity, path, new OnCompressListener()
                        {
                            @Override
                            public void onStart()
                            {

                            }

                            @Override
                            public void onSuccess(File file)
                            {
                                if (file == null) return;
                                filingReceiptPath = file.getAbsolutePath();
                                Logger.error(" 压缩后文件路径= " + filingReceiptPath);
                                ImageLoadUtils.load(file, R.mipmap.default_friend, R.mipmap.default_friend, iv_filingReceipt);
                                tv_filingReceiptText.setTextColor(ResUtils.resToColor(mActivity, R.color.white));
                                tv_filingReceiptText.setText("点击可重拍");
                            }

                            @Override
                            public void onError(Throwable e)
                            {

                            }
                        });
                    }
                } else if (requestCode == PhotoDialog.REQ_CODE_CAMERA)
                {// 从相机返回,从设置相机图片的输出路径中提取数据
                    if (picFile == null) return;
                    Logger.error("拍照图片路径= " + picFile.getAbsolutePath());
                    LuBanUtils.load(mActivity, picFile, new OnCompressListener()
                    {
                        @Override
                        public void onStart()
                        {

                        }

                        @Override
                        public void onSuccess(File file)
                        {
                            filingReceiptPath = file.getAbsolutePath();
                            Logger.error(" 压缩后文件路径= " + filingReceiptPath);
                            ImageLoadUtils.load(file, R.mipmap.default_friend, iv_filingReceipt);
                            tv_filingReceiptText.setTextColor(ResUtils.resToColor(mActivity, R.color.white));
                            tv_filingReceiptText.setText("点击可重拍");
                        }

                        @Override
                        public void onError(Throwable e)
                        {

                        }
                    });
                }
                break;
            case AppConstants.RESULT_CODE_QUERY:
                if (data == null) return;
                mPresenter.getClaim(2, claimId);
                evidenceTime = data.getStringExtra("evidenceTime");
                String evidencePath = data.getStringExtra("evidencePath");
                Logger.error("接收压缩的拍照图片路径= " + evidencePath);
                ImageLoadUtils.load(new File(evidencePath), R.mipmap.default_friend, R.mipmap.default_friend, iv_evidence);
                tv_evidenceText.setTextColor(ResUtils.resToColor(mActivity, R.color.white));
                tv_evidenceText.setText("点击可重拍");
                tv_evidenceTime.setText("(拍摄时间:" + evidenceTime + ")");
                if (countDownTimer != null) countDownTimer.cancel();
                break;
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (countDownTimer != null) countDownTimer.cancel();
    }

    private void updateClaim()
    {
        Map<String, Object> bodyParams = new HashMap<>();
        bodyParams.put("id", claimId);//理赔id
        if (!TextUtils.isEmpty(evidenceUrl))
        {
            bodyParams.put("evidence", evidenceUrl);//取证地址
            bodyParams.put("evidenceTime", evidenceTime);
            bodyParams.put("lat", deviceLat);//设备经纬度
            bodyParams.put("lon", deviceLng);//
            bodyParams.put("evidenceLat", phoneLat);//手机经纬度
            bodyParams.put("evidenceLon", phoneLng);//
            bodyParams.put("distance", distance);//距离
        }
        if (!TextUtils.isEmpty(filingReceiptUrl))
        {
            bodyParams.put("filingReceipt", filingReceiptUrl);//立案回执
        }
        if (!TextUtils.isEmpty(filingTime))
        {
            bodyParams.put("filingTime", filingTime);//立案时间
        }
        bodyParams.put("commitType", !TextUtils.isEmpty(filingReceiptUrl) && !TextUtils.isEmpty(filingTime) && !TextUtils.isEmpty(evidenceUrl) ? 1 : 2);//1正式提交 2草稿提交
        mPresenter.updateClaim(bodyParams);
    }

    /**
     * 倒计时
     *
     * @param hour
     * @param minute
     * @param second
     */
    private void updateEvidenceTime(String hour, String minute, String second)
    {
        int orangeColor = ResUtils.resToColor(mActivity, R.color.orange_text);
        int text6Color = ResUtils.resToColor(mActivity, R.color.text_6);
        SpannableStringBuilder ssb = new SpannableStringBuilder(String.format("请在%s小时%s分%s秒内完成拍照", hour, minute, second));
        ssb.setSpan(new ForegroundColorSpan(text6Color), 0, 2, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        //        时
        int hourLength = 2 + hour.length();
        ssb.setSpan(new ForegroundColorSpan(orangeColor), 2, hourLength, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        ssb.setSpan(new ForegroundColorSpan(text6Color), hourLength, hourLength + 2, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        //       分
        int minuteLength = hourLength + minute.length() + 2;
        ssb.setSpan(new ForegroundColorSpan(orangeColor), hourLength + 2, minuteLength, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        ssb.setSpan(new ForegroundColorSpan(text6Color), minuteLength, minuteLength + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        //        秒
        int secondLength = minuteLength + second.length() + 1;
        ssb.setSpan(new ForegroundColorSpan(orangeColor), minuteLength + 1, secondLength, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        ssb.setSpan(new ForegroundColorSpan(text6Color), secondLength, secondLength + 6, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        tv_evidenceTime.setText(ssb);
    }
}
