package com.potato.oneall.ui.main;

import android.annotation.SuppressLint;
import android.content.Intent;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.potato.timetable.R;
import com.potato.oneall.ui.param.DBHelper;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.potato.oneall.ui.web.WebActivity;
import com.potato.oneall.ui.web.WebActivity3;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.Calendar;


public class Main0Activity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_main_1);
        ImageButton schoolButton = findViewById(R.id.schoolButton);
        //跳转至官网
        ImageButton engButton = findViewById(R.id.engButton);
        //跳转至英汉翻译
        ImageButton DIYButton = findViewById(R.id.DIYButton);
        //跳转至自定义页面
        ImageButton tableButton = findViewById(R.id.tableButton);
        //跳转至课表
        ImageButton playButton = findViewById(R.id.playButton);
        //休闲按钮
        TextView DIYname = findViewById(R.id.DIYname);
        TextView UserID = findViewById(R.id.userid);
        ImageButton setButton = findViewById(R.id.setButton);

        TextView classpd = findViewById(R.id.class_pd);
        ImageButton refresh_button = findViewById(R.id.refresh_button);

        //设置年月日
        TextView getTime = findViewById(R.id.getTime);
        String get_time = get_Time();
        getTime.setText(get_time);


        DBHelper dbHelper = new DBHelper(Main0Activity.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery("select * from param",null);
        cursor.moveToFirst();
        String diyname = cursor.getString(cursor.getColumnIndex("name"));
        DIYname.setText(diyname);
        String url = cursor.getString(cursor.getColumnIndex("http"));
        @SuppressLint("Recycle") Cursor cursor2 = db.rawQuery("select * from user",null);
        cursor2.moveToFirst();
        String userid = cursor2.getString(cursor2.getColumnIndex("user")) + "：";
        UserID.setText(userid);

        refresh_button.setOnClickListener(v -> {
            String[] msg_class = {"今天的课程结束了！\n <(￣︶￣)/"
                    ,"今天的课程结束了！\n o(≧口≦)o"
                    ,"今天的课程结束了！\n (=^_^=)"
                    ,"今天的课程结束了！\n XD"
                    ,"今天的课程结束了！\n (#￣▽￣#)"
                    ,"今天的课程结束了！\n <(@￣︶￣@)>"
                    ,"今天的课程结束了！\n ╮(￣▽￣)╭"};

            int num = (int) (Math.random() * msg_class.length-1 + 0);

            classpd.setText(msg_class[num]);

        });

        tableButton.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(Main0Activity.this,MainActivity.class);
            //跳转至课表界面
            startActivity(intent);
        });

        schoolButton.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(Main0Activity.this, WebActivity.class);
            //跳转至官网界面
            startActivity(intent);
        });

        engButton.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(Main0Activity.this, Eng_Chs.class);
            //跳转至英翻界面
            startActivity(intent);
        });

        DIYButton.setOnClickListener(v -> {
                Intent intent = new Intent();
                intent.setClass(Main0Activity.this, WebActivity3.class);
                //跳转至DIY界面
                startActivity(intent);
        });

        playButton.setOnClickListener(v -> {
            String[] msg = {"劳逸结合是不错，但也别放松过头 "
                    ,"耽误太多时间，事情就做不完了 o(≧口≦)o"
                    ,"无论是冒险还是做生意，机会都稍纵即逝"
                    ,"劲腰娜舞————！！"
                    ,"交给我，什么都可以交给我"
                    ,"进不去！怎么想都进不去吧"
                    ,"工作还没做完，真的能提前休息吗"};
            //“经典语录”

            int num = (int) (Math.random() * msg.length-1 + 0);
            //随机数
            Toast.makeText(Main0Activity.this,msg[num],Toast.LENGTH_SHORT).show();
        });

        setButton.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(Main0Activity.this, MenuSetting.class);
            //跳转至设置
            startActivity(intent);
        });

        first_load();
    }


    /**
     * 获取当前时间
     */
    private String get_Time(){
        String times = "";
        Calendar i = Calendar.getInstance();
        times += i.get(Calendar.YEAR)+"年";
        times += i.get(Calendar.MONTH)+1+"月";
        times += i.get(Calendar.DAY_OF_MONTH)+"日\t\t";
        int week = i.get(Calendar.DAY_OF_WEEK)-1;
        String[] weeks = {"星期日","星期一","星期二","星期三","星期四","星期五","星期六","出错"};
        times += weeks[week];


        return times;
    }

    /**
     * 检测是否第一次启动
     */
    private void first_load(){
        DBHelper dbHelper = new DBHelper(Main0Activity.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery("select * from param",null);
        cursor.moveToFirst();
        String first = cursor.getString(cursor.getColumnIndex("firstload"));
        if (first.equals("1")) {
            new AlertDialog.Builder(Main0Activity.this)
                    .setTitle("系统提示:")
                    .setMessage("你当前为首次使用壹软通，是否前往设置自定义网页内容？")
                    .setPositiveButton("立即前往", (dialog, which) -> {
                        Intent intent = new Intent();
                        intent.setClass(Main0Activity.this, MenuSetting.class);
                        //跳转至设置
                        startActivity(intent);
                    })
                    .setNegativeButton("稍后设置", (dialog, which) ->
                            Toast.makeText(Main0Activity.this,"以后可点击左下角进行设置",Toast.LENGTH_SHORT).show()).create().show();
        }
        db.execSQL("update param set firstload = '0' where param.id = '1'");
    }


}
