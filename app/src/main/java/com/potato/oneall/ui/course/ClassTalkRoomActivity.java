package com.potato.oneall.ui.course;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONArray;
import com.potato.oneall.util.SSLPass;
import com.potato.timetable.R;

import org.w3c.dom.Text;

import okhttp3.OkHttpClient;

public class ClassTalkRoomActivity extends AppCompatActivity {
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
        name = gets.getStringExtra("name");
        setContentView(R.layout.talk_room);

        String msg = classname+"聊天室";
        TextView textView = findViewById(R.id.title);
        textView.setText(msg);

        EditText editText = findViewById(R.id.talkMsg);
        ImageButton imageButton = findViewById(R.id.talkBtn);

        imageButton.setOnClickListener(v -> {
            LinearLayout scrollView = findViewById(R.id.msgLine);
            String msg2 = editText.getText().toString();
            editText.setText("");
            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setOrientation(LinearLayout.VERTICAL);

            TextView textView1 = new TextView(this);
            textView1.setText(name);
            textView1.setGravity(Gravity.END);
            textView1.setTextSize(18);

            TextView textView2 = new TextView(this);
            textView2.setText(msg2);
            textView2.setBackgroundResource(R.drawable.border_black);
            textView2.setPadding(10,0,10,0);
            textView2.setTextSize(26);

            linearLayout.addView(textView1);
            linearLayout.addView(textView2);
            linearLayout.setGravity(Gravity.END|Gravity.CENTER);

            ImageView imageView = new ImageView(this);
            imageView.setImageResource(R.mipmap.boqi_1);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                imageView.setForegroundGravity(Gravity.END|Gravity.CENTER);
            }

            LinearLayout linearLayout1 = new LinearLayout(this);
            linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout1.setGravity(Gravity.END|Gravity.CENTER);
            linearLayout1.addView(linearLayout);
            linearLayout1.setPadding(0,0,0,10);
            linearLayout1.addView(imageView);

            scrollView.addView(linearLayout1);


        });
    }
}
