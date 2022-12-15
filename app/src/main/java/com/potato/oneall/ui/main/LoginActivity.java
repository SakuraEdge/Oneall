package com.potato.oneall.ui.main;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.potato.oneall.bean.Person;
import com.potato.oneall.ui.param.DBHelper;
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


public class LoginActivity extends AppCompatActivity {

    static boolean is_login = false;
    static String post = null;
    static String classname = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DBHelper dbHelper = new DBHelper(LoginActivity.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        @SuppressLint("Recycle") Cursor cursor = db.rawQuery("select * from login_info",null);
        cursor.moveToFirst();
        String names = cursor.getString(cursor.getColumnIndex("name"));
        String posts = cursor.getString(cursor.getColumnIndex("post"));

        System.out.println(names+" "+posts);

        if (names!=null){
            Intent intent = new Intent();
            if(posts.equals("2")){
                intent.setClass(LoginActivity.this, MenuStuActivity.class);
            }
            else{
                intent.setClass(LoginActivity.this, MenuTeaActivity.class);
            }
            startActivity(intent);
            cursor.close();
            finish();
        }

        setContentView(R.layout.login);
        EditText userName = findViewById(R.id.login_id);
        EditText password = findViewById(R.id.login_pwd);
        ImageButton button1 = findViewById(R.id.login_check);
        TextView button2 = findViewById(R.id.reg_switch);



        button1.setOnClickListener(v -> {
            OkHttpClient client = SSLPass.sslPass();
            String url="https://223.247.140.116:35152/Login";

            Person person = new Person(userName.getText().toString(),password.getText().toString(),null);
            String json = JSON.toJSONString(person);

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
                    String str = Objects.requireNonNull(response.body()).string();
                    JSONObject jsonObject = JSON.parseObject(str);
                    System.out.println(jsonObject);
                    if (jsonObject!=null){
                        is_login = true;
                        post = jsonObject.get("post").toString();
                        classname = jsonObject.get("classname").toString();
                    }
                }
            });
            new Handler().postDelayed(() -> {
                if (is_login){
                    Toast.makeText(LoginActivity.this,"登录成功，即将进入页面...",Toast.LENGTH_SHORT).show();

                    db.execSQL("update login_info set name = ?",new String[]{userName.getText()+""});
                    db.execSQL("update login_info set pwd = ?",new String[]{password.getText()+""});
                    db.execSQL("update login_info set post = ?",new String[]{post});
                    db.execSQL("update login_info set classname = ?",new String[]{classname});

                    if (Objects.equals(post, "2")){
                        Intent intent = new Intent();
                        intent.setClass(LoginActivity.this, MenuStuActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        Intent intent = new Intent();
                        intent.setClass(LoginActivity.this, MenuTeaActivity.class);
                        startActivity(intent);
                        finish();
                    }

                }

                else
                    Toast.makeText(LoginActivity.this,"账号或密码错误！",Toast.LENGTH_SHORT).show();
            },1000);

        });

        button2.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

}