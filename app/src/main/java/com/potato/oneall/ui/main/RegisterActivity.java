package com.potato.oneall.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.potato.oneall.bean.Person;
import com.potato.oneall.ui.web.WebActivity3;
import com.potato.oneall.util.SSLPass;
import com.potato.timetable.R;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {

    static String msg = null;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        EditText userName = findViewById(R.id.reg_id);
        EditText password = findViewById(R.id.reg_pwd);
        EditText tel = findViewById(R.id.reg_tel);
        ImageButton button1 = findViewById(R.id.reg_check);
        TextView button2 = findViewById(R.id.login_switch);

        button1.setOnClickListener(v -> {
            OkHttpClient client = SSLPass.sslPass();
            String url="https://223.247.140.116:35152/Register";

            Gson gson = new Gson();
            Person person = new Person(userName.getText().toString(),password.getText().toString(),tel.getText().toString());
            String json = gson.toJson(person);

            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);

            Request request = new Request.Builder()
                    .post(requestBody)
                    .url(url)
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    e.printStackTrace();
                }
                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    msg = response.body().string();
                }
            });

            new Handler().postDelayed(() -> {
                    Toast.makeText(RegisterActivity.this,msg,Toast.LENGTH_SHORT).show();
            },1000);
        });

        button2.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }
}
