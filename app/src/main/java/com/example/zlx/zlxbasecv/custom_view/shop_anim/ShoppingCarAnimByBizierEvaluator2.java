package com.example.zlx.zlxbasecv.custom_view.shop_anim;/**
 * Created by Zlx on 2017/2/14.
 */

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Point;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by Zlx on 2017/2/14.
 */
public class ShoppingCarAnimByBizierEvaluator2 {

    private Activity activity;

    protected int anim_drawable;

    public ShoppingCarAnimByBizierEvaluator2() {
    }

    public ShoppingCarAnimByBizierEvaluator2(Activity activity) {
        this.activity = activity;
    }

    public void startAnimation(View startView, View endView) {

        if (anim_drawable != 0) {
            final ImageView mImg = new ImageView(activity);
            mImg.setImageResource(anim_drawable);
            mImg.setScaleType(ImageView.ScaleType.MATRIX);
            ViewGroup rootView = (ViewGroup) activity.getWindow().getDecorView();
            rootView.addView(mImg);

            int[] position = new int[2];
            startView.getLocationInWindow(position);

            int[] des = new int[2];
            endView.getLocationInWindow(des);

        /*动画开始位置，也就是物品的位置;动画结束的位置，也就是购物车的位置 */
            Point startPosition = new Point(position[0], position[1]);
//        Point endPosition = new Point(des[0] + idEnd.getWidth() / 2, des[1] + idEnd.getHeight() / 2);
            Point endPosition = new Point(des[0] + endView.getWidth() / 2, des[1] + endView.getHeight() / 2);

            int pointX = (startPosition.x + endPosition.x) / 2 - 100;
            int pointY = startPosition.y - 200;
            Point controllPoint = new Point(pointX, pointY);

        /*
        * 属性动画，依靠TypeEvaluator来实现动画效果，其中位移，缩放，渐变，旋转都是可以直接使用
        * 这里是自定义了TypeEvaluator， 我们通过point记录运动的轨迹，然后，物品随着轨迹运动，
        * 一旦轨迹发生变化，就会调用addUpdateListener这个方法，我们不断的获取新的位置，是物品移动
        * */
            ValueAnimator valueAnimator = ValueAnimator.ofObject(new BizierEvaluator2(controllPoint), startPosition, endPosition);
            valueAnimator.start();
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    Point point = (Point) valueAnimator.getAnimatedValue();
                    mImg.setX(point.x);
                    mImg.setY(point.y);
                }
            });

            /**
             * 动画结束，移除掉小圆圈
             */
            valueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    ViewGroup rootView = (ViewGroup) activity.getWindow().getDecorView();
                    rootView.removeView(mImg);
                }
            });
        } else {
            throw new IllegalArgumentException("anim_drawable is null");
        }
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public int getAnim_drawable() {
        return anim_drawable;
    }

    public void setAnim_drawable(int anim_drawable) {
        this.anim_drawable = anim_drawable;
    }
}
