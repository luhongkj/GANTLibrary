package com.luhong.locwithlibrary.zxing.encode;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.luhong.locwithlibrary.utils.Logger;

import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @类功能说明: 生成二维码图片示例
 */
public class QRCodeUtil {
    private static Bitmap bitmap;

    /**
     * @param content    二维码内容
     * @param QR_WIDTH   宽度
     * @param QR_HEIGHT  高度
     * @param logoBitmap logo
     * @param filePath   二维码保存路径
     * @方法功能说明: 生成二维码图片
     */
    public static Bitmap createQRImage(String content, int QR_WIDTH, int QR_HEIGHT, Bitmap logoBitmap, String filePath) {
        try {
            if (content == null || "".equals(content) || content.length() < 1) {
                return null;
            }
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);// 指定纠错等级
            hints.put(EncodeHintType.MARGIN, 0);//设置二维码边的空度，非负数
            //图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
            int[] pixels = new int[bitMatrix.getWidth() * bitMatrix.getHeight()];
            //下面这里按照二维码的算法，逐个生成二维码的图片，
            //两个for循环是图片横列扫描的结果
            for (int y = 0; y < bitMatrix.getHeight(); y++) {
                for (int x = 0; x < bitMatrix.getWidth(); x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * bitMatrix.getWidth() + x] = 0xff000000;
                    } else {
                        pixels[y * bitMatrix.getWidth() + x] = 0xffffffff;
                    }
                }
            }
            //生成二维码图片的格式，使用ARGB_8888
            bitmap = Bitmap.createBitmap(bitMatrix.getWidth(), bitMatrix.getHeight(), Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, bitMatrix.getWidth(), 0, 0, bitMatrix.getWidth(), bitMatrix.getHeight());
            if (logoBitmap != null) {
                bitmap = addLogo(bitmap, logoBitmap);
            }

            if (filePath == null) {
                return bitmap;
            }

            // 必须使用compress方法将bitmap保存到文件中再进行读取。直接返回的bitmap是没有任何压缩的，内存消耗巨大！
            if (bitmap != null && bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(filePath)))
                return BitmapFactory.decodeFile(filePath);

        } catch (Exception e) {
            Logger.error("生存二维码出错=" + e);
        }
        return null;
    }

    /**
     * 在二维码中间添加Logo图案
     */
    private static Bitmap addLogo(Bitmap src, Bitmap logo) {
        if (src == null) {
            return null;
        }
        if (logo == null) {
            return src;
        }
        // 获取图片的宽高
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();
        int logoWidth = logo.getWidth();
        int logoHeight = logo.getHeight();

        if (srcWidth == 0 || srcHeight == 0) {
            return null;
        }
        if (logoWidth == 0 || logoHeight == 0) {
            return src;
        }

        // logo大小为二维码整体大小的1/5
        float scaleFactor = srcWidth * 1.0f / 5 / logoWidth;
        Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
        try {
            Canvas canvas = new Canvas(bitmap);
            canvas.drawBitmap(src, 0, 0, null);
            canvas.scale(scaleFactor, scaleFactor, srcWidth / 2, srcHeight / 2);
            canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2, (srcHeight - logoHeight) / 2, null);

            canvas.save(/*Canvas.ALL_SAVE_FLAG*/);
            canvas.restore();
        } catch (Exception e) {
            bitmap = null;
            e.getStackTrace();
        }

        return bitmap;
    }

}
