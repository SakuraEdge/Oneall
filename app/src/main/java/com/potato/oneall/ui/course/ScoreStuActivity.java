package com.potato.oneall.ui.course;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class ScoreStuActivity extends AppCompatActivity {
    JSONArray array = new JSONArray();
    String name;
    String classname;
    String courseName;
    String studentName;
    String msg;
    OkHttpClient client = SSLPass.sslPass();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent gets = getIntent();
        classname = gets.getStringExtra("className");
        courseName = gets.getStringExtra("courseName");
        setContentView(R.layout.class_info);
        TextView textView = findViewById(R.id.classname);
        LinearLayout linearLayout = findViewById(R.id.courseList);
        textView.setText(courseName);

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
        initData(linearLayout);

    }

    private void initData(LinearLayout linearLayout){
        new Handler().postDelayed(() -> {
            for(int i=0; i<array.size(); i++){

                String str = JSON.toJSONString(array.get(i));
                JSONObject maps = JSON.parseObject(str);

                String studentName = "姓名：" + maps.get("studentName");
                String score = "分数：" + maps.get("score");

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
                textView.setText(studentName);
                textView.setId(i);
                textView.setTextSize(22);
                textView.setTextColor(Color.BLACK);

                TextView textView1 = new TextView(this);
                textView1.setText(score);
                int scores = Integer.parseInt(String.valueOf(maps.get("score")));
                if (scores>=60){
                    textView1.setTextColor(Color.GRAY);
                }
                else{
                    textView1.setTextColor(Color.RED);
                }
                textView1.setTextSize(16);

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
    class textListener implements View.OnClickListener{
        private final TextView textView;

        private textListener(TextView textView) {
            this.textView = textView;
        }

        @Override
        public void onClick(View v) {
            LinearLayout linearLayout = new LinearLayout(ScoreStuActivity.this);
            linearLayout.setPadding(10,10,10,10);
            linearLayout.setOrientation(LinearLayout.VERTICAL);


            studentName = (String) textView.getText();
            studentName = studentName.replace("姓名：","");

            EditText editText = new EditText(ScoreStuActivity.this);
            editText.setTextSize(20);
            editText.setBackgroundResource(R.drawable.border_black);
            editText.setHint("输入分数");
            editText.setPadding(20,0,0,0);
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            editText.setTextColor(Color.BLACK);


            TextView textView1 = new TextView(ScoreStuActivity.this);
            textView1.setText("输入该学生分数：");
            textView1.setTextSize(20);

            linearLayout.addView(textView1);
            linearLayout.addView(editText);

            AlertDialog.Builder builder = new AlertDialog.Builder(ScoreStuActivity.this);
            builder.setView(linearLayout);
            builder.setNegativeButton("取消",null);
            builder.setPositiveButton("修改分数", (dialog, which) -> {

                    String url1="https://223.247.140.116:35152/UpdateScore";
                    Map<String,String> map1 = new HashMap<>();
                    map1.put("courseName",courseName);
                    map1.put("studentName",studentName);
                    map1.put("score",String.valueOf(editText.getText()));
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
                recreate();
                new Handler().postDelayed(() -> {
                    Toast.makeText(ScoreStuActivity.this,msg,Toast.LENGTH_SHORT).show();
                },500);
            }).show();
        }
    }
}
