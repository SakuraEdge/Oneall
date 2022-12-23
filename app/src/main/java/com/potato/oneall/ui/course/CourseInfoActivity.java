package com.potato.oneall.ui.course;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
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
import com.potato.oneall.ui.param.DBHelper;
import com.potato.oneall.util.SSLPass;
import com.potato.timetable.R;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CourseInfoActivity extends AppCompatActivity {
    JSONObject array;
    String courseName;
    String name;
    String classname;
    String code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent gets = getIntent();
        courseName = gets.getStringExtra("courseName");
        name = gets.getStringExtra("name");
        classname = gets.getStringExtra("classname");

        setContentView(R.layout.course_info);
        TextView textView = findViewById(R.id.classname);
        LinearLayout linearLayout = findViewById(R.id.signInLy);
        ImageButton signInButton = findViewById(R.id.signInButton);
        String title = courseName;
        textView.setText(title);

        OkHttpClient client = SSLPass.sslPass();
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

        new Handler().postDelayed(() -> {
            initData(linearLayout);
            },500);


        signInButton.setOnClickListener(v -> {
            LinearLayout linearLayout1 = new LinearLayout(this);
            linearLayout1.setPadding(20, 20, 20, 20);
            linearLayout1.setOrientation(android.widget.LinearLayout.VERTICAL);

            EditText time = new EditText(this);
            time.setHint("请输入持续时长（分钟）");
            time.setBackgroundResource(R.drawable.border_black);
            time.setInputType(InputType.TYPE_CLASS_NUMBER);
            time.setPadding(20, 20, 20, 20);

            EditText code = new EditText(this);
            code.setHint("请输入四位数字验证码（留空则随机）");
            code.setInputType(InputType.TYPE_CLASS_NUMBER);
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
                String num = "";
                if (codes.equals("")){
                    num = String.valueOf((int)((Math.random()*9+1)*1000));
                }
                else{
                    num = codes;
                }

                System.out.println(times);
                Map<String,String> map1 = new HashMap<>();
                map1.put("time",times);
                map1.put("code",num);
                map1.put("courseName",courseName);
                map1.put("classname",classname);
                String url1 = "https://223.247.140.116:35152/ResetSign";
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
                        System.out.println(Objects.requireNonNull(response.body()).string());
                    }
                });
                recreate();
            }).show();

        });
    }

    private void initData(LinearLayout linearLayout){
        if(array!=null){
            linearLayout.removeAllViews();
            linearLayout.setPadding(10,0,10,50);

            LinearLayout linearLayout1 = new LinearLayout(this);
            linearLayout1.setBackgroundColor(Color.WHITE);
            linearLayout1.setOrientation(LinearLayout.VERTICAL);

            ImageView imageView = new ImageView(this);
            imageView.setPadding(10,10,10,10);
            imageView.setImageResource(R.mipmap.no_signin);

            LinearLayout linearLayout2 = new LinearLayout(this);
            linearLayout2.setOrientation(LinearLayout.VERTICAL);

            LinearLayout titleLine = new LinearLayout(this);
            TextView title = new TextView(this);
            title.setText("当前已发起签到");
            title.setPadding(10,10,10,10);
            title.setTextSize(20);
            title.setTextColor(Color.BLACK);

            TextView title2 = new TextView(this);
            title2.setPadding(250,10,10,10);
            title2.setTextSize(18);
            title2.setText("查看签到情况>>");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                title2.setForegroundGravity(Gravity.END);
            }
            title2.setOnClickListener(new textListener(title2));

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

            String msg = "签到码："+array.getString("code");
            textView.setText(msg);

            TextView textView1 = new TextView(this);
            textView1.setTextSize(20);
            textView1.setGravity(Gravity.CENTER);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                textView1.setForegroundGravity(Gravity.CENTER);
            }

            msg = "签到截止时间："+array.getString("time");
            textView1.setText(msg);

            titleLine.addView(title);
            titleLine.addView(title2);
            linearLayout2.addView(textView);
            linearLayout2.addView(textView1);
            linearLayout1.addView(titleLine);
            linearLayout1.addView(imageView);
            linearLayout1.addView(linearLayout2);
            linearLayout.addView(linearLayout1);

        }
    }

    class textListener implements View.OnClickListener {
        private final TextView textView;

        private textListener(TextView textView){
            this.textView = textView;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.putExtra("courseName",courseName);
            intent.putExtra("name",name);
            intent.putExtra("classname",classname);
            intent.setClass(CourseInfoActivity.this,SignInInfoActivity.class);
            startActivity(intent);
        }
    }

}
