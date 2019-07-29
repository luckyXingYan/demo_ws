package com.example.dragdemo;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * webview的原生引擎是webkit，坑太多
 * android 4.4 后引入了chrome引擎
 * 其他的也有腾讯的 x5内核 和 crosswalk
 * x5内核 是在 webkit 的基础上进行优化的
 * <p>
 * 内存泄漏优化：
 * 1、new一个 wiebview 而不是在.xml中定义 webview节点
 * 2、创建webview时new WebView(...);传入ApplicationContext的context==采用activity的context细想来貌似和在xml中直接定义没有什么区别；
 * 3、让onDetachedFromWindow先走 === 在主动调用destroy()的开始，把webview从它的parent上面移除掉。
 * 4、释放资源
 */
public class WebViewActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = "---WebViewActivity";
    private WebView webView;
    private WebSettings webSettings;
    private Button btnJs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        initView();
        initListener();
        initData();
    }

    private void initListener() {
        //==============================WebViewClient相关方法=========================
        /**
         * shouldOverrideUrlLoading：重定向时被调用（网页自动重定型 或者 手动点击网页内部链接）
         * onTooManyRedirects 过时且废弃的方法，也是用于重定向
         */
        webView.setWebViewClient(new WebViewClient() {
            //API24（Android 7.0）之后 过时了;但为了兼容 24之前的版本 还要复写，因为24版本之前只会走此方法
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.e(TAG, "view：" + view.getUrl());
                Log.e(TAG, "shouldOverrideUrlLoading(WebView view, String url)：" + url);

                /**
                 * js 调 android 代码 方法2 == 通过 shouldOverrideUrlLoading 拦截 uri
                 */
                Uri uri = Uri.parse(url);
                if ("android".equals(uri.getScheme()) && "webview".equals(uri.getAuthority())) {
                    Set<String> params = uri.getQueryParameterNames();
                    Iterator iterable = params.iterator();
                    while ((iterable).hasNext()) {
                        Log.e(TAG, (String) iterable.next());
                    }
                }
                return super.shouldOverrideUrlLoading(view, url);
            }


            /**
             * API24之后 只会执行 次方法
             * 返回值 true、false、super.shouldOverrideUrlLoading(view, request);==还是会回调 上面的方法
             * @param view
             * @param request
             * @return
             */
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Log.e(TAG, "shouldOverrideUrlLoading(WebView view,  WebResourceRequest request)：" + view.getUrl());
                return super.shouldOverrideUrlLoading(view, request);
            }

            /**
             * 过时了,但会被调用 == 拦截加载的资源（资源的请求 + 返回响应数据）
             * 注意：API21以下的AJAX请求会走onLoadResource，无法通过此方法拦截
             * @param view
             * @param url
             * @return
             */
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                Log.e(TAG, "shouldInterceptRequest(WebView view, String url)：" + url);
                return super.shouldInterceptRequest(view, url);
            }

            /**
             * API21之后 的新方法 == 在 非UI线程 被调用 所以要创建子线程做操作
             *
             * @param view
             * @param request
             * @return
             */
            @Override
            public WebResourceResponse shouldInterceptRequest(final WebView view, WebResourceRequest request) {
                new Thread(new Runnable() {//必须在子线程做操作，不然崩溃
                    @Override
                    public void run() {
                        Log.e(TAG, "shouldInterceptRequest(WebView view, WebResourceRequest request：" + view.getUrl());
                    }
                });
                return super.shouldInterceptRequest(view, request);
            }

            /**
             * 对应于 shouldInterceptRequest(WebView view, String url)的功能，但没有过时
             * @param view
             * @param url
             */
            @Override
            public void onLoadResource(WebView view, String url) {
                Log.e(TAG, "onLoadResource(WebView view, String url)：" + url);
                super.onLoadResource(view, url);
            }

            /**
             * 网页加载开始
             * @param view
             * @param url
             * @param favicon
             */
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.e(TAG, "onPageStarted：" + url);
                super.onPageStarted(view, url, favicon);
            }

            /**
             * 网页加载结束
             * @param view
             * @param url
             */
            @Override
            public void onPageFinished(WebView view, String url) {
                Log.e(TAG, "onPageFinished：" + url);
                super.onPageFinished(view, url);
            }

            /**
             * API23 添加 == 网页内的跳转会被调用
             * @param view
             * @param url 只当前跳转页面的 url
             */
            @Override
            public void onPageCommitVisible(WebView view, String url) {
                Log.e(TAG, "onPageCommitVisible：" + url);
                super.onPageCommitVisible(view, url);
            }

            /**
             *  API23 添加 == 加载资源(iframe,image,js,css,ajax...)时 收到 http 错误，且 状态码 >= 400
             * @param view
             * @param request
             * @param errorResponse
             */
            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                Log.e(TAG, "onReceivedHttpError：" + "errorResponse.getStatusCode()");
                super.onReceivedHttpError(view, request, errorResponse);
            }

            /**
             * 是否重新提交 表单，默认不提交
             * @param view
             * @param dontResend
             * @param resend
             */
            @Override
            public void onFormResubmission(WebView view, Message dontResend, Message resend) {
                Log.e(TAG, "onFormResubmission：" + resend.obj);
                super.onFormResubmission(view, dontResend, resend);
            }

        });
        //==============================WebChromeClient 相关方法=========================
        webView.setWebChromeClient(new WebChromeClient() {
            //监听网页加载进度
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                Log.e(TAG, "onProgressChanged：" + newProgress);
                super.onProgressChanged(view, newProgress);
            }

            //获取网页的标题
            @Override
            public void onReceivedTitle(WebView view, String title) {
                Log.e(TAG, "onReceivedTitle：" + title);
                super.onReceivedTitle(view, title);
            }

            //当网页接收到icon时 执行此方法
            @Override
            public void onReceivedIcon(WebView view, Bitmap icon) {
                Log.e(TAG, "onReceivedIcon：" + icon);
                super.onReceivedIcon(view, icon);
            }

            //触摸当前网页接收到icon时 执行此方法
            @Override
            public void onReceivedTouchIconUrl(WebView view, String url, boolean precomposed) {
                Log.e(TAG, "onReceivedTouchIconUrl：" + url);
                super.onReceivedTouchIconUrl(view, url, precomposed);
            }

            /**
             * JS层的window也有三个方法window.alert，window.confirm，window.prompt
             * 当JS层调用window对象中的某个方法的时候，相应的就会触发WebChromeClient对象中对应的方法
             * @param view
             * @param url
             * @param message
             * @param result
             * @return
             */
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                Log.e(TAG, "onJsAlert：" + url);
                return super.onJsAlert(view, url, message, result);
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                Log.e(TAG, "onJsConfirm：" + url);
                return super.onJsConfirm(view, url, message, result);
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                Log.e(TAG, "onJsPrompt：" + url);
                return super.onJsPrompt(view, url, message, defaultValue, result);
            }
        });
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
//        String url = "http://img.daimg.com/uploads/allimg/110825/3-110R5133545427.jpg";
//        String url = "https://qa01-jr-mobile-430.yingujr.com/h5/other/honorary";
//        String url = "https://qa01-jr-mobile-430.yingujr.com/activity/activity_View/third_anniversary";
//        webView.loadUrl(url);
        /**
         * android 调 js
         * assets文件要在src --> main下，不然拿不到资源
         * 资源路径必须是：android_asset,不然读取不到
         */
        // android 调 js
