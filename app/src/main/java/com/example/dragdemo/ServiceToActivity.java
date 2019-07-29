package com.example.dragdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * activity 和 servise 通信的方式
 * 方法一：扩展Binder
 * 方法二：广播 Broadcast
 * 方法三：共享文件 SharedPreferences 高并发不适用
 * 方法四：Messenger 信使 一种轻量级的IPC方案，底层是用AIDL实现的
 * 方法五：AIDL Android的IPC机制，常用于跨进程通信，主要实现原理基于底层Binder机制
 */
public class ServiceToActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnBinder;
    private Button btnBroadcast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_to);
        initView();
    }

    private void initView() {

        btnBinder = (Button) findViewById(R.id.btn_binder);
        btnBroadcast = (Button) findViewById(R.id.btn_broadcast);

        btnBinder.setOnClickListener(this);
        btnBroadcast.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_binder://通过 Binder 进行通信
                startActivity(new Intent(this, ServiceBinderActivity.class));
                break;
            case R.id.btn_broadcast://通过 Broadcast 广播
                break;
            default:
                break;
        }
    }
}
