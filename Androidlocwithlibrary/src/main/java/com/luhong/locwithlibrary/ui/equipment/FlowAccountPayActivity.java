package com.luhong.locwithlibrary.ui.equipment;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.just.agentweb.AgentWeb;
import com.just.agentweb.WebViewClient;
import com.luhong.locwithlibrary.Locwith;
import com.luhong.locwithlibrary.R;
import com.luhong.locwithlibrary.app.BaseConstants;
import com.luhong.locwithlibrary.base.BaseActivity;
import com.luhong.locwithlibrary.base.BaseMvpActivity;
import com.luhong.locwithlibrary.contract.FlowAccountContract;
import com.luhong.locwithlibrary.dialog.DevicePromptDialog;
import com.luhong.locwithlibrary.entity.FlowAccountEntity;
import com.luhong.locwithlibrary.entity.FlowBillEntity;
import com.luhong.locwithlibrary.net.response.BasePageEntity;
import com.luhong.locwithlibrary.presenter.FlowAccountPresenter;
import com.luhong.locwithlibrary.utils.SPUtils;

public class FlowAccountPayActivity extends BaseMvpActivity<FlowAccountPresenter> implements FlowAccountContract.View {

    public static final int FLOWACCOUNTPAY = 520;

    @Override
    protected int initLayoutId() {
        return R.layout.activity_flow_account_pay;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        String token = SPUtils.getString(Locwith.getContext(), BaseConstants.TOKEN);
        String url = "http://gis.luhongkj.com:8802/flowTopUp.html?" + "token=" + token;
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
        mPresenter.getFlowAccountInfo();
    }

    float isbalance;//支付前的月份_用于判断支付完成后的一个支付状态

    boolean isPay;//判斷是否拿了數據,數據只拿一次

    @Override
    public void onFlowAccountInfoSuccess(FlowAccountEntity resultEntity) {
        cancelLoading();
        if (resultEntity != null || resultEntity.getMoney() < resultEntity.getFlowFee()) {
            float moneys =  resultEntity.getMoney();
            float flowFee = (float) resultEntity.getFlowFee();
            if (!isPay) {
                isPay = !isPay;
                isbalance = (moneys / flowFee);
            } else {
                float yue = (moneys / flowFee);
                if (yue > isbalance) {
                    setResult(FLOWACCOUNTPAY);
                    finish();
                }
            }
        }
    }

    @Override
    public void onBillSuccess(BasePageEntity<FlowBillEntity> dataList) {

    }

    @Override
    public void onFailure(int errType, String errMsg) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isPay) {
            showLoading("获取支付结果中...");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mPresenter.getFlowAccountInfo();
                }
            }, 1000);

        }
    }
}
