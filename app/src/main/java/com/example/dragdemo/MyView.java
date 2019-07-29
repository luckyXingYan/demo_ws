package com.example.dragdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;

/**
 * @Author: xingyan
 * @Date: 2019/7/22
 * @Desc: Android Scroller分析
 */
public class MyView extends View {
    private Paint paint;
    private float mDownX = 0;
    private float mDownY = 0;
    private Scroller scroller;

    public MyView(Context context) {
//        super(context);//super==调用父类的构造方法
//        init(context);
        //层级调用==替代上面2行，更简洁
        this(context, null);//this==调用当前类的构造方法
    }


    public MyView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        init(context);
        this(context, attrs, 0);
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        paint = new Paint();
        paint.setTextSize(80);

        /**
         * new Scroller的时候 就创建了一个默认的插值器 ViscousFluidInterpolator
         * 插值器Interpolator/TimeInterpolator：根据时间流逝的百分比 计算出当前属性值改变的 百分比
         * Scroller 默认插值器是 ViscousFluidInterpolator
         * 也可指定插值器 new Scroller(mContext, new AccelerateDecelerateInterpolator());
         * 参数3标识 是否开启“飞轮”效果，也就是多次滚动时速度叠加
         * new Scroller(mContext, new AccelerateDecelerateInterpolator(), false);
         *
         * LinearInterpolator 匀速
         * AccelerateDecelerateInterpolator 加速减速插值器，2头慢中间快
         * DecelerateInterpolator 减速
         *
         * 估值器TypeEvaluator：根据当前属性值改变的百分比 计算出改变后的属性值
         * IntEvaluator
         * FloatEvaluator
         * ArgbEvaluator
         */
//        scroller = new Scroller(context);
        scroller = new Scroller(context, new LinearInterpolator());
        /**
         * Scroller  其实就是在scrollerTo/scrollerBy 的基础上添加了 滚动效果(一个动画)
         */
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawText("测试", 100, 100, paint);
    }

    /**
     * 处理拖动事件
     * scrollTo/scrollBy 无滚动特效==移动
     * scrollTo 叠加移动，每次移动赋值都是初始化位置
     * scrollBy 叠加移动，是叠加距离移动
     * scroller 有滚动特效
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //初始x、y偏移量坐标
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //按下时x、y坐标
                mDownX = x;
                mDownY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                //计算当前已经移动的x、y轴方向的距离
                float moveX = mDownX - x;
                float moveY = mDownY - y;

                //======移动===scrollTo/scrollBy方法
//                scrollTo((int) moveX, (int) moveY);//不能叠加移动

                //======滚动===scroller对象
                //参数1 、2是 view最终停止的位置=即view滚动的其实位置；参数3、4表示偏移量；参数5表示滚动所消耗的时间，默认250mm
                scroller.startScroll(scroller.getFinalX(), scroller.getFinalY(), (int) moveX, (int) moveY, 3000);
                invalidate();//重绘View==调computeScroll方法，要复写

                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        /**
         * 是否还没有停止了滚动
         * true 滚动没结束
         * false 滚动结束了
         *
         * 目的：将一次大的滚动 分成若干次小的滑动，一段时间内完成滚动
         */
        if (scroller.computeScrollOffset()) {
            //反复将view直接移动到当前滚动的位置（即分段出的距离）
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            //重绘View
            invalidate();//重绘view
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
//        scroller.abortAnimation();
    }

}
