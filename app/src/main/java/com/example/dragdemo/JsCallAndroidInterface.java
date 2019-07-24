package com.example.dragdemo;

import android.util.Log;
import android.webkit.JavascriptInterface;

/**
 * @Author: xingyan
 * @Date: 2019/7/24
 * @Desc: 定义 js 调 android 的接口
 * 接口中的方法 必须添加  @JavascriptInterface 注解，意思是将 android 的接口方法
 * 暴漏给 js。作用是 为 js 提供 调用的接口
 */
public class JsCallAndroidInterface {

    @JavascriptInterface//为 js 提供 调用的接口
    public void hello() {
        Log.e("JsCallAndroidInterface", "hello()");
    }
}
