package com.example.dragdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;

/**
 * @Author: xingyan
 * @Date: 2019/8/1
 * @Desc:
 */
public class MyBitmapView extends View {
    private int imgWidth, imgHeight;
    private BitmapRegionDecoder bitmapRegionDecoder;
    private float mX;
    private float mY;

    private static final BitmapFactory.Options options = new BitmapFactory.Options();
    /**
     * 绘制的区域
     * //Rect类与RectF类（android.graphics.RectF）的区别??
     * //答：主要还是在精度上的不同，他们分别是：int、float类型的
     */
    private volatile Rect rect = new Rect();


    static {
        options.inPreferredConfig = Bitmap.Config.RGB_565;
    }

    public MyBitmapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
    }

    public void initInputStream(InputStream inputStream) {
        try {


            BitmapFactory.Options optionsTemp = new BitmapFactory.Options();
            optionsTemp.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(inputStream, null, optionsTemp);
            //放在 decodeStream 后，否则 width、height为 -1
            imgWidth = optionsTemp.outWidth;
            imgHeight = optionsTemp.outHeight;

            //要放在 获取完 宽、高 后面
            bitmapRegionDecoder = BitmapRegionDecoder.newInstance(inputStream, false);

            requestLayout();
            invalidate();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (bitmapRegionDecoder != null) {
            Bitmap bitmap = bitmapRegionDecoder.decodeRegion(rect, options);
            canvas.drawBitmap(bitmap, 0, 0, null);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mX = event.getX();
                mY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (imgWidth > getWidth()) {
                    //该矩阵在x轴和y轴分别发生的偏移量（很有用，可以上下移动矩阵）
                    rect.offset((int) (mX - event.getX()), 0);
                    checkWidth();
                    invalidate();
                }
                if (imgHeight > getHeight()) {
                    rect.offset(0, (int) (mY - event.getY()));
                    checkHeight();
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return true;
    }

    private void checkWidth() {
        if (rect.right > imgWidth) {
            rect.right = imgWidth;
            rect.left = imgWidth - getWidth();
        }
        if (rect.left < 0) {
            rect.left = 0;
            rect.right = getWidth();
        }

    }

    private void checkHeight() {
        if (rect.bottom > imgHeight) {
            rect.bottom = imgHeight;
            rect.top = imgHeight - getHeight();
        }
        if (rect.top < 0) {
            rect.top = 0;
            rect.bottom = getHeight();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        //默认显示图片中心区域
        rect.left = imgWidth / 2 - width / 2;
        rect.top = imgHeight / 2 - height / 2;
        rect.right = rect.left + width;
        rect.bottom = rect.top + height;
    }
}
