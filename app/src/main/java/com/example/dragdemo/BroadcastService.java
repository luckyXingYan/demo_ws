package com.example.dragdemo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 邢燕 on 2019/7/29 20:52.
 * Email:lucky_xyic@sina.cn
 * ToDo:
 */
public class BroadcastService extends Service {
    private int counter;
    private final static String TAG = "---BroadcastService";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate");
        super.onCreate();
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        counter = intent.getIntExtra("counter", -1);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                //通过 广播 发送数据给 activity
                Intent counterIntent = new Intent();
                counterIntent.putExtra("counter", counter);
                counterIntent.setAction("com.example.dragdemo.COUNTER_ACTION");
                sendBroadcast(counterIntent);
                counter++;
            }
        }, 0, 1000);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
    }
}
