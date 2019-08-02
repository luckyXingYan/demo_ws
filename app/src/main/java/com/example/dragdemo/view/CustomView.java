package com.example.dragdemo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.example.dragdemo.R;

/**
 * @Author: xingyan
 * @Date: 2019/8/2
 * @Desc:
 */
public class CustomView extends View {

    private String mText;
    private int mTextColor;
    private int mTextSize;
    private Paint mPaint;

    public CustomView(Context context) {
        this(context, null);
    }

    public CustomView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        TypedArray typedArray = getContext().obtainStyledAttributes(R.styleable.CustomView);
        int count = typedArray.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.CustomView_text:
                    mText = typedArray.getString(attr);
                    break;
                case R.styleable.CustomView_textColor:
                    mTextColor = typedArray.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.CustomView_textSize:
                    int defaultSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics());
                    mTextSize = typedArray.getDimensionPixelSize(attr, defaultSize);
                    break;
                default:
                    break;
            }
        }
        typedArray.recycle();//资源回收

        //初始化画笔
        mPaint = new Paint();
        mPaint.setTextSize(mTextSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawText(mText, 0, getWidth(), 0, getHeight(), mPaint);
    }
}
