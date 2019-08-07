package com.example.dragdemo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.example.dragdemo.R;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

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
    private Rect mRect;//文字边框
    private Random random;
    private int[] tempCheckNum;

    public CustomView(Context context) {
        this(context, null);
    }

    public CustomView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //必须传入 attrs
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        //此处需要的属性、文字、颜色、大小等 必须在 xml 中也声明，不然没效果
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CustomView);
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

        //初始化画笔 + 边框
        mPaint = new Paint();
        mRect = new Rect();
        random = new Random();
        tempCheckNum = new int[]{0, 0, 0, 0};

        mPaint.setTextSize(mTextSize);
        mPaint.getTextBounds(mText, 0, mText.length(), mRect);

        //给当前 view 设置点击事件 可以在onDraw中再添加一点噪点，模拟 获取验证码
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mText = randomText();
                postInvalidate();
            }
        });
    }

    private String randomText() {
        Set<Integer> set = new HashSet<>();
        while (set.size() < 4) {
            int randomInt = random.nextInt(10);
            set.add(randomInt);
        }
        StringBuilder sb = new StringBuilder();
        for (Integer i : set) {
            sb.append("" + i);
        }
        return sb.toString();
    }

    /**
     * view布局中为 wrap_content 时，此方法 重新测量 view 的宽高
     * EXACTLY：一般是设置了明确的值或者是MATCH_PARENT
     * AT_MOST：表示子布局限制在一个最大值内，一般为WARP_CONTENT
     * UNSPECIFIED：表示子布局想要多大就多大，很少使用
     * <p>
     * int widthSize = MeasureSpec.getSize(widthMeasureSpec); == widthMeasureSpec
     * int heightSize = MeasureSpec.getSize(heightMeasureSpec); == heightMeasureSpec
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int width, height;
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthMeasureSpec;
        } else {//AT_MOST 和 UNSPECIFIED
            mPaint.setTextSize(mTextSize);
            mPaint.getTextBounds(mText, 0, mText.length(), mRect);
            float textWidth = mRect.width();
            int desired = (int) (getPaddingLeft() + textWidth + getPaddingRight());
            width = desired;
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightMeasureSpec;
        } else {
            mPaint.setTextSize(mTextSize);
            mPaint.getTextBounds(mText, 0, mText.length(), mRect);
            float textHeight = mRect.height();
            int desired = (int) (getPaddingTop() + textHeight + getPaddingBottom());
            height = desired;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画边框
        mPaint.setColor(Color.YELLOW);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);
        //画文字
        mPaint.setColor(mTextColor);
        canvas.drawText(mText, getWidth() / 2 - mRect.width() / 2, getHeight() / 2 + mRect.height() / 2, mPaint);

        // 绘制小圆点
        int[] point;
        for (int i = 0; i < 100; i++) {
            mPaint.setColor(randomTextColor());
            point = getPoint(getWidth(), getHeight());//参数自定义的view宽高
            canvas.drawCircle(point[0], point[1], getRandomRadius(), mPaint);//圆心的x坐标、圆心的y坐标、圆的半径、绘制时所使用的画笔
        }
        //画横线
        int[] line;
        for (int i = 0; i < 100; i++) {
            mPaint.setColor(randomTextColor());
            line = getLine(getWidth(), getHeight());
            canvas.drawLine(line[0], line[1], line[2], line[3], mPaint);//起始端点的X坐标、起始端点的Y坐标、终止端点的X坐标、终止端点的Y坐标、绘制直线所使用的画笔
        }
    }

    private float getRandomRadius() {
        float radius = (float) (Math.random() * 15);
        return radius;
    }

    public int[] getLine(int width, int height) {
        for (int i = 0; i < 4; i += 2) {
            tempCheckNum[i] = (int) (Math.random() * width);
            tempCheckNum[i + 1] = (int) (Math.random() * height);
        }
        return tempCheckNum;
    }

    private int randomTextColor() {
        int ranColor = 0xff000000 | random.nextInt(0x00ffffff);
        return ranColor;
    }

    private int[] getPoint(int width, int height) {
        tempCheckNum[0] = (int) (Math.random() * width);
        tempCheckNum[1] = (int) (Math.random() * height);
        return tempCheckNum;
    }
}
