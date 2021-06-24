package com.luhong.locwithlibrary.ui.equipment.electronic;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.just.agentweb.AgentWeb;
import com.just.agentweb.WebViewClient;
import com.luhong.locwithlibrary.Locwith;
import com.luhong.locwithlibrary.R;
import com.luhong.locwithlibrary.api.ApiServer;
import com.luhong.locwithlibrary.app.BaseConstants;
import com.luhong.locwithlibrary.base.BaseMvpActivity;
import com.luhong.locwithlibrary.contract.PhoneAlarmContract;
import com.luhong.locwithlibrary.entity.DeviceServerEntity;
import com.luhong.locwithlibrary.presenter.PhoneAlarmPresenter;
import com.luhong.locwithlibrary.utils.DateUtils;
import com.luhong.locwithlibrary.utils.SPUtils;
import com.luhong.locwithlibrary.utils.ToastUtil;

import java.text.ParseException;

public class RechargeActivityActivity extends BaseMvpActivity<PhoneAlarmPresenter> implements PhoneAlarmContract.View {

    public static final int RECHARGE_SUCCESS_CODE = 520;
    String sn;

    @Override
    protected int initLayoutId() {
        return R.layout.activity_recharge_activity;
    }

    AgentWeb agentWeb;

    @Override
    protected void initView(Bundle savedInstanceState) {
        sn = getIntent().getStringExtra("sn");
        String token = SPUtils.getString(Locwith.getContext(), BaseConstants.TOKEN);
        String url = ApiServer.H5_PAY + "electronicFence.html?" + "token=" + token + "&sn=" + sn;
        initTitleView(true, "支付");
        //获得控件
        LinearLayout webView = findViewById(R.id.pay_ivew);
        agentWeb = AgentWeb.with(this)
                .setAgentWebParent(webView, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .setWebViewClient(mWebViewClient)
                .createAgentWeb()
                .ready()
                .go(url);
    }

    private WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            //do you  work
        }
    };

    @Override
    protected void initData() {

    }

    @Override
    protected void onEventListener() {

    }


    @Override
    protected void fetchData() {
        mPresenter.getVehicleBySn(sn);
    }

    @Override
    public void onGetVehicleBySnSuccess(DeviceServerEntity deviceServerEntity) {
        cancelLoading();
        if (deviceServerEntity == null) {
            ToastUtil.show("设备未激活");
            return;
        }
        // deviceServerEntity.getTelAlarmDate()  电话告警过期时间
        try {
            if (!isPay) {
                isPay = !isPay;
                if (!TextUtils.isEmpty(deviceServerEntity.getFenceAlarmDate())) {
                    isbalance = DateUtils.dateToStamp(deviceServerEntity.getFenceAlarmDate());
                } else {//首次充值,没有返回时间
                    isbalance = DateUtils.dateToStamp(DateUtils.formatCurrentDateTime(System.currentTimeMillis() + ""));
                }
            } else {
                if (isbalance < DateUtils.dateToStamp(deviceServerEntity.getFenceAlarmDate())) {
                    setResult(RECHARGE_SUCCESS_CODE);
                    finish();
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(int errType, String errMsg) {

    }

    long isbalance;//充值前的到期时间
    boolean isPay;//判斷是否拿了數據,數據只拿一次

    @Override
    protected void onResume() {
        super.onResume();
        if (isPay) {
            showLoading("获取支付结果中...");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mPresenter.getVehicleBySn(sn);
                }
            }, 2000);
        }
    }
}
