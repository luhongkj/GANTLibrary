package com.luhong.locwithlibrary.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.luhong.locwithlibrary.R;


/**
 * 确认的弹窗
 */
public class DefaultConfirmDialog extends BaseDialogTow {
    View tv_cancel;
    View back_layout;
    View tv_ok;
    View split_line;

    // 标题
    private TextView title;
    // 内容
    private TextView content;

    private View.OnClickListener onOkClickListener;
    private View.OnClickListener onCancelListener;

    private String titleStr;
    private String contentStr;


    public DefaultConfirmDialog(Context context) {
        super(context, R.layout.dialog_delete_contact);
        setCancelable(false);
    }

    @Override
    public void initView(AlertDialog alertDialog, View view) {
        tv_cancel = view.findViewById(R.id.tv_cancel);
        back_layout = view.findViewById(R.id.back_layout);
        tv_ok = view.findViewById(R.id.tv_ok);
        split_line = view.findViewById(R.id.split_line);

        title = view.findViewById(R.id.title);
        content = view.findViewById(R.id.content);

        tv_cancel.setVisibility(View.GONE);
        split_line.setVisibility(View.GONE);

        this.title.setText(titleStr);
        if (contentStr == null) {
            this.content.setVisibility(View.GONE);
        } else {
            this.content.setText(contentStr);
        }
    }

    // 设置内容
    public void setContent(String title, String content) {
        this.titleStr = title;
        this.contentStr = content;
    }

    // 设置内容
    public void setContent(String title) {
        this.titleStr = title;
        this.contentStr = null;
    }

    @Override
    public void initEvent(AlertDialog alertDialog, View view) {
        tv_cancel.setOnClickListener(v ->
        {
            if (onCancelListener != null)
                onCancelListener.onClick(v);
            dismiss();
        });
        back_layout.setOnClickListener(v -> dismiss());
        tv_ok.setOnClickListener(v ->
        {
            if (onOkClickListener != null)
                onOkClickListener.onClick(v);
            dismiss();
        });
    }

    /**
     * 点击OK的事件
     */
    public void setOnOkClickListener(View.OnClickListener onOkClickListener) {
        this.onOkClickListener = onOkClickListener;
    }

    @Override
    public void dismiss() {
        this.onOkClickListener = null;
        this.onCancelListener = null;
        super.dismiss();
    }
}
