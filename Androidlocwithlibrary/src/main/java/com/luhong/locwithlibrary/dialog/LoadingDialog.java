package com.luhong.locwithlibrary.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.luhong.locwithlibrary.R;


public class LoadingDialog extends Dialog {
    public LoadingDialog(@NonNull Context context) {
        super(context);
    }

    public LoadingDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected LoadingDialog(@NonNull Context context, boolean cancelable, @Nullable DialogInterface.OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    //2,创建静态内部类Builder，将dialog的部分属性封装进该类
    public static class Builder {

        private Activity context;
        //提示信息
        private String message;
        //是否展示提示信息
        private boolean isShowMessage = true;
        //是否按返回键取消
        private boolean isCancelable = true;
        //是否取消
        private boolean isCancelOutside = false;

        public Builder(Activity context) {
            this.context = context;
        }

        /**
         * 设置提示信息
         *
         * @param message
         * @return
         */
        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        /**
         * 设置是否显示提示信息
         *
         * @param isShowMessage
         * @return
         */
        public Builder setShowMessage(boolean isShowMessage) {
            this.isShowMessage = isShowMessage;
            return this;
        }

        /**
         * 设置是否可以按返回键取消
         *
         * @param isCancelable
         * @return
         */
        public Builder setCancelable(boolean isCancelable) {
            this.isCancelable = isCancelable;
            return this;
        }

        /**
         * 设置是否可以取消
         *
         * @param isCancelOutside
         * @return
         */
        public Builder setCancelOutside(boolean isCancelOutside) {
            this.isCancelOutside = isCancelOutside;
            return this;
        }

        //创建Dialog
        public LoadingDialog create() {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.dialog_progres_view, null);
            //设置带自定义主题的dialog
            LoadingDialog loadingDialog = new LoadingDialog(context/*, R.style.DialogBottomAnimStyle*/);
            Window window = loadingDialog.getWindow();
            window.setBackgroundDrawableResource(R.color.transparent);//透明背景
//            window.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);

            TextView msgText = view.findViewById(R.id.tv_progressText_dialog);
            if (isShowMessage) {
                if (!TextUtils.isEmpty(message)) msgText.setText(message);
            } else {
                msgText.setVisibility(View.GONE);
            }
            loadingDialog.setContentView(view);
            loadingDialog.setCancelable(isCancelable);
            loadingDialog.setCanceledOnTouchOutside(isCancelOutside);

            return loadingDialog;
        }
    }

}
