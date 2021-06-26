package com.luhong.locwithlibrary.utils;

import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

import androidx.annotation.IdRes;

public class FromNetUtil
{
    private static final boolean IS_FROM_NET = true;

    /**
     * 用来判断控件的这个操作，是不是由网络/代码调用的，不响应来自代码调用的事件
     *
     * @param view 传入控件
     * @return 是不是由网络/代码调用的
     */
    public static boolean isFromNet(View view)
    {
        boolean result = view.getTag() != null && (boolean) view.getTag();
        view.setTag(null);
        return result;
    }

    /**
     * 设置勾选状态，以网络/代码调用的形式，主要是为了不响应对应的事件
     *
     * @param radioGroup 单选父控件
     * @param childId    子控件ID
     */
    public static void setCheckedFromNet(RadioGroup radioGroup, @IdRes int childId)
    {
        RadioButton radioButton = radioGroup.findViewById(childId);
        if (radioButton != null && !radioButton.isChecked())
        {
            radioGroup.setTag(IS_FROM_NET);
            radioButton.setChecked(true);
        }
    }

    /**
     * 设置勾选状态，以网络/代码调用的形式，主要是为了不响应对应的事件
     *
     * @param aSwitch 单选父控件
     * @param checked 勾选状态
     */
    public static void setCheckedFromNet(Switch aSwitch, boolean checked)
    {
        if (aSwitch.isChecked() != checked)
        {
            aSwitch.setTag(IS_FROM_NET);
            aSwitch.setChecked(checked);
        }
    }
}
