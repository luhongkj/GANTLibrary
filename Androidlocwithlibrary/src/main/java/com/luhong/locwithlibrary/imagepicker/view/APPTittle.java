package com.luhong.locwithlibrary.imagepicker.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintSet;

import com.luhong.locwithlibrary.R;
import com.luhong.locwithlibrary.utils.Ext;


/**
 * 统一封装的界面标题栏
 */
public class APPTittle extends ImmersiveView {
    private ConstraintSet constraintSet = new ConstraintSet();
    private final int normal = 0x00000000, italic = 0x00000016, bold = 0x00000032;
    // 左边图标
    public ImageView leftImgView;
    // 左边文字
    public TextView leftTextView;
    // 中间标题左边小图标
    public ImageView titleLeftIconImageView;
    // 中间标题
    public TextView titleTextView;
    // 中间标题右边小图标
    public ImageView titleRightIconImageView;
    // 右边文字
    public TextView rightTextView;
    // 右边图标
    public ImageView rightImgView;

    public APPTittle(Context context) {
        this(context, null);
    }

    public APPTittle(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public APPTittle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.APPTittle);
        init(context, typedArray);
        typedArray.recycle();
        initView(context);
    }

    /**
     * 控件的初始化
     *
     * @param context
     * @param typedArray
     */
    private void init(Context context, TypedArray typedArray) {
        leftImgView = initLeftImgView(context);
        leftTextView = initLeftTextView(context);
        titleLeftIconImageView = initTitleLeftIconImageView(context);
        titleTextView = initTitleTextView(context);
        titleRightIconImageView = initTitleRightIconImageView(context);
        rightTextView = initRightTextView(context);
        rightImgView = initRightImgView(context);


        // 左边图片是否显示
        leftImgView.setVisibility(typedArray.getInt(R.styleable.APPTittle_left_img_visible, VISIBLE));
        // 左边文字是否显示
        leftTextView.setVisibility(typedArray.getInt(R.styleable.APPTittle_left_text_visible, VISIBLE));
        // 标题左边icon是否显示
        titleLeftIconImageView.setVisibility(typedArray.getInt(R.styleable.APPTittle_title_left_icon_visible, GONE));
        // 标题是否显示
        titleTextView.setVisibility(typedArray.getInt(R.styleable.APPTittle_title_text_visible, VISIBLE));
        // 标题右边icon是否显示,默认为不显示
        titleRightIconImageView.setVisibility(typedArray.getInt(R.styleable.APPTittle_title_right_icon_visible, GONE));
        // 右边文字是否显示
        rightTextView.setVisibility(typedArray.getInt(R.styleable.APPTittle_right_text_visible, GONE));
        // 右边图片是否显示
        rightImgView.setVisibility(typedArray.getInt(R.styleable.APPTittle_right_img_visible, GONE));

        // 左边图片，默认为返回图标
        int leftImgDrawId = typedArray.getResourceId(
                R.styleable.APPTittle_left_img,
                R.mipmap.title_return
        );
        leftImgView.setImageResource(leftImgDrawId);
        // 左边图片的内距,默认为8dp
        int leftImgPadding = typedArray.getDimensionPixelSize(
                R.styleable.APPTittle_left_img_padding,
                Ext.dp2px(context, 12)
        );
        leftImgView.setPaddingRelative(leftImgPadding, 0, leftImgPadding, 0);
        // 左边文字
        String leftTextStr = typedArray.getString(R.styleable.APPTittle_left_text);
        leftTextView.setText(leftTextStr == null ? "Back" : leftTextStr);
        // 左边文字大小,避免受到老年机字体的影响，默认单位是13DP
        int leftTextSize = typedArray.getDimensionPixelSize(
                R.styleable.APPTittle_left_text_size,
                Ext.dp2px(context, 13)
        );
        leftTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, leftTextSize);
        // 左边的文字颜色，默认为白色
        int leftTextColor = typedArray.getColor(R.styleable.APPTittle_left_text_color, Color.WHITE);
        leftTextView.setTextColor(leftTextColor);
        // 左边文字样式
        int leftTextStyle = typedArray.getInt(R.styleable.APPTittle_left_text_style, normal);

        if (leftTextStyle == normal) {

        } else if (leftTextStyle == italic) {
            leftTextView.setTypeface(leftTextView.getTypeface(), Typeface.ITALIC);
        } else if (leftTextStyle == bold) {
            Paint paint = leftTextView.getPaint();
            paint.setFakeBoldText(true);
        }

        // 标题左边的图标，默认为红点点
        int leftIcon = typedArray.getResourceId(
                R.styleable.APPTittle_title_left_icon,
                R.mipmap.base_icon_point_default);
        titleLeftIconImageView.setImageResource(leftIcon);
        // 标题文字
        String titleTextStr = typedArray.getString(R.styleable.APPTittle_title_text);
        titleTextView.setText(titleTextStr == null ? "Title" : titleTextStr);
        // 标题文字大小，默认15，避免被老年机影响，默认单位使用DP
        int titleTextSize = typedArray.getDimensionPixelSize(
                R.styleable.APPTittle_title_text_size,
                Ext.dp2px(context, 15));
        titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize);
        // 获取标题文字颜色
        int titleTextColor =
                typedArray.getColor(R.styleable.APPTittle_title_text_color, Color.WHITE);
        titleTextView.setTextColor(titleTextColor);
        // 设置标题文字样式
        int titleTextStyle = typedArray.getInt(R.styleable.APPTittle_title_text_style, normal);

        if (titleTextStyle == normal) {

        } else if (titleTextStyle == italic) {
            titleTextView.setTypeface(titleTextView.getTypeface(), Typeface.ITALIC);
        } else if (titleTextStyle == bold) {
            Paint paint = titleTextView.getPaint();
            paint.setFakeBoldText(true);
        }
        // 标题右边图标，默认为下拉图标
        int titleRightIcon = typedArray.getResourceId(
                R.styleable.APPTittle_title_right_icon,
                R.mipmap.base_icon_dorp_down_default);
        titleRightIconImageView.setImageResource(titleRightIcon);
        // 获取右边图标的内距
        int rightImgPadding = typedArray.getDimensionPixelSize(
                R.styleable.APPTittle_right_img_padding,
                Ext.dp2px(context, 12));
        rightImgView.setPaddingRelative(rightImgPadding, 0, rightImgPadding, 0);
        // 右边文字
        String rightText = typedArray.getString(R.styleable.APPTittle_right_text);
        rightTextView.setText(rightText == null ? "Menu" : rightText);
        // 右边文字大小，默认为13，避免受老年机影响，单位为DP
        int rightTextSize = typedArray.getDimensionPixelSize(
                R.styleable.APPTittle_right_text_size,
                Ext.dp2px(context, 13));
        rightTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, rightTextSize);
        // 右边文字颜色，默认为白色
        int rightTextColor = typedArray.getColor(R.styleable.APPTittle_right_text_color, Color.WHITE);
        rightTextView.setTextColor(rightTextColor);
        // 设置标题文字样式
        int rightTextStyle = typedArray.getInt(R.styleable.APPTittle_right_text_style, normal);
        if (rightTextStyle == normal) {

        } else if (rightTextStyle == italic) {
            rightTextView.setTypeface(rightTextView.getTypeface(), Typeface.ITALIC);
        } else if (rightTextStyle == bold) {
            Paint paint = rightTextView.getPaint();
            paint.setFakeBoldText(true);
        }

        // 右边图标，默认为菜单图标
        int rightImage = typedArray.getResourceId(
                R.styleable.APPTittle_right_img,
                R.mipmap.base_icon_menu_default);
        rightImgView.setImageResource(rightImage);
        // 获取左右两个图标的内距
        int leftRightPadding = typedArray.getDimensionPixelSize(R.styleable.APPTittle_left_right_img_padding, -1);
        if (leftRightPadding != -1) {
            leftImgView.setPaddingRelative(leftRightPadding, 0, leftRightPadding, 0);
            rightImgView.setPaddingRelative(leftRightPadding, 0, leftRightPadding, 0);
        }

    }


    /**
     * 初始化界面
     */
    private void initView(Context context) {
        this.addView(leftImgView);
        this.addView(leftTextView);
        this.addView(titleLeftIconImageView);
        this.addView(titleTextView);
        this.addView(titleRightIconImageView);
        this.addView(rightTextView);
        this.addView(rightImgView);

        constraintSet.clone(contentView);
        // 左边图片的设置
        constraintSet.constrainWidth(leftImgView.getId(), ConstraintSet.WRAP_CONTENT);
        constraintSet.constrainHeight(leftImgView.getId(), 0);
        constraintSet.connect(
                leftImgView.getId(),
                ConstraintSet.BOTTOM,
                ConstraintSet.PARENT_ID,
                ConstraintSet.BOTTOM
        );
        constraintSet.connect(
                leftImgView.getId(),
                ConstraintSet.START,
                ConstraintSet.PARENT_ID,
                ConstraintSet.START
        );
        constraintSet.connect(
                leftImgView.getId(),
                ConstraintSet.TOP,
                ConstraintSet.PARENT_ID,
                ConstraintSet.TOP
        );
        constraintSet.connect(
                leftImgView.getId(),
                ConstraintSet.END,
                leftTextView.getId(),
                ConstraintSet.START
        );

        // 左边文字的设置
        constraintSet.constrainWidth(leftTextView.getId(), ConstraintSet.WRAP_CONTENT);
        constraintSet.constrainHeight(leftTextView.getId(), 0);
        constraintSet.connect(
                leftTextView.getId(),
                ConstraintSet.BOTTOM,
                ConstraintSet.PARENT_ID,
                ConstraintSet.BOTTOM
        );
        constraintSet.connect(
                leftTextView.getId(),
                ConstraintSet.TOP,
                ConstraintSet.PARENT_ID,
                ConstraintSet.TOP
        );
        constraintSet.connect(
                leftTextView.getId(),
                ConstraintSet.START,
                leftImgView.getId(),
                ConstraintSet.END
        );
        constraintSet.setGoneMargin(leftTextView.getId(), ConstraintSet.START, Ext.dp2px(context, 12));

        // 标题左边图标的设置
        constraintSet.constrainWidth(titleLeftIconImageView.getId(), ConstraintSet.WRAP_CONTENT);
        constraintSet.constrainHeight(titleLeftIconImageView.getId(), ConstraintSet.WRAP_CONTENT);
        constraintSet.connect(
                titleLeftIconImageView.getId(),
                ConstraintSet.BOTTOM,
                ConstraintSet.PARENT_ID,
                ConstraintSet.BOTTOM
        );
        constraintSet.connect(
                titleLeftIconImageView.getId(),
                ConstraintSet.END,
                titleTextView.getId(),
                ConstraintSet.START
        );
        constraintSet.setHorizontalChainStyle(titleLeftIconImageView.getId(), ConstraintSet.CHAIN_PACKED);
        constraintSet.connect(
                titleLeftIconImageView.getId(),
                ConstraintSet.START,
                ConstraintSet.PARENT_ID,
                ConstraintSet.START
        );
        constraintSet.connect(
                titleLeftIconImageView.getId(),
                ConstraintSet.TOP,
                ConstraintSet.PARENT_ID,
                ConstraintSet.TOP
        );

        // 标题的设置
        constraintSet.constrainWidth(titleTextView.getId(), ConstraintSet.WRAP_CONTENT);
        constraintSet.constrainHeight(titleTextView.getId(), 0);
        constraintSet.connect(
                titleTextView.getId(),
                ConstraintSet.BOTTOM,
                ConstraintSet.PARENT_ID,
                ConstraintSet.BOTTOM
        );
        constraintSet.connect(
                titleTextView.getId(),
                ConstraintSet.END,
                titleRightIconImageView.getId(),
                ConstraintSet.START
        );
        constraintSet.connect(
                titleTextView.getId(),
                ConstraintSet.START,
                titleLeftIconImageView.getId(),
                ConstraintSet.END
        );
        constraintSet.connect(
                titleTextView.getId(),
                ConstraintSet.TOP,
                ConstraintSet.PARENT_ID,
                ConstraintSet.TOP
        );
        constraintSet.setMargin(titleTextView.getId(), ConstraintSet.START, Ext.dp2px(context, 5));
        constraintSet.setMargin(titleTextView.getId(), ConstraintSet.END, Ext.dp2px(context, 5));

        // 标题右边的图标的设置
        constraintSet.constrainWidth(titleRightIconImageView.getId(), ConstraintSet.WRAP_CONTENT);
        constraintSet.constrainHeight(titleRightIconImageView.getId(), ConstraintSet.WRAP_CONTENT);
        constraintSet.connect(
                titleRightIconImageView.getId(),
                ConstraintSet.BOTTOM,
                ConstraintSet.PARENT_ID,
                ConstraintSet.BOTTOM
        );
        constraintSet.connect(
                titleRightIconImageView.getId(),
                ConstraintSet.END,
                ConstraintSet.PARENT_ID,
                ConstraintSet.END
        );
        constraintSet.connect(
                titleRightIconImageView.getId(),
                ConstraintSet.START,
                titleTextView.getId(),
                ConstraintSet.END
        );
        constraintSet.connect(
                titleRightIconImageView.getId(),
                ConstraintSet.TOP,
                ConstraintSet.PARENT_ID,
                ConstraintSet.TOP
        );

        // 右边文字的设置
        constraintSet.constrainWidth(rightTextView.getId(), ConstraintSet.WRAP_CONTENT);
        constraintSet.constrainHeight(rightTextView.getId(), 0);
        constraintSet.connect(
                rightTextView.getId(),
                ConstraintSet.BOTTOM,
                ConstraintSet.PARENT_ID,
                ConstraintSet.BOTTOM
        );
        constraintSet.connect(
                rightTextView.getId(),
                ConstraintSet.END,
                rightImgView.getId(),
                ConstraintSet.START
        );
        constraintSet.connect(
                rightTextView.getId(),
                ConstraintSet.TOP,
                ConstraintSet.PARENT_ID,
                ConstraintSet.TOP
        );
        constraintSet.setGoneMargin(rightTextView.getId(), ConstraintSet.END, Ext.dp2px(context, 12));

        // 右边图片的设置
        constraintSet.constrainWidth(rightImgView.getId(), ConstraintSet.WRAP_CONTENT);
        constraintSet.constrainHeight(rightImgView.getId(), 0);
        constraintSet.connect(
                rightImgView.getId(),
                ConstraintSet.BOTTOM,
                ConstraintSet.PARENT_ID,
                ConstraintSet.BOTTOM
        );
        constraintSet.connect(
                rightImgView.getId(),
                ConstraintSet.END,
                ConstraintSet.PARENT_ID,
                ConstraintSet.END
        );
        constraintSet.connect(
                rightImgView.getId(),
                ConstraintSet.TOP,
                ConstraintSet.PARENT_ID,
                ConstraintSet.TOP
        );

        constraintSet.applyTo(contentView);
    }


    private ImageView initLeftImgView(Context context) {
        ImageView imageView = new ImageView(context);
        imageView.setId(generateViewId());
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        imageView.setPaddingRelative(Ext.dp2px(context, 12), 0, Ext.dp2px(context, 12), 0);
        imageView.setBackgroundResource(android.R.color.transparent);
        return imageView;
    }

    private TextView initLeftTextView(Context context) {
        TextView textView = new TextView(context);
        textView.setId(generateViewId());
        textView.setGravity(Gravity.CENTER);
        textView.setBackgroundResource(android.R.color.transparent);
        return textView;
    }

    private ImageView initTitleLeftIconImageView(Context context) {
        ImageView imageView = new ImageView(context);
        imageView.setId(generateViewId());
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        imageView.setBackgroundResource(android.R.color.transparent);
        return imageView;
    }

    private TextView initTitleTextView(Context context) {
        TextView textView = new TextView(context);
        textView.setMaxLines(1);
        textView.setSingleLine(true);
        textView.setMaxEms(9);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setId(generateViewId());
        textView.setGravity(Gravity.CENTER);
        textView.setBackgroundResource(android.R.color.transparent);
        return textView;
    }

    private ImageView initTitleRightIconImageView(Context context) {
        ImageView imageView = new ImageView(context);
        imageView.setId(generateViewId());
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        imageView.setBackgroundResource(android.R.color.transparent);
        return imageView;
    }

    private TextView initRightTextView(Context context) {
        TextView textView = new TextView(context);
        textView.setId(generateViewId());
        textView.setGravity(Gravity.CENTER);
        textView.setBackgroundResource(android.R.color.transparent);
        return textView;
    }

    private ImageView initRightImgView(Context context) {
        ImageView imageView = new ImageView(context);
        imageView.setId(generateViewId());
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        imageView.setPaddingRelative(Ext.dp2px(context, 12), 0, Ext.dp2px(context, 12), 0);
        imageView.setBackgroundResource(android.R.color.transparent);
        return imageView;
    }


    /**
     * 当左边图片被点击
     */
    public void onLeftImgClick(View.OnClickListener clickListener) {
        leftImgView.setOnClickListener(clickListener);
    }

    /**
     * 当左边文字被点击
     */
    public void onLeftTextClick(View.OnClickListener clickListener) {
        leftTextView.setOnClickListener(clickListener);
    }

    /**
     * 当标题被点击
     */
    public void onTitleClick(View.OnClickListener clickListener) {
        titleTextView.setOnClickListener(clickListener);
    }

    /**
     * 当标题左边Icon被点击
     */
    public void onTitleLeftIconClick(View.OnClickListener clickListener) {
        titleLeftIconImageView.setOnClickListener(clickListener);
    }

    /**
     * 当标题右边Icon被点击
     */
    public void onTitleRightIconClick(View.OnClickListener clickListener) {
        titleRightIconImageView.setOnClickListener(clickListener);
    }

    /**
     * 当右边文字被点击
     */
    public void onRightTextClick(View.OnClickListener clickListener) {
        rightTextView.setOnClickListener(clickListener);
    }

    /**
     * 当右边图片被点击
     */
    public void onRightImgClick(View.OnClickListener clickListener) {
        rightImgView.setOnClickListener(clickListener);
    }

    /**
     * 设置标题
     */
    public void setTitleText(String title) {
        titleTextView.setText(title);
    }

    /**
     * 左边文字
     */
    public void setLeftText(String text) {
        leftTextView.setText(text);
    }

    /**
     * 右边文字
     */
    public void setRightText(String text) {
        rightTextView.setText(text);
    }
}
