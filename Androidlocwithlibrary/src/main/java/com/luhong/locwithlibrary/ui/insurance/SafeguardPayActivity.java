package com.luhong.locwithlibrary.ui.insurance;

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
import com.luhong.locwithlibrary.base.BaseMvpFragment;
import com.luhong.locwithlibrary.contract.SafeguardContract;
import com.luhong.locwithlibrary.entity.SafeguardEntity;
import com.luhong.locwithlibrary.net.response.BasePageEntity;
import com.luhong.locwithlibrary.presenter.SafeguardPresenter;
import com.luhong.locwithlibrary.utils.SPUtils;
import com.luhong.locwithlibrary.utils.ToastUtil;

import java.util.List;

public class SafeguardPayActivity extends BaseMvpActivity<SafeguardPresenter> implements SafeguardContract.View {
    public static final String DATA_PAY = "DATA_PAY";//4待支付
    SafeguardEntity data;

    @Override
    protected int initLayoutId() {
        return R.layout.activity_safeguard_pay;
    }

    /**
     * 购买保险：
     * http://gis.luhongkj.com:8802/buyInsurance.html
     * 需要拼接参数：token：token；insureCost：保险费用；cost：应收保费/年；valuation：车辆估值；insureFee：赠送保费；orderNo：保单号
     * @param savedInstanceState
     */
    @Override
    protected void initView(Bundle savedInstanceState) {
        data = (SafeguardEntity) getIntent().getSerializableExtra(DATA_PAY);
        String token = SPUtils.getString(Locwith.getContext(), BaseConstants.TOKEN);
        String url = "http://gis.luhongkj.com:8802/buyInsurance.html?" + "token=" + token + "&insureCost=" +data.getCost() + "&cost=" +  data.getInsureCost() + "&valuation="
                + data.getValuation() + "&insureFee=" + data.getInsureFee() + "&orderNo=" + data.getInsuranceNo();
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
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showLoading("加载中...");
                mPresenter.getSafeguardMine();
            }
        }, 2000);
    }

    @Override
    protected void fetchData() {

    }

    @Override
    public void onSafeguardHomeSuccess(List<SafeguardEntity> dataList) {
        cancelLoading();
    }

    @Override
    public void onSafeguardMineSuccess(BasePageEntity<SafeguardEntity> pageList) {
        cancelLoading();
        for (SafeguardEntity dataConten : pageList.getRecords()) {
            if (dataConten.getInsuranceNo().equals(data.getInsuranceNo())) {
                if (data.getStatus() != SafeguardEntity.TYPE_PAY) {
                    ToastUtil.show("保单支付成功");
                    finish();
                }
                return;
            }
        }
    }

    @Override
    public void onFailure(int errType, String errMsg) {
        cancelLoading();
    }
}
