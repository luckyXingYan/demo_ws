package com.example.dragdemo;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * 广播 === startService ===
 */
public class ServiceBroadcastActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "ServiceBroadcastActivity";
    private Button btnStartService;

    private int counter;

    private MyBroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_broadcast);
        initView();

    }

    private void initView() {

        btnStartService = (Button) findViewById(R.id.btn_start_service);
        btnStartService.setOnClickListener(this);

        //动态注册广播
        receiver = new MyBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.dragdemo.COUNTER_ACTION");

        registerReceiver(receiver, intentFilter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start_service:
                Intent intent = new Intent(this, BroadcastService.class);
                intent.putExtra("counter", counter);
                startService(intent);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    public class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            counter = intent.getIntExtra("counter", -1);
            btnStartService.setText(counter + "");
        }
    }
}
