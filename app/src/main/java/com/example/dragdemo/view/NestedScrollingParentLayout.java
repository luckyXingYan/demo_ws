package com.example.dragdemo.view;

import android.content.Context;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @Author: xingyan
 * @Date: 2019/8/12
 * @Desc:
 */
public class NestedScrollingParentLayout extends RelativeLayout implements NestedScrollingParent {
    private NestedScrollingParentHelper mHelper;
    private ImageView mImageView;
    //    private RecyclerView mRecyclerView;
    private TextView mTextView;
    private NestedScrollingChildLayout mChildLayout;
    private int mImgHeight, mTitleHeight;

    public NestedScrollingParentLayout(Context context) {
        this(context, null);
    }

    public NestedScrollingParentLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NestedScrollingParentLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mHelper = new NestedScrollingParentHelper(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mImageView = (ImageView) getChildAt(0);//获取第一个子view
//        mRecyclerView = (RecyclerView) getChildAt(1);//获取第二个子view
        mTextView = (TextView) getChildAt(1);//获取第二个子view

        mChildLayout = (NestedScrollingChildLayout) getChildAt(2);

        //获取布局高度 方法1
        mImageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //当布局变化时，获取图片布局的高度
                if (mImgHeight <= 0) {
                    mImgHeight = mImageView.getMeasuredHeight();
                }
            }
        });
        mTextView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //当布局变化时，获取文字布局的高度
                if (mTitleHeight <= 0) {
                    mTitleHeight = mTextView.getMeasuredHeight();
                }
            }
        });

    }

    /**
     * 在此可以判断参数target是哪一个子view以及滚动的方向，然后决定是否要配合其进行嵌套滚动
     *
     * @param child
     * @param target
     * @param nestedScrollAxes
     * @return
     */
    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {//当执行子view的startNestedScroll方法时回调此方法
        if (target instanceof NestedScrollingChildLayout) {
            return true;//true 表明父容器接受嵌套滚动,如果为false 则其他方法将不会调用
        }
        return false;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
        mHelper.onNestedScrollAccepted(child, target, axes);
    }

    @Override
    public void onStopNestedScroll(View child) {//停止滚动
        mHelper.onStopNestedScroll(child);
    }

    @Override
    public int getNestedScrollAxes() {//获取滚动的方向
        return mHelper.getNestedScrollAxes();
    }

    /**
     * @param target
     * @param dx       子view传递给父view的移动距离
     * @param dy       子view传递给父view的移动距离
     * @param consumed 需要父view给它复制，然后传递给子view，告诉子view父view当前消费的距离
     */
    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {//当执行子view的dispatchNestedPreScroll方法时回调
//        if (showImg(dy) || hideImg(dy)) {//根据图片的高度判断上拉和下拉的处理
//        boolean showTop = dy < 0 && getScaleY() >= 0 && !ViewCompat.canScrollVertically(target, -1);
//        boolean hiddenTop = dy > 0 && getScaleY() < mImgHeight;
        if (showImg(dy) || hideImg(dy)) {
            scrollBy(0, -dy);
            consumed[1] = dy;//告诉child消费了多少距离
        }
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        return super.onNestedPreFling(target, velocityX, velocityY);
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return super.onNestedFling(target, velocityX, velocityY, consumed);
    }

    //获取布局高度 方法2
//    @Override
//    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//        super.onSizeChanged(w, h, oldw, oldh);
//        mImgHeight = mImageView.getMeasuredHeight();
//        mTitleHeight = mTextView.getMeasuredHeight();
//    }

    //下拉滚动 是否要向下滚动以显示图片
    private boolean showImg(int dy) {
        if (dy > 0 && getScrollY() > 0 && mChildLayout.getScrollY() == 0) {
            return true;
        }
        return false;
    }

    //上拉滚动 是否要向上滚动，隐藏图片
    private boolean hideImg(int dy) {
        if (dy < 0 && getScrollY() < mImgHeight) {
            return true;
        }
        return false;
    }

    //限制滚动范围，防止出现偏差
    @Override
    public void scrollTo(int x, int y) {
        if (y < 0) {
            y = 0;
        }
        if (y > mImgHeight) {
            y = mImgHeight;
        }
        super.scrollTo(x, y);
    }
}
