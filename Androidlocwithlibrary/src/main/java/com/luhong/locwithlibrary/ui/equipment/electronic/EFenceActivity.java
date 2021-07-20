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
import com.luhong.locwithlibrary.base.BaseFragment;
import com.luhong.locwithlibrary.base.BaseFragmentTow;
import com.luhong.locwithlibrary.base.BaseMvpActivity;
import com.luhong.locwithlibrary.contract.EFenceContract;
import com.luhong.locwithlibrary.dialog.AlarmRadiusDialog;
import com.luhong.locwithlibrary.dialog.BaseDialog;
import com.luhong.locwithlibrary.dialog.DevicePromptDialog;
import com.luhong.locwithlibrary.entity.DeviceServerEntity;
import com.luhong.locwithlibrary.entity.FenceRadiusEntity;
import com.luhong.locwithlibrary.event.PushEvent;
import com.luhong.locwithlibrary.fragment.AlarmRecordFragment;
import com.luhong.locwithlibrary.fragment.ServiceDescriptionFragment;
import com.luhong.locwithlibrary.listener.SingleClickListener;
import com.luhong.locwithlibrary.presenter.EFencePresenter;
import com.luhong.locwithlibrary.ui.equipment.FlowAccountPayActivity;
import com.luhong.locwithlibrary.utils.DateUtils;
import com.luhong.locwithlibrary.utils.Logger;
import com.luhong.locwithlibrary.view.NoScrollViewPager;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class EFenceActivity extends BaseMvpActivity<EFencePresenter> implements EFenceContract.View {
    @BindView(R2.id.tabLayout_eFence)
    XTabLayout mTabLayout;
    @BindView(R2.id.viewPager_eFence)
    NoScrollViewPager mViewPager;
    @BindView(R2.id.btn_confirm_eFence)
    Button btn_confirm;
    @BindView(R2.id.tv_expiryDate_eFence)
    TextView tv_expiryDate;

    private List<String> tabTitleList = new ArrayList<>();
    private List<BaseFragmentTow> fragmentList = new ArrayList<>();
    private TypePageAdapter mTypeAdapter;
    private AlarmRadiusDialog alarmRadiusDialog;
    private String initRadius = "300";
    private boolean isActivation = true;//true 未激活

    @Override
    protected int initLayoutId() {
        return R.layout.activity_efence;
    }

    String sn;

    @Override
    protected void initView(Bundle savedInstanceState) {
        sn = getIntent().getStringExtra("SN");
        initTitleView(true, "电子围栏", "设置", new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                alarmRadiusDialog = new AlarmRadiusDialog(mActivity);
                alarmRadiusDialog.showDialog(initRadius, new AlarmRadiusDialog.IAlarmRadiusListener() {
                    @Override
                    public void onConfirm(String content) {
                        Map<String, Object> bodyParams = new HashMap<>();
                        bodyParams.put("vehicleId", AppVariable.currentVehicleId);
                        bodyParams.put("flowId", "A" + System.currentTimeMillis());
                        bodyParams.put("paramValue", content);//半径
                        bodyParams.put("msgId", AppConstants.MSG_EFENCE_FORTIFICATION);
                        mPresenter.sendCommand(bodyParams);
                    }
                });
            }
        });
        tabTitleList.add("服务说明");
        fragmentList.add(ServiceDescriptionFragment.newInstance(1));
        tabTitleList.add("告警记录");
        fragmentList.add(AlarmRecordFragment.newInstance(1));
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
        mPresenter.getVehicleBySn(sn);
        mPresenter.getFenceRadius();
    }

    @Override
    public void onGetFenceRadiusSuccess(FenceRadiusEntity resultEntity) {
        if (resultEntity == null) return;
        initRadius = resultEntity.getFenceAlarmRadius();
    }

    long isbalance = 0;

    @Override
    public void onGetVehicleBySnSuccess(DeviceServerEntity installModeEntity) {
        cancelLoading();
        if (installModeEntity == null) {
            tv_expiryDate.setVisibility(View.INVISIBLE);
            return;
        }
        isActivation = installModeEntity.getStatus() == 0 ? true : false;
        if (TextUtils.isEmpty(installModeEntity.getFenceAlarmDate() + "") || installModeEntity.getStatus() == 0) {//未激活
            isActivation = true;
            return;
        } else {
            isActivation = false;
        }
        if (!TextUtils.isEmpty(installModeEntity.getFenceAlarmDate())) {
            tv_expiryDate.setText("有效期至：" + installModeEntity.getFenceAlarmDate() + (installModeEntity.isFencePass() ? "（已到期请续费）" : ""));
            btn_confirm.setText("立即续费");
            tv_expiryDate.setVisibility(View.VISIBLE);
        }
        try {
            if (!isPay) {
                if (!TextUtils.isEmpty(installModeEntity.getFenceAlarmDate())) {
                    isbalance = DateUtils.dateToStamp(installModeEntity.getFenceAlarmDate());
                } else {//首次充值,没有返回时间
                    isbalance = DateUtils.dateToStamp(DateUtils.formatCurrentDateTime(System.currentTimeMillis() + ""));
                }
            } else {
                if (!TextUtils.isEmpty(installModeEntity.getFenceAlarmDate())) {
                    if (isbalance < DateUtils.dateToStamp(installModeEntity.getFenceAlarmDate())) {
                        isPay = false;
                        isbalance = DateUtils.dateToStamp(installModeEntity.getFenceAlarmDate());
                        paySuccess(DevicePromptDialog.TYPE_PAY_FENCE_SUCCESS, installModeEntity.getFenceAlarmDate());
                    }
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onSendCommandSuccess(Object resultEntity) {
        showToast("设置成功");
        mPresenter.getFenceRadius();
        if (alarmRadiusDialog != null) alarmRadiusDialog.cancel();
    }

    @Override
    public void onFailure(int errType, String errMsg) {
        cancelLoading();
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
                Bundle bundle = new Bundle();
                bundle.putString("sn", sn);
                startIntentActivityForResult(RechargeActivityActivity.class, RechargeActivityActivity.RECHARGE_SUCCESS_CODE, bundle);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSubscribeEvent(PushEvent eventMessage) {//
        Logger.error("支付成功推送ID=" + eventMessage.getMsgId());
        switch (eventMessage.getMsgId()) {
            case AppConstants.MSG_FENCE://
                //                showToast("支付成功");
                fetchData();
                paySuccess(DevicePromptDialog.TYPE_PAY_FENCE_SUCCESS, eventMessage.getDeadlineDate());
                break;
        }
    }

    private void paySuccess(int type, String content) {
        Map<String, Object> bodyParams = new HashMap<>();
        bodyParams.put("vehicleId", AppVariable.currentVehicleId);
        bodyParams.put("flowId", "A" + System.currentTimeMillis());
        if (TextUtils.isEmpty(initRadius)) {
            bodyParams.put("paramValue", "300");//半径
        } else {
            bodyParams.put("paramValue", initRadius);//半径
        }
        bodyParams.put("msgId", AppConstants.MSG_EFENCE_FORTIFICATION);
        mPresenter.sendCommand(bodyParams);
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
        if (resultCode == RechargeActivityActivity.RECHARGE_SUCCESS_CODE) {
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
