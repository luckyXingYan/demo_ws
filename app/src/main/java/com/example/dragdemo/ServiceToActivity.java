package com.example.dragdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * activity 和 servise 通信的方式
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
                startActivity(new Intent(this,ServiceBinderActivity.class));
                break;
            case R.id.btn_broadcast://通过 Broadcast 广播
                break;
            default:
                break;
        }
    }
}
