package com.example.dragdemo;

import com.example.dragdemo.iview.IServerStatus;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Author: xingyan
 * @Date: 2019/7/22
 * @Desc:服务器
 */
public class TcpServer {
    private static ServerSocket serverSocket;
    private static Socket socket;

    /**
     * 初始化 ServerSocket + 绑定端口号
     * while(true) 死循环 + 调用 ServerSocket.accept 获得 Socket
     * 接受客户端消息 socket.InputStream.read(*)
     * 发送消息给客户端 socket.OutputStream().write(*)
     *
     * @param iServerStatus
     */
    public static void startServer(final IServerStatus iServerStatus) {
        if (serverSocket == null) {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        serverSocket = new ServerSocket(8080);
                        iServerStatus.waitClientConn();

                        /**
                         * 如果服务器面向多个客户的链接(比如多个APP)，要使用
                         * while + accept阻塞
                         */
                        while (true) {
                            socket = serverSocket.accept();
                            iServerStatus.clientConnectted();
                            receivedMsg(iServerStatus);//接收数据

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }).start();
        }
    }

    private static void receivedMsg(final IServerStatus iServerStatus) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream inputStream = socket.getInputStream();
                    byte[] buffer = new byte[1024];
                    int len = -1;
                    while ((len = inputStream.read(buffer)) != -1) {
                        String data = new String(buffer, 0, len);
                        iServerStatus.receivedMsg(data);
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

    /**
     * 服务端发送消息给客户端
     *
     * @param msg
     */
    public static void sendTcpMsgToClient(final String msg) {

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
