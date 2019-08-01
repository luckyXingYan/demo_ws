package com.example.dragdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * 解决冷启动 的 黑白屏(闪屏)问题
         * 在启动 Activity 的onCreate()方法中，执行setContentView(R.layout.activity_splash);出现白屏。
         * 设想，onCreate---setContentView这个并不是发生在，窗体绘制的第一步，系统会在执行这个步骤之前，先绘制窗体，这时候布局资源还没加载，于是就使用默认背景色。
         * -------
         * 1、把启动图 bg_splash 设置为窗体背景，避免刚刚启动App的时候出现，黑/白屏
         *      styles.xml中配置AppTheme.Launcher ---> <item name="android:windowBackground">@mipmap/bg_splash</item> ，
         *      WelcomeActivity设置android:theme="@style/AppTheme.Launcher"
         * 2、设置为背景bg_splash显示的时候，后台负责加载资源，同时去下载广告图，广告图下载成功或者超时的时候显示 SplashActivity 的真实样子
         * 3、随后进入MainActivity
         */
        setContentView(R.layout.activity_splash);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
