package com.luhong.locwithlibrary.ui.insurance;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.luhong.locwithlibrary.R;
import com.luhong.locwithlibrary.R2;
import com.luhong.locwithlibrary.base.BaseActivity;
import com.luhong.locwithlibrary.entity.SafeguardEntity;
import com.luhong.locwithlibrary.listener.SingleClickListener;

import butterknife.BindView;

public class SafeguardSuccessActivity extends BaseActivity {
    public static final String dataTypeKey = "dataTypeKey";
    @BindView(R2.id.btn_confirm_safeguardSuccess)
    Button btn_confirm;
    private SafeguardEntity safeguardEntity;

    @Override
    protected int initLayoutId() {
        return R.layout.activity_safeguard_success;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        initTitleView(true, "保障服务详情");
        Bundle bundle = getIntent().getExtras();
        safeguardEntity = (SafeguardEntity) bundle.getSerializable(dataTypeKey);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onEventListener() {
        btn_confirm.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (safeguardEntity != null) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(SafeguardSuccessActivity.dataTypeKey, safeguardEntity);
                    startIntentActivity(SafeguardDetailActivity.class, bundle);
                } else {
                    startIntentActivity(SafeguardDetailActivity.class);
                }
                finish();
            }
        });
    }

}
