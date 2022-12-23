package com.potato.oneall.ui.course;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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

public class ScoreClassActivity extends AppCompatActivity {
    JSONArray array = new JSONArray();
    String msg = null;
    String teacherName;
    OkHttpClient client = SSLPass.sslPass();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DBHelper dbHelper = new DBHelper(ScoreClassActivity.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        @SuppressLint("Recycle") Cursor cursor = db.rawQuery("select * from login_info",null);
        cursor.moveToFirst();
        teacherName = cursor.getString(cursor.getColumnIndex("name"));

        setContentView(R.layout.class_info);

        LinearLayout LinearLayout = findViewById(R.id.courseList);

        String url="https://223.247.140.116:35152/SelectClassByTeacherName";
        Map<String,String> map = new HashMap<>();
        map.put("teacherName",teacherName);
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

        cursor.close();

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
                textView.setText("当前未有管理的班级");
                textView.setTextSize(18);
                linearLayout2.addView(textView);
                linearLayout1.addView(linearLayout2);
                linearLayout.addView(linearLayout1);
            }

            for(int i=0; i<array.size(); i++){

                String str = JSON.toJSONString(array.get(i));
                JSONObject maps = JSON.parseObject(str);

                String classname = "班级名称：" + maps.get("classname");
                String msg = "最大人数：" + maps.get("maxNum") + " \t \t" + "班级主任："+maps.get("teacherName");
                LinearLayout linearLayout1 = new LinearLayout(this);
                LinearLayout linearLayout2 = new LinearLayout(this);
                LinearLayout linearLayout3 = new LinearLayout(this);

                linearLayout2.setPadding(20,10,20,30);
                linearLayout2.setOrientation(LinearLayout.VERTICAL);

                linearLayout3.setBackgroundColor(Color.WHITE);
                if (i==0)
                    linearLayout3.setPadding(20,50,10,15);
                else
                    linearLayout3.setPadding(20,0,10,15);

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

                linearLayout1.setOnClickListener(new ScoreClassActivity.textListener(textView));
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
            Intent intent = new Intent();
            String className = textView.getText().toString();
            className = className.replace("班级名称：","");
            intent.putExtra("teacherName",teacherName);
            intent.putExtra("className",className);
            intent.setClass(ScoreClassActivity.this,ScoreCourseActivity.class);
            startActivity(intent);
        }
    }

}
