package com.potato.oneall.ui.course;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.solver.SolverVariable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.potato.oneall.util.SSLPass;
import com.potato.timetable.R;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
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

public class StuCourseInfoActivity extends AppCompatActivity {
    JSONObject array;
    JSONObject array1;
    String msg;
    String classname;
    String name;
    String code;
    String isSignIn;
    String courseName;
    OkHttpClient client = SSLPass.sslPass();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent gets = getIntent();
        classname = gets.getStringExtra("classname");
        name = gets.getStringExtra("name");
        courseName = gets.getStringExtra("courseName");
        setContentView(R.layout.course_stuinfo);
        TextView textView = findViewById(R.id.classname);
        LinearLayout linearLayout = findViewById(R.id.signInLy);
        String title = courseName;
        textView.setText(title);

        String url="https://223.247.140.116:35152/SelectSign";
        Map<String,String> map = new HashMap<>();
        map.put("courseName",courseName);
        map.put("classname",classname);

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
                array = JSON.parseObject(str);
            }
        });

        String url1="https://223.247.140.116:35152/SelectSignIn";
        Map<String,String> map1 = new HashMap<>();
        map1.put("courseName",courseName);
        map1.put("classname",classname);
        map1.put("studentName",name);
        String json1 = JSON.toJSONString(map1);
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
                String str = (Objects.requireNonNull(response.body()).string());
                array1 = JSON.parseObject(str);
            }
        });


        new Handler().postDelayed(() -> {
            initData(linearLayout);
        },500);

    }

    private void initData(LinearLayout linearLayout){
        if(array!=null){
            linearLayout.removeAllViews();
            linearLayout.setPadding(10,0,10,50);
            code = (String) array.get("code");
            isSignIn = (String) array1.get("isSignIn");
            LinearLayout linearLayout1 = new LinearLayout(this);
            linearLayout1.setBackgroundColor(Color.WHITE);
            linearLayout1.setOrientation(LinearLayout.VERTICAL);

            ImageView imageView = new ImageView(this);
            imageView.setPadding(10,10,10,10);
            imageView.setImageResource(R.mipmap.no_signin);

            LinearLayout linearLayout2 = new LinearLayout(this);
            linearLayout2.setOrientation(LinearLayout.VERTICAL);

            Button button = new Button(this);
            button.setTextSize(20);
            switch (isSignIn){
                case "0": button.setText("点击签到");button.setTextColor(Color.BLACK);
                button.setOnClickListener(new buttonListener());break;
                case "1": button.setText("已签到");button.setTextColor(Color.GRAY);break;
                case "2": button.setText("已请假");button.setTextColor(Color.GRAY);break;
            }

            LinearLayout titleLine = new LinearLayout(this);
            TextView title = new TextView(this);
            title.setText("当前已发起签到");
            title.setPadding(10,10,10,10);
            title.setTextSize(20);
            title.setTextColor(Color.BLACK);

            title.setBackgroundResource(R.drawable.border_black);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                title.setForegroundGravity(Gravity.TOP|Gravity.START);
            }


            TextView textView = new TextView(this);
            textView.setTextSize(24);
            textView.setTextColor(Color.BLACK);
            textView.setGravity(Gravity.CENTER);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                textView.setForegroundGravity(Gravity.CENTER);
            }


            TextView textView1 = new TextView(this);
            textView1.setTextSize(20);
            textView1.setGravity(Gravity.CENTER);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                textView1.setForegroundGravity(Gravity.CENTER);
            }
            String msg;


            msg = "签到截止时间："+array.getString("time");
            textView1.setText(msg);

            titleLine.addView(title);
            linearLayout2.addView(textView);
            linearLayout2.addView(textView1);
            linearLayout1.addView(titleLine);
            linearLayout1.addView(imageView);
            linearLayout1.addView(linearLayout2);
            linearLayout1.addView(button);
            linearLayout.addView(linearLayout1);


        }
    }

    class buttonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            LinearLayout linearLayout1 = new LinearLayout(StuCourseInfoActivity.this);
            linearLayout1.setPadding(10,10,10,10);
            linearLayout1.setOrientation(LinearLayout.VERTICAL);

            TextView textView = new TextView(StuCourseInfoActivity.this);
            textView.setText("输入当前签到码：");
            textView.setTextSize(20);

            EditText editText = new EditText(StuCourseInfoActivity.this);
            editText.setBackgroundResource(R.drawable.border_black);
            editText.setTextSize(20);
            editText.setPadding(20,0,0,0);
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            editText.setHint("签到：");
            editText.setTextColor(Color.BLACK);

            linearLayout1.addView(textView);
            linearLayout1.addView(editText);

            AlertDialog.Builder builder = new AlertDialog.Builder(StuCourseInfoActivity.this);
            builder.setView(linearLayout1);
            builder.setNegativeButton("取消",null);
            builder.setPositiveButton("签到", (dialog, which) -> {
                System.out.println(code);
                System.out.println(editText.getText().toString());
                if (!editText.getText().toString().equals(code)){
                    msg = "签到码错误，请重试！";
                }
                else{
                    String url1="https://223.247.140.116:35152/SignIn";
                    Map<String,String> map1 = new HashMap<>();
                    map1.put("courseName",courseName);
                    map1.put("studentName",name);
                    map1.put("signNum","1");
                    String json1 = JSON.toJSONString(map1);
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
                            msg = (Objects.requireNonNull(response.body()).string());
                        }
                    });
                }
                recreate();
                new Handler().postDelayed(() -> {
                    Toast.makeText(StuCourseInfoActivity.this,msg,Toast.LENGTH_SHORT).show();
                },500);
            }).show();
        }
    }

}
