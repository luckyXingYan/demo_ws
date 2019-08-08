package com.example.dragdemo;

import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;

public class MediaPlayerActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnPlay;
    private Button btnPouse;
    private Button btnStop;
    private SeekBar sekBar;
    private TextView tvCurrentTime;
    private TextView tvTotalTime;

    private String url = "http://jzvd.nathen.cn/342a5f7ef6124a4a8faf00e738b8bee4/cf6d9db0bd4d41f59d09ea0a81e918fd-5287d2089db37e62345123a1be272f8b.mp4";
    private MediaPlayer mediaPlayer;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x01:
                    int currentTime = Math.round(mediaPlayer.getCurrentPosition() / 1000);
                    String currentStr = String.format("%s%02d:%02d", "当前时间：", currentTime / 60, currentTime % 60);
                    tvCurrentTime.setText(currentStr);
                    sekBar.setProgress(mediaPlayer.getCurrentPosition());
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);

        initView();

        initMediaPlayer();


    }

    private void initMediaPlayer() {
        //这时就不用调用setDataSource了
//        MediaPlayer mediaPlayer = MediaPlayer.create(this, 0);

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        btnPlay = (Button) findViewById(R.id.btn_play);
        btnPouse = (Button) findViewById(R.id.btn_pouse);
        btnStop = (Button) findViewById(R.id.btn_stop);
        sekBar = (SeekBar) findViewById(R.id.sekBar);
        tvCurrentTime = (TextView) findViewById(R.id.tv_current_time);
        tvTotalTime = (TextView) findViewById(R.id.tv_total_time);

        btnPlay.setOnClickListener(this);
        btnPouse.setOnClickListener(this);
        btnStop.setOnClickListener(this);

        //监听 seekBar 拖动事件 -- 更新 mediaPlayer 进度
        sekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer != null) {
                    mediaPlayer.seekTo(seekBar.getProgress());
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_play://播放
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();

                    tvCurrentTime.setText("当前时间：00:00");//初始化当前播放时长
                    int totalTime = Math.round(mediaPlayer.getDuration() / 1000);//获取视频总时长
                    String totalTimeStr = String.format("%02d:%02d", totalTime / 60, totalTime % 60);//格式化总时长
                    tvTotalTime.setText("总时长：" + totalTimeStr);//设置总时长
                    sekBar.setMax(mediaPlayer.getDuration());//设置sekBar最大长度

                    //异步播放视频，handler通知ui刷新当前播放时长+进度
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            handler.sendEmptyMessage(0x01);
                        }
                    }, 1000);//一秒更新下进度
                }
                break;
            case R.id.btn_pouse://暂停
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                }
                break;
            case R.id.btn_stop://停止
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.reset();
                    initMediaPlayer();
                }
                break;

            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        if (handler != null) {
            handler.removeCallbacks(null);
            handler = null;
        }
    }
}
