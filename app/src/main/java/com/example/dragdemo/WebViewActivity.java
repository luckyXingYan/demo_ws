package com.example.dragdemo;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.util.HashMap;
import java.util.Map;

/**
 * webview的原生引擎是webkit，坑太多
 * android 4.4 后引入了chrome引擎
 * 其他的也有腾讯的 x5内核 和 crosswalk
 * x5内核 是在 webkit 的基础上进行优化的
 */
public class WebViewActivity extends AppCompatActivity {
    private final String TAG = "---WebViewActivity";
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        initView();
        initData();
    }

    private void initData() {
        //===加载数据
//        String data = "<html>###标题###<body><p>我是段落</p><body/></html>";
//        webView.loadData(data,"text/html","UTF-8");//方法1，编码可能失效
//        webView.loadData(data, "text/html;charset=UTF-8", null);//方法2
//        webView.loadDataWithBaseURL(null,data,"text/html","UTF-8",null);//方法3==官方推荐

        //===加载网络数据 方法1 == data中的路径是相对路径 要结合baseUrl才能加载
//        String data = "<img src = '/uploads/allimg/110825/3-110R5133545427.jpg'/></p><img src = '/uploads/allimg/120302/3-1203021T03E04.jpg'/>";
//        String baseUrl = "http://img.daimg.com";
//        webView.loadDataWithBaseURL(baseUrl, data, "text/html", "UTF-8", null);

        //=== 加载网络网址
        String url = "http://img.daimg.com/uploads/allimg/110825/3-110R5133545427.jpg";
        webView.loadUrl(url);
        //===加载带头Header的 网络地址
//        Map<String, String> params = new HashMap<>();
//        params.put("key", "value");
//        webView.loadUrl(url, params);

        //===加载本地文件
//        webView.loadUrl("file:///asset/xxx.html");
//        webView.loadUrl("content://authorities/person/xxxx");

        //post 请求的url == 带有参数的请求
        //webView.postUrl(String url, byte[] postData);

//        webView.reload();//重新加载当前网页

    }

    private void initView() {
        webView = (WebView) findViewById(R.id.web_view);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);//js交互需要配置

        //网页可把缩放屏蔽掉，网页优先级大于安卓代码
        webSettings.setSupportZoom(true);//是否支持缩放
        webSettings.setBuiltInZoomControls(true);//设置内置的缩放控件，true可缩放
        webSettings.setDisplayZoomControls(false);//是否显示原生的缩放控件(即 加、减号的图标 )

        Log.e(TAG, webSettings.supportZoom() + "===");//判断当前页面是否支持缩放

        webSettings.setTextZoom(100);//默认100,保证webview的自提大小不随手机自带字体大小改变
        webSettings.setDefaultTextEncodingName("UTF-8");//默认是utf-8,设置字符编码

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            webSettings.setMediaPlaybackRequiresUserGesture(true);//对于视频是否需要手势点击才能播放，默认true==需要，改为false可实现自动播放
        }

        /**
         * Android 5.0 上 https 和 http 混合问题
         * 在Android 5.0 上为了提高安全性，不允许https的页面直接访问http的页面
         * mixed_content_always_allow：允许从任何来源加载内容
         * mixed_content_never_allow：不允许https加载http的内容 == 默认方式
         * mixed_content_compatibility_allow：当涉及到混合式内容时，WebView 会尝试去兼容最新Web浏览器的风格
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //解决Android 5.0 上 https的页面不能直接访问http的页面的问题
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        //以后的webview版本中都不会使用到插件

        /**
         * webview的安全浏览：允许WebView通过验证链接来防范恶意软件和网络钓鱼攻击
         *
         * 在android 8.0 之后 新增了特性 可以用清单标签 代替代码配置安全浏览，切优先级高于代码
         * 即在<application>标签下配置     <meta-data android:name="android.webkit.WebView.EnableSafeBrowsing"
         *         android:value="true" />
         * 使用标签 可以禁止项目中 所有的WebView的安全浏览功能
         *
         */
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            webSettings.setSafeBrowsingEnabled(true);//代码设置是否启用安全浏览
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.e(TAG, "onDetachedFromWindow");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy");
    }
}
