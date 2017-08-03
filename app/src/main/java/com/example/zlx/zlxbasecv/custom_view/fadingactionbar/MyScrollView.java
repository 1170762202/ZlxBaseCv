package com.example.zlx.zlxbasecv.custom_view.fadingactionbar;/**
 * Created by Zlx on 2017/2/21.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by Zlx on 2017/2/21.
 */
public class MyScrollView extends ScrollView {

    public MyScrollView(Context context) {
        this(context, null);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (onScrollChangeListener != null) {
            onScrollChangeListener.ScrollChangeListener(l, t, oldl, oldt);
        }
        if (translucentListener != null) {
            translucentListener.onTranslucent(getAlpha());
        }
    }

    public float getAlpha() {
        // alpha = 滑出去的高度/(screenHeight/3);
        float heightPixels = getContext().getResources().getDisplayMetrics().heightPixels;
        float scrollY = getScrollY();//该值 大于0
        float alpha = 1 - scrollY / (heightPixels / 5);// 0~1  透明度是1~0
        //这里选择的screenHeight的1/3 是alpha改变的速率 （根据你的需要你可以自己定义）
        return alpha;
    }

    private TranslucentListener translucentListener = null;

    public void setTranslucentListener(TranslucentListener translucentListener) {
        this.translucentListener = translucentListener;
    }

    private OnScrollChangeListener onScrollChangeListener = null;

    public void setOnScrollChangeListener(OnScrollChangeListener onScrollChangeListener) {
        this.onScrollChangeListener = onScrollChangeListener;
    }

    public interface OnScrollChangeListener {
        void ScrollChangeListener(int l, int t, int oldl, int oldt);
    }

    public interface TranslucentListener {


        /**
         * 透明度的回调
         *
         * @param alpha
         */
        public void onTranslucent(float alpha);
    }
}
