package com.example.zlx.zlxbasecv.custom_view.waveview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import com.example.zlx.zlxbasecv.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Zlx on 2017/4/6.
 */
public class LX_WaveView extends View {

    private int mViewHeight;
    private int mViewWidth;

    private int oneWaveMoveLength;

    private int waveWidth;
    private int waveHeight;

    private int levelLine;
    private int progress = 0;

    private Paint oneOrbitPaint;
    private int oneOrbitColor = Color.GREEN;
    private Path oneOrbitPath;

    private Paint circlePaint;
    private int circleColor = Color.WHITE;

    private Paint textPaint;
    private int normalTextColor = Color.RED;
    private int overTextColor = Color.WHITE;

    private Paint borderPaint;
    private int borderColor = Color.RED;
    private float borderWidth = 4;

    private List<Point> onePointList;
    private float textSize = 48;

    private float move_distance = 5;
    private float two_move_instance = 8;

    private int speed = 10;

    private boolean isStart;
    private boolean isCircle;
    /**
     * 圆环区域
     */
    private RectF cirlceRectF;
    private int circleStrokeWidth;//圆线宽

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            initWaveMove();
        }
    };

    private void initWaveMove() {
        oneWaveMoveLength += move_distance;
        if (oneWaveMoveLength >= waveWidth) {
            oneWaveMoveLength = 0;
        }
        if (two_move_instance >= waveWidth) {
        }
        invalidate();
    }

    public LX_WaveView(Context context) {
        super(context, null);
    }

    public LX_WaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttr(context, attrs);
        init();
    }

    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LX_WaveView);
        int n = ta.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = ta.getIndex(i);
            switch (attr) {
                case R.styleable.LX_WaveView_is_circle:
                    isCircle = ta.getBoolean(attr, true);
                    break;
                case R.styleable.LX_WaveView_borderColor:
                    borderColor = ta.getColor(attr, Color.RED);
                    break;
                case R.styleable.LX_WaveView_borderWidth:
                    borderWidth = ta.getDimension(attr, 2.0f);
                    break;
                case R.styleable.LX_WaveView_first_wave_color:
                    oneOrbitColor = ta.getColor(attr, Color.GREEN);
                    break;
                case R.styleable.LX_WaveView_move_distance:
                    move_distance = ta.getDimension(attr, 10);
                    break;
                case R.styleable.LX_WaveView_normalPercentColor:
                    normalTextColor = ta.getColor(attr, Color.GREEN);
                    break;
                case R.styleable.LX_WaveView_overPercentColor:
                    normalTextColor = ta.getColor(attr, Color.WHITE);
                    break;
                case R.styleable.LX_WaveView_speed:
                    speed = ta.getInt(attr, 5);
                    break;
                case R.styleable.LX_WaveView_percentSize:
                    textSize = ta.getDimension(attr, 40);
                    break;
            }
        }
    }


    private void init() {
        oneOrbitPaint = new Paint();
        oneOrbitPath = new Path();
        onePointList = new ArrayList<>();


        textPaint = new Paint();
        textPaint.setColor(normalTextColor);
        textPaint.setTextSize(textSize);

        circlePaint = new Paint();

        circlePaint.setColor(circleColor);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setAntiAlias(true);

        borderPaint = new Paint();
        borderPaint.setColor(borderColor);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(borderWidth);
        borderPaint.setAntiAlias(true);

        oneOrbitPaint.setColor(oneOrbitColor);
        oneOrbitPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        oneOrbitPaint.setAntiAlias(true);

    }

    private void drawTextInCenterOfButton(Canvas c, Paint mPaint, float x, float y, String text) {
        float w = mPaint.measureText(text);
        Paint.FontMetrics fm = mPaint.getFontMetrics();
        float h = Math.abs(fm.descent - fm.ascent);
        c.drawText(text, x - w / 2, y + h / 2 - fm.descent, mPaint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        oneOrbitPath.reset();
        int i = 0;
        oneOrbitPath.moveTo(
                onePointList.get(0).getX() + oneWaveMoveLength,
                onePointList.get(0).getY() - mViewHeight / 100 * progress
        );
        for (; i < onePointList.size() - 2; i += 2) {
            oneOrbitPath.quadTo(
                    onePointList.get(i + 1).getX() + oneWaveMoveLength,
                    onePointList.get(i + 1).getY() - mViewHeight * progress / 100,
                    onePointList.get(i + 2).getX() + oneWaveMoveLength,
                    onePointList.get(i + 2).getY() - mViewHeight * progress / 100
            );
        }
        oneOrbitPath.lineTo(onePointList.get(i).getX() + oneWaveMoveLength, onePointList.get(i).getY());
        oneOrbitPath.lineTo(onePointList.get(0).getX() + oneWaveMoveLength, onePointList.get(0).getY());
        oneOrbitPath.close();

        //绘制轨迹
        canvas.drawPath(oneOrbitPath, oneOrbitPaint);



        //绘制进度
        String pg = String.format("%d%%", progress);
        if (progress >= 50) {
            textPaint.setColor(overTextColor);
        } else {
            textPaint.setColor(normalTextColor);
        }
        drawTextInCenterOfButton(canvas, textPaint, mViewWidth / 2, mViewHeight / 2, pg);
        if (isCircle) {
            //绘制圆环
            canvas.drawArc(cirlceRectF, 0, 360, true, circlePaint);
            //绘制边界
            canvas.drawCircle(mViewWidth / 2, mViewHeight / 2, mViewHeight / 2 - borderWidth / 2, borderPaint);
        } else {
            canvas.drawRect(0, 0, mViewWidth, mViewHeight, borderPaint);
        }

        if (isStart) {
            handler.sendEmptyMessageDelayed(0, speed);
        }
    }

    private boolean isMeasured = false;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!isMeasured && Math.abs(getMeasuredHeight() - getMeasuredWidth()) < 50) {
            mViewHeight = MeasureSpec.getSize(heightMeasureSpec);
            mViewWidth = MeasureSpec.getSize(widthMeasureSpec);
            levelLine = mViewHeight;//初始化波的准位线       起始位视图最底部
            {
                levelLine = mViewHeight * (100 - progress) / 100;
                if (levelLine < 0) levelLine = 0;
            }
            waveHeight = mViewHeight / 20;
            waveWidth = mViewWidth;

            for (int i = 0; i < 9; i++) {
                int x = 0;
                int y = 0;
                x = -waveWidth + waveWidth * i / 4;
                switch (i % 4) {
                    case 0:
                        y = levelLine;
                        break;
                    case 1:
                        y = levelLine - waveHeight;
                        break;
                    case 2:
                        y = levelLine;
                        break;
                    case 3:
                        y = levelLine + waveHeight;
                        break;
                }
                onePointList.add(new Point(x, y));
            }
            /**
             * 计算圆环宽度
             */

            int inCircleRadius = mViewHeight < mViewWidth ? mViewHeight / 2 : mViewWidth / 2;//内切圆半径
            int outCircleRadius = (int) Math.sqrt((float) (Math.pow(mViewHeight / 2, 2) + Math.pow(mViewWidth / 2, 2)) + 0.5);
            int radius = inCircleRadius / 2 + outCircleRadius / 2;
            cirlceRectF = new RectF(
                    mViewWidth / 2 - radius,
                    mViewHeight / 2 - radius,
                    mViewWidth / 2 + radius,
                    mViewHeight / 2 + radius
            );
            circleStrokeWidth = outCircleRadius - inCircleRadius;
            circlePaint.setStrokeWidth(circleStrokeWidth);
            isMeasured = true;
        }
    }

    public class Point {
        private int x;
        private int y;

        public Point() {
        }

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
        levelLine = (100 - progress) * mViewHeight / 100;
    }

    public boolean isCircle() {
        return isCircle;
    }

    public void setCircle(boolean circle) {
        isCircle = circle;
    }

    public void start() {
        isStart = true;
    }

    public void stop() {
        isStart = false;
    }

    public void setNormalTextColor(int normalTextColor) {
        this.normalTextColor = normalTextColor;
    }

    public void setOverTextColor(int overTextColor) {
        this.overTextColor = overTextColor;
    }

    public void setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
    }

    public void setspeed(int speed) {
        this.speed = speed;
    }

    public void setmove_distance(int move_distance) {
        this.move_distance = move_distance;
    }

    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
    }

    public void setCircleColor(int circleColor) {
        this.circleColor = circleColor;
    }

    public void setOneOrbitColor(int oneOrbitColor) {
        this.oneOrbitColor = oneOrbitColor;
    }

    public void setOneWaveMoveLength(int oneWaveMoveLength) {
        this.oneWaveMoveLength = oneWaveMoveLength;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }
}
