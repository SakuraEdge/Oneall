package com.potato.oneall.ui.web;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.potato.oneall.ui.param.DBHelper;

import androidx.appcompat.app.AppCompatActivity;

import com.potato.timetable.R;

public class WebActivity3 extends AppCompatActivity {
    @SuppressLint("SetJavaScriptEnabled")
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_main);

        WebView mMVhtml = findViewById(R.id.webView);
        //获取控件对象
        WebSettings webSettings = mMVhtml.getSettings();

        webSettings.setDisplayZoomControls(false);
        //设置关闭缩放

        DBHelper dbHelper = new DBHelper(WebActivity3.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery("select * from param",null);
        cursor.moveToFirst();

        mMVhtml.setWebChromeClient(new WebChromeClient());
        mMVhtml.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        mMVhtml.getSettings().setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        mMVhtml.loadUrl(cursor.getString(cursor.getColumnIndex("http")));
    }
}
