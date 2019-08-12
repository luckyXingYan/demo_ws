package com.example.dragdemo.iview;

/**
 * @Author: xingyan
 * @Date: 2019/7/23
 * @Desc:服务端状态
 */
public interface IServerStatus {
    void waitClientConn();//等待客户端链接

    void clientConnectted();//客户端已连接

    void receivedMsg(String msg);//接收客户端的消息
}
