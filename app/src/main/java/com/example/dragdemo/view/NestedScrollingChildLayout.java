package com.example.dragdemo.view;

import android.content.Context;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * @Author: xingyan
 * @Date: 2019/8/12
 * @Desc:
 */
public class NestedScrollingChildLayout extends RelativeLayout implements NestedScrollingChild {
    private NestedScrollingChildHelper mHelper;

    public NestedScrollingChildLayout(Context context) {
        super(context);
    }

    public NestedScrollingChildLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NestedScrollingChildLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //初始化 mHelper
    private NestedScrollingChildHelper getHelper() {
        if (mHelper == null) {
            mHelper = new NestedScrollingChildHelper(this);
            mHelper.setNestedScrollingEnabled(true);//允许滑动
        }
        return mHelper;
    }

    @Override
    public void setNestedScrollingEnabled(boolean enabled) {//是否允许滑动
        getHelper().setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled() {//滑动状态
        return getHelper().isNestedScrollingEnabled();
    }

    /**
     * 实现了 NestedScrollingChild 接口的 view ，滑动时回调此方法。
     * 且寻找已实现 NestedScrollingParent 接口的父 view (===通过回调此方法 hasNestedScrollingParent)
     *
     * @param axes 滑动的方向ViewCompat.SCROLL_AXIS_VERTICAL或ViewCompat.SCROLL_AXIS_HORIZONTAL
     * @return
     */
    @Override
    public boolean startNestedScroll(int axes) {//开始滑动
        return getHelper().startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll() {//停止滑动，并清除滑动状态
        getHelper().stopNestedScroll();
    }

    @Override
    public boolean hasNestedScrollingParent() {//判断是否存在 实现 NestedScrollingParent 接口的父 view
        return getHelper().hasNestedScrollingParent();
    }

    /**
     * 询问父view是否要在子view滚动之前滚动
     *
     * @param dx             告诉父view此次滚动的距离
     * @param dy             告诉父view此次滚动的距离
     * @param consumed       子view获取父view消费掉的距离
     * @param offsetInWindow 子view获取父view位置的偏移量
     * @return
     */
    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {//滚动之前回调
        return getHelper().dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    /**
     * 询问父view是否还要进行余下(unconsumed)的滚动 == 如果父view接受了子view的滚动参数，进行了部分消费，则这个函数返回true，否则为false。
     *
     * @param dxConsumed     告诉父view已经消费的距离
     * @param dyConsumed
     * @param dxUnconsumed   告诉父view尚未消费的距离
     * @param dyUnconsumed
     * @param offsetInWindow 子view获取父view位置的偏移量
     * @return
     */
    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {//滚动之后回调
        return super.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return getHelper().dispatchNestedPreFling(velocityX, velocityY);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return getHelper().dispatchNestedFling(velocityX, velocityY, consumed);
    }

    private float oldY;
    private final int[] offset = new int[2]; //偏移量
    private final int[] consumed = new int[2]; //消费

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                oldY = event.getRawY();//屏幕上触摸时y轴坐标
                break;
            case MotionEvent.ACTION_MOVE:
                float newY = event.getY();//屏幕上滑动时y轴坐标
                float dy = newY - oldY;//屏幕上滑动的偏移量
                oldY = newY;
                dispatchNestedPreScroll(0, (int) dy, offset, consumed);//询问父view是否要在子view滚动之前滚动
                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);//开始滑动
                break;
            default:
                break;
        }
        return true;//子view 要消费
    }
}
