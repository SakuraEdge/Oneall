package com.potato.oneall.ui.course;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.potato.oneall.ui.param.DBHelper;
import com.potato.oneall.util.SSLPass;
import com.potato.timetable.R;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CourseInfoActivity extends AppCompatActivity {
    OkHttpClient client = SSLPass.sslPass();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent gets = getIntent();
        String courseName = gets.getStringExtra("courseName");
        String name = gets.getStringExtra("name");
        String classname = gets.getStringExtra("classname");

        setContentView(R.layout.course_info);
        TextView textView = findViewById(R.id.classname);
        ImageButton signInButton = findViewById(R.id.signInButton);
        String title = courseName;
        textView.setText(title);

        OkHttpClient client = SSLPass.sslPass();
        String url="https://223.247.140.116:35152/IsSignIn";
        Map<String,String> map = new HashMap<>();
        map.put("courseName",courseName);
        map.put("name",name);

        String json = JSON.toJSONString(map);
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
                String str = (Objects.requireNonNull(response.body()).string());
                //array = JSON.parseArray(str);
            }
        });

        signInButton.setOnClickListener(v -> {
            LinearLayout linearLayout1 = new LinearLayout(this);
            linearLayout1.setPadding(20, 20, 20, 20);
            linearLayout1.setOrientation(android.widget.LinearLayout.VERTICAL);

            EditText time = new EditText(this);
            time.setHint("请输入持续时长（分钟）");
            time.setBackgroundResource(R.drawable.border_black);
            time.setPadding(20, 20, 20, 20);

            EditText code = new EditText(this);
            code.setHint("请输入四位数字验证码（留空则随机）");
            code.setBackgroundResource(R.drawable.border_black);
            code.setPadding(20, 20, 20, 20);


            linearLayout1.addView(time);
            linearLayout1.addView(code);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("开启签到")
                    .setView(linearLayout1).setNegativeButton("取消", null);
            builder.setPositiveButton("创建", (dialog, which) -> {
                String times = time.getText().toString();
                String codes = code.getText().toString();
                Map<String,String> map1 = new HashMap<>();
                map1.put("time",times);
                map1.put("code",codes);
                map1.put("courseName",courseName);
                map1.put("classname",classname);
                String url1 = "https://223.247.140.116:35152/AddSignIn";
                String json1 = JSON.toJSONString(map);
                RequestBody requestBody1 = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json1);

                Request request1 = new Request.Builder()
                        .post(requestBody1)
                        .url(url1)
                        .build();
                Call call1 = client.newCall(request1);
                call1.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        e.printStackTrace();
                    }
                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        System.out.println(Objects.requireNonNull(response.body()).string());
                    }
                });
                recreate();
            }).show();

        });
    }

}
