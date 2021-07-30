package com.luhong.locwithlibrary.ui.insurance;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.gson.reflect.TypeToken;
import com.luhong.locwithlibrary.R;
import com.luhong.locwithlibrary.R2;
import com.luhong.locwithlibrary.api.AppConstants;
import com.luhong.locwithlibrary.app.BaseConstants;
import com.luhong.locwithlibrary.app.BaseVariable;
import com.luhong.locwithlibrary.base.BaseMvpActivity;
import com.luhong.locwithlibrary.contract.SafeguardEditContract;
import com.luhong.locwithlibrary.dialog.DateDIYDialog;
import com.luhong.locwithlibrary.dialog.DeviceManageDialog;
import com.luhong.locwithlibrary.dialog.PhotoDialog;
import com.luhong.locwithlibrary.dialog.SelectMultipleDialog;
import com.luhong.locwithlibrary.dialog.TypeOfCertificateDialog;
import com.luhong.locwithlibrary.dialog.VehicleDialog;
import com.luhong.locwithlibrary.entity.OptionsMultipleEntity;
import com.luhong.locwithlibrary.entity.PicEntity;
import com.luhong.locwithlibrary.entity.SafeguardEntity;
import com.luhong.locwithlibrary.entity.UrlEntity;
import com.luhong.locwithlibrary.entity.UserEntity;
import com.luhong.locwithlibrary.imagepicker.ImagePicker;
import com.luhong.locwithlibrary.listener.SingleClickListener;
import com.luhong.locwithlibrary.net.response.BasePageEntity;
import com.luhong.locwithlibrary.presenter.SafeguardEditPresenter;
import com.luhong.locwithlibrary.ui.CaptureActivity;
import com.luhong.locwithlibrary.utils.AppUtils;
import com.luhong.locwithlibrary.utils.DateUtils;
import com.luhong.locwithlibrary.utils.FileUtils;
import com.luhong.locwithlibrary.utils.ImageLoadUtils;
import com.luhong.locwithlibrary.utils.Logger;
import com.luhong.locwithlibrary.utils.LuBanUtils;
import com.luhong.locwithlibrary.utils.ResUtils;
import com.luhong.locwithlibrary.utils.SPUtils;
import com.luhong.locwithlibrary.utils.StringUtils;
import com.luhong.locwithlibrary.view.ClearEditText;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import top.zibin.luban.OnCompressListener;

public class SafeguardEditActivity extends BaseMvpActivity<SafeguardEditPresenter> implements SafeguardEditContract.View {
    private final int REQUEST_SCAN_CODE = 0x01;
    public static final int RESULT_CODE = 11010;
    public static final String dataTypeKey = "dataTypeKey";
    public static final String safeguardIdKey = "safeguardIdKey";
    public static final String insureSetIdKey = "insureSetIdKey";
    public static final int dataType_edit = 0;
    public static final int dataType_info = 1;
    private int dataType = dataType_edit;
    private boolean isEdit = true;
    private int status;
    @BindView(R2.id.rl_prompt_safeguardEdit)
    RelativeLayout rl_prompt;
    @BindView(R2.id.tv_prompt_safeguardEdit)
    TextView tv_prompt;
    @BindView(R2.id.tv_edit_safeguardSchedule)
    TextView tv_edit;
    @BindView(R2.id.tv_valuation_safeguardSchedule)
    TextView tv_valuation;
    @BindView(R2.id.tv_pay_safeguardSchedule)
    TextView tv_pay;
    @BindView(R2.id.et_name_safeguardEdit)
    ClearEditText et_name;
    @BindView(R2.id.tv_area_safeguardEdit)
    TextView tv_area;
    @BindView(R2.id.et_address_safeguardEdit)
    ClearEditText et_address;
    @BindView(R2.id.et_idCard_safeguardEdit)
    ClearEditText et_idCard;
    @BindView(R2.id.iv_idCardFront_safeguardEdit)
    ImageView iv_idCardFront;
    @BindView(R2.id.tv_idCardFront_safeguardEdit)
    TextView tv_idCardFront;
    @BindView(R2.id.iv_idCardBack_safeguardEdit)
    ImageView iv_idCardBack;
    @BindView(R2.id.tv_idCardBack_safeguardEdit)
    TextView tv_idCardBack;
    @BindView(R2.id.tv_device_safeguardEdit)
    TextView tv_device;
    @BindView(R2.id.ll_bicycleInfo_safeguardEdit)
    LinearLayout ll_bicycleInfo;
    @BindView(R2.id.tv_brand_safeguardEdit)
    TextView tv_brand;
    @BindView(R2.id.tv_bicycleType_safeguardEdit)
    TextView tv_bicycleType;
    @BindView(R2.id.tv_bicycleModel_safeguardEdit)
    TextView tv_bicycleModel;
    @BindView(R2.id.tv_fivePass_safeguardEdit)
    TextView tv_fivePass;
    @BindView(R2.id.et_devicePrice_safeguardEdit)
    ClearEditText et_devicePrice;
    @BindView(R2.id.tv_devicePrice_safeguardEdit)
    TextView tv_devicePrice;
    @BindView(R2.id.tv_deviceDate_safeguardEdit)
    TextView tv_deviceDate;
    @BindView(R2.id.iv_voucher_safeguardEdit)
    ImageView iv_voucher;
    @BindView(R2.id.tv_voucher_safeguardEdit)
    TextView tv_voucher;
    @BindView(R2.id.iv_overall_safeguardEdit)
    ImageView iv_overall;
    @BindView(R2.id.tv_overall_safeguardEdit)
    TextView tv_overall;
    @BindView(R2.id.iv_overallSide_safeguardEdit)
    ImageView iv_overallSide;
    @BindView(R2.id.tv_overallSide_safeguardEdit)
    TextView tv_overallSide;
    @BindView(R2.id.iv_frameSide_safeguardEdit)
    ImageView iv_frameSide;
    @BindView(R2.id.tv_frameSide_safeguardEdit)
    TextView tv_frameSide;
    @BindView(R2.id.iv_fivePass_safeguardEdit)
    ImageView iv_fivePass;
    @BindView(R2.id.tv_fivePassText_safeguardEdit)
    TextView tv_fivePassText;
    @BindView(R2.id.et_fivePass_safeguardEdit)
    ClearEditText et_fivePass;
    @BindView(R2.id.btn_confirm_safeguardEdit)
    Button btn_confirm;

