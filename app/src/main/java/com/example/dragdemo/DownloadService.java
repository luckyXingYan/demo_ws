package com.example.dragdemo;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

/**
 * @Author: xingyan
 * @Date: 2019/7/29
 * @Desc:
 */
public class DownloadService extends Service {

    private final static String TAG = "---DownloadService";

    public static final int MAX_PROGRESS = 100;//进度条的最大值
    private int progress = 0;//进度条的进度值

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new DownloadBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 模拟下载任务，每秒更新一次
     */
    public void startDownload(String id) {
        Log.e(TAG, id);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (progress < MAX_PROGRESS) {
                    progress += 5;
                    //接口回调 通知更新 进度
                    if (updateProgressListener != null) {
                        updateProgressListener.updateProgress(progress);
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

    public class DownloadBinder extends Binder {
        public DownloadService getService() {
            return DownloadService.this;
        }
    }

    private UpdateProgressListener updateProgressListener;

    public interface UpdateProgressListener {
        void updateProgress(int progress);
    }

    public void setUpdateProgressListener(UpdateProgressListener updateProgressListener) {
        this.updateProgressListener = updateProgressListener;
    }

}
