package com.example.dragdemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

/**
 * 加载一个大图而不会发生OOM 这里有两种途径，
 * 一、降采样大图；二、局部加载大图。(重写View的onTouchEvent方法监听手指滑动事件，更新显示区域)
 */
public class BitmapActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnLoad;
    private ImageView ivBg;
    private int imgWidth, imgHeight;
    private float mStartX = 0;
    private float mStartY = 0;
    private Rect rect;
    private BitmapRegionDecoder bitmapRegionDecoder;
    private BitmapFactory.Options options;

    private Button btnLoadView;
    private MyBitmapView viewMy;

    private InputStream inputStream;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bitmap);
        initView();
    }

    private void initView() {

        btnLoad = (Button) findViewById(R.id.btn_load);
        ivBg = (ImageView) findViewById(R.id.iv_bg);

        btnLoadView = (Button) findViewById(R.id.btn_load_view);
        viewMy = (MyBitmapView) findViewById(R.id.view_my);


        btnLoad.setOnClickListener(this);
        btnLoadView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /**
             * 拿到大图的 InputStream
             * 通过 BitmapFactory.Options 读取图片的宽、高，不加载到内存
             * 获取 bitmapRegionDecoder.newInstance(inputStream, false);
             * 可设置新的 options
             * 通过 Rect 设置 预加载局部图片的位置
             * bitmapRegionDecoder.decodeRegion(rect, options); 获得局部的 bitmap
             * 给 imageView 设置新的 局部图片
             * onTouchEvent 监听手势滑动 在 MOVE 中动态修改 rect.offset ，
             *      并设置图片显示的局部位置
             */
            case R.id.btn_load:
                try {
                    InputStream inputStream = getAssets().open("beauty.jpg");
                    //获得图片的原始宽高
                    BitmapFactory.Options optionsTemp = new BitmapFactory.Options();
                    optionsTemp.inJustDecodeBounds = true;//只读取图片，不加载到内存中
                    BitmapFactory.decodeStream(inputStream, null, optionsTemp);
                    //放在 decodeStream 后，否则 width、height为 -1
                    imgWidth = optionsTemp.outWidth;
                    imgHeight = optionsTemp.outHeight;


                    bitmapRegionDecoder = BitmapRegionDecoder.newInstance(inputStream, false);

                    options = new BitmapFactory.Options();
                    options.inPreferredConfig = Bitmap.Config.RGB_565;

                    //设置预加载显示图片中心的位置
                    rect = new Rect(imgWidth / 2 - 100, imgHeight / 2 - 100, imgWidth / 2 + 100, imgHeight / 2 + 100);
                    Bitmap bitmap = bitmapRegionDecoder.decodeRegion(rect, options);

                    ivBg.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_load_view:
                try {
                    if (inputStream == null) {
                        inputStream = getAssets().open("beauty.jpg");
                    }
                    inputStream.mark(inputStream.available());//标记起点
                    viewMy.initInputStream(inputStream);
                    inputStream.reset(); //用完inputStream后就重置状态，否则下次(inputStream.available()为0，无法再次使用
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//
//        switch (event.getAction() & MotionEvent.ACTION_MASK) {//处理多点触摸的ACTION_POINTER_DOWN和ACTION_POINTER_UP事件
//            case MotionEvent.ACTION_DOWN:
//                mStartX = event.getX();
//                mStartY = event.getY();
//                break;
//            case MotionEvent.ACTION_MOVE://ACTION_DOWN和ACTION_UP就是单点触摸屏幕，按下去和放开的操作
//                if (imgWidth > ivBg.getWidth()) {
//                    rect.offset((int) (mStartX - event.getX()), 0);
//
//                    Bitmap bitmap = bitmapRegionDecoder.decodeRegion(rect, options);
//                    ivBg.setImageBitmap(bitmap);
//                }
//                if (imgHeight > ivBg.getHeight()) {
//                    rect.offset(0, (int) (mStartY - event.getY()));
//
//                    Bitmap bitmap = bitmapRegionDecoder.decodeRegion(rect, options);
//                    ivBg.setImageBitmap(bitmap);
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//            case MotionEvent.ACTION_CANCEL:
//                break;
//        }
//        return true;
//    }


    @Override
    protected void onDestroy() {
        //释放资源
        if (inputStream != null) {
            try {
                inputStream.close();
                inputStream = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onDestroy();
    }
}
