package com.example.dragdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SocketActivity extends AppCompatActivity implements View.OnClickListener {

    private Button start_server, start_client, btn_send_msg_to_client, btn_send_msg_to_server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket);
        intView();
    }

    private void intView() {
        start_server = findViewById(R.id.start_server);
        start_client = findViewById(R.id.start_client);
        btn_send_msg_to_client = findViewById(R.id.btn_send_msg_to_client);
        btn_send_msg_to_server = findViewById(R.id.btn_send_msg_to_server);
        start_server.setOnClickListener(this);
        start_client.setOnClickListener(this);
        btn_send_msg_to_client.setOnClickListener(this);
        btn_send_msg_to_server.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_server://开启服务器
                break;
            case R.id.start_client://开启客户端
                break;
            case R.id.btn_send_msg_to_client://发送时间戳给客户端
                break;
            case R.id.btn_send_msg_to_server://发送时间戳给服务器
                break;
            default:
                break;
        }
    }
}
