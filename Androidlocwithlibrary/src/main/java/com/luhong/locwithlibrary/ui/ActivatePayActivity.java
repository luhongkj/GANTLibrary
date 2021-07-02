package com.luhong.locwithlibrary.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.just.agentweb.AgentWeb;
import com.just.agentweb.WebViewClient;
import com.luhong.locwithlibrary.BuildConfig;
import com.luhong.locwithlibrary.Locwith;
import com.luhong.locwithlibrary.R;
import com.luhong.locwithlibrary.api.ApiServer;
import com.luhong.locwithlibrary.app.BaseConstants;
import com.luhong.locwithlibrary.base.BaseMvpActivity;
import com.luhong.locwithlibrary.contract.home.HomeContract;
import com.luhong.locwithlibrary.entity.DevicePositionEntity;
import com.luhong.locwithlibrary.entity.HomeDataEntity;
import com.luhong.locwithlibrary.entity.UserEntity;
import com.luhong.locwithlibrary.presenter.home.HomePresenter;
import com.luhong.locwithlibrary.utils.SPUtils;

/**
 * 设备激活
 */
public class ActivatePayActivity extends BaseMvpActivity<HomePresenter> implements HomeContract.View {
    public static final int TYPE_ACTIVE = 522;
    String sn;
    String money;

    @Override
    protected int initLayoutId() {
        return R.layout.activity_activate_pay;
    }

    boolean isPay;

    @Override
    protected void initView(Bundle savedInstanceState) {
        sn = getIntent().getStringExtra("SN");
        money = getIntent().getStringExtra("money");
        String token = SPUtils.getString(Locwith.getContext(), BaseConstants.TOKEN);
        String url = BuildConfig.H5_PAY + "equipmentActivation.html?" + "token=" + token + "&sn=" + sn + "&cost=" + money;
        initTitleView(true, "设备激活支付");
        //获得控件
        LinearLayout webView = findViewById(R.id.pay_ivew);
        AgentWeb.with(this)
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
            isPay = true;
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

    }

    @Override
    public void onLocationInitFailure(int errCode) {

    }

    @Override
    public void onLocationInitSuccess() {

    }

    @Override
    public void onHomeDataSuccess(HomeDataEntity resultEntity) {
        cancelLoading();
        for (HomeDataEntity.Vehicle vehicle : resultEntity.getVehicles()) {
            if (vehicle == null || TextUtils.isEmpty(vehicle.getSn()) || TextUtils.isEmpty(vehicle.getId()))
                continue;
            //当前选中的这边为空AppVariable.currentDeviceId
            if (!TextUtils.isEmpty(sn) && vehicle.getSn().equals(sn)) {//判断设备是否是当前选中的
                if (vehicle.getUnitStatus() == HomeDataEntity.Vehicle.TYPE_ACTIVATED) {//已激活
                    setResult(TYPE_ACTIVE);
                    finish();
                    break;
                }
            }
        }
    }

    @Override
    public void onTrackInfoSuccess(DevicePositionEntity dataList) {

    }

    @Override
    public void onSendCommandSuccess(Object resultEntity) {

    }

    @Override
    public void onActiveBySnSuccess(Object resultEntity) {

    }

    @Override
    public void onLoginSuccess(UserEntity userEntity) {

    }

    @Override
    public void onVehicleConfirmPaySuccess(Object resultEntity) {

    }

    @Override
    public void onCheckTokenBindSuccess(UserEntity resultEntity) {

    }

    @Override
    public void onFailure(int errType, String errMsg) {
        cancelLoading();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isPay) {
            showLoading("加载中...");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mPresenter.getHomeData();
                }
            }, 2000);
        }
    }
}
