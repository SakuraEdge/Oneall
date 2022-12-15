package com.potato.oneall.ui.web;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.potato.timetable.R;

public class WebActivity extends AppCompatActivity {
    @SuppressLint("SetJavaScriptEnabled")
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_main);

        WebView mMVmhtml = (WebView) findViewById(R.id.webView);
        //获取控件对象
        WebSettings webSettings = mMVmhtml.getSettings();

        webSettings.setDisplayZoomControls(false);
        //设置关闭缩放

        mMVmhtml.setWebChromeClient(new WebChromeClient());
        //用Chrome启动

        mMVmhtml.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        mMVmhtml.getSettings().setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        mMVmhtml.loadUrl("https://www.aiit.edu.cn/?gkfrom=card");
    }
}
