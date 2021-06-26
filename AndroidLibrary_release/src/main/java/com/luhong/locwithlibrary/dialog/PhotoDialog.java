package com.luhong.locwithlibrary.dialog;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.luhong.locwithlibrary.R;
import com.luhong.locwithlibrary.R2;
import com.luhong.locwithlibrary.base.BaseActivity;
import com.luhong.locwithlibrary.imagepicker.ImagePicker;
import com.luhong.locwithlibrary.listener.GlideLoader;
import com.luhong.locwithlibrary.listener.SingleClickListener;
import com.luhong.locwithlibrary.utils.ToastUtil;
import com.zyq.easypermission.EasyPermissionResult;

import java.util.List;

import butterknife.BindView;

/**
 * 相册/照相
 * Created by ITMG on 2019/9/30 0030.
 */
public class PhotoDialog extends BaseDialog {
    public final static int REQ_CODE_GALLERY = 100;//本地图库
    public final static int REQ_CODE_CAMERA = 203;//相机
    @BindView(R2.id.v_titleLine_photoDialog)
    View v_titleLine;
    @BindView(R2.id.tv_title_photoDialog)
    TextView tv_title;
    @BindView(R2.id.tv_camera_photoDialog)
    TextView tv_camera;
    @BindView(R2.id.tv_photo_photoDialog)
    TextView tv_photo;
    @BindView(R2.id.tv_cancel_photoDialog)
    TextView tv_cancel;
    private BaseActivity mContext;
    private String title, topText, bottomText;
    private IPhotoListener photoListener;

    public static PhotoDialog getInstance(BaseActivity context) {
        return new PhotoDialog(context);
    }

    public void showDialog(IPhotoListener photoListener) {
        this.photoListener = photoListener;
        show();
    }

    public void showDialog(String title, String topText, String bottomText, IPhotoListener photoListener) {
        this.title = title;
        this.topText = topText;
        this.bottomText = bottomText;
        this.photoListener = photoListener;
        show();
    }

    public PhotoDialog(BaseActivity context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected int initLayoutId() {
        return R.layout.dialog_photo_view;
    }

    @Override
    protected int setGravity() {
        return Gravity.BOTTOM;
    }

    @Override
    protected void initAlertDialogView(Window window) {
        // TODO Auto-generated method stub
        if (!TextUtils.isEmpty(title)) {
            tv_title.setText(title);
        }
        if (!TextUtils.isEmpty(topText)) {
            tv_camera.setText(topText);
        }
        if (!TextUtils.isEmpty(bottomText)) {
            tv_photo.setText(bottomText);
        }
    }

    @Override
    protected void onEventListener() {
        // TODO Auto-generated method stub
        tv_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mContext.requestStoragePermissions(new EasyPermissionResult() {
                    @Override
                    public void onPermissionsAccess(int requestCode) {
                        super.onPermissionsAccess(requestCode);
                        photoListener.onPhotoCallback();
                        cancel();
                    }

                    @Override
                    public void onPermissionsDismiss(int requestCode, @NonNull List<String> permissions) {
                        super.onPermissionsDismiss(requestCode, permissions);
                        ToastUtil.show("没有权限");
                        cancel();
                    }
                });
            }
        });
        tv_camera.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                // TODO Auto-generated method stub
                mContext.requestStorageAndCameraPermissions(new EasyPermissionResult() {
                    @Override
                    public void onPermissionsAccess(int requestCode) {
                        super.onPermissionsAccess(requestCode);
                        photoListener.onCameraCallback();
                        cancel();
                    }

                    @Override
                    public void onPermissionsDismiss(int requestCode, @NonNull List<String> permissions) {
                        super.onPermissionsDismiss(requestCode, permissions);
                        ToastUtil.show("没有权限");
                        cancel();
                    }
                });
            }
        });
        tv_cancel.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                // TODO Auto-generated method stub
                cancel();
            }
        });
    }

    /**
     * 相机
     */
    public static void takePhoto(Activity context, Uri uri) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        context.startActivityForResult(intent, REQ_CODE_CAMERA);
    }

    public static void selectPhoto(Activity context) {
        selectPhoto(context, 1);
    }

    //选择图片
    public static void selectPhoto(Activity context, int maxCount) {//9 maxImgCount - selImageList.size()
        ImagePicker.getInstance()
                .setTitle("选择照片")//设置标题
                .showCamera(true)//设置是否显示拍照按钮
                .showImage(true)//设置是否展示图片
                .setIsFiltration("")//设置图片路径
                .showVideo(false)//设置是否展示视频
                .filterGif(false)//设置是否过滤gif图片
                .setMaxCount(maxCount)//设置最大选择图片数目(默认为1，单选)
                .setSingleType(true)//设置图片视频不能同时选择
//                .setImagePaths(mImagePaths)//设置历史选择记录
                .setImageLoader(new GlideLoader())//设置自定义图片加载器
                .start(context, REQ_CODE_GALLERY);//REQEST_SELECT_IMAGES_CODE为Intent调用的requestCode
    }

    public interface IPhotoListener {
        void onPhotoCallback();//相册
        void onCameraCallback();//拍照
    }
}
