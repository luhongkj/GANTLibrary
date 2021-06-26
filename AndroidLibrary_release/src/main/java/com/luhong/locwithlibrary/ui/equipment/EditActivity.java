package com.luhong.locwithlibrary.ui.equipment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.luhong.locwithlibrary.R;
import com.luhong.locwithlibrary.R2;
import com.luhong.locwithlibrary.api.AppConstants;
import com.luhong.locwithlibrary.base.BaseActivity;
import com.luhong.locwithlibrary.listener.SingleClickListener;
import com.luhong.locwithlibrary.view.ClearEditText;

import butterknife.BindView;

public class EditActivity extends BaseActivity {
    @BindView(R2.id.et_content_edit)
    ClearEditText et_content;

    public final int TYPE_DEVICE = 101;
    public final int TYPE_USER = 102;
    private int dataType = TYPE_USER;
    private String title, content, hint;

    @Override
    protected int initLayoutId() {
        return R.layout.activity_edit;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        Bundle bundle = getIntent().getExtras();
        dataType = bundle.getInt(AppConstants.dataTypeKey, 0);
        content = bundle.getString(AppConstants.dataKey, "");
        if (dataType == TYPE_DEVICE) {
            title = "修改车辆昵称";
            hint = "请输入车辆昵称";
        } else if (dataType == TYPE_USER) {
            title = "修改昵称";
            hint = "请输入昵称";
        }
        et_content.setHint(hint);
        if (!TextUtils.isEmpty(content)) {
            et_content.setText(content);
            int maxLength = 20;
            et_content.setSelection(Math.min(maxLength, content.length()));
        }
        initTitleView(true, title, "完成", new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                String content = et_content.getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
                    showToast(hint);
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra(AppConstants.dataKey, content);
                setResult(AppConstants.RESULT_CODE_UPDATE, intent);
                finish();
            }
        });
        openKeyboard(et_content);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onEventListener() {

    }

}
