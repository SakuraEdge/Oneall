package com.potato.oneall.ui.course;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
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

public class ClassStuInfoActivity extends AppCompatActivity {
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
        setContentView(R.layout.class_info);
        String names = classname+" 班";
        TextView textView = findViewById(R.id.classname);
        LinearLayout linearLayout = findViewById(R.id.courseList);
        textView.setText(names);

        String url="https://223.247.140.116:35152/SelectStudentByClassname";

        Map<String,String> map = new HashMap<>();
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
                array = JSON.parseArray(str);
            }
        });
        initData(linearLayout);

    }

    private void initData(LinearLayout linearLayout){
        new Handler().postDelayed(() -> {
            for(int i=0; i<array.size(); i++){

                String str = JSON.toJSONString(array.get(i));
                JSONObject maps = JSON.parseObject(str);

                String classname = "姓名：" + maps.get("name");
                String msg = "学号：" + maps.get("number");

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
                textView1.setTextSize(13);
                textView1.setTextColor(Color.GRAY);

                linearLayout1.setOnLongClickListener(new textListener(textView1));
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
    class textListener implements View.OnLongClickListener{
        private final TextView textView;

        private textListener(TextView textView) {
            this.textView = textView;
        }

        @Override
        public boolean onLongClick(View v) {
            TextView textView1 = new TextView(ClassStuInfoActivity.this);
            LinearLayout linearLayout1 = new LinearLayout(ClassStuInfoActivity.this);
            linearLayout1.setPadding(100,60,20,20);
            textView1.setPadding(20,20,20,20);
            textView1.setTextSize(15);
            textView1.setBackgroundResource(R.drawable.border_black);
            textView1.setTextColor(Color.RED);

            linearLayout1.addView(textView1);

            String number = textView.getText().toString();
            number = number.replace("学号：","");
            String msg = "是否将学号为"+number+"的学生\n移出当前班级？";
            textView1.setText(msg);
            AlertDialog.Builder builder = new AlertDialog.Builder(ClassStuInfoActivity.this);
            builder.setTitle("移除确认")
                    .setView(linearLayout1).setNegativeButton("取消",null);
            String finalClassname = number;

            builder.setPositiveButton("确认", (dialog, which) -> {
                Map<String,String> map = new HashMap<>();
                map.put("studentID", finalClassname);
                map.put("classname", "null");

                String url1 = "https://223.247.140.116:35152/UpdateClassName";
                String json1 = JSON.toJSONString(map);
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
            }).show();
            return true;
        }
    }
}
