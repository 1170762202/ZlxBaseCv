package com.example.zlx.zlxbasecv.custom_view.recyclerviewtitle;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.zlx.zlxbasecv.DensityUtil;
import com.example.zlx.zlxbasecv.R;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Zlx on 2017/3/15.
 */
public class IndexBar extends View {
    private static final String[] SECTIONS = {"*", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#"};
    private List<String> indexDatas = Arrays.asList(SECTIONS);
    private Paint paint;
    private int mPressedBackground;
    private Context context;

    private float width, height;
    private float tvW, tvH;
    private int textSize;

    private List<CityBean> sourceList;

    private LinearLayoutManager linearLayoutManager;

    public IndexBar(Context context) {
        this(context, null);
    }

    public IndexBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initAttr(attrs);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(textSize);

        setOnIndexBarPressedListener(new IndexBarPressedListener() {
            @Override
            public void onIndexPressed(int position, String text) {
                Toast.makeText(context,text,Toast.LENGTH_SHORT).show();
                int pos = getPosByTag(text);
                linearLayoutManager.scrollToPositionWithOffset(pos, 0);
            }

            @Override
            public void onEventEnd() {

            }
        });
    }

    private int getPosByTag(String tag) {
        if (TextUtils.isEmpty(tag)) {
            return -1;
        }
        for (int i = 0; i < sourceList.size(); i++) {
            if (sourceList.get(i).getTag().equals(tag)) {
                return i;
            }
        }
        return -1;
    }

    private void initAttr(AttributeSet attrs) {
        textSize = DensityUtil.sp2px(context, 16);//默认的TextSize
        mPressedBackground = Color.BLACK;//默认按下是纯黑色
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.IndexBar);
        int n = typedArray.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.IndexBar_textSize:
                    textSize = typedArray.getDimensionPixelSize(attr, textSize);
                    break;
                case R.styleable.IndexBar_pressBackground:
                    mPressedBackground = typedArray.getColor(attr, mPressedBackground);
                default:
                    break;
            }
        }
        typedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < indexDatas.size(); i++) {
            canvas.drawText(indexDatas.get(i), tvW, tvH * (i + 1), paint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        tvW = width / 2;
        tvH = height / 28;

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float y = event.getY();
        int pos = 0;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.e("TAG", "ACTION_DOWN");
                setBackgroundColor(mPressedBackground);
            case MotionEvent.ACTION_MOVE:
                Log.e("TAG", "ACTION_MOVE");
                pos = (int) (y / tvH);
                if (pos < 0) {
                    pos = 0;
                }
                if (pos > indexDatas.size()) {
                    pos = indexDatas.size() - 1;
                }
                if (pos >= 0 && pos < indexDatas.size()) {
                    Log.e("TAG", pos + "    " + indexDatas.get(pos));
                    if (listener != null) {
                        listener.onIndexPressed(pos, indexDatas.get(pos));
                    }
                }

                break;
            case MotionEvent.ACTION_UP:
                Log.e("TAG", "ACTION_UP");
            case MotionEvent.ACTION_CANCEL:
                Log.e("TAG", "cancel");
            default:
                setBackgroundResource(ContextCompat.getColor(context, R.color.colorAccent));//手指抬起时背景恢复透明
                if (listener != null) {
                    listener.onEventEnd();
                }
                break;
        }
        return true;
    }

    private IndexBarPressedListener listener;

    public void setOnIndexBarPressedListener(IndexBarPressedListener listener) {
        this.listener = listener;
    }

    public interface IndexBarPressedListener {
        void onIndexPressed(int position, String text);//当某个Index被按下

        void onEventEnd();//当触摸事件结束（UP CANCEL）
    }

    public LinearLayoutManager getLinearLayoutManager() {
        return linearLayoutManager;
    }

    public void setLinearLayoutManager(LinearLayoutManager linearLayoutManager) {
        this.linearLayoutManager = linearLayoutManager;
    }

    public List<CityBean> getSourceList() {
        return sourceList;
    }

    public void setSourceList(List<CityBean> sourceList) {
        this.sourceList = sourceList;
    }
}
