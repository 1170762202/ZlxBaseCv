package com.example.zlx.zlxbasecv;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by Zlx on 2017/3/1.
 */
public class DensityUtil {
    private static float density = -1F;
    private static int widthPixels = -1;
    private static int heightPixels = -1;

    private DensityUtil() {
    }
    /**
     * 测量 View
     *
     * @param measureSpec
     * @param defaultSize View 的默认大小
     * @return
     */
    public static int measure(int measureSpec, int defaultSize) {
        int result = defaultSize;
        int specMode = View.MeasureSpec.getMode(measureSpec);
        int specSize = View.MeasureSpec.getSize(measureSpec);

        if (specMode == View.MeasureSpec.EXACTLY) {
            result = specSize;
        } else if (specMode == View.MeasureSpec.AT_MOST) {
            result = Math.min(result, specSize);
        }
        return result;
    }
    /**
     * 反转数组
     *
     * @param arrays
     * @param <T>
     * @return
     */
    public static <T> T[] reverse(T[] arrays) {
        if (arrays == null) {
            return null;
        }
        int length = arrays.length;
        for (int i = 0; i < length / 2; i++) {
            T t = arrays[i];
            arrays[i] = arrays[length - i - 1];
            arrays[length - i - 1] = t;
        }
        return arrays;
    }

    /**
     * 测量文字高度
     * @param paint
     * @return
     */
    public static float measureTextHeight(Paint paint) {
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        return (Math.abs(fontMetrics.ascent) - fontMetrics.descent);
    }
    public static float getDensity(Context context) {
        if (density <= 0F) {
            density = context.getResources().getDisplayMetrics().density;
        }
        return density;
    }
    public static int dip2px(Context context, float dpValue) {
        return (int) (dpValue * getDensity(context) + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        return (int) (pxValue / getDensity(context) + 0.5f);
    }
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }
}
