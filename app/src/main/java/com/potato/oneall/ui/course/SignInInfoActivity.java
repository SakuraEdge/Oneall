package com.potato.oneall.ui.course;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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

public class SignInInfoActivity extends AppCompatActivity {
    JSONArray array = new JSONArray();
    String name;
    String classname;
    String msg;
    String courseName;
    OkHttpClient client = SSLPass.sslPass();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent gets = getIntent();
        classname = gets.getStringExtra("className");
        name = gets.getStringExtra("teacherName");
        courseName = gets.getStringExtra("courseName");
        setContentView(R.layout.class_info);

        TextView textView = findViewById(R.id.classname);
        textView.setText("课程签到情况");
        LinearLayout LinearLayout = findViewById(R.id.courseList);

        String url="https://223.247.140.116:35152/SelectStudent";

        Map<String,String> map = new HashMap<>();
        map.put("courseName",courseName);

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
                array = JSON.parseArray(str);
            }
        });
        initData(LinearLayout);

    }

    private void initData(LinearLayout linearLayout){
        new Handler().postDelayed(() -> {

            if (array.size()==0){
                LinearLayout linearLayout1 = new LinearLayout(this);
                linearLayout1.setPadding(70,300,70,100);
                LinearLayout linearLayout2 = new LinearLayout(this);
                linearLayout2.setPadding(20,20,20,20);
                linearLayout2.setBackgroundResource(R.drawable.border_black);
                TextView textView = new TextView(this);
                textView.setText("当前课程下没有学生");
                textView.setTextSize(18);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    textView.setForegroundGravity(Gravity.CENTER);
                }
                linearLayout2.addView(textView);
                linearLayout1.addView(linearLayout2);
                linearLayout.addView(linearLayout1);
            }

            for(int i=0; i<array.size(); i++){

                String str = JSON.toJSONString(array.get(i));
                JSONObject maps = JSON.parseObject(str);

                String classname = "姓名：" + maps.get("studentName");
                String sign = (String) maps.get("signIn");
                switch (Objects.requireNonNull(sign)){
                    case "0": msg = "签到状态：未签到";break;
                    case "1": msg = "签到状态：已签到";break;
                    case "2": msg = "签到状态：已请假";break;
                }


                LinearLayout linearLayout1 = new LinearLayout(this);
                LinearLayout linearLayout2 = new LinearLayout(this);
                LinearLayout linearLayout3 = new LinearLayout(this);

                linearLayout2.setPadding(20,10,20,30);
                linearLayout2.setOrientation(LinearLayout.VERTICAL);
                linearLayout2.setGravity(Gravity.CENTER);

                ImageView imageView = new ImageView(this);
                imageView.setPadding(10,0,20,0);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    imageView.setForegroundGravity(Gravity.CENTER|Gravity.START);
                }

                int num = (int) (Math.random() * 4 + 0);

                switch (num){
                    case 0:
                        imageView.setImageResource(R.mipmap.boqi_1);break;
                    case 1:
                        imageView.setImageResource(R.mipmap.boqi_2);break;
                    case 2:
                        imageView.setImageResource(R.mipmap.boqi_3);break;
                    case 3:
                        imageView.setImageResource(R.mipmap.boqi_4);break;
                }


                TextView textView = new TextView(this);
                textView.setText(classname);
                textView.setId(i);
                textView.setTextSize(22);
                textView.setTextColor(Color.BLACK);

                TextView textView1 = new TextView(this);
                textView1.setText(msg);
                textView1.setTextSize(14);
                textView1.setTextColor(Color.GRAY);
                textView1.setPadding(0,0,0,0);

                linearLayout1.setOnClickListener(new textListener(textView));
                linearLayout1.setPadding(20,20,20,20);
                linearLayout1.setGravity(Gravity.CENTER);


                linearLayout1.addView(imageView);
                linearLayout2.addView(textView);
                linearLayout2.addView(textView1);
                linearLayout1.addView(linearLayout2);
                linearLayout3.addView(linearLayout1);


                linearLayout.addView(linearLayout3);
            }
        },800);
    }

    class textListener implements View.OnClickListener {
        private final TextView textView;

        private textListener(TextView textView){
            this.textView = textView;
        }

        @Override
        public void onClick(View v) {
            String names = textView.getText().toString();
            names = names.replace("姓名：","");

            LinearLayout linearLayout1 = new LinearLayout(SignInInfoActivity.this);
            Button button = new Button(SignInInfoActivity.this);
            button.setText("已签到");
            button.setTextSize(20);
            button.setTextColor(Color.GREEN);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                button.setForegroundGravity(Gravity.CENTER);
            }

            Button button2 = new Button(SignInInfoActivity.this);
            button2.setText("未签到");
            button2.setTextSize(20);
            button2.setTextColor(Color.RED);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                button2.setForegroundGravity(Gravity.CENTER);
            }

            Button button3 = new Button(SignInInfoActivity.this);
            button3.setText("已请假");
            button3.setTextSize(20);
            button3.setTextColor(Color.GRAY);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                button3.setForegroundGravity(Gravity.CENTER);
            }

            linearLayout1.setPadding(10,10,10,10);
            linearLayout1.setOrientation(LinearLayout.VERTICAL);
            linearLayout1.addView(button);
            linearLayout1.addView(button2);
            linearLayout1.addView(button3);

            AlertDialog.Builder builder = new AlertDialog.Builder(SignInInfoActivity.this);
            builder.setTitle("学生签到维护：");
            builder.setView(linearLayout1);
            AlertDialog dialog = builder.show();
            String finalNames = names;
            button.setOnClickListener(v1 -> {
                dialog.dismiss();
                Map<String,String> map1 = new HashMap<>();
                map1.put("courseName",courseName);
                map1.put("studentName", finalNames);
                map1.put("signNum","1");
                String url1 = "https://223.247.140.116:35152/SignIn";
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
                        System.out.println(Objects.requireNonNull(response.body()).string());
                    }
                });
                recreate();
            });

            button2.setOnClickListener(v1 -> {
                dialog.dismiss();
                Map<String,String> map1 = new HashMap<>();
                map1.put("courseName",courseName);
                map1.put("studentName", finalNames);
                map1.put("signNum","0");
                String url1 = "https://223.247.140.116:35152/SignIn";
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
                        System.out.println(Objects.requireNonNull(response.body()).string());
                    }
                });
                recreate();
            });

            button3.setOnClickListener(v1 -> {
                dialog.dismiss();
                Map<String,String> map1 = new HashMap<>();
                map1.put("courseName",courseName);
                map1.put("studentName", finalNames);
                map1.put("signNum","2");
                String url1 = "https://223.247.140.116:35152/SignIn";
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
                        System.out.println(Objects.requireNonNull(response.body()).string());
                    }
                });
                recreate();
            });

        }
    }

}
