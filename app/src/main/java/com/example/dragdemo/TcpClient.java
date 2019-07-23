package com.example.dragdemo;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * @Author: xingyan
 * @Date: 2019/7/22
 * @Desc:客户端
 */
public class TcpClient {
    private static Socket socket;

    /**
     * 初始化 Socket 绑定ip地址 + 端口号
     * 接受服务器发来的消息，socket.getInputStream().read(*)
     * 发送消息给服务器，socket.getOutputStream().write(*）
     * @param address
     * @param port
     * @param iClientStatus
     */

    public static void startCient(final String address, final int port, final IClientStatus iClientStatus) {
        if (address == null) {
            return;
        }
        if (socket == null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        socket = new Socket(address, port);
                        iClientStatus.startClientConn();//启动客户端
                        iClientStatus.connectSuccess();//客户端链接服务器成功

                        InputStream inputStream = socket.getInputStream();
                        byte[] buffer = new byte[1024];
                        int len = -1;
                        while ((len = inputStream.read(buffer)) != -1) {
                            String data = new String(buffer, 0, len);
                            iClientStatus.receivedMsg(data);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (socket != null) {
                            try {
                                socket.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        socket = null;
                    }

                }
            }).start();
        }
    }

    public static void sendTcpMsgToServer(final String msg) {
        if (socket != null && socket.isConnected()) {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        socket.getOutputStream().write(msg.getBytes());
                        socket.getOutputStream().flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
//                        if (socket != null) {
//                            try {
//                                socket.shutdownOutput();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                        socket = null;
                    }

                }
            }).start();
        }
    }
}
