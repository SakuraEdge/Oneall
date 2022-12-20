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

public class ClassSetActivity extends AppCompatActivity {
    JSONArray array = new JSONArray();
    String msg = null;
    String teacherName;
    OkHttpClient client = SSLPass.sslPass();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DBHelper dbHelper = new DBHelper(ClassSetActivity.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        @SuppressLint("Recycle") Cursor cursor = db.rawQuery("select * from login_info",null);
        cursor.moveToFirst();
        teacherName = cursor.getString(cursor.getColumnIndex("name"));

        setContentView(R.layout.class_set);

        LinearLayout LinearLayout = findViewById(R.id.courseList);
        ImageButton imageButton = findViewById(R.id.createClassButton);

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


        imageButton.setOnClickListener(v -> {
            LinearLayout linearLayout1 = new LinearLayout(this);
            linearLayout1.setPadding(20,20,20,20);
            linearLayout1.setOrientation(android.widget.LinearLayout.VERTICAL);

            EditText classname = new EditText(this);
            classname.setHint("请输入课程名称");
            classname.setBackgroundResource(R.drawable.border_black);
            classname.setPadding(20,20,20,20);

            EditText maxNum = new EditText(this);
            maxNum.setHint("请输入班级最大人数");
            maxNum.setBackgroundResource(R.drawable.border_black);
            maxNum.setPadding(20,20,20,20);


            linearLayout1.addView(classname);
            linearLayout1.addView(maxNum);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("创建新班级")
                    .setView(linearLayout1).setNegativeButton("取消",null);
            builder.setPositiveButton("创建", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String name = classname.getText().toString();
                    String num = maxNum.getText().toString();
                    System.out.println(name);
                    System.out.println(num);

                    Map<String,String> map1 = new HashMap<>();
                    map1.put("classname",name);
                    map1.put("teacherName",teacherName);
                    map1.put("maxNum",num);

                    String url1 = "https://223.247.140.116:35152/InsertClass";
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
                    recreate();
                }
            }).show();



        });

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
                textView.setText("当前未有管理的班级，请右上角新建班级");
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

                linearLayout1.setOnClickListener(new ClassSetActivity.textListener(textView));
                linearLayout1.setOnLongClickListener(new ClassSetActivity.textListener2(textView));
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
            intent.setClass(ClassSetActivity.this,ClassTeaListActivity.class);
            startActivity(intent);
        }
    }

    class textListener2 implements View.OnLongClickListener {
        private final TextView textView;

        private textListener2(TextView textView) {
            this.textView = textView;
        }

        @Override
        public boolean onLongClick(View v) {
            TextView textView1 = new TextView(ClassSetActivity.this);
            LinearLayout linearLayout1 = new LinearLayout(ClassSetActivity.this);
            linearLayout1.setPadding(100,60,20,20);
            textView1.setPadding(20,20,20,20);
            textView1.setTextSize(15);
            textView1.setBackgroundResource(R.drawable.border_black);
            textView1.setTextColor(Color.RED);

            linearLayout1.addView(textView1);

            String classname1 = textView.getText().toString();
            classname1 = classname1.replace("班级名称：","");
            String msg = "是否删除班级"+classname1+"？";
            textView1.setText(msg);
            AlertDialog.Builder builder = new AlertDialog.Builder(ClassSetActivity.this);
            builder.setTitle("删除确认")
                    .setView(linearLayout1).setNegativeButton("取消",null);
            String finalClassname = classname1;

            builder.setPositiveButton("确认", (dialog, which) -> {
                Map<String,String> map = new HashMap<>();
                map.put("classname", finalClassname);

                String url1 = "https://223.247.140.116:35152/DeleteClass";
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
