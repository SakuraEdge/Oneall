package com.potato.oneall.ui.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.potato.oneall.ui.course.ClassInfoActivity;
import com.potato.oneall.ui.course.ClassSetActivity;
import com.potato.oneall.ui.param.DBHelper;
import com.potato.oneall.ui.web.WebActivity;
import com.potato.oneall.ui.web.WebActivity3;
import com.potato.timetable.R;

import java.util.Calendar;


public class MenuTeaActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_teacher);
        ImageButton schoolButton = findViewById(R.id.schoolButton);
        //跳转至官网
        ImageButton classInfoButton = findViewById(R.id.classInfoButton);
        //跳转至英汉翻译
        ImageButton scoreInfoButton = findViewById(R.id.scoreInfoButton);
        //跳转至自定义页面
        ImageButton tableButton = findViewById(R.id.tableButton);

        //登出
        ImageButton LoginOutButton = findViewById(R.id.login_out);

        //设置年月日
        TextView getTime = findViewById(R.id.getTime);
        String get_time = get_Time();
        getTime.setText(get_time);


        DBHelper dbHelper = new DBHelper(MenuTeaActivity.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        LoginOutButton.setOnClickListener(v -> {
            db.execSQL("update login_info set name = null");
            db.execSQL("update login_info set pwd = null");
            Toast.makeText(MenuTeaActivity.this,"登出成功!",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.setClass(MenuTeaActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        });

        tableButton.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(MenuTeaActivity.this,MainActivity.class);
            //跳转至课表界面
            startActivity(intent);
        });

        schoolButton.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(MenuTeaActivity.this, WebActivity.class);
            //跳转至官网界面
            startActivity(intent);
        });

        classInfoButton.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(MenuTeaActivity.this, ClassSetActivity.class);
            //跳转至班级管理界面
            startActivity(intent);
        });

        scoreInfoButton.setOnClickListener(v -> {
                Intent intent = new Intent();
                intent.setClass(MenuTeaActivity.this, WebActivity3.class);
                //跳转至DIY界面
                startActivity(intent);
        });
    }


    /**
     * 获取当前时间
     */
    private String get_Time(){
        String times = "";
        Calendar i = Calendar.getInstance();
        times += i.get(Calendar.MONTH)+1+"月";
        times += i.get(Calendar.DAY_OF_MONTH)+"日 | ";
        int week = i.get(Calendar.DAY_OF_WEEK)-1;
        String[] weeks = {"星期日","星期一","星期二","星期三","星期四","星期五","星期六","出错"};
        times += weeks[week];


        return times;
    }

//    /**
//     * 检测是否第一次启动
//     */
//    private void first_load(){
//        DBHelper dbHelper = new DBHelper(Main0Activity.this);
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        @SuppressLint("Recycle") Cursor cursor = db.rawQuery("select * from param",null);
//        cursor.moveToFirst();
//        String first = cursor.getString(cursor.getColumnIndex("firstload"));
//        if (first.equals("1")) {
//            new AlertDialog.Builder(Main0Activity.this)
//                    .setTitle("系统提示:")
//                    .setMessage("你当前为首次使用壹软通，是否前往设置自定义网页内容？")
//                    .setPositiveButton("立即前往", (dialog, which) -> {
//                        Intent intent = new Intent();
//                        intent.setClass(Main0Activity.this, MenuSetting.class);
//                        //跳转至设置
//                        startActivity(intent);
//                    })
//                    .setNegativeButton("稍后设置", (dialog, which) ->
//                            Toast.makeText(Main0Activity.this,"以后可点击左下角进行设置",Toast.LENGTH_SHORT).show()).create().show();
//        }
//        db.execSQL("update param set firstload = '0' where param.id = '1'");
//    }


}