//        String url = "file:///android_asset/html_demo.html";// android 调 js 配合android中的按钮

        //js 调 android
//        String url = "file:///android_asset/html_demo2.html";//注解，js  调 android 配合 js 中的按钮
        //JS代码想要调用Android代码声明的接口，就必须给JS创建一个映射
//        webView.addJavascriptInterface(new JsCallAndroidInterface(), "android");

        //js 调 android
        String url = "file:///android_asset/html_demo3.html";//拦截，js  调 android 配合 js 中的按钮


        webView.loadUrl(url);

        //===加载带头Header的 网络地址
//        Map<String, String> params = new HashMap<>();
//        params.put("key", "value");
//        webView.loadUrl(url, params);

        //===加载本地文件
//        webView.loadUrl("file:///assets/xxx.html");
//        webView.loadUrl("content://authorities/person/xxxx");

        //post 请求的url == 带有参数的请求
        //webView.postUrl(String url, byte[] postData);

//        webView.reload();//重新加载当前网页

    }

    private void initView() {
        webView = (WebView) findViewById(R.id.web_view);

        btnJs = (Button) findViewById(R.id.btn_js);
        btnJs.setOnClickListener(this);


        webSettings = webView.getSettings();
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

    /**
     * 首先 因为 onDestroy 是在 onDetachedFromWindow 之前执行
     * 再者 android 5.1之前 onDetachedFromWindow 是没有  if (isDestroyed()) return; 这个判断的，
     * 所以没问题；
     * 但是 google在高版本加入了判断，onDestroy后调 onDetachedFromWindow 会被
     * if (isDestroyed()) return; 拦截，无法反注册，导致了内存溢出。
     * 解决方案也比较简单，核心思路就是让onDetachedFromWindow先走，那么在主动调用destroy()的开始，把webview从它的parent上面移除掉。
     * 所以 在 onDestroy 释放资源的之前 要反注册(解除) webview 和 parent 的依赖
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy===在onDetachedFromWindow方法之前执行");
        if (webView != null) {

            //解除绑定==反注册
            ViewParent parent = webView.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(webView);
            }

            //释放资源
            webSettings.setJavaScriptEnabled(false);
            webView.clearHistory();
            webView.clearView();
            webView.removeAllViews();
            webView.destroy();
            /**
             * 此处的webview是全局变量的强引用，所以要释放，设为null，等 jvm 的gc 来回收
             * 如果是在一个强引用方法体中，new 出一个局部强引用的变量 则不用手动 = null，因为局部变量会被gc回收
             */
            webView = null;
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.e(TAG, "onDetachedFromWindow");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_js://点击按钮，加载js中的代码 == 1、原生android 调用 js的代码
//            webView.loadUrl("javascript:callJS2()");//弊端：会刷新当前页面，执行效率慢

                //不会刷新当前页面，弊端：android 4.4 之后可用
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    webView.evaluateJavascript("javascript:callJS2()", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                            Log.e(TAG, "onReceiveValue" + value);
                        }
                    });
                } else {
                    webView.loadUrl("javascript:callJS2()");//弊端：会刷新当前页面，执行效率慢
                }
                break;
            //2、js 调用 android原生代码
            /**
             * 方法1、采用addJavascriptInterface映射
             * webSettings.setJavaScriptEnabled(true);//js交互配置
             * 定义js 调 android的接口JsCallAndroidInterface，方法（此 hello 方法 和 js 中定义的方法要一致）添加 @JavascriptInterface 注解，为 js 提供 调用的接口，将 android 的接口方法暴漏给 js。
             * 优点：使用简单
             * 缺点：注解使Android的接口暴露出来，是一个高危漏洞
             *
             * 方法2、采用shouldOverrideUrlLoading拦截URl
             * 优点：没有方法1的安全漏洞
             * 缺点：使用复杂，并且将所有的JS处理都放在shouldOverrideUrlLoading中略显臃肿
             * 方法3、采用onJsAlert、onJsConfirm、onJsPrompt 捕获URL，推荐在onJsPrompt捕获 == 和方法2原理类似
             * 优点：没有方法1的安全漏洞
             * 缺点：使用复杂，并且将所有的JS处理都放在onJsAlert、onJsConfirm、onJsPrompt中略显臃肿。
             * 方法4、JsBridge实现JS交互  推荐
             * JSBridge就是一个native层和JS层约定好的协议，类似于http
             * 首先 依赖 javajsbridge 三方包
             */
            default:
                break;
        }
    }

}
