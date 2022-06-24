package com.potato.oneall.ui.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.text.InputFilter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.potato.oneall.ui.param.DBHelper;

import com.potato.timetable.R;

public class MenuSetting extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_settings);
        EditText web_name = findViewById(R.id.diy_web_name);
        EditText web_http = findViewById(R.id.diy_http);
        Button button1 = findViewById(R.id.save_button);
        Button button2 = findViewById(R.id.userid);

        //数据库
        DBHelper dbHelper = new DBHelper(MenuSetting.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery("select * from param",null);
        cursor.moveToFirst();
        String name = cursor.getString(cursor.getColumnIndex("name"));
        String http = cursor.getString(cursor.getColumnIndex("http"));
        web_name.setText(name);
        web_http.setText(http);



        button1.setOnClickListener(v -> new AlertDialog.Builder(MenuSetting.this)
                .setTitle("请进行确认")
                .setMessage("是否进行保存\n（保存后以往的链接将会消失）")
                .setPositiveButton("确定",(dialog, which) ->{
                    db.execSQL("update param set name = ? where param.id = '1'",new String[]{web_name.getText()+""});
                    db.execSQL("update param set http = ? where param.id = '1'",new String[]{web_http.getText()+""});
                    Toast.makeText(MenuSetting.this,"保存成功，将返回主页面...",Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(() -> {
                        //执行在主线程
                        //启动主页面
                        startActivity(new Intent(MenuSetting.this,Main0Activity.class));

                        //关闭当前页面
                        finish();
                    },2000);
                })
                .setNegativeButton("取消", (dialog, which) -> {
                        }
                ).create().show()
        );

        button2.setOnClickListener(v -> {
            final EditText inputUser = new EditText(this);
            inputUser.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
            inputUser.setHint("点此输入用户名");
            inputUser.setGravity(1);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("用户名设置")
                    .setMessage("设置一个昵称吧~")
                    .setView(inputUser)
                    .setPositiveButton("确认",((dialog, which) -> {
                        String _user = inputUser.getText().toString();
                        if(!_user.isEmpty())
                        {
                            db.execSQL("update user set user = ? where user.id = '1'",new String[]{inputUser.getText()+""});
                            Toast.makeText(MenuSetting.this,"保存成功，将返回主页面...",Toast.LENGTH_SHORT).show();
                            new Handler().postDelayed(() -> {
                                //执行在主线程
                                //启动主页面
                                startActivity(new Intent(MenuSetting.this,Main0Activity.class));

                                //关闭当前页面
                                finish();
                            },2000);
                        }
                        else
                        {
                            Toast.makeText(MenuSetting.this,"保存失败，用户名为空或无效！",Toast.LENGTH_SHORT).show();
                        }

                    })).create().show();


        });

    }
}
