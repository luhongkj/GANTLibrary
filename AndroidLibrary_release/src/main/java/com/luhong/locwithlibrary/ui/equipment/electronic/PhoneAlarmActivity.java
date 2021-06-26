package com.luhong.locwithlibrary.ui.equipment.electronic;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.androidkun.xtablayout.XTabLayout;
import com.luhong.locwithlibrary.R;
import com.luhong.locwithlibrary.R2;
import com.luhong.locwithlibrary.apadter.TypePageAdapter;
import com.luhong.locwithlibrary.api.AppConstants;
import com.luhong.locwithlibrary.api.AppVariable;
import com.luhong.locwithlibrary.base.BaseFragmentTow;
import com.luhong.locwithlibrary.base.BaseMvpActivity;
import com.luhong.locwithlibrary.contract.PhoneAlarmContract;
import com.luhong.locwithlibrary.dialog.BaseDialog;
import com.luhong.locwithlibrary.dialog.DevicePromptDialog;
import com.luhong.locwithlibrary.entity.DeviceServerEntity;
import com.luhong.locwithlibrary.event.PushEvent;
import com.luhong.locwithlibrary.fragment.AlarmRecordFragment;
import com.luhong.locwithlibrary.fragment.ServiceDescriptionFragment;
import com.luhong.locwithlibrary.listener.SingleClickListener;
import com.luhong.locwithlibrary.presenter.PhoneAlarmPresenter;
import com.luhong.locwithlibrary.ui.equipment.FlowAccountPayActivity;
import com.luhong.locwithlibrary.utils.DateUtils;
import com.luhong.locwithlibrary.utils.Logger;
import com.luhong.locwithlibrary.view.NoScrollViewPager;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class PhoneAlarmActivity extends BaseMvpActivity<PhoneAlarmPresenter> implements PhoneAlarmContract.View {
    @BindView(R2.id.tabLayout_phoneAlarm)
    XTabLayout mTabLayout;
    @BindView(R2.id.viewPager_phoneAlarm)
    NoScrollViewPager mViewPager;
    @BindView(R2.id.btn_confirm_phoneAlarm)
    Button btn_confirm;
    @BindView(R2.id.tv_expiryDate_phoneAlarm)
    TextView tv_expiryDate;

    private List<String> tabTitleList = new ArrayList<>();
    private List<BaseFragmentTow> fragmentList = new ArrayList<>();
    private TypePageAdapter mTypeAdapter;
    private boolean isActivation = true;//true 未激活
    String SN;

    @Override
    protected int initLayoutId() {
        return R.layout.activity_phone_alarm;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        initTitleView(true, "电话告警");
        SN = getIntent().getStringExtra("SN");
        tabTitleList.add("服务说明");
        fragmentList.add(ServiceDescriptionFragment.newInstance(0));
        tabTitleList.add("告警记录");
        fragmentList.add(AlarmRecordFragment.newInstance(0));
        mTypeAdapter = new TypePageAdapter(getSupportFragmentManager(), fragmentList, tabTitleList);
        mViewPager.setAdapter(mTypeAdapter);
        mViewPager.setOffscreenPageLimit(tabTitleList.size() - 1);
        mViewPager.setCurrentItem(0);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.getTabAt(0).select();
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void fetchData() {
        mPresenter.getVehicleBySn(SN);
    }

    @Override
    protected void onEventListener() {
        btn_confirm.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (isActivation) {
                    showToast("设备未激活");
                    return;
                }
                if (TextUtils.isEmpty(SN)) {
                    showToast("未获取到设备SN");
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString("SN", SN);
                startIntentActivityForResult(PhonePayActivity.class, PhonePayActivity.RECHARGE_SUCCESS_CODE, bundle);
            }
        });
        mTabLayout.setOnTabSelectedListener(new XTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(XTabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(XTabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(XTabLayout.Tab tab) {

            }
        });
    }

    long isbalance = 0;

    @Override
    public void onGetVehicleBySnSuccess(DeviceServerEntity deviceServerEntity) {
        cancelLoading();
        if (deviceServerEntity == null) {
            tv_expiryDate.setVisibility(View.INVISIBLE);
            return;
        }
        isActivation = deviceServerEntity.getStatus() == 0 ? true : false;
        if (TextUtils.isEmpty(deviceServerEntity.getTelAlarmDate() + "") || deviceServerEntity.getStatus() == 0) {//未激活
            isActivation = true;
            return;
        } else {
            isActivation = false;
        }
        if (!TextUtils.isEmpty(deviceServerEntity.getTelAlarmDate())) {
            tv_expiryDate.setText("有效期至：" + deviceServerEntity.getTelAlarmDate() + (deviceServerEntity.isTelPass() ? "（已到期请续费）" : ""));
            btn_confirm.setText("立即续费");
            tv_expiryDate.setVisibility(View.VISIBLE);
        }
        try {
            if (!isPay) {
                if (!TextUtils.isEmpty(deviceServerEntity.getTelAlarmDate())) {
                    isbalance = DateUtils.dateToStamp(deviceServerEntity.getTelAlarmDate());
                } else {//首次充值,没有返回时间
                    isbalance = DateUtils.dateToStamp(DateUtils.formatCurrentDateTime(System.currentTimeMillis() + ""));
                }
            } else {
                if (isbalance < DateUtils.dateToStamp(deviceServerEntity.getTelAlarmDate())) {
                    isPay = false;
                    isbalance = DateUtils.dateToStamp(deviceServerEntity.getTelAlarmDate());
                    paySuccess(DevicePromptDialog.TYPE_PAY_PHONE_SUCCESS, deviceServerEntity.getTelAlarmDate());
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(int errType, String errMsg) {
        cancelLoading();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSubscribeEvent(PushEvent eventMessage) {//
        Logger.error("支付成功推送ID=" + eventMessage.getMsgId());
        switch (eventMessage.getMsgId()) {
            case AppConstants.MSG_PHONE://
                //                showToast("支付成功");
                fetchData();
                paySuccess(DevicePromptDialog.TYPE_PAY_PHONE_SUCCESS, eventMessage.getDeadlineDate());
                break;
        }
    }

    private void paySuccess(int type, String content) {
        DevicePromptDialog.getInstance(mActivity).showDialog(type, "", content, new BaseDialog.IEventListener() {
            @Override
            public void onConfirm() {

            }

            @Override
            public void onCancel() {

            }
        });
    }

    boolean isPay;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode ==  PhonePayActivity.RECHARGE_SUCCESS_CODE) {
            showLoading();
            isPay = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    fetchData();
                }
            }, 1000);
        }
    }
}
