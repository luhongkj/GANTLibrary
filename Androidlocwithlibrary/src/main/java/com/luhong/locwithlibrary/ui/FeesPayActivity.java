package com.luhong.locwithlibrary.ui;

import androidx.appcompat.app.AppCompatActivity;

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
import com.luhong.locwithlibrary.api.AppVariable;
import com.luhong.locwithlibrary.app.BaseConstants;
import com.luhong.locwithlibrary.base.BaseActivity;
import com.luhong.locwithlibrary.base.BaseMvpActivity;
import com.luhong.locwithlibrary.contract.home.HomeContract;
import com.luhong.locwithlibrary.entity.DeviceEntity;
import com.luhong.locwithlibrary.entity.DevicePositionEntity;
import com.luhong.locwithlibrary.entity.HomeDataEntity;
import com.luhong.locwithlibrary.entity.UserEntity;
import com.luhong.locwithlibrary.entity.VehicleListEntity;
import com.luhong.locwithlibrary.presenter.BaseMvpView;
import com.luhong.locwithlibrary.presenter.home.HomePresenter;
import com.luhong.locwithlibrary.utils.SPUtils;

import java.util.List;

/**
 * 补缴/缴费
 */
public class FeesPayActivity extends BaseMvpActivity<HomePresenter> implements HomeContract.View {

    public static final int RECHARGE_SUCCESS_CODE = 521;
    String activateSn;
    float money;
    float serverLength;


    @Override
    protected int initLayoutId() {
        return R.layout.activity_fees_pay;
    }

    boolean isPay;

    @Override
    protected void initView(Bundle savedInstanceState) {
        activateSn = getIntent().getStringExtra("SN");
        money = Float.parseFloat(getIntent().getStringExtra("money"));
        serverLength = Float.parseFloat(getIntent().getStringExtra("serverLength"));
        String token = SPUtils.getString(Locwith.getContext(), BaseConstants.TOKEN);
        String url =  BuildConfig.H5_PAY + "supplementaryPayment.html?" + "token=" + token + "&sn=" + activateSn + "&cost=" + money + "&serverLength=" + (money / serverLength);
        initTitleView(true, "支付");
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

    @Override
    public void onFailure(int errType, String errMsg) {
        cancelLoading();
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
            if (!TextUtils.isEmpty(activateSn) && vehicle.getSn().equals(activateSn)) {//判断设备是否是当前选中的
                if (vehicle.getOweFee() >= 0) {//欠费
                    setResult(RECHARGE_SUCCESS_CODE);
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
    public void onDeviceListSuccess(List<DeviceEntity> dataList) {

    }

    @Override
    public void onVehicleListSuccess(List<VehicleListEntity> resultEntity) {

    }
}
