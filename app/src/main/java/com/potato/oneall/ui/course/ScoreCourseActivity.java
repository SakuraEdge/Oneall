package com.potato.oneall.ui.course;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
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
import com.potato.oneall.ui.param.DBHelper;
import com.potato.oneall.util.SSLPass;
import com.potato.timetable.R;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
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

public class ScoreCourseActivity extends AppCompatActivity {
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


        String names = classname;
        TextView textView = findViewById(R.id.classname);
        textView.setText(names);
        LinearLayout LinearLayout = findViewById(R.id.courseList);

        String url="https://223.247.140.116:35152/SelectCourse";

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
        initData(LinearLayout);

    }

    private void initData(LinearLayout linearLayout) {
        new Handler().postDelayed(() -> {

            if (array.size() == 0) {
                LinearLayout linearLayout1 = new LinearLayout(this);
                linearLayout1.setPadding(70, 300, 70, 100);
                LinearLayout linearLayout2 = new LinearLayout(this);
                linearLayout2.setPadding(20, 20, 20, 20);
                linearLayout2.setBackgroundResource(R.drawable.border_black);
                TextView textView = new TextView(this);
                textView.setText("当前班级下未有课程，请右上角新建课程");
                textView.setTextSize(18);
                linearLayout2.addView(textView);
                linearLayout1.addView(linearLayout2);
                linearLayout.addView(linearLayout1);
            }

            for (int i = 0; i < array.size(); i++) {
                String str = JSON.toJSONString(array.get(i));
                JSONObject maps = JSON.parseObject(str);

                String isOver = (String) maps.get("isOver");

                if (Objects.equals(isOver, "true")) {
                    String classname = "课程名称：" + maps.get("name");


                    String msg = "课程地址：" + maps.get("address") + " \t \t" + "授课老师：" + maps.get("teacherName");

                    LinearLayout linearLayout1 = new LinearLayout(this);
                    LinearLayout linearLayout2 = new LinearLayout(this);
                    LinearLayout linearLayout3 = new LinearLayout(this);

                    linearLayout2.setPadding(20, 10, 20, 30);
                    linearLayout2.setOrientation(LinearLayout.VERTICAL);
                    linearLayout2.setGravity(Gravity.CENTER);

                    ImageView imageView = new ImageView(this);
                    imageView.setPadding(10, 0, 20, 0);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        imageView.setForegroundGravity(Gravity.CENTER | Gravity.START);
                    }

                    int num = (int) (Math.random() * 4 + 0);

                    switch (num) {
                        case 0:
                            imageView.setImageResource(R.mipmap.boqi_1);
                            break;
                        case 1:
                            imageView.setImageResource(R.mipmap.boqi_2);
                            break;
                        case 2:
                            imageView.setImageResource(R.mipmap.boqi_3);
                            break;
                        case 3:
                            imageView.setImageResource(R.mipmap.boqi_4);
                            break;
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
                    textView1.setPadding(0, 0, 0, 0);

                    linearLayout1.setOnClickListener(new textListener(textView));
                    linearLayout1.setPadding(20, 20, 20, 20);
                    linearLayout1.setGravity(Gravity.CENTER);


                    linearLayout1.addView(imageView);
                    linearLayout2.addView(textView);
                    linearLayout2.addView(textView1);
                    linearLayout1.addView(linearLayout2);
                    linearLayout3.addView(linearLayout1);


                    linearLayout.addView(linearLayout3);
                }
            }
        }, 800);
    }

    class textListener implements View.OnClickListener {
        private final TextView textView;

        private textListener(TextView textView){
            this.textView = textView;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            String courseName = textView.getText().toString();

            courseName = courseName.replace("课程名称：","");

            intent.putExtra("courseName",courseName);
            intent.putExtra("classname",classname);
            intent.setClass(ScoreCourseActivity.this,ScoreStuActivity.class);
            startActivity(intent);
        }
    }

}
