package com.potato.oneall.ui.course;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.potato.oneall.ui.main.LoginActivity;
import com.potato.oneall.ui.main.WelcomeActivity;
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

public class ClassTeaListActivity extends AppCompatActivity {
    JSONArray array = new JSONArray();
    String name;
    String classname;
    String msg;
    OkHttpClient client = SSLPass.sslPass();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent gets = getIntent();
        classname = gets.getStringExtra("className");
        name = gets.getStringExtra("teacherName");
        setContentView(R.layout.class_tea_list);


        String names = classname;
        TextView textView = findViewById(R.id.classname);
        textView.setText(names);

        ImageButton courseList = findViewById(R.id.courseListButton);
        ImageButton stuList = findViewById(R.id.stuListButton);
        ImageButton stuAdd = findViewById(R.id.stuAddButton);
        ImageButton talkRoom = findViewById(R.id.talkRoomButton);


        courseList.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra("className",classname);
            intent.putExtra("teacherName",name);
            intent.setClass(ClassTeaListActivity.this,ClassTeaInfoActivity.class);
            startActivity(intent);
        });

        stuList.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra("className",classname);
            intent.setClass(ClassTeaListActivity.this,ClassStuInfoActivity.class);
            startActivity(intent);
        });

        talkRoom.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra("className",classname);
            intent.putExtra("name",name);
            intent.setClass(ClassTeaListActivity.this,ClassTalkRoomActivity.class);
            startActivity(intent);
        });


        stuAdd.setOnClickListener(v -> {
            LinearLayout linearLayout1 = new LinearLayout(this);
            linearLayout1.setPadding(20,20,20,20);
            linearLayout1.setOrientation(android.widget.LinearLayout.VERTICAL);

            EditText num = new EditText(this);
            num.setHint("请输入需要添加的学生学号");
            num.setBackgroundResource(R.drawable.border_black);
            num.setPadding(20,20,20,20);
            linearLayout1.addView(num);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("添加学生（学号）")
                    .setView(linearLayout1).setNegativeButton("取消",null);

            builder.setPositiveButton("添加", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String id = num.getText().toString();

                    Map<String,String> map1 = new HashMap<>();
                    map1.put("classname",classname);
                    map1.put("studentID",id);
                    String url1 = "https://223.247.140.116:35152/UpdateClassName";
                    String json1 = JSON.toJSONString(map1);
                    RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json1);

                    Request request = new Request.Builder()
                            .post(requestBody)
                            .url(url1)
                            .build();
                    Call call1 = client.newCall(request);
                    call1.enqueue(new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            e.printStackTrace();
                        }
                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            msg = (Objects.requireNonNull(response.body()).string());
                        }
                    });
                }

            }).show();
        });


    }
}
