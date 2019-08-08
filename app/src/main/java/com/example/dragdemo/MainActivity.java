package com.example.dragdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * 动画分类演示
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //给Button设置 android:textAllCaps="false" 属性，防止text变大写
    private Button btn15, btn_animator, btn_socket;
    private Button btnWebView;
    private Button btnActService;
    private Button btnCp;
    private Button btnBitmap;
    private Button btnConstraintLayout;
    private Button btnView;
    private Button btnMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        btn15 = findViewById(R.id.btn15);
        btn_animator = findViewById(R.id.btn_animator);
        btn_socket = findViewById(R.id.btn_socket);
        btnWebView = (Button) findViewById(R.id.btn_web_view);
        btnActService = (Button) findViewById(R.id.btn_act_service);
        btnCp = (Button) findViewById(R.id.btn_cp);
        btnBitmap = (Button) findViewById(R.id.btn_bitmap);
        btnConstraintLayout = (Button) findViewById(R.id.btn_constraintLayout);
        btnView = (Button) findViewById(R.id.btn_view);
        btnMediaPlayer = (Button) findViewById(R.id.btn_mediaPlayer);
        btn15.setOnClickListener(this);
        btn_animator.setOnClickListener(this);
        btn_socket.setOnClickListener(this);
        btnActService.setOnClickListener(this);
        btnWebView.setOnClickListener(this);
        btnCp.setOnClickListener(this);
        btnBitmap.setOnClickListener(this);
        btnConstraintLayout.setOnClickListener(this);
        btnView.setOnClickListener(this);
        btnMediaPlayer.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_animator://动画
                startActivity(new Intent(this, AnimatorActivity.class));
                break;
            case R.id.btn15://scroller分析
                startActivity(new Intent(this, ScrollerActivity.class));
                break;
            case R.id.btn_socket://socket
                startActivity(new Intent(this, SocketActivity.class));
                break;
            case R.id.btn_web_view://WebView分析
                startActivity(new Intent(this, WebViewActivity.class));
                break;
            case R.id.btn_act_service://activity 与 service 交互
                startActivity(new Intent(this, ServiceToActivity.class));
                break;
            case R.id.btn_cp://contentProvider
                startActivity(new Intent(this, ContentProviderActivity.class));
                break;
            case R.id.btn_bitmap://大图 局部位置 预加载
                startActivity(new Intent(this, BitmapActivity.class));
                break;
            case R.id.btn_constraintLayout://constraintLayout 样例
                startActivity(new Intent(this, ConstraintLayoutActivity.class));
                break;
            case R.id.btn_view://自定义view
                startActivity(new Intent(this, CustomViewActivity.class));
                break;
            case R.id.btn_mediaPlayer://mediaPlayer
                startActivity(new Intent(this, MediaPlayerActivity.class));
                break;
            default:
                break;
        }
    }

}
