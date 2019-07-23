package com.example.dragdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.net.Socket;

/**
 * 动画分类演示
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn15, btn_animator, btn_socket;

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
        btn15.setOnClickListener(this);
        btn_animator.setOnClickListener(this);
        btn_socket.setOnClickListener(this);
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
            default:
                break;
        }
    }

}
