package com.example.dragdemo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

/**
 * Binder  === bindService === 只适用于本应用程序内适用，即组件和Service在同一个进程中
 * 给清单文件的 <service> 配置此属性  android:process=":remote"  会报错
 * <p>
 * 创建服务 service -- 服务中创建 Binder 对象， 在 Binder 对象中创建获取当前服务对象的方法 --
 * 服务中 onBind 方法中返回 binder 对象 -- activity 中绑定服务 --
 * activity 中创建 ServiceConnection 复写方法 onServiceConnected -- onServiceConnected 拿到 IBinder 对象，
 * 通过 IBinder 获取 DownloadService 对象 -- 通过 DownloadService 调 服务的下载方法 可以参数的形式传入下载的资源id --
 * 服务中通过接口回调 把数据 回传给activity -- 并在 onServiceConnected 方法中 通过 DownloadService 设置更新监听器，更新 progressbar
 */
public class ServiceBinderActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "--ServiceBinderActivity";
    private Button btnBindService;
    private Button btnMsgService;

    private Button btnBindDownloadService;
    private Button btnDownloadService;
    private ProgressBar progressBar;

    private Intent serviceIntent, startDownloadIntent;
    private BinderService binderService;
    private DownloadService downloadService;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
            Log.e(TAG, "serviceConnection===onServiceConnected");
            BinderService.MyBinder myBinder = (BinderService.MyBinder) iBinder;
            binderService = myBinder.getBinderService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    private ServiceConnection serviceConnectionDownload = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            Log.e(TAG, "serviceConnectionDownload===onServiceConnected");

            DownloadService.DownloadBinder downloadBinder = (DownloadService.DownloadBinder) service;
            downloadService = downloadBinder.getService();

            downloadService.setUpdateProgressListener(new DownloadService.UpdateProgressListener() {
                @Override
                public void updateProgress(int progress) {
                    progressBar.setProgress(progress);
                }
            });

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_binder);
        initView();
    }

    private void initView() {
        btnBindService = (Button) findViewById(R.id.btn_bind_service);
        btnMsgService = (Button) findViewById(R.id.btn_msg_service);

        btnBindDownloadService = (Button) findViewById(R.id.btn_bind_download_service);
        btnDownloadService = (Button) findViewById(R.id.btn_download_service);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);


        btnBindService.setOnClickListener(this);
        btnMsgService.setOnClickListener(this);
        btnBindDownloadService.setOnClickListener(this);
        btnDownloadService.setOnClickListener(this);

//        serviceIntent = new Intent(this, BinderService.class);
        startDownloadIntent = new Intent(this, DownloadService.class);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_bind_service://绑定服务
                if (serviceIntent != null) {
                    bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
                }
                break;
            case R.id.btn_msg_service://Binder 通信
                if (binderService != null) {
                    binderService.doAnything("通信");
                }
                break;
            case R.id.btn_bind_download_service://绑定下载服务
                if (startDownloadIntent != null) {
                    bindService(startDownloadIntent, serviceConnectionDownload, Context.BIND_AUTO_CREATE);
                }
                break;
            case R.id.btn_download_service://下载开始
                if (downloadService != null) {
                    downloadService.startDownload("下载资源id");
                }
                break;
            default:
                break;
        }
    }

    /**
     * 解绑
     */
    @Override
    protected void onDestroy() {
//            unbindService(serviceConnection);
        unbindService(serviceConnectionDownload);
        super.onDestroy();
    }
}
