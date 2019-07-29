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
public class BinderService extends Service {

    private final static String TAG = "---BinderService";

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate");
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "onBind");
        return new MyBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e(TAG, "onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    public void onRebind(Intent intent) {
        Log.e(TAG, "onRebind");
        super.onRebind(intent);
    }

    public void doAnything(String str) {
        Log.e(TAG, "doAnything：" + str);
    }

    public class MyBinder extends Binder {

        public BinderService getBinderService() {
            return BinderService.this;
        }

    }
}
