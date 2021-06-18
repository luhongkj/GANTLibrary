package com.luhong.locwithlibrary.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;


import com.luhong.locwithlibrary.R;
import com.luhong.locwithlibrary.R2;
import com.luhong.locwithlibrary.listener.SingleClickListener;

import butterknife.BindView;

/**
 * Created by ITMG on 2017/12/11.
 */
public class PromptDialog extends BaseDialog {
    @BindView(R2.id.tv_message_promptDialog)
    TextView tv_message;
    @BindView(R2.id.tv_cancel_promptDialog)
    TextView tv_cancel;
    @BindView(R2.id.tv_confirm_promptDialog)
    TextView tv_confirm;
    private IEventListener confirmListener;
    private String content;

    private static PromptDialog promptDialog;

    public static PromptDialog getInstance(Context context) {
        promptDialog = new PromptDialog(context);
        return promptDialog;
    }

    public void showDialog(String content, IEventListener onConfirmListener) {
        this.content = content;
        this.confirmListener = onConfirmListener;
        show();
    }

    public PromptDialog(Context context) {
        super(context);
    }

    @Override
    protected int initLayoutId() {
        return R.layout.dialog_prompt;
    }

    @Override
    protected int setGravity() {
        return Gravity.CENTER;
    }

    @Override
    protected void initAlertDialogView(Window window) {
        setCancelable(false);
        tv_message.setText(content);
    }

    @Override
    protected void onEventListener() {
        tv_confirm.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (confirmListener != null) confirmListener.onConfirm();
                cancel();
            }
        });
        tv_cancel.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (confirmListener != null) confirmListener.onCancel();
                cancel();
            }
        });
    }


}
