package com.luhong.locwithlibrary.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.luhong.locwithlibrary.R;
import com.luhong.locwithlibrary.R2;
import com.luhong.locwithlibrary.base.BaseActivity;
import com.luhong.locwithlibrary.imagepicker.ImagePicker;
import com.luhong.locwithlibrary.listener.GlideLoader;
import com.luhong.locwithlibrary.listener.SingleClickListener;
import com.luhong.locwithlibrary.utils.FileUtils;
import com.luhong.locwithlibrary.utils.Logger;
import com.luhong.locwithlibrary.utils.ResUtils;
import com.luhong.locwithlibrary.utils.ToastUtil;
import com.luhong.locwithlibrary.zxing.camera.CameraManager;
import com.luhong.locwithlibrary.zxing.camera.PlanarYUVLuminanceSource;
import com.luhong.locwithlibrary.zxing.decoding.CaptureActivityHandler;
import com.luhong.locwithlibrary.zxing.decoding.InactivityTimer;
import com.luhong.locwithlibrary.zxing.decoding.RGBLuminanceSource;
import com.luhong.locwithlibrary.zxing.view.ViewfinderView;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import butterknife.BindView;

/**
 * 二维码扫码界面
 *
 * @author Baozi
 */
public class CaptureActivity extends BaseActivity implements SurfaceHolder.Callback {
    public static final String dataTypeKey = "dataTypeKey";
    public static final int resultDecode = 300;
    private static final int REQUEST_SELECT_IMAGES_CODE = 0x01;
    @BindView(R2.id.surfaceView_zxing)
    SurfaceView surfaceView;
    @BindView(R2.id.viewfinderView_zxing)
    ViewfinderView viewfinderView;
    @BindView(R2.id.tv_scannerPhoto_zxing)
    TextView tv_scannerPhoto;
    @BindView(R2.id.tv_onLight_zxing)
    TextView tv_onLight;

    private CaptureActivityHandler handler;
    private Vector<BarcodeFormat> decodeFormats;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private final float BEEP_VOLUME = 0.10f;
    private String characterSet;
    private boolean isLight = true, hasSurface, playBeep, vibrate;
    private Bitmap scanBitmap;
    private ArrayList<String> mImagePaths;

