package com.luhong.locwithlibrary.utils;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Rect;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 类库
 * Created by ITMG on 2020-01-08.
 */
public class BaseUtils {


    /**
     * 判断GPS是否打开
     *
     * @param context
     * @return
     */
    public static boolean isGpsEnabled(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager == null) return false;
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean isGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean isNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (isGps/* || isNetwork*/) {
            return true;
        }

        return false;
    }

    /**
     * 身份证号
     *
     * @param idCardNo
     * @return
     */
    public static boolean isIdCardNo(String idCardNo) {
        if (TextUtils.isEmpty(idCardNo)) return false;
        String regex = "^[1-9]\\d{5}[1-9]\\d{3}((0[1-9])|(1[0-2]))(([0|1|2][1-9])|3[0-1])((\\d{4})|\\d{3}X)$";
//        String regex = "\\d{15}(\\d{2}[0-9xX])?";
        return regex.matches(idCardNo);
    }


    /**
     * 手机号脱敏处理
     *
     * @param phone
     * @return
     */
    public static String phoneEncrypt(String phone) {
        if (!isPhoneNo(phone)) { //throw new IllegalArgumentException("手机号格式不正确!");
            return phone;
        }
        return phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }

    /**
     * 判断是否为电话号码格式
     */
    public static boolean isPhoneNo(String phoneNo) {
        if (TextUtils.isEmpty(phoneNo)) return false;
        Pattern p = Pattern.compile("^[+0-9][-0-9]{1,}$");
//        Pattern p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$");
        Matcher m = p.matcher(phoneNo);
        return m.matches();
    }

    /**
     * 拨打电话
     *
     * @param phoneNo
     * @param isDirectCall 是否直接拨打
     */
    public static void startCallPhone(Context context, String phoneNo, boolean isDirectCall) {
        if (TextUtils.isEmpty(phoneNo)) return;
        if (!isPhoneNo(phoneNo)) {
            ToastUtil.show("电话号码不正确");
            return;
        }
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(isDirectCall ? Intent.ACTION_CALL : Intent.ACTION_DIAL);//直接拨打
//        intent.setAction(Intent.ACTION_DIAL);//调用拨打电话页面
        intent.setData(Uri.parse("tel:" + phoneNo));
        if (isIntentAvailable(context, intent)) {
            context.startActivity(intent);
        } else {
            ToastUtil.show("没有拨打电话功能");
        }
    }

    /**
     * 判断设备有无拨打电话功能
     *
     * @param intent
     * @return
     */
    public static boolean isIntentAvailable(Context context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    /**
     * 打卡软键盘
     *
     * @param editText 输入框
     */
    public static void openInPut(Activity mActivity, EditText editText) {
        if (editText == null || mActivity == null) return;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        editText.setFocusable(true);
                        editText.setFocusableInTouchMode(true);
                        //请求获得焦点
                        editText.requestFocus();
                        //调用系统输入法
                        InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.showSoftInput(editText, 0);
                    }
                });
            }
        }, 200);
    }

    /**
     * 关闭对应输入框的软键盘
     */
    public static void closeInPut(EditText edit) {
        if (null != edit && edit.getContext() != null) {
            InputMethodManager imm = (InputMethodManager) edit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(edit.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 判断当前软键盘是否打开
     */
    public static boolean isInputShow(Activity context) {
        //获取当前屏幕内容的高度
        int screenHeight = context.getWindow().getDecorView().getHeight();
        //获取View可见区域的bottom
        Rect rect = new Rect();
        //DecorView即为activity的顶级view
        context.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        //考虑到虚拟导航栏的情况（虚拟导航栏情况下：screenHeight = rect.bottom + 虚拟导航栏高度）
        //选取screenHeight*2/3进行判断
        return screenHeight * 2 / 3 > rect.bottom;
    }

    /**
     * 设置密码可见和不可见
     */
    public static void setPasswordEye(EditText editText) {
        if (EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD == editText.getInputType()) {
            //如果不可见就设置为可见
            editText.setInputType(EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
            editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        } else {
            //如果可见就设置为不可见
            editText.setInputType(EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }
        //执行上面的代码后光标会处于输入框的最前方,所以把光标位置挪到文字的最后面
        editText.setSelection(editText.getText().toString().length());
    }


    /**
     * 是否同时包含数字和字母
     *
     * @param content
     * @return
     */
    public static boolean isMatcherNumberAndLetter(String content) {
        boolean isDigit = false;//是否包含数字
        boolean isLetter = false;//是否包含字母
        for (int i = 0; i < content.length(); i++) {
            if (Character.isDigit(content.charAt(i))) {
                isDigit = true;
            }
            if (Character.isLetter(content.charAt(i))) {
                isLetter = true;
            }
        }
        String regex = "^[a-zA-Z0-9]+$";
        boolean isMatcher = isDigit && isLetter && content.matches(regex);
        return isMatcher;
    }


    /**
     * 复制文本
     *
     * @param context
     * @param text
     */
    public static void copyText(Context context, String text) {
        if (TextUtils.isEmpty(text)) return;
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData mClipData = ClipData.newPlainText("Label", text);
        cm.setPrimaryClip(mClipData);
    }
}
