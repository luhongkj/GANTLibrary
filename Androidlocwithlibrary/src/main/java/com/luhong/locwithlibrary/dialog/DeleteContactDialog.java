package com.luhong.locwithlibrary.dialog;

import android.content.Context;
import android.view.View;

import androidx.appcompat.app.AlertDialog;

import com.luhong.locwithlibrary.R;


/**
 * 删除紧急联系人的弹窗
 */
public class DeleteContactDialog extends BaseDialogTow {

    View tv_cancel;
    View back_layout;
    View tv_ok;

    private View.OnClickListener onOkClickListener;

    public DeleteContactDialog(Context context) {
        super(context, R.layout.dialog_delete_contact);
    }


    @Override
    public void initView(AlertDialog alertDialog, View view) {
        tv_cancel = view.findViewById(R.id.tv_cancel);
        back_layout = view.findViewById(R.id.back_layout);
        tv_ok = view.findViewById(R.id.tv_ok);
    }

    @Override
    public void initEvent(AlertDialog alertDialog, View view) {
        tv_cancel.setOnClickListener(v -> dismiss());
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
        super.dismiss();
    }
}
