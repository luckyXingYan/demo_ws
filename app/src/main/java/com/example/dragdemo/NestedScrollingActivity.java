package com.example.dragdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * NestedScrolling是嵌套滑动机制（Nested是嵌套的意思），为了完成父和子之间优雅的滑动效果而引出的机制。
 */
public class NestedScrollingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nested_scrolling);
    }
}
