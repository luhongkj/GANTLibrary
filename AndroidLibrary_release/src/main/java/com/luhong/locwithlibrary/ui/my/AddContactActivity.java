package com.luhong.locwithlibrary.ui.my;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.luhong.locwithlibrary.R;
import com.luhong.locwithlibrary.R2;
import com.luhong.locwithlibrary.api.ApiClient;
import com.luhong.locwithlibrary.api.ApiServer;
import com.luhong.locwithlibrary.api.SCallBack;
import com.luhong.locwithlibrary.app.BaseVariable;
import com.luhong.locwithlibrary.base.BaseActivity;
import com.luhong.locwithlibrary.base.BaseActivityTow;
import com.luhong.locwithlibrary.listener.SingleClickListener;
import com.luhong.locwithlibrary.net.response.BaseResponse;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;

/**
 * 添加紧急联系人
 */
public class AddContactActivity extends BaseActivity {
    @BindView(R2.id.et_phone)
    EditText et_phone;
    @BindView(R2.id.et_name)
    EditText et_name;

    @BindView(R2.id.btn_save)
    Button btn_save;

    @Override
    protected int initLayoutId() {
        return R.layout.activity_add_contact;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        initTitleView(true, "添加紧急联系人");
        btn_save.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                String phone = et_phone.getText().toString();
                String name = et_name.getText().toString();
                if (phone.isEmpty() || phone.length() < 11) {
                    showToast(et_phone.getHint().toString());
                    return;
                }

                ApiServer apiServer = ApiClient.getInstance().getApiServer();
                Map<String, Object> params = new HashMap<>();
                params.put("contactName", name);
                params.put("phone", phone);
                params.put("userId", BaseVariable.userId);
                Call<BaseResponse<String>> call = apiServer.insertContact(params);
                call.enqueue(new SCallBack<BaseResponse<String>>() {
                    @Override
                    public void onSuccess(@NonNull BaseResponse<String> result) {
                        showToast(result.getMsg());
                        if (result.isSuccess()) {
                            et_phone.setText("");
                            et_name.setText("");
                            setResult(Activity.RESULT_OK);
                            finish();
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onEventListener() {

    }

}
