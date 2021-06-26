package com.luhong.locwithlibrary.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.luhong.locwithlibrary.R;
import com.luhong.locwithlibrary.R2;
import com.luhong.locwithlibrary.base.BaseActivity;
import com.luhong.locwithlibrary.listener.SingleClickListener;
import com.luhong.locwithlibrary.utils.ToastUtil;

import butterknife.BindView;

/**
 * 温馨提示
 * Created by ITMG on 2019/12/11.
 */
public class MessageDialog extends BaseDialog {

    @BindView(R2.id.tv_confirm_devicePrompt)
    TextView tvConfirm;
    @BindView(R2.id.tv_title_devicePrompt)
    TextView tvTitle;
    @BindView(R2.id.rl_contentBg_devicePrompt)
    RelativeLayout rl_contentBg;
    @BindView(R2.id.tv_content_devicePrompt)
    TextView tvContent;

    private static MessageDialog devicePromptDialog;
    private IEventListener confirmListener;
    private String title;
    private String confirmTitle;

    public static MessageDialog getInstance(Context context) {
        devicePromptDialog = new MessageDialog(context);
        return devicePromptDialog;
    }

    public void showDialog(String title, String confirmTitle, IEventListener onConfirmListener) {
        this.confirmListener = onConfirmListener;
        this.confirmTitle = confirmTitle;
        this.title = title;
        if (devicePromptDialog != null) show();
    }

    public MessageDialog(Context context) {
        super(context);
    }

    @Override
    protected int initLayoutId() {
        return R.layout.dialog_message;
    }

    @Override
    protected int setGravity() {
        return Gravity.CENTER;
    }

    @Override
    protected void initAlertDialogView(Window window) {
        tvContent.setText(title);
        tvConfirm.setText(confirmTitle);
    }

    @Override
    protected void onEventListener() {
        tvConfirm.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (confirmListener != null) confirmListener.onConfirm();
                cancel();
            }
        });
    }

    private long firstPressedTime = 0;

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - firstPressedTime > 2000) {
            ToastUtil.show(R.string.exit_app);
            firstPressedTime = System.currentTimeMillis();
        } else {
            BaseActivity.exitApp();
        }
    }
}
