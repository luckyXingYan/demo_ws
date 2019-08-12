package com.example.dragdemo;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.dragdemo.iview.IClientStatus;
import com.example.dragdemo.iview.IServerStatus;

public class SocketActivity extends AppCompatActivity implements View.OnClickListener {

    private Button start_server, start_client, btn_send_msg_to_client, btn_send_msg_to_server;
    private TextView tvServer;
    private TextView tvClient;

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
        tvServer = (TextView) findViewById(R.id.tv_server);
        tvClient = (TextView) findViewById(R.id.tv_client);
        start_server.setOnClickListener(this);
        start_client.setOnClickListener(this);
        btn_send_msg_to_client.setOnClickListener(this);
        btn_send_msg_to_server.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_server://开启服务器
                TcpServer.startServer(new IServerStatus() {
                    @Override
                    public void waitClientConn() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvServer.setText("服务器状态：服务器等待客户端链接");
                            }
                        });
                    }

                    @Override
                    public void clientConnectted() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvServer.setText("服务器状态：服务器与客户端链接成功");
                            }
                        });
                    }

                    @Override
                    public void receivedMsg(final String msg) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvServer.setText("服务器收到客户端发来的消息：" + msg);
                            }
                        });
                    }
                });
                break;
            case R.id.start_client://开启客户端
                TcpClient.startCient(getIpAddressByWifi(), 8080, new IClientStatus() {
                    @Override
                    public void startClientConn() {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvClient.setText("客户端状态：开启客户端链接服务器");
                            }
                        });

                    }

                    @Override
                    public void connectSuccess() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvClient.setText("客户端状态：客户端链接服务器成功");
                            }
                        });

                    }

                    @Override
                    public void connectFailed() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvClient.setText("客户端状态：客户端链接服务器失败");
                            }
                        });

                    }

                    @Override
                    public void receivedMsg(final String msg) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvClient.setText("客户端收到服务器发来的消息：" + msg);
                            }
                        });
                    }
                });
                break;
            case R.id.btn_send_msg_to_client://发送时间戳给客户端
                TcpServer.sendTcpMsgToClient("服务端-->客户端==当前时间戳：" + System.currentTimeMillis());
                break;
            case R.id.btn_send_msg_to_server://发送时间戳给服务器
                TcpClient.sendTcpMsgToServer("客户端-->服务器==当前时间戳：" + System.currentTimeMillis());
                break;
            default:
                break;
        }
    }

    /**
     * 根据wifi 获取本地 ip 地址
     *
     * @return
     */
    private String getIpAddressByWifi() {
        //获取wifi服务
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        //判断wifi是否开启
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);//把不可用的wifi设置为可用的wifi
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        return intToIp(ipAddress);//将整型的ip地址转换成字符串
    }

    private String intToIp(int ipAddress) {
        return (ipAddress & 0xFF) + "." +
                ((ipAddress >> 8) & 0xFF) + "." +
                ((ipAddress >> 16) & 0xFF) + "." +
                (ipAddress >> 24 & 0xFF);
    }
}