    @Override
    protected int initLayoutId() {
        return R.layout.activity_capture_zxing;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        initTitleView(true, "扫码", "相册", new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                selectPhoto(CaptureActivity.this);
            }
        });
    }

    @Override
    protected void initData() {
        CameraManager.init(getApplication());// 初始化 CameraManager
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
    }

    @Override
    protected void onEventListener() {
        tv_scannerPhoto.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                selectPhoto(CaptureActivity.this);
            }
        });
        tv_onLight.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                light();
            }
        });
    }

    protected void light() {
        if (isLight == true) {
            isLight = false;
            CameraManager.get().openLight(); // 开闪光灯
            tv_onLight.setText("轻触关闭");
            tv_onLight.setTextColor(mActivity.getResources().getColor(R.color.white));
            tv_onLight.setCompoundDrawablesRelativeWithIntrinsicBounds(null, ResUtils.resToDrawable(mActivity, R.mipmap.qr_light_off), null, null);
        } else {
            isLight = true;
            CameraManager.get().offLight(); // 关闪光灯
            tv_onLight.setText("轻触照亮");
            tv_onLight.setTextColor(mActivity.getResources().getColor(R.color.theme_color));
            tv_onLight.setCompoundDrawablesRelativeWithIntrinsicBounds(null, ResUtils.resToDrawable(mActivity, R.mipmap.qr_light_on), null, null);
        }
    }


    public static void selectPhoto(Activity context) {//9 maxImgCount - selImageList.size()
        ArrayList<String> array = new ArrayList<>();
        array.add(FileUtils.getRootPicDir());
        ImagePicker.getInstance()
                .setTitle("选择照片")//设置标题
                .showCamera(true)//设置是否显示拍照按钮
                .showImage(true)//设置是否展示图片
                .showVideo(false)//设置是否展示视频
                .filterGif(false)//设置是否过滤gif图片
                .setMaxCount(1)//设置最大选择图片数目(默认为1，单选)
                .setSingleType(true)//设置图片视频不能同时选择
                .setIsFiltration(FileUtils.getPicDir())
//                .setImagePaths(mImagePaths)//设置历史选择记录
                .setImageLoader(new GlideLoader())//设置自定义图片加载器
                .start(context, REQUEST_SELECT_IMAGES_CODE);//REQEST_SELECT_IMAGES_CODE为Intent调用的requestCode
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_OK:
                switch (requestCode) {
                    case REQUEST_SELECT_IMAGES_CODE:
                        mImagePaths = data.getStringArrayListExtra(ImagePicker.EXTRA_SELECT_IMAGES);
                        if (mImagePaths != null && mImagePaths.size() != 0) {
                            String photoPath = mImagePaths.get(0);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Result result = scanningImage(photoPath);
                                    if (result == null) {
                                        Looper.prepare();
                                        ToastUtil.show("图片格式有误");
                                        Looper.loop();
                                    } else {// 数据返回
                                        String recode = recode(result.toString());
                                        Logger.error("二维码识别结果=" + recode);
                                        Intent data = new Intent();
                                        data.putExtra(dataTypeKey, recode);
                                        setResult(resultDecode, data);
                                        finish();
                                    }
                                }
                            }).start();
                        }
                        break;
                }
                break;
        }
    }

    // TODO: 解析部分图片
    protected Result scanningImage(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        // DecodeHintType 和EncodeHintType
        Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
        hints.put(DecodeHintType.CHARACTER_SET, "utf-8"); // 设置二维码内容的编码
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // 先获取原大小
        scanBitmap = BitmapFactory.decodeFile(path, options);
        options.inJustDecodeBounds = false; // 获取新的大小

        int sampleSize = (int) (options.outHeight / (float) 200);
        if (sampleSize <= 0)
            sampleSize = 1;
        options.inSampleSize = sampleSize;
        scanBitmap = BitmapFactory.decodeFile(path, options);
        if(scanBitmap==null){
            return null;
        }
        // --------------测试的解析方法---PlanarYUVLuminanceSource-这几行代码对project没作功----------
        LuminanceSource source1 = new PlanarYUVLuminanceSource(rgb2YUV(scanBitmap), scanBitmap.getWidth(), scanBitmap.getHeight(), 0, 0, scanBitmap.getWidth(), scanBitmap.getHeight()/*, false*/);
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(source1));
        MultiFormatReader reader1 = new MultiFormatReader();
        Result result1;
        try {
            result1 = reader1.decode(binaryBitmap);
            String content = result1.getText();
            Log.i("123content", content);
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        // ----------------------------
        RGBLuminanceSource source = new RGBLuminanceSource(scanBitmap);
        BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
        QRCodeReader reader = new QRCodeReader();
        try {
            return reader.decode(bitmap1, hints);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;
        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        if (inactivityTimer != null) inactivityTimer.shutdown();
        super.onDestroy();
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;

    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();
    }

    public void handleDecode(final Result result, Bitmap barcode) {
        if (inactivityTimer != null) inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        String recode = recode(result.toString());
        // 数据返回
        Intent data = new Intent();
        data.putExtra(dataTypeKey, recode);
        setResult(resultDecode, data);
        finish();
    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it too loud, so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final OnCompletionListener beepListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

    /**
     * 中文乱码
     * <p>
     * 暂时解决大部分的中文乱码 但是还有部分的乱码无法解决 .
     * <p>
     * 如果您有好的解决方式 请联系 2221673069@qq.com
     * <p>
     * 我会很乐意向您请教 谢谢您
     * @return
     */
    private String recode(String str) {
        String format = "";
        try {
            boolean ISO = Charset.forName("ISO-8859-1").newEncoder().canEncode(str);
            if (ISO) {
                format = new String(str.getBytes("ISO-8859-1"), "GB2312");
                Log.i("1234      ISO8859-1", format);
            } else {
                format = str;
                Log.i("1234      stringExtra", str);
            }
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return format;
    }

    /**
     * //TODO: TAOTAO 将bitmap由RGB转换为YUV //TOOD: 研究中
     * @param bitmap 转换的图形
     * @return YUV数据
     */
    public byte[] rgb2YUV(Bitmap bitmap) {
        // 该方法来自QQ空间
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        int len = width * height;
        byte[] yuv = new byte[len * 3 / 2];
        int y, u, v;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int rgb = pixels[i * width + j] & 0x00FFFFFF;
                int r = rgb & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = (rgb >> 16) & 0xFF;
                y = ((66 * r + 129 * g + 25 * b + 128) >> 8) + 16;
                u = ((-38 * r - 74 * g + 112 * b + 128) >> 8) + 128;
                v = ((112 * r - 94 * g - 18 * b + 128) >> 8) + 128;
                y = y < 16 ? 16 : (y > 255 ? 255 : y);
                u = u < 0 ? 0 : (u > 255 ? 255 : u);
                v = v < 0 ? 0 : (v > 255 ? 255 : v);
                yuv[i * width + j] = (byte) y;
                // yuv[len + (i >> 1) * width + (j & ~1) + 0] = (byte) u;
                // yuv[len + (i >> 1) * width + (j & ~1) + 1] = (byte) v;
            }
        }
        return yuv;
    }
}