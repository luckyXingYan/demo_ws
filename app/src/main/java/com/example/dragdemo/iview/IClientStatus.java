package com.example.dragdemo.iview;

/**
 * @Author: xingyan
 * @Date: 2019/7/23
 * @Desc:客户端状态
 */
public interface IClientStatus {
    void startClientConn();//启动客户端socket链接

    void connectSuccess();//链接服务器成功

    void connectFailed();//链接服务器失败

    void receivedMsg(String msg);//接受服务端的消息
}
