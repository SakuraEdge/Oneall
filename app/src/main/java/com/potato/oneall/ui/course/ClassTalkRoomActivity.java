package com.potato.oneall.ui.course;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.potato.oneall.util.SSLPass;
import com.potato.timetable.R;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

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

public class ClassTalkRoomActivity extends AppCompatActivity {
    JSONArray array = new JSONArray();
    String name;
    String classname;
    String id;
    String msg;
    OkHttpClient client = SSLPass.sslPass();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent gets = getIntent();
        classname = gets.getStringExtra("className");
        name = gets.getStringExtra("name");
        setContentView(R.layout.talk_room);

        String msg = classname+"聊天室";
        TextView textView = findViewById(R.id.title);
        textView.setText(msg);

        EditText editText = findViewById(R.id.talkMsg);
        ImageButton imageButton = findViewById(R.id.talkBtn);

        String url="https://223.247.140.116:35152/SelectChat";
        LinearLayout scrollView = findViewById(R.id.msgLine);
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

        initData(scrollView);


        imageButton.setOnClickListener(v -> {
            String msg2 = editText.getText().toString();
            editText.setText("");
            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setOrientation(LinearLayout.VERTICAL);

            TextView textView1 = new TextView(this);
            textView1.setText(name);
            textView1.setGravity(Gravity.END);
            textView1.setTextSize(15);

            TextView textView2 = new TextView(this);
            textView2.setText(msg2);
            textView2.setTextColor(Color.BLACK);
            textView2.setBackgroundResource(R.mipmap.talk_me);
            textView2.setPadding(20,20,50,20);
            textView2.setTextSize(20);

            linearLayout.addView(textView1);
            linearLayout.addView(textView2);
            linearLayout.setGravity(Gravity.END|Gravity.CENTER);

            ImageView imageView = new ImageView(this);
            imageView.setImageResource(R.mipmap.boqi_1);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                imageView.setForegroundGravity(Gravity.END);
            }

            LinearLayout linearLayout1 = new LinearLayout(this);
            linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout1.setGravity(Gravity.END|Gravity.CENTER);
            linearLayout1.addView(linearLayout);
            linearLayout1.setPadding(0,0,20,30);
            linearLayout1.addView(imageView);

            scrollView.addView(linearLayout1);

            String url1="https://223.247.140.116:35152/InsertChat";
            Map<String,String> map1 = new HashMap<>();
            map1.put("classname",classname);
            map1.put("userName",name);
            map1.put("chatTxt",msg2);

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


        });
    }

    private void initData(LinearLayout scrollView){
        new Handler().postDelayed(() -> {
            for(int i=0; i<array.size(); i++){

                String str = JSON.toJSONString(array.get(i));
                JSONObject maps = JSON.parseObject(str);

                String names = maps.getString("userName");

                if (Objects.equals(names, name)){
                    String msg2 = (String) maps.get("chatTxt");
                    LinearLayout linearLayout = new LinearLayout(this);
                    linearLayout.setOrientation(LinearLayout.VERTICAL);

                    TextView textView1 = new TextView(this);
                    textView1.setText(names);
                    textView1.setGravity(Gravity.END);
                    textView1.setTextSize(15);

                    TextView textView2 = new TextView(this);
                    textView2.setText(msg2);
                    textView2.setTextColor(Color.BLACK);
                    textView2.setBackgroundResource(R.mipmap.talk_me);
                    textView2.setPadding(20,20,50,20);
                    textView2.setTextSize(20);

                    linearLayout.addView(textView1);
                    linearLayout.addView(textView2);
                    linearLayout.setGravity(Gravity.END|Gravity.CENTER);

                    ImageView imageView = new ImageView(this);
                    imageView.setImageResource(R.mipmap.boqi_1);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        imageView.setForegroundGravity(Gravity.END);
                    }

                    LinearLayout linearLayout1 = new LinearLayout(this);
                    linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
                    linearLayout1.setGravity(Gravity.END|Gravity.CENTER);
                    linearLayout1.addView(linearLayout);
                    linearLayout1.setPadding(0,0,20,30);
                    linearLayout1.addView(imageView);

                    scrollView.addView(linearLayout1);
                }

                else{
                    String msg2 = (String) maps.get("chatTxt");
                    LinearLayout linearLayout = new LinearLayout(this);
                    linearLayout.setOrientation(LinearLayout.VERTICAL);

                    TextView textView1 = new TextView(this);
                    textView1.setText(names);
                    textView1.setGravity(Gravity.START);
                    textView1.setTextSize(15);

                    TextView textView2 = new TextView(this);
                    textView2.setText(msg2);
                    textView2.setTextColor(Color.BLACK);
                    textView2.setBackgroundResource(R.mipmap.talk_other);
                    textView2.setPadding(50,20,20,20);
                    textView2.setTextSize(20);

                    linearLayout.addView(textView1);
                    linearLayout.addView(textView2);
                    linearLayout.setGravity(Gravity.START);

                    ImageView imageView = new ImageView(this);
                    imageView.setImageResource(R.mipmap.boqi_2);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        imageView.setForegroundGravity(Gravity.START|Gravity.CENTER);
                    }

                    LinearLayout linearLayout1 = new LinearLayout(this);
                    linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
                    linearLayout1.setGravity(Gravity.START|Gravity.CENTER);
                    linearLayout1.addView(imageView);
                    linearLayout1.addView(linearLayout);
                    linearLayout1.setPadding(20,0,0,30);

                    scrollView.addView(linearLayout1);
                }

            }
        },800);
    }
}
