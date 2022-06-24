package com.potato.oneall.ui.main;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.potato.timetable.R;


public class WelcomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_main);
        //两秒钟进入主页面
        new Handler().postDelayed(() -> {
            //执行在主线程
            //启动主页面
            startActivity(new Intent(WelcomeActivity.this,Main0Activity.class));
            //关闭当前页面
            finish();
        },2000);
    }

}