    @BindView(R2.id.scan_wutong_btn)
    ImageView scan_wutong_btn;

    @BindView(R2.id.tv_cardType)
    TextView tv_cardType;

    private final int frontType = 0;
    private final int backType = 1;
    private final int voucherType = 2;
    private final int overallType = 3;
    private final int overallSideType = 4;
    private final int frameSideType = 5;
    private final int fivePassType = 6;
    private int photoType;
    private String frontPath, backPath, voucherPath, overallPath, overallSidePath, frameSidePath, fivePassPath;
    private String frontUrl, backUrl, voucherUrl, overallUrl, overallSideUrl, frameSideUrl, fivePassUrl;
    private List<PicEntity> picList = new ArrayList<>();//图片列表数据
    private Uri picUri;
    private File picFile;
    private String realName, provice, city, area, address, cardType, idCardNo, deviceName, devicePrice, deviceDate, fivePass;
    private String insureSetId = "1", safeguardId, vehicleId;
    private List<UserEntity.VehicleList> vehicleList = new CopyOnWriteArrayList<>();

    @Override
    protected int initLayoutId() {
        return R.layout.activity_safeguard_edit;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        initTitleView(true, "保障服务详情");
        Bundle bundle = getIntent().getExtras();
        dataType = bundle.getInt(dataTypeKey, dataType_edit);
        insureSetId = bundle.getString(insureSetIdKey, "1");
        if (dataType == dataType_edit) {
            isEdit = true;
            updateEditStatus(true);
            btn_confirm.setVisibility(View.VISIBLE);
            tv_valuation.setEnabled(false);
        } else if (dataType == dataType_info) {
            isEdit = false;
            safeguardId = bundle.getString(safeguardIdKey);
            updateEditStatus(false);
            btn_confirm.setVisibility(View.GONE);
            tv_valuation.setEnabled(true);
        }
        tv_edit.setEnabled(true);
        tv_pay.setEnabled(false);
        scan_wutong_btn.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                startIntentActivityForResult(CaptureActivity.class, REQUEST_SCAN_CODE);
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void fetchData() {
        mPresenter.getSafeguardMine();
        if (dataType == dataType_info) {
            Map<String, Object> bodyParams = new HashMap<>();
            bodyParams.put("id", safeguardId);
            mPresenter.getSafeguardInfo(bodyParams);
        }
    }

    @Override
    public void onSafeguardMineSuccess(BasePageEntity<SafeguardEntity> pageList) {
        UserEntity userEntity = SPUtils.getObject(mActivity,
                BaseConstants.LOGIN_KEY,
                new TypeToken<UserEntity>() {
                }.getType());
        if (userEntity == null || userEntity.getVehicles() == null || userEntity.getVehicles().size() == 0)
            return;
        vehicleList.addAll(userEntity.getVehicles());

        if (pageList != null && pageList.getRecords() != null && pageList.getRecords().size() > 0) {
            for (UserEntity.VehicleList vehicle : vehicleList) {
                if (vehicle == null) continue;
                for (SafeguardEntity safeguardEntity : pageList.getRecords()) {
                    if (safeguardEntity != null && !TextUtils.isEmpty(safeguardEntity.getMyVehicleId()) && (safeguardEntity.getStatus() != SafeguardEntity.TYPE_EXPIRED && safeguardEntity.getStatus() != SafeguardEntity.TYPE_CLAIMED)) {//保障中。保单处于初审中、待终审、审核未过、待支付、已保障或者过期未支付中，不能继续新增？？
                        if (vehicle.getId().equals(safeguardEntity.getMyVehicleId())) {
                            vehicleList.remove(vehicle);
                        }
                    }
                }
            }
        }
        SPUtils.putObject(mActivity, AppConstants.safeguardVehicleKey + BaseVariable.phone, vehicleList);
    }

    @Override
    public void onGetSafeguardInfoSuccess(SafeguardEntity resultEntity) {
        //1待初审,2待终审,3审核未过,4待支付,5已保障,6已过期,7已理赔,8过期未支付
        rl_prompt.setVisibility(View.VISIBLE);
        tv_prompt.setText(resultEntity.getMessage());
        status = resultEntity.getStatus();
        if (status == SafeguardEntity.TYPE_FIRST_TRIAL) {
            updateEditStatus(false);
            btn_confirm.setVisibility(View.VISIBLE);
        } else if (status == SafeguardEntity.TYPE_FINAL_TRIAL) {
            btn_confirm.setVisibility(View.GONE);
        } else if (status == SafeguardEntity.TYPE_FAILED) {
            updateEditStatus(false);
            btn_confirm.setVisibility(View.VISIBLE);
        } else if (status == SafeguardEntity.TYPE_PAY) {
            updateEditStatus(false);
            btn_confirm.setVisibility(View.VISIBLE);
        } else if (status == SafeguardEntity.TYPE_OVERDUE) {
            updateEditStatus(false);
            btn_confirm.setVisibility(View.VISIBLE);
        }
        realName = resultEntity.getRealName();
        et_name.setText(realName);
        et_name.setClearIconVisible(false);
        provice = resultEntity.getProvice();
        city = resultEntity.getCity();
        area = resultEntity.getArea();
        tv_area.setText(provice + city + area);
        address = resultEntity.getAddress();
        et_address.setText(address);
        et_address.setClearIconVisible(false);
        idCardNo = resultEntity.getIdCard();
        et_idCard.setText(idCardNo);
        et_idCard.setClearIconVisible(false);

        cardType = resultEntity.getCardType();
        String cardTypeStr = "";

        switch (cardType + "") {
            case "01":
                cardTypeStr = "大陆身份证";
                break;
            case "02":
                cardTypeStr = "港澳台身份证";
                break;
            case "03":
                cardTypeStr = "护照";
                break;
        }

        tv_cardType.setText(cardTypeStr);
        frontUrl = resultEntity.getCardFront();
        ImageLoadUtils.load(frontUrl, 0, iv_idCardFront);
        backUrl = resultEntity.getCardBack();
        ImageLoadUtils.load(backUrl, iv_idCardBack);
        vehicleId = resultEntity.getMyVehicleId();
        tv_device.setText(resultEntity.getNickName());
        tv_brand.setText(resultEntity.getVehicleBrand());
        tv_bicycleType.setText(resultEntity.getVehicleType());
        tv_bicycleModel.setText(resultEntity.getVehicleModel());
        tv_fivePass.setText(resultEntity.getVin());
        devicePrice = "" + resultEntity.getPrice();
        et_devicePrice.setText(devicePrice);
        tv_devicePrice.setText("¥" + resultEntity.getPrice());
        et_devicePrice.setClearIconVisible(false);
        deviceDate = resultEntity.getBuyDate();
        tv_deviceDate.setText(deviceDate);
        voucherUrl = resultEntity.getVehiclePurchase();
        overallUrl = resultEntity.getVehicleWhole();
        overallSideUrl = resultEntity.getVehicleSide();
        frameSideUrl = resultEntity.getVinSide();
        fivePassUrl = resultEntity.getVinNumber();
        ImageLoadUtils.load(voucherUrl, R.mipmap.safeguard_schedule_voucher, R.mipmap.safeguard_schedule_voucher, iv_voucher);
        ImageLoadUtils.load(overallUrl, R.mipmap.safeguard_schedule_overall, R.mipmap.safeguard_schedule_overall, iv_overall);
        ImageLoadUtils.load(overallSideUrl, R.mipmap.safeguard_schedule_overall_side, R.mipmap.safeguard_schedule_overall_side, iv_overallSide);
        ImageLoadUtils.load(frameSideUrl, R.mipmap.safeguard_schedule_frame_side, R.mipmap.safeguard_schedule_frame_side, iv_frameSide);
        ImageLoadUtils.load(fivePassUrl, R.mipmap.safeguard_schedule_five, R.mipmap.safeguard_schedule_five, iv_fivePass);
        fivePass = resultEntity.getVin();
        et_fivePass.setText(fivePass);
        et_fivePass.setClearIconVisible(false);
    }

    @Override
    public void onSaveSafeguardSuccess(Object resultEntity) {
        cancelLoading();
        Intent intent = new Intent();
        setResult(RESULT_CODE, intent);
        finish();
    }

    @Override
    public void onUpdateSafeguardSuccess(Object resultEntity) {
        cancelLoading();
        Intent intent = new Intent();
        setResult(RESULT_CODE, intent);
        finish();
    }

    @Override
    public void onUploadSuccess(int dataType, UrlEntity resultEntity) {
        picList.clear();
        if (dataType == frontType) {//未优化
            frontUrl = resultEntity.getUrl();
            if (!TextUtils.isEmpty(backPath)) {
                picList.add(new PicEntity(backPath, PicEntity.TYPE_LOCAL));
                mPresenter.uploadFile(mActivity, backType, picList.get(0).getPicUrl());
                return;
            }
            if (!TextUtils.isEmpty(voucherPath)) {
                picList.add(new PicEntity(voucherPath, PicEntity.TYPE_LOCAL));
                mPresenter.uploadFile(mActivity, voucherType, picList.get(0).getPicUrl());
                return;
            }
            if (!TextUtils.isEmpty(overallPath)) {
                picList.add(new PicEntity(overallPath, PicEntity.TYPE_LOCAL));
                mPresenter.uploadFile(mActivity, overallType, picList.get(0).getPicUrl());
                return;
            }
            if (!TextUtils.isEmpty(overallSidePath)) {
                picList.add(new PicEntity(overallSidePath, PicEntity.TYPE_LOCAL));
                mPresenter.uploadFile(mActivity, overallSideType, picList.get(0).getPicUrl());
                return;
            }
            if (!TextUtils.isEmpty(frameSidePath)) {
                picList.add(new PicEntity(frameSidePath, PicEntity.TYPE_LOCAL));
                mPresenter.uploadFile(mActivity, frameSideType, picList.get(0).getPicUrl());
                return;
            }
            if (!TextUtils.isEmpty(fivePassPath)) {
                picList.add(new PicEntity(fivePassPath, PicEntity.TYPE_LOCAL));
                mPresenter.uploadFile(mActivity, fivePassType, picList.get(0).getPicUrl());
                return;
            }
        } else if (dataType == backType) {
            backUrl = resultEntity.getUrl();
            if (!TextUtils.isEmpty(voucherPath)) {
                picList.add(new PicEntity(voucherPath, PicEntity.TYPE_LOCAL));
                mPresenter.uploadFile(mActivity, voucherType, picList.get(0).getPicUrl());
                return;
            }
            if (!TextUtils.isEmpty(overallPath)) {
                picList.add(new PicEntity(overallPath, PicEntity.TYPE_LOCAL));
                mPresenter.uploadFile(mActivity, overallType, picList.get(0).getPicUrl());
                return;
            }
            if (!TextUtils.isEmpty(overallSidePath)) {
                picList.add(new PicEntity(overallSidePath, PicEntity.TYPE_LOCAL));
                mPresenter.uploadFile(mActivity, overallSideType, picList.get(0).getPicUrl());
                return;
            }
            if (!TextUtils.isEmpty(frameSidePath)) {
                picList.add(new PicEntity(frameSidePath, PicEntity.TYPE_LOCAL));
                mPresenter.uploadFile(mActivity, frameSideType, picList.get(0).getPicUrl());
                return;
            }
            if (!TextUtils.isEmpty(fivePassPath)) {
                picList.add(new PicEntity(fivePassPath, PicEntity.TYPE_LOCAL));
                mPresenter.uploadFile(mActivity, fivePassType, picList.get(0).getPicUrl());
                return;
            }
        } else if (dataType == voucherType) {
            voucherUrl = resultEntity.getUrl();
            if (!TextUtils.isEmpty(overallPath)) {
                picList.add(new PicEntity(overallPath, PicEntity.TYPE_LOCAL));
                mPresenter.uploadFile(mActivity, overallType, picList.get(0).getPicUrl());
                return;
            }
            if (!TextUtils.isEmpty(overallSidePath)) {
                picList.add(new PicEntity(overallSidePath, PicEntity.TYPE_LOCAL));
                mPresenter.uploadFile(mActivity, overallSideType, picList.get(0).getPicUrl());
                return;
            }
            if (!TextUtils.isEmpty(frameSidePath)) {
                picList.add(new PicEntity(frameSidePath, PicEntity.TYPE_LOCAL));
                mPresenter.uploadFile(mActivity, frameSideType, picList.get(0).getPicUrl());
                return;
            }
            if (!TextUtils.isEmpty(fivePassPath)) {
                picList.add(new PicEntity(fivePassPath, PicEntity.TYPE_LOCAL));
                mPresenter.uploadFile(mActivity, fivePassType, picList.get(0).getPicUrl());
                return;
            }
        } else if (dataType == overallType) {
            overallUrl = resultEntity.getUrl();
            if (!TextUtils.isEmpty(overallSidePath)) {
                picList.add(new PicEntity(overallSidePath, PicEntity.TYPE_LOCAL));
                mPresenter.uploadFile(mActivity, overallSideType, picList.get(0).getPicUrl());
                return;
            }
            if (!TextUtils.isEmpty(frameSidePath)) {
                picList.add(new PicEntity(frameSidePath, PicEntity.TYPE_LOCAL));
                mPresenter.uploadFile(mActivity, frameSideType, picList.get(0).getPicUrl());
                return;
            }
            if (!TextUtils.isEmpty(fivePassPath)) {
                picList.add(new PicEntity(fivePassPath, PicEntity.TYPE_LOCAL));
                mPresenter.uploadFile(mActivity, fivePassType, picList.get(0).getPicUrl());
                return;
            }
        } else if (dataType == overallSideType) {
            overallSideUrl = resultEntity.getUrl();
            if (!TextUtils.isEmpty(frameSidePath)) {
                picList.add(new PicEntity(frameSidePath, PicEntity.TYPE_LOCAL));
                mPresenter.uploadFile(mActivity, frameSideType, picList.get(0).getPicUrl());
                return;
            }
            if (!TextUtils.isEmpty(fivePassPath)) {
                picList.add(new PicEntity(fivePassPath, PicEntity.TYPE_LOCAL));
                mPresenter.uploadFile(mActivity, fivePassType, picList.get(0).getPicUrl());
                return;
            }
        } else if (dataType == frameSideType) {
            frameSideUrl = resultEntity.getUrl();
            if (!TextUtils.isEmpty(fivePassPath)) {
                picList.add(new PicEntity(fivePassPath, PicEntity.TYPE_LOCAL));
                mPresenter.uploadFile(mActivity, fivePassType, picList.get(0).getPicUrl());
                return;
            }
        } else if (dataType == fivePassType) {
            fivePassUrl = resultEntity.getUrl();
        }
        saveSafeguard();
    }

    private void saveSafeguard() {
        Map<String, Object> bodyParams = new HashMap<>();
        bodyParams.put("insureSetId", insureSetId); //所属保险设置主键,目前只有一种默认传1
        bodyParams.put("realName", realName);
        bodyParams.put("provice", provice);
        bodyParams.put("city", city);
        bodyParams.put("area", area);
        bodyParams.put("address", address);
        bodyParams.put("idCard", idCardNo);
        bodyParams.put("cardType", cardType);
        bodyParams.put("cardBack", backUrl);
        bodyParams.put("cardFront", frontUrl);
        bodyParams.put("myVehicleId", vehicleId);
        bodyParams.put("price", devicePrice);
        bodyParams.put("buyDate", deviceDate);
        bodyParams.put("vehiclePurchase", voucherUrl);
        bodyParams.put("vehicleWhole", overallUrl);
        bodyParams.put("vehicleSide", overallSideUrl);
        bodyParams.put("vinSide", frameSideUrl);
        bodyParams.put("vinNumber", fivePassUrl);
        bodyParams.put("vin", fivePass);
        if (dataType == dataType_info) {
            bodyParams.put("id", safeguardId);
            bodyParams.put("status", status);
            mPresenter.updateSafeguard(bodyParams);
        } else {
            mPresenter.saveSafeguard(bodyParams);
        }
    }

    @Override
    public void onFailure(int errType, String errMsg) {
        cancelLoading();
        if (errType == 1) {
            List<UserEntity.VehicleList> vehicles = SPUtils.getObject(mActivity,
                    AppConstants.safeguardVehicleKey + BaseVariable.phone,
                    new TypeToken<List<UserEntity.VehicleList>>() {
                    }.getType());
            if (vehicles != null && vehicles.size() != 0) {
                vehicleList.addAll(vehicles);
            }
        }
    }

    @Override
    protected void onEventListener() {
        tv_device.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                clearFocus();
                if (vehicleList == null || vehicleList.size() == 0) {
                    showToast("没有未购买保险的车辆");
                    return;
                }
                VehicleDialog.getInstance(mActivity).showDialog(new VehicleDialog.IEventListener() {
                    @Override
                    public void onSelectCallback(UserEntity.VehicleList device) {
                        tv_device.setText(device.getNickName());
                        vehicleId = device.getId();
                    }
                });
            }
        });
        // 选择证件类型
        tv_cardType.setOnClickListener(v -> TypeOfCertificateDialog.getInstance(mActivity).showDialog(data ->
        {
            tv_cardType.setText(data);
        }));

        et_devicePrice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                Logger.error("获取焦点情况=" + hasFocus);
                if (!hasFocus) {
                    closeKeyboard();
                }
            }
        });
        tv_deviceDate.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                clearFocus();
                DateDIYDialog.getInstance(mActivity).showDialog(new DateDIYDialog.IResultListener() {
                    @Override
                    public void onConfirm(int year, int month, int day) {
                        deviceDate = year + "-" + StringUtils.formatInt(month) + "-" + StringUtils.formatInt(day);
                        tv_deviceDate.setText(deviceDate);
                    }
                });
            }
        });
        tv_idCardFront.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                showPhotoDialog(frontType);
            }
        });
        tv_idCardBack.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                showPhotoDialog(backType);
            }
        });
        tv_overall.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                showPhotoDialog(overallType);
            }
        });
        tv_voucher.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                showPhotoDialog(voucherType);
            }
        });
        tv_overallSide.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                showPhotoDialog(overallSideType);
            }
        });
        iv_frameSide.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                showPhotoDialog(frameSideType);
            }
        });
        tv_fivePassText.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                showPhotoDialog(fivePassType);
            }
        });
        tv_area.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                clearFocus();
                SelectMultipleDialog.getInstance(mActivity).showDialog(new SelectMultipleDialog.IResultListener() {
                    @Override
                    public void onConfirm(@Nullable OptionsMultipleEntity selectOptions1, int position1, @Nullable OptionsMultipleEntity selectOptions2, int position2, @Nullable OptionsMultipleEntity selectOptions3, int position3) {
                        provice = selectOptions1.getName();
                        city = selectOptions2.getName();
                        area = selectOptions3.getName();
                        tv_area.setText(provice + "-" + city + "-" + area);
                    }
                });
            }
        });
        btn_confirm.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (isEdit) {
                    realName = et_name.getText().toString().trim();
                    if (TextUtils.isEmpty(realName)) {
                        showToast(et_name.getHint().toString());
                        return;
                    }
                    if (TextUtils.isEmpty(area)) {
                        showToast("请选择居住区域");
                        return;
                    }
                    address = et_address.getText().toString().trim();
                    if (TextUtils.isEmpty(address)) {
                        showToast("请输入街道门牌号");
                        return;
                    }

                    String cardTypeStr = tv_cardType.getText().toString().trim();
                    switch (cardTypeStr) {
                        case "大陆身份证":
                            cardType = "01";
                            break;
                        case "港澳台身份证":
                            cardType = "02";
                            break;
                        case "护照":
                            cardType = "03";
                            break;
                        default: {
                            cardType = "";
                        }
                        break;
                    }

                    if (TextUtils.isEmpty(cardType)) {
                        showToast("请选择证件类型");
                        return;
                    }

                    idCardNo = et_idCard.getText().toString().trim().toUpperCase(Locale.CHINA);
                    if (TextUtils.isEmpty(idCardNo)) {
                        showToast(et_idCard.getHint().toString());
                        return;
                    }
                    //                    if (!BaseUtils.isIdCardNo(idCardNo)) {
                    //                        showToast("请输入正确的证件号码");
                    //                        return;
                    //                    }
                    if (TextUtils.isEmpty(frontPath) && TextUtils.isEmpty(frontUrl)) {
                        showToast("请" + tv_idCardFront.getText().toString());
                        return;
                    }
                    if (TextUtils.isEmpty(backPath) && TextUtils.isEmpty(backUrl)) {
                        showToast("请" + tv_idCardBack.getText().toString());
                        return;
                    }
                    deviceName = tv_device.getText().toString();
                    if (TextUtils.isEmpty(deviceName)) {
                        showToast("请选择购保车辆");
                        return;
                    }
                    devicePrice = et_devicePrice.getText().toString().trim();
                    if (TextUtils.isEmpty(devicePrice)) {
                        showToast("请输入购车价格");
                        return;
                    }
                    deviceDate = tv_deviceDate.getText().toString().trim();
                    if (TextUtils.isEmpty(deviceDate)) {
                        showToast("请选择购车日期");
                        return;
                    }
                    if (TextUtils.isEmpty(voucherPath) && TextUtils.isEmpty(voucherUrl)) {
                        showToast("请上传购车凭证");
                        return;
                    }
                    if (TextUtils.isEmpty(overallPath) && TextUtils.isEmpty(overallUrl)) {
                        showToast("请上传整体图");
                        return;
                    }
                    if (TextUtils.isEmpty(overallSidePath) && TextUtils.isEmpty(overallSideUrl)) {
                        showToast("请上传整体侧面图");
                        return;
                    }
                    if (TextUtils.isEmpty(frameSidePath) && TextUtils.isEmpty(frameSideUrl)) {
                        showToast("请上传车架侧面图");
                        return;
                    }
                    if (TextUtils.isEmpty(fivePassPath) && TextUtils.isEmpty(fivePassUrl)) {
                        showToast("请上传五通码图");
                        return;
                    }
                    fivePass = et_fivePass.getText().toString().trim();
                    if (TextUtils.isEmpty(fivePass)) {
                        showToast(et_fivePass.getHint().toString());
                        return;
                    }
                    showLoading("资料提交中", false);
                    picList.clear();
                    if (!TextUtils.isEmpty(frontPath)) {
                        picList.add(new PicEntity(frontPath, PicEntity.TYPE_LOCAL));
                        mPresenter.uploadFile(mActivity, frontType, picList.get(0).getPicUrl());
                    } else if (!TextUtils.isEmpty(backPath)) {
                        picList.add(new PicEntity(backPath, PicEntity.TYPE_LOCAL));
                        mPresenter.uploadFile(mActivity, backType, picList.get(0).getPicUrl());
                    } else if (!TextUtils.isEmpty(voucherPath)) {
                        picList.add(new PicEntity(voucherPath, PicEntity.TYPE_LOCAL));
                        mPresenter.uploadFile(mActivity, voucherType, picList.get(0).getPicUrl());
                    } else if (!TextUtils.isEmpty(overallPath)) {
                        picList.add(new PicEntity(overallPath, PicEntity.TYPE_LOCAL));
                        mPresenter.uploadFile(mActivity, overallType, picList.get(0).getPicUrl());
                    } else if (!TextUtils.isEmpty(overallSidePath)) {
                        picList.add(new PicEntity(overallSidePath, PicEntity.TYPE_LOCAL));
                        mPresenter.uploadFile(mActivity, overallSideType, picList.get(0).getPicUrl());
                    } else if (!TextUtils.isEmpty(frameSidePath)) {
                        picList.add(new PicEntity(frameSidePath, PicEntity.TYPE_LOCAL));
                        mPresenter.uploadFile(mActivity, frameSideType, picList.get(0).getPicUrl());
                    } else if (!TextUtils.isEmpty(fivePassPath)) {
                        picList.add(new PicEntity(fivePassPath, PicEntity.TYPE_LOCAL));
                        mPresenter.uploadFile(mActivity, fivePassType, picList.get(0).getPicUrl());
                    } else {
                        saveSafeguard();
                    }
                    //                    updateEditStatus(true);
                } else {
                    if (status == SafeguardEntity.TYPE_FIRST_TRIAL || status == SafeguardEntity.TYPE_FAILED || status == SafeguardEntity.TYPE_PAY || status == SafeguardEntity.TYPE_OVERDUE) {
                        isEdit = true;
                        updateEditStatus(true);
                        tv_idCardFront.setText("点击可修改");
                        tv_idCardBack.setText("点击可修改");
                        tv_voucher.setText("点击可修改");
                        tv_overall.setText("点击可修改");
                        tv_overallSide.setText("点击可修改");
                        tv_frameSide.setText("点击可修改");
                        tv_fivePassText.setText("点击可修改");
                    } else
                        updateEditStatus(false);
                }
            }
        });
    }

    private void updateEditStatus(boolean isEdit) {
        if (isEdit) {
            ll_bicycleInfo.setVisibility(View.GONE);
            tv_valuation.setEnabled(false);
            tv_pay.setEnabled(false);
            et_name.setEnabled(true);
            tv_area.setEnabled(true);
            tv_area.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ResUtils.resToDrawable(mActivity, R.mipmap.arrow_right), null);
            et_address.setEnabled(true);
            et_idCard.setEnabled(true);
            tv_cardType.setEnabled(true);
            scan_wutong_btn.setEnabled(true);
            tv_idCardFront.setVisibility(View.VISIBLE);
            tv_idCardBack.setVisibility(View.VISIBLE);
            tv_device.setEnabled(true);
            tv_device.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ResUtils.resToDrawable(mActivity, R.mipmap.arrow_right), null);
            et_devicePrice.setVisibility(View.VISIBLE);
            et_devicePrice.setEnabled(true);
            tv_devicePrice.setText("¥");
            tv_deviceDate.setEnabled(true);
            tv_deviceDate.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ResUtils.resToDrawable(mActivity, R.mipmap.arrow_right), null);
            tv_voucher.setVisibility(View.VISIBLE);
            tv_overall.setVisibility(View.VISIBLE);
            tv_overallSide.setVisibility(View.VISIBLE);
            tv_frameSide.setVisibility(View.VISIBLE);
            tv_fivePassText.setVisibility(View.VISIBLE);
            et_fivePass.setEnabled(true);
            btn_confirm.setText("提交资料");
        } else {
            ll_bicycleInfo.setVisibility(View.VISIBLE);
            rl_prompt.setVisibility(View.VISIBLE);
            tv_valuation.setEnabled(true);
            tv_pay.setEnabled(false);
            et_name.setEnabled(false);
            et_name.setGravity(Gravity.END);
            et_name.setClearIconVisible(false);
            tv_area.setEnabled(false);
            tv_area.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
            et_address.setEnabled(false);
            et_address.setGravity(Gravity.END);
            et_address.setClearIconVisible(false);
            et_idCard.setEnabled(false);
            et_idCard.setGravity(Gravity.END);
            et_idCard.setClearIconVisible(false);
            tv_cardType.setEnabled(false);
            scan_wutong_btn.setEnabled(false);
            tv_idCardFront.setVisibility(View.GONE);
            tv_idCardBack.setVisibility(View.GONE);
            tv_device.setEnabled(false);
            tv_device.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
            et_devicePrice.setEnabled(false);
            et_devicePrice.setClearIconVisible(false);
            et_devicePrice.setVisibility(View.INVISIBLE);
            tv_deviceDate.setEnabled(false);
            tv_deviceDate.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
            tv_voucher.setVisibility(View.GONE);
            tv_overall.setVisibility(View.GONE);
            tv_overallSide.setVisibility(View.GONE);
            tv_frameSide.setVisibility(View.GONE);
            tv_fivePassText.setVisibility(View.GONE);
            et_fivePass.setEnabled(false);
            et_fivePass.setClearIconVisible(false);
            btn_confirm.setText("修改资料");
        }
    }

    private void clearFocus() {
        et_name.clearFocus();
        et_address.clearFocus();
        et_idCard.clearFocus();
        et_fivePass.clearFocus();
        et_devicePrice.clearFocus();
        closeKeyboard();
    }

    private void showPhotoDialog(int type) {
        if (AppUtils.checkStorageManagerPermission()) {
            clearFocus();
            this.photoType = type;
            PhotoDialog.getInstance(mActivity).showDialog(new PhotoDialog.IPhotoListener() {
                @Override
                public void onPhotoCallback() {
                    PhotoDialog.selectPhoto(mActivity, 1);
                }

                @Override
                public void onCameraCallback() {
                    picFile = FileUtils.createSDFile(FileUtils.getRootPicDirImg(), DateUtils.formatCurrentDateTime() + ".jpg");
                    FileUtils.deleteFile(picFile.getAbsolutePath());
                    picUri = FileUtils.getUriForFile(mActivity, picFile);
                    Logger.error("拍照file=" + picFile);
                    PhotoDialog.takePhoto(mActivity, picUri);
                }
            });
        }else{
            showDolag();
        }
    }

    private void updatePhotoView(String path) {
        if (photoType == frontType) {
            frontPath = path;
            ImageLoadUtils.load(path, iv_idCardFront);
            tv_idCardFront.setText("点击可修改");
        } else if (photoType == backType) {
            backPath = path;
            ImageLoadUtils.load(path, iv_idCardBack);
            tv_idCardBack.setText("点击可修改");
        } else if (photoType == voucherType) {
            voucherPath = path;
            ImageLoadUtils.load(path, R.mipmap.safeguard_schedule_voucher, R.mipmap.safeguard_schedule_voucher, iv_voucher);
            tv_voucher.setText("点击可修改");
        } else if (photoType == overallType) {
            overallPath = path;
            ImageLoadUtils.load(path, R.mipmap.safeguard_schedule_overall, R.mipmap.safeguard_schedule_overall, iv_overall);
            tv_overall.setText("点击可修改");
        } else if (photoType == overallSideType) {
            overallSidePath = path;
            ImageLoadUtils.load(path, R.mipmap.safeguard_schedule_overall_side, R.mipmap.safeguard_schedule_overall_side, iv_overallSide);
            tv_overallSide.setText("点击可修改");
        } else if (photoType == frameSideType) {
            frameSidePath = path;
            ImageLoadUtils.load(path, R.mipmap.safeguard_schedule_frame_side, R.mipmap.safeguard_schedule_frame_side, iv_frameSide);
            tv_frameSide.setText("点击可修改");
        } else if (photoType == fivePassType) {
            fivePassPath = path;
            ImageLoadUtils.load(path, R.mipmap.safeguard_schedule_five, R.mipmap.safeguard_schedule_five, iv_fivePass);
            tv_fivePassText.setText("点击可修改");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (REQUEST_SCAN_CODE == requestCode && resultCode == CaptureActivity.resultDecode) {
            if (data != null) {
                String code = data.getStringExtra(CaptureActivity.dataTypeKey);
                if (code != null) {
                    et_fivePass.setText(code);
                }
            }
        }

        switch (resultCode) {
            case RESULT_OK:
                if (requestCode == PhotoDialog.REQ_CODE_GALLERY) {// 从相册返回
                    if (data == null) return;
                    List<String> mImagePaths = data.getStringArrayListExtra(ImagePicker.EXTRA_SELECT_IMAGES);
                    if (mImagePaths != null && mImagePaths.size() != 0) {
                        String path = mImagePaths.get(0);
                        Logger.error("图库图片路径= " + path);
                        if (!TextUtils.isEmpty(path)) {
                            LuBanUtils.load(mActivity, path, new OnCompressListener() {
                                @Override
                                public void onStart() {

                                }

                                @Override
                                public void onSuccess(File file) {
                                    Logger.error(" 压缩后文件路径= " + file.getAbsolutePath());
                                    updatePhotoView(file.getAbsolutePath());
                                }

                                @Override
                                public void onError(Throwable e) {

                                }
                            });
                        }
                    }
                } else if (requestCode == PhotoDialog.REQ_CODE_CAMERA) {// 从相机返回,从设置相机图片的输出路径中提取数据
                    if (picFile == null) return;
                    Logger.error("拍照图片路径= " + picFile.getAbsolutePath());
                    LuBanUtils.load(mActivity, picFile, new OnCompressListener() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onSuccess(File file) {
                            Logger.error(" 压缩后文件路径= " + file.getAbsolutePath());
                            updatePhotoView(file.getAbsolutePath());
                        }

                        @Override
                        public void onError(Throwable e) {

                        }
                    });
                }
                break;
        }
    }

    void showDolag() {
        DeviceManageDialog.getInstance(mActivity).showDialog(DeviceManageDialog.DIALOG_JURISDICTION, 0, "", new DeviceManageDialog.IEventListeners() {
            @RequiresApi(api = 30)
            @Override
            public void onConfirm(int type) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(Uri.parse("package:" + mActivity.getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivityForResult(intent, 100);
            }

            @Override
            public void onCancel() {
                mActivity.finish();
            }
        });
    }
}