package com.hhhhhx.autotouch.activity;

import android.graphics.Bitmap;
import android.media.Image;
import android.media.ImageReader;
import android.util.Log;

import com.hhhhhx.autotouch.bean.Color;
import java.nio.ByteBuffer;
import me.jessyan.autosize.utils.LogUtils;

//根据坐标获取颜色
public class GBData {
    private static final String TAG = "GBData";
    public static ImageReader reader;
    private static Bitmap bitmap;

    /**
     * 获取目标点的RGB值
     */
    public static int getColor(int x, int y) {
        if (reader == null) {
            LogUtils.w("reader is null");
            return -1;
        }
        Image image = reader.acquireLatestImage();

        if (image == null) {
            if (bitmap == null) {
                LogUtils.w("image is null");
                return -1;
            }
            return bitmap.getPixel(x, y);
        }
        int width = image.getWidth();
        int height = image.getHeight();
        final Image.Plane[] planes = image.getPlanes();
        final ByteBuffer buffer = planes[0].getBuffer();
        int pixelStride = planes[0].getPixelStride();
        int rowStride = planes[0].getRowStride();
        int rowPadding = rowStride - pixelStride * width;
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height, Bitmap.Config.ARGB_8888);
        }
        bitmap.copyPixelsFromBuffer(buffer);
        image.close();
        return bitmap.getPixel(x, y);
    }

    public static boolean isSameColor(Color color) {
        int color1 = getColor(color.getX(), color.getY());
        int color2 = color.getColor();
        Log.d(TAG, "----颜色比较: ("+color2+" -> "+color1+")");
        return color1 == color2;
    }

    public static void close() {
        reader.close();
    }
}
