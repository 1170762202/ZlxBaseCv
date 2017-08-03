package com.example.zlx.zlxbasecv.custom_view.peiview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.example.zlx.zlxbasecv.PieData;

import java.util.List;

/**
 * Created by zlx on 2017/7/19.
 */

public class PieView extends View {
    // 颜色表(注意: 此处定义颜色使用的是ARGB，带Alpha通道的)
    private int[] mColors = {0xFFCCFF00, 0xFF6495ED, 0xFFE32636, 0xFF800000, 0xFF808000, 0xFFFF8C69, 0xFF808080,
            0xFFE6B800, 0xFF7CFC00};
    private Paint paint;
    private int startAngle = 0;
    private int currentAngle = 0;

    private int width;
    private int height;

    private List<PieData> datas;
    private RectF rectF;

    private float radius;

    public PieView(Context context) {
        super(context, null);
    }

    public PieView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        radius = (float) (Math.min(width, height) / 2 * 0.8);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (datas == null) {
            return;
        }
        currentAngle = startAngle;
        rectF = new RectF(0, 0, width, height);

        for (int i = 0; i < datas.size(); i++) {
            PieData data = datas.get(i);
            paint.setColor(data.getColor());
            canvas.drawArc(rectF, currentAngle, data.getAngle(), true, paint);
            currentAngle += data.getAngle();
        }
    }

    public void setDatas(List<PieData> datas) {
        this.datas = datas;
        initDatas();
        invalidate();
    }

    public void setStartAngle(int startAngle) {
        this.startAngle = startAngle;
        invalidate();
    }

    private void initDatas() {
        if (datas == null || datas.size() <= 0) {
            return;
        }

        float sumValue = 0;
        for (int i = 0; i < datas.size(); i++) {
            datas.get(i).setColor(mColors[i % mColors.length]);
            sumValue += datas.get(i).getValue();
        }

        float sumAngle = 0;
        for (int i = 0; i < datas.size(); i++) {
            PieData data = datas.get(i);
            float percentAge = data.getValue() / sumValue;
            float angle = percentAge * 360;
            data.setAngle(angle);
            data.setPercentage(percentAge);
            sumAngle += angle;
        }
    }
}
