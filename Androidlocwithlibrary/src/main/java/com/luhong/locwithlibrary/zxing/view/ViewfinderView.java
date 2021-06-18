/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.luhong.locwithlibrary.zxing.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.google.zxing.ResultPoint;
import com.luhong.locwithlibrary.R;
import com.luhong.locwithlibrary.zxing.camera.CameraManager;

import java.util.Collection;
import java.util.HashSet;

/**
 * This view is overlaid on top of the camera preview. It adds the viewfinder rectangle and partial
 * transparency outside it, as well as the laser scanner animation and result points.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class ViewfinderView extends View {

    private static final int[] SCANNER_ALPHA = {0, 64, 128, 192, 255, 192, 128, 64};
    private static final long ANIMATION_DELAY = 10L;//刷新时间
    private static final int OPAQUE = 0xFF;

    private int distance = 10;// 调取景框大小
    private int MIDDLE_LINE_WIDTH = 6; // 扫描框中的中间线的宽度
    private int MIDDLE_LINE_PADDING = 10 + distance; // 扫描框中的中间线的与扫描框左右的间隙
    private int move_distance = 6; // 中间那条线每次刷新移动的距离
    private int slideTop; // 中间滑动线的最顶端位置
    private int slideBottom; // 中间滑动线的最底端位置
    boolean isFirst;

    private final Paint paint;
    private Bitmap resultBitmap;
    private final int maskColor;
    private final int resultColor;
    private final int frameColor;
    //  private final int laserColor;
    private final int resultPointColor;
    //  private int scannerAlpha;
    private Collection<ResultPoint> possibleResultPoints;
    private Collection<ResultPoint> lastPossibleResultPoints;

    // This constructor is used when the class is built from an XML resource.
    public ViewfinderView(Context context, AttributeSet attrs) {
        super(context, attrs);

//     Initialize these once for performance rather than calling them every time in onDraw().
        paint = new Paint();
        Resources resources = getResources();
        maskColor = resources.getColor(R.color.viewfinder_mask);
        resultColor = resources.getColor(R.color.result_view);
        frameColor = resources.getColor(R.color.theme_color);
//    laserColor = resources.getColor(R.color.yellow);
        resultPointColor = resources.getColor(R.color.theme_color);
//    scannerAlpha = 0;
        possibleResultPoints = new HashSet<ResultPoint>(5);
    }

    @Override
    public void onDraw(Canvas canvas) {
        Rect frame = CameraManager.get().getFramingRect();
        if (frame == null) {
            return;
        }
        // 初始化中间线滑动的最上边和最下边
        if (!isFirst) {
            isFirst = true;
            slideTop = frame.top + distance + 10;
            slideBottom = frame.bottom - distance;
        }
        // 获取屏幕的宽和高
        int width = canvas.getWidth();
        int height = canvas.getHeight();

        // 灰色半透明Draw the exterior (i.e. outside the framing rect) darkened
        paint.setColor(resultBitmap != null ? resultColor : maskColor);
        canvas.drawRect(0, 0, width, frame.top + distance, paint);//顶
        canvas.drawRect(0, frame.top + distance, frame.left + distance, frame.bottom + 1 - distance, paint);//左
        canvas.drawRect(frame.right + 1 - distance, frame.top + distance, width, frame.bottom + 1 - distance, paint);//右
        canvas.drawRect(0, frame.bottom + 1 - distance, width, height, paint);//底

        if (resultBitmap != null) {
            // Draw the opaque result bitmap over the scanning rectangle
            paint.setAlpha(OPAQUE); //设置 扫描框的外部是否显示扫描的背景
            canvas.drawBitmap(resultBitmap, frame.left, frame.top, paint);
        } else {
            int linewidht = 10;//四个角画笔宽度
            // Draw a two pixel solid black border inside the framing rect  2个像素的黑边框
            paint.setColor(frameColor);
            //左上
            canvas.drawRect(distance + frame.left, distance + frame.top, distance + (linewidht + frame.left), distance + (50 + frame.top), paint);
            canvas.drawRect(distance + frame.left, distance + frame.top, distance + (50 + frame.left), distance + (linewidht + frame.top), paint);
            //右上
            canvas.drawRect(-distance + ((0 - linewidht) + frame.right), distance + frame.top, -distance + (1 + frame.right), distance + (50 + frame.top), paint);
            canvas.drawRect(-distance + (-50 + frame.right), distance + frame.top, -distance + frame.right, distance + (linewidht + frame.top), paint);
            //左下
            canvas.drawRect(distance + frame.left, -distance + (-49 + frame.bottom), distance + (linewidht + frame.left), -distance + (1 + frame.bottom), paint);
            canvas.drawRect(distance + frame.left, -distance + ((0 - linewidht) + frame.bottom), distance + (50 + frame.left), -distance + (1 + frame.bottom), paint);
            //右下
            canvas.drawRect(-distance + ((0 - linewidht) + frame.right), -distance + (-49 + frame.bottom), -distance + (1 + frame.right), -distance + (1 + frame.bottom), paint);
            canvas.drawRect(-distance + (-50 + frame.right), -distance + ((0 - linewidht) + frame.bottom), -distance + frame.right, -distance + (linewidht - (linewidht - 1) + frame.bottom), paint);

            // Draw a red "laser scanner" line through the middle to show decoding is active
//            paint.setColor(laserColor);
            //绘制中间的线,每次刷新界面，中间的线往下移动SPEEN_DISTANCE
            paint.setColor(getResources().getColor(R.color.theme_color));
            slideTop += move_distance;
            if (slideTop >= (frame.bottom - distance)) {
                slideTop = frame.top - distance + 30;
            }
            canvas.drawRect(frame.left + MIDDLE_LINE_PADDING, slideTop - MIDDLE_LINE_WIDTH / 2, frame.right - MIDDLE_LINE_PADDING, slideTop + MIDDLE_LINE_WIDTH / 2, paint);


//      paint.setAlpha(SCANNER_ALPHA[scannerAlpha]);
//      scannerAlpha = (scannerAlpha + 1) % SCANNER_ALPHA.length;
//      int middle = frame.height() / 2 + frame.top;
//      canvas.drawRect(frame.left + 2, middle - 1, frame.right - 1, middle + 2, paint);

            Collection<ResultPoint> currentPossible = possibleResultPoints;
            Collection<ResultPoint> currentLast = lastPossibleResultPoints;
            if (currentPossible.isEmpty()) {
                lastPossibleResultPoints = null;
            } else {
                possibleResultPoints = new HashSet<ResultPoint>(5);
                lastPossibleResultPoints = currentPossible;
                paint.setAlpha(OPAQUE);
                paint.setColor(resultPointColor);
                for (ResultPoint point : currentPossible) {
                    canvas.drawCircle(frame.left + point.getX(), frame.top + point.getY(), 6.0f, paint);
                }
            }
            if (currentLast != null) {
                paint.setAlpha(OPAQUE / 2);
                paint.setColor(resultPointColor);
                for (ResultPoint point : currentLast) {
                    canvas.drawCircle(frame.left + point.getX(), frame.top + point.getY(), 3.0f, paint);
                }
            }

            // Request another update at the animation interval, but only repaint the laser line, not the entire viewfinder mask.
            postInvalidateDelayed(ANIMATION_DELAY, frame.left, frame.top, frame.right, frame.bottom);
        }
    }

    public void drawViewfinder() {
        resultBitmap = null;
        invalidate();
    }

    /**
     * Draw a bitmap with the result points highlighted instead of the live scanning display.
     *
     * @param barcode An image of the decoded barcode.
     */
    public void drawResultBitmap(Bitmap barcode) {
        resultBitmap = barcode;
        invalidate();
    }

    public void addPossibleResultPoint(ResultPoint point) {
        possibleResultPoints.add(point);
    }

}